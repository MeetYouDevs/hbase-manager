<!DOCTYPE html>
<html>
<head>
  	<#import "common/common.macro.ftl" as comm>
    <@comm.commonStyle />
    <title>HBase Manager-集群迁移</title>
</head>
<div class="wrapper">
    <!-- header -->
	<@comm.commonHeader />
	<div class="container-fluid" role="main">
		<ol class="breadcrumb">
			<li><a href="/cluster">Clusters</a></li>
			<li><a href="/cluster/${clusterName}">${clusterName}</a></li>
			<li>Tables</li>
		</ol>
	</div>
	<div class="container-fluid" role="main">
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3>Tables</h3>
					<a href="/cluster/${clusterName}/table/add" id="table-add-btn" class="btn btn-default btn-sm">
						<span class="glyphicon glyphicon-plus"></span> Add
					</a>
				</div>
			</div>
		</div>
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<table id="tb_table"></table>
				</div>
			</div>
		</div>
		<div class="modal fade" id="enable-table-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<input type="hidden" id="table-to-enable"/>
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">开启表</h4>
					</div>
					<div class="modal-body">确认开启该表?</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" id="enable-table-modal-confirm-btn" class="btn btn-success ops-button">确认</button>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" id="disable-table-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<input type="hidden" id="table-to-disable"/>
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">关闭表</h4>
					</div>
					<div class="modal-body">确认关闭该表?</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" id="disable-table-modal-confirm-btn" class="btn btn-warning ops-button">确认</button>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" id="delete-table-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<input type="hidden" id="table-to-delete"/>
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">删除表</h4>
					</div>
					<div class="modal-body">确认删除该表?</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" id="delete-table-modal-confirm-btn" class="btn btn-danger ops-button">确认</button>
					</div>
				</div>
			</div>
		</div>
    </div>

    <hr>
    <!-- footer -->
	<@comm.commonFooter />
</div>

</body>
<@comm.commonScript />


<script type="text/javascript">
$(function(){
	$("#tb_table").bootstrapTable({
		data : ${tableList},
		idField : 'tableName',	//id字段
		striped : false,		//表格显示条纹
		pagination : true,  	//是否分页
	    pageSize : 10,      	//单页记录数
	    pageNumber : 1,
		pageList : "[10, 25, 50, 100, All]",
		search : true, 			//显示搜索框
		columns : [
			{
				field : 'namespace', // 返回json数据中的name
				title : '命名空间', // 表格表头显示文字
				align : 'center', // 左右居中
				valign : 'middle',
				sortable : true,
			}, {
				field : 'tableName',
				title : '表名',
				align : 'center',
				valign : 'middle',
				sortable : true,
				class : 'sorting',
				width : 100,
				formatter : function(value, row, index) {
					text = "<a href='/cluster/${clusterName}/table/"+value+"/edit'>"+value+"</a>";
					return text;
				}
			}, {
				field : '',
				title : '操作',
				align : 'center',
				valign : 'middle',
				sortable : true,
				class : 'sorting',
				formatter : function(value, row, index) {
					text = "<button type='button' value='"+row.tableName+"' class='btn btn-success ops-button enable-table-btn'>Enable</button>&nbsp" +
						"<button type='button' value='"+row.tableName+"' class='btn btn-warning ops-button disable-table-btn'>Disable</button>&nbsp" +
						"<button type='button' value='"+row.tableName+"' class='btn btn-danger ops-button delete-table-btn'>Delete</button>";
					return text;
				}
			}, {
				field : 'desc',
				title : '列族',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'spaceSize',
				title : '表大小(MB)',
				align : 'center',
				valign : 'middle',
				sortable : true,
				formatter : function(value, row, index) {
					text = String((parseFloat(value)/(1024*1024)).toFixed(2));
					return text;
				}
			}
		],
		formatLoadingMessage: function(){
			return "正在统计表信息, 请稍等...";
		}
	});
	//隐藏加载
	$("#tb_table").bootstrapTable('hideLoading');
	
	// 开启表
	$("#tb_table").delegate('.enable-table-btn', 'click', function () {
		$("#table-to-enable").val($(this).val());
		$('#enable-table-modal').modal('show');
    });
	$("#enable-table-modal-confirm-btn").click(function(){
		var tableName = $("#table-to-enable").val();
  		$.ajaxSettings.async = false;
  		$.post("/cluster/${clusterName}/table/"+tableName+"/enable", {"clusterName": "${clusterName}", "tableName": tableName}, function(data)
  		{
  			$('#enable-table-modal').modal('hide');
  			if(data.code = 200){
                alert(data.data);
                window.location.reload();
            }else{
                alert(data.message);
            }
  		});
  		$.ajaxSettings.async = true;
  	});
	
	// 关闭表	
	$("#tb_table").delegate('.disable-table-btn', 'click', function () {
		$("#table-to-disable").val($(this).val());
		$('#disable-table-modal').modal('show');
    });
	$("#disable-table-modal-confirm-btn").click(function(){
		var tableName = $("#table-to-disable").val();
  		$.ajaxSettings.async = false;
  		$.post("/cluster/${clusterName}/table/"+tableName+"/disable", {"clusterName": "${clusterName}", "tableName": tableName}, function(data)
  		{
  			$('#disable-table-modal').modal('hide');
  			if(data.code = 200){
                alert(data.data);
                window.location.reload();
            }else{
            	alert(data.message);
            }
  		});
  		$.ajaxSettings.async = true;
  	});
	
	// 删除表
	$("#tb_table").delegate('.delete-table-btn', 'click', function () {
		$("#table-to-delete").val($(this).val());
		$('#delete-table-modal').modal('show');
    });
	$("#delete-table-modal-confirm-btn").click(function(){
		var tableName = $("#table-to-delete").val();
  		$.ajaxSettings.async = false;
  		$.post("/cluster/${clusterName}/table/"+tableName+"/delete", {"clusterName": "${clusterName}", "tableName": tableName}, function(data)
  		{
  			$('#delete-table-modal').modal('hide');
  			if(data.code = 200){
                alert(data.data);
                window.location.reload();
            }else{
            	alert(data.message);
            }
  		});
  		$.ajaxSettings.async = true;
  	});
	
});
</script>
</html>
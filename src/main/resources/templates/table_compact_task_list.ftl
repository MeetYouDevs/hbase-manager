<!DOCTYPE html>
<html>
<head>
  	<#import "common/common.macro.ftl" as comm>
    <@comm.commonStyle />
    <title>HBase Manager</title>
</head>
<div class="wrapper">
    <!-- header -->
	<@comm.commonHeader />
	<div class="container-fluid" role="main">
		<ol class="breadcrumb">
			<li><a href="/cluster">Clusters</a></li>
			<li><a href="/cluster/${clusterName}">${clusterName}</a></li>
			<li>Compact Tasks</li>
		</ol>
	</div>
	<div class="col-md-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3>Compact Tasks</h3>
				<a href="#" id="add-compact-task-btn" class="btn btn-default btn-sm">
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
	<div class="modal fade" id="delete-compact-task-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<input type="hidden" id="compact-task-to-delete"/>
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="myModalLabel">删除表合并任务</h4>
	            </div>
	            <div class="modal-body">确认删除该表合并任务?</div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	                <button type="button" id="delete-compact-task-modal-confirm-btn" class="btn btn-danger ops-button">确认</button>
	            </div>
	        </div>
	    </div>
	</div>
	<div class="modal fade" id="add-compact-task-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="myModalLabel">表合并任务</h4>
	            </div>
	            <div class="modal-body">
	            	<form id="compact-task-form" class="form-horizontal" role="form" action="#">
						<input name="id" id="compact-id" hidden="true">
			            <div class="form-group">
			                <label for="tableName" class="col-sm-2 control-label">表名</label>
			                <div class="col-sm-9">
			                    <select class="form-control selectpicker show-tick" data-live-search="true" id="compact-table-name-select" name="tableName"></select>
			                </div>
			            </div>
			            <div class="form-group">
							<label class="control-label col-md-2">调度间隔</label>
							<div class="col-md-3">
								<select name="intervalPosition" id="compact-schedule-interval-position" class="form-control">
									<option value="0">秒</option>
									<option value="1">分钟</option>
									<option value="2">小时</option>
									<option value="3">日</option>
									<option value="4">月</option>
								</select>
							</div>
							<div class="col-md-6">
								<input type="text" name="interval" id="compact-schedule-interval" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-2">时间偏移</label>
							<div class="col-md-3">
								<select name="offsetPosition" id="compact-schedule-offset-position" class="form-control">
									<option value="0">秒</option>
									<option value="1">分钟</option>
									<option value="2">小时</option>
									<option value="3">日</option>
									<option value="4">月</option>
								</select>
							</div>
							<div class="col-md-6">
								<input type="text" name="offset" id="compact-schedule-offset" class="form-control">
							</div>
						</div>
			        </form>
			   	</div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	                <button type="button" id="add-compact-task-modal-confirm-btn" class="btn btn-primary ops-button">确认</button>
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
		data : ${compactTaskList},
		striped : false,		//表格显示条纹
		pagination : true,  	//是否分页
	    pageSize : 10,      	//单页记录数
	    pageNumber : 1,
		pageList : "[10, 25, 50, 100, All]",
		search : true, 			//显示搜索框
		uniqueId: "id",
		columns : [
			{
				field : 'tableName',
				title : '表名',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'cron',
				title : '合并周期',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : '',
				title : '操作',
				align : 'center',
				valign : 'middle',
				sortable : true,
				formatter : function(value, row, index) {
					text = "<button type='button' value='"+row.id+"' class='btn btn-default ops-button modify-compact-task-btn'>Modify</button>&nbsp" + 
						"<button type='button' value='"+row.id+"' class='btn btn-danger ops-button delete-compact-task-btn'>Delete</button>";
					return text;
				}
			}
		]
	});
	//隐藏加载
	$("#tb_table").bootstrapTable('hideLoading');
	
	// 增加合并任务
	$("#add-compact-task-btn").click(function () {
		$.ajaxSettings.async = false;
	    $.post("/cluster/${clusterName}/table/name/list", function(data)
	    {
	    	var jsonObj = JSON.parse(data);
	    	var html = "<option value=''>请选择需要合并的表</option>";
	    	$.each(jsonObj, function(n, value){
	    		html += "<option value= '"+value+"'>"+value+"</option>";
           	});
	    	$("#compact-table-name-select").attr("disabled", false);
	    	$("#compact-table-name-select").find("option:selected").remove();
	    	$("#compact-schedule-interval-position").find("option:selected").attr("selected",false);
	        $("#compact-schedule-interval").val("");
	        $("#compact-schedule-offset-position").find("option:selected").attr("selected",false);
	        $("#compact-schedule-offset").val("");
            $("#compact-table-name-select").append(html);
            $("#compact-table-name-select").selectpicker("refresh");
	    });
	    $.ajaxSettings.async = true;
		$('#add-compact-task-modal').modal('show');
    });
	
	$("#add-compact-task-modal-confirm-btn").click(function(){
		// 获取校验器
	    var bootstrapValidator = $('#compact-task-form').data('bootstrapValidator');
	    // 手动触发验证
	    bootstrapValidator.validate();
	    // 检验通过
	    if(bootstrapValidator.isValid()){
	    	var id = $("#compact-id").val();
	    	var tableName = $("#compact-table-name-select").val();
	    	var intervalPosition = $("#compact-schedule-interval-position").val();
			var interval = $("#compact-schedule-interval").val();
			var offsetPosition = $("#compact-schedule-offset-position").val();
			var offset = $("#compact-schedule-offset").val();
	  		$.ajaxSettings.async = false;
	  		$.post(
	  			"/cluster/${clusterName}/table/compact/task/add", 
	  			{
	  				"id": id,
	  				"clusterName": "${clusterName}",
	  				"tableName": tableName,
	  				"intervalPosition": intervalPosition,
	  				"interval": interval,
	  				"offsetPosition": offsetPosition,
	  				"offset": offset
	  			}, function(data) {
	  				$('#add-compact-task-modal').modal('hide');
	  				window.location.reload();
	  			}
	  		);
	  		$.ajaxSettings.async = true;
	    }
  	});
	
	// 合并表单参数验证
	$('#compact-task-form').bootstrapValidator({
		live: 'submitted',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
        fields: {
        	tableName: {
                validators: {
                    notEmpty: {
                        message: '表不能为空'
                    }
                }
            }, 
            interval: {
                validators: {
                    notEmpty: {
                        message: '调度间隔不能为空'
                    }
                }
            },
            offset: {
                validators: {
                    notEmpty: {
                        message: '时间偏移不能为空'
                    }
                }
            }
        }
    });
	
	// 修改合并任务
	$("#tb_table").delegate('.modify-compact-task-btn', 'click', function () {
		var id = $(this).val();
		$.ajaxSettings.async = false;
	    $.post("/cluster/${clusterName}/table/compact/task/edit", {"id": id}, function(data)
	    {
	    	var jsonObj = JSON.parse(data);
	    	var html = "<option selected='selected' value= '"+jsonObj.tableName+"'>"+jsonObj.tableName+"</option>";
	    	$("#compact-id").val(jsonObj.id);
	        $("#compact-table-name-select").append(html);
	        $("#compact-table-name-select").attr("disabled", true);
	        $("#compact-table-name-select").selectpicker("refresh");
	        $("#compact-schedule-interval-position").find("option[value='"+jsonObj.intervalPosition+"']").attr("selected",true);
	        $("#compact-schedule-interval").val(jsonObj.interval);
	        $("#compact-schedule-offset-position").find("option[value='"+jsonObj.offsetPosition+"']").attr("selected",true);
	        $("#compact-schedule-offset").val(jsonObj.offset);
	    });
	    $.ajaxSettings.async = true;
		$('#add-compact-task-modal').modal('show');
    });
	
	// 删除合并任务
	$("#tb_table").delegate('.delete-compact-task-btn', 'click', function () {
		$("#compact-task-to-delete").val($(this).val());
		$('#delete-compact-task-modal').modal('show');
    });
	$("#delete-compact-task-modal-confirm-btn").click(function(){
		var id = $("#compact-task-to-delete").val();
  		$.ajaxSettings.async = false;
  		$.post("/cluster/${clusterName}/table/compact/task/delete", {"id": id}, function(data)
  		{
  			$('#delete-compact-task-modal').modal('hide');
  			window.location.reload();
  		});
  		$.ajaxSettings.async = true;
  	});
	
});
</script>
</html>
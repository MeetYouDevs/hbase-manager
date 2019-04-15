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
			<li>Stat Tasks</li>
		</ol>
	</div>
	<div class="col-md-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3>Stat Tasks</h3>
				<a href="#" id="add-stat-task-btn" class="btn btn-default btn-sm">
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
	<div class="modal fade" id="delete-stat-task-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<input type="hidden" id="stat-task-to-delete"/>
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="myModalLabel">删除表统计任务</h4>
	            </div>
	            <div class="modal-body">确认删除该表统计任务?</div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	                <button type="button" id="delete-stat-task-modal-confirm-btn" class="btn btn-danger ops-button">确认</button>
	            </div>
	        </div>
	    </div>
	</div>
	<div class="modal fade" id="execute-stat-task-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<input type="hidden" id="stat-task-to-execute"/>
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="myModalLabel">执行统计任务</h4>
	            </div>
	            <div class="modal-body">确认立即执行该表统计任务?</div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	                <button type="button" id="execute-stat-task-modal-confirm-btn" class="btn btn-primary ops-button">确认</button>
	            </div>
	        </div>
	    </div>
	</div>
	<div class="modal fade" id="add-stat-task-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="myModalLabel">表统计任务</h4>
	            </div>
	            <div class="modal-body">
	            	<form id="stat-task-form" class="form-horizontal" role="form" action="#">
						<input name="id" id="stat-id" hidden="true">
			            <div class="form-group">
			                <label for="tableName" class="col-sm-2 control-label">表名</label>
			                <div class="col-sm-9">
			                    <select class="form-control selectpicker show-tick" data-live-search="true" id="stat-table-name-select" name="tableName"></select>
			                </div>
			            </div>
			            <div class="form-group">
							<label class="control-label col-md-2">调度间隔</label>
							<div class="col-md-3">
								<select name="intervalPosition" id="stat-schedule-interval-position" class="form-control">
									<option value="0">秒</option>
									<option value="1">分钟</option>
									<option value="2">小时</option>
									<option value="3">日</option>
									<option value="4">月</option>
								</select>
							</div>
							<div class="col-md-6">
								<input type="text" name="interval" id="stat-schedule-interval" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-2">时间偏移</label>
							<div class="col-md-3">
								<select name="offsetPosition" id="stat-schedule-offset-position" class="form-control">
									<option value="0">秒</option>
									<option value="1">分钟</option>
									<option value="2">小时</option>
									<option value="3">日</option>
									<option value="4">月</option>
								</select>
							</div>
							<div class="col-md-6">
								<input type="text" name="offset" id="stat-schedule-offset" class="form-control">
							</div>
						</div>
			        </form>
			   	</div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	                <button type="button" id="add-stat-task-modal-confirm-btn" class="btn btn-primary ops-button">确认</button>
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
		data : ${statTaskList},
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
				title : '统计周期',
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
					text = "<button type='button' value='"+row.id+"' class='btn btn-primary ops-button execute-stat-task-btn'>Execute</button>&nbsp" + 
						"<button type='button' value='"+row.id+"' class='btn btn-default ops-button modify-stat-task-btn'>Modify</button>&nbsp" + 
						"<button type='button' value='"+row.id+"' class='btn btn-danger ops-button delete-stat-task-btn'>Delete</button>";
					return text;
				}
			}
		]
	});
	//隐藏加载
	$("#tb_table").bootstrapTable('hideLoading');
	
	// 增加统计任务
	$("#add-stat-task-btn").click(function () {
		$.ajaxSettings.async = false;
	    $.post("/cluster/${clusterName}/table/name/list", function(data)
	    {
	    	var jsonObj = JSON.parse(data);
	    	var html = "<option value=''>请选择需要统计的表</option>";
	    	$.each(jsonObj, function(n, value){
	    		html += "<option value= '"+value+"'>"+value+"</option>";
           	});
	    	$("#stat-table-name-select").attr("disabled", false);
	    	$("#stat-table-name-select").find("option:selected").remove();
	    	$("#stat-schedule-interval-position").find("option:selected").attr("selected",false);
	        $("#stat-schedule-interval").val("");
	        $("#stat-schedule-offset-position").find("option:selected").attr("selected",false);
	        $("#stat-schedule-offset").val("");
            $("#stat-table-name-select").append(html);
            $("#stat-table-name-select").selectpicker("refresh");
	    });
	    $.ajaxSettings.async = true;
		$('#add-stat-task-modal').modal('show');
    });
	
	$("#add-stat-task-modal-confirm-btn").click(function(){
		// 获取校验器
	    var bootstrapValidator = $('#stat-task-form').data('bootstrapValidator');
	    // 手动触发验证
	    bootstrapValidator.validate();
	    // 检验通过
	    if(bootstrapValidator.isValid()){
	    	var id = $("#stat-id").val();
	    	var tableName = $("#stat-table-name-select").val();
	    	var intervalPosition = $("#stat-schedule-interval-position").val();
			var interval = $("#stat-schedule-interval").val();
			var offsetPosition = $("#stat-schedule-offset-position").val();
			var offset = $("#stat-schedule-offset").val();
	  		$.ajaxSettings.async = false;
	  		$.post(
	  			"/cluster/${clusterName}/table/stat/task/add", 
	  			{
	  				"id": id,
	  				"clusterName": "${clusterName}",
	  				"tableName": tableName,
	  				"intervalPosition": intervalPosition,
	  				"interval": interval,
	  				"offsetPosition": offsetPosition,
	  				"offset": offset
	  			}, function(data) {
	  				$('#add-stat-task-modal').modal('hide');
	  				window.location.reload();
	  			}
	  		);
	  		$.ajaxSettings.async = true;
	    }
  	});
	
	// 统计表单参数验证
	$('#stat-task-form').bootstrapValidator({
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
	
	// 执行统计任务
	$("#tb_table").delegate('.execute-stat-task-btn', 'click', function () {
		$("#stat-task-to-execute").val($(this).val());
		$('#execute-stat-task-modal').modal('show');
    });
	$("#execute-stat-task-modal-confirm-btn").click(function(){
		var tableName = $("#stat-task-to-execute").val();
  		$.ajax({
  	        url: "/cluster/${clusterName}/table/stat/task/execute",
  	      	type: "POST",
  	      	async: true,
  	        dataType: "json",
  	        data: {"clusterName": "${clusterName}", "tableName": tableName}
  	    });
  		alert("Execute stat task...");
  		$('#execute-stat-task-modal').modal('hide');
		window.location.reload();
  	});
	
	// 修改统计任务
	$("#tb_table").delegate('.modify-stat-task-btn', 'click', function () {
		var id = $(this).val();
		$.ajaxSettings.async = false;
	    $.post("/cluster/${clusterName}/table/stat/task/edit", {"id": id}, function(data)
	    {
	    	var jsonObj = JSON.parse(data);
	    	var html = "<option selected='selected' value= '"+jsonObj.tableName+"'>"+jsonObj.tableName+"</option>";
	    	$("#stat-id").val(jsonObj.id);
	        $("#stat-table-name-select").append(html);
	        $("#stat-table-name-select").attr("disabled", true);
	        $("#stat-table-name-select").selectpicker("refresh");
	        $("#stat-schedule-interval-position").find("option[value='"+jsonObj.intervalPosition+"']").attr("selected",true);
	        $("#stat-schedule-interval").val(jsonObj.interval);
	        $("#stat-schedule-offset-position").find("option[value='"+jsonObj.offsetPosition+"']").attr("selected",true);
	        $("#stat-schedule-offset").val(jsonObj.offset);
	    });
	    $.ajaxSettings.async = true;
		$('#add-stat-task-modal').modal('show');
    });
	
	// 删除统计任务
	$("#tb_table").delegate('.delete-stat-task-btn', 'click', function () {
		$("#stat-task-to-delete").val($(this).val());
		$('#delete-stat-task-modal').modal('show');
    });
	$("#delete-stat-task-modal-confirm-btn").click(function(){
		var id = $("#stat-task-to-delete").val();
  		$.ajaxSettings.async = false;
  		$.post("/cluster/${clusterName}/table/stat/task/delete", {"id": id}, function(data)
  		{
  			$('#delete-stat-task-modal').modal('hide');
  			window.location.reload();
  		});
  		$.ajaxSettings.async = true;
  	});
	
});
</script>
</html>
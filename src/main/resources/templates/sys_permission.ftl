<!DOCTYPE html>
<html>
<head>
<#import "common/common.macro.ftl" as comm> <@comm.commonStyle
/>
<title>HBase Manager</title>
</head>
<div class="wrapper">
	<!-- header -->
	<@comm.commonHeader />
	<div class="col-md-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3>系统权限</h3>
				<a href="#" id="add-sys-permission-btn" class="btn btn-default btn-sm">
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
	<div class="modal fade" id="delete-sys-permission-modal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<input type="hidden" id="sys-permission-to-delete" />
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">删除系统权限</h4>
				</div>
				<div class="modal-body">确认删除该系统权限?</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" id="delete-sys-permission-modal-confirm-btn"
						class="btn btn-danger ops-button">确认</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="add-sys-permission-modal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">添加监控邮件</h4>
				</div>
				<div class="modal-body">
					<form id="sys-permission-form" class="form-horizontal" role="form"
						action="#">
						<input name="id" id="sys-permission-id" hidden="true">
						<div class="form-group">
							<label for="permissionName" class="col-sm-2 control-label">权限名</label>
							<div class="col-sm-9">
								<input type="text" name="permissionName" id="sys-permission-name-input"
									class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="resName" class="col-sm-2 control-label">资源名称</label>
							<div class="col-sm-9">
								<input type="text" name="resName" id="sys-permission-resName-input"
									class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="url" class="col-sm-2 control-label">url</label>
							<div class="col-sm-9">
								<input type="text" name="url" id="sys-permission-url-input"
									class="form-control">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" id="add-sys-permission-modal-confirm-btn"
						class="btn btn-primary ops-button">确认</button>
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
		data : ${sysPermissionList},
		striped : false,		//表格显示条纹
		pagination : true,  	//是否分页
	    pageSize : 10,      	//单页记录数
	    pageNumber : 1,
		pageList : "[10, 25, 50, 100, All]",
		search : true, 			//显示搜索框
		uniqueId: "id",
		columns : [
			{
				field : 'permissionName',
				title : '权限名',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'resName',
				title : '资源名称',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'url',
				title : 'url (shiro url表达式)',
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
					text = "<button type='button' value='"+row.id+"' class='btn btn-default ops-button modify-sys-permission-btn'>Modify</button>&nbsp" + 
						"<button type='button' value='"+row.id+"' class='btn btn-danger ops-button delete-sys-permission-btn'>Delete</button>";
					return text;
				}
			}
		]
	});
	//隐藏加载
	$("#tb_table").bootstrapTable('hideLoading');
	
	// 增加系统权限
	$("#add-sys-permission-btn").click(function () {
		$("#sys-permission-id").val("");
		$("#sys-permission-name-input").val("");
		$("#sys-permission-name-input").removeAttr('readonly');
		$("#sys-permission-resName-input").val("");
		$("#sys-permission-url-input").val("");
		$('#add-sys-permission-modal').modal('show');
    });
	
	$("#add-sys-permission-modal-confirm-btn").click(function(){
		// 获取校验器
	    var bootstrapValidator = $('#sys-permission-form').data('bootstrapValidator');
	    // 手动触发验证
	    bootstrapValidator.validate();
	    // 检验通过
	    if(bootstrapValidator.isValid()){
	    	var id = $("#sys-permission-id").val();
	    	var permissionName = $("#sys-permission-name-input").val();
	    	var resName = $("#sys-permission-resName-input").val();
	    	var url = $("#sys-permission-url-input").val();
	    	$.ajaxSettings.async = false;
	  		$.post(
	  			"/sys/permission/add", 
	  			{
	  				"id": id,
	  				"permissionName": permissionName,
	  				"resName": resName,
	  				"url": url
	  			}, function(data) {
	  				$('#add-sys-permission-modal').modal('hide');
	  				window.location.reload();
	  			}
	  		);
	  		$.ajaxSettings.async = true;
	    }
  	});
	
	// 表单参数验证
	$('#sys-permission-form').bootstrapValidator({
		live: 'submitted',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
        fields: {
        	permissionName: {
                validators: {
                    notEmpty: {
                        message: '权限名不能为空'
                    }
                }
            }, resName: {
                validators: {
                    notEmpty: {
                        message: '资源名不能为空'
                    }
                }
            }, url: {
                validators: {
                    notEmpty: {
                        message: 'url不能为空'
                    }
                }
            }
        }
    });
	
	// 修改系统权限
	$("#tb_table").delegate('.modify-sys-permission-btn', 'click', function () {
		var id = $(this).val();
		var row = $('#tb_table').bootstrapTable('getRowByUniqueId', id);
		$("#sys-permission-id").val(id);
		$("#sys-permission-name-input").val(row.permissionName);
		$("#sys-permission-name-input").attr('readonly', "readonly");
		$("#sys-permission-resName-input").val(row.resName);
		$("#sys-permission-url-input").val(row.url);
		$('#add-sys-permission-modal').modal('show');
    });
	
	// 删除URL配置
	$("#tb_table").delegate('.delete-sys-permission-btn', 'click', function () {
		$("#sys-permission-to-delete").val($(this).val());
		$('#delete-sys-permission-modal').modal('show');
    });
	$("#delete-sys-permission-modal-confirm-btn").click(function(){
		var id = $("#sys-permission-to-delete").val();
  		$.ajaxSettings.async = false;
  		$.post("/sys/permission/delete", {"id": id}, function(data)
  		{
  			$('#delete-sys-permission-modal').modal('hide');
  			window.location.reload();
  		});
  		$.ajaxSettings.async = true;
  	});
});
</script>
</html>
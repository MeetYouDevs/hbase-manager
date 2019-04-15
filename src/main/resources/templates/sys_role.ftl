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
				<h3>系统角色</h3>
				<a href="#" id="add-sys-role-btn" class="btn btn-default btn-sm">
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
	<div class="modal fade" id="delete-sys-role-modal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<input type="hidden" id="sys-role-to-delete" />
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">删除系统角色</h4>
				</div>
				<div class="modal-body">确认删除该系统角色?</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" id="delete-sys-role-modal-confirm-btn"
						class="btn btn-danger ops-button">确认</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="add-sys-role-modal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">添加系统角色</h4>
				</div>
				<div class="modal-body">
					<form id="sys-role-form" class="form-horizontal" role="form"
						action="#">
						<input name="roleId" id="sys-role-id" hidden="true">
						<div class="form-group">
							<label for="roleName" class="col-sm-2 control-label">角色名</label>
							<div class="col-sm-9">
								<input type="text" name="roleName" id="sys-role-roleName"
									class="form-control">
							</div>
						</div>
						<hr>
						<div class="form-group">
							<label for="roleDesc" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-9">
                            	<input type="text" name="roleDesc" id="sys-role-roleDesc" class="form-control">
							</div>
						</div>
						<hr>
						<div class="form-group">
							<label for="sysPermissionIds" class="col-sm-2 control-label">权限</label>
							<div class="col-sm-9">
								<select name="sysPermissionIds" id="sys-permission-ids" class="selectpicker show-tick form-control" multiple data-live-search="true">
                            	</select>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" id="add-sys-role-modal-confirm-btn"
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
	// 加载权限列表
	function loadSysPermissionList(select, url, value){
        $.ajax({
            url: url,
            dataType: "json",
            success: function (data) {
            	select.empty();
                $.each(data, function (index, entity) {
                	select.append("<option value="+entity.id+">" + entity.resName + "</option>");
                });
                select.selectpicker('refresh');
            	select.selectpicker('val', value.split(','));
            	select.selectpicker({noneSelectedText:'请选择'});
            }
        });
	}
	
	$("#tb_table").bootstrapTable({
		data : ${sysRoleList},
		striped : false,		//表格显示条纹
		pagination : true,  	//是否分页
	    pageSize : 10,      	//单页记录数
	    pageNumber : 1,
		pageList : "[10, 25, 50, 100, All]",
		search : true, 			//显示搜索框
		uniqueId: "roleId",
		columns : [
			{
				field : 'roleName',
				title : '角色名',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'roleDesc',
				title : '描述',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'sysPermissionIds',
				title : '权限',
				visible : false
			}, {
				field : '',
				title : '操作',
				align : 'center',
				valign : 'middle',
				sortable : true,
				formatter : function(value, row, index) {
					text = "<button type='button' value='"+row.roleId+"' class='btn btn-default ops-button modify-sys-role-btn'>Modify</button>&nbsp" + 
						"<button type='button' value='"+row.roleId+"' class='btn btn-danger ops-button delete-sys-role-btn'>Delete</button>";
					return text;
				}
			}
		]
	});
	//隐藏加载
	$("#tb_table").bootstrapTable('hideLoading');
	
	// 增加角色
	$("#add-sys-role-btn").click(function () {
		loadSysPermissionList($("#sys-permission-ids"), "/sys/permission/list", "");
		$("#sys-role-id").val("");
		$("#sys-role-roleName").val("");
		$("#sys-role-roleDesc").val("");
		$('#add-sys-role-modal').modal('show');
    });
	
	$("#add-sys-role-modal-confirm-btn").click(function(){
		// 获取校验器
	    var bootstrapValidator = $('#sys-role-form').data('bootstrapValidator');
	    // 手动触发验证
	    bootstrapValidator.validate();
	    // 检验通过
	    if(bootstrapValidator.isValid()){
	    	var roleId = $("#sys-role-id").val();
	    	var roleName = $("#sys-role-roleName").val();
	    	var roleDesc = $("#sys-role-roleDesc").val();
	    	// 权限列表
	    	var sysPermissionIdsValue = $("#sys-permission-ids").val();
	    	var sysPermissionIds = "";
	    	if(sysPermissionIdsValue) {
	    		$.each(sysPermissionIdsValue, function(index, value){
		    		sysPermissionIds = sysPermissionIds + value + ",";
		    	});
		    	sysPermissionIds = sysPermissionIds.substring(0, sysPermissionIds.length-1);
	    	}
	    	
	  		$.ajaxSettings.async = false;
	  		$.post(
	  			"/sys/role/add",
	  			{
	  				"roleId": roleId,
	  				"roleName": roleName,
	  				"roleDesc": roleDesc,
	  				"sysPermissionIds": sysPermissionIds
	  			}, function(data) {
	  				$('#add-sys-role-modal').modal('hide');
	  				window.location.reload();
	  			}
	  		);
	  		$.ajaxSettings.async = true;
	    }
  	});
	
	// 角色表单参数验证
	$('#sys-role-form').bootstrapValidator({
		live: 'submitted',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
        fields: {
        	roleName: {
                validators: {
                    notEmpty: {
                        message: '角色名不能为空'
                    }
                }
            }, roleDesc: {
                validators: {
                    notEmpty: {
                        message: '角色描述不能为空'
                    }
                }
            }
        }
    });
	
	// 修改角色
	$("#tb_table").delegate('.modify-sys-role-btn', 'click', function () {
		var roleId = $(this).val();
		var row = $('#tb_table').bootstrapTable('getRowByUniqueId', roleId);
		loadSysPermissionList($("#sys-permission-ids"), "/sys/permission/list", row.sysPermissionIds);
		$("#sys-role-id").val(roleId);
		$("#sys-role-roleName").val(row.roleName);
		$("#sys-role-roleDesc").val(row.roleDesc);
		$('#add-sys-role-modal').modal('show');
    });
	
	// 删除角色
	$("#tb_table").delegate('.delete-sys-role-btn', 'click', function () {
		$("#sys-role-to-delete").val($(this).val());
		$('#delete-sys-role-modal').modal('show');
    });
	$("#delete-sys-role-modal-confirm-btn").click(function(){
		var roleId = $("#sys-role-to-delete").val();
  		$.ajaxSettings.async = false;
  		$.post("/sys/role/delete", {"roleId": roleId}, function(data)
  		{
  			$('#delete-sys-role-modal').modal('hide');
  			window.location.reload();
  		});
  		$.ajaxSettings.async = true;
  	});
});
</script>
</html>
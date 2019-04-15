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
				<h3>系统用户</h3>
				<a href="#" id="add-sys-user-btn" class="btn btn-default btn-sm">
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
	<div class="modal fade" id="delete-sys-user-modal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<input type="hidden" id="sys-user-to-delete" />
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">删除系统用户</h4>
				</div>
				<div class="modal-body">确认删除该系统用户?</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" id="delete-sys-user-modal-confirm-btn"
						class="btn btn-danger ops-button">确认</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="add-sys-user-modal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">添加系统用户</h4>
				</div>
				<div class="modal-body">
					<form id="sys-user-form" class="form-horizontal" role="form"
						action="#">
						<input name="userId" id="sys-user-id" hidden="true">
						<div class="form-group">
							<label for="userName" class="col-sm-2 control-label">用户名</label>
							<div class="col-sm-9">
								<input type="text" name="userName" id="sys-user-userName"
									class="form-control">
							</div>
						</div>
						<hr>
						<div class="form-group">
							<label for="nickName" class="col-sm-2 control-label">昵称</label>
							<div class="col-sm-9">
                            	<input type="text" name="nickName" id="sys-user-nickName" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="password" class="col-sm-2 control-label">密码</label>
							<div class="col-sm-9">
                            	<input type="password" name="password" id="sys-user-password" class="form-control">
							</div>
						</div>
						<hr>
						<div class="form-group">
							<label for="sysRoleIds" class="col-sm-2 control-label">角色</label>
							<div class="col-sm-9">
								<select name="sysRoleIds" id="sys-role-ids" class="selectpicker show-tick form-control" multiple data-live-search="true">
                            	</select>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" id="add-sys-user-modal-confirm-btn"
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
	function loadSysRoleList(select, url, value){
        $.ajax({
            url: url,
            dataType: "json",
            success: function (data) {
            	select.empty();
                $.each(data, function (index, entity) {
                	select.append("<option value="+entity.roleId+">" + entity.roleName + "</option>");
                });
                select.selectpicker('refresh');
            	select.selectpicker('val', value.split(','));
            	select.selectpicker({noneSelectedText:'请选择'});
            }
        });
	}
	
	$("#tb_table").bootstrapTable({
		data : ${sysUserList},
		striped : false,		//表格显示条纹
		pagination : true,  	//是否分页
	    pageSize : 10,      	//单页记录数
	    pageNumber : 1,
		pageList : "[10, 25, 50, 100, All]",
		search : true, 			//显示搜索框
		uniqueId: "userId",
		columns : [
			{
				field : 'userName',
				title : '用户名',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'nickName',
				title : '昵称',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'password',
				title : '密码',
				visible : false
			}, {
				field : 'sysRoleIds',
				title : '角色',
				visible : false
			}, {
				field : '',
				title : '操作',
				align : 'center',
				valign : 'middle',
				sortable : true,
				formatter : function(value, row, index) {
					text = "<button type='button' value='"+row.userId+"' class='btn btn-default ops-button modify-sys-user-btn'>Modify</button>&nbsp" + 
						"<button type='button' value='"+row.userId+"' class='btn btn-danger ops-button delete-sys-user-btn'>Delete</button>";
					return text;
				}
			}
		]
	});
	//隐藏加载
	$("#tb_table").bootstrapTable('hideLoading');
	
	// 增加用户
	$("#add-sys-user-btn").click(function () {
		loadSysRoleList($("#sys-role-ids"), "/sys/role/list", "");
		$("#sys-user-id").val("");
		$("#sys-user-userName").val("");
		$("#sys-user-nickName").val("");
		$("#sys-user-password").val("");
		$('#add-sys-user-modal').modal('show');
    });
	
	$("#add-sys-user-modal-confirm-btn").click(function(){
		// 获取校验器
	    var bootstrapValidator = $('#sys-user-form').data('bootstrapValidator');
	    // 手动触发验证
	    bootstrapValidator.validate();
	    // 检验通过
	    if(bootstrapValidator.isValid()){
	    	var userId = $("#sys-user-id").val();
	    	var userName = $("#sys-user-userName").val();
	    	var nickName = $("#sys-user-nickName").val();
	    	var password = $("#sys-user-password").val();
	    	// 权限列表
	    	var sysRoleIdsValue = $("#sys-role-ids").val();
	    	var sysRoleIds = "";
	    	$.each(sysRoleIdsValue, function(index, value){
	    		sysRoleIds = sysRoleIds + value + ",";
	    	});
	    	sysRoleIds = sysRoleIds.substring(0, sysRoleIds.length-1);
	    	
	  		$.ajaxSettings.async = false;
	  		$.post(
	  			"/sys/user/add",
	  			{
	  				"userId": userId,
	  				"userName": userName,
	  				"nickName": nickName,
	  				"password": password,
	  				"sysRoleIds": sysRoleIds
	  			}, function(data) {
	  				$('#add-sys-user-modal').modal('hide');
	  				window.location.reload();
	  			}
	  		);
	  		$.ajaxSettings.async = true;
	    }
  	});
	
	// 用户表单参数验证
	$('#sys-user-form').bootstrapValidator({
		live: 'submitted',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
        fields: {
        	userName: {
                validators: {
                    notEmpty: {
                        message: '用户名不能为空'
                    }
                }
            }, nickName: {
                validators: {
                    notEmpty: {
                        message: '昵称不能为空'
                    }
                }
            }, password: {
                validators: {
                    notEmpty: {
                        message: '密码不能为空'
                    }
                }
            }, sysRoleIds: {
                validators: {
                    notEmpty: {
                        message: '角色不能为空'
                    }
                }
            }
        }
    });
	
	// 修改用户
	$("#tb_table").delegate('.modify-sys-user-btn', 'click', function () {
		var userId = $(this).val();
		var row = $('#tb_table').bootstrapTable('getRowByUniqueId', userId);
		loadSysRoleList($("#sys-role-ids"), "/sys/role/list", row.sysRoleIds);
		$("#sys-user-id").val(userId);
		$("#sys-user-userName").val(row.userName);
		$("#sys-user-nickName").val(row.nickName);
		$("#sys-user-password").val("");
		$('#add-sys-user-modal').modal('show');
    });
	
	// 删除用户
	$("#tb_table").delegate('.delete-sys-user-btn', 'click', function () {
		$("#sys-user-to-delete").val($(this).val());
		$('#delete-sys-user-modal').modal('show');
    });
	$("#delete-sys-user-modal-confirm-btn").click(function(){
		var userId = $("#sys-user-to-delete").val();
  		$.ajaxSettings.async = false;
  		$.post("/sys/user/delete", {"userId": userId}, function(data)
  		{
  			$('#delete-sys-user-modal').modal('hide');
  			window.location.reload();
  		});
  		$.ajaxSettings.async = true;
  	});
});
</script>
</html>
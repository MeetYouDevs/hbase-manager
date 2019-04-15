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
				<h3>运行集群</h3>
				<a href="#" id="add-run-cluster-btn" class="btn btn-default btn-sm">
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
	<div class="modal fade" id="delete-run-cluster-modal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<input type="hidden" id="run-cluster-to-delete" />
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">删除运行集群</h4>
				</div>
				<div class="modal-body">确认删除该运行集群?</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" id="delete-run-cluster-modal-confirm-btn"
						class="btn btn-danger ops-button">确认</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="add-run-cluster-modal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">添加运行集群</h4>
				</div>
				<div class="modal-body">
					<form id="run-cluster-form" class="form-horizontal" role="form"
						action="#">
						<input name="id" id="run-cluster-id" hidden="true">
						<div class="form-group">
							<label for="clusterName" class="col-sm-2 control-label">集群名</label>
							<div class="col-sm-9">
								<input type="text" name="clusterName" id="run-cluster-name-input"
									class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="ip" class="col-sm-2 control-label">ip</label>
							<div class="col-sm-9">
								<input type="text" name="ip" id="run-cluster-ip-input"
									class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="port" class="col-sm-2 control-label">端口</label>
							<div class="col-sm-9">
								<input type="number" name="port" id="run-cluster-port-input" min="0" max="65535" value="22"
									class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="sshUser" class="col-sm-2 control-label">ssh用户</label>
							<div class="col-sm-9">
								<input type="text" name="sshUser" id="run-cluster-sshUser-input"
									class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="sshPassword" class="col-sm-2 control-label">ssh密码</label>
							<div class="col-sm-9">
								<input type="password" name="sshPassword" id="run-cluster-sshPassword-input"
									class="form-control">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" id="add-run-cluster-modal-confirm-btn"
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
		data : ${runClusterList},
		striped : false,		//表格显示条纹
		pagination : true,  	//是否分页
	    pageSize : 10,      	//单页记录数
	    pageNumber : 1,
		pageList : "[10, 25, 50, 100, All]",
		search : true, 			//显示搜索框
		uniqueId: "id",
		columns : [
			{
				field : 'clusterName',
				title : '集群名',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'ip',
				title : 'ip地址',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'port',
				title : '端口',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'sshUser',
				title : 'ssh用户名',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'sshPassword',
				title : 'ssh密码',
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
					text = "<button type='button' value='"+row.id+"' class='btn btn-default ops-button modify-run-cluster-btn'>Modify</button>&nbsp" + 
						"<button type='button' value='"+row.id+"' class='btn btn-danger ops-button delete-run-cluster-btn'>Delete</button>";
					return text;
				}
			}
		]
	});
	//隐藏加载
	$("#tb_table").bootstrapTable('hideLoading');
	
	// 增加运行集群
	$("#add-run-cluster-btn").click(function () {
		$("#run-cluster-id").val("");
		$("#run-cluster-name-input").val("");
		$("#run-cluster-name-input").removeAttr('readonly');
		$("#run-cluster-ip-input").val("");
		$("#run-cluster-sshUser-input").val("");
		$("#run-cluster-sshPassword-input").val("");
		$('#add-run-cluster-modal').modal('show');
    });
	
	$("#add-run-cluster-modal-confirm-btn").click(function(){
		// 获取校验器
	    var bootstrapValidator = $('#run-cluster-form').data('bootstrapValidator');
	    // 手动触发验证
	    bootstrapValidator.validate();
	    // 检验通过
	    if(bootstrapValidator.isValid()){
	    	var id = $("#run-cluster-id").val();
	    	var clusterName = $("#run-cluster-name-input").val();
	    	var ip = $("#run-cluster-ip-input").val();
	    	var port = $("#run-cluster-port-input").val();
	    	var sshUser = $("#run-cluster-sshUser-input").val();
	    	var sshPassword = $("#run-cluster-sshPassword-input").val();
	    	$.ajaxSettings.async = false;
	  		$.post(
	  			"/run/cluster/add", 
	  			{
	  				"id": id,
	  				"clusterName": clusterName,
	  				"ip": ip,
	  				"port": port,
	  				"sshUser": sshUser,
	  				"sshPassword": sshPassword
	  			}, function(data) {
	  				$('#add-run-cluster-modal').modal('hide');
	  				window.location.reload();
	  			}
	  		);
	  		$.ajaxSettings.async = true;
	    }
  	});
	
	// 表单参数验证
	$('#run-cluster-form').bootstrapValidator({
		live: 'submitted',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
        fields: {
        	clusterName: {
                validators: {
                    notEmpty: {
                        message: '集群名不能为空'
                    }
                }
            }, ip: {
                validators: {
                    notEmpty: {
                        message: 'ip不能为空'
                    }
                }
            }, port: {
                validators: {
                    notEmpty: {
                        message: '端口不能为空'
                    }
                }
            }, sshUser: {
                validators: {
                    notEmpty: {
                        message: 'ssh用户不能为空'
                    }
                }
            }, sshPassword: {
                validators: {
                    notEmpty: {
                        message: 'ssh密码不能为空'
                    }
                }
            }
        }
    });
	
	// 修改运行集群
	$("#tb_table").delegate('.modify-run-cluster-btn', 'click', function () {
		var id = $(this).val();
		var row = $('#tb_table').bootstrapTable('getRowByUniqueId', id);
		$("#run-cluster-id").val(id);
		$("#run-cluster-name-input").val(row.clusterName);
		$("#run-cluster-name-input").attr('readonly', "readonly");
		$("#run-cluster-ip-input").val(row.ip);
		$("#run-cluster-port-input").val(row.port);
		$("#run-cluster-sshUser-input").val(row.sshUser);
		$("#run-cluster-sshPassword-input").val(row.sshPassword);
		$('#add-run-cluster-modal').modal('show');
    });
	
	// 删除URL配置
	$("#tb_table").delegate('.delete-run-cluster-btn', 'click', function () {
		$("#run-cluster-to-delete").val($(this).val());
		$('#delete-run-cluster-modal').modal('show');
    });
	$("#delete-run-cluster-modal-confirm-btn").click(function(){
		var id = $("#run-cluster-to-delete").val();
  		$.ajaxSettings.async = false;
  		$.post("/run/cluster/delete", {"id": id}, function(data)
  		{
  			$('#delete-run-cluster-modal').modal('hide');
  			window.location.reload();
  		});
  		$.ajaxSettings.async = true;
  	});
});
</script>
</html>
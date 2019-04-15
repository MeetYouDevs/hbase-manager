<!DOCTYPE html>
<html>
<head>
  	<#import "common/common.macro.ftl" as comm>
    <@comm.commonStyle />
    <title>HBase Manager</title>
</head>
<body>
	<div class="lowin">
		<div class="lowin-brand">
			<img src="/image/hbase.png" alt="logo">
		</div>
		<div class="lowin-wrapper">
			<div class="lowin-box lowin-login">
				<div class="lowin-box-inner">
					<form id="user-login-form" action="/login" role="form" method="post" onsubmit="return false">
						<div class="form-group lowin-group">
							<label>用户名</label> <input type="text" id="userName"
								name="userName" autocomplete="userName" class="lowin-input">
						</div>
						<div class="form-group lowin-group password-group">
							<label>密码</label> <input type="password" id="password"
								name="password" autocomplete="current-password"
								class="lowin-input">
						</div>
						<p id="login-result" class="lowin lowin-red"></p>
						<button id="user-login-btn" class="lowin-btn login-btn">登录</button>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
<@comm.commonScript />
<script type="text/javascript">
$(function(){
	$("#user-login-btn").click(
			function() {
				// 获取校验器
				var bootstrapValidator = $('#user-login-form').data(
						'bootstrapValidator');
				// 手动触发验证
				bootstrapValidator.validate();
				// 检验通过
				if (bootstrapValidator.isValid()) {
					$("#user-login-form").ajaxSubmit({
		                dataType:"json",
		                async: false,
		                success:function(resp){
		                    if(resp.code == 200){
		                      	//跳转到列表界面
		                        window.location.href="/cluster";
		                    }else{
		                    	$("#login-result").text(resp.message);
		                    }
		                }
		            });
				}
			});

	// 统计表单参数验证
	$('#user-login-form').bootstrapValidator({
		live : 'submitted',
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			userName : {
				validators : {
					notEmpty : {
						message : '用户名不能为空'
					}
				}
			},
			password : {
				validators : {
					notEmpty : {
						message : '密码不能为空'
					}
				}
			}
		}
	});
});
</script>
</html>
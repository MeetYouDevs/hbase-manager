<!DOCTYPE html>
<html>
<head>
  	<#import "common/common.macro.ftl" as comm>
    <@comm.commonStyle />
    <title>HBase Manager-快照定时任务</title>
</head>
<div class="wrapper">
    <!-- header -->
	<@comm.commonHeader />
	<div class="col-md-12">
		<#--<div class="panel panel-default">-->
			<#--<div class="panel-heading">-->
				<#--<h3>Edit Snapshot Tasks</h3>-->
			<#--</div>-->
		<#--</div>-->
	</div>
	<div class="col-md-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3>Edit Snapshot Tasks</h3>
			</div>
			<div class="panel-body">
				<form id="add-form" class="form-horizontal" role="form" action="#">
					<input value="${task.id}" class="form-control" type="hidden" id="ipt_id">
					<div class="form-group">
						<label for="cluster" class="col-sm-2 control-label">集群</label>
						<div class="col-sm-7">
							<input id="ipt_cluster" readonly value="${task.cluster}" class="form-control" >
						</div>
					</div>
					<div class="form-group">
						<label for="select_table" class="col-sm-2 control-label">表名</label>
						<div class="col-sm-7">
							<input id="ipt_tableName" readonly value="${task.tableName}" class="form-control" >
						</div>
					</div>
					<hr>
					<div class="form-group">
						<label for="cycleType" class="col-sm-2 control-label">周期类型</label>
						<div class="col-sm-2">
							<select id="select_cycle_type" name="cycleType" class="selectpicker show-tick form-control">
								<option value="1" <#if task.cycleType==1>selected</#if> >小时</option>
								<option value="2" <#if task.cycleType==2>selected</#if> >天</option>
								<option value="3" <#if task.cycleType==3>selected</#if> >周</option>
								<option value="4" <#if task.cycleType==4>selected</#if> >月</option>
							</select>
						</div>
						<label for="keepCnt" class="col-sm-3 control-label" >保留周期数</label>
						<div class="col-sm-2">
							<input id="ipt_keepCnt" name="keepCnt" class="form-control" value="${task.keepCnt}" type="number" min="0"/>
						</div>
					</div>
					<div class="form-group">
						<label for="customCron" class="col-sm-2 control-label">cron</label>
						<div class="col-sm-7">
							<input id="ipt_customCron" name="customCron" class="form-control" value="${task.customCron}" />
						</div>
					</div>
					<input id="ipt_customCronOn" name="customCronOn" hidden value="1"/>
					<hr>

					<div class="form-group">
						<label for="snapshotName" class="col-sm-2 control-label">快照名</label>
						<div class="col-sm-7">
							<input id="ipt_snapshotName" name="snapshotName" class="form-control" value="${task.snapshotName}"/>
						</div>
					</div>
					<hr>
					<div class="form-group">
						<label for="dtp_inure_date" class="col-md-2 control-label">生效日期</label>
						<div id ="dtp_inure_date" class="input-group date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="ipt_inure_date" data-link-format="yyyy-mm-dd">
							<input class="form-control" size="16" type="text" value="${task.inureDate?string('yyyy-MM-dd')}" readonly>
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						</div>
						<input type="hidden" id="ipt_inure_date" name="inureDate" value="${task.inureDate?string('yyyy-MM-dd')}"/>
					</div>
					<div class="form-group">
						<label for="dtp_expire_date" class="col-md-2 control-label">失效日期</label>
						<div id = "dtp_expire_date" class="input-group date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="ipt_expire_date" data-link-format="yyyy-mm-dd">
							<input class="form-control" size="16" type="text" value="${task.expireDate?string('yyyy-MM-dd')}" readonly>
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						</div>
						<input type="hidden" id="ipt_expire_date" name="expireDate" value="${task.expireDate?string('yyyy-MM-dd')}" />
					</div>
					<div class="form-group">
						<label for="cycle" class="col-sm-2 control-label">状态</label>
						<div class="col-sm-2">
							<select id="select_status" name="status" class="selectpicker show-tick form-control">
								<option value="0" <#if task.status==0>selected</#if>>正常</option>
								<option value="1" <#if task.status==1>selected</#if>>禁用</option>
							</select>
						</div>
					</div>
					<hr>
					<div class="form-group">
						<div class="col-sm-4"></div>
						<div class="col-sm-4">
							<button type="button" id="add-modal-confirm-btn" class="btn btn-primary ops-button">修改</button>
							 &nbsp;&nbsp;&nbsp;
							<button type="button" class="cancel-button btn btn-default" onclick="goBack()">返回</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
    <!-- footer -->
	<@comm.commonFooter />
</div>
</body>

<@comm.commonScript />
<script type="text/javascript">
$(function(){

    function goBack() {
        window.history.back()
    }

    $('#dtp_inure_date').datetimepicker({
        language:  'zh-CN',
        weekStart: 1,
        todayBtn:  1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0
    });

    $('#dtp_expire_date').datetimepicker({
        language:  'zh-CN',
        weekStart: 1,
        todayBtn:  1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0
    });

	$("#add-modal-confirm-btn").click(function(){
		// 获取校验器
	    var bootstrapValidator = $('#add-form').data('bootstrapValidator');
	    // 手动触发验证
	    bootstrapValidator.validate();
	    // 检验通过
	    if(bootstrapValidator.isValid()){
            var id = $("#ipt_id").val();
	    	var cluster = $("#ipt_cluster").val();
	    	var tableName = $("#ipt_tableName").val();
	    	var cycleType = $("#select_cycle_type").val();
            var snapshotName = $("#ipt_snapshotName").val();
            var inureDate = $("#ipt_inure_date").val();
            var expireDate = $("#ipt_expire_date").val();
            var status = $("#select_status").val();
            var customCron = $("#ipt_customCron").val();
            var customCronOn = $("#ipt_customCronOn").val();
            var keepCnt = $("#ipt_keepCnt").val();

	  		$.ajaxSettings.async = false;

            $.ajax({
                type: 'POST',
                url: "/cluster/${clusterName}/snapshot/task/add",
				contentType:"application/json;charset=UTF-8",
                // data:$("#add-form").serialize(),
				data:JSON.stringify({
					"id":id,
					"cluster":cluster,
                    "tableName":tableName,
                    "cycleType":cycleType,
                    "snapshotName":snapshotName,
                    "inureDate":inureDate,
                    "expireDate":expireDate,
                    "status":status,
                    "customCron":customCron,
					"customCronOn":customCronOn,
					"keepCnt": keepCnt
                }),
                success:  function(data) {
                    $('#add-modal').modal('hide');
                    window.location.href="/cluster/${clusterName}/snapshot/task";
                },
				dataType:"json"
            });
	  		$.ajaxSettings.async = true;
	    }
  	});
	
	// 同步表单参数验证
	$('#add-form').bootstrapValidator({
		live: 'submitted',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
        fields: {
        	cluster: {
                validators: {
                    notEmpty: {
                        message: '源集群不能为空'
                    }
                }
            }, 
            table: {
                validators: {
                    notEmpty: {
                        message: '源表不能为空'
                    }
                }
            },
            customCron: {
                validators: {
                    notEmpty: {
                        message: 'Cron不能为空'
                    }
                }
            },
            inureDate: {
                validators: {
                    notEmpty: {
                        message: '生效时间不能为空'
                    }
                }
            },
            expireDate: {
                validators: {
                    notEmpty: {
                        message: '失效时间不能为空'
                    }
                }
            },
            keepCnt:{
        	    validators:{
        	        notEmpty:{
        	            message:'保留周期数不能为空'
					}
				}
			}

        }
    });
});
</script>
</html>
<!DOCTYPE html>
<html>
<head>
  	<#import "common/common.macro.ftl" as comm>
    <@comm.commonStyle />
    <title>HBase Manager-迁移</title>
</head>
<div class="wrapper">
    <!-- header -->
	<@comm.commonHeader />
	<div class="col-md-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3>
					<button type="button" class="btn btn-link" onclick="goBack()">
						<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>
					</button>
					Edit Migration Task
				</h3>
			</div>
			<div class="panel-body">
				<form id="add-form" class="form-horizontal" role="form" action="/mig/task/add" method="post">
					<div class="form-group">
						<label for="id" class="col-sm-2 control-label">ID</label>
						<div class="col-sm-2">
							<input value="${task.id!}" class="form-control"  name="id" readonly>
						</div>
						<label for="createdDate" class="col-sm-1 control-label">创建时间</label>
						<div class="col-sm-2">
							<input value="${task.createdDate!}" class="form-control"  name="createdDate" readonly>
						</div>
					</div>
					<div class="form-group">
						<label for="sourceCluster" class="col-sm-2 control-label">源集群</label>
						<div class="col-sm-2">
							<select id="sourceCluster" name="sourceCluster" class="selectpicker show-tick form-control" data-live-search="true">
							</select>
						</div>
						<label for="sourceTable" class="col-sm-1 control-label">源表名</label>
						<div class="col-sm-2">
							<select id="sourceTable" name="sourceTable" class="selectpicker show-tick form-control" data-live-search="true">
							</select>
						</div>
						<p id="tableSpaceSize" class="help-block" style="display: none;"></p>
					</div>

					<hr>
					<div class="form-group">
						<label for="targetCluster" class="col-sm-2 control-label">目标集群</label>
						<div class="col-sm-2">
							<select id="targetCluster" name="targetCluster" class="selectpicker show-tick form-control" data-live-search="true">
							</select>
						</div>
						<label for="targetTable" class="col-sm-1 control-label">目标表名</label>
						<div class="col-sm-2">
							<input id="targetTable" name="targetTable"  type="text" class="form-control" placeholder="请输入目标表名" value="${task.targetTable!}">
						</div>
					</div>

					<hr>
					<div class="form-group">
						<label for="migrateType" class="col-sm-2 control-label">迁移方式</label>
						<div class="col-sm-2">
							<select name="migrateType" class="selectpicker show-tick form-control" data-live-search="true">
								<option value="1">快照迁移(SNAPSHOT)</option>
							</select>
						</div>
						<label for="runCluster" class="col-sm-1 control-label">运行集群</label>
						<div class="col-sm-2">
							<select id = "runCluster" name="runCluster" class="selectpicker show-tick form-control" data-live-search="true" >
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="runQueue" class="col-sm-2 control-label">运行队列</label>
						<div class="col-sm-2">
							<input name="runQueue" type="text" class="form-control" placeholder="root.test" value="${task.runQueue!}">
						</div>
					</div>

					<div class="form-group">
						<label for="programArguments" class="col-sm-2 control-label">运行参数</label>
						<div class="col-sm-5">
							<textarea class="form-control" rows="2" name="programArguments">${task.programArguments!}</textarea>
							<p class="help-block">-overwrite -chuser <b>USERNAME</b> -chgroup <b>GROUP</b>  -chmod <b>MODE</b> -mappers <b>NUMBER</b></p>
						</div>
					</div>

					<div class="form-group">
						<label for="status" class="col-sm-2 control-label">状态</label>
						<div class="col-sm-2">
							<select id="select_status" name="status" class="selectpicker show-tick form-control">
								<option value="0"  <#if task.status==0>selected</#if> >正常</option>
								<option value="1"  <#if task.status==1>selected</#if> >禁用</option>
							</select>
						</div>
					</div>
					<hr>
					<div class="form-group">
						<label for="customCronOn" class="col-sm-2 control-label">是否定时</label>
						<div class="col-sm-2">
							<select id="select_customCronOn" name="customCronOn" class="selectpicker show-tick form-control">
								<option value="0" <#if task.customCronOn==0>selected</#if> >否</option>
								<option value="1" <#if task.customCronOn==1>selected</#if> >是</option>
							</select>
						</div>
					</div>

					<div <#if task.customCronOn==0>style="display:none"</#if> id="cycle_div">
						<hr>
						<div class="form-group">
							<label for="cycle" class="col-sm-2 control-label">周期类型</label>
							<div class="col-sm-2">
								<select id="select_cycle_type" name="cycle" class="selectpicker show-tick form-control">
									<option value="1" <#if task.cycle?default(0)==1>selected</#if> >小时</option>
									<option value="2" <#if task.cycle?default(0)==2>selected</#if> >天</option>
									<option value="3" <#if task.cycle?default(0)==3>selected</#if> >周</option>
									<option value="4" <#if task.cycle?default(0)==4>selected</#if> >月</option>
								</select>
							</div>
							<label for="keepCnt" class="col-sm-1 control-label" >保留周期数</label>
							<div class="col-sm-2">
								<input id="ipt_keepCnt" name="keepCnt" class="form-control" value="${task.keepCnt!0}" type="number" min="0"/>
							</div>
						</div>
						<div class="form-group">
							<label for="customCron" class="col-sm-2 control-label">cron</label>
							<div class="col-sm-2">
								<input id="ipt_customCron" name="customCron" class="form-control" value="${task.customCron!}" />
							</div>
						</div>
					</div>
					<hr>
					<div class="form-group">
						<div class="col-sm-4"></div>
						<div class="col-sm-4">
							<button type="submit" id="add-modal-confirm-btn" class="btn btn-primary">确认</button>
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

	function goBack() {
		window.history.back()
	}

$(function(){
    //初始化 集群
    $.ajax({
        url: "/api/cluster/list",
        dataType: "json",
        success: function (data) {
            $.each(data, function (index, cluster) {
                $('#sourceCluster,#targetCluster').append("<option value="+cluster.clusterName+">" + cluster.clusterName + "</option>");
                $('#sourceCluster,#targetCluster').selectpicker('refresh');
                $('#sourceCluster').selectpicker('val', '${task.sourceCluster!}');
                $('#targetCluster').selectpicker('val', '${task.targetCluster!}');
                $('#sourceCluster,#targetCluster').selectpicker({noneSelectedText:'请选择'});
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("集群列表加载错误！");
        }
    });

    $.ajax({
        url: "/run/cluster/list",
        dataType: "json",
        success: function (data) {
            $.each(data, function (index, cluster) {
                $('#runCluster').append("<option value="+cluster.clusterName+">" + cluster.clusterName + "</option>");
                $('#runCluster').selectpicker('refresh');
                $('#runCluster').selectpicker('val', '${task.runCluster!}');
                $('#runCluster').selectpicker({noneSelectedText:'请选择'});
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("集群列表加载错误！");
        }
    });

    //初始化
    if('${task.sourceCluster!}'!=''){
        loadSourceTableList('${task.sourceCluster!}', '${task.sourceTable!}');
	}else{
        $("#sourceTable").selectpicker({noneSelectedText:'请选择'});
	}

    function loadSourceTableList(cluster,curTable){
        $.ajax({
            url: "/cluster/"+cluster+"/api/tables",
            dataType: "json",
            success: function (data) {
                $.each(data, function (index, table) {
                    $("#sourceTable").append("<option namespace="+table.namespace+" value="+table.tableName+">" + table.tableName + "</option>");
                    $('#sourceTable').selectpicker('refresh');
                    $('#sourceTable').selectpicker('val', curTable);
                    $("#sourceTable").selectpicker({noneSelectedText:'请选择'});
                });
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert("hbase-cluster-table error");
            }
        });
	}

    //源表名下拉列表
    $("#sourceCluster").bind("change", function (){
        $("#sourceTable").find("option").remove();
		var cluster = $("option:selected",this).text();
        loadSourceTableList(cluster,'');
    });

    //选择源表
    $("#sourceTable").bind("change", function (){
        var tableSelected = $("option:selected",this);
        var cluster = $("option:selected",$("#sourceCluster")).text();
        var namespace = tableSelected.attr("namespace");
        var tableName = tableSelected.text();
        $.ajax({
            url: "/cluster/"+cluster+"/namespace/"+namespace+"/table/"+tableName+"/getSpaceSize",
            success: function (data) {
                var text = String((parseFloat(data)/(1024*1024)).toFixed(2))+" MB";
                $("#tableSpaceSize").text("大小: "+text);
                $("#tableSpaceSize").show();
            }
        });
    });

    //选择源表
    $("#select_customCronOn").bind("change", function (){
        var customCronOn = $("option:selected",this).val();
		if(customCronOn==1){
            $("#cycle_div").show();//表示display:block,
		}else{
            $("#cycle_div").hide();//表示display:none;
		}
    });


    // 同步表单参数验证
    $('#add-form').bootstrapValidator({
        // live: 'submitted',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            sourceCluster:{
                validators:{
                    notEmpty:{
                        message:'源集群不能为空!'
                    }
                }
            },sourceTable:{
                validators:{
                    notEmpty:{
                        message:'源表名不能为空!'
                    }
                }
            },targetCluster:{
                validators:{
                    notEmpty:{
                        message:'目标集群不能为空!'
                    }
                }
            },targetTable:{
                validators:{
                    notEmpty:{
                        message:'目标表名不能为空!'
                    }
                }
            },migrateType:{
            	validators:{
                	notEmpty:{
                    	message:'迁移方式不能为空!'
                	}
            	}
        	},runCluster:{
                validators:{
                    notEmpty:{
                        message:'运行集群不能为空!'
                    }
                }
            },runQueue:{
                validators:{
                    notEmpty:{
                        message:'运行集群不能为空!'
                    }
                }
            }
        }
    });

    $("#add-form").submit(function(ev){ev.preventDefault();});

    $("#add-modal-confirm-btn").on("click", function(){
        $('#add-form').bootstrapValidator('validate');
        var flag = $("#add-form").data('bootstrapValidator').isValid();
        if(flag){
            $("#add-form").ajaxSubmit({
                dataType:"json",
                success:function(resp){
                    if(resp.code = 200){
                        alert("保存成功！");
                        //window.location.reload();
                        window.location.href="/mig/task";
                    }else{
                        alert("保存失败！ code:"+resp.code+"  message:" + resp.message);
                    }
                }
            });
            // $("#add-form").submit();
		}else{
            alert("表单验证失败！");
		}
    });
});
</script>
</html>
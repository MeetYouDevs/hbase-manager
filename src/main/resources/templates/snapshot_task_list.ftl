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
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3>Snapshot Tasks</h3>
				<a href="#" id="show-add-modal-btn" class="btn btn-default btn-sm">
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
	<div class="modal fade" id="delete-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<input type="hidden" id="ipt-id-to-delete"/>
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="myModalLabel">删除Snapshot任务</h4>
	            </div>
	            <div class="modal-body">确认删除Snapshot任务?<span id="span-id-to-delete"/></div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	                <button type="button" id="delete-modal-confirm-btn" class="btn btn-danger ops-button">确认</button>
	            </div>
	        </div>
	    </div>
	</div>

    <!--新增快照任务对话框-->
	<div class="modal fade" id="add-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="myModalLabel">快照任务</h4>
	            </div>
	            <div class="modal-body">
	            	<form id="add-form" class="form-horizontal" role="form" action="#">
				        <div class="form-group">
	                        <label for="cluster" class="col-sm-2 control-label">集群</label>
	                        <div class="col-sm-7">
	                            <input id="ipt_cluster" readonly value="${clusterName}" class="form-control" >
	                        </div>
	                    </div>
	                    <div class="form-group">
	                        <label for="select_table" class="col-sm-2 control-label">表名</label>
	                        <div class="col-sm-7">
	                            <select id="select_table" name="tableName" class="selectpicker show-tick form-control" data-live-search="true">
                                    <option>-请选择-</option>
                                    <#list tableNameList as tableName>
                                        <option value="${tableName}">${tableName}</option>
                                    </#list>
	                            </select>
	                        </div>
	                    </div>
                        <hr>
	                    <div class="form-group">
	                        <label for="cycle" class="col-sm-2 control-label">周期类型</label>
	                        <div class="col-sm-2">
	                        	<select id="select_cycle_type" name="cycle" class="selectpicker show-tick form-control">
                                    <option value="1">小时</option>
                                    <option value="2">天</option>
                                    <option value="3">周</option>
                                    <option value="4">月</option>
	                            </select>
	                        </div>
							<label for="keepCnt" class="col-sm-3 control-label">保留周期数</label>
							<div class="col-sm-2">
								<input id="ipt_keepCnt" name="keepCnt" class="form-control"/>
							</div>
						</div>
						<div class="form-group">
							<label for="customCron" class="col-sm-2 control-label">cron</label>
							<div class="col-sm-7">
								<input id="ipt_customCron" name="customCron" class="form-control" />
							</div>
						</div>
						<input id="ipt_customCronOn" name="customCronOn" hidden value="1"/>
						<hr>
						
						<div class="form-group">
							<label for="snapshotName" class="col-sm-2 control-label">快照名</label>
							<div class="col-sm-7">
								<input id="ipt_snapshotName" name="snapshotName" class="form-control" />
							</div>
						</div>
						<hr>
						<div class="form-group">
							<label for="dtp_inure_date" class="col-md-2 control-label">生效日期</label>
							<div id ="dtp_inure_date" class="input-group date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="ipt_inure_date" data-link-format="yyyy-mm-dd">
								<input class="form-control" size="16" type="text" value="" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
							</div>
							<input type="hidden" id="ipt_inure_date" name="inureDate" />
						</div>
						<div class="form-group">
							<label for="dtp_expire_date" class="col-md-2 control-label">失效日期</label>
							<div id = "dtp_expire_date" class="input-group date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="ipt_expire_date" data-link-format="yyyy-mm-dd">
								<input class="form-control" size="16" type="text" value="" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
							</div>
							<input type="hidden" id="ipt_expire_date" name="expireDate" />
						</div>
						<div class="form-group">
							<label for="cycle" class="col-sm-2 control-label">状态</label>
							<div class="col-sm-2">
								<select id="select_status" name="status" class="selectpicker show-tick form-control">
									<option value="0" selected>正常</option>
									<option value="1">禁用</option>
								</select>
							</div>
						</div>
			        </form>
			   	</div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	                <button type="button" id="add-modal-confirm-btn" class="btn btn-primary ops-button">确认</button>
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
		data : ${snapshotTaskList},
		striped : false,		//表格显示条纹
		pagination : true,  	//是否分页
	    pageSize : 10,      	//单页记录数
	    pageNumber : 1,
		pageList : "[10, 25, 50, 100, All]",
		search : true, 			//显示搜索框
		columns : [
            {
                field : 'id',
                title : 'ID',
                align : 'center',
                valign : 'middle',
                sortable : true,
                formatter : function(value, row, index) {
                    	var  text ="<a href='/cluster/${clusterName}/snapshot/task/"+value+"/edit'>"+value+"</a>";
                    	return text;
					}

            },{
				field : 'cluster',
				title : '集群',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'tableName',
				title : '表名',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'cycleType',
				title : '周期',
				align : 'center',
				valign : 'middle',
				sortable : true ,
                formatter : function(value, row, index) {
				    //周期类型(1-小时 2-天; 3-周 4-月)
                    var text="-";
                    if(value==1){
                        text="小时";
					}else if(value==2){
                        text="天";
                    }else if(value==3){
                        text="周";
                    }else if(value==4){
                        text="月";
                    }
                    return text;
                }
			}, {
                field : 'keepCnt',
                title : '保留周期数',
                align : 'center',
                valign : 'middle',
                sortable : true
            }, {
                field : 'inureDate',
                title : '生效时间',
                align : 'center',
                valign : 'middle',
                sortable : true,
                formatter:function (value,row,index) {
                    return  new Date(parseInt(value)).Format('yyyy-MM-dd');;
                }
            }, {
                field : 'expireDate',
                title : '失效时间',
                align : 'center',
                valign : 'middle',
                sortable : true,
                formatter:function (value,row,index) {
                    return  new Date(parseInt(value)).Format('yyyy-MM-dd');;
                }
            }, {
                field : 'customCron',
                title : 'cron',
                align : 'center',
                valign : 'middle',
                sortable : true
            }, {
                field : 'status',
                title : '状态',
                align : 'center',
                valign : 'middle',
                sortable : true,
                formatter : function(value, row, index) {
                    //0-停用 1-正常
                    var text="-";
                    if(value==0){
                        text="正常";
                    }else if(value==1){
                        text="停用";
                    }
                    return text;
                }
            }, {
                field : 'createDate',
                title : '创建时间',
                align : 'center',
                valign : 'middle',
                sortable : true,
                formatter:function (value,row,index) {
                    return  new Date(parseInt(value)).Format('yyyy-MM-dd hh:mm:ss');;
                }
            }, {
				field : '',
				title : '操作',
				align : 'center',
				valign : 'middle',
				sortable : true,
				formatter : function(value, row, index) {
					text = "<button type='button' value='"+row.id+"' class='btn btn-danger ops-button delete-task-btn'>Delete</button>";
					return text;
				}
			}
		]
	});
	//隐藏加载
	$("#tb_table").bootstrapTable('hideLoading');
	
	// 新增快照定时任务
	$("#show-add-modal-btn").click(function () {
	    //数据清除
		// var clusterSelect = $("#replication-source-cluster-select, #replication-target-cluster-select");
		// clusterSelect.find("option").remove();

		//弹出表单框
        $('#add-modal').modal('show');
	});

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
	    	var cluster = $("#ipt_cluster").val();
	    	var tableName = $("#select_table").val();
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
                data:$("#add-form").serialize(),
				data:JSON.stringify({"cluster":cluster,
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
                    window.location.reload();
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
	
	// 删除同步任务
	$("#tb_table").delegate('.delete-task-btn', 'click', function () {
	    var id = $(this).val();
		$("#ipt-id-to-delete").val(id);
        $("#span-id-to-delete").text('【'+id+'】');
		$('#delete-modal').modal('show');
    });
	$("#delete-modal-confirm-btn").click(function(){
		var id = $("#ipt-id-to-delete").val();

  		$.ajaxSettings.async = false;
  		$.post("/cluster/${clusterName}/snapshot/task/del", {"id": id}, function(data)
  		{
  			$('#delete-modal').modal('hide');
  			window.location.reload();
  		});
  		$.ajaxSettings.async = true;
  	});
	
});
</script>
</html>
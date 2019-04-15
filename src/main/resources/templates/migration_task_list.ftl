<!DOCTYPE html>
<html>
<head>
	<#import "common/common.macro.ftl" as comm>
	<@comm.commonStyle />
	<title>HBase Manager-迁移管理</title>
</head>
<div class="wrapper">
	<!-- header -->
	<@comm.commonHeader />
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<table id="tb_table"></table>
				</div>
			</div>
		</div>
		<div class="modal fade" id="delete-task-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<input type="hidden" id="id-to-delete"/>
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">删除任务</h4>
					</div>
					<div class="modal-body">确认删除任务? <span id="id-to-delete-span"/></div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" id="delete-modal-confirm-btn" class="btn btn-danger ops-button">确认</button>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" id="add-replication-task-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">新增表同步任务</h4>
					</div>
					<div class="modal-body">
						<form id="replication-task-form" class="form-horizontal" role="form" action="#">
							<div class="form-group">
								<label for="sourceCluster" class="col-sm-2 control-label">源集群</label>
								<div class="col-sm-9">
									<select id="replication-source-cluster-select" name="sourceCluster" class="selectpicker show-tick form-control" data-live-search="true">
									</select>
								</div>
							</div>
							<div class="form-group">
								<label for="sourceTable" class="col-sm-2 control-label">源表名</label>
								<div class="col-sm-9">
									<select id="replication-source-table-select" name="sourceTable" class="selectpicker show-tick form-control" data-live-search="true">
									</select>
								</div>
							</div>
							<hr>
							<div class="form-group">
								<label for="targetCluster" class="col-sm-2 control-label">目标集群</label>
								<div class="col-sm-9">
									<select id="replication-target-cluster-select" name="targetCluster" class="selectpicker show-tick form-control" data-live-search="true">
									</select>
								</div>
							</div>
							<div class="form-group">
								<label for="targetTable" class="col-sm-2 control-label">目标表名</label>
								<div class="col-sm-9">
									<select id="replication-target-table-select" name="targetTable" class="selectpicker show-tick form-control" data-live-search="true">
									</select>
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal" >取消</button>
						<button type="button" id="add-replication-task-modal-confirm-btn" class="btn btn-primary ops-button">确认</button>
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
            data : ${migrationTaskList},
            striped : false,		//表格显示条纹
            pagination : true,  	//是否分页
            pageSize : 10,      	//单页记录数
            pageNumber : 1,
            pageList : "[10, 20, 50, 100, All]",
            search : true, 			//显示搜索框
            columns : [
                {
                    field : 'id',
                    title : 'ID',
                    align : 'center',
                    valign : 'middle',
                    sortable : true,
					width:'5%'
                }, {
                    field : 'sourceCluster',
                    title : '源集群',
                    align : 'center',
                    valign : 'middle',
                    sortable : true
                }, {
                    field : 'sourceTable',
                    title : '源表名',
                    align : 'center',
                    valign : 'middle',
                    sortable : true
                }, {
                    field : 'targetCluster',
                    title : '目标集群',
                    align : 'center',
                    valign : 'middle',
                    sortable : true
                }, {
                    field : 'targetTable',
                    title : '目标表名',
                    align : 'center',
                    valign : 'middle',
                    sortable : true
                }, {
                    field : 'runCluster',
                    title : '运行集群',
                    align : 'center',
                    valign : 'middle',
                    sortable : true
                }, {
                    field : 'runQueue',
                    title : '运行队列',
                    align : 'center',
                    valign : 'middle',
                    sortable : true
                }, {
                    field : 'programArguments',
                    title : '运行参数',
                    align : 'left',
                    valign : 'middle',
                    sortable : true ,
                    width:'10%'
                }, {
                	field : 'customCronOn',
               		title : '是否开启定时',
                	align : 'center',
               		valign : 'middle',
                	sortable : true,
                    formatter : function(value, row, index) {
                        //0-停用 1-正常
                        var text="-";
                        if(value==0){
                            text="否";
                        }else if(value==1){
                            text="是";
                        }
                        return text;
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
                            text="禁用";
                        }
                        return text;
                    }
                }, {
                    field : 'createdDate',
                    title : '创建时间',
                    align : 'center',
                    valign : 'middle',
                    sortable : true,
					formatter:function (value,row,index) {
						return  new Date(parseInt(value)).Format('yyyy-MM-dd');;
                    }
                }, {
                    field : '',
                    title : '操作',
                    align : 'center',
                    valign : 'middle',
                    formatter : function(value, row, index) {
                        var text = "<button type='button' value='"+row.id+"' class='btn btn-danger ops-button delete-btn btn-xs'>删除</button>&nbsp;<button type='button' value='"+row.id+"' class='btn btn-warning ops-button modify-btn btn-xs'>编辑</button>&nbsp;<button type='button'  value='"+row.id+"' class='btn btn-success ops-button exe-btn btn-xs'>手工执行</button>";
                        return text;
                    }
                }
            ]
        });
        //隐藏加载
        $("#tb_table").bootstrapTable('hideLoading');

        // 删除按钮
        $("#tb_table").delegate('.delete-btn', 'click', function () {
            var id = $(this).val();
            $("#id-to-delete").val(id);
            $("#id-to-delete-span").text('【'+id+'】');
            $('#delete-task-modal').modal('show');
        });

        // 编辑按钮
        $("#tb_table").delegate('.modify-btn', 'click', function () {
            var id = $(this).val();
   			//跳转到编辑界面
            window.location.href="/mig/task/edit/"+id;
        });

        // 执行按钮
        $("#tb_table").delegate('.exe-btn', 'click', function () {
            var id = $(this).val();
            $.post("/mig/task/run", {"id": id}, function(result)
            {
                if(result.code == 200){
                    //跳转到详细日志界面
                    window.open("/mig/task/detail/log/"+result.data)
				}
            });
        });

        $("#delete-modal-confirm-btn").click(function(){
            var id = $("#id-to-delete").val();
            $.ajaxSettings.async = false;
            $.post("/mig/task/del", {"id": id}, function(result)
            {
                $('#delete-task-modal').modal('hide');
                window.location.reload();
            });
            $.ajaxSettings.async = true;
        });
    });
</script>
</html>
<!DOCTYPE html>
<html>
<head>
	<#import "common/common.macro.ftl" as comm>
	<@comm.commonStyle />
	<title>HBase Manager-快照管理</title>
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
		<div class="modal fade" id="delete-replication-task-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<input type="hidden" id="replication-task-id-to-delete"/>
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">删除快照</h4>
					</div>
					<div class="modal-body">确认删除快照? <span id="replication-task-id-to-delete-span"/></div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" id="delete-replication-task-modal-confirm-btn" class="btn btn-danger ops-button">确认</button>
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
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
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
            data : ${snapshotList},
            striped : false,		//表格显示条纹
            pagination : true,  	//是否分页
            pageSize : 10,      	//单页记录数
            pageNumber : 1,
            pageList : "[10, 20, 50, 100, All]",
            search : true, 			//显示搜索框
            columns : [
                {
                    field : 'name',
                    title : '快照名称',
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
                    field : 'createTime',
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
                    formatter : function(value, row, index) {
                        text = "<button type='button' value='"+row.name+"' class='btn btn-danger ops-button delete-btn'>Delete</button>";
                        return text;
                    }
                }
            ]
        });
        //隐藏加载
        $("#tb_table").bootstrapTable('hideLoading');

        // 删除快照
        $("#tb_table").delegate('.delete-btn', 'click', function () {
            var name = $(this).val();
            $("#replication-task-id-to-delete").val(name);
            $("#replication-task-id-to-delete-span").text('【'+name+'】');
            $('#delete-replication-task-modal').modal('show');
        });

        $("#delete-replication-task-modal-confirm-btn").click(function(){
            var snapshotName = $("#replication-task-id-to-delete").val();
            $.ajaxSettings.async = false;
            $.post("/cluster/${clusterName}/snapshot/delete", {"clusterName": "${clusterName}","name": snapshotName}, function(result)
            {
                $('#delete-replication-task-modal').modal('hide');
                window.location.reload();
            });
            $.ajaxSettings.async = true;
        });
    });
</script>
</html>
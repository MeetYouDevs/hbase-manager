<!DOCTYPE html>
<html>
<head>
	<#import "common/common.macro.ftl" as comm>
	<@comm.commonStyle />
	<title>HBase Manager-快照任务日志</title>
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
		<hr>
		<!-- footer -->
	<@comm.commonFooter />
</div>
</body>

<@comm.commonScript />
<script type="text/javascript">
    $(function(){
        $("#tb_table").bootstrapTable({
            data : ${snapshotTaskLogList},
            striped : false,		//表格显示条纹
            pagination : true,  	//是否分页
            pageSize : 10,      	//单页记录数
            pageNumber : 1,
            pageList : "[10, 20, 50, 100, All]",
            search : true, 			//显示搜索框
            columns : [
                {
                    field : 'id',
                    title : 'id',
                    align : 'center',
                    valign : 'middle',
                    sortable : true
                }, {
                    field : 'snapshotTaskId',
                    title : '快照任务ID',
                    align : 'center',
                    valign : 'middle',
                    sortable : true
                }, {
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
                    field : 'snapshotName',
                    title : '快照名次',
                    align : 'center',
                    valign : 'middle',
                    sortable : true
                }, {
                    field : 'suffix',
                    title : '后缀',
                    align : 'center',
                    valign : 'middle',
                    sortable : true
                }, {
                    field : 'fireTime',
                    title : '调度时间',
                    align : 'center',
                    valign : 'middle',
                    sortable : true,
                    formatter:function (value,row,index) {
                        return  new Date(parseInt(value)).Format('yyyy-MM-dd hh:mm:ss');;
                    }
                }, {
                    field : 'startTime',
                    title : '开始时间',
                    align : 'center',
                    valign : 'middle',
                    sortable : true,
                    formatter:function (value,row,index) {
                        return  new Date(parseInt(value)).Format('yyyy-MM-dd hh:mm:ss');;
                    }
                }, {
                    field : 'endTime',
                    title : '结束时间',
                    align : 'center',
                    valign : 'middle',
                    sortable : true,
                    formatter:function (value,row,index) {
                        return  new Date(parseInt(value)).Format('yyyy-MM-dd hh:mm:ss');;
                    }
                }, {
                    field : 'createTime',
                    title : '记录时间',
                    align : 'center',
                    valign : 'middle',
                    sortable : true,
					formatter:function (value,row,index) {
						return  new Date(parseInt(value)).Format('yyyy-MM-dd hh:mm:ss');;
                    }
                },{
                    field : 'hasDeleted',
                    title : '快照是否已清理',
                    align : 'center',
                    valign : 'middle',
                    sortable : true,
                    formatter:function (value,row,index) {
                        //0-否 1-是
                        var text="-";
                        if(value==0){
                            text="否";
                        }else if(value==1){
                            text="是";
                        }
                        return text;
                    }
                },{
                    field : '',
                    title : '操作',
                    align : 'center',
                    valign : 'middle',
                    formatter : function(value, row, index) {
                        var text = "-"
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
            $.post("/snapshot/delete", {"clusterName": "${clusterName}","name": snapshotName}, function(result)
            {
                $('#delete-replication-task-modal').modal('hide');
                window.location.reload();
            });
            $.ajaxSettings.async = true;
        });
    });
</script>
</html>
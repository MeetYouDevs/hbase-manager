<!DOCTYPE html>
<html>
<head>
  	<#import "common/common.macro.ftl" as comm>
    <@comm.commonStyle />
    <title>HBase Manager</title>
</head>
<div class="wrapper">
	<@comm.commonHeader />
    <div class="container-fluid">
        <div class="col-md-6 un-pad-me">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3>Clusters</h3>
                    <a href="/cluster/add" class="btn btn-default btn-sm">
                        <span class="glyphicon glyphicon-plus"></span> Add
                    </a>
                </div>
                <table id="tb_table"></table>
            </div>
        </div>
		<div class="modal fade" id="delete-cluster-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		    <div class="modal-dialog">
		        <div class="modal-content">
		            <div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title" id="myModalLabel">删除集群</h4>
		            </div>
		            <div class="modal-body">确认删除该集群?（删除后将导致集群的相关任务都失效）</div>
		            <div class="modal-footer">
		                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		                <input type="hidden" id="cluster-to-delete"/>
		                <button type="button" id="delete-cluster-modal-confirm-btn" class="btn btn-danger ops-button">确认</button>
		            </div>
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
		data : ${clusterList},
		striped : false,		//表格显示条纹
		uniqueId: "id",
		columns : [
			{
				field : 'clusterName',
				title : '集群名',
				align : 'left',
				valign : 'middle',
				sortable : true,
				formatter : function(value, row, index) {
					text = "<a href='/cluster/"+value+"'>"+value+"</a>";
					return text;
				}
			}, {
				field : '',
				title : '操作',
				align : 'left',
				valign : 'middle',
				sortable : true,
				formatter : function(value, row, index) {
					text = "<a href='/cluster/"+row.clusterName+"/edit' class='btn btn-default ops-button' role='button'>Modify</a>&nbsp&nbsp" + 
						"<button type='submit' value='"+row.id+"' class='btn btn-warning ops-button delete-cluster-btn'>Delete</button>";
					return text;
				}
			}, {
				field : 'version',
				title : '版本',
				align : 'left',
				valign : 'middle',
				sortable : true
			}
		]
	});
	//隐藏加载
	$("#tb_table").bootstrapTable('hideLoading');
	
    $(".delete-cluster-btn").click(function(){
        $("#cluster-to-delete").val($(this).val());
        $('#delete-cluster-modal').modal('show');
    });

    $("#delete-cluster-modal-confirm-btn").click(function(){
        $.ajaxSettings.async = false;
        $.post("/cluster/delete", {id: $("#cluster-to-delete").val()}, function(data)
        {
            $('#delete-cluster-modal').modal('hide');
            window.location.reload();
        });
        $.ajaxSettings.async = true;
    });
});
</script>
</html>
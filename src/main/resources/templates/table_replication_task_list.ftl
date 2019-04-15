<!DOCTYPE html>
<html>
<head>
  	<#import "common/common.macro.ftl" as comm>
    <@comm.commonStyle />
    <title>HBase Manager</title>
</head>
<div class="wrapper">
    <!-- header -->
	<@comm.commonHeader />
	<div class="col-md-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3>Replication Tasks</h3>
				<a href="#" id="add-replication-task-btn" class="btn btn-default btn-sm">
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
	<div class="modal fade" id="delete-replication-task-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<input type="hidden" id="replication-task-id-to-delete"/>
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="myModalLabel">删除表同步任务</h4>
	            </div>
	            <div class="modal-body">确认删除该表同步任务?</div>
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
	                <h4 class="modal-title" id="myModalLabel">表同步任务</h4>
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
		data : ${tableReplicationTaskList},
		striped : false,		//表格显示条纹
		pagination : true,  	//是否分页
	    pageSize : 10,      	//单页记录数
	    pageNumber : 1,
		pageList : "[10, 25, 50, 100, All]",
		search : true, 			//显示搜索框
		columns : [
			{
				field : 'sourceTable',
				title : '表名',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'sourceCluster',
				title : '源集群',
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
				field : '',
				title : '操作',
				align : 'center',
				valign : 'middle',
				sortable : true,
				formatter : function(value, row, index) {
					text = "<button type='button' value='"+row.id+"' class='btn btn-danger ops-button delete-replication-task-btn'>Delete</button>";
					return text;
				}
			}
		]
	});
	//隐藏加载
	$("#tb_table").bootstrapTable('hideLoading');
	
	// 增加同步任务
	$("#add-replication-task-btn").click(function () {
		var clusterSelect = $("#replication-source-cluster-select, #replication-target-cluster-select");
		clusterSelect.find("option").remove();
		//获取集群列表
	    $.ajaxSettings.async = false;
  		$.post("/api/cluster/list", function(data)
  		{
  			$.each(data, function (index, cluster) {
  				clusterSelect.append("<option value="+cluster.clusterName+">" + cluster.clusterName + "</option>");
  				clusterSelect.selectpicker('refresh');
  				clusterSelect.selectpicker('val', '');
  				clusterSelect.selectpicker({noneSelectedText:'请选择'});
            });
  			$('#add-replication-task-modal').modal('show');
  		}, "json");
  		$.ajaxSettings.async = true;
    });
	
	//源集群选择
	$("#replication-source-cluster-select").selectpicker({noneSelectedText:'请选择源集群'});
    //源表下拉列表
    $("#replication-source-cluster-select").bind("change", function (){
    	var sourceTableSelect = $("#replication-source-table-select");
        sourceTableSelect.find("option").remove();
        var cluster = $("option:selected",this).text();
        $.ajax({
            url: "/cluster/"+cluster+"/api/tables",
            dataType: "json",
            success: function (data) {
                $.each(data, function (index, table) {
                	sourceTableSelect.append("<option value="+table.tableName+">" + table.tableName + "</option>");
                	sourceTableSelect.selectpicker('refresh');
                	sourceTableSelect.selectpicker('val', '');
                	sourceTableSelect.selectpicker({noneSelectedText:'请选择'});
                });
            }
        });
    });
	
  	//目标集群选择
	$("#replication-target-cluster-select").selectpicker({noneSelectedText:'请选择目标集群'});
    //目标表下拉列表
    $("#replication-target-cluster-select").bind("change", function (){
    	var targetTableSelect = $("#replication-target-table-select");
    	targetTableSelect.find("option").remove();
        var cluster = $("option:selected", this).text();
        $.ajax({
            url: "/cluster/"+cluster+"/api/tables",
            dataType: "json",
            success: function (data) {
                $.each(data, function (index, table) {
                	targetTableSelect.append("<option value="+table.tableName+">" + table.tableName + "</option>");
                	targetTableSelect.selectpicker('refresh');
                	targetTableSelect.selectpicker('val', '');
                	targetTableSelect.selectpicker({noneSelectedText:'请选择'});
                });
            }
        });
    });
    
	$("#add-replication-task-modal-confirm-btn").click(function(){
		// 获取校验器
	    var bootstrapValidator = $('#replication-task-form').data('bootstrapValidator');
	    // 手动触发验证
	    bootstrapValidator.validate();
	    // 检验通过
	    if(bootstrapValidator.isValid()){
	    	var sourceCluster = $("#replication-source-cluster-select").val();
	    	var sourceTable = $("#replication-source-table-select").val();
	    	var targetCluster = $("#replication-target-cluster-select").val();
	    	var targetTable = $("#replication-target-table-select").val();
	  		$.ajaxSettings.async = false;
	  		$.post(
	  			"/table/replication/task/add", 
	  			{
	  				"sourceCluster": sourceCluster,
	  				"sourceTable": sourceTable,
	  				"targetCluster": targetCluster,
	  				"targetTable": targetTable
	  			}, function(data) {
	  				$('#add-replication-task-modal').modal('hide');
	  				window.location.reload();
	  			}
	  		);
	  		$.ajaxSettings.async = true;
	    }
  	});
	
	// 同步表单参数验证
	$('#replication-task-form').bootstrapValidator({
		live: 'submitted',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
        fields: {
        	sourceCluster: {
                validators: {
                    notEmpty: {
                        message: '源集群不能为空'
                    }
                }
            }, 
            sourceTable: {
                validators: {
                    notEmpty: {
                        message: '源表不能为空'
                    }
                }
            },
            targetCluster: {
                validators: {
                    notEmpty: {
                        message: '目标集群不能为空'
                    },
					callback: {
						message: '源集群和目标集群不能相同',
						callback: function(value, validator, $field) {
							var sourceCluster = $("#replication-source-cluster-select").val();
							var targetCluster = $("#replication-target-cluster-select").val();
							return sourceCluster!=targetCluster;
						}
					}
                }
            }, 
            targetTable: {
                validators: {
                    notEmpty: {
                        message: '目标表不能为空'
                    },
					callback: {
						message: '源表和目标表名必须相同',
						callback: function(value, validator, $field) {
							var sourceTable = $("#replication-source-table-select").val();
							var targetTable = $("#replication-target-table-select").val();
							return sourceTable==targetTable;
						}
					}
                }
            }
        }
    });
	
	// 删除同步任务
	$("#tb_table").delegate('.delete-replication-task-btn', 'click', function () {
		$("#replication-task-id-to-delete").val($(this).val());
		$('#delete-replication-task-modal').modal('show');
    });
	$("#delete-replication-task-modal-confirm-btn").click(function(){
		var id = $("#replication-task-id-to-delete").val();
  		$.ajaxSettings.async = false;
  		$.post("/table/replication/task/delete", {"id": id}, function(data)
  		{
  			$('#delete-replication-task-modal').modal('hide');
  			window.location.reload();
  		});
  		$.ajaxSettings.async = true;
  	});
	
});
</script>
</html>
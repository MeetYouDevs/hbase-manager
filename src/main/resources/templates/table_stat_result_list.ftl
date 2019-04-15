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
	<div class="container-fluid" role="main">
		<ol class="breadcrumb">
			<li><a href="/cluster">Clusters</a></li>
			<li><a href="/cluster/${clusterName}">${clusterName}</a></li>
			<li>Stat Results</li>
		</ol>
	</div>
	<div class="col-md-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3>Stat Results</h3>
				<p class="help-block">提示：统计任务异步执行, 统计大表需要一定时间, 请耐心等待</p>
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
    <hr>
    <!-- footer -->
	<@comm.commonFooter />
</div>
</body>
<@comm.commonScript />
<script type="text/javascript">
$(function(){
	$("#tb_table").bootstrapTable({
		data : ${tableStatResultList},
		striped : false,		//表格显示条纹
		pagination : true,  	//是否分页
	    pageSize : 10,      	//单页记录数
	    pageNumber : 1,
		pageList : "[10, 25, 50, 100, All]",
		search : true, 			//显示搜索框
		columns : [
			{
				field : 'tableName',
				title : '表名',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'columnFamily',
				title : '列族',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'maxRowSize',
				title : '最大行的字节数',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'biggestRowKey',
				title : '最大行的rowkey',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'tableRegionServerUrl',
				title : '表分区url',
				align : 'center',
				valign : 'middle',
				sortable : true,
				width : 500,
				formatter : function(value, row, index) {
					text = "<a target='_blank' href='"+value+"'>"+value+"</a>";
					return text;
				}
			}
		]
	});
	//隐藏加载
	$("#tb_table").bootstrapTable('hideLoading');
	
});
</script>
</html>
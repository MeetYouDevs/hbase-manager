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
					<input type="hidden" id="ipt_maxId" value="${maxId!}"/>
					<table id="tb_table"></table>
					<#--<table class="table table-bordered table-striped table-hover">-->
						<#--<thead>-->
						<#--<tr>-->
							<#--<th>id</th>-->
							<#--<th>开始时间</th>-->
							<#--<th>日志</th>-->
						<#--</tr>-->
						<#--</thead>-->
						<#--<tbody id="data_body">-->
						<#--</tbody>-->
					<#--</table>-->
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
        var CUR_MAX = ${maxId!-1};
        $("#tb_table").bootstrapTable({
            data : ${detailLogList},
            striped : false,		//表格显示条纹
            pagination : false,  	//是否分页
            search : true, 			//显示搜索框
            columns : [
                {
                    field : 'id',
                    title : 'ID',
                    align : 'center',
                    valign : 'middle',
                    sortable : true,
                    width:'5%'
                },{
                    field : 'createTime',
                    title : '记录时间',
                    align : 'center',
                    valign : 'middle',
                    width:'10%',
                    sortable : true,
                    formatter:function (value,row,index) {
                        return  new Date(parseInt(value)).Format('yyyy-MM-dd hh:mm:ss');;
                    }
                }, {
                    field : 'content',
                    title : '日志',
                    align : 'left'
                }
            ]
        });
        //隐藏加载
        $("#tb_table").bootstrapTable('hideLoading');
        function reflashLog(id){
            $.ajax({
                url: "/mig/task/detail/log/"+id+"/append?maxId="+CUR_MAX,
                success: function (resp) {
                    var dataObj= JSON.parse(resp.data);
                    for ( var i = 0; i < dataObj.length; i++){
                        if(dataObj[i].id > CUR_MAX) CUR_MAX = dataObj[i].id;
                    }
                   if(dataObj.length > 0){
                       $("#tb_table").bootstrapTable('append', dataObj);
				   }
                }
            });
        }
        //定时器每秒调用一次
        setInterval(function(){
            // var max = $("#ipt_maxId").val();
            reflashLog(${logId},CUR_MAX);
            },5000);

    });
</script>
</html>
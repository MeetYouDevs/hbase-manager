<!DOCTYPE html>
<html>
<head>
  	<#import "common/common.macro.ftl" as comm>
    <@comm.commonStyle />
    <title>Hbase Manager-表结构</title>
</head>
<div class="wrapper">
    <!-- header -->
	<@comm.commonHeader />
	<div class="container-fluid" role="main">
		<ol class="breadcrumb">
			<li><a href="/cluster">Clusters</a></li>
			<li><a href="/cluster/${clusterName}">${clusterName}</a></li>
			<li>Hbase表结构</li>
		</ol>
	</div>
	<div class="container-fluid" role="main">
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-body">
                    <div id="toolbar">
                        <div class="btn-group">
                            <button class="btn btn-default" onclick="addOrModifyColumn(0)">
                                <span class="glyphicon glyphicon-plus">新增</span>
                            </button>
                            <button class="btn btn-default" onclick="addOrModifyColumn(1)">
                                <i class="glyphicon glyphicon-pencil">修改</i>
                            </button>
                            <button class="btn btn-default">
                            <i class="glyphicon glyphicon-trash" onclick="delColumn()">删除</i>
                            </button>
                        </div>
                    </div>
					<table id="tb_table"></table>
				</div>
			</div>
		</div>
		<div class="modal fade" id="delete-table-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<input type="hidden" id="table-to-delete"/>
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">删除表</h4>
					</div>
					<div class="modal-body">确认删除该表?</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" id="delete-table-modal-confirm-btn" class="btn btn-danger ops-button">确认</button>
					</div>
				</div>
			</div>
		</div>
    </div>

    <hr>
    <!-- footer -->
	<@comm.commonFooter />
</div>
<!-- 模态框（Modal） -->
<div class="modal fade" id="add-table-schema" tabindex="-1" role="dialog" aria-labelledby="add-table-schema-label" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title" id="add-table-schema-label">
                </h4>
            </div>
            <div class="modal-body" id="add-table-schema-body">
                <form class="form-horizontal" role="form" action="/schema/${clusterName}/${tableName}/add">
                    <input type="text" class="form-control" id="id" name="id" value="" hidden>
                    <div class="form-group">
                        <label for="clusterName" class="col-sm-3 control-label">集群名称</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" id="clusterName" name="clusterName" value="${clusterName}" disabled>
                            <p class="help-block"></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="tableName" class="col-sm-3 control-label">表名</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" id="tableName" name="tableName"  value="${tableName}" disabled>
                            <p class="help-block"></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="columnName" class="col-sm-3 control-label">字段名称</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" id="columnName" name="columnName"  placeholder="请输入字段名称">
                            <p class="help-block"></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="nameType" class="col-sm-3 control-label">名称类型</label>
                        <div class="col-sm-5">
                            <label class="radio-inline">
                                <input type="radio" value="0" name="nameType">
                                全称
                            </label>
                            <label class="radio-inline">
                                <input type="radio" value="1" name="nameType">
                                前缀
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="dataType" class="col-sm-3 control-label">字段类型</label>
                        <div class="col-sm-5">
                            <select class="form-control input-xlarge" name="dataType" id="dataType">
                                <option value="string">string</option>
                                <option value="long">long</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            关闭
                        </button>
                        <button type="submit" class="btn btn-primary">
                            提交
                        </button>
                    </div>
                </form>
            </div>

        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<!-- 信息删除确认 -->
<div class="modal fade" id="delcfmModel">
    <div class="modal-dialog">
        <div class="modal-content message_align">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                <h4 class="modal-title">提示信息</h4>
            </div>
            <div class="modal-body">
                <p>您确认要删除吗？</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <a onclick="urlSubmit()" class="btn btn-success" data-dismiss="modal">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

</body>
<@comm.commonScript />

<script type="text/javascript">
    function delColumn() {

        var rows =$("#tb_table").bootstrapTable("getSelections"); //获取所选中的行
        if(!rows||rows.length==0){
            alert("请选择要删除的字段");
            return ;
        }
        var ids="";
        $(rows).each(function(index,row){
            ids= ids +","+row.id;
        });
        ids = ids.substr(1)

        var mymessage=confirm("确认删除? ["+ids+"]");

        if(mymessage==true){
            window.location.href = "/schema/${clusterName}/${tableName}/del/"+ids;
        }
    }

    function addOrModifyColumn(flag){
        if(flag==0){//新增
            $("#add-table-schema-label").html("新增字段");
            $("#columnName").val("");
            $("#dataType").val("");
            $("#id").val("");
            $('input:radio:first').attr('checked', 'true');
        }else{//修改
            $("#add-table-schema-label").html("修改字段");
            var rows =$("#tb_table").bootstrapTable("getSelections"); //获取所选中的行
            var row = rows[0]; //选中的第一行
            if(!row){
                alert("请选择要修改的字段");
                return ;
            }
            //
            $("#columnName").val(row.columnName);
            $("#dataType").val(row.dataType);
            $("#nameType").val(row.nameType);
            $("#id").val(row.id);
            $("input[value="+ row.nameType+"]").attr('checked','true');
        }

        // add-table-schema-body
        $('#add-table-schema').modal('show');
    }
$(function(){
    $("#tb_table").bootstrapTable('destroy').bootstrapTable({ // 对应table标签的id
        url: "/schema/${clusterName}/${tableName}/list", // 获取表格数据的url
        toolbar: '#toolbar',
        showRefresh: true,
        detailView: false, //父子表
        clickToSelect: true,
        cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
        striped: true,  //表格显示条纹，默认为false
        pagination: true, // 在表格底部显示分页组件，默认false
        pageList: [5, 10, 20], // 设置页面可以显示的数据条数
        pageSize: 10, // 页面数据条数
        pageNumber: 1, // 首页页码
        sidePagination: 'client', // 设置为服务器端分页
        queryParams: function (params) { // 请求服务器数据时发送的参数，可以在这里添加额外的查询参数，返回false则终止请求
            return {
                pageSize: params.limit, // 每页要显示的数据条数
                offset: params.offset, // 每页显示数据的开始行号
                sort: params.sort, // 要排序的字段
                sortOrder: params.order // 排序规则
            }
        },
        sortName: 'id', // 要排序的字段
        sortOrder: 'asc', // 排序规则
        columns: [{
            checkbox: true
        },{
            title: "序号",
            align: 'center', // 居中显示
            formatter:function (value,row,index) {
                return index + 1;
            }
        }, {
            field: 'clusterName',
            title: '集群名称',
            align: 'center',
            valign: 'left',
            formatter:function (value,row,index) {
                return "<a href=\"/cluster/${clusterName}\" class=\"btn btn-xs\">"+value+"</a>";
            }
        }, {
            field: 'tableName',
            title: '表名',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'columnName',
            title: '字段名称',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'dataType',
            title: '字段类型',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'nameType',
            title: '字段名形式',
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) { // 单元格格式化函数
                var text = "-";
				if(value==0){
                    text="全称";
				}else if(value==1){
                    text="前缀";
				}
				return text;
            }
        }
        // , {
        //     title: "操作",
        //     align: 'center',
        //     valign: 'middle',
        //     width: 160, // 定义列的宽度，单位为像素px
        //     formatter: function (value, row, index){
        //         return "<button type='button' class='btn btn-warning btn-xs' onclick='addOrModifyColumn(null)'>修改</button><button type='button' class='btn btn-danger btn-xs'>删除</button>";
        //     }
        // }
        ]
    });

    $('#add-table-schema').modal('hide');


});
</script>
</html>
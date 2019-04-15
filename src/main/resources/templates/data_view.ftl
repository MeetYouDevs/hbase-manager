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

    <!-- content begin -->
    <div class="container-fluid">
        <div class="panel panel-default">
            <div class="panel-heading">
            </div>
            <div class="panel-body">
                <form class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="clusterSelect" class="col-sm-1 control-label">集群</label>
                        <div class="col-sm-2">
                            <select id="clusterSelect" class="selectpicker show-tick form-control" data-live-search="true">
                            </select>
                        </div>
                        <label for="tableSelect" class="col-sm-1 control-label">表名</label>
                        <div class="col-sm-3">
                            <select id="tableSelect" class="selectpicker show-tick form-control" data-live-search="true">
                            </select>
                        </div>
                        <#--<label class="col-sm-1 control-label">offset</label>-->
                        <#--<div class="col-sm-2">-->
                            <#--<label class="radio-inline">-->
                                <#--<input type="radio" name="offset"  value="latest" checked>latest-->
                            <#--</label>-->
                            <#--<label class="radio-inline">-->
                                <#--<input type="radio" name="offset"  value="earliest"> earliest-->
                            <#--</label>-->
                        <#--</div>-->

                        <label for="limit" class="col-sm-1 control-label">记录条数</label>
                        <div class="col-sm-1">
                            <input type="text" name="limit" class="form-control" id="limit" placeholder="记录条数" value="10">
                        </div>

                        <label class="col-sm-1 control-label"></label>
                    </div>
                    <hr>
                    <#--<div class="form-group" >-->
                        <#--<label  class="col-sm-1 control-label">列簇信息</label>-->
                        <#--<div class="col-sm-11" id="family">-->

                        <#--</div>-->
                    <#--</div>-->
                    <#--<hr>-->
                    <div class="form-group">
                        <label for="rowKey" class="col-sm-1 control-label">rowKey</label>
                        <div class="col-sm-3">
                            <input type="text" class="form-control" name="rowKey" placeholder="rowkey">
                        </div>

                        <div class="col-sm-offset-1 col-sm-4">
                            <button type="button" class="btn btn-primary" id="search">Search</button>
                            <#--<button type="button" class="btn btn-primary" id="clear">清屏</button>-->
                        </div>
                    </div>
                </form>
            </div>
        </div>
    <div class="panel panel-default" id="list" style="min-height:520px;">
        <table class="table table-bordered table-striped table-hover">
            <thead>
            <tr>
                <th>rowkey</th>
                <th>column</th>
                <th>timestamp</th>
                <th>value</th>
            </tr>
            </thead>
            <tbody id="data_body">
            <#--<tr>-->
                <#--<td width="15%">000005a0_2_28788657</td>-->
                <#--<td width="15%">c2:ttP</td>-->
                <#--<td width="15%">1540324509271</td>-->
                <#--<td width="15%">54:0.9403</td>-->
            <#--</tr>-->
            <#--<tr>-->
                <#--<td width="15%">000005a0_2_28788657</td>-->
                <#--<td width="15%">c2:ttP</td>-->
                <#--<td width="15%">1540324509271</td>-->
                <#--<td width="15%">54:0.9403</td>-->
            <#--</tr>-->
            <#--<tr>-->
                <#--<td width="15%">000005a0_2_28788657</td>-->
                <#--<td width="15%">c2:ttP</td>-->
                <#--<td width="15%">1540324509271</td>-->
                <#--<td width="15%">54:0.9403</td>-->
            <#--</tr>-->
            <#--<tr>-->
                <#--<td width="15%">000005a0_2_28788657</td>-->
                <#--<td width="15%">c2:ttP</td>-->
                <#--<td width="15%">1540324509271</td>-->
                <#--<td width="15%">54:0.9403</td>-->
            <#--</tr>-->
            </tbody>
        </table>
        <#--<div class="panel-heading">-->
            <#--<h3 class="panel-title">Show records : <span class="label label-default" id="showCnt">0</span></h3>-->
        <#--</div>-->
    </div>
    </div>
    <!-- content end -->

    <hr>
    <!-- footer -->
	<@comm.commonFooter />
</div>
<@comm.commonScript />


<script type="text/javascript">




$(function(){
    //初始化 集群
    $.ajax({
        url: "/api/cluster/list",
        dataType: "json",
        success: function (data) {
            $.each(data, function (index, cluster) {
                $("#clusterSelect").append("<option value="+cluster.clusterName+">" + cluster.clusterName + "</option>");
                $('#clusterSelect').selectpicker('refresh');
                $('#clusterSelect').selectpicker('val', '');
                $("#clusterSelect").selectpicker({noneSelectedText:'请选择'});
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("error");
        }
    });

    $("#tableSelect").selectpicker({noneSelectedText:'请选择'});
    //表名下拉列表
    $("#clusterSelect").bind("change", function (){
        //$("#tableSelect").find("option:not(:first)").remove();
        $("#tableSelect").find("option").remove();
        var cluster = $("option:selected",this).text();
        $.ajax({
            url: "/cluster/"+cluster+"/api/tables",
            dataType: "json",
            success: function (data) {
                $.each(data, function (index, table) {
                    $("#tableSelect").append("<option value="+table.tableName+">" + table.tableName + "</option>");
                    $('#tableSelect').selectpicker('refresh');
                    $('#tableSelect').selectpicker('val', '');
                    $("#tableSelect").selectpicker({noneSelectedText:'请选择'});
                });
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert("hbase-cluster-table error");
            }
        });
    });

    // $("#tableSelect").bind("change", function (){
    //     var cluster = $("#clusterSelect").val();
    //     var table = $("option:selected",this).text();
    //     $.ajax({
    //         url: "/cluster/"+cluster+"/table/"+table+"/info.api",
    //         dataType: "json",
    //
    //         success: function (data) {
    //             $('#family').html("");
    //             $.each(data.families, function (rowkey, family){
    //                 $('#family').append("<label>"+family.familyName+"</label>");
    //             });
    //         },
    //         error: function (XMLHttpRequest, textStatus, errorThrown) {
    //             alert("hbase-cluster-table error");
    //         }
    //     });
    // });

});


$("#search").click(function(){
    var cluster = $('#clusterSelect option:selected').val(); //集群
    var table =  $('#tableSelect option:selected').val(); //表名
    var limit =  $('input[name="limit"]').val();//limit
    var rowKey = $('input[name="rowKey"]').val();  // "000005a0_2_28788657";

    var dataMap = new Map();

    $("select[name^='family-'").each(function(){
        dataMap.set($(this).attr("name").substring(7),$(this).val());
    });

    //Map转为Json的方法
    let obj= Object.create(null);
    for (let[k,v] of dataMap) {
        obj[k] = v;
    }

    $.ajax({
        url: "/data/"+cluster+"/query.api",
        dataType: "json",
        data: { table: table, rowKey: rowKey,limit:limit,family:JSON.stringify(obj)},
        success: function (data) {
            $('#data_body').html("");
            $.each(data, function (k, v) {
                $.each(v, function (rowkey, cell){
                    $('#data_body').prepend("<tr> <td width=\"30%\">"+cell.rowKey+"</td> <td width=\"15%\">"+cell.column+"</td> <td width=\"15%\">"+formatDateTime(cell.timestamp)+"</td> <td width=\"40%\">"+cell.value+"</td> </tr>");
                    });
                });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("hbase-cluster-table error");
        }
    });
});
</script>
</body>
</html>
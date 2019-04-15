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
            <li>${cluster.clusterName}</li>
            <li>Summary</li>
        </ol>
        <div class="col-md-6 un-pad-me">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3>Cluster Information</h3>
                </div>
                <table class="table">
                    <tbody>
                    <tr>
                        <td>Cluster Zookeeper Hosts</td>
                        <td>${cluster.zookeeperAddress}</td>
                    </tr>
                    <tr>
                        <td>Cluster Znode</td>
                        <td>${cluster.znodeParent}</td>
                    </tr>
                    <tr>
                        <td>HBase Version</td>
                        <td>${cluster.version}</td>
                    </tr>
                    <tr>
                        <td>HDFS Root Path</td>
                        <td>${cluster.hdfsRootPath}</td>
                    </tr>
                    <tr>
                        <td>Web Console Url</td>
                        <td><a target="_blank" href="${cluster.webConsoleUrl}">${cluster.webConsoleUrl}</a></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3>Cluster Summary</h3>
                </div>
                <table class="table">
                    <tbody>
                    <tr>
                        <td>Tables</td>
                        <td><a class="table-list-href-class" href="/cluster/${cluster.clusterName}/table/list">${cluster.tableCount}</a></td>
                    </tr>
                    </tbody>
                </table>
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
    // 表单验证
    $('#cluster-add-form').bootstrapValidator({
        live: 'submitted',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            clusterName: {
                validators: {
                    notEmpty: {
                        message: '集群名不能为空'
                    }
                }
            },
            zookeeperAddress: {
                validators: {
                    notEmpty: {
                        message: 'zk地址不能为空'
                    }
                }
            },
            znodeParent: {
                validators: {
                    notEmpty: {
                        message: 'zk节点不能为空'
                    }
                }
            },
            version: {
                validators: {
                    notEmpty: {
                        message: '版本不能为空'
                    }
                }
            }
        }
    });
</script>
</html>
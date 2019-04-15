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
            <li>Add Cluster</li>
        </ol>
    </div>
    <div class="container-fluid">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3>
                    <button type="button" class="btn btn-link" onclick="goBack()">
                        <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>
                    </button>
                    Add Cluster
                </h3>
            </div>
            <div class="panel-body">
                <div class="col-md-6 un-pad-me">
                    <form id="cluster-add-form" action="/cluster/save" method="POST" class="form-vertical " role="form">
                        <input value="" name="id" hidden="true" >
                        <fieldset>
                            <div class="form-group">
                                <label class="control-label">Cluster Name</label>
                                <input type="text" name="clusterName" class="form-control" size="4">
                            </div>

                            <div class="form-group">
                                <label class="control-label">Cluster Zookeeper Hosts</label>
                                <input type="text" name="zookeeperAddress" value="" class="form-control" placeholder="zk1:2181,zk2:2181,zk3:2181">
                            </div>

                            <div class="form-group">
                                <label class="control-label">Cluster Znode</label>
                                <input type="text" name="znodeParent" value="" class="form-control" placeholder="/hbase">
                            </div>

                            <div class="form-group">
                                <label class="control-label" for="version">HBase Version</label>
                                <input type="text" name="version" value="" class="form-control" >
                            </div>

                            <div class="form-group">
                                <label class="control-label" for="version">HDFS Root Path</label>
                                <input type="text" name="hdfsRootPath" value="" class="form-control" placeholder="hdfs://namenode:8020/hbase">
                            </div>

                            <div class="form-group">
                                <label class="control-label" for="version">Web Console Url</label>
                                <input type="text" name="webConsoleUrl" value="" class="form-control" placeholder="http://master:60011/master-status">
                            </div>

                            <div class="form-group">
                                <button type="submit" class="submit-button btn btn-primary btn">Save</button>
                                <a href="/cluster" class="cancel-button btn btn-default" role="button">Cancel</a>
                            </div>
                        </fieldset>
                    </form>
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
</html>
<script type="text/javascript">
    function goBack() {
        window.history.back()
    }

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
            },
            hdfsRootPath: {
                validators: {
                    notEmpty: {
                        message: 'hdfs根路径不能为空'
                    }
                }
            },
            webConsoleUrl: {
                validators: {
                    notEmpty: {
                        message: 'web控制台地址不能为空'
                    }
                }
            }
        }
    });
</script>

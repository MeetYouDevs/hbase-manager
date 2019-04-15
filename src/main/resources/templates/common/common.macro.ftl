<#macro commonStyle>

    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <link rel="shortcut icon" href="${request.contextPath}/favicon.ico"/>
    <link rel="bookmark" href="${request.contextPath}/favicon.ico"/>

    <link rel="stylesheet" href="${request.contextPath}/css/bootstrap.min.css">
    <!-- bootstrap-select -->
    <link rel="stylesheet" href="${request.contextPath}/css/bootstrap-select.min.css">
    <!--  -->
    <link rel="stylesheet" href="${request.contextPath}/css/jquery.steps.css">
	<link rel="stylesheet" href="/css/bootstrapValidator.min.css">
    <link rel="stylesheet" href="/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="/css/login.css">
</#macro>

<#macro commonScript>
    <script src="${request.contextPath}/js/jquery.min.js"></script>
    <script src="${request.contextPath}/js/bootstrap.min.js"></script>
    <!-- bootstrap-select -->
    <script src="${request.contextPath}/js/bootstrap-select.min.js"></script>
    <script src="${request.contextPath}/js/bootstrap-table.min.js"></script>
	<script src="/js/bootstrapValidator.min.js"></script>
	
    <!-- steps -->
    <script src="${request.contextPath}/js/jquery.steps.min.js"></script>
    <script src="${request.contextPath}/js/jquery.form.js" type="text/javascript"></script>

    <!--datetimepicker-->
    <script src="${request.contextPath}/js/bootstrap-datetimepicker.js"></script>
    <script src="${request.contextPath}/js/bootstrap-datetimepicker-zh-CN.js"></script>
    <script src="${request.contextPath}/js/common.js"></script>
</#macro>

<#macro commonHeader>
<nav class="navbar navbar-default" role="navigation">
    <div class="container">
        <div class="container-fluid">
            <div class="navbar-header">
                <img src="/image/favicon.ico">
                <a class="navbar-brand" href="/">HBase Manager</a>
                <span class="label label-primary" <#if (!clusterName??)>style="display: none;"</#if>>
                    ${clusterName!!}
                </span>
            </div>
            <div>
                <ul class="nav navbar-nav">
                    <li class="head-menu-class <#if (nav??&&nav='cluster')>active</#if>">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">集群管理<span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="/cluster/add">Add Cluster</a></li>
                            <li <#if (!clusterName??)>style="display: none;"</#if>><a href="/cluster/${clusterName!!}">Summary</a></li>
                            <li><a href="/">List</a></li>
                        </ul>
                    </li>

                    <li class="head-menu-class <#if (nav??&&nav='table')>active</#if>" <#if (!clusterName??)>style="display: none;"</#if>>
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">表管理<span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="/cluster/${clusterName!!}/table/add">Create Table</a></li>
                            <li><a href="/cluster/${clusterName!!}/table/list">List</a></li>
                            <li><a href="/cluster/${clusterName!!}/table/compact/task/list">Compact Task</a></li>
                        </ul>
                    </li>
                    <li class="head-menu-class <#if (nav??&&nav='data')>active</#if>">
                        <a href="/data">数据预览</a>
                    </li>

                    <li class="head-menu-class <#if (nav??&&nav='mig')>active</#if>">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">数据迁移<span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="/mig/task/add">Add Migration</a></li>
                            <li><a href="/mig/task">Migration Task</a></li>
                            <li><a href="/mig/task/log">Migration Task Log</a></li>
                            <li><a href="/run/cluster">Run Cluster</a></li>
                        </ul>
                    </li>

                    <li class="head-menu-class <#if (nav??&&nav='replication')>active</#if>">
                        <a href="/table/replication/task/list">数据同步</a>
                    <li>
					
					<li class="head-menu-class <#if (nav??&&nav='tableStat')>active</#if>" <#if (!clusterName??)>style="display: none;"</#if>>
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">数据统计<span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="/cluster/${clusterName!!}/table/stat/task/list">Stat Task</a></li>
                            <li><a href="/cluster/${clusterName!!}/table/stat/result/list">Stat Result</a></li>
                        </ul>
                    </li>
                    <li class="head-menu-class <#if (nav??&&nav='snapshot')>active</#if>" <#if (!clusterName??)>style="display: none;"</#if>>
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">快照管理<span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="/cluster/${clusterName!!}/snapshot">Snapshot</a></li>
                            <li><a href="/cluster/${clusterName!!}/snapshot/task">Snapshot Task</a></li>
                            <li><a href="/cluster/${clusterName!!}/snapshot/task/log">Snapshot Task Log</a></li>
                        </ul>
                    </li>
                     
                    <li class="head-menu-class <#if (nav??&&nav='clean')>active</#if>">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">系统管理<span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                        	<li><a href="/sys/user">用户管理</a></li>
                        	<li><a href="/sys/role">角色管理</a></li>
                            <li><a href="/sys/permission">权限管理</a></li>
                            <li><a href="/logout">退出登录</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</nav>
</#macro>

<#macro commonFooter >
    <footer class="container">
        <p>Copyright &copy; 2015-${.now?string('yyyy')} &nbsp;Powered by <b>基础架构组</p>
    </footer>
</#macro>
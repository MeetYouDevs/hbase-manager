<!DOCTYPE html>
<html>
<head>
  	<#import "common/common.macro.ftl" as comm>
    <@comm.commonStyle />
    <title>HBase Manager</title>
</head>
<div class="wrapper">
	<@comm.commonHeader />
    <div class="container-fluid">>
		<div class="col-md-6 un-pad-me">
			<div class="panel panel-default">
				<div class="panel-heading"><h3>Add / Update Cluster</h3></div>
				<div class="alert alert-success" role="alert">Done!</div>
				<div class="alert alert-info" role="alert">
					<a href="/cluster">Go to cluster view.</a>
				</div>
			</div>
		</div>
    </div
	<hr>
	<!-- footer -->
	<@comm.commonFooter />
</div>
</body>
<@comm.commonScript />
</html>
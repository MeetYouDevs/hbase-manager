<!DOCTYPE html>
<html>
<head>
  	<#import "common/common.macro.ftl" as comm>
    <@comm.commonStyle />
    <title>HBase Manager-集群迁移</title>
</head>
<div class="wrapper">
    <!-- header -->
	<@comm.commonHeader />
    <div class="container-fluid" role="main">
        <ol class="breadcrumb">
            <li><a href="/cluster">Clusters</a></li>
            <li><a href="/cluster/${clusterName}">${clusterName}</a></li>
            <li><a href="/cluster/${clusterName}/table/list">Tables</a></li>
            <li>Create Table</li>
        </ol>
    </div>
    <div class="container-fluid">
        <div class="col-md-12 un-pad-me">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3>
				<button type="button" class="btn btn-link" onclick="goBack()">
					<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>
				</button>
				Add Table
			</h3>
		</div>
		<div class="panel-body">
			<div class="col-md-6 un-pad-me">
				<form id="table-add-form" class="form-horizontal" role="form" action="#">
					<input value="" name="id" hidden="true" >
					<div class="form-group">
		                <label for="user_id" class="col-sm-3 control-label">表名</label>
		                <div class="col-sm-9">
		                    <input type="text" class="form-control base-property-class" id="tableName" name="tableName" value=""
		                           placeholder="">
		                </div>
		            </div>
		            
		            <div class="form-group">
		                <label for="lastname" class="col-sm-3 control-label">命名空间</label>
		                <div class="col-sm-9">
		                    <input type="text" class="form-control base-property-class" id="namespace" name="namespace" value="" 
		                           placeholder="">
		                </div>
		            </div>
		            
		            <div class="form-group">
		                <label for="lastname" class="col-sm-3 control-label">预分区数</label>
		                <div class="col-sm-9">
		                    <input type="number" min="1" max="50" class="form-control base-property-class" id="numRegions" name="numRegions" value="1"
									placeholder="">
		                </div>
		            </div>
		            
		            <div class="form-group">
		            	<div class="col-sm-2">
		                    <button type="button" id="family-add-btn" class="btn btn-default btn-sm form-control">
		          				<span class="glyphicon glyphicon-plus"></span> Add Family
		        			</button>
		                </div>
		                <div class="col-sm-10"></div>
		            </div>
					
					<div class="family-add-template-class">
					
						<div class="form-group">
							<div class="col-sm-2">
								<button type="button" class="family-remove-btn btn btn-default btn-sm form-control">
									<span class="glyphicon glyphicon-minus"></span> Remove Family
								</button>
							</div>
							<div class="col-sm-10"></div>
						</div>
						
						<div class="form-group">
							<label for="lastname" class="col-sm-1 control-label">列族</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" id="familyName" name="familyName" value=""
									placeholder="">
							</div>
							
							<label for="lastname" class="col-sm-1 control-label">TTL</label>
							<div class="col-sm-3">
								<input type="number" min="0" class="form-control" id="timeToLive" name="timeToLive" value="2147483647"
									placeholder="保存的时间, 单位：s">
							</div>
							
							<label for="lastname" class="col-sm-1 control-label">Block大小</label>
							<div class="col-sm-3">
								<input type="number" min="0" class="form-control" id="blockSize" name="blockSize" value="65536"
									placeholder="块单位大小, 单位：byte">
							</div>
						</div>
						
						<div class="form-group">
							<label for="lastname" class="col-sm-1 control-label">压缩类型</label>
							<div class="col-sm-3">
								<select class="form-control" id="compressionType" name="compressionType">
									<option value="1">SNAPPY</option>
									<option value="2">LZO</option>
									<option value="3">GZ</option>
									<option value="4">LZ4</option>
									<option value="0">NONE</option>
								</select>     
							</div>
							
							<label for="lastname" class="col-sm-1 control-label">允许BlockCache</label>
							<div class="col-sm-3">
								<select class="form-control" id="blockCacheEnabled" name="blockCacheEnabled">
									<option value="1">是</option>
									<option value="0">否</option>
								</select>     
							</div>
							
							<label for="lastname" class="col-sm-1 control-label">允许Replication</label>
							<div class="col-sm-3">
								<select class="form-control" id="replicationScope" name="replicationScope">
									<option value="0">否</option>
									<option value="1">是</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="lastname" class="col-sm-1 control-label">最小版本</label>
							<div class="col-sm-3">
								<input type="number" min="0" class="form-control" id="minVersion" name="minVersion" value="0"
									placeholder="最小版本">
							</div>
							
							<label for="lastname" class="col-sm-1 control-label">最大版本</label>
							<div class="col-sm-3">
								<input type="number" min="0" class="form-control" id="maxVersion" name="maxVersion" value="1"
									placeholder="最大版本">
							</div>
							
							<label for="lastname" class="col-sm-1 control-label">布隆过滤器</label>
							<div class="col-sm-3">
								<select class="form-control" id="bloomFilterType" name="bloomFilterType" >
									<option value="1">ROW</option>
									<option value="2">ROWCOL</option>
									<option value="0">NONE</option>
								</select>     
							</div>
						</div>
					            
		            </div>
					
		            <div class="form-group">
		                <label for="desc" class="col-sm-3 control-label">备注</label>
		                <div class="col-sm-9">
		                    <textarea class="form-control base-property-class" id="desc" name="desc"
		                           placeholder="备注"></textarea>
		                </div>
		            </div>
					            
		            <div class="form-group">
		            	<div class="col-sm-9"></div>
		            	<div class="col-sm-3">
		                    <button type="button" class="submit-button btn btn-primary btn" onclick="add_table()">Save</button>
		                    &nbsp&nbsp&nbsp
							<button type="button" class="cancel-button btn btn-default" onclick="goBack()">Cancel</button>
		                </div>
					</div>
				</form>
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
    function goBack() {
        window.history.back()
    }

    // 添加列族
    $('#family-add-btn').on('click', function(){
        var $template = $('.family-add-template-class:last');
        var $clone = $template.clone();
        $clone.insertAfter($template);
    });

    // 删除列族
    $("#table-add-form").delegate('.family-remove-btn', 'click', function () {
        var template_size = $('.family-add-template-class').length;
        if(template_size<=1){
            return false;
        }
        var $row = $(this).parents('.family-add-template-class');
        $row.remove();
    });

    function add_table(){
        // 获取校验器
        var bootstrapValidator = $('#table-add-form').data('bootstrapValidator');
        // 手动触发验证
        bootstrapValidator.validate();
        // 检验通过
        if(bootstrapValidator.isValid()){
            var ddlStr = "{";
            $('.base-property-class').each(function(){
                var value = $(this).val().trim();
                if(value!=""){
                    ddlStr = ddlStr + "\"" + $(this).attr('name') + "\":\"" + value + "\",";
                }
            });
            ddlStr = ddlStr.substring(0, ddlStr.length - 1);
            ddlStr = ddlStr + ",\"families\":[{";
            var templates = $('.family-add-template-class');
            for(var i = 0;i < templates.length;i++){
                var temp = templates[i];
                $(temp).find('input').each(function(){
                    var name = $(this).attr('name');
                    var value = $(this).val().trim();
                    if(value!=""){
                        if(name!="familyName"){
                            ddlStr = ddlStr + "\"" + name + "\":" + value + ",";
                        }
                        else{
                            ddlStr = ddlStr + "\"" + name + "\":\"" + value + "\",";
                        }
                    }
                });
                $(temp).find('select').each(function(){
                    var value = $(this).val().trim();
                    if(value!=""){
                        ddlStr = ddlStr + "\"" + $(this).attr('name') + "\":\"" + value + "\",";
                    }
                });
                ddlStr = ddlStr.substring(0, ddlStr.length - 1);
                ddlStr = ddlStr + "},{";
            }
            ddlStr = ddlStr.substring(0, ddlStr.length - 2);
            ddlStr = ddlStr + "]}";

            $.ajaxSettings.async = false;
            $.post("/cluster/${clusterName}/table/save", {"ddlStr": ddlStr, "updateFlag": 0}, function(data)
            {
                if(data.code = 200){
                    alert(data.data);
                }else{
                	alert(data.message);
                }
                window.location.href = "./list";
            });
            $.ajaxSettings.async = true;
        }
    }

    // 表单验证
    $('#table-add-form').bootstrapValidator({
        live: 'submitted',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            tableName: {
                validators: {
                    notEmpty: {
                        message: '表名不能为空'
                    }
                }
            },
            namespace: {
                validators: {
                    notEmpty: {
                        message: '命名空间不能为空'
                    }
                }
            },
            familyName: {
                validators: {
                    notEmpty: {
                        message: '列族不能为空'
                    }
                }
            }
        }
    });

</script>
</html>
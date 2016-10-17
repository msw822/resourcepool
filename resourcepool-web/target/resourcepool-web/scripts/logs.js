// Licensed to the Apache Software Foundation (ASF) under one
(function($, cloudStack) {
    cloudStack.sections.logs = {
        title: '日志管理',
        id: 'logs',
        listView: {
            section: 'logs',
            firstClick : false,
            filters: {		// 过滤依据-下拉选项
            	all:{
                	preFilter : function(args){
                		if(isAdmin()){
                			return true;
                		}else{
                			return false;
                		}
                	},
                	label : '所有模块'
                },
                serviceApplication : {
                	label : '服务申请'
                },
                serviceApprove : {
                	label : '服务审批'
                },
                authManage : {
                	label : '用户认证'
                },
                createResource : {
                	label : '资源创建'
                },
                recycleResource : {
                	label : '资源回收'
                }
            },
            /*preFilter: function(args) {
                var hiddenFields = [];
                if (!isAdmin()) {
                    hiddenFields.push('instancename');
                }
                return hiddenFields;
            },*/
            fields: {
            	loginName: {
                    label: '用户'
                },
            	ip: {
                    label: '客户端IP'
                },
                operation: {
                    label: '操作'
                },
                content: {
                    label: '内容'
                },
                module: {
                    label: '模块'
                },
                result: {
                    label: '结果'
                },
                info : {
                	label : '信息'
                },
                createdOn:{
                	label : '日期',
                	converter: function(date){return cloudStack.converters.toCloudDate(date,"1");}
                }
            },
            advSearchFields: {
            	operation : {
                    label: '操作类型',
                    select: function(args) {
                        var items = [];
                        items.push({
                            id: "",
                            description: "所有类型"
                        });
                        items.push({id:"登录",description : '登录'});
                        //items.push({id:"成功",description : '成功'});
                        items.push({id:"创建",description : '创建'});
                        items.push({id:"修改",description : '修改'});
                        items.push({id:"删除",description : '删除'});
                        //items.push({id:"失败",description : '失败'});
                        items.push({id:"接口调用",description : '接口调用'});
                        args.response.success({
                            data: items
                        });
                    }
                },
                loginName: {
                    label: '用户',
                    isHidden: function(args) {
                        if (isAdmin() || isDomainAdmin())
                            return false;
                        else
                            return true;
                    }
                },
                startDate: {
                    label: '开始时间',
                    isDatepicker: true
                },
                endDate: {
                    label: '结束时间',
                    isDatepicker: true
                }
            },

           
            dataProvider: function(args) {
                var data = {
		                	cmsz: 'yes'
		                    //,response :'json'
		                	//admin 1
		                	//domainadmin 2
		                	//user 0
		                	//,role : g_role
                    	};
                listViewDataProvider(args, data);

                if (args.filterBy != null) { //filter dropdown
                    if (args.filterBy.kind != null) {
                    	var module = "";
                        switch (args.filterBy.kind) {
                            case "all":
                                break;
                            case 'serviceApplication':
                            	module = '服务申请';
                                break;
                            case 'serviceApprove':
                            	module ='服务审批';
                            	break;
                            case 'authManage':
                            	module = '用户认证';
                            	break;
                            case 'createResource':
                            	module = '资源创建';
                            	break;
                            case 'recycleResource':
                            	module = '资源回收';
                            	break;
                          /*  case 'userManage':
                            	module = '用户管理';
                            	break;
                            case 'hostManage':
                            	module = '主机池管理';
                            	break;
                            case 'ipManage':
                            	module = 'IP池管理';
                                break;
                            case 'templateManage':
                            	module = '模版管理';
                                break;
                            case 'roleManage':
                            	module = '角色管理';
                            	break;
                            case 'vmManage':
                            	module ='虚机管理';
                            	break;
                            case 'propertyManage':
                            	module ="资产管理";
                            	break;
                            case 'resourcePoolPermission':
                            	module ="资源池分配";
                            	break;*/
                        }
                        $.extend(data, {
                        	module: module
                        });
                    }
                }

                $.ajax({
                    url: createURL('listLog'),
                    data: data,
                    success: function(json) {
                    	//console.info(json.root.listLog);
                        var items = json.logs;
                        args.response.success({
                            data: items
                        });
                    }
                });
            }
        }
    };

})(jQuery, cloudStack);

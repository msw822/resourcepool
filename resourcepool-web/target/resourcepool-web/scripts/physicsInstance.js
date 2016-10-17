// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
(function($, cloudStack) {
    var vmMigrationHostObjs;
    cloudStack.sections.physicsInstances = {
        title: '物理机实例',
        id: 'physicsInstances',
        listView: {
        	disableInfiniteScrolling : true,
        
            filters: {
                all: {
                	preFilter: function(args) {
                        if (isAdmin() || isDomainAdmin())
                            return true;
                        else
                            return false;
                    },
                    label: 'ui.listView.filters.all'
                },
                mine: {
                    label: 'ui.listView.filters.mine'
                },
                unassignment: {
                	 preFilter: function(args) {
                         if (isAdmin() || isDomainAdmin())
                             return true;
                         else
                             return false;
                     },
                    label: '未分配'
                },
                assignmented: {
                	 preFilter: function(args) {
                         if (isAdmin() || isDomainAdmin())
                             return true;
                         else
                             return false;
                     },
                    label: '已分配'
                },
                virtualed: {
                    preFilter: function(args) {
                        if (isAdmin() || isDomainAdmin())
                            return true;
                        else
                            return false;
                    },
                    label: '已虚拟化'
                }
            },
            preFilter: function(args) {
                var hiddenFields = [];
               /* if (!isAdmin()) {
                    hiddenFields.push('instancename');
                }*/
                return hiddenFields;
            },
            fields: {
            	hostName: {
	                label: '机器名称'
	            },
	            /*
	            osCaption: {
	                label: 'OS类型'
	              
	            },
	            type: {
	                label: '机器类型'	                
	            },
	            */
	            cpuNumber:{
	            	label: 'cpu个数'
            		
	            },
	            physicsMemory: {
	                label: '内存(GB)' /*,
	                converter: function(memoryObj){
	                	return memoryObj.physicsMemory;
    				}*/
	            },
	            ipAddr: {
	                label: 'IP地址' /*,
	                converter: function(memoryObj){
	                	return memoryObj.physicsMemory;
    				}*/
	            },
	            ownerName: {
	                label: '拥有者' /*,
	                converter: function(memoryObj){
	                	return memoryObj.physicsMemory;
    				}*/
	            },
	            cpuStatus: {
	                label: '状态',
	                indicator: {
                        'on': 'on',
                        'off': 'off'
                    }
	            }
	        },
	        advSearchFields: {
            	hostName: {
               	 label : "机器名称"
               	
                },
                ipAddr:{
               	 label : "IP地址"
                },
                cpuStatus: {
               	 label : "状态",
               	 select: function(args) {
               		 
               		 var obj = [{id:"",description: '所有状态'}];               			 
        				obj.push({
        					 id:'on',
        					 description:'on'
        				 });
        				obj.push({
	       					 id:'off',
	       					 description:'off'
	       				 });	
               			 args.response.success({
	                             data: obj
               			 });
               		 }
               	 }
               
            },
            dataProvider: function(args) {            
            	console.info(args);
            	var data = {};            	
            	listViewDataProvider(args, data);     
                if (args.filterBy != null) { //filter dropdown
                    if (args.filterBy.kind != null) { 
                        switch (args.filterBy.kind) {
                            case "all":
                                break;
                            case "mine":
                                if (!args.context.projects) {                                	
//                                    $.extend(data, {
//                                        domainid: g_domainid,
//                                        userId: g_userid
//                                    });
                                	$.extend(data, {
                                    	hostTypeStatus: 'mine'
                                    });                                  
                                }
                                break;
                            case "unassignment":
                                $.extend(data, {
                                	hostTypeStatus: 'unassignment'
                                });
                              
                                break;
                            case "assignmented":
                                $.extend(data, {
                                	hostTypeStatus: 'assignmented'
                                });   
                                break;
                            case "virtualed":
                                $.extend(data, {
                                	hostTypeStatus: 'virtualed'
                                });
                                break;
                        }
                    }
                }
                          
            	if(args.filterBy.search != null && args.filterBy.search.by != null  && args.filterBy.search.value!=""){
            		$.extend(data,{"hostName":args.filterBy.search.value});
            	}
            	/*            	
                if(args.context.hpHostManagement!=null){//查看
                	alert(args.context.hpHostManagement[0].hostName);
                	$.extend(data,{"hostName":args.context.hpHostManagement[0].hostName});
                }*/
            	
            	 $.ajax({
            		 url: createURL('listPhysicsHost&cmsz=yes&page=1&pagesize=10000'),
                     data: data,
                     async: true,
                     success: function(json) {                        
                         args.response.success({
                             actionFilter: hostActionfilter,
                             data: json.physicsHosts
                         });
                     }
                 });
            	 
                
            },            
            actions: {/*
                add: {
                	preFilter: function(args) {
                         if (isAdmin() || isDomainAdmin())
                             return true;
                         else
                             return false;
                    },
                    label: '添加实例',

                    messages: {
                        notification: function(args) {
                            return 'label.add.affinity.group';
                        }
                    },

                    createForm: {
                        title: '添加物理机实例',
                        fields: {
                            name: {
                                label: 'label.name',
                                //validation: {
                                //    required: true
                                //}
			                    select: function(args) {
			                        $.ajax({
			                            url: createURL('listUsers'),
			                            success: function(json) {
			                                var types = [];
			                                var items = json.listusersresponse.user;
			                                if (items != null) {
			                                    for (var i = 0; i < items.length; i++) {
			                                        types.push({
			                                            id: items[i].id,
			                                            description: "帐号："+items[i].account+" 姓名："+items[i].lastname+items[i].firstname
			                                        });
			                                    }
			                                }
			                                args.response.success({
			                                    data: types
			                                })
			                            }
			                        });
			                    }
                            },
                            type: {
                                label: 'label.type',
                                select: function(args) {
			                        $.ajax({
			                            url: createURL('listUsers'),
			                            success: function(json) {
			                                var types = [];
			                                var items = json.listusersresponse.user;
			                                if (items != null) {
			                                    for (var i = 0; i < items.length; i++) {
			                                        types.push({
			                                            id: items[i].id,
			                                            description: "帐号："+items[i].account+" 姓名："+items[i].username
			                                        });
			                                    }
			                                }
			                                args.response.success({
			                                    data: types
			                                })
			                            }
			                        });
			                    }
                            },
                            description: {
                                label: 'label.description'
                            }
                          
                        }
                    },

                    action: function(args) {
                        var data = {
                            name: args.data.name,
                            type: args.data.type
                        };
                        if (args.data.description != null && args.data.description.length > 0)
                            $.extend(data, {
                                description: args.data.description
                            });

                        $.ajax({
                            url: createURL('createAffinityGroup'),
                            data: data,
                            success: function(json) {
                                var jid = json.createaffinitygroupresponse.jobid;
                                args.response.success({
                                    _custom: {
                                        jobId: jid,
                                        getUpdatedItem: function(json) {
                                            return json.queryasyncjobresultresponse.jobresult.affinitygroup;
                                        }
                                    }
                                });
                            }
                        });
                    },

                    notification: {
                        poll: pollAsyncJobResult
                    }
                } */
            },
            detailView: {
                name: '实例详情',
                isMaximized: true,
              
                /*viewAll: {  
                    path: 'physicsInstances',
                    label: '主机'
                   
                }, */
                actions: {
                	 doStart: {                		
                         label: '启动',
                         messages: {
                             confirm: function(args) {
                                 return 'message.update.resource.count';
                             },
                             notification: function(args) {
                                 return 'label.action.update.resource.count';
                             }
                         },
                         action: function(args) {
                             var accountObj = args.context.accounts[0];
                             var data = {
                                 domainid: accountObj.domainid,
                                 account: accountObj.name
                             };

                           
                         },
                         notification: {
                             poll: function(args) {
                                 args.complete();
                             }
                         }
                     
                	 },
                	 doStop:{                		
                         label: '停止',
                         messages: {
                             confirm: function(args) {
                                 return 'message.disable.account';
                             },
                             notification: function(args) {
                                 return 'label.action.disable.account';
                             }
                         },
                         action: function(args) {
                             var accountObj = args.context.accounts[0];
                             var data = {
                                 lock: false,
                                 domainid: accountObj.domainid,
                                 account: accountObj.name
                             };

//                             $.ajax({
//                                 url: createURL('disableAccount'),
//                                 data: data,
//                                 async: true,
//                                 success: function(json) {
//                                     var jid = json.disableaccountresponse.jobid;
//                                     args.response.success({
//                                         _custom: {
//                                             jobId: jid,
//                                             getUpdatedItem: function(json) {
//                                                 return json.queryasyncjobresultresponse.jobresult.account;
//                                             },
//                                             getActionFilter: function() {
//                                                 return accountActionfilter;
//                                             }
//                                         }
//                                     });
//                                 }
//                             });
                         },
                         notification: {
                             poll: pollAsyncJobResult
                         }
                     
                	 }
                	
                },
                tabs: {
                    details: {
                        title: '实例详情',
                        fields:[{
                           hostName: {
      			                label: '名称'
      			            },
      			           osCaption: {
      			                label: 'OS类型'
      			            },
      			           cpuNumber:{
      			            	label: 'cpu个数'
      			            	
      			            },
      			            physicsMemory: {
      			            	label: '内存(GB)'
      			            },
      			            ipAddr:{label : "ip"},
      			            
      			        /*
      			          storage:{
      			        	label:"物理驱动",
      			        	  converter: function(storage){
  			                	return storage.device[0].physical_device;
  		    				  }
      			          },
      			         storage:{
      			        	label:"物理存储",
    			        	  converter: function(storage){
			                	return storage.device[0].physical_storage;
		    				  }
    			          },*/
      			         cpuStatus: {
      		                label: '是否可运行',
      		                indicator: {
      	                        'on': 'on',
      	                        'off': 'off'
      	                    }
      		             } 
      			         
                    }],
                    dataProvider: function(args) {                    	
                    	var data = {
    		                	cmsz: 'yes',
    		                    query:true,
    		                    hostname : args.context.physicsInstances[0].hostName,
    		                    type : args.context.physicsInstances[0].type
                        	};
                    	//console.info(data);
                        $.ajax({
                        	url: createURL("physicsHostInfo"),
                            dataType: "json",
                            async: true,
                            data:data,
                            success: function(json) {                              
                            	console.info(json);
                                args.response.success({
                                	actionFilter: hostActionfilter,
                                    data: json.host
                                });
                            }
                        });
                    }
            
    			}
    		}
            }
        }
    };

    var hostActionfilter = function(args) {
        var jsonObj = args.context.item;
        var allowedActions = [];
        if(jsonObj.cpuStatus=='on'){
        	 allowedActions.push("doStop");
        }else{
        	 allowedActions.push("doStart");
        }
        
        return allowedActions;
    }
})(jQuery, cloudStack);

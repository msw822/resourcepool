 // Licensed to the Apache Software Foundation (ASF) under one
(function($, cloudStack) { 
	var g_workOrders=undefined;
	var g_fields=undefined;
	var g_worktype = undefined;
	//添加实例
	var deployVirtualMachine=5;
	var projectLimitapplication=2;
	//新加卷
	var newVolume=7;
	
	var status_provisonfail = 6;//处理失败
	var status_provisionsucceed=5;//处理成功
	var status_provisioning=4;//审批通过正在处理
	var status_rejected=3;//审批未通过
	var status_approved=2;//审批通过待处理
	var status_waitingforapproval=1;//待审批
	
	var g_iscustomized = undefined;
	
	
	//工单类型条件
	var filterWorkOrderType = {all:{label: "所有工单"}};
	$.ajax({ 
		url : createURL("listConfig"),
		dataType : "json", 
		async : false, 
		data : { 
			cmsz : "yes", //configKey :		 "workorder_status%" 
			configKey : "workorder_type%" 
			}, 
		success :function(json) { 
			workOrderType = json.configList;
			$.each(workOrderType,function(index, val){
				var obj = {};
				$.extend(obj,{label : val.description});
				filterWorkOrderType[val.key]=obj;
			});
		}
	});
    var vmSnapshotAction = function(args) {
        var action = {
            messages: {
                notification: function(args) {
                    return 'label.action.vmsnapshot.create';
                }
            },
            label: 'label.action.vmsnapshot.create',
            addRow: 'false',
            createForm: {
                title: 'label.action.vmsnapshot.create',
                fields: {
                    name: {
                        label: 'label.name',
                        isInput: true
                    },
                    description: {
                        label: 'label.description',
                        isTextarea: true
                    },
                    snapshotMemory: {
                        label: 'label.vmsnapshot.memory',
                        isBoolean: true,
                        isChecked: false
                    },
                    quiescevm: {
                        label: 'label.quiesce.vm',
                        isBoolean: true,
                        isChecked: false,
                        isHidden: function(args) {
                            if (args.context.instances[0].hypervisor !== 'VMware') {
                                return true;
                            }

                            args.form.fields.quiescevm.isChecked = true;
                            
                            return false;
                        }
                    }
                }
            },
            action: function(args) {
                var instances = args.context.instances;
                
                $(instances).map(function(index, instance) {
                    var array1 = [];
                    array1.push("&snapshotmemory=" + (args.data.snapshotMemory == "on"));
                    array1.push("&quiescevm=" + (args.data.quiescevm == "on"));
                    var displayname = args.data.name;
                    if (displayname != null && displayname.length > 0) {
                        array1.push("&name=" + todb(displayname));
                    }
                    var description = args.data.description;
                    if (description != null && description.length > 0) {
                        array1.push("&description=" + todb(description));
                    }
                    $.ajax({
                        url: createURL("createVMSnapshot&virtualmachineid=" + instance.id + array1.join("")),
                        dataType: "json",
                        async: true,
                        success: function(json) {
                            var jid = json.createvmsnapshotresponse.jobid;
                            args.response.success({
                                _custom: {
                                    jobId: jid,
                                    getUpdatedItem: function(json) {
                                        return json.queryasyncjobresultresponse.jobresult.virtualmachine;
                                    },
                                    getActionFilter: function() {
                                        return vmActionfilter;
                                    }
                                }
                            });
                        }
                    });       
                });

            },
            notification: {
                poll: pollAsyncJobResult
            }
        };

        if (args && args.listView) {
            $.extend(action, {
                isHeader: true,
                isMultiSelectAction: true
            });
        }
        
        return action;
    };  
    cloudStack.sections.workOrders = {
        title: '所有订单',
        id: 'workOrders',
        listView: {
            section: 'workOrders',
            filters:filterWorkOrderType, 
            preFilter: function(args) {
                var hiddenFields = [];
                /*if (!isAdmin()) {
                    hiddenFields.push('instancename');
                }*/
                return hiddenFields;
            },
            
            afterFilter: function(args) {
            	$("div.button.action").hide();
            },
            
            fields: {
            	id:{
            		label : '工单编号',
            	},
        		workOrderType : {
        			label : '工单类型',
        			converter : function(arg) {
        				return showWorkOrderType(arg);
        			}
        		},
        		applierName : {
        			label : '申请人'
        		},
        		createdOn : {
        			label : '申请时间',
        			converter : function(date) {
        				return cloudStack.converters.toCloudDate(date, "1");
        			}
        		},
        		approveName:{
        			label : '审批人',
        		},
//        		modifiedOn:{
//        			label : '审批时间',
//        			converter : function(date) {
//        				return cloudStack.converters.toCloudDate(date, "1");
//        			}
//        		},      		
        		status : {
        			label : '状态',
        			converter : function(status) {
        				return showWorkOrderStatus(status);
        			}
        		}	
            },
            advSearchFields: {
                 status: {
                	 label : "工单状态",
                	 select: function(args) {
                		 var obj = [{id:"",description: '所有类型'}];
                		 if(workOrderStatus){
                			 $.each(workOrderStatus,function(index, val){
	                				obj.push({
	                					 id:val.value,
	                					 description: val.description
	                				 });
                				});
                			 args.response.success({
	                             data: obj
                			 });
                		 }
                	 }
                 },
                 isOverDue:{
                	 label : "实例过期状态",
                	 select: function(args){
                		 var obj = [{id:"0",description: '所有类型'},{id:"1",description: '即将到期'},{id:"2",description: '已到期'}];
                		 args.response.success({
                             data: obj
            			 });
                	 }
                 },
                 startDate:{
                	 label : "申请时间从",
                	 isDatepicker: true
                 },
                 endDate : {
                	 label : "至",
                	 isDatepicker: true
                 }
            },
            
            actions: {
//                add: {
//                    label: 'label.apply.immediately',
//
//                    preFilter: function(args) {
//                    	return true;
//                    },
//
//                    messages: {
//                        notification: function(args) {
//                            return '服务申请';
//                        }
//                    },
//
//                    createForm: {
//                        title: '服务申请',
//                        fields: {
//                        	operatingSystem: {
//                                label: '操作系统',
//                                validation: {
//                                    required: true,
//                                    maxlength: 50
//                                }
//                            },                         
//                            cpu: {
//                                label: 'CPU核数',
//                                validation: {
//                                    required: true,
//                                    number: true,
//                                    min: 1
//                                }
//                            },
//                            memory: {
//                                label: '内存(MB)',
//                                validation: {
//                                    required: true,
//                                    maxlength: 20,
//                                    number: true,
//                                    min: 1
//                                }
//                            },
//                            unit: {
//                                label: '台数',
//                                docID: 'helpApplyAmountDescription',
//                                validation: {
//                                    required: true,
//                                    number: true,
//                                    min: 1,
//                                    max: 100
//                                }
//                            },
//                            description: {
//                            	isTextarea: true,
//                                label: '说明',
//                                docID: 'helpApplicationDescription',
//                                validation: {
//                                	required: false,
//                                    maxlength: 100
//                                }
//                            },
//                            applyReason: {
//                            	isTextarea: true,
//                                label: '申请理由',
//                                validation: {
//                                    required: true,
//                                    maxlength: 100
//                                }
//                            }
//                        }
//                    },
//
//                    action: function(args) {
//                        $.ajax({
//                            url: createURL('saveOrder', {
//                                ignoreProject: true
//                            }),
//                            data: {
//                            	workOrderType: function() {
//                            		return getWorkOrderVal($("#filterBy").val());
//                            	},
//                            	cmsz: 'yes',
//                            	status: 1,
//                            	accountId: args.context.users[0].account,
//                                domainId: args.context.users[0].domainid,
//                                operatingSystem: args.data['operatingSystem'],
//                                //resourcePoolId: args.data['resourcePoolId'],
//                                cpu: args.data['cpu'],
//                                memory: args.data['memory'],
//                                unit: args.data['unit'],
//                                description: args.data['description'],
//                                applyReason: args.data['applyReason']
//                            },
//                            dataType: 'json',
//                            async: true,
//                            type:'post',
//                            success: function(data) {
//                            	cloudStack.dialog.window({message:"提交成功"}, function() {
//                              		$("#basic_search").click();
//                            	});
//                            },
//                            error: function(XMLHttpResponse) {
//                                var errorMsg = parseXMLHttpResponse(XMLHttpResponse);
//                                args.response.error(errorMsg);
//                            }
//                        });
//                    },
//
//                    notification: {
//                        poll: function(args) {
//                            args.complete();
//                        }
//                    }
//                },
                add:{
                	//isHeader: true,
                    label: 'label.apply.immediately',

                    action: {
                        custom: cloudStack.uiCustom.instanceWizard(cloudStack.instanceWizard)
                    },

                    messages: {
                        notification: function(args) {
                            return 'label.vm.add';
                        }
                    },
                    notification: {
                        poll: pollAsyncJobResult
                    }
                
                },
                add2:{
                	isHeader: true,
                    label: 'label.add.volume',

                    messages: {
                        confirm: function(args) {
                            return 'message.add.volume';
                        },
                        notification: function(args) {
                            return 'label.add.volume';
                        }
                    },

                    createForm: {
                        title: 'label.add.volume',
                        desc: 'message.add.volume',
                        fields: {
                            name: {
                                //docID: 'helpVolumeName',
                                label: 'label.name',
                                validation: {
                                    required: true
                                }
                            },
                            /**
                             * 
                             * 新需求 添加卷不需要zone区域选择
                             */
                            /*availabilityZone: {
                                label: 'label.availability.zone',
                                docID: 'helpVolumeAvailabilityZone',
                                select: function(args) {
                                    $.ajax({
                                        url: createURL("listZones&available=true"),
                                        dataType: "json",
                                        async: true,
                                        success: function(json) {
                                            var zoneObjs = json.listzonesresponse.zone;
                                            args.response.success({
                                                descriptionField: 'name',
                                                data: zoneObjs
                                            });
                                        }
                                    });
                                }
                            },*/
                            //选择虚拟机
                            instance: {
                                label: '虚机',
                                select: function(args) {
                                    $.ajax({
                                        url: createURL("listVirtualMachines"),
                                        dataType: "json",
                                        async: true,
                                        success: function(json) {
                                            var vmObjs = json.listvirtualmachinesresponse.virtualmachine;
                                            args.response.success({
                                                descriptionField: 'name',
                                                data: vmObjs
                                            });
                                        }
                                    });
                                }
                            },
                            diskOffering: {
                                label: 'label.disk.offering',
                                docID: 'helpVolumeDiskOffering',
                                select: function(args) {
                                    $.ajax({
                                        url: createURL("listDiskOfferings"),
                                        dataType: "json",
                                        async: false,
                                        success: function(json) {
                                            diskofferingObjs = json.listdiskofferingsresponse.diskoffering;
                                            var items = [];
                                            $(diskofferingObjs).each(function() {
                                                items.push({
                                                    id: this.id,
                                                    description: this.displaytext
                                                });
                                            });
                                            args.response.success({
                                                data: items
                                            });
                                        }
                                    });

                                    args.$select.change(function() {
                                        var diskOfferingId = $(this).val();
                                        $(diskofferingObjs).each(function() {
                                            if (this.id == diskOfferingId) {
                                                selectedDiskOfferingObj = this;
                                                return false; //break the $.each() loop
                                            }
                                        });
                                        if (selectedDiskOfferingObj == null)
                                            return;

                                        var $form = $(this).closest('form');
                                        var $diskSize = $form.find('.form-item[rel=diskSize]');
                                        if (selectedDiskOfferingObj.iscustomized == true) {
                                            $diskSize.css('display', 'inline-block');
                                        } else {
                                            $diskSize.hide();
                                        }
                                        var $minIops = $form.find('.form-item[rel=minIops]');
                                        var $maxIops = $form.find('.form-item[rel=maxIops]');
                                        if (selectedDiskOfferingObj.iscustomizediops == true) {
                                            $minIops.css('display', 'inline-block');
                                            $maxIops.css('display', 'inline-block');
                                        } else {
                                            $minIops.hide();
                                            $maxIops.hide();
                                        }
                                    });
                                }
                            } ,
                            /**
                        	加上一个申请理由
                             */
                            applyReason:{
                            	id:'applyReason',
                            	label:'申请理由',
                            	isTextarea:true,
                            	validation: {
                                    required: true
                                }
                            },
                            diskSize: {
                                label: 'label.disk.size.gb',
                                validation: {
                                    required: true,
                                    number: true,
                                    min:1
                                },
                                isHidden: true
                            },

                            minIops: {
                                label: 'label.disk.iops.min',
                                validation: {
                                    required: false,
                                    number: true
                                },
                                isHidden: true
                            },

                            maxIops: {
                                label: 'label.disk.iops.max',
                                validation: {
                                    required: false,
                                    number: true
                                },
                                isHidden: true
                            },

                        }
                    },

                    action: function(args) {
                    	$("tr.loading").remove();
                    	
                    	var projectid = $("div.project-switcher > select").val();
                        
                        var data = {
                            name: args.data.name,
                            /*zoneId: args.data.availabilityZone,*/ //deleted by steven
                            diskOfferingId: args.data.diskOffering,
                            /**
                             * added by  steven
                             */
                            accountId: cloudStack.context.users[0].account,
                            domainId: args.context.users[0].domainid,
                            virtualmachineid: args.data.instance,
                        	workOrderType:7,
                        	applyReason:args.data.applyReason,
                         	cmsz:'yes',
                         	status:1
                        };
                        /*var appendData ={
                        					accountId: cloudStack.context.users[0].account,
                        					domainId: args.context.users[0].domainid
                        				};
                       */
                        if(projectid!="-1"){
                        	 $.extend(data,{projectid:projectid});
                        }                 
                       

                        // if(thisDialog.find("#size_container").css("display") != "none") { //wait for Brian to include $form in args
                        if (selectedDiskOfferingObj.iscustomized == true) {
                            $.extend(data, {
                            	diskSize: args.data.diskSize
                            });
                            /**
                             * added by steven
                             * 2014/4/16
                             */
                            $.extend(data, {
                            	iscustomized: 1
                            });
                        }else{
                        	/**
                             * added by steven
                             * 2014/4/17
                             * 如果是非自定化磁盘方案，将传入disksize以及iscustomized
                             */
                        	$.extend(data, {
                            	iscustomized: 0
                            });
                        	$.extend(data, {
                            	diskSize: selectedDiskOfferingObj.disksize
                            });
                        }

                        if (selectedDiskOfferingObj.iscustomizediops == true) {
                            if (args.data.minIops != "" && args.data.minIops > 0) {
                                $.extend(data, {
                                    miniops: args.data.minIops
                                });
                            }

                            if (args.data.maxIops != "" && args.data.maxIops > 0) {
                                $.extend(data, {
                                    maxiops: args.data.maxIops
                                });
                            }
                        }
                        $.ajax({
                        	/**
                        	 * updated by steven
                        	 * createVolume改为saveOrder
                        	 */
                            url: createURL('saveOrder'),
                            data: data,
                            async: false,
                            type:'post',
                            success: function(json) {
                            	/**
                            	 * deleted by steven
                            	 */
                               /* var jid = json.createvolumeresponse.jobid;
                                args.response.success({
                                    _custom: {
                                        jobId: jid,
                                        getUpdatedItem: function(json) {
                                            return json.queryasyncjobresultresponse.jobresult.volume;
                                        },
                                        getActionFilter: function() {
                                            return volumeActionfilter;
                                        }
                                    }
                                });*/
                            	
                            	/**
                            	 * added by steven
                            	 */
                            	cloudStack.dialog.window({message:"工单提交成功"},function(){
                             		cloudStack.dialog.hide();
                             		$("#basic_search").click();
                             		return;
                             	});

                            },
                            error: function(json) {
                                args.response.error(parseXMLHttpResponse(json));
                            }
                        });
                    },

                    notification: {
                        poll: pollAsyncJobResult
                    }
                }
            },
            
            dataProvider: function(args) {
				var data = {
        					cmsz : 'yes',
        					page : args.page,
        					pageSize  : pageSize,
        					listAll : true
        				};
                listViewDataProvider(args, data);
                data.pagesize = 22;
                
                var getWorkOrderVal=function(key){

            		if (!workOrderType) {
            			$.ajax({ 
            				url : createURL("listConfig"),
            				dataType : "json", 
            				async : false, 
            				data : { 
            					cmsz : "yes", //configKey :		 "workorder_status%" 
            					configKey : "workorder_type%" 
            					}, 
            				success :function(json) { 
            					workOrderType = json.configList;
            				}
            			});
            		}
            		var dbconfig = $.grep(workOrderType, function(value, index) { 
            			return value.key == key; 
            		});
            		if (dbconfig.length > 0) { 
            			return	 ""+dbconfig[0].value; 
            		} else {
            			return null; 
            		}
            	
            	};
                
                if (args.filterBy != null) { //filter dropdown
                	
                	if (args.filterBy.kind != null) {
                    	var value = getWorkOrderVal(args.filterBy.kind);
                    	//alert(args.filterBy.kind);
//                    	var add = $("div.button.action.add");
                    	var add = $("div.button.action");
                    	if (add) {
                    		if(args.filterBy.kind == "workorder_type.deployVirtualMachine") {
                    			add.hide();
                    			$("div.button.action.add").show();
                        	}
                    		else if(args.filterBy.kind=="workorder_type.newVolume"){
                        		add.hide();     
                        		$("div.button.action.add2").show();
                        	}               		
                    		else {
                        		add.hide();
                        	}
                    	}
                    	
                    	if(value!=null){
                    		 $.extend(data, {
                    			 WORKORDER_TYPE: value
                             });
                    	}
                    }
                }
                if ("instances" in args.context) {//@ma add for view workorder in the instances
                    $.extend(data, {
                        instanceId: args.context.instances[0].id
                    });
                }
                $.ajax({
        			url : createURL('listWorkOrder'),
        			data : data,
        			success : function(json) {        				
        				var items = json.workOrders;
        				args.response.success({
        					data : items
        				});
        			}
                
               
        		});
            },

		detailView : {
				quickView : false,
				// name: 'Instance details',
				isMaximized : true,
				// viewAll: [{
				// path: 'storage.volumes',
				// label: 'label.volumes'
				// }, {
				// path: 'vmsnapshots',
				// label: 'label.snapshots'
				// }, {
				// path: 'affinityGroups',
				// label: 'label.affinity.groups'
				// }],
				actions:{
                    remove: {
                        label: '取消订单',
                        messages: {
                            confirm: function(args) {
                                return 'message.delete.workorder';
                            },
                            notification: function(args) {
                                return 'label.action.delete.workorder';
                            }
                        },
                        action: function(args) {
                            var data = {
                                id: args.context.workOrders[0].id,
                                cmsz : "yes"        
                            };
                            $.ajax({
                                url: createURL('cancelOrder'),
                                data: data,
                                async: true,
                                success: function(json) {
									cloudStack.dialog.window(
											{
												message : "订单取消成功"
											},
										function() {
											args.response.success();
											$(".loading-overlay").remove();
											$("#breadcrumbs ul li:eq(0)").click();
											// $(window).trigger('cloudStack.fullRefresh');
											$("#basic_search").click();
										});
                                },
                                error : function(json) {
                                	$(".loading-overlay").remove();
                                	cloudStack.dialog.error(parseXMLHttpResponse(json));
                                }
                            });
                        },
                        notification: {
							poll : function(args) {
								args.complete({});
							}
						}
                    },
                    extendOrder:{
                    	
                        label: '实例续订',
                        createForm: {
                            title: '实例续订',
                           // desc: 'Please read the dynamic scaling section in the admin guide before scaling up.',
                            fields: {
                            	extendMonths: {
                                    label: '续订月数',
                                    docID: 'helpExtendApplyMonthsDescription',
                                    validation: {
                                        required: true,
                                        number: true,
                                        min:1,
                                        max:120
                                    },
                                },
                                applyReason:{
                                	label:'续订理由',
                                	isTextarea:true,
                                	validation: {
                                        required: true,
                                        maxlength: 100
                                    }
                                }
                            }
                        },

                        action: function(args) {

                            $.ajax({
                                url: createURL('saveOrder', {
                                    ignoreProject: true
                                }),
                                data: {
                                	workOrderType: 8,
                                	cmsz: 'yes',
                                	status: 1,
                                	accountId: args.context.users[0].account,
                                    domainId: args.context.users[0].domainid,
                                    originalOrder:args.context.workOrders[0].id,
                                    extendMonths: args.data['extendMonths'],
                                    applyReason: args.data['applyReason']
                                },
                                dataType: 'json',
                                async: true,
                                type:'post',
                                success: function(data) {
                                	$(".loading-overlay").remove();
									//$("#breadcrumbs ul li:eq(0)").click();
                                	cloudStack.dialog.window({message:"订单提交成功"}, function() {
                                  		$("#basic_search").click();
                                	});
                                },
                                error: function(XMLHttpResponse) {
                                	$(".loading-overlay").remove();
                                    var errorMsg = parseXMLHttpResponse(XMLHttpResponse);
                                    args.response.error(errorMsg);
                                }
                            });                       
                        },
                       messages: {
                         notification: function(args) {
                            return '续订申请';
                         }
                     },
                        notification: {
                            poll: pollAsyncJobResult
                        }              
                    },
                reinstall:{
                    label: '回收实例资源',
                    messages: {
                        confirm: function(args) {
                            return '确认回收吗?';
                        },
                        notification: function(args) {
                            return '实例回收';
                        }
                    },
                    action: function(args) {
                        var data = {
                            id: args.context.workOrders[0].id,
                            cmsz : "yes"        
                        };
                        $.ajax({
                            url: createURL('recycleOrder'),
                            data: data,
                            async: true,
                            success: function(json) {
								cloudStack.dialog.window(
										{
											message : json.message
										},
									function() {
										if(json.success==1)
										args.response.success();
										$(".loading-overlay").remove();
										$("#breadcrumbs ul li:eq(0)").click();
										// $(window).trigger('cloudStack.fullRefresh');
										$("#basic_search").click();
									});
                            },
                            error : function(json) {
                            	$(".loading-overlay").remove();
                            	cloudStack.dialog.error(parseXMLHttpResponse(json));
                            }
                        });
                    },
                    notification: {
						poll : function(args) {
							args.complete({});
						}
					}
                }
				},
				tabFilter : function(args) {
				 
					g_workOrders = args.context.workOrders[0];
					
					
					var hiddenTabs = [];
					g_worktype = args.context.workOrders[0].workOrderType;
					var status = args.context.workOrders[0].status;
					// alert(g_worktype);
					if (g_worktype != 8) {
						//	hiddenTabs.push("test");
					} 
					
					if(status==3 || status ==1)
					{
						//hiddenTabs.push("test");
					}
					if (args.context.workOrders[0].status == "1") {// 待审批
						hiddenTabs.push("viewApprove");
						// return ["details","approve"];
					} else {
						hiddenTabs.push("approve");
						// return ["details","viewApprove"];
					}

					if (isDomainAdmin() || isUser()) {
						hiddenTabs.push("approve");
					}
					return hiddenTabs;
				},	            
                viewAll: [//添加viewall @ma只有当处理成功(状态为5)才会显示查看申请的实例
                {
                    path: 'instances',
                    label: 'label.instances',
                    preFilter: function(args) {
                    	var workordertype = args.context.workOrders[0].workOrderType;
                        return  (workordertype == 5 || workordertype == 6) && args.context.workOrders[0].status==5;
                    },
                },{
                    path: 'storage',
                    label: '卷',
                    preFilter: function(args) {
                        return args.context.workOrders[0].workOrderType==7 && args.context.workOrders[0].status==5;
                    },
                
                },{
                	 path: 'projects',
                     label: 'label.projects',
                     preFilter: function(args) {
                         return args.context.workOrders[0].workOrderType==1 && args.context.workOrders[0].status==5;
                     },
                }
                ],
                
				tabs : {
					// Details tab
					details : {
						title : '订单详情',
						/*
						 * preFilter: function(args) { return []; },
						 */
						preFilter : function(args) {
							var hiddenFields = [];
							if (args.context.workOrders[0].workOrderType == newVolume) {
								var iscustomized = getItemAttributeValue(
										args.context.workOrders[0],
										"iscustomized");
								if (iscustomized == 0) {// 非自定义磁盘大小的方案
									hiddenFields.push("diskSize");
								}
							} else if (args.context.workOrders[0].workOrderType == deployVirtualMachine) {
								var dvmdiskSize = getItemAttributeValue(
										args.context.workOrders[0], "diskSize");
								if (dvmdiskSize == undefined
										|| dvmdiskSize == ""
										|| dvmdiskSize == 0) {// 非自定义磁盘大小的方案
									hiddenFields.push("diskSize");
								}
							}
							else if (args.context.workOrders[0].workOrderType == 9) {
								var dvmdiskSize = getItemAttributeValue(
										args.context.workOrders[0], "diskSize");
								if (dvmdiskSize == undefined
										|| dvmdiskSize == ""
										|| dvmdiskSize == 0) {// 非自定义磁盘大小的方案
									hiddenFields.push("diskSize");
								}
							}
							return hiddenFields;
						},
						/*
						 * actions:{ edit: { label: '编辑', action:null } },
						 */
						fields : [],

						dataProvider : function(args) {

							var renderObj = {};
							if (args.context.workOrders[0].status != 1) {
								var data = {
									id : args.context.workOrders[0].id,
									cmsz : "yes",
									step : 1    //2--->1
								};
								$.ajax({
									url : createURL("listWorkOrder"),
									data : data,
									dataType : "json",
									async : false,
									success : function(json) {
										renderObj = json.workOrders[0];
									}
								});

							} else {
								renderObj = args.context.workOrders[0];
							}

							// 初始化字段
							var fields = viewFields(args.context.workOrders[0]);
							//全局化 field
							//g_fields = fields;
							
							
							var obj = {};
							$.extend(obj, renderObj);

							for (var i = 0; i < args.context.workOrders[0].workItems.length; ++i) {
								var workItem = args.context.workOrders[0].workItems[i];

								// 控制 以Default View视图添加实例时 ProjectId字段不显示
								if (workItem.attributeName == "projectid"
										&& workItem.attributeValue == ""
										&& args.context.workOrders[0].workOrderType == deployVirtualMachine) {
									// fields[workItem.attributeName] =
									// undefined;
									delete fields[workItem.attributeName];
									continue;
								}
								/*
								 * //控制新加卷时，非自定义磁盘大小时不显示磁盘大小
								 * if(workItem.attributeName=="diskSize" &&
								 * fields["iscustomized"]==0 &&
								 * args.context.workOrders[0].workOrderType==newVolume){
								 * //delete fields[workItem.attributeName];
								 * fields[workItem.attributeName].isHidden=true;
								 * continue; }
								 */
								if (fields[workItem.attributeName]
										&& fields[workItem.attributeName].externalValue == 1
										&& (fields[workItem.attributeName].valType == "text" || (fields[workItem.attributeName].valType == "enum" && !fields[workItem.attributeName].editable))) {
									//alert(workItem.attributeValue+"---"+workItem.attributeName);
									$
											.ajax({
												url : createURL("getExtval"),
												async : false,
												data : {
													cmsz : "yes",
													workordertype : args.context.workOrders[0].workOrderType,
													attributename : workItem.attributeName,
													id : workItem.attributeValue
												},
												success : function(json) {
													//alert(workItem.attributeValue);
													if (json
															&& json.keyValues.length > 0) {
														for (var i = 0; i < json.keyValues.length; ++i) {
															if (json.keyValues[i].id == workItem.attributeValue) {
																obj[workItem.attributeName] = json.keyValues[i].description;
																break;
															}
														}
													} else {
														obj[workItem.attributeName] = workItem.attributeValue;
													}
												}
											});
								} else {
									obj[workItem.attributeName] = workItem.attributeValue;
								}
							}

							args.response.success({
								data : obj,
								fields : fields,
								actionFilter: orderActionfilter,
							});
							
							//全局 field
							g_fields = obj;
							
						}
					},

					/**
					 * 审批 tab
					 */
					approve : {
						title : '审批',
						multiple : true,
						noHeader : true,
						preFilter : function(args) {
							var hiddenFields = [];
							g_iscustomized = undefined;
							if (args.context.workOrders[0].workOrderType == newVolume) {
								g_iscustomized = getItemAttributeValue(
										args.context.workOrders[0],
										"iscustomized");
								if (g_iscustomized == 0) {// 非自定义磁盘大小的方案
									hiddenFields.push("diskSize");
								}
							} else if (args.context.workOrders[0].workOrderType == deployVirtualMachine) {
								var dvmdiskSize = getItemAttributeValue(
										args.context.workOrders[0], "diskSize");
								if (dvmdiskSize == undefined
										|| dvmdiskSize == ""
										|| dvmdiskSize == 0) {// 非自定义磁盘大小的方案
									hiddenFields.push("diskSize");
								}
							}else if (args.context.workOrders[0].workOrderType == 9) {
								var dvmdiskSize = getItemAttributeValue(
										args.context.workOrders[0], "diskSize");
								if (dvmdiskSize == undefined
										|| dvmdiskSize == ""
										|| dvmdiskSize == 0) {// 非自定义磁盘大小的方案
									hiddenFields.push("diskSize");
								}							
							}
							return hiddenFields;
						},
						actions : {
							add : {

								label : '审批',
								messages : {
									/*
									 * confirm: function(args) { return 'Please
									 * confirm that you would like to add a new
									 * VM NIC for this network.'; },
									 */
									notification : function(args) {
										// return 'Add network to VM';
										return '审批工单';
									}
								},
								action : function(args) {
									if (!$("#validateForm").valid()) {
										// $(".loading-overlay").hide();
										// $(".loading-overlay").remove();
										return;
									}

									$("#details-tab-approve").append(
											$('.loading-overlay'));

									var data = cloudStack
											.serializeForm($("[formdata=true]"));
									var id = args.context.workOrders[0].id;
									var workOrder = args.context.workOrders[0];
									workOrder.approveDesc = data["approveDesc"];
									workOrder.approveResult = data['approveResult'];
									if (g_iscustomized != undefined) {
										$.extend(data, {
											iscustomized : g_iscustomized
										});
									}
									$.extend(data, {
										workOrderId : id,
										cmsz : "yes"
									});
									var datastring="";
//									for(var key in data)
//										{
//										
//										alert(data[key]);
//										
//										}
									$.ajax({
												url : createURL("doapprove"),
												data : data,
												dataType : "json",
												async : false,
												success : function(json) {
													cloudStack.dialog.window(
													{
														message : "审批成功"
													},
													function() {
														args.response.success();
														$(".loading-overlay").remove();
														$("#breadcrumbs ul li:eq(0)").click();
														// $(window).trigger('cloudStack.fullRefresh');
														$("#basic_search").click();
													});
												},
												error : function(json) {
													$(".loading-overlay").remove();
													cloudStack.dialog.error(parseXMLHttpResponse(json));
												}
											});

								},
								notification : {
									poll : function(args) {
										args.complete({});
									}

								}
							}
						},
						fields : [],

						dataProvider : function(args) {

							var renderObj = args.context.workOrders[0];

							// 初始化字段
							var fields = initFields(args.context.workOrders[0]);

							//g_workOrders = fields;
							
							var obj = {};
							$.extend(obj, renderObj);
							for (var i = 0; i < args.context.workOrders[0].workItems.length; ++i) {
								var workItem = args.context.workOrders[0].workItems[i];

								// 控制 以Default View视图添加实例时 ProjectId字段不显示
								if (workItem.attributeName == "projectid"
										&& workItem.attributeValue == ""
										&& args.context.workOrders[0].workOrderType == deployVirtualMachine) {
									// fields[workItem.attributeName] =
									// undefined;
									delete fields[workItem.attributeName];
									continue;
								}

								/*
								 * //控制新加卷时，非自定义磁盘大小时不显示磁盘大小
								 * if(workItem.attributeName=="diskSize" &&
								 * fields["iscustomized"]==0 &&
								 * args.context.workOrders[0].workOrderType==newVolume){
								 * //delete fields[workItem.attributeName];
								 * fields[workItem.attributeName].isHidden=true;
								 * continue; }
								 */

								if (fields[workItem.attributeName]
										&& fields[workItem.attributeName].externalValue == 1
										&& (fields[workItem.attributeName].valType == "text" || (fields[workItem.attributeName].valType == "enum" && !fields[workItem.attributeName].editable))) {
									$
											.ajax({
												url : createURL("getExtval"),
												async : false,
												data : {
													cmsz : "yes",
													workordertype : args.context.workOrders[0].workOrderType,
													attributename : workItem.attributeName,
													id : workItem.attributeValue
												},
												success : function(json) {

													if (json
															&& json.keyValues.length > 0) {
														for (var i = 0; i < json.keyValues.length; ++i) {
															if (json.keyValues[i].id == workItem.attributeValue) {
																obj[workItem.attributeName] = json.keyValues[i].description;
																obj[workItem.attributeName
																		+ "_id"] = workItem.attributeValue;
																break;
															}
														}
													} else {
														obj[workItem.attributeName] = workItem.attributeValue;
														obj[workItem.attributeName
																+ "_id"] = workItem.attributeValue;
													}
													// obj[workItem.attributeName]=json.keyValues[0].description;
												}
											});
								} else {
									obj[workItem.attributeName] = workItem.attributeValue;
								}
							}

							args.response.success({
								data : obj,
								fields : fields,
								name : "审批"
							});

							if (g_worktype == newVolume) {// 添加卷
								$("select[name=diskOfferingId]",
										$("#details-tab-approve"))
										.bind(
												"change",
												function() {
													var iscustomized = $(this)
															.find(
																	"option[value="
																			+ $(
																					this)
																					.val()
																			+ "]")
															.attr(
																	"iscustomized");
													g_iscustomized = iscustomized;
													if (iscustomized == 1) {
														$(
																"input[name=diskSize]",
																$("#details-tab-approve"))
																.parents("tr")
																.show();
													} else {
														$(
																"input[name=diskSize]",
																$("#details-tab-approve"))
																.parents("tr")
																.hide();
													}
												});
								$("select[name=diskOfferingId]",
										$("#details-tab-approve")).change();
							} else if (g_worktype == deployVirtualMachine) {// 添加实例
								// 显隐磁盘大小输入框
								$("select[name=diskofferingid]",
										$("#details-tab-approve"))
										.bind(
												"change",
												function() {
													$(
															"input[name=diskSize]",
															$("#details-tab-approve"))
															.removeClass(
																	"required number");
													var iscustomized = $(this)
															.find(
																	"option[value="
																			+ $(
																					this)
																					.val()
																			+ "]")
															.attr(
																	"iscustomized");
													// g_iscustomized =
													// iscustomized;
													if (iscustomized == 1) {
														$(
																"input[name=diskSize]",
																$("#details-tab-approve"))
																.parents("tr")
																.show();
														$(
																"input[name=diskSize]",
																$("#details-tab-approve"))
																.addClass(
																		"required number");
													} else {
														$(
																"input[name=diskSize]",
																$("#details-tab-approve"))
																.parents("tr")
																.hide();
														$(
																"input[name=diskSize]",
																$("#details-tab-approve"))
																.removeClass(
																		"required number");
													}
												});
								$("select[name=diskofferingid]",
										$("#details-tab-approve")).change();

								// 更改zone时，模版会级联变化 网络会级联变化
								$("select[name=zoneid]",
										$("#details-tab-approve"))
										.bind(
												"change",
												function() {
													// 模版
													$(
															"select[name=templateid]",
															$("#details-tab-approve"))
															.empty();
													$
															.ajax({
																url : createURL("getExtval"),
																async : false,
																data : {
																	cmsz : "yes",
																	workordertype : g_worktype,
																	attributename : "templateid",
																	zoneid : this.value
																},
																success : function(
																		json) {
																	if (json
																			&& json.keyValues.length > 0) {

																		for (var i = 0; i < json.keyValues.length; ++i) {
																			var $option = $("<option value='"
																					+ json.keyValues[i].id
																					+ "'>"
																					+ json.keyValues[i].description
																					+ "</option>");
																			$(
																					"select[name=templateid]",
																					$("#details-tab-approve"))
																					.append(
																							$option);
																		}

																	}
																}
															});
													// 网络
													$(
															"select[name=networkids]",
															$("#details-tab-approve"))
															.empty();
													$
															.ajax({
																url : createURL("getExtval"),
																async : false,
																data : {
																	cmsz : "yes",
																	workordertype : g_worktype,
																	attributename : "networkids",
																	domainid : args.context.workOrders[0].domainId,
																	account : args.context.workOrders[0].account,
																	zoneid : this.value
																},
																success : function(
																		json) {
																	console
																			.info(json);
																	if (json
																			&& json.keyValues.length > 0) {
																		for (var i = 0; i < json.keyValues.length; ++i) {
																			var $option = $("<option value='"
																					+ json.keyValues[i].id
																					+ "'>"
																					+ json.keyValues[i].description
																					+ "</option>");
																			$(
																					"select[name=networkids]",
																					$("#details-tab-approve"))
																					.append(
																							$option);
																		}

																	}
																}
															});

												});
//								$("select[name=zoneid]",
	//									$("#details-tab-approve")).change();  

							}else if (g_worktype == 9){
								$("select[name=diskofferingid]",
										$("#details-tab-approve"))
										.bind(
												"change",
												function() {
													$(
															"input[name=diskSize]",
															$("#details-tab-approve"))
															.removeClass(
																	"required number");
													var iscustomized = $(this)
															.find(
																	"option[value="
																			+ $(
																					this)
																					.val()
																			+ "]")
															.attr(
																	"iscustomized");
													// g_iscustomized =
													// iscustomized;
													if (iscustomized == 1) {
														$(
																"input[name=diskSize]",
																$("#details-tab-approve"))
																.parents("tr")
																.show();
														$(
																"input[name=diskSize]",
																$("#details-tab-approve"))
																.addClass(
																		"required number");
													} else {
														$(
																"input[name=diskSize]",
																$("#details-tab-approve"))
																.parents("tr")
																.hide();
														$(
																"input[name=diskSize]",
																$("#details-tab-approve"))
																.removeClass(
																		"required number");
													}
												});
								$("select[name=diskofferingid]",
										$("#details-tab-approve")).change();

							}

						}
					},
					viewApprove : {

						title : '审批信息',
						preFilter : function(args) {
							var hiddenFields = [];
							if (args.context.workOrders[0].workOrderType == newVolume) {
								var iscustomized = getItemAttributeValue(
										args.context.workOrders[0],
										"iscustomized");
								if (iscustomized == 0) {// 非自定义磁盘大小的方案
									hiddenFields.push("diskSize");
								}
							} else if (args.context.workOrders[0].workOrderType == deployVirtualMachine) {
								var dvmdiskSize = getItemAttributeValue(
										args.context.workOrders[0], "diskSize");
								// alert(dvmdiskSize);
								if (dvmdiskSize == undefined
										|| dvmdiskSize == ""
										|| dvmdiskSize == 0) {// 非自定义磁盘大小的方案
									hiddenFields.push("diskSize");
								}
							}else if (args.context.workOrders[0].workOrderType == 9) {
								var dvmdiskSize = getItemAttributeValue(
										args.context.workOrders[0], "diskSize");
								if (dvmdiskSize == undefined
										|| dvmdiskSize == ""
										|| dvmdiskSize == 0) {// 非自定义磁盘大小的方案
									hiddenFields.push("diskSize");
								}							
							}
							return hiddenFields;
						},
						/*
						 * actions:{ edit: { label: '编辑', action:null } },
						 */

						fields : [],

						/*
						 * tags: cloudStack.api.tags({ resourceType: 'UserVm',
						 * contextId: 'instances' }),
						 */

						dataProvider : function(args) {

							var renderObj = {};
							if (args.context.workOrders[0].status != 1) {
								var data = {
									id : args.context.workOrders[0].id,
									cmsz : "yes",
									step : 2
								};
								$.ajax({
									url : createURL("listWorkOrder"),
									data : data,
									dataType : "json",
									async : false,
									success : function(json) {
										renderObj = json.workOrders[0];
									}
								});

							} else {
								renderObj = args.context.workOrders[0];
							}

							// 初始化字段
							var fields = viewFields(args.context.workOrders[0]);
							$.extend(fields, {
								approveResult : {
									label : "审批结果",
									select : function(args) {
										args.response.success({
											data : [ {
												id : '1',
												description : '审批通过'
											}, {
												id : '0',
												description : '审批不通过'
											} ]
										});
									}
								},
								approveDesc : {
									label : "审批描述"
								}
							});
							var obj = {};
							$.extend(obj, renderObj);
							for (var i = 0; i < args.context.workOrders[0].workItems.length; ++i) {
								var workItem = args.context.workOrders[0].workItems[i];
								// 控制 以Default View视图添加实例时 ProjectId字段不显示
								if (workItem.attributeName == "projectid"
										&& workItem.attributeValue == ""
										&& args.context.workOrders[0].workOrderType == deployVirtualMachine) {
									// fields[workItem.attributeName] =
									// undefined;
									delete fields[workItem.attributeName];
									continue;
								}

								if (fields[workItem.attributeName]
										&& fields[workItem.attributeName].externalValue == 1) {// &&
									// fields[workItem.attributeName].valType=="text"){
									//alert("111"+workItem.attributeName+workItem.attributeValue);
									$
											.ajax({
												url : createURL("getExtval"),
												async : false,
												data : {
													cmsz : "yes",
													workordertype : args.context.workOrders[0].workOrderType,
													attributename : workItem.attributeName,
													id : workItem.attributeValue
												},												
												//success{}修改 by ma10-20 修改审批详情页面显示id为空显示bug
//												success : function(json) {
//													if (json
//															&& json.keyValues[0]
//															&& json.keyValues.length > 0) {
//														obj[workItem.attributeName] = json.keyValues[0].description;
//													} else {
//														alert(workItem.attributeName+workItem.attributeValue);
//														obj[workItem.attributeName] = "22222222222";
//													}
//												}
												success : function(json) {
													if (json
															&& json.keyValues.length > 0) {
														for (var i = 0; i < json.keyValues.length; ++i) {
															if (json.keyValues[i].id == workItem.attributeValue) {
																obj[workItem.attributeName] = json.keyValues[i].description;
																break;
															}
														}
													} else {
														obj[workItem.attributeName] = workItem.attributeValue;
													}
												}
											});
								} else {
									obj[workItem.attributeName] = workItem.attributeValue;
								}
							}
							args.response.success({
								data : obj,
								fields : fields
							});

						}

					}
					//分配信息
//					test : {
//                       title: '分配信息',
//                       listView:{
//                       	disableInfiniteScrolling : true,
//                       	//multiSelect : true,
//                           filters: {
//                               unassignment: {
//                               	 preFilter: function(args) {
//                                        if (isAdmin() || isDomainAdmin())
//                                            return true;
//                                        else
//                                            return false;
//                                    },
//                                   label: '未分配'
//                               }
//                           },
//                           preFilter: function(args) {
//                        	   
//                        	   //var td = $('tbody');
//                        	   
//                        	
//                        	  // var btn = tr.find("input[type='button']")
//    						   //btn.attr("disabled","disabled").css("border","1px solid #525252").css("background","#525252").val("已分配");
//                        	   //alert(btn);
//                        	   
//                              var hiddenFields = [];
//                              // hiddenFields.push('hold0')
//                               return hiddenFields;
//                           },
//                           fields: {
//                           	hostName: {
//               	                label: '机器名称',
//               	            },
//               	            type: {
//            	                label: '类型'
//            	            },	
//               	            cpu:{
//               	            	label: 'cpu个数'
//                           		
//               	            },
//               	            physicsMemory: {
//               	                label: '内存(GB)',
//               	            },
//               	         ipAddr: {
//            	                label: 'ip地址',
//            	            },
//               	         save : {
//               	        	 	label : '分配',
//     							button : true,
//     							preFilter: function(args) {
//     					 			/*$.ajax({
//	                               		 url: createURL('listPhysicsByOrderId'),
//	                                        data: {cmsz : "yes",orderId:g_workOrders.id},
//	                                        success: function(json) { 
//	                                        	alert(json);
//	                                        	//var btn = tr.find("input[type='button']")
//	    		     							//btn.attr("disabled","disabled").css("border","1px solid #525252").css("background","#525252").val("已分配");
//	                                        }
//	                                    });*/
//     								
//                               },
//	     						action : function(dataItem, tr) {
//	     							var physicsName = dataItem.hostName;
//	     							var physicsType = dataItem.type;
//	     							var orderId = g_workOrders.id;
//	     							var owner = g_workOrders.applierName;
//	     							var status = g_workOrders.status;
//	     							if(confirm('确定要分配此物理机？'))
//	     							{
//	     								
//	     								var btn = tr.find("input[type='button']")
//		     							btn.attr("disabled","disabled").css("border","1px solid #525252").css("background","#525252").val("分配");
//	     								
//	     								$.ajax({
//		                               		 url: createURL('savePhysicsHostAssignment&cmsz=yes'),
//		                                        data: {cmsz : "yes",physicsName : physicsName,physicsType:physicsType,orderId:orderId,status:status},
//		                                        success: function(json) {                        
//		                                        }
//		                                    });
//	     								
//	     								
//	     							}
//	     		 
//	     						}
//               	         	}
//               	            
//               	        },
//
//                           dataProvider: function(args) {
//                           	console.info(args);
//                           	var data = {};            	
//                           	listViewDataProvider(args, data);
//                               if (args.filterBy != null) { //filter dropdown
//                                   if (args.filterBy.kind != null) { 
//                                       switch (args.filterBy.kind) {
//                                           case "all":
//                                               break;
//                                           case "mine":
//                                               if (!args.context.projects) {                                	
//                                               	$.extend(data, {
//                                                   	hostTypeStatus: 'mine'
//                                                   });                                  
//                                               }
//                                               break;
//                                           case "unassignment":
//                                               $.extend(data, {
//                                               	hostTypeStatus: 'unassignment'
//                                               });
//                                             
//                                               break;
//                                           case "assignmented":
//                                               $.extend(data, {
//                                               	hostTypeStatus: 'assignmented'
//                                               });   
//                                               break;
//                                           case "virtualed":
//                                               $.extend(data, {
//                                               	hostTypeStatus: 'virtualed'
//                                               });
//                                               break;
//                                       }
//                                   }
//                               }
//
//                                
//                           	if(args.filterBy.search.value!=""){
//                           		$.extend(data,{"hostName":args.filterBy.search.value});
//                           	} 
//                           	 $.ajax({
//                           		 url: createURL('listPhysicsHost&cmsz=yes&page=1&pagesize=10000&orderId='+g_workOrders.id),
//                                    data: data,
//                                    async: true,
//                                    success: function(json) {                        
//                                        args.response.success({
//                                            actionFilter: hostActionfilter,
//                                            data: json.physicsHosts
//                                        });
//                                        
//                                        var ordered =undefined;
//                                        
//                                        $.ajax({
//	                               		 url: createURL('listPhysicsByOrderId'),
//	                               		 	async: false,
//	                                        data: {cmsz : "yes",orderId:g_workOrders.id},
//	                                        success: function(json) { 
//	                                        	ordered = json.ordered;
//	                                        	
//	                                        }
//	                                    });/**/
//                                        
//                                        if(ordered!=undefined)
//                                        {
//                                        	//alert(ordered.ordered[0].hostName);
//                                        	
//                                        	 var tr =  $("#details-tab-test tbody").children("tr");
//                                        	  $.each(tr,function(i,n){
//                                        		  
//                                        		  var hostName = $(this).data("jsonObj").hostName;
//                                        		  var type = $(this).data("jsonObj").type;
//                                        		  var hostandtype = hostName+"|"+type;
//                                        		  
//                                        		  for(var key in ordered)
//                                        		  {
//                                        			  var r = ordered[key].hostName+"|"+ordered[key].type;
//                                        			  
//                                        			  if(r==hostandtype)
//                                        			  {
//                                        				  
//                                        				  var btn = $(n).find("input[type='button']")
//                                        				  btn.attr("disabled","disabled").css("border","1px solid #525252").css("background","#525252").val("分配");
//                                        			  }
//                                        			  
//                                        			  
//                                        	      }
//                                        		  
//                                        	  // var btn = $(n).find("input[type='button']");
//                                        	   
//                                        	   //btn.attr("disabled","disabled").css("border","1px solid #525252").css("background","#525252").val("已分配");
//                                        		 
//                                        	   //alert($(this).data("jsonObj").hostName);
//                                        	  // alert($(n).children(".hostName reduced-hide>span").val());
//                                        		   
//                                        	   }); 
//                                        }
//                                        
//                                       
//                                 	   
//                                    }
//                                });
//                           	 
//                               
//                           },
//                           actions: {
//                               add: {
//                               	preFilter: function(args) {
//                                        if (isAdmin() || isDomainAdmin())
//                                            return true;
//                                        else
//                                            return false;
//                                   },
//                                   label: '重新分配',
//                                   
//                                   messages: {
//                                       confirm: function(args) {
//                                           return '确定要给该订单分配所选的物理机？';
//                                       },
//                                       notification: function(args) {
//                                           return '确定要给该订单分配所选的物理机？';
//                                       }
//                                   },
//                                   
//                                   action: function(args) {
//                                	   
//                                	   $("[disabled='disabled']").removeAttr("disabled").css("background","url('images/gradients.png') repeat scroll 0 -317px #0049ff").css("border","1px solid #0069CF").val("分配");
//                                	   
//                                	   
//                                	   $.ajax({
//		                               		 url: createURL('saveEnabledPhysicsInstanceByOrderid&cmsz=yes'),
//		                                        data: {orderId:g_workOrders.id},
//		                                        success: function(json) {                        
//		                                        }
//		                                    });
//                                	   
//                                	   
//                                	  // $('.last ui-state-default ui-corner-top ui-tabs-selected ui-state-active').hide();
//                                	   //var o = args.context.workOrders[0].id;
//                                	  // alert(o);
//                                	   
//                      /*          	   for(var key in o)
//                                	   {
//                                		   
//                                		   alert(key+"----"+o[key]);
//                                		   
//                                		   for(var i in o[key])
//                            			   {
//                            				   
//                            				   alert(key+"--------"+i+"----"+o[key][i]);
//                            				   
//                            			   }
//                                	   }
//                                	   
//                                	   
//                                	   for(var key in u)
//                                	   {
//                                		   
//                                		   alert(key+"----"+o[u]);
//                                	   }*/
//                                	   
//               /*                 	   for(var key in args)
//                                	  {
//                                		   if(args[key]!=undefined)
//                                		   {
//                                			   
//                                			   for(var i in args[key])
//                                			   {
//                                				   
//                                				   alert(key+"--------"+i+"----"+args[key][i]);
//                                				   
//                                			   }
//                                			  
//                                			   
//                                			   
//                                		   }
//                                		   
//                                	  }*/
//                                	   
//                                	  
//                                	  // alert($('td').find('input[type=checkbox]').is(':checked'));
//                                	   	
//                                	   
//                                   },
//
//                                   notification: {
//                                       poll: pollAsyncJobResult
//                                   }
//                               } 
//                           },
//                           detailView: {
//                        	   quickView : false,
//                               name: '实例详情',
//                               isMaximized: true,
//                             
//                               /*viewAll: {  
//                                   path: 'physicsInstances',
//                                   label: '主机'
//                                  
//                               }, */
//                               actions: {
//                               	 doStart: {                		
//                                        label: '启动',
//                                        messages: {
//                                            confirm: function(args) {
//                                                return 'message.update.resource.count';
//                                            },
//                                            notification: function(args) {
//                                                return 'label.action.update.resource.count';
//                                            }
//                                        },
//                                        action: function(args) {
//                                            var accountObj = args.context.accounts[0];
//                                            var data = {
//                                                domainid: accountObj.domainid,
//                                                account: accountObj.name
//                                            };
//
//                                          
//                                        },
//                                        notification: {
//                                            poll: function(args) {
//                                                args.complete();
//                                            }
//                                        }
//                                    
//                               	 },
//                               	 doStop:{                		
//                                        label: '停止',
//                                        messages: {
//                                            confirm: function(args) {
//                                                return 'message.disable.account';
//                                            },
//                                            notification: function(args) {
//                                                return 'label.action.disable.account';
//                                            }
//                                        },
//                                        action: function(args) {
//                                            var accountObj = args.context.accounts[0];
//                                            var data = {
//                                                lock: false,
//                                                domainid: accountObj.domainid,
//                                                account: accountObj.name
//                                            };
//
////                                            $.ajax({
////                                                url: createURL('disableAccount'),
////                                                data: data,
////                                                async: true,
////                                                success: function(json) {
////                                                    var jid = json.disableaccountresponse.jobid;
////                                                    args.response.success({
////                                                        _custom: {
////                                                            jobId: jid,
////                                                            getUpdatedItem: function(json) {
////                                                                return json.queryasyncjobresultresponse.jobresult.account;
////                                                            },
////                                                            getActionFilter: function() {
////                                                                return accountActionfilter;
////                                                            }
////                                                        }
////                                                    });
////                                                }
////                                            });
//                                        },
//                                        notification: {
//                                            poll: pollAsyncJobResult
//                                        }
//                                    
//                               	 }
//                               	
//                               },
//                               tabs: {
//                                   details: {
//                                	   quickview:false,
//                                       title: '实例详情',
//                                       fields:[{
//                                          hostName: {
//                     			                label: '名称'
//                     			            },
//                     			           osCaption: {
//                     			                label: 'OS类型'
//                     			            },
//                     			           cpu:{
//                     			            	label: 'cpu个数'
//                     			            	
//                     			            },
//                     			            physicsMemory: {
//                     			            	label: '内存(GB)'
//                     			            },
//                     			            ipAddr:{label : "ip"},
//                     			            
//                     			        /*
//                     			          storage:{
//                     			        	label:"物理驱动",
//                     			        	  converter: function(storage){
//                 			                	return storage.device[0].physical_device;
//                 		    				  }
//                     			          },
//                     			         storage:{
//                     			        	label:"物理存储",
//                   			        	  converter: function(storage){
//               			                	return storage.device[0].physical_storage;
//               		    				  }
//                   			          },*/
//                     			         cpuStatus: {
//                     		                label: '是否可运行',
//                     		                indicator: {
//                     	                        'on': 'on',
//                     	                        'off': 'off'
//                     	                    }
//                     		             } 
//                     			         
//                                   }],
//                                   dataProvider: function(args) {                    	
//                                   	var data = {
//                   		                	cmsz: 'yes',
//                   		                    query:true,
//                   		                    hostname : args.context.physicsInstances[0].hostName,
//                   		                    type : args.context.physicsInstances[0].type
//                                       	};
//                                   	//console.info(data);
//                                       $.ajax({
//                                       	url: createURL("physicsHostInfo"),
//                                           dataType: "json",
//                                           async: true,
//                                           data:data,
//                                           success: function(json) {                              
//                                           	console.info(json);
//                                               args.response.success({
//                                               	actionFilter: hostActionfilter,
//                                                   data: json.host
//                                               });
//                                           }
//                                       });
//                                   }
//                           
//                   			}
//                   		}
//                           }
//                       
//                       }
//                    }
				}
			}
        }
    };
	
	
	var workOrderStatus = undefined;
	var showWorkOrderStatus = function(args) {
		
		 if(!workOrderStatus){ 
			 $.ajax({ 
					 url : createURL("listConfig"), 
					 dataType :"json", 
					 async : false, 
					 data : { cmsz : "yes", 
						 configKey : "workorder_status%" }, 
					success : function(json) { 
						workOrderStatus = json.configList;
					} 
			 });
		 } 
		 var dbconfig =	 $.grep(workOrderStatus,function(value,index){ return value.value == args; }); 
		 if(dbconfig.length>0){ 
			 return dbconfig[0].description;
		 }else{ 
			 return args;
		 }
		/*
		 * 1 "待审批"; 2 "审批通过待处理"; 3 "审批未通过"; 4 "审批通过正在处理"; 5 "处理成功"; 6 "处理失败"; }
		 */

	};

	var workOrderType = undefined;
	var showWorkOrderType = function(args) {

		if (!workOrderType) {
			$.ajax({ 
				url : createURL("listConfig"),
				dataType : "json", 
				async : false, 
				data : { 
					cmsz : "yes", // configKey : "workorder_status%"
					configKey : "workorder_type%" 
					}, 
				success :function(json) { 
					workOrderType = json.configList;
				}
			});
		}
		var dbconfig = $.grep(workOrderType, function(value, index) { 
							return value.value == args; 
						});
		if (dbconfig.length > 0) { 
				return	 dbconfig[0].description; 
		} else {
			return args; 
		}
	};
	
	

	var commonField = {
		id : {
			label : '工单编号'
		},
		createdOn : {
			label : '创建时间',
			converter : function(date) {
				return cloudStack.converters.toCloudDate(date, "1");
			}
		},
		workOrderType : {
			label : '工单类型',
			converter : function(arg) {
				return showWorkOrderType(arg);
			}
		},
		createdBy : {
			label : '申请人'
		}
	};
	// 初始化查看字段
	var viewFields = function(workOrder){
		var workOrderType = workOrder.workOrderType;
		var fields = {};
		$.ajax({
			url : createURL("getAttribute"),
			dataType : "json",
			async : false,
			data : {
				cmsz : "yes",
				workordertype : workOrderType
			},
			success : function(json) {
				$.extend(fields, commonField);
				if(workOrderType==5 && workOrder.status >=5 ){//add if @ma
					$.extend(fields, {
						workorder_due_date : {
							label : '到期时间',
							converter : function(date) {
								return cloudStack.converters.toCloudDate(date, "1");
							}
						}
					});
				}					
				for ( var i = 0; i < json.fields.length; ++i) {
					var obj = {};
					// 不可见不显示
					if(json.fields[i].visible==0){
						if(json.fields[i].required==0){// 不是必要的
							continue;
						}else{// 必要的
							obj.isHidden=true;
						}
					}
					obj.label = json.fields[i].displayName;
					obj.externalValue = json.fields[i].externalValue;
					obj.valType = json.fields[i].type;
					obj.alignTo = json.fields[i].alignto;
					// obj.isEditable = true;
					// externalValue=1时表示要从外部获取
					
					// 获取枚举值
					if (json.fields[i].externalValue == 1) {
						var renderObj = {};
						$.ajax({
							url : createURL("getExtval"),
							async : false,
							data : {
								cmsz : "yes",
								workordertype : workOrderType,
								attributename : json.fields[i].attributeName
							},
							success : function(json) {
								renderObj = json.keyValues;
							}
						});
						obj.select = function(args) {
							args.response.success({
								data : renderObj
							});
						};
					}
				// if(json.fields[i])
					fields[json.fields[i].attributeName] = obj;
//					for(var key in fields)
//						{
						
//						alert(key+fields[key]);
//						for(var k in fields[key]){
//							alert(k+fields[key][k]);
//						}
//						}
					
				}
			}
			
		});
		
		// 添加开通失败信息字段
		if(status_provisonfail==workOrder.status){
			$.extend(fields,{
				errorCode:{
					label : "错误码"
				},
				errorText:{
					label : "错误信息"
				}
			});
		}
		return fields;
	
	};
	var initFields = function(workOrder) {
		var workOrderType = workOrder.workOrderType;
		var fields = {};
		var alignToVal = {};
		$.ajax({
			url : createURL("getAttribute"),
			dataType : "json",
			async : false,
			data : {
				cmsz : "yes",
				workordertype : workOrderType
			},
			success : function(json) {
				$.extend(fields, commonField);
				for(var i= 0;i<json.fields.length;++i){
					
					if(json.fields[i].alignto!=""){
						alignToVal[json.fields[i].alignto] ="true";// 临时值
					}
				}
				for(var i=0;i<workOrder.workItems.length;++i){
					if(alignToVal[workOrder.workItems[i].attributeName]=="true"){
						alignToVal[workOrder.workItems[i].attributeName] = workOrder.workItems[i].attributeValue;
					}
				}
				for ( var i = 0; i < json.fields.length; ++i) {
					
					var obj = {};
					
					//不可见不显示
					if(json.fields[i].visible==0){
						if(json.fields[i].required==0){//不是必要的
							continue;
						}else{//必要的
							obj.isHidden=true;
						}
						obj.isHidden=true;
					}
					
					obj.label = json.fields[i].displayName;
					
					//可编辑
					obj.editable = json.fields[i].editable==1;
					obj.valType = json.fields[i].type;
					obj.externalValue = json.fields[i].externalValue;
					obj.alignTo = json.fields[i].alignto;
					obj.required = json.fields[i].required;
					var querystr = "";
					if(obj.alignTo!=""){
						var refs = obj.alignTo.split(",");
						for(var j=0;j<refs.length;j++){
						 querystr  = querystr +"&"+ refs[j]+"="+alignToVal[refs[j]];
						}
						//alert(querystr);
						//console.info(querystr);
					}
					// externalValue=1时表示要从外部获取
					//获取枚举值
					if (json.fields[i].externalValue == 1) {
						var dataParam = {
								cmsz : "yes",
								workordertype : workOrderType,
								attributename : json.fields[i].attributeName
							};
						//添加实例5特殊处理
						if(workOrderType==deployVirtualMachine){
							if(json.fields[i].attributeName=="networkids"){//网络
								$.extend(dataParam,{
										"domainid":workOrder.domainId,
										"account":workOrder.account
									});
							}
						}
						
						//console.info(querystr +":"+    json.fields[i].attributeName  );
						var renderObj = {};
						$.ajax({
							//url : createURL("getExtval"+querystr),
							url : createURL("getExtval"),
							async : false,
							data :dataParam,
							success : function(json) {
								renderObj = json.keyValues;
							}
						});
						if(json.fields[i].type=="enum"){
							obj.select = function(args) {
								args.response.success({
									data : renderObj
								});
							};
							obj.data = renderObj;
						}
						if(workOrderType==deployVirtualMachine){
							if(json.fields[i].attributeName=="networkids"){//网络
								//TODO
							}
						}
					}
					fields[json.fields[i].attributeName] = obj;
				}
			}
			
		});
		$.extend(fields,{
			approveResult:{
				label : "审批结果",
				editable : true,
				isEditable : true,
				data : [{
                    id: '1',
                    description: '审批通过'
                }, {
                    id: '0',
                    description: '审批不通过'
                }],
				select :  function(args){
					args.response.success({data: [{
                        id: '1',
                        description: '审批通过'
                    }, {
                        id: '0',
                        description: '审批不通过'
                    }]});
				}
			},
			approveDesc : {
				label : "审批描述",
				inputType:"textarea",
				editable : true,
				required : 1
			}});		
		return fields;
	};
	
	var getItemAttributeValue = function(workOrder,attributeName){
		if(workOrder==undefined){
			return undefined;
		}else{
        	var items = workOrder.workItems;
        	for(var i=0;i<items.length;++i){
        		if(items[i].attributeName==attributeName){
        			return items[i].attributeValue;
        		}
        	}
		}
			
	};

	var getWorkOrderVal = function(key){

		if (!workOrderType) {
			$.ajax({ 
				url : createURL("listConfig"),
				dataType : "json", 
				async : false, 
				data : { 
					cmsz : "yes", //configKey :		 "workorder_status%" 
					configKey : "workorder_type%" 
					}, 
				success :function(json) { 
					workOrderType = json.configList;
				}
			});
		}
		var dbconfig = $.grep(workOrderType, function(value, index) { 
			return value.key == key; 
		});
		if (dbconfig.length > 0) { 
			return	 ""+dbconfig[0].value; 
		} else {
			return null; 
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
    };
    var orderActionfilter = cloudStack.actionFilter.orderActionFilter = function(args) {
        var jsonObj = args.context.item;
        var allowedActions = [];
//        if (jsonObj.workOrderType==5){
//        	allowedActions.push("remove");
//        	 if(jsonObj.status == 5){        		
//        		allowedActions.push("extendOrder");
//        	}
//        }
        if(jsonObj.status == 1 &&  g_username == jsonObj.applierName){//自己的待审批的订单都可以取消
        	allowedActions.push("remove");
        }
        if(jsonObj.workOrderType==5 && jsonObj.status == 5){//实例订单处理成功之后可以取消和续订
        	if(g_username == jsonObj.applierName){
        		allowedActions.push("remove");
        		allowedActions.push("extendOrder");
        	}
        	if (isAdmin()){
        		allowedActions.push("reinstall");
        	}
        }
        return allowedActions;
    }
})(jQuery, cloudStack);



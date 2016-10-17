(function(cloudStack) {
    cloudStack.sections['resourceStatistics'] = {
        title: '资源统计',
        id: 'global-settings',
        sectionSelect: {
            label: 'label.select-view'
        },
        sections: {
        	resourceStatistics: {
                type: 'select',
                title: '整体资源统计',
                listView: {
					id : 'global-settings',
					//firstClick : false,
					hideToolbar2 : true,
					fields : {
						'resourcePool' : {
							label : '资源池'
						},
						'total' : {
							label : '总量'
						},
						'used' : {
							label : '已使用'
						},
						'unused' : {
							label : '未使用'
						},
						'percentused' : {
							label : '使用率'
						}
					},

					dataProvider : function(args) {
						//var domainObj = args.context.domains[0];
						//alert("domainObj.type:"+domainObj.type);
						var url = "";
						var data = {
							listAll : true
						};

					
							url = createURL('computeresource');
							data.cmsz = 'yes';
						

						$.ajax({
							url : url,
							data : data,
							dataType : 'json',
							async : true,
							success : function(json) {
								var capacities = json.listcapacityresponse.capacity;
								var data = [];

								var showItems = {
									0 : {
										name : _l('label.memory')
									},
									1 : {
										name : _l('label.cpu')
									},
									2 : {
										name : _l('label.storage')
									},
									3 : {
										name : _l('label.primary.allocated')
									},
									4 : {
										name : _l('label.public.ips')
									},
									5 : {
										name : _l('label.management.ips')
									},
									6 : {
										name : _l('label.secondary.storage')
									},
									7 : {
										name : _l('label.vlan')
									},
									8 : {
										name : _l('label.direct.ips')
									},
									9 : {
										name : _l('label.local.storage')
									}
								};

								$.each(showItems, function(id, item) {
									//alert("item.name"+item.name+"::::"+data.length+"kkk::"+showItems);
									var n = data.length;
									$(capacities).each(
											function(i) {
												var capacity = this;
												if (id == capacity.type) {
													data[n] = {
														resourcePool : item.name,
														total : cloudStack.converters.convertByType(capacity.type,
																capacity.capacitytotal),
														used : cloudStack.converters.convertByType(capacity.type,
																capacity.capacityused),
														unused : cloudStack.converters.convertByType2(capacity.type,
																capacity.capacitytotal, capacity.capacityused),
														percentused : parseInt(capacity.percentused)+"%"
													};
													
													return false;
												}
											});

									if (data[n] == undefined || data[n] == null) {
								
											data[n] = {
												resourcePool : item.name,
												used : 0,
												unused : 0,
												total : 0,
												percentused : 0
											};
										
									}
								});

								args.response.success({
									data : data
								});
							}
						});
					}
                }
            },
            resourceStatistics2: {
                type: 'select',
                title: '按人员(账号)统计',
                listView: {
					id : 'resourceStatistics2',
					//firstClick : false,
					hideToolbar2 : true,
					fields : {
						'used' : {
							label : '帐户'
						},
						'resourcePool' : {
							label : '内存'
						},
						'total' : {
							label : 'CPU内核数'
						},
						'cpuspeed' : {
							label : 'CPU(GHz)'
						},
						
						'xunijigeshu' : {
							label : '拥有实例个数'
						},
						
						'zhucunchu' : {
							label : '主存储'
						}
					},

					dataProvider : function(args) {
						//var domainObj = args.context.domains[0];
						//alert("domainObj.type:"+domainObj.type);
						var url = "";
						var data = {
							listAll : true
						};

					
							url = createURL('accountresource');
							data.cmsz = 'yes';
						

						$.ajax({
							url : url,
							data : data,
							dataType : 'json',
							async : true,
							success : function(json) {
								
								var capacities = eval('(' + json.listcapacityresponse.capacity + ')');
								//alert("json.listcapacityresponse.virtualmachine:::>>"+capacities);
								var data = [];

									//alert("capacities>>"+capacities.length);
									var n = data.length;
									//alert("n:"+n);
									$(capacities).each(function(n) {
												
												var capacity = this;
												//alert("sssss>>>>>"+capacity.capacitytotal);
												//if (id == capacity.type) {
													//alert(123);
													data[n] = {
														used : cloudStack.converters.convertByType(capacity.type,
																	capacity.account),
														resourcePool : cloudStack.converters.convertByType(capacity.type,
																capacity.memory),
														total : cloudStack.converters.convertByType(capacity.type,
																capacity.capacitytotal),
														cpuspeed : cloudStack.converters.convertByType(capacity.type,
																		capacity.cpuspeed),
														
													   xunijigeshu:cloudStack.converters.convertByType(capacity.type,
																capacity.xunjicount),
																zhucunchu:cloudStack.converters.convertByType(capacity.type,
																			capacity.zhucunchu),
													};
													
													//return false;
												//}
											});

									if (data[n] == undefined || data[n] == null) {
								
											data[n] = {
												resourcePool : 0,
												used : 0,
												total : 0,
												xunijigeshu:0,
											};
										
									}

								args.response.success({
									data : data
								});
							}
						});
					}
                }
            }, 
            resourceStatistics3: {
                type: 'select',
                title: '按业务系统统计',
                listView: {
					id : 'resourceStatistics3',
					//firstClick : false,
					hideToolbar2 : true,
					fields : {
						'name' : {
							label : '名称'
						},
						'resourcePool' : {
							label : '网络总数'
						},
						'total' : {
							label : '主储'
						},
						'total2' : {
							label : '辅助存储'
						},
						'used' : {
							label : 'CPU内核数'
						},
						'unused' : {
							label : '内存总数'
						},
						'percentused' : {
							label : '虚拟机总数'
						}
					},

					dataProvider : function(args) {
						//var domainObj = args.context.domains[0];
						//alert("domainObj.type:"+domainObj.type);
						var url = "";
						var data = {
							listAll : true
						};

					
							url = createURL('operationresource');
							data.cmsz = 'yes';
						

						$.ajax({
							url : url,
							data : data,
							dataType : 'json',
							async : true,
							success : function(json) {
								var capacities = json.listcapacityresponse.capacity;
								//alert("json.listcapacityresponse.virtualmachine:::>>"+capacities);
								var data = [];
									var n = data.length;
									$(capacities).each(
											function(n) {
												var capacity = this;
													data[n] = {
														name : cloudStack.converters.convertByType(capacity.type,
																capacity.name),
														resourcePool : cloudStack.converters.convertByType(capacity.type,
																		capacity.networktotal),
														total : cloudStack.converters.convertByType(capacity.type,
																capacity.primarystoragetotal),
														total2 : cloudStack.converters.convertByType(capacity.type,
																		capacity.secondarystoragetotal),
														used : cloudStack.converters.convertByType(capacity.type,
																capacity.cputotal),
														unused : cloudStack.converters.convertByType(capacity.type,
																capacity.memorytotal),
														percentused : cloudStack.converters.convertByType(capacity.type,
																capacity.vmtotal)
													};
													
											});

									/*if (data[n] == undefined || data[n] == null) {
								
											data[n] = {
												resourcePool : 0,
												used : 0,
												unused : 0,
												total : 0,
												percentused : 0
											};
										
									}*/

								args.response.success({
									data : data
								});
							}
						});
					}
                }
            },
        }
    };
})(cloudStack);


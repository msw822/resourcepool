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
	cloudStack.sections.topData = {
		title : 'TOPN统计',
		id : 'topData',
		show : function(data) {
			var $panel = $(".panel").addClass("plot-view");

			var $toolbar = $("<div/>").addClass("toolbar");
			$toolbar.appendTo($panel);

			var margin = "5px";

			var $selectDiv = $("<div style='float:left'/>").appendTo($toolbar);
			// 指标
			$("<select id='targetSelect'/>").css({
				margin : margin,
				width : "120px"
			}).appendTo($selectDiv);

			// 查询维度：一级资源池，二级资源池，pod,cluster,host
			// resoucePoolSelect zoneSelect podSelect clusterSelect hostSelect
	/*		$("<select id='resoucePoolSelect'/>").css({
				margin : margin,
				width : "100px"
			}).appendTo($selectDiv);*/

			$("<select id='zoneSelect'/>").css({
				margin : margin,
				width : "100px"
			}).appendTo($selectDiv);

			$("<select id='podSelect'/>").css({
				margin : margin,
				width : "100px"
			}).appendTo($selectDiv);

			$("<select id='clusterSelect'/>").css({
				margin : margin,
				width : "100px"
			}).appendTo($selectDiv);

			$("<select id='hostSelect'/>").css({
				margin : margin,
				width : "100px"
			}).appendTo($selectDiv);

			$("<div id='bt_query' class='button add' style='margin-left:5px; float:left'>查询</div>").appendTo($toolbar);

			$("<div id='bt_excel' class='button add' style='float:left'>导出</div>").appendTo($toolbar);

			$('<form id="reportData" method="post" enctype="multipart/form-data"></form>').appendTo($toolbar);

			var $dataDiv = $("<div/>").css({
				height : "254px"
			}).addClass("plot reportTable").appendTo($panel);
			$("<div id='result_info'/>").css({
				"text-align" : "center",
				color : "red",
				padding : "2px"
			}).appendTo($dataDiv);

			$('<div id="dataHover">TOPN统计数据</div>').addClass("dataTip columnTip").appendTo($dataDiv);
			$('<div id="choices"/>').css({
				height : "20px"
			}).addClass("dataTitle").appendTo($dataDiv);
			$('<div id="placeholder"/>').css({
				height : "200px"
			}).addClass("dataStatus").appendTo($dataDiv);

			var $timeAndTarget = $("<div id='timeAndTarget'><label style='margin-left: 10px'>统计时间：</label><span id='topDataTime'/><label style='margin-left: 50px'>指标名称：</label><span id='topDataTarget'/></div>");
			$("<div/>").append($timeAndTarget).appendTo($panel);

			initUI();

			$("<div/>").addClass("plot-list").listView(cloudStack.sections.topData, {}).appendTo($panel);
			$panel.find(".list-view .toolbar").remove();

		},
		target : {
			cpunumber : {
				label : 'label.total.cpu'
			},
			cpuused : {
				label : 'label.cpu.utilized'
			},
			networkkbsread : {
				label : 'label.network.read'
			},
			networkkbswrite : {
				label : 'label.network.write'
			},
			diskkbsread : {
				label : 'label.disk.read.bytes'
			},
			diskkbswrite : {
				label : 'label.disk.write.bytes'
			},
			diskioread : {
				label : 'label.disk.read.io'
			},
			diskiowrite : {
				label : 'label.disk.write.io'
			}
		},
		listView : {
			firstClick : false,
			hideToolbar : true,
			filters : false,
			fields : {
				name : {
					label : '虚机名称'
				},
				ipAddress : {
					label : 'label.ip.address'
				},
//				resoucePool : {
//					label : '一级池'
//				},
				zone : {
					label : '区域'
				},
				host : {
					label : '主机名'
				},
				hypervisor:{
					label : '虚拟化'
				},
				value : {
					label : '值'
				}
			},

			dataProvider : function(args) {
				$("#result_info").text("查询中,请稍后.....");
				$("#dataHover").html("TOPN统计数据");
				$("#bt_query").attr("disabled", true);
				var data = {
					cmsz : 'yes',
					target : getTarget(),
				//	resoucePools : getOptionValues(1),
				//	zones : getOptionValues(2),
				//	pods : getOptionValues(3),
				//	clusters : getOptionValues(4),
				//	hosts : getOptionValues(5)
				};
				if(undefined!=$("#zoneSelect").val() && ""!=$("#zoneSelect").val() && $("#zoneSelect").val() != "all"){
					var zoneSelect = {};
					zoneSelect[0]= $("#zoneSelect").val();
					$.extend(data,{zones:zoneSelect});
				}
				if(undefined!=$("#podSelect").val() && ""!=$("#podSelect").val() && $("#podSelect").val() != "all"){
					var podSelect = {};
					podSelect[0] = $("#podSelect").val();
					$.extend(data,{pods:podSelect});
				}
				if(undefined!=$("#clusterSelect").val() && ""!=$("#clusterSelect").val() && $("#clusterSelect").val() != "all"){
					var clusterSelect = {};
					clusterSelect[0] = $("#clusterSelect").val();
					$.extend(data,{clusters:clusterSelect});
				}
				if(undefined!=$("#hostSelect").val() && ""!=$("#hostSelect").val() && $("#hostSelect").val() != "all"){
					var hostSelect = {};
					hostSelect[0] = $("#hostSelect").val();
					$.extend(data,{hosts:hostSelect});
				}

				$.ajax({
					url : createURL('listTopData'),
					data : data,
					dataType : "json",
					success : function(json) {
						var items = json.listresponse.topdatavoobj;

						var reslut = [];
						$("#bt_query").attr("disabled", false);
						$("#topDataTime").text(new Date().format("yyyy:MM:dd hh:mm:ss"));
						$("#topDataTarget").text(getTargetText());
						if (!items) {
							$("#result_info").text("该时间段内没有相关数据");
						} else {
							$("#result_info").text("返回数据成功");

							$.each(items, function(i, jsonObj) {
								reslut.push({
									name : jsonObj.name,
									ipAddress : jsonObj.ipAddress,
									//resoucePool : jsonObj.resoucePool,
									zone : jsonObj.zone,
									host : jsonObj.host,
									hypervisor : jsonObj.hypervisor,
									value : getTopDataValue(jsonObj)
								});
							});
						}

						args.response.success({
							data : reslut
						});

						cloudStack.topDataCat.getBarData(items);
					},
					error : function(json) {
						args.response.error(parseXMLHttpResponse(json));
					}
				});
			}

		}
	};

	function getTopDataValue(jsonObj) {
		var value = "";
		if ("cpunumber" == getTarget()) {
			return cloudStack.converters.convertHz(jsonObj.value * jsonObj.cpuSpeed);// MHz
		} else if ("cpuused" == getTarget()) {
			return jsonObj.value;
		} else if ("networkkbsread" == getTarget()) {
			return (jsonObj.value == null) ? "N/A" : cloudStack.converters.convertBytes(jsonObj.value * 1024);
		} else if ("networkkbswrite" == getTarget()) {
			return (jsonObj.value == null) ? "N/A" : cloudStack.converters.convertBytes(jsonObj.value * 1024);
		} else if ("diskkbsread" == getTarget()) {
			return (jsonObj.value == null) ? "N/A" : ((jsonObj.hypervisor == "KVM") ? cloudStack.converters.convertBytes(jsonObj.value * 1024)
					: ((jsonObj.hypervisor == "XenServer") ? cloudStack.converters.convertBytes(jsonObj.value * 1024) + "/s" : "N/A"));
		} else if ("diskkbswrite" == getTarget()) {
			return (jsonObj.value == null) ? "N/A" : ((jsonObj.hypervisor == "KVM") ? cloudStack.converters.convertBytes(jsonObj.value * 1024)
					: ((jsonObj.hypervisor == "XenServer") ? cloudStack.converters.convertBytes(jsonObj.value * 1024) + "/s" : "N/A"));
		} else if ("diskioread" == getTarget()) {
			return (jsonObj.value == null) ? "N/A" : ((jsonObj.hypervisor == "KVM") ? jsonObj.value : "N/A");
		} else if ("diskiowrite" == getTarget()) {
			return (jsonObj.value == null) ? "N/A" : ((jsonObj.hypervisor == "KVM") ? jsonObj.value : "N/A");
		}
		return value;
	}

	cloudStack.topDataCat = {
		option : {
			grid : {
				hoverable : true,
				clickable : false
			},
			series : {
				bars : {
					show : true,
					barWidth : 0.5
				}
			},
			xaxis : {
				show : true,
				mode : "categories",
				ticks : [],
				tickLength : 0,
				tickSize : 1,
				labelWidth : 70,
				min : 0,
				max : 10
			}
		},
		unit : {},
		target : {},
		getBarData : function(items) {
			var ticks = [];
			var datas = [];
			if (items == null || items.length < 1) {
				cloudStack.topDataCat.option.xaxis.ticks = ticks;
				$.plot("#placeholder", [ datas ], cloudStack.topDataCat.option);
				return;
			}

			cloudStack.topDataCat.target = getTarget();

			if ("cpunumber" == getTarget()) {
				if (items[0].value * items[0].cpuSpeed < 1000) {
					cloudStack.topDataCat.unit = "MHz";
				} else {
					cloudStack.topDataCat.unit = "GHz";
				}
			} else if ("networkkbsread" == getTarget() || "networkkbswrite" == getTarget() || "diskkbsread" == getTarget()
					|| "diskkbswrite" == getTarget()) {
				var bytes = items[0].value;
				if (bytes < 1024) {
					cloudStack.topDataCat.unit = "KB";
				} else if (bytes < 1024 * 1024) {
					cloudStack.topDataCat.unit = "MB";
				} else if (bytes < 1024 * 1024 * 1024) {
					cloudStack.topDataCat.unit = "GB";
				} else {
					cloudStack.topDataCat.unit = "TB";
				}
			}

			$.each(items, function(i, jsonObj) {
				// ticks.push([ i + 1, "虚机" + (i + 1) ]);
				ticks.push([ i, jsonObj.name ]);

				if ("cpunumber" == getTarget()) {
					if (cloudStack.topDataCat.unit == "MHz") {
						datas.push([ i, jsonObj.value * jsonObj.cpuSpeed ]);
					} else {
						datas.push([ i, (jsonObj.value * jsonObj.cpuSpeed / 1000) ]);
					}
				} else if ("cpuused" == getTarget()) {
					datas.push([ i, jsonObj.value.replace("%", "") ]);
				} else if ("networkkbsread" == getTarget() || "networkkbswrite" == getTarget() || "diskkbsread" == getTarget()
						|| "diskkbswrite" == getTarget()) {
					if (cloudStack.topDataCat.unit == "KB") {
						datas.push([ i, (jsonObj.value) ]);
					} else if (cloudStack.topDataCat.unit == "MB") {
						datas.push([ i, (jsonObj.value / 1024) ]);
					} else if (cloudStack.topDataCat.unit == "GB") {
						datas.push([ i, (jsonObj.value / 1024 / 1024) ]);
					} else {
						datas.push([ i, (jsonObj.value / 1024 / 1024 / 1024) ]);
					}
				} else if ("diskioread" == getTarget() || "diskiowrite" == getTarget()) {
					datas.push([ i, jsonObj.value ]);
				}
			});

			cloudStack.topDataCat.option.xaxis.ticks = ticks;

			$("#placeholder").empty();
			$.plot("#placeholder", [ datas ], cloudStack.topDataCat.option);

			$("#placeholder").bind("plothover", pieHover);
		}

	};

	Date.prototype.format = function(format) {
		var o = {
			"M+" : this.getMonth() + 1,
			"d+" : this.getDate(),
			"h+" : this.getHours(),
			"m+" : this.getMinutes(),
			"s+" : this.getSeconds(),
			"q+" : Math.floor((this.getMonth() + 3) / 3),
			"S" : this.getMilliseconds()
		};
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	};

	// 存放6个下拉选择框
	var selectType = [];

	function pieHover(event, pos, obj) {
		if (!obj) {
			return;
		}

		var datapoint = obj.datapoint[1];
		if ("cpunumber" == cloudStack.topDataCat.target) {
			if (cloudStack.topDataCat.unit == "MHz") {
				datapoint = cloudStack.converters.convertHz(datapoint);
			} else {
				datapoint = cloudStack.converters.convertHz(datapoint * 1000);
			}
		} else if ("cpuused" == cloudStack.topDataCat.target) {
			datapoint += "%";
		} else if ("networkkbsread" == cloudStack.topDataCat.target || "networkkbswrite" == cloudStack.topDataCat.target
				|| "diskkbsread" == cloudStack.topDataCat.target || "diskkbswrite" == cloudStack.topDataCat.target) {
			if (cloudStack.topDataCat.unit == "KB") {
				datapoint = cloudStack.converters.convertBytes(datapoint * 1024);
			} else if (cloudStack.topDataCat.unit == "MB") {
				datapoint = cloudStack.converters.convertBytes(datapoint * 1024 * 1024);
			} else if (cloudStack.topDataCat.unit == "GB") {
				datapoint = cloudStack.converters.convertBytes(datapoint * 1024 * 1024 * 1024);
			} else {
				datapoint = cloudStack.converters.convertBytes(datapoint * 1024 * 1024 * 1024 * 1024);
			}
		}
		// else if ($.inArray(cloudStack.topDataCat.target, [ "diskioread",
		// "diskiowrite" ])) {
		// }

		$("#dataHover").html(getTargetText() + '：<span style="color:' + obj.series.color + '"><strong>' + datapoint + '</strong></span>');
	}

	function initUI() {
		//  podSelect clusterSelect hostSelect
//		$('#podSelect').hide();
//		$('#clusterSelect').hide();
//		$('#hostSelect').hide();
		selectType = [];
		selectType.push($("#targetSelect"));
		selectType.push($("#resoucePoolSelect"));
		selectType.push($("#zoneSelect"));
		selectType.push($("#podSelect"));
		selectType.push($("#clusterSelect"));
		selectType.push($("#hostSelect"));

		var targetSelect = selectType[0];
		$.each(cloudStack.sections.topData.target, function(i, val) {
			targetSelect.append($("<option value='" + i + "'>" + _l(val.label) + "</option>"));
		});

		// resoucePoolSelect zoneSelect podSelect clusterSelect hostSelect
		//var resoucePoolSelect = selectType[1];
		var zoneSelect = selectType[2];
		var podSelect = selectType[3];
		var clusterSelect = selectType[4];
		var hostSelect = selectType[5];
		//var resoucePoolData = [];
		var zoneData = [];
		var podData = [];
		var clusterData = [];
		var hostData = [];

		var optionAll = "<option value='all'>全部</option>";
		//resoucePoolSelect.append($(optionAll));
		zoneSelect.append($(optionAll));
		podSelect.append($(optionAll));
		clusterSelect.append($(optionAll));
		hostSelect.append($(optionAll));

		var dimResource = {};
		$.ajax({
            url: createURL('listZones'),
            data: {
                listAll: true
            },
            success: function(json) {
                var zones = json.listzonesresponse.zone ? json.listzonesresponse.zone : [];
                
                $.each(zones,function(n,val) {   
                   // alert(n+' '+value);  
                	zoneSelect.append(createOption(val.id, val.name));   
                    });  
//                args.response.success({
//                    data: $.map(zones, function(zone) {
//                        return {
//                            id: zone.id,
//                            description: zone.name
//                        };
//                    })
//                });
            }
        });
		zoneSelect.change(function() {
			var data = 	 { 
					listAll: true,
				};
			var zoneSelected = $("#zoneSelect").val();
		//	alert($("#zoneSelect").val());
			if(undefined!=zoneSelected && ""!=zoneSelected && zoneSelected != "all")
				$.extend(data,{
					zoneid:zoneSelected
				});
            $.ajax({
                url: createURL("listPods"),
                dataType: "json",
                data:data,
                success: function (json) {
                    var pods = json.listpodsresponse.pod;
                	$("#podSelect").find("option").not(":first").remove();
                    $(pods).each(function (n,val) {
                    	podSelect.append(createOption(val.id, val.name));
                    });
                }
            });
            $('#podSelect').find("option[value='all']").attr("selected",true);
            $('#clusterSelect').find("option[value='all']").attr("selected",true);
            $('#hostSelect').find("option[value='all']").attr("selected",true); 
            //alert($("#hostSelect").val());
		});
		podSelect.change(function() {
			var data = 	 { 
					listAll: true,
				};
			var podSelected = $("#podSelect").val();
			//alert($("#podSelect").val());
			if(undefined != podSelected && ""!=podSelected && podSelected != "all")
				$.extend(data,{
					podid:podSelected
				});
		       $.ajax({
                   url: createURL("listClusters"),
                   dataType: "json",
                   data:data,
                   success: function (json) {
                	   $("#clusterSelect").find("option").not(":first").remove();
                       var items = json.listclustersresponse.cluster;
                       $(items).each(function (n,val) {
                    	   clusterSelect.append(createOption(val.id, val.name));
                       });
                   }
               });
		       $('#clusterSelect').find("option[value='all']").attr("selected",true);
	           $('#hostSelect').find("option[value='all']").attr("selected",true);      
		});
		clusterSelect.change(function() {
			var data = 	 { 
					listAll: true,
					//type:'routing'
				};
			var clusterSelected = $("#clusterSelect").val();
			//alert($("#clusterSelect").val());
			if(undefined != clusterSelected && ""!=clusterSelected && clusterSelected != "all")
				$.extend(data,{
					clusterid:clusterSelected
				});
//		       $.ajax({
//                   url: createURL("listClusters"),
//                   dataType: "json",
//                   data:data,
//                   success: function (json) {
//                	   $("#clusterSelect").find("option").not(":first").remove();
//                       var items = json.listclustersresponse.cluster;
//                       $(items).each(function (n,val) {
//                    	   clusterSelect.append(createOption(val.id, val.name));
//                       });
//                   }
//               });
		       $.ajax({
                   url: createURL('listHosts'),
                   data:data,
                   success: function (json) {
                	   $("#hostSelect").find("option").not(":first").remove();
                       var hostObjs = json.listhostsresponse.host;
                       $(hostObjs).each(function (n,val) {
                    	   hostSelect.append(createOption(val.id, val.name));
                       });           
                   }
               });
	           $('#hostSelect').find("option[value='all']").attr("selected",true);      
		});
//		$.ajax({
//			url : createURL('listDimResourceTree'),
//			data : {
//				cmsz : 'yes'
//			},
//			dataType : "json",
//			async : false,
//			success : function(json) {
//				dimResource = json.listresponse.dimresourceobj;
//
//				$.each(dimResource, function(i, val) {
//					var select = null;
//					if (val.type == "1") {
//						//resoucePoolData.push(val);
//						//select = resoucePoolSelect;
//					} else if (val.type == "2") {
//						zoneData.push(val);
//						select = zoneSelect;
//					} else if (val.type == "3") {
//						podData.push(val);
//						select = podSelect;
//					} else if (val.type == "4") {
//						clusterData.push(val);
//						select = clusterSelect;
//					} else if (val.type == "5") {
//						hostData.push(val);
//						select = hostSelect;
//					}
//
//					if (select != null) {
//						select.append(createOption(val.resourceId, val.name));
//					}
//				});
//
//				targetSelect.change(function() {
//					changeSelect(1, {
//					//	resoucePoolData : resoucePoolData,
//						zoneData : zoneData,
//						podData : podData,
//						clusterData : clusterData,
//						hostData : hostData
//					});
//				});
//
////				resoucePoolSelect.change(function() {
////					changeSelect(2, {
////						zoneData : zoneData,
////						podData : podData,
////						clusterData : clusterData,
////						hostData : hostData
////					});
////				});
//
//				zoneSelect.change(function() {
//					changeSelect(3, {
//						podData : podData,
//						clusterData : clusterData,
//						hostData : hostData
//					});
//				});
//
//				podSelect.change(function() {
//					changeSelect(4, {
//						clusterData : clusterData,
//						hostData : hostData
//					});
//				});
//
//				clusterSelect.change(function() {
//					changeSelect(5, {
//						hostData : hostData
//					});
//				});
//
//				// hostSelect.change(hostData, function() {
//				// });
//			},
//			error : function(json) {
//				args.response.error(parseXMLHttpResponse(json));
//			}
//		});

		$("#bt_query").click(function() {
			$('.list-view').listView('refresh');
		});

		$("#bt_excel").click(function() {
			exportTopData();
		});
	}

	function createOption(val, text) {
		return $("<option value='" + val + "'>" + text + "</option>");
	}

	function getTarget() {
		return selectType[0].val();
	}

	function getTargetText() {
		return selectType[0].find("option:selected").text();
	}

	function getOptionValues(type) {
		var values = {};
		if (selectType[type].val() == "all") {
			var options = selectType[type].find("option:first").nextAll();
			$.each(options, function(i) {
				values[i] = this.value;
			});
		} else {
			values[0] = selectType[type].val();
		}
		return values;
	}

	function changeSelect(type, data) {
		var showAll = true;
		// disk
		if (selectType[0].val().match(/^disk.+$/g)) {
			showAll = false;
		}

		if (type == 1) {
			addOptions(showAll, 1, data);
			addOptions(showAll, 2, data);
			addOptions(showAll, 3, data);
			addOptions(showAll, 4, data);
			addOptions(showAll, 5, data);
		} else if (type == 2) {
			addOptions(showAll, 2, data);
			addOptions(showAll, 3, data);
			addOptions(showAll, 4, data);
			addOptions(showAll, 5, data);
		} else if (type == 3) {
			addOptions(showAll, 3, data);
			addOptions(showAll, 4, data);
			addOptions(showAll, 5, data);
		} else if (type == 4) {
			addOptions(showAll, 4, data);
			addOptions(showAll, 5, data);
		} else if (type == 5) {
			addOptions(showAll, 5, data);
		}
	}

	function removeOptions(type) {
		selectType[type].find("option:first").nextAll().remove();
	}

	function addOptions(showAll, type, data) {
		// resoucePoolData : resoucePoolData,
		// zoneData : zoneData,
		// podData : podData,
		// clusterData : clusterData,
		// hostData : hostData
		var preResourceIds = [];
		var selectValue = null;
		if (type == 1) {
			//data = data.resoucePoolData;
			// preResourceIds = objectToArray(getOptionValues(-1));
			//selectValue = selectType[1].val();
		} else if (type == 2) {
			data = data.zoneData;
			preResourceIds = objectToArray(getOptionValues(1));
			selectValue = selectType[2].val();
		} else if (type == 3) {
			data = data.podData;
			preResourceIds = objectToArray(getOptionValues(2));
			selectValue = selectType[3].val();
		} else if (type == 4) {
			data = data.clusterData;
			preResourceIds = objectToArray(getOptionValues(3));
			selectValue = selectType[4].val();
		} else if (type == 5) {
			data = data.hostData;
			preResourceIds = objectToArray(getOptionValues(4));
			selectValue = selectType[5].val();
		}

		removeOptions(type);

		$.each(data, function(i, val) {
			// hypervisor KVM XenServer
			if ((showAll || (val.hypervisor && val.hypervisor.match(/KVM/g)))
					&& (type == 1 || jQuery.inArray(val.preResourceId, preResourceIds) != -1)) {
				selectType[type].append(createOption(val.resourceId, val.name));
			}
		});

		selectType[type].val(selectValue);
	}

	function objectToArray(values) {
		if (values == null) {
			return null;
		}

		var arrs = [];
		$.each(values, function(i, val) {
			arrs.push(val);
		});
		return arrs;
	}

	function exportTopData() {
		var excelForm = $('#reportData');
		excelForm.empty();
		excelForm.attr("action", createURL('listTopData') + '&cmsz=yes&export=true');
		$("<input type='hidden' name='timeAndTarget' value='统计时间：" + new Date().format("yyyy:MM:dd hh:mm:ss") + "    指标名称：" + getTargetText() + "'/>")
				.appendTo(excelForm);
		// target : getTarget(),
		// resoucePools : getOptionValues(1),
		// zones : getOptionValues(2),
		// pods : getOptionValues(3),
		// clusters : getOptionValues(4),
		// hosts : getOptionValues(5)
		$("<input type='hidden' name='target' value='" + getTarget() + "'/>").appendTo(excelForm);
		$.each(getOptionValues(1), function(i, val) {
			//$("<input type='hidden' name='resoucePools' value='" + val + "'/>").appendTo(excelForm);
		});
		$.each(getOptionValues(2), function(i, val) {
			$("<input type='hidden' name='zones' value='" + val + "'/>").appendTo(excelForm);
		});
		$.each(getOptionValues(3), function(i, val) {
			$("<input type='hidden' name='pods' value='" + val + "'/>").appendTo(excelForm);
		});
		$.each(getOptionValues(4), function(i, val) {
			$("<input type='hidden' name='clusters' value='" + val + "'/>").appendTo(excelForm);
		});
		$.each(getOptionValues(5), function(i, val) {
			$("<input type='hidden' name='hosts' value='" + val + "'/>").appendTo(excelForm);
		});
		$('#reportData').submit();
	}

})(jQuery, cloudStack);

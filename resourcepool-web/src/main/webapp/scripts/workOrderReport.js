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

	cloudStack.sections.workOrderReport = {
		title : '工单统计',
		id : 'workOrderReport',
		show : function(data) {
			var $panel = $(".panel").addClass("plot-view");

			var $toolbar = $("<div/>").addClass("toolbar");
			$toolbar.appendTo($panel);

			// 开始年月
			var $beginDiv = $("<div/>").css({
				margin : "5px 9px 5px 15px",
				width : "200px",
				float : "left"
			});
			$beginDiv.appendTo($toolbar);
			$("<label>开始年月：</label>").appendTo($beginDiv);
			var $beginSelectY = $("<select id='startDate_year'/>").css({
				width : "60px"
			});
			$beginSelectY.appendTo($beginDiv);
			for ( var y = 2000; y < 2030; y++) {
				$beginSelectY.append('<option value="' + y + '">' + y + '</option>');
			}

			var $beginSelectM = $("<select id='startDate_month'/>").css({
				width : "40px"
			});
			$beginSelectM.appendTo($beginDiv);
			for ( var m = 1; m <= 12; m++) {
				if (m < 10)
					$beginSelectM.append('<option value="0' + m + '">0' + m + '</option>');
				else
					$beginSelectM.append('<option value="' + m + '">' + m + '</option>');
			}

			// 截至年月
			var $endDiv = $("<div/>").css({
				margin : "5px 9px 5px 15px",
				width : "200px",
				float : "left"
			});
			$endDiv.appendTo($toolbar);
			$("<label>截至年月：</label>").appendTo($endDiv);
			var $endSelectY = $("<select id='endDate_year'/>").css({
				width : "60px"
			});
			$endSelectY.appendTo($endDiv);
			for ( var y = 2000; y < 2030; y++) {
				$endSelectY.append('<option value="' + y + '">' + y + '</option>');
			}

			var $endSelectM = $("<select id='endDate_month'/>").css({
				width : "40px"
			});
			$endSelectM.appendTo($endDiv);
			for ( var m = 1; m <= 12; m++) {
				if (m < 10)
					$endSelectM.append('<option value="0' + m + '">0' + m + '</option>');
				else
					$endSelectM.append('<option value="' + m + '">' + m + '</option>');
			}

			// 工单类型
			var $workDiv = $("<div/>").css({
				margin : "5px 9px 5px 15px",
				width : "250px",
				float : "left"
			});
			$workDiv.appendTo($toolbar);
			$("<label>工单类型：</label>").appendTo($workDiv);
			var $workSelect = $("<select id='workOrderTypeSelect'/>").css({
				width : "130px"
			}).appendTo($workDiv);

			initWorkOrderTypeAndStatus();
			$.each(cloudStack.sections.workOrderReport.type, function(i, type) {
				$workSelect.append('<option value="' + type.value + '">' + type.desc + '</option>');
			});

			$("<div id='bt_query' class='button add' style='float:left'>查询</div>").appendTo($toolbar);

			$("<div id='bt_excel' class='button add' style='float:left'>导出</div>").appendTo($toolbar);

			var excelForm = $('<form id="reportData" method="post" enctype="multipart/form-data"></form>');
			excelForm.appendTo($panel);
			$('<input id="startDate" name="startDate" type="hidden" />').appendTo(excelForm);
			$('<input id="endDate" name="endDate" type="hidden" />').appendTo(excelForm);
			$('<input id="workOrderType" name="workOrderType" type="hidden" />').appendTo(excelForm);
			$('<input id="workOrderTypeName" name="workOrderTypeName" type="hidden" />').appendTo(excelForm);

			var $dataDiv = $("<div/>").addClass("plot reportTable").appendTo($panel);
			$("<div id='result_info'/>").css({
				"text-align" : "center",
				color : "red",
				padding : "2px"
			}).appendTo($dataDiv);

			// var $dataUl = $("<ul/>").addClass("sysData
			// orderData").append("<li/>").appendTo($dataDiv);
			$('<div id="dataHover">工单数据</div>').addClass("dataTip columnTip").appendTo($dataDiv);
			$('<div id="choices"/>').addClass("dataTitle").appendTo($dataDiv);
			$('<div id="placeholder"/>').addClass("dataStatus").appendTo($dataDiv);

			initUI();

			$("<div/>").addClass("plot-list").listView(cloudStack.sections.workOrderReport, {}).appendTo($panel);
			$panel.find(".list-view .toolbar").remove();

		},

		type : [],

		status : [],

		listView : {
			firstClick : false,
			// section: 'instances',
			hideToolbar : true,
			filters : false,
			fields : {
				ym : {
					label : '月份'
				},
				s1 : {
					label : '待审批'
				},
				s2 : {
					label : '审批通过待处理'
				},
				s3 : {
					label : '审批未通过'
				},
				/*s4 : {
					label : '审批通过正在处理'
				},*/
				s5 : {
					label : '处理成功'
				},
				s6 : {
					label : '处理失败'
				},
				s7 : {
					label : '待回收'
				},
				s8 : {
					label : '已结束'
				},
				total : {
					label : '工单总数'
				}
			},

			dataProvider : function(args) {
				$("#result_info").text("查询中,请稍后.....");
				$("#dataHover").html("工单数据");
				$("#bt_query").attr("disabled", true);
				var data = [];

				$.ajax({
					url : createURL('getWorkOrderReport'),
					data : {
						cmsz : 'yes',
						startDate : getStartDate(),
						endDate : getEndDate(),
						workOrderType : getWorkOrderType()
					},
					success : function(json) {
						var items = json.listresponse.workorderreportvoobj;

						$("#bt_query").attr("disabled", false);
						if (!items) {
							$("#result_info").text("该时间段内没有相关数据");
						} else {
							$("#result_info").text("返回数据成功");
							$.each(items, function(i, val) {
								data.push({
									ym : val.obj[0],
									s1 : val.obj[1],
									s2 : val.obj[2],
									s3 : val.obj[3],
									//s4 : val.obj[4],
									s5 : val.obj[4],
									s6 : val.obj[5],
									s7 : val.obj[6],
									s8 : val.obj[7],
									total : val.obj[8],
								});
							});
						}

						if (items != undefined) {
						}
						args.response.success({
							data : data
						});

						// load data
						cloudStack.mon.getBarData(items);
					},
					error : function() {
						$("#bt_query").attr("disabled", false);
						$("#result_info").text(null);
					}
				});
			}

		}
	};

	var initWorkOrderTypeAndStatus = function() {
		$.ajax({
			url : createURL("listConfig"),
			dataType : "json",
			async : false,
			data : {
				cmsz : "yes", // configKey : "workorder_status%"
				configKey : "workorder_type%"
			},
			success : function(json) {
				workOrderType = json.configList;
				cloudStack.sections.workOrderReport.type = [];
				$.each(workOrderType, function(index, val) {
					cloudStack.sections.workOrderReport.type.push({
						key : val.key,
						value : val.value,
						desc : val.description
					});
				});
			}
		});

		$.ajax({
			url : createURL("listConfig"),
			dataType : "json",
			async : false,
			data : {
				cmsz : "yes", // configKey : "workorder_status%"
				configKey : "workorder_status%"
			},
			success : function(json) {
				workOrderType = json.configList;
				cloudStack.sections.workOrderReport.status = [];
				$.each(workOrderType, function(index, val) {
					cloudStack.sections.workOrderReport.status.push({
						key : val.key,
						value : val.value,
						desc : val.description
					});
				});
			}
		});
	};

	cloudStack.mon = {
		flag : true,
		option : {},
		datasets : {},
		getBarData : function(items) {
			var data1 = [];
			var data2 = [];
			var data3 = [];
			var data4 = [];
			var data5 = [];
			var data6 = [];
			var data7 = [];
			var ticks = [];
			if (items) {
				$.each(items, function(i, val) {
					//alert("(obj.series.label>>>"+obj.series.label+"<<<<obj.datapoint[1]>>>"+obj.datapoint[1]+"===="+obj);
					ticks.push([ i + 1, val.obj[0] ]);
					data1.push([ i + 1, val.obj[1] ]);
					data2.push([ i + 1, val.obj[2] ]);
					data3.push([ i + 1, val.obj[3] ]);
					data4.push([ i + 1, val.obj[4] ]);
					data5.push([ i + 1, val.obj[5] ]);
					data6.push([ i + 1, val.obj[6] ]);
					data7.push([ i + 1, val.obj[7] ]);
				});
			}
			var max = ticks.length + 1;

			cloudStack.mon.option = {
				lines : {
					show : true
				},
				points : {
					show : true
				},
				grid : {
					hoverable : true,
					clickable : false
				},
				series : {
					lines : {
						show : true
					},
					points : {
						show : true
					}
				},
				xaxis : {
					tickDecimals : 0,
					tickSize : 1,
					ticks : ticks,
					min : 1,
					max : max
				},
				yaxis : {
					ticks : 8,
					min : 0
				}
			};

			cloudStack.mon.datasets = {
				"option1" : {
					label : cloudStack.sections.workOrderReport.status[0].desc,
					data : data1,
					color : 0
				},
				"option2" : {
					label : cloudStack.sections.workOrderReport.status[1].desc,
					data : data2,
					color : 1
				},
				"option3" : {
					label : cloudStack.sections.workOrderReport.status[2].desc,
					data : data3,
					color : 2
				},
				"option4" : {
					label : cloudStack.sections.workOrderReport.status[3].desc,
					data : data4,
					color : 3
				},
				"option5" : {
					label : cloudStack.sections.workOrderReport.status[4].desc,
					data : data5,
					color : 4
				},
				"option6" : {
					label : cloudStack.sections.workOrderReport.status[5].desc,
					data : data6,
					color : 5
				},
				"option7" : {
					label : cloudStack.sections.workOrderReport.status[6].desc,
					data : data7,
					color : 6
				}
			};

			if (cloudStack.mon.flag) {
				$("#choices").empty();
				$.each(cloudStack.mon.datasets, function(key, val) {
					$("#choices").append(
							' <input type="checkbox" name="' + key + '" checked="checked" id="id' + key + '" style="vertical-align: middle;" />'
									+ ' <label for="id' + key + '">' + val.label + '</label>');
				});
				cloudStack.mon.flag = false;
			}

			$("#choices").find("input").click(cloudStack.mon.plotAccordingToChoices);
			cloudStack.mon.plotAccordingToChoices();
		},

		plotAccordingToChoices : function() {
			var data = [];
			$("#choices").find("input:checked").each(function() {
				var key = $(this).attr("name");
				if (key && cloudStack.mon.datasets[key])
					data.push(cloudStack.mon.datasets[key]);
			});
			if (data.length > 0) {
				$.plot($("#placeholder"), data, cloudStack.mon.option);
				$("#placeholder").bind("plothover", pieHover);
			}
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

	function checkDate() {
		if (getStartDate() > getEndDate()) {
			alert("开始时间不能大于结束时间");
			return false;
		}
		return true;
	}

	function pieHover(event, pos, obj) {
		if (!obj)
			return;
		$("#dataHover").html(obj.series.label + '：<span style="color:' + obj.series.color + '"><strong>' + obj.datapoint[1] + '</strong> 份</span>');
	}

	function initUI() {
		// init,默认最近6个月数据
		var today = new Date();
		var firstDay = new Date(today.getFullYear(), today.getMonth() - 5, 1);
		$('#startDate_year').val(firstDay.format("yyyy"));
		$('#startDate_month').val(firstDay.format("MM"));
		$('#endDate_year').val(today.format("yyyy"));
		$('#endDate_month').val(today.format("MM"));
		cloudStack.mon.flag = true;
		$("#bt_query").click(function() {
			if (checkDate()) {
				$('.list-view').listView('refresh');
			}
		});

		$("#bt_excel").click(function() {
			if (checkDate()) {
				exportOrderReport();
			}
		});
	}

	function getStartDate() {
		return $('#startDate_year').val() + '-' + $('#startDate_month').val();
	}

	function getEndDate() {
		return $('#endDate_year').val() + '-' + $('#endDate_month').val();
	}

	function getWorkOrderType() {
		return $('#workOrderTypeSelect').val();
	}

	function getWorkOrderTypeName() {
		return $('#workOrderTypeSelect option:selected').text();
	}

	function exportOrderReport() {
		$('#reportData').attr("action", createURL('workOrderReportExcel') + '&cmsz=yes');
		$('#startDate').val(getStartDate());
		$('#endDate').val(getEndDate());
		$('#workOrderType').val(getWorkOrderType());
		$('#workOrderTypeName').val(getWorkOrderTypeName());
		$('#reportData').submit();
	}

})(jQuery, cloudStack);

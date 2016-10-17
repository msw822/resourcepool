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

// 

//控制板	dashboard
//实例 instances
// 资源
// [基础架构
// 存储
// 网络
// 模版
// 资产管理]
//"resourceMenu", "system", "storage", "network", "templates", "property"
(function(cloudStack) {
	cloudStack.sections.instanceMenu = {
		title : '实例管理&nbsp;&nbsp;&nbsp;&nbsp;&nbsp>>',
		id : 'instanceMenu'
	};
})(cloudStack);
// 资源
// [基础架构
// 存储
// 网络
// 模版
// 资产管理]
//"resourceMenu", "system", "storage", "network", "templates", "property"
(function(cloudStack) {
	cloudStack.sections.resourceMenu = {
		title : '资源&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;>>',
		id : 'resourceMenu'
	};
})(cloudStack);

// 系统
// [账户
// 域
// 项目
// 关联性组
// 全局设置
// 资源池分配]
//"systemMenu", "accounts", "domains", "projects", "affinityGroups", "global-settings", "resourcePoolPermission"
(function(cloudStack) {
	cloudStack.sections.systemMenu = {
		title : '系统&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;>>',
		id : 'systemMenu'
	};
})(cloudStack);


// 服务审批 workOrders
// 服务方案 configuration

// 日志
// [事件
// 日志]
// "logMenu", "events", "logs"
(function(cloudStack) {
	cloudStack.sections.logMenu = {
		title : '日志&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;>>',
		id : 'logMenu'
	};
})(cloudStack);

// 统计报表
// [资源统计
// 工单统计
// topn统计]
// "statisticMenu", "resourceStatistics", "workOrderReport", "topData"
(function(cloudStack) {
	cloudStack.sections.statisticMenu = {
		title : '统计报表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;>>',
		id : 'statisticMenu'
	};
})(cloudStack);

(function(cloudStack) {
	cloudStack.sections.workOrderMenu = {
		title : '订单管理&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;>>',
		id : 'workOrderMenu'
	};
})(cloudStack);
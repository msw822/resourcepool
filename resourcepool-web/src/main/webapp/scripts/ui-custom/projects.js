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
(function(cloudStack, $) {
    var pageElems = cloudStack.uiCustom.projectsTabs = {
        /**
         * User management multi-edit
         */
        userManagement: function(args) {
            var multiEdit = !args.useInvites ?
                cloudStack.projects.addUserForm :
                cloudStack.projects.inviteForm;

            var $multi = $('<div>').multiEdit($.extend(true, {}, multiEdit, {
                context: args.context
            }));

            if (args.useInvites) {
                var $fields = $multi.find('form table').find('th, td');
                var $accountFields = $fields.filter(function() {
                    return $(this).hasClass('account');
                });
                var $emailFields = $fields.filter(function() {
                    return $(this).hasClass('email');
                });

                $multi.prepend(
                    $('<div>').addClass('add-by')
                    .append($('<span>').html(_l('label.add.by') + ':'))
                    .append(
                        $('<div>').addClass('selection')
                        .append(
                            $('<input>').attr({
                                type: 'radio',
                                name: 'add-by',
                                checked: 'checked'
                            }).click(function() {
                                $accountFields.show();
                                $emailFields.hide();
                                $emailFields.find('input').val('');

                                return true;
                            }).click()
                        )
                        .append($('<label>').html(_l('label.account')))
                        .append(
                            $('<input>').attr({
                                type: 'radio',
                                name: 'add-by'
                            }).click(function() {
                                $accountFields.hide();
                                $accountFields.find('input').val('');
                                $emailFields.show();

                                return true;
                            })
                        )
                        .append($('<label>').html(_l('label.email')))
                    )
                );
            }

            return $multi;
        },

        dashboardTabs: {
            overview: function() {
                var $dashboard = $('#template').find('.project-dashboard-view').clone();
                $dashboard.data('tab-title', _l('label.menu.dashboard'));

                var getData = function() {
                    // Populate data
                    $dashboard.find('[data-item]').hide();
                    var $loading = $('<div>').addClass('loading-overlay').prependTo($dashboard);
                    cloudStack.projects.dashboard({
                        response: {
                            success: function(args) {
                                $loading.remove();
                                var data = args.data;

                                // Iterate over data; populate corresponding DOM elements
                                $.each(data, function(key, value) {
                                    var $elem = $dashboard.find('[data-item=' + key + ']');

                                    // This assumes an array of data
                                    if ($elem.is('ul')) {
                                        $elem.show();
                                        var $liTmpl = $elem.find('li').remove();
                                        $(value).each(function() {
                                            var item = this;
                                            var $li = $liTmpl.clone().appendTo($elem).hide();

                                            $.each(item, function(arrayKey, arrayValue) {
                                                var $arrayElem = $li.find('[data-list-item=' + arrayKey + ']');

                                                $arrayElem.html(_s(arrayValue));
                                            });

                                            $li.attr({
                                                title: item.description
                                            });

                                            $li.fadeIn();
                                        });
                                    } else {
                                        $elem.each(function() {
                                            var $item = $(this);
                                            if ($item.hasClass('chart-line')) {
                                                $item.show().animate({
                                                    width: value + '%'
                                                });
                                            } else {
                                                $item.hide().html(_s(value)).fadeIn();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                };

                getData();

                $dashboard.find('.button.manage-resources').click(function() {
                    $('.navigation-item.network').click();
                });

                $dashboard.find('.info-box.events .button').click(function() {
                    $('.navigation-item.events').click();
                });

                return $dashboard;
            },

            users: function() {
                return $('<div>').addClass('management').data('tab-title', _l('label.menu.accounts'));
            },

            invitations: function() {
                return $('<div>').addClass('management-invite').data('tab-title', _l('label.invitations'));
            },

            resources: function(options) {
                if (!options) options = {};

                var $resources = $('<div>').addClass('resources').data('tab-title', _l('label.resources'));
                var $form = $('<form>');
                var $submit = $('<input>').attr({
                    type: 'submit'
                }).val(_l('提交申请'));

                cloudStack.projects.resourceManagement.dataProvider({
                    response: {
                        success: function(args) {
                            $(args.data).each(function() {
                                var resource = this;
                                var $field = $('<div>').addClass('field');
                                var $label = $('<label>').attr({
                                    'for': resource.type
                                }).html(_s(resource.label));
                                /**
                                 * added 
                                 * 新增最后一行的申请理由文本输入框
                                 */
                                if(resource.type==12){
                                	 var $textarea = $('<textarea>').attr({
                                     	rows:5,
                                         name: resource.name,
                                         value: resource.value,
                                         id: resource.type
                                     }).addClass('required');
                                	 $field.append($label, $textarea);
                                }else{
	                                var $input = $('<input>').attr({
	                                    type: 'text',
	                                    name: resource.name,
	                                    value: resource.value,
	                                    id: resource.type
	                                    /**	                                    
	                                     * add class number
	                                     */
	                                }).addClass('required number').attr("min", 1);
	                                $field.append($label, $input);
                                }
                                
                                $field.appendTo($form);
                            });

                            $form.validate();
                            $form.submit(function() {
                            	/**
                            	 * !$form.valid改为!$form.valid()
                            	 */
                                if (!$form.valid()) {
                                    return false;
                                }
                                /**
                                 * 增加项目扩容申请的时候对数值的判断
                                 */
                                $("#details-tab-resources form").find("input").each(function() {
                            		var value=$(this).val();
                            		var errorText="<label  generated='true' class='error'>Please enter a value greater than or equal to 1.</label>";
                            		if(value<1){
                            			$(this).parent().append(errorText);
                            		}
                            	});
                            	if($("#details-tab-resources form").find("label.error:visible").size()>0){
                            		return false;
                            	}
                                var $loading = $('<div>').addClass('loading-overlay').appendTo($form);

                                cloudStack.projects.resourceManagement.update({
                                	/**
                                	 * 新增一行参数供项目扩容申请context: cloudStack.context
                                	 */
                                	context: cloudStack.context,
                                    data: cloudStack.serializeForm($form),
                                    response: {
                                        success: function(args) {
                                            $loading.remove();
                                            $('.notifications').notifications('add', {
                                                section: 'dashboard',
                                                desc: 'label.update.project.resources',
                                                interval: 1000,
                                                poll: function(args) {
                                                    args.complete();
                                                }
                                            });
                                        }
                                    }
                                }, options.projectID);

                                return false;
                            });

                            $submit.appendTo($form);
                            $form.appendTo($resources);
                        }
                    }
                }, options.projectID);

                return $resources;
            }
        },

        /**
         * Projects dashboard
         */
        dashboard: function() {
            var tabs = {
                dashboard: pageElems.dashboardTabs.overview
            };

            // Only show management tabs to owner of project
            if (isAdmin() || isDomainAdmin() || (
                cloudStack.context.projects &&
                (cloudStack.context.projects[0].account == cloudStack.context.users[0].account)
            )) {
                tabs.users = pageElems.dashboardTabs.users;

                if (g_capabilities.projectinviterequired) {
                    tabs.invitations = pageElems.dashboardTabs.invitations;
                }

                if (isAdmin() || isDomainAdmin()) {
                    tabs.resources = pageElems.dashboardTabs.resources;
                }
            }

            var $tabs = $('<div>').addClass('tab-content').append($('<ul>'));
            var $toolbar = $('<div>').addClass('toolbar');

            // Make UI tabs
            $.each(tabs, function(tabName, tab) {
                var $tab = $('<li>').appendTo($tabs.find('ul:first'));
                var $tabContent = tab();
                var $tabLink = $('<a>')
                    .attr({
                        href: '#project-view-dashboard-' + tabName
                    })
                    .html($tabContent.data('tab-title'))
                    .appendTo($tab);
                var $content = $('<div>')
                    .appendTo($tabs)
                    .attr({
                        id: 'project-view-dashboard-' + tabName
                    })
                    .append($tabContent);
            });

            $tabs.find('ul li:first').addClass('first');
            $tabs.find('ul li:last').addClass('last');

            $tabs.bind('tabsshow', function(event, ui) {
                var $panel = $(ui.panel);
                var $management = $panel.find('.management');
                var $managementInvite = $panel.find('.management-invite');

                if ($management.size()) {
                    $management.children().remove();
                    $management.append(pageElems.userManagement({
                        context: cloudStack.context
                    }));

                    return true;
                }

                if ($managementInvite.size()) {
                    $managementInvite.children().remove();
                    $managementInvite.append(pageElems.userManagement({
                        context: cloudStack.context,
                        useInvites: true
                    }));
                }

                return true;
            });

            return $('<div>').addClass('project-dashboard')
                .append($.merge(
                    $toolbar,
                    $tabs.tabs()
                ));
        },

        /**
         * Add new project flow
         */
        newProjectForm: function() {
        	
            var $newProject = $('<div>').addClass('new-project');
            $newProject.append($('<div>').addClass('title').html(_l('label.create.project')));

            var $form = $('<form>');
            var $formDesc = $('<div>').addClass('form-desc');
            var $projectName = $('<div>').addClass('field name')
                .append($('<label>').attr('for', 'project-name').html(_l('label.project.name')))
                .append($('<input>').addClass('required disallowSpecialCharacters').attr({
                    type: 'text',
                    name: 'project-name',
                    id: 'project-name'
                }));
            var $projectDesc = $('<div>').addClass('field desc')
                .append($('<label>').attr('for', 'project-desc').html(_l('label.display.text')))
                .append($('<input>').addClass('required disallowSpecialCharacters').attr({
                    type: 'text',
                    name: 'project-display-text',
                    id: 'project-desc'
                }));
            var $projectmaxVM = $('<div>').addClass('field maxVm')
            .append($('<label>').attr('for', 'project-maxVm').html(_l('最大用户VM数')))
            .append($('<input>').addClass('required number').attr({
                type: 'text',
                name: 'project-maxVm',
                id: 'project-maxVm',
                value:'20'
            }));
            var $projectmaxIP = $('<div>').addClass('field maxIp')
            .append($('<label>').attr('for', 'project-maxIp').html(_l('最大公用IP数')))
            .append($('<input>').addClass('required number').attr({
                type: 'text',
                name: 'project-maxIp',
                id: 'project-maxIp',
                value:'20'
            }));
            var $projectmaxSpeed = $('<div>').addClass('field maxSpeed')
            .append($('<label>').attr('for', 'project-maxSpeed').html(_l('最大卷数')))
            .append($('<input>').addClass('required number').attr({
                type: 'text',
                name: 'project-maxSpeed',
                id: 'project-maxSpeed',
                value:'20'
            }));
            
            var $projectmaxCopy = $('<div>').addClass('field maxCopy')
            .append($('<label>').attr('for', 'project-maxCopy').html(_l('最大快照数')))
            .append($('<input>').addClass('required number').attr({
                type: 'text',
                name: 'project-maxCopy',
                id: 'project-maxCopy',
                value:'20'
            }));
            
            var $projectmaxTemps = $('<div>').addClass('field maxTemps')
            .append($('<label>').attr('for', 'project-maxTemps').html(_l('最大模板数')))
            .append($('<input>').addClass('required number').attr({
                type: 'text',
                name: 'project-maxTemps',
                id: 'project-maxTemps',
                value:'20'
            }));
            
            var $projectmaxNets = $('<div>').addClass('field maxNets')
            .append($('<label>').attr('for', 'project-maxSpeed').html(_l('最大网络数')))
            .append($('<input>').addClass('required number').attr({
                type: 'text',
                name: 'project-maxNets',
                id: 'project-maxNets',
                value:'20'
            }));
            
            var $projectmaxVPC = $('<div>').addClass('field maxVPC')
            .append($('<label>').attr('for', 'project-maxSpeed').html(_l('最大VPC数')))
            .append($('<input>').addClass('required number').attr({
                type: 'text',
                name: 'project-maxVPC',
                id: 'project-maxVPC',
                value:'20'
            }));
            
            var $projectmaxCpu = $('<div>').addClass('field maxCpu')
            .append($('<label>').attr('for', 'project-maxSpeed').html(_l('最大CPU内核数')))
            .append($('<input>').addClass('required number').attr({
                type: 'text',
                name: 'project-maxCpu',
                id: 'project-maxCpu',
                value:'40'
            }));
            
            var $projectmaxMem = $('<div>').addClass('field maxMem')
            .append($('<label>').attr('for', 'project-maxMem').html(_l('最大内存(MB) ')))
            .append($('<input>').addClass('required number').attr({
                type: 'text',
                name: 'project-maxMem',
                id: 'project-maxMem',
                value:'40960'
            }));
            
            var $projectmainDisk = $('<div>').addClass('field mainDisk')
            .append($('<label>').attr('for', 'project-maxSpeed').html(_l('最大主存储(GB)')))
            .append($('<input>').addClass('required number').attr({
                type: 'text',
                name: 'project-mainDisk',
                id: 'project-mainDisk',
                value:'200'
            }));
            
            var $projectsecondDisk = $('<div>').addClass('field secondDisk')
            .append($('<label>').attr('for', 'project-secondDisk').html(_l('最大辅助存储(GB)')))
            .append($('<input>').addClass('required number').attr({
                type: 'text',
                name: 'project-secondDisk',
                id: 'project-secondDisk',
                value:'400'
            }));
            
//            var $selectPool=$('<select>').attr({
//                name: 'resourcePool',
//                id: 'resourcePool'
//            });
            
//            $.ajax({
//				url : createURL("listresourcepool"),
//				async : false,
//				data : {
//					cmsz : "yes"
//				},
//				success : function(json) {
//					/*$selectPool.append("<option value='all'>所有资源池</option>");
//					for(var i=0;i<json.root.length;i++){
//						var option=$('<option>').attr({value:json.root[i].resourcePoolId}).html(_l(json.root[i].name));
//							$selectPool.append(option);
//					}*/
//					var allPool="<option value='all'>所有资源池</option>";
//					var productPool=null;
// 					var testPool=null;
// 					if(json.root&&json.root.length>0){
// 						productPool=$('<option>').attr({value:json.root[0].resourcePoolId}).html(_l(json.root[0].name));
// 						testPool=$('<option>').attr({value:json.root[1].resourcePoolId}).html(_l(json.root[1].name));
// 						//var option=$('<option>').attr({value:json.root[i].resourcePoolId}).html(_l(json.root[i].name));
// 							//$step.find('.select-zone select.pool').append(option);
// 					}
// 					if($(".project-switcher select").val()!=-1){
//     					 $.ajax({
//              				url : createURL("listProjectResourcePoolPermission"),
//              				async : false,
//              				data : {
//              					cmsz : "yes",
//              					key:3,
//              					value:$(".project-switcher select").val()
//              				},
//              				success : function(data) {
//              					if(data&&data.resourcepoolpermission.resourcepoolpermissionobj.productionPool==1){
//              						$selectPool.append(productPool);
//              					}
//              					if(data&&data.resourcepoolpermission.resourcepoolpermissionobj.testingPool==1){
//              						$selectPool.append(testPool);
//              					}
//              				}
//              			});
// 					}else{
// 						 $.ajax({
//               				url : createURL("listProjectResourcePoolPermission"),
//               				async : false,
//               				data : {
//               					cmsz : "yes",
//               					key:2,
//               					value:cloudStack.context.users[0].domainid
//               				},
//               				success : function(data) {
//               					if(data){
//                   					if(data&&data.resourcepoolpermission.resourcepoolpermissionobj.productionPool==1){
//                   						$selectPool.append(productPool);
//                   					}
//                   					if(data&&data.resourcepoolpermission.resourcepoolpermissionobj.testingPool==1){
//                   						$selectPool.append(testPool);
//                   					}
//               					}
//               				}
//               			});
// 					}
// 					if($selectPool.find("option").size()==0){
// 						$.ajax({
//               				url : createURL("listProjectResourcePoolPermission"),
//               				async : false,
//               				data : {
//               					cmsz : "yes",
//               					key:1,
//               					value:"account"
//               				},
//               				success : function(data) {
//               					if(data&&data.resourcepoolpermission.resourcepoolpermissionobj.productionPool==1){
//               						$selectPool.append(productPool);
//               					}
//               					if(data&&data.resourcepoolpermission.resourcepoolpermissionobj.testingPool==1){
//               						$selectPool.append(testPool);
//               					}
//               				}
//               			});
// 					}else if($selectPool.find("option").size()==2){
// 						$(allPool).insertBefore(productPool);
// 					}
//				}
//			});
//            var $resourcePool = $('<div>').addClass('field resourePool')
//            .append($('<label>').attr('for', 'resourePool').html(_l('一级资源池')))
//            .append($selectPool);
            var $projectReason = $('<div>').addClass('field projectReason')
            .append($('<label>').attr('for', 'projectReason').html(_l('申请理由')))
            .append($('<textarea>').attr({
                name: 'projectReason',
                rows:5,
                id: 'projectReason'
            }).addClass('required'));
            var $submit = $('<input>').attr({
                type: 'submit'
            }).val(_l('提交申请'));
            var $cancel = $('<div>').addClass('button cancel').html(_l('label.cancel'));
            var $loading = $('<div>').addClass('loading-overlay');

            // Form events/validation
            $form.validate();
            $form.submit(function() {
                /**
                 * 增加申请项目提交工单时对数值的判断
                 */
                $(".new-project form").find("input.number").each(function() {
            		var value=$(this).val();
            		var errorText="<label  generated='true' class='error'>数值不得小于1</label>";
            		if(value<1){
            			$(this).parent().append(errorText);
            		}
            	});
                $(".new-project form").find("input.number").live('blur',function() {
            		var value=$(this).val();
            		var errorText="<label  generated='true' class='error'>数值不得小于1</label>";
            		if(value>0){
            			$(this).parent().find("label.error").remove();
            		}
            		else if(value<1&&$(this).parent().find("label.error:visible").size()==0){
            			$(this).parent().append(errorText);
            		}
            	});
            	if($(".new-project form").find("label.error:visible").size()>0){
            		return false;
            	}
            	if (!$form.valid()) return false;
                $form.prepend($loading);

                cloudStack.projects.add({
                    context: cloudStack.context,
                    data: cloudStack.serializeForm($form),
                    response: {
                        success: function(args) {
                            var project = args.data;
                            var $projectSwitcher = $('div.project-switcher');

                            $(window).trigger('cloudStack.fullRefresh');

                            // dynamically add newly created project into project switcher
                            $projectSwitcher.find('select').append(
                                $('<option>').val(project.id).html(project.name)
                            );

                            $loading.remove();

                            // Confirmation
                            $form.replaceWith(function() {
                                var $confirm = $('<div>').addClass('confirm');

                                // Update title with project name
                                $newProject.find('.title').html(_s(args.data.name));

                                // Show field data
                                $confirm.append($projectName).find('input').replaceWith( // Name
                                    $('<span>').addClass('value').html(_s(
                                        args.data.name
                                    ))
                                );
                                $confirm.append($projectDesc).find('input').replaceWith( // Display text
                                    $('<span>').addClass('value').html(_s(
                                        args.data.displayText
                                    ))
                                );
                                $confirm.append($projectmaxVM).find('input').replaceWith( // Display text
                                		 $('<span>').addClass('value').html(_s(
                                                 args.data.maxVm
                                             ))
                                             );
                                $confirm.append($projectmaxIP).find('input').replaceWith( // Display text
                               		 $('<span>').addClass('value').html(_s(
                                                args.data.maxIp
                                            ))
                                            );
                                $confirm.append($projectmaxSpeed).find('input').replaceWith( // Display text
                                  		 $('<span>').addClass('value').html(_s(
                                                   args.data.maxSpeed
                                               ))
                                               );
                                $confirm.append($projectmaxCopy).find('input').replaceWith( // Display text
                                 		 $('<span>').addClass('value').html(_s(
                                                  args.data.maxCopy
                                              ))
                                              );
                                $confirm.append($projectmaxTemps).find('input').replaceWith( // Display text
                                 		 $('<span>').addClass('value').html(_s(
                                                  args.data.maxTemps
                                              ))
                                              );
                                $confirm.append($projectmaxNets).find('input').replaceWith( // Display text
                                 		 $('<span>').addClass('value').html(_s(
                                                  args.data.maxNets
                                              ))
                                              );
                                $confirm.append($projectmaxVPC).find('input').replaceWith( // Display text
                                 		 $('<span>').addClass('value').html(_s(
                                                  args.data.maxVPC
                                              ))
                                              );
                                $confirm.append($projectmaxCpu).find('input').replaceWith( // Display text
                                 		 $('<span>').addClass('value').html(_s(
                                                  args.data.maxCpu
                                              ))
                                              );
                                $confirm.append($projectmaxMem).find('input').replaceWith( // Display text
                                 		 $('<span>').addClass('value').html(_s(
                                                  args.data.maxMem
                                              ))
                                              );
                                $confirm.append($projectmainDisk).find('input').replaceWith( // Display text
                                 		 $('<span>').addClass('value').html(_s(
                                                  args.data.mainDisk
                                              ))
                                              );
                                $confirm.append($projectsecondDisk).find('input').replaceWith( // Display text
                                 		 $('<span>').addClass('value').html(_s(
                                                  args.data.secondDisk
                                              ))
                                              );
                               /* var textPool="生产池";
                                args.data.resourcePool=="0"?textPool="生产池":textPool="测试池";
                                $confirm.append($resourcePool).find('select').replaceWith( // Display text
                                		 $('<select>').addClass('resourcePool').append($('<option>').attr({value:args.data.resourcePool}).html(_s(
                                                 textPool
                                             )))
                                             );*/
//                                $confirm.append($resourcePool).find('select').replaceWith( // Display text
//                                		 $('<span>').addClass('value').html(_s(
//                                                 args.data.secondDisk=="0"?'生产池':'测试池'
//                                             ))
//                                             );
                                $confirm.append($projectReason).find('textarea').replaceWith( // Display text
                               		 $('<span>').addClass('value').html(_s(
                                                args.data.projectReason
                                            ))
                                            );
                                var $buttons = $('<div>').addClass('buttons');
                                var $addAccountButton = $('<div>').addClass('button confirm').html(_l('label.add.accounts'));

                                $addAccountButton.click(function() {
                                    // Show add user form
                                    $confirm.replaceWith(function() {
                                        var $userManagement = pageElems.userManagement({
                                            context: $.extend(true, {}, cloudStack.context, {
                                                projects: [project]
                                            }),
                                            useInvites: cloudStack.projects.requireInvitation()
                                        });
                                        var $nextButton = $('<div>').addClass('button confirm next').html(_l('label.next'));

                                        $newProject.find('.title').html(
                                            cloudStack.projects.requireInvitation() ?
                                            _l('label.invite.to') + ' ' + _s(args.data.name) :
                                            _l('label.add.accounts.to') + ' ' + _s(args.data.name)
                                        );
                                        $nextButton.appendTo($userManagement).click(function() {
                                            $newProject.find('.title').html(_l('label.review'));
                                            $userManagement.replaceWith(function() {
                                                var $review = $('<div>').addClass('review');
                                                var $projectData = $('<div>').addClass('project-data');

                                                // Basic project data
                                                $review.append($projectData);
                                                $projectData.append($projectName).find('input').replaceWith( // Name
                                                    $('<span>').addClass('value').html(_s(
                                                        args.data.name
                                                    ))
                                                );
                                                $projectData.append($projectDesc).find('input').replaceWith( // Display text
                                                    $('<span>').addClass('value').html(_s(
                                                        args.data.displayText
                                                    ))
                                                );
                                                $projectData.append($projectmaxVM).find('input').replaceWith( // Display text
                                                        $('<span>').addClass('value').html(_s(
                                                            args.data.maxVm
                                                        ))
                                                    );
                                                $projectData.append($projectmaxIP).find('input').replaceWith( // Display text
                                                        $('<span>').addClass('value').html(_s(
                                                            args.data.maxIp
                                                        ))
                                                    );
                                                $projectData.append($projectmaxSpeed).find('input').replaceWith( // Display text
                                                        $('<span>').addClass('value').html(_s(
                                                            args.data.maxSpeed
                                                        ))
                                                    );
                                                $projectData.append($projectmaxCopy).find('input').replaceWith( // Display text
                                                        $('<span>').addClass('value').html(_s(
                                                            args.data.maxCopy
                                                        ))
                                                    );
                                                $projectData.append($projectmaxTemps).find('input').replaceWith( // Display text
                                                        $('<span>').addClass('value').html(_s(
                                                            args.data.maxTemps
                                                        ))
                                                    );
                                                $projectData.append($projectmaxNets).find('input').replaceWith( // Display text
                                                        $('<span>').addClass('value').html(_s(
                                                            args.data.maxNets
                                                        ))
                                                    );
                                                $projectData.append($projectmaxVPC).find('input').replaceWith( // Display text
                                                        $('<span>').addClass('value').html(_s(
                                                            args.data.maxVPC
                                                        ))
                                                    );
                                                $projectData.append($projectmaxCpu).find('input').replaceWith( // Display text
                                                        $('<span>').addClass('value').html(_s(
                                                            args.data.maxCpu
                                                        ))
                                                    );
                                                $projectData.append($projectmaxMem).find('input').replaceWith( // Display text
                                                        $('<span>').addClass('value').html(_s(
                                                            args.data.maxMem
                                                        ))
                                                    );
                                                $projectData.append($projectmainDisk).find('input').replaceWith( // Display text
                                                        $('<span>').addClass('value').html(_s(
                                                            args.data.mainDisk
                                                        ))
                                                    );
                                                $projectData.append($projectsecondDisk).find('input').replaceWith( // Display text
                                                        $('<span>').addClass('value').html(_s(
                                                            args.data.secondDisk
                                                        ))
                                                    );
//                                                $projectData.append($resourcePool).find('select').replaceWith( // Display text
//                                                        $('<span>').addClass('value').html(_s(
//                                                            args.data.resourcePool
//                                                        ))
//                                                    );
                                                $projectData.append($projectReason).find('textarea').replaceWith( // Display text
                                                        $('<span>').addClass('value').html(_s(
                                                            args.data.projectReason
                                                        ))
                                                    );
                                                // User/resouce tabs
                                                var $tabs = $('<div>').addClass('tabs resources').appendTo($review);
                                                var $ul = $('<ul>').appendTo($tabs)
                                                    .append(
                                                        // Users tab
                                                        $('<li>').addClass('first').append(
                                                            $('<a>').attr({
                                                                href: '#new-project-review-tabs-users'
                                                            }).html(
                                                                cloudStack.projects.requireInvitation() ?
                                                                _l('label.invitations') : _l('label.accounts')
                                                            )
                                                        )
                                                );

                                                if (isAdmin() || isDomainAdmin()) {
                                                    $ul.append(
                                                        // Resources tab
                                                        $('<li>').addClass('last').append(
                                                            $('<a>').attr({
                                                                href: '#new-project-review-tabs-resouces'
                                                            }).html(_l('label.resources'))
                                                        )
                                                    );
                                                }

                                                var $users = $('<div>').attr({
                                                    id: 'new-project-review-tabs-users'
                                                }).appendTo($tabs);
                                                cloudStack.context.projects = [project];

                                                var $resources;
                                                if (isAdmin() || isDomainAdmin()) {
                                                    $resouces = $('<div>')
                                                        .attr({
                                                            id: 'new-project-review-tabs-resouces'
                                                        })
                                                        .appendTo($tabs)
                                                        .append(pageElems.dashboardTabs.resources);
                                                }

                                                $tabs.tabs();

                                                $users.listView({
                                                    listView: {
                                                        id: 'project-accounts',
                                                        disableInfiniteScrolling: true,
                                                        fields: !cloudStack.projects.requireInvitation() ? {
                                                            username: {
                                                                label: _l('label.account')
                                                            }
                                                        } : {
                                                            account: {
                                                                label: _l('label.invited.accounts')
                                                            }
                                                        },
                                                        dataProvider: function(args) {
                                                            setTimeout(function() {
                                                                args.response.success({
                                                                    data: $.map($userManagement.find('.data-item tr'), function(elem) {
                                                                        // Store previous user data in list table
                                                                        return !cloudStack.projects.requireInvitation() ? {
                                                                            username: $(elem).find('td.username span').html()
                                                                        } : {
                                                                            account: $(elem).find('td.account span').html()
                                                                        };
                                                                    })
                                                                });
                                                            }, 0);
                                                        }
                                                    }
                                                });

                                                // Save button
                                                var $saveButton = $nextButton.clone().appendTo($review);
                                                $saveButton.html(_l('label.save'));
                                                $saveButton.click(function() {
                                                    $('#new-project-review-tabs-resouces').find('form').submit();
                                                    $('.ui-dialog, .overlay').remove();
                                                });

                                                $laterButton.click(function() {
                                                    $(':ui-dialog, .overlay').remove();

                                                    return false;
                                                });

                                                return $review;
                                            });

                                            $(':ui-dialog').dialog('option', 'position', 'center');
                                        });
                                        $laterButton.html(_l('label.close')).appendTo($userManagement);

                                        return $userManagement;
                                    });

                                    $(':ui-dialog').dialog('option', 'position', 'center');

                                    return false;
                                });

                                var $laterButton = $('<div>').addClass('button later').html(_l('label.remind.later'));
                                $laterButton.click(function() {
                                    $(':ui-dialog, .overlay').remove();

                                    return false;
                                });

                                $buttons.appendTo($confirm).append($.merge(
                                    $addAccountButton, $laterButton
                                ));

                                return $confirm;
                            });
                        },
                        error: cloudStack.dialog.error(function() {
                            $loading.remove();
                        })
                    }
                });

                return false;
            });

            $cancel.click(function() {
                $(':ui-dialog, .overlay').remove();
            });
            
            return $newProject
                .append(
                    $form
                    .append($formDesc)
                    .append($projectName)
                    .append($projectDesc)
                    .append($projectmaxVM)
                    .append($projectmaxIP)
                    .append($projectmaxSpeed)
                    .append($projectmaxCopy)
                    .append($projectmaxTemps)
                    .append($projectmaxNets)
                    .append($projectmaxVPC)
                    .append($projectmaxCpu)
                    .append($projectmaxMem)
                    .append($projectmainDisk)
                    .append($projectsecondDisk)
//                     .append($resourcePool)
                     .append($projectReason)
                    .append($cancel)
                    .append($submit)
            );
        },

        /**
         * Project selection list
         */
        selector: function(args) {
            var $selector = $('<div>').addClass('project-selector');
            var $toolbar = $('<div>').addClass('toolbar');
            var $list = $('<div>').addClass('listing')
                .append($('<div>').addClass('header').html(_l('label.name')))
                .append($('<div>').addClass('data').append($('<ul>')));
            var $searchForm = $('<form>');
            var $search = $('<div>').appendTo($toolbar).addClass('search')
                .append(
                    $searchForm
                    .append($('<input>').attr({
                        type: 'text'
                    }))
                    .append($('<input>').attr({
                        type: 'submit'
                    }).val(''))
            );
            var $projectSelect = args.$projectSelect;
            var $cancel = $('<div>').addClass('button cancel').html(_l('label.cancel'));

            // Get project data
            var loadData = function(complete, options) {
                cloudStack.projects.dataProvider({
                    projectName: options ? options.projectName : null,
                    context: cloudStack.context,
                    response: {
                        success: function(args) {
                            var data = args.data;

                            $projectSelect.find('option').remove();
                            $(data).each(function() {
                                var displayText = this.displayText ? this.displayText : this.name;

                                $('<li>')
                                    .data('json-obj', this)
                                    .html(_s(displayText))
                                    .appendTo($list.find('ul'));

                                // Populate project select
                                $('<option>')
                                    .appendTo($projectSelect)
                                    .data('json-obj', this)
                                    .html(_s(displayText));
                            });

                            cloudStack.evenOdd($list, 'li', {
                                even: function($elem) {
                                    $elem.addClass('even');
                                },

                                odd: function($elem) {
                                    $elem.addClass('odd');
                                }
                            });

                            if (complete) complete();
                        }
                    }
                });
            };

            // Search form
            $searchForm.submit(function() {
                $list.find('li').remove();
                loadData(null, {
                    projectName: $(this).find('input[type=text]').val()
                });

                return false;
            });

            //Cancel button
            $cancel.click(function() {
                $(':ui-dialog, .overlay').remove();
                $('.select.default-view').click();
            });


            // Initial load
            loadData(function() {
                if (!$list.find('li').size()) {
                    cloudStack.dialog.notice({
                        message: isAdmin() || isDomainAdmin() || g_userProjectsEnabled ? _l('message.no.projects') : _l('message.no.projects.adminOnly')
                    }).closest('.ui-dialog');
                    $.merge($selector, $('.overlay')).remove();
                    $('.select.default-view').click();
                } else {
                    $selector.dialog({
                        title: _l('label.select.project'),
                        dialogClass: 'project-selector-dialog',
                        closeOnEscape: false,
                        width: 420
                    }).closest('.ui-dialog').overlay();
                }
            });

            // Project item click event
            $selector.click(function(event) {
                var $target = $(event.target);

                if ($target.is('li')) {
                    cloudStack.context.projects = [$target.data('json-obj')];
                    showDashboard();

                    var $switcher = $('.select.project-view');
                    var projectName = _s(cloudStack.context.projects[0].name);

                    $switcher.attr('title', projectName);
                    if (projectName.length > 10) {
                        projectName = projectName.substr(0, 10).concat('...');
                    }

                    // Put project name in header
                    $switcher.html('<span class="icon">&nbsp;</span>' + projectName);


                    $.merge($selector, $('.overlay')).remove();

                    // Select active project
                    $projectSelect
                        .find('option').attr('selected', '')
                        .filter(function() {
                            return $(this).data('json-obj').name == _s(cloudStack.context.projects[0].name);
                        }).attr('selected', 'selected');

                    ////
                    // Hidden for now
                    //$projectSelect.parent().show();
                }
            });

            return $selector
                .append($toolbar)
                .append($list)
                .append($cancel);
        }
    };

    /**
     * Show project-mode appearance on CloudStack UI
     */
    var applyProjectStyle = function() {
        var $container = $('#cloudStack3-container');
        $container.addClass('project-view');
    };

    /**
     * Initiate new project flow
     */
    var addProject = function() {
        pageElems.newProjectForm().dialog({
            title: 'New Project',
            closeOnEscape: false,
            width: 760
        }).closest('.ui-dialog').overlay();
    };

    var deleteProject = function(args) {
        var projectId = args.id;
        var $projectSwitcher = $('div.project-switcher');
        var contextProjectId = cloudStack.context.projects ? cloudStack.context.projects[0].id : -1;

        $projectSwitcher.find('option[value="' + projectId + '"]').remove();

        //return to default view if current project is deleted
        if (contextProjectId == projectId) {
            $projectSwitcher.find('select').trigger('change');
        }
        return false;
    };

    /**
     * Show the dashboard, in panel
     */
    var showDashboard = function() {
        var $browser = $('#browser .container');
        applyProjectStyle($('html body'));

        // Cleanup project context
        if (cloudStack.context.projects)
            cloudStack.context.projects[0].isNew = false;

        $browser.cloudBrowser('removeAllPanels');
        $browser.cloudBrowser('addPanel', {
            title: _l('label.project.dashboard'),
            complete: function($newPanel) {
                $('#navigation li.dashboard').addClass('active').siblings().removeClass('active');
                $newPanel.append(pageElems.dashboard);
            }
        });
    };

    /**
     * Projects entry point
     */
    cloudStack.uiCustom.projects = function(args) {
        var $dashboardNavItem = $('#navigation li.navigation-item.dashboard');

        // Use project dashboard
        var event = function() {
            if (!$('#cloudStack3-container').hasClass('project-view')) {
                // No longer in project view, go back to normal dashboard
                $dashboardNavItem.unbind('click', event);

                return true;
            }

            $(this).addClass('active');
            $(this).siblings().removeClass('active');

            if (cloudStack.context.projects && cloudStack.context.projects[0]) {
                showDashboard();
            }

            return false;
        };
        $dashboardNavItem.bind('click', event);

        if (args.alreadySelected) {
            showDashboard();
        } else {
            pageElems.selector(args);
        }
    };

    /**
     * New project event
     */
    $(window).bind('cloudStack.newProject', function() {
        addProject();
    });


    $(window).bind('cloudStack.deleteProject', function(event, args) {
        deleteProject({
            id: args.data.id
        });
    });
})(cloudStack, jQuery);

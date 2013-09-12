var userDetail = {
		
		leave : function (){
			var me = this;
			me.emptyUpdateUserTab();
		},

		visit : function (){
			this.init();
		},
		
		init : function(){
			var me = this;
			me.initLocaleUserTab();
			me.initUpdateUserElements();
			me.addUpdateUserEventListeners();
			me.showUserDetailInfo();
			openSubPage('cropUserpicturePanel','previewPicture.html','content',cropUserPicture,null);
		},
		
		
		initLocaleUserTab:function (){
			
			$(".updateUserTable").i18n();
			
			$('#updateUserSexRadioButton1').text(i18n.t("userManagement.sex.male"));
			$('#updateUserSexRadioButton2').text(i18n.t("userManagement.sex.female"));
			
			$('#userBasicInfoLabel').text(i18n.t("userManagement.page.userDetail.basicInfo"));
			$('#userAdvancedInfoLabel').text(i18n.t("userManagement.page.userDetail.advancedInfo"));
			$('#userPictrueLabel').text(i18n.t("userManagement.page.userDetail.userPictrue"));
			$('#userJobInfoLabel').text(i18n.t("userManagement.page.userDetail.jobInfo"));
			
			$('#updateUserBasicInfoButton').val(i18n.t("userManagement.button.saveBasicInfo"));
		    $('#updateUserAdvancedInfoButton').val(i18n.t("userManagement.button.saveAdvancedInfo"));
		    $('#updateUserJobInfoButton').val(i18n.t("userManagement.button.saveJobInfo"));
		},
		
		emptyUpdateUserTab:function (){
			
			$("#updateUserPhoneInput").jqxMaskedInput({value: null });
			$('#updateUserBirthdayInput').jqxDateTimeInput({value:AppUtil.findDateTime("2000-01-01 00:00:00")});
			
			$("#updateUserSexRadioButton1").jqxRadioButton({checked: true});
		    $("#updateUserSexRadioButton2").jqxRadioButton({checked: false});
		    $('#acceptInput').jqxCheckBox({checked: false});
		    $("#updateUserRolesCombobox").jqxComboBox('uncheckAll');
		    
			$("updateUserSexInput").val(i18n.t("userManagement.sex.male"));
			$("#updateUserIdInput").val(null);
			$("#updateUserAccountInput").val(null);
			$("#updateUserRealNameInput").val(null);
			$("#updateUserPasswordInput").val(null);
			$("#updateUserPasswordConfirmInput").val(null);
			$("#updateUserAddressInput").val(null);
			
			$("#updateUserDepartmentInput").val(null);
			$("#updateUserRolesInput").val(null);
			$("#updateUserRolesInput").val(null);
			$("#updateUserEmailInput").val(null);
			$("#updateUserResult").text("");
		},

		initUpdateUserElements:function () {
			
		    $('#updateUserBasicInfoButton').jqxButton({ width: 100, height: 25, theme: theme });
		    $('#updateUserAdvancedInfoButton').jqxButton({ width: 100, height: 25, theme: theme });
		    $('#updateUserJobInfoButton').jqxButton({ width: 100, height: 25, theme: theme });
		    
		  //  $("#updateUserAddressInput").jqxMaskedInput({ mask: '省-市-区-街道-门牌号', width: 150, height: 22, theme: theme });
		    $("#updateUserPhoneInput").jqxMaskedInput({ mask: '### #### ####', width: 150, height: 25, theme: theme });
		    $('.updateUserTextInput').jqxInput({width: 150, height: 25, theme: theme });
		    $('#updateUserAccountInput').jqxInput({disabled: true });
		    
		    var d1 = new Date();
		    $('#updateUserBirthdayInput').jqxDateTimeInput({ theme: theme,width: 150, height: 22,formatString: "yyyy/MM/dd", value: d1 });
		    
			$("#updateUserSexRadioButton1").jqxRadioButton({ width: 70, height: 25,checked: true, theme: theme });
			$("#updateUserSexRadioButton2").jqxRadioButton({ width: 70, height: 25,  theme: theme });
			$("updateUserSexInput").val(i18n.t("userManagement.sex.male"));
		    
			var userDepartmentData = [
			          				{ value: 2, label: i18n.t("userManagement.department.BUSSINESS") },
			          				{ value: 3, label: i18n.t("userManagement.department.PRODUCTION") },
			          				{ value: 4, label: i18n.t("userManagement.department.FINANCE") },
			          				{ value: 5, label: i18n.t("userManagement.department.PERSONNEL") },
			          				{ value: 6, label: i18n.t("userManagement.department.DEVERLOPE") },
			          				{ value: 7, label: i18n.t("userManagement.department.MANAGEMENT") }
			                  ];
			              
		  	// Create a jqxComboBox
		  	$("#updateUserDepartmentCombobox").jqxComboBox({ 
		  		checkboxes: true, 
		  		source: userDepartmentData, 
		  		displayMember: "label", 
		  		valueMember: "value", 
		  		width: 150, 
		  		height: 25, 
		  		theme: theme 
		  	});
		  
		    var userRoleData = [
						{ value: 2, label: i18n.t("userManagement.role.ANONYMOUS") },
						{ value: 3, label: i18n.t("userManagement.role.CUSTOMER") },
						{ value: 4, label: i18n.t("userManagement.role.CUSTOMER_VIP1") },
						{ value: 5, label: i18n.t("userManagement.role.CUSTOMER_VIP2") },
						{ value: 6, label: i18n.t("userManagement.role.WELCOMER") },
						{ value: 7, label: i18n.t("userManagement.role.CHEF") },
						{ value: 8, label: i18n.t("userManagement.role.WAITER") },
						{ value: 9, label: i18n.t("userManagement.role.CASHIER") },
						{ value: 10, label: i18n.t("userManagement.role.MANAGER") },
						{ value: 11, label: i18n.t("userManagement.role.ADMIN") }
		        ];
		    
			// Create a jqxComboBox
			$("#updateUserRolesCombobox").jqxComboBox({ 
				checkboxes: true, 
				source: userRoleData, 
				displayMember: "label", 
				valueMember: "value", 
				width: 150, 
				height: 25, 
				theme: theme 
			});
		    // initialize validator.
		    $('#updateUserInfoForm').jqxValidator({
		     rules: [
		            
		            { input: '#updateUserPasswordInput', message: i18n.t("userManagement.validation.message.requirePassword"), action: 'keyup, blur', rule: 'required' },
		            { input: '#updateUserPasswordInput', message: i18n.t("userManagement.validation.message.passwordLength"), action: 'keyup, blur', rule: 'length=6,20' },
		            { input: '#updateUserPasswordConfirmInput', message: i18n.t("userManagement.validation.message.requireConfirmPassword"), action: 'keyup, blur', rule: 'required' },
		            { input: '#updateUserPasswordConfirmInput', message: i18n.t("userManagement.validation.message.NomatchPassword"), action: 'keyup, focus', rule: function (input, commit) {
		                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
		                    if (input.val() === $('#updateUserPasswordInput').val()) {
		                        return true;
		                    }
		                    return false;
		            	}
		            },
		            { input: '#updateUserBirthdayInput', message: i18n.t("userManagement.validation.message.birthdayRange",{year:((new Date()).getFullYear() + 1)}), action: 'valuechanged', rule: function (input, commit) {
		                var date = $('#updateUserBirthdayInput').jqxDateTimeInput('value');
		                var result = date.getFullYear() >= 1900 && date.getFullYear() <= (new Date()).getFullYear();
		                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
		                return result;
		            	}
		            },
		            { input: '#updateUserEmailInput', message: i18n.t("userManagement.validation.message.requireEmail"), action: 'keyup, blur', rule: 'required' },
		            { input: '#updateUserEmailInput', message: i18n.t("userManagement.validation.message.invalidEmail"), action: 'keyup', rule: 'email' }
		            ], 
		            theme: theme
		    });
		    
		    $('#jqxTabs').jqxTabs({ 
		    	selectionTracker: true, 
		    	width: '800', 
		    	height: 500, 
		 //   	position: 'top', 
		    	theme: theme 
		    });
		},

		formatUpdateUserElements:function (rowData) {
			
		    var d1 = {};
			if (rowData["birthday"] != null) {
				d1 = AppUtil.findDateTime(rowData["birthday"]);
			}else {
				d1= new Date();
			}
		    $('#updateUserBirthdayInput').jqxDateTimeInput({formatString: "yyyy/MM/dd", value: d1 });
		    
		    if(rowData["sex"] != null && rowData["sex"] == i18n.t("userManagement.sex.female")) {
		    	$("#updateUserSexRadioButton1").jqxRadioButton({checked: false});
		    	$("#updateUserSexRadioButton2").jqxRadioButton({checked: true});
		    }else {
		    	$("#updateUserSexRadioButton1").jqxRadioButton({checked: true});
		    	$("#updateUserSexRadioButton2").jqxRadioButton({checked: false});
		    }
		    
		    
		    $("#updateUserIdInput").val(rowData["id"]);
			$("#updateUserAccountInput").val(rowData["account"]);
			$("#updateUserRealNameInput").val(rowData["name"]);
			$("#updateUserPasswordInput").val(rowData["password"]);
			$("#updateUserPasswordConfirmInput").val(rowData["password"]);
			$("#updateUserAddressInput").val(rowData["address"]);
			
			$("#updateUserDepartmentInput").val(rowData["department"]);
			$("#updateUserRolesInput").val(rowData["roles"]);
			
			$("#updateUserEmailInput").val(rowData["email"]);
			$("#updateUserPhoneInput").val(rowData["phone"]);
			$("#updateUserSexInput").val(rowData["sex"]);
		    
			// Create a jqxComboBox
			$("#updateUserDepartmentCombobox").jqxComboBox('checkIndex', AppUtil.findDepartmentValue($("#updateUserDepartmentInput").val()) - 2);
			
			var roleValues = AppUtil.findRoleValue($("#updateUserRolesInput").val());
			for(var i = 0;i< roleValues.length;i++) {
				$("#updateUserRolesCombobox").jqxComboBox('checkIndex', roleValues[i] - 2);
			}
		},

		addUpdateUserEventListeners:function () {
			var me = this;
			$('#updateUserBasicInfoButton').bind('click', function () {
		        me.updateUser('basic');
		    });
			
			$('#updateUserAdvancedInfoButton').bind('click', function () {
		        me.updateUser('advanced');
		    });
			
			$('#updateUserJobInfoButton').bind('click', function () {
		        me.updateUser('job');
		    });
		    
		    $("#updateUserSexRadioButton1").bind('change', function (event) {
		        var checked = event.args.checked;
		        if (checked) {
		        	 $("#updateUserSexInput").val(i18n.t("userManagement.sex.male"));
		        }
		    });
		    $("#updateUserSexRadioButton2").bind('change', function (event) {
		        var checked = event.args.checked;
		        if (checked) {
		        	
		        	$("#updateUserSexInput").val(i18n.t("userManagement.sex.female"));
		        }
		    });
		    
		    $("#updateUserDepartmentCombobox").bind('checkChange', function (event) {
		            if (event.args) {
		                var item = event.args.item;
		                if (item && item.checked) {
		                	var checkedItems = $("#updateUserDepartmentCombobox").jqxComboBox('getCheckedItems');
		                	if(checkedItems && checkedItems.length > 1) {
		                		$.each(checkedItems, function (index) {
				                    if(checkedItems[index].value != item.value) {
				                    	 $("#updateUserDepartmentCombobox").jqxComboBox('uncheckItem',checkedItems[index]);
				                    }
				                });
		                	}
		                	$("#updateUserDepartmentInput").val(item.value);
		                }
		            }
		        });
		    
		    $("#updateUserRolesCombobox").bind('checkChange', function (event) {
		        if (event.args) {
		            var item = event.args.item;
		            if (item) {
		                var items = $("#updateUserRolesCombobox").jqxComboBox('getCheckedItems');
		                var checkedItems = "";
		                $.each(items, function (index) {
		                    checkedItems += this.value + ", ";                          
		                });
		                if(checkedItems.length > 0) {
		                	checkedItems = checkedItems.substring(0, checkedItems.length - 2);
		                }
		                $("#updateUserRolesInput").val(checkedItems);
		            }
		        }
		    });
		    
		},

		updateUser:function (infoType) {
			var me = this;
			var updateUserUIData = {};
			if(infoType == "basic") {
				updateUserUIData = {
					"user.id" : $("#updateUserIdInput").val(),
					"user.account" : $("#updateUserAccountInput").val(),
					"user.name" : $("#updateUserRealNameInput").val(),
					"user.password" : $("#updateUserPasswordConfirmInput").val(),
					"user.birthday" : $('#updateUserBirthdayInput').jqxDateTimeInput('value'),
					"user.sex" : $("#updateUserSexInput").val()
				};
			}else if(infoType == "advanced") {
				updateUserUIData = {
					"user.id" : $("#updateUserIdInput").val(),
					"user.address" : $("#updateUserAddressInput").val(),
					"user.email" : $("#updateUserEmailInput").val(),
					"user.phone" : $("#updateUserPhoneInput").val()
				};
			}else if(infoType == "job") {
				updateUserUIData = {
					"user.id" : $("#updateUserIdInput").val(),
					"user.department" : $("#updateUserDepartmentInput").val(),
					"user.roles" : $("#updateUserRolesInput").val()
				};
			}else if(infoType == "pictrue") {
				
			}else {
				updateUserUIData = {
					"user.id" : $("#updateUserIdInput").val(),
					"user.account" : $("#updateUserAccountInput").val(),
					"user.name" : $("#updateUserRealNameInput").val(),
					"user.password" : $("#updateUserPasswordConfirmInput").val(),
					"user.address" : $("#updateUserAddressInput").val(),
					"user.birthday" : $('#updateUserBirthdayInput').jqxDateTimeInput('value'),
					"user.department" : $("#updateUserDepartmentInput").val(),
					"user.roles" : $("#updateUserRolesInput").val(),
					"user.email" : $("#updateUserEmailInput").val(),
					"user.phone" : $("#updateUserPhoneInput").val(),
					"user.sex" : $("#updateUserSexInput").val()
				};
			}
			
			var updateUserData = userManagement.parseUIDataToUserData(updateUserUIData);
			
			if(infoType == "basic" || infoType == "advanced") {
				AppUtil.request("updateUser.action", updateUserData, function(result){
					if(result && result.id != null){
				    	$("#eventLog").text(i18n.t("userManagement.result.updateSuccess"));
					}else {
						$("#eventLog").text(i18n.t("userManagement.result.updateFail"));
					}
					
				}, function(){
					alert("Fail to update user info!");
				});
				
			}else if(infoType == "job") {
				AppUtil.request("updateRoleWithUser.action", updateUserData, function(result){
					if(result && result.id != null){
				    	$("#eventLog").text(i18n.t("userManagement.result.updateSuccess"));
					}else {
						$("#eventLog").text(i18n.t("userManagement.result.updateFail"));
					}
					
				}, function(){
					alert("Fail to update user info!");
				});
			}else if(infoType == "pictrue") {
				
			}else {
				
			}
			
		},
		
		showUserDetailInfo:function () {
			var me = this;
			$.post("findLoginUser.action", function(user) {
				
				user = userManagement.parseUserDataToUIData(user);
				
				me.formatUpdateUserElements(user);
			});
		}
		
};

	
	
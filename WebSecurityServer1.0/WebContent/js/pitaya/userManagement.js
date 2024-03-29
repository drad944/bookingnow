var userManagement = {
		highlightRows:[],
		leave : function (){
			$("#userDataGrid").unbind('rowselect');
			$("#updateUserRowButton").unbind('click');
			$("#addUserRowButton").unbind('click');
			$("#deleteUserRowButton").unbind('click');
			
			$("#updateUserPopupWindow").unbind('close');
			$("#updateUserUpdateButton").unbind('click');
			$("#updateUserSexRadioButton1").unbind('change');
			$("#updateUserSexRadioButton2").unbind('change');
			$("#updateUserCancelButton").unbind('click');
			$("#updateUserDepartmentCombobox").unbind('select');
			$("#updateUserRolesCombobox").unbind('checkChange');
			$("#updateUserInfoForm").unbind('validationSuccess');
			
			$("#addUserPopupWindow").unbind('close');
			$("#registerUserRegisterButton").unbind('click');
			$("#registerUserSexRadioButton1").unbind('change');
			$("#registerUserSexRadioButton2").unbind('change');
			$("#registerUserCancelButton").unbind('click');
			$("#registerUserDepartmentCombobox").unbind('select');
			$("#registerUserRolesCombobox").unbind('checkChange');
			$("#registerUserInfoForm").unbind('validationSuccess');
			
			
		    $('#updateUserUpdateButton').jqxButton('destroy');
		    $('#updateUserResetButton').jqxButton('destroy');
		    $('#updateUserCancelButton').jqxButton('destroy');
		    $("#updateUserPhoneInput").jqxMaskedInput('destroy');
	//	    $('.updateUserTextInput').jqxInput('destroy');
		    $('#updateUserAccountInput').jqxInput('destroy');
            $("#updateUserBirthdayInput").jqxDateTimeInput('destroy');
			$("#updateUserSexRadioButton1").jqxRadioButton('destroy');
			$("#updateUserSexRadioButton2").jqxRadioButton('destroy');
		  	$("#updateUserDepartmentCombobox").jqxComboBox('destroy');
			$("#updateUserRolesCombobox").jqxComboBox('destroy');
				
			$("#updateUserDiv").jqxExpander('destroy');
			$("#updateUserPopupWindow").jqxWindow('destroy');
			
			

		    $('#registerUserRegisterButton').jqxButton('destroy');
		    $('#registerUserResetButton').jqxButton('destroy');
		    $('#registerUserCancelButton').jqxButton('destroy');
		    $('#acceptInput').jqxCheckBox('destroy');
		    $("#registerUserPhoneInput").jqxMaskedInput('destroy');
	//	    $('.registerUserTextInput').jqxInput('destroy');
            $("#registerUserBirthdayInput").jqxDateTimeInput('destroy');
		    $("#registerUserSexRadioButton1").jqxRadioButton('destroy');
		    $("#registerUserSexRadioButton2").jqxRadioButton('destroy');
			$("#registerUserDepartmentCombobox").jqxComboBox('destroy');
			$("#registerUserRolesCombobox").jqxComboBox('destroy');
			$("#registerUserDiv").jqxExpander('destroy');
		    $("#addUserPopupWindow").jqxWindow('destroy');
		    
		    $("#addUserRowButton").jqxButton('destroy');
			$("#deleteUserRowButton").jqxButton('destroy');
			$("#updateUserRowButton").jqxButton('destroy');
			
			$('#userDataGrid').jqxGrid('removesort');
			$("#userDataGrid").jqxGrid('clearfilters');
			$("#userDataGrid").jqxGrid('clear');
			$('#userDataGrid').jqxGrid({ filterable: false});
			$('#userDataGrid').jqxGrid('disabled');
		},

		visit : function (){
			this.init();
		},
		init : function(){
			var me = this;
			me.parseUserGridHtml();
		},
		checkUserAccountExist : function(){
			if($("#registerUserAccountInput").val() == null || $("#registerUserAccountInput").val() == "") {
				return true;
			}
			var existUserAccountValidation = false;
			$.ajaxSetup({ async: false }); 
			$.post("existUser.action", {"user.account":$("#registerUserAccountInput").val()}, function(result){
				$.ajaxSetup({ async: true });
				if(result && result.executeResult == true){
					existUserAccountValidation = false;
				}else if(result && result.executeResult == false && result.errorType == Constants.SUCCESS){
					existUserAccountValidation =  true;
				}else {
					existUserAccountValidation =  true;
				}
			});
			return existUserAccountValidation;
		},
		emptyRegisterUserWindow:function (){
			
			//init registerUserWindow widget data
			$("#registerUserPhoneInput").jqxMaskedInput({value: null });
			$('#registerUserBirthdayInput').jqxDateTimeInput({value:AppUtil.findDateTime("2000-01-01 00:00:00")});
			
			$("#registerUserSexRadioButton1").jqxRadioButton({checked: true});
		    $("#registerUserSexRadioButton2").jqxRadioButton({checked: false});
		    $('#acceptInput').jqxCheckBox({checked: false});
		    $("#registerUserRolesCombobox").jqxComboBox('uncheckAll');
		    
		    //init registerUserWindow store data
			$("registerUserSexInput").val(i18n.t("userManagement.sex.male"));
			$("#registerUserIdInput").val(null);
			$("#registerUserAccountInput").val(null);
			$("#registerUserRealNameInput").val(null);
			$("#registerUserPasswordInput").val(null);
			$("#registerUserPasswordConfirmInput").val(null);
			$("#registerUserAddressInput").val(null);
			
			$("#registerUserDepartmentInput").val(null);
			$("#registerUserRolesInput").val(null);
			
			$("#registerUserEmailInput").val(null);
			$("#registerUserResult").text("");
		},

		emptyUpdateUserWindow:function (){
			
			//init registerUserWindow widget data
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
			
		    $("#updateUserDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
		    $('#updateUserUpdateButton').jqxButton({ width: 60, height: 25, theme: theme });
		    $('#updateUserResetButton').jqxButton({ width: 60, height: 25, theme: theme });
		    $('#updateUserCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
		    

		    
		  //  $("#updateUserAddressInput").jqxMaskedInput({ mask: '省-市-区-街道-门牌号', width: 150, height: 22, theme: theme });
		    $("#updateUserPhoneInput").jqxMaskedInput({ mask: '### #### ####', width: 150, height: 27, theme: theme });
		    $('.updateUserTextInput').jqxInput({width: 150, height: 25, theme: theme });
		    $('#updateUserAccountInput').jqxInput({disabled: true });
		    
		    var d1 = new Date();
		    $.getScript('js/jqwidgets/globalization/globalize.culture.zh-CN.js', function () {
                $("#updateUserBirthdayInput").jqxDateTimeInput({ enableAbsoluteSelection: true,culture: 'zh-CN',theme: theme,width: 150, height: 22,formatString: "yyyy/MM/dd", value: d1 });
            });
		    
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
//			$("#updateUserRolesCombobox").jqxComboBox('checkIndex', 0);
		    // initialize validator.
		    $('#updateUserInfoForm').jqxValidator({
		     rules: [
		            { input: '#updateUserAccountInput', message: i18n.t("userManagement.validation.message.requireAccount"), action: 'keyup, blur', rule: 'required' },
		            { input: '#updateUserAccountInput', message: i18n.t("userManagement.validation.message.accountLength"), action: 'keyup, blur', rule: 'length=3,12' },
		            { input: '#updateUserRealNameInput', message: i18n.t("userManagement.validation.message.requireUsername"), action: 'keyup, blur', rule: 'required' },
		            { input: '#updateUserRealNameInput', message: i18n.t("userManagement.validation.message.usernameLength"), action: 'keyup', rule: 'length=1,12' },
		            
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
		    $("#updateUserPopupWindow").jqxWindow({
		    	autoOpen : false,
		    	isModal: true,
		    	width: 300, 
		    	height: 500, 
		    	resizable: false, 
		    	theme: theme, 
		    	cancelButton: $("#updateUserCancelButton"), 
		    	modalOpacity: 0.5,
		    	initContent: function () {
		          //  $('#updateUserPopupWindow').jqxWindow('focus');
		        }
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
			
			$('#updateUserPopupWindow').bind('close', function (event) { 
				me.emptyUpdateUserWindow();
				$('#updateUserInfoForm').jqxValidator('hide');
		      //  $('#updateUserPopupWindow').jqxWindow('close');
			});
			$('#updateUserUpdateButton').bind('click', function () {
		        $('#updateUserInfoForm').jqxValidator('validate');
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
		    
		    $("#updateUserCancelButton").bind('click', function (event) {
		    	$('#updateUserInfoForm').jqxValidator('hide');
		        $('#updateUserPopupWindow').jqxWindow('close');
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
		                if (item && item.checked) {
		                	var checkedItems = $("#updateUserRolesCombobox").jqxComboBox('getCheckedItems');
		                	if(checkedItems && checkedItems.length > 1) {
		                		$.each(checkedItems, function (index) {
				                    if(checkedItems[index].value != item.value) {
				                    	 $("#updateUserRolesCombobox").jqxComboBox('uncheckItem',checkedItems[index]);
				                    }
				                });
		                	}
		                	$("#updateUserRolesInput").val(item.value);
		                }
		            }
		    	/*
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
		        */
		    });
		    
		    $('#updateUserInfoForm').bind('validationSuccess', function (event) { 
		    	// Some code here. 
		    	me.updateUser();
		    });
		},

		initUpdateUserWindow:function (rowData,position) {
			var me = this;
			$("#updateUserPopupWindow").removeAttr("style");
			
			me.formatUpdateUserElements(rowData);
			
			// initialize the popup window and buttons.
			$("#updateUserPopupWindow").jqxWindow({position:position});
			
		    $("#updateUserPopupWindow").jqxWindow('open');
		},

		updateUser:function () {
			var me = this;

			var updateUserUIData = {
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
			
			var updateUserData = me.parseUIDataToUserData(updateUserUIData);

			$.post("updateRoleWithUser.action", updateUserData, function(result) {
		    	
				if (result != null && result["id"] != null) {
					
					$("#updateUserResult").text(i18n.t("userManagement.result.updateSuccess"));
					$("#updateUserPopupWindow").jqxWindow('close');
					
					var userUIResult = me.parseUserDataToUIData(result);
					
					//update row in grid
					var selectedrowindex = $("#userDataGrid").jqxGrid('getselectedrowindex');
		            var rowscount = $("#userDataGrid").jqxGrid('getdatainformation').rowscount;
		            if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
		            	var id = $("#userDataGrid").jqxGrid('getrowid', selectedrowindex);
		                var commit = $("#userDataGrid").jqxGrid('updaterow', id, userUIResult);
		                $("#userDataGrid").jqxGrid('ensurerowvisible', selectedrowindex);
		            }
					
					$("#eventLog").text(i18n.t("userManagement.result.updateSuccess"));

				} else if (result != null && result["executeResult"] != null
						&& result["executeResult"] == false) {
					$("#updateUserResult").text(i18n.t("userManagement.result.updateFail"));
				}
			});
		},

		initRegisterUserElements:function () {
			var me = this;
			
		    $("#registerUserDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
		    $('#registerUserRegisterButton').jqxButton({ width: 60, height: 25, theme: theme });
		    $('#registerUserResetButton').jqxButton({ width: 60, height: 25, theme: theme });
		    $('#registerUserCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
		    
		    $('#acceptInput').jqxCheckBox({ width: 130, theme: theme ,checked: false});
		    
		    $("#registerUserPhoneInput").jqxMaskedInput({ mask: '### #### ####', width: 150, height: 25, theme: theme });
		    $('.registerUserTextInput').jqxInput({width: 150, height: 25, theme: theme });
		    var date = AppUtil.findDateTime("2000-01-01 00:00:00");
		    $.getScript('js/jqwidgets/globalization/globalize.culture.zh-CN.js', function () {
                $("#registerUserBirthdayInput").jqxDateTimeInput({ culture: 'zh-CN',theme: theme,width: 150, height: 25,formatString: "yyyy/MM/dd", value: $.jqx._jqxDateTimeInput.getDateTime(date) });
            });
		    
		    $("#registerUserSexRadioButton1").jqxRadioButton({ width: 70, height: 25, checked: true, theme: theme });
		    $("#registerUserSexRadioButton2").jqxRadioButton({ width: 70, height: 25, theme: theme });
		    
		    var userDepartmentData = [
					{ value: 2, label: i18n.t("userManagement.department.BUSSINESS") },
					{ value: 3, label: i18n.t("userManagement.department.PRODUCTION") },
					{ value: 4, label: i18n.t("userManagement.department.FINANCE") },
					{ value: 5, label: i18n.t("userManagement.department.PERSONNEL") },
					{ value: 6, label: i18n.t("userManagement.department.DEVERLOPE") },
					{ value: 7, label: i18n.t("userManagement.department.MANAGEMENT") }
		                            ];
		                        
			// Create a jqxComboBox
			$("#registerUserDepartmentCombobox").jqxComboBox({ 
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
			$("#registerUserRolesCombobox").jqxComboBox({ 
				checkboxes: true, 
				source: userRoleData, 
				displayMember: "label", 
				valueMember: "value", 
				width: 150, 
				height: 25, 
				theme: theme 
			});
			
			$('#registerUserAccountInput').on('validationSuccess', function (event) { userAccountExist = -1;});
			
		    // initialize validator.
		    $('#registerUserInfoForm').jqxValidator({
		     rules: [
		            { input: '#registerUserAccountInput', message: i18n.t("userManagement.validation.message.requireAccount"), action: 'keyup, blur', rule: 'required' },
		            { input: '#registerUserAccountInput', message: i18n.t("userManagement.validation.message.accountLength"), action: 'keyup, blur', rule: 'length=3,12' },
		            { input: '#registerUserAccountInput', message: i18n.t("userManagement.validation.message.existAccount"), action: 'blur', rule: function(input, commit){
		            		return me.checkUserAccountExist();
		            	}
		            },
		            { input: '#registerUserRealNameInput', message: i18n.t("userManagement.validation.message.requireUsername"), action: 'keyup, blur', rule: 'required' },
		            { input: '#registerUserRealNameInput', message: i18n.t("userManagement.validation.message.usernameLength"), action: 'keyup', rule: 'length=1,12' },
		            
		            { input: '#registerUserPasswordInput', message: i18n.t("userManagement.validation.message.requirePassword"), action: 'keyup, blur', rule: 'required' },
		            { input: '#registerUserPasswordInput', message: i18n.t("userManagement.validation.message.passwordLength"), action: 'keyup, blur', rule: 'length=6,20' },
		            { input: '#registerUserPasswordConfirmInput', message: i18n.t("userManagement.validation.message.requireConfirmPassword"), action: 'keyup, blur', rule: 'required' },
		            { input: '#registerUserPasswordConfirmInput', message: i18n.t("userManagement.validation.message.NomatchPassword"), action: 'keyup, focus', rule: function (input, commit) {
		                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
		                    if (input.val() === $('#registerUserPasswordInput').val()) {
		                        return true;
		                    }
		                    return false;
		            	}
		            },
		            { input: '#registerUserBirthdayInput', message: i18n.t("userManagement.validation.message.birthdayRange",{year:((new Date()).getFullYear() + 1)}), action: 'valuechanged', rule: function (input, commit) {
		                var date = $('#registerUserBirthdayInput').jqxDateTimeInput('value');
		                var result = date.getFullYear() >= 1900 && date.getFullYear() <= (new Date()).getFullYear();
		                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
		                return result;
		            	}
		            },
		            { input: '#registerUserEmailInput', message: i18n.t("userManagement.validation.message.requireEmail"), action: 'keyup, blur', rule: 'required' },
		            { input: '#registerUserEmailInput', message: i18n.t("userManagement.validation.message.invalidEmail"), action: 'keyup', rule: 'email' },
		            { input: '#acceptInput', message: i18n.t("userManagement.validation.message.acceptService"), action: 'change', rule: 'required', position: 'right:0,0'}
		            ], 
		            theme: theme
		    });
		    
		    $("#addUserPopupWindow").jqxWindow({
		    	autoOpen : false,
		    	isModal: true,
		    	width: 300, 
		    	height: 500, 
		    	resizable: false, 
		    	theme: theme, 
		    	cancelButton: $("#registerUserCancelButton"), 
		    	modalOpacity: 0.5,
		    	
		    	initContent: function () {
		         //   $('#addUserPopupWindow').jqxWindow('focus');
		        }
		        
		    });
		},

		addRegisterUserEventListeners:function () {
			var me = this;
			$('#addUserPopupWindow').bind('close', function (event) { 
				me.emptyRegisterUserWindow();
				$('#registerUserInfoForm').jqxValidator('hide');
		      //  $('#updateUserPopupWindow').jqxWindow('close');
			});
			
			$('#registerUserRegisterButton').bind('click', function () {
		        $('#registerUserInfoForm').jqxValidator('validate');
		    });
			
		    $("#registerUserSexRadioButton1").bind('change', function (event) {
		        var checked = event.args.checked;
		        if (checked) {
		        	 $("#registerUserSexInput").val(i18n.t("userManagement.sex.male"));
		        }
		    });
		    $("#registerUserSexRadioButton2").bind('change', function (event) {
		        var checked = event.args.checked;
		        if (checked) {
		        	$("#registerUserSexInput").val(i18n.t("userManagement.sex.female"));
		        }
		    });
		    
		    $("#registerUserCancelButton").bind('click', function (event) {
		        $('#addUserPopupWindow').jqxWindow('close');
		        
		    });
		        
		    $("#registerUserDepartmentCombobox").bind('checkChange', function (event) {
		            if (event.args) {
		                var item = event.args.item;
		                if (event.args) {
			                var item = event.args.item;
			                if (item && item.checked) {
			                	var checkedItems = $("#registerUserDepartmentCombobox").jqxComboBox('getCheckedItems');
			                	if(checkedItems && checkedItems.length > 1) {
			                		$.each(checkedItems, function (index) {
					                    if(checkedItems[index].value != item.value) {
					                    	 $("#registerUserDepartmentCombobox").jqxComboBox('uncheckItem',checkedItems[index]);
					                    }
					                });
			                	}
			                	$("#registerUserDepartmentInput").val(item.value);
			                }
			            }
		            }
		        });
		    
		    $("#registerUserRolesCombobox").bind('checkChange', function (event) {
		    	if (event.args) {
		                var item = event.args.item;
		                if (event.args) {
			                var item = event.args.item;
			                if (item && item.checked) {
			                	var checkedItems = $("#registerUserRolesCombobox").jqxComboBox('getCheckedItems');
			                	if(checkedItems && checkedItems.length > 1) {
			                		$.each(checkedItems, function (index) {
					                    if(checkedItems[index].value != item.value) {
					                    	 $("#registerUserRolesCombobox").jqxComboBox('uncheckItem',checkedItems[index]);
					                    }
					                });
			                	}
			                	$("#registerUserRolesInput").val(item.value);
			                }
			            }
		            }
		    	
		    });
		    
		    $('#registerUserInfoForm').bind('validationSuccess', function (event) { 
		    	// Some code here. 
		    	me.registerUser();
		    });
		},
		
		initRegisterUserWindow:function (position) {
			var me = this;
			me.emptyRegisterUserWindow();
			$("#addUserPopupWindow").removeAttr("style");
			$("#addUserPopupWindow").attr("style","overflow:hidden");
			
			$("#addUserPopupWindow").jqxWindow({position:position});
		    $("#addUserPopupWindow").jqxWindow('open');
		},

		registerUser:function () {
			var me = this;

			var registerUserUIData = {
				"user.account" : $("#registerUserAccountInput").val(),
				"user.name" : $("#registerUserRealNameInput").val(),
				"user.password" : $("#registerUserPasswordConfirmInput").val(),
				"user.address" : $("#registerUserAddressInput").val(),
				"user.birthday" : $("#registerUserBirthdayInput").jqxDateTimeInput('value'),
				"user.department" : $("#registerUserDepartmentInput").val(),
				"user.roles" : $("#registerUserRolesInput").val(),
				"user.email" : $("#registerUserEmailInput").val(),
				"user.phone" : $("#registerUserPhoneInput").val(),
				"user.sex" : $("#registerUserSexInput").val()
			};

			var registerUserData = me.parseUIDataToUserData(registerUserUIData);
			
			$.post("registerRoleWithUser.action", registerUserData, function(result) {
		    	
				if (result != null && result["id"] != null) {
					result = me.parseUserDataToUIData(result);
					
					$("#registerUserResult").text(i18n.t("userManagement.result.insertSuccess"));
					$('#addUserPopupWindow').jqxWindow('close');
					
					var commit = $("#userDataGrid").jqxGrid('addrow', null, result);
					
					$("#eventLog").text(i18n.t("userManagement.result.insertSuccess"));
				} else if (result != null && result["executeResult"] != null
						&& result["executeResult"] == false) {
					$("#registerUserResult").text(i18n.t("userManagement.result.insertFail"));
				}
			});
		},

		

		uploadUserImage:function () {
			var me = this;
			$.post("findLoginUser.action", function(user) {
				if (user != null && user.id != null) {
					
					var imgOriginalSize = {};
		        			
		        	var start_time = new Date().getTime();
		        	var img_url = user.image_relative_path + '?'+start_time;
		        	var img = new Image();
		        	img.src = img_url;
		        	var check = function(){
		        	    // 只要任何一方大于0
		        	    // 表示已经服务器已经返回宽高
		        	    if(img.width>0 || img.height>0){
		        	        var diff = new Date().getTime() - start_time;
		        	        imgOriginalSize['width'] = img.width;
		        			imgOriginalSize['height'] = img.height;
		        			imgOriginalSize['time'] = diff;
		        	        clearInterval(set);
		        	        
		        	        var panelWidth = 200;
		            		var panelHeight = 200;
		            		var rectRatio = 0;
		            		var scaleRatio = 0;
		            		
			            	if(imgOriginalSize != null && imgOriginalSize["width"] != null) {
			            		rectRatio = (imgOriginalSize["width"] * 10000) / (imgOriginalSize["height"] * 10000);
			            		if(rectRatio > 1) {
			            			scaleRatio = (imgOriginalSize["width"] * 10000) / (200 * 10000);
			            			panelWidth = 200;
			            			panelHeight = Math.round((panelWidth * 10000) / (rectRatio * 10000));
			            		}else {
			            			scaleRatio = (imgOriginalSize["height"] * 10000) / (200 * 10000);
			            			panelHeight = 200;
			            			panelWidth = Math.round(panelHeight * rectRatio);
			            		}
			            	}
			            	$("#scaleRatio").val(scaleRatio);
			            	$("#crop_now").attr("width",panelWidth);
			            	$("#crop_now").attr("height",panelHeight);
			            	
			            	$("#crop_now").attr("src",user.image_relative_path);
			            	$("#crop_preview").attr("src",user.image_relative_path);
			            	$("#image_relative_path").val(user.image_relative_path);
			            	$("#user_id").val(user.id);
		        	    }
		        	};
		        	 
		        	var set = setInterval(check,40);
					

		        	me.initUploadUserImage();
					me.initImageAreaSelect();
					//initJCrop();
				}

			});
		},

		initUploadUserImage:function (){
			
			$(function(){
				$("#file_upload").uploadify({
			        //开启调试
			        'debug' : false,
			        //是否自动上传
			        'auto':true,
			        //超时时间
			        'successTimeout':99999,
			        //附带值
			        'formData':{
			            'userid':'用户id',
			            'username':'用户名',
			            'rnd':'加密密文'
			        },
			        //flash
			        'swf': "js/uploadify.swf",
			        //不执行默认的onSelect事件
			        'overrideEvents' : ['onDialogClose'],
			        //文件选择后的容器ID
			        'queueID':'uploadfileQueue',
			        'uploadLimit':100,
			        //服务器端脚本使用的文件对象的名称 $_FILES个['upload']
			        'fileObjName':'uploadFile',
			        //上传处理程序
			        'uploader':'uploadFile.action',
			        //浏览按钮的背景图片路径
			    //    'buttonImage':'../../css/upbutton.png',
			        //浏览按钮的宽度
			        'buttonText': '选择图片',
					'buttonClass': 'browser_file',
					'removeCompleted': true,
					'removeTimeout':1,
			        'width':'100',
			        //浏览按钮的高度
			        'height':'25',
			        //expressInstall.swf文件的路径。
			        'expressInstall':'js/expressInstall.swf',
			        //在浏览窗口底部的文件类型下拉菜单中显示的文本
			        'fileTypeDesc':'支持的格式：',
			        //允许上传的文件后缀
			        'fileTypeExts':'*.jpg;*.jpge;*.gif;*.png',
			        //上传文件的大小限制
			        'fileSizeLimit':'3MB',
			        //上传数量
			        'queueSizeLimit' : 1,
			        'multi':false,
			        //每次更新上载的文件的进展
			        'onUploadProgress' : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
			             //有时候上传进度什么想自己个性化控制，可以利用这个方法
			             //使用方法见官方说明
			        },
			        'onUploadError' : function(file, errorCode, errorMsg, errorString) {
			            alert('The file ' + file.name + ' could not be uploaded: ' + errorString);
			        },
			        //选择上传文件后调用
			        'onSelect' : function(file) {
			              //  alert(file);   
			        },
			        'onCancel': function(file) {
			        	//clear all upload files in queue
			        	$('#file_upload').uploadify('cancel','*');
			        },
			        //返回一个错误，选择文件的时候触发
			        'onSelectError':function(file, errorCode, errorMsg){
			            switch(errorCode) {
			                case -100:
			                    alert("上传的文件数量已经超出系统限制的"+$('#file_upload').uploadify('settings','queueSizeLimit')+"个文件！");
			                    break;
			                case -110:
			                    alert("文件 ["+file.name+"] 大小超出系统限制的"+$('#file_upload').uploadify('settings','fileSizeLimit')+"大小！");
			                    break;
			                case -120:
			                    alert("文件 ["+file.name+"] 大小异常！");
			                    break;
			                case -130:
			                    alert("文件 ["+file.name+"] 类型不正确！");
			                    break;
			            }
			        },
			        //检测FLASH失败调用
			        'onFallback':function(){
			            alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
			        },
			        //上传到服务器，服务器返回相应信息到data里
			        'onUploadSuccess':function(file, data, response){
			        	//data is string here,need to parse to json object.
			        	 var jsonData = eval('(' + data + ')');
			        	
			            if(jsonData != null && jsonData.fileRelativePath != null && response == true) {
			            	var user = jsonData;
			            	
			            	
			            	
			            	var imgOriginalSize = {};
		        			
			            	var start_time = new Date().getTime();
			            	var img_url = user.fileRelativePath + '?'+start_time;
			            	var img = new Image();
			            	img.src = img_url;
			            	var check = function(){
			            	    // 只要任何一方大于0
			            	    // 表示已经服务器已经返回宽高
			            	    if(img.width>0 || img.height>0){
			            	        var diff = new Date().getTime() - start_time;
			            	        imgOriginalSize['width'] = img.width;
			            			imgOriginalSize['height'] = img.height;
			            			imgOriginalSize['time'] = diff;
			            	        clearInterval(set);
			            	        
			            	        var panelWidth = 200;
			                		var panelHeight = 200;
			                		var rectRatio = 0;
			                		var scaleRatio = 0;
			                		
			    	            	if(imgOriginalSize != null && imgOriginalSize["width"] != null) {
			    	            		rectRatio = (imgOriginalSize["width"] * 10000) / (imgOriginalSize["height"] * 10000);
			    	            		if(rectRatio > 1) {
			    	            			scaleRatio = (imgOriginalSize["width"] * 10000) / (200 * 10000);
			    	            			panelWidth = 200;
			    	            			panelHeight = Math.round((panelWidth * 10000) / (rectRatio * 10000));
			    	            		}else {
			    	            			scaleRatio = (imgOriginalSize["height"] * 10000) / (200 * 10000);
			    	            			panelHeight = 200;
			    	            			panelWidth = Math.round(panelHeight * rectRatio);
			    	            		}
			    	            	}
			    	            	$("#scaleRatio").val(scaleRatio);
			    	            	$("#crop_now").attr("width",panelWidth);
			    	            	$("#crop_now").attr("height",panelHeight);
			    	            	
			    	            	$("#crop_now").attr("src",user.fileRelativePath);
			    	            	$("#crop_preview").attr("src",user.fileRelativePath);
			    	            	$("#image_relative_path").val(user.fileRelativePath);
			            	    }
			            	};
			            	 
			            	var set = setInterval(check,40);
			            
			            }else {
			            	//failed to upload image.
			            	
			            }
			        }
			    });
			});
		},

		initImageAreaSelect:function () {
			$('#crop_now').imgAreaSelect({
				aspectRatio: '1:1',
				handles: true,
				fadeSpeed: 200, 
				onSelectChange: preview
		    });
			
			$("#crop_submit").click(function(){
				if(parseInt($("#x").val())){
					var ias = $('#crop_now').imgAreaSelect({ instance: true });
					ias.cancelSelection();
					var params = {};
					params["params.x"] = $("#x").val();
					params["params.y"] = $("#y").val();
					params["params.w"] = $("#w").val();
					params["params.h"] = $("#h").val();
					params["params.user_id"] = $("#user_id").val();
					params["params.scaleRatio"] = $("#scaleRatio").val();
					params["params.imagePath"] = $("#image_relative_path").val();
					$.post("cropPictureForUser.action",params, function(user) {
						if (user != null && user.id != null) {
							$("#crop_now").attr("src", user.image_relative_path);
							$("#crop_preview").attr("src", user.image_relative_path);
				
							$("#user_id").val(user.id);
							$("#image_relative_path").val(user.image_relative_path);
							
							
							
			            	var imgOriginalSize = {};
		        			
			            	var start_time = new Date().getTime();
			            	var img_url = user.image_relative_path + '?'+start_time;
			            	var img = new Image();
			            	img.src = img_url;
			            	var check = function(){
			            	    // 只要任何一方大于0
			            	    // 表示已经服务器已经返回宽高
			            	    if(img.width>0 || img.height>0){
			            	        var diff = new Date().getTime() - start_time;
			            	        imgOriginalSize['width'] = img.width;
			            			imgOriginalSize['height'] = img.height;
			            			imgOriginalSize['time'] = diff;
			            	        clearInterval(set);
			            	        
			            	        var panelWidth = 200;
			                		var panelHeight = 200;
			                		var rectRatio = 0;
			                		var scaleRatio = 0;
			                		
			    	            	if(imgOriginalSize != null && imgOriginalSize["width"] != null) {
			    	            		rectRatio = (imgOriginalSize["width"] * 10000) / (imgOriginalSize["height"] * 10000);
			    	            		if(rectRatio > 1) {
			    	            			scaleRatio = (imgOriginalSize["width"] * 10000) / (200 * 10000);
			    	            			panelWidth = 200;
			    	            			panelHeight = Math.round((panelWidth * 10000) / (rectRatio * 10000));
			    	            		}else {
			    	            			scaleRatio = (imgOriginalSize["height"] * 10000) / (200 * 10000);
			    	            			panelHeight = 200;
			    	            			panelWidth = Math.round(panelHeight * rectRatio);
			    	            		}
			    	            	}
			    	            	$("#scaleRatio").val(scaleRatio);
			    	            	$("#crop_now").attr("width",panelWidth);
			    	            	$("#crop_now").attr("height",panelHeight);
			    	            	
			            	    }
			            	};
			            	 
			            	var set = setInterval(check,40);
			            
						}else {
						alert("failed to cut picture!");
						}
					});
				}else{
					alert("要先在图片上划一个选区再单击确认剪裁的按钮哦！");	
				}
			});
		},

		preview:function (img, selection) {
		    if (!selection.width || !selection.height)
		        return;
		    
		    var scaleX = 100 / selection.width;
		    var scaleY = 100 / selection.height;

		    /*
		    $('#preview img').css({
		        width: Math.round(scaleX * 300),
		        height: Math.round(scaleY * 300),
		        marginLeft: -Math.round(scaleX * selection.x1),
		        marginTop: -Math.round(scaleY * selection.y1)
		    });
			*/
		    
		    $('#x').val(selection.x1);
		    $('#y').val(selection.y1);
		    $('#w').val(selection.width);
		    $('#h').val(selection.height);  
		    
		},

		showPreview:function (coords){
			$("#x").val(coords.x);
			$("#y").val(coords.y);
			$("#w").val(coords.w);
			$("#h").val(coords.h);
		    if(parseInt(coords.w) > 0){
		        //计算预览区域图片缩放的比例，通过计算显示区域的宽度(与高度)与剪裁的宽度(与高度)之比得到
		        var rx = $("#preview_box").width() / coords.w; 
		        var ry = $("#preview_box").height() / coords.h;
		        //通过比例值控制图片的样式与显示
		        $("#crop_preview").css({
		            width:Math.round(rx * $("#crop_now").width()) + "px",	//预览图片宽度为计算比例值与原图片宽度的乘积
		            height:Math.round(rx * $("#crop_now").height()) + "px",  //预览图片高度为计算比例值与原图片高度的乘积
		            marginLeft:"-" + Math.round(rx * coords.x) + "px",
		            marginTop:"-" + Math.round(ry * coords.y) + "px"
		        });
		    }
		},

		initOperateUserGridElements:function () {
			
			$("#addUserRowButton").jqxButton({ height: 25,theme: theme });
			$("#deleteUserRowButton").jqxButton({ height: 25,theme: theme });
			$("#updateUserRowButton").jqxButton({ height: 25,theme: theme });
		},	

		addOperateUserGridEventListeners:function () {
			var me = this;
			// display selected row index.
		    $("#userDataGrid").bind('rowselect', function (event) {
		        $("#eventLog").text(i18n.t("userManagement.grid.selectRow",{index:event.args.rowindex}));
		    	for (var i = 0; i < userManagement.highlightRows.length; i++) {
                    if (userManagement.highlightRows[i].data["id"] == event.args.row["id"]) {
                        userManagement.highlightRows.remove(i);
                        $('#userDataGrid').jqxGrid('render');
                        break;
                    }
                }
		        
		    });
		    
			// update row.
			$("#updateUserRowButton").bind('click', function () {
				selectedupdaterowindex = $("#userDataGrid").jqxGrid('getselectedrowindex');
				if(selectedupdaterowindex != -1) {
					 rowData = $('#userDataGrid').jqxGrid('getrowdata', selectedupdaterowindex);
					    
						
						if(rowData != null) {
							var offset = $("#userDataGrid").offset();
							var position = {};
							position.x = parseInt(offset.left) + 200;
							position.y = parseInt(offset.top) - 120;
							
							me.initUpdateUserWindow(rowData,position);
						}
				}else {
					
					$("#eventLog").text(i18n.t("userManagement.validation.message.requireSelectOneRow"));
				}
			});
		    
			// show the popup window
			$("#addUserRowButton").bind('click', function () {
			//	openContentPage('addUserPopupWindowDiv','page/common/addUserPopupWindow.html','content');
				
				var offset = $("#userDataGrid").offset();
				var position = {};
				position.x = parseInt(offset.left) + 200;
				position.y = parseInt(offset.top) - 120;
				
				
				// show the popup window.
				me.initRegisterUserWindow(position);
				
					
			});
			
			// delete row.
			$("#deleteUserRowButton").bind('click', function () {
			    var selectedrowindex = $("#userDataGrid").jqxGrid('getselectedrowindex');
			    var rowscount = $("#userDataGrid").jqxGrid('getdatainformation').rowscount;
			    if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
			        //var id = $("#userDataGrid").jqxGrid('getrowid', selectedrowindex);
			        
			        var rowData = $('#userDataGrid').jqxGrid('getrowdata', selectedrowindex);
			        var r=confirm("您确定要删除账号: " + rowData["account"] + " 吗?");
					if (r==true)
					{
						$.post("removeUser.action", {"user.id": rowData["id"]},function(result){
							if(result != null && result["executeResult"] != null && result["executeResult"] == true){
								var id = $("#userDataGrid").jqxGrid('getrowid', selectedrowindex);
			                    var commit = $("#userDataGrid").jqxGrid('deleterow', id);
				            	
				            	if(commit != null) {
				            		
				            	}
				            }
						});
					}
					else
					{
					  
					}
			        
			        
			    }
			});
		},

		parseUserGridHtml:function () {
			var me = this;
			me.highlightRows = [];
				$.post("findRoleWithUser.action", 
					{"user.enabled": true}, 
					function(matchedusers){
						
			var option = {
					lng: 'zh',
					fallbackLng: 'zh',
			//		fallbackLng: 'en-US',
			//		lng: 'en-US',
					resGetPath: 'resources/locales/__lng__/__ns__.json',
			//		resPostPath:'resources/locales/__lng__/__ns__.json',
					getAsync: false,
					ns: 'bookingnow.view',
					preload:'zh',
					load:'current'
			//		load:'unspecific'
				};
			 
			i18n.init(option);
			
			var cellclass = function (row, datafield, value, rowdata) {
                for (var i = 0; i < me.highlightRows.length; i++) {
                    if (me.highlightRows[i].data["id"] == rowdata["id"]) {
                        return "highlightRow";
                    }
                }
            }
			        	
			if(matchedusers != null && matchedusers.result != null){
				users = matchedusers.result;
			}
			
			var imageRenderer = function (row, datafield, value) {
				return '<img style="margin-left: 5px;" height="60" width="50" src="' + value + '"/>';
			};
			var userSize = users.length;
			var userData = {};
			
			var columns = [
							
							{ cellclassname: cellclass, text: i18n.t("userManagement.field.account"), datafield: 'account',filtertype:'textbox', width: 100 },
							{ cellclassname: cellclass, text: i18n.t("userManagement.field.name"), datafield: 'name',filtertype:'textbox', width: 100 },
							{ cellclassname: cellclass, text: i18n.t("userManagement.field.phone"), datafield: 'phone',filtertype:'textbox', width: 100 },
							{ cellclassname: cellclass, text: i18n.t("userManagement.field.sex"), datafield: 'sex',filtertype:'checkedlist', width: 50 },
							{ cellclassname: cellclass, text: i18n.t("userManagement.field.email"), datafield: 'email',filtertype:'textbox', width: 100 },
							{ cellclassname: cellclass, text: i18n.t("userManagement.field.address"), datafield: 'address',filtertype:'textbox', width: 100 },
							{ cellclassname: cellclass, text: i18n.t("userManagement.field.birthday"), datafield: 'birthday',filtertype:'textbox', width: 100 },
							{ cellsrenderer:imageRenderer,cellclassname: cellclass, text: i18n.t("userManagement.field.image_relative_path"), datafield: 'image_relative_path',filtertype:'textbox', width: 100 },
							{ cellclassname: cellclass, text: i18n.t("userManagement.field.department"), datafield: 'department',filtertype:'checkedlist', width: 80 },
							{ cellclassname: cellclass, text: i18n.t("userManagement.field.roles"), datafield: 'roles',filtertype:'textbox', width: 150 }
								    ];
			
			var datafields = [
							{name: 'id',type:"number"},
							{name: 'password',type:"string"},
			                  {name: 'image_relative_path',type:"string"},
			                  {name: 'account',type:"string"},
			                  {name: 'name',type:"string"},
			                  {name: 'phone',type:"string"},
			                  {name: 'sex',type:"string"},
			                  {name: 'email',type:"string"},
			                  {name: 'address',type:"string"},
			                  {name: 'birthday',type:"string"},
			                  {name: 'department',type:"string"},
			                  {name: 'roles',type:"string"}
			                  ];
			
			for(var i = 0;i < userSize; i++) {
				user = me.parseUserDataToUIData(users[i]);
				
				var rowData = {};
				for(var item in user) {
					if(item == "role_Details" || item == "image" || item == "image_absolute_path" || item == "enabled"){
							
					}else if (item == "modifyTime" || item == "image_size" || item == "sub_system" || item == "description") {
						
					}else {
						rowData[item] = user[item];
					}
				
				}
				
				userData[i] = rowData;
			}
			
			
			var source =
			{
			    localdata: userData,
			    datatype: "local",
			    datafields:datafields,
			    addrow: function (rowid, rowdata, position, commit) {
					var rowindex = ($('#userDataGrid').jqxGrid('getboundrows')).length;
                    me.highlightRows.push({ index: rowindex, data: rowdata });
			        // synchronize with the server - send insert command
			        // call commit with parameter true if the synchronization with the server is successful 
			        //and with parameter false if the synchronization failed.
			        // you can pass additional argument to the commit callback which represents the new ID if it is generated from a DB.
			        commit(true);
			    },
			    deleterow: function (rowid, commit) {
			        // synchronize with the server - send delete command
			        // call commit with parameter true if the synchronization with the server is successful 
			        //and with parameter false if the synchronization failed.
			        commit(true);
			    },
			    updaterow: function (rowid, newdata, commit) {
			    	var rowindex = $("#userDataGrid").jqxGrid('getrowboundindexbyid', rowid);          
				    me.highlightRows.push({ index: rowindex, data: newdata });
			        // synchronize with the server - send update command
			        // call commit with parameter true if the synchronization with the server is successful 
			        // and with parameter false if the synchronization failed.
			        commit(true);
			    }
			};
			var dataAdapter = new $.jqx.dataAdapter(source);
			// initialize jqxGrid
			$("#userDataGrid").jqxGrid(
			{
			    width: 800,
			    height: 350,
			    source: dataAdapter,
			    theme: theme,
			    sortable: true,
			    showfilterrow: true,
			    enableellipsis: true,
		        filterable: true,
		        enabletooltips: true,
		        altrows: true,
		        altstart: 0,
		        altstep: 1,
			    pageable: true,
			    autoheight: true,
			    selectionmode:'singlerow',
			    columnsresize: true,
			  //  columnsreorder: true,
			    columns: columns
			});
			
		    me.initUserManagementLocaleElements();
		    me.initOperateUserGridElements();
		    me.addOperateUserGridEventListeners();
			
		    me.initUpdateUserElements();
		    me.addUpdateUserEventListeners();
			
			// initialize the popup window and buttons.
		    me.initRegisterUserElements();
		    me.addRegisterUserEventListeners();
			
			/*
			 $("#userDataGrid").on('columnreordered', function (event) {
			    var column = event.args.columntext;
			    var newindex = event.args.newindex
			    var oldindex = event.args.oldindex;
			});
			*/
			});
				
		},

		initUserManagementLocaleElements:function () {
			
			$("#addUserRowButton").val(i18n.t("userManagement.button.operationUserGrid.addUserRow"));
			$("#updateUserRowButton").val(i18n.t("userManagement.button.operationUserGrid.updateUserRow"));
			$("#deleteUserRowButton").val(i18n.t("userManagement.button.operationUserGrid.deleteUserRow"));
			
			$("#registerUserSexRadioButton1").text(i18n.t("userManagement.sex.male"));
			$("#registerUserSexRadioButton2").text(i18n.t("userManagement.sex.female"));
			$("#registerUserSexInput").val(i18n.t("userManagement.sex.male"));
			
			$("#acceptInput").text(i18n.t("userManagement.service.accept"));
			
			$("#updateUserSexRadioButton1").text(i18n.t("userManagement.sex.male"));
			$("#updateUserSexRadioButton2").text(i18n.t("userManagement.sex.female"));
			$("#updateUserSexInput").val(i18n.t("userManagement.sex.male"));
			
			$("#updateUserUpdateButton").val(i18n.t("userManagement.button.update"));
			$("#updateUserResetButton").val(i18n.t("userManagement.button.reset"));
			$("#updateUserCancelButton").val(i18n.t("userManagement.button.cancel"));
			
			$("#registerUserRegisterButton").val(i18n.t("userManagement.button.register"));
			$("#registerUserResetButton").val(i18n.t("userManagement.button.reset"));
			$("#registerUserCancelButton").val(i18n.t("userManagement.button.cancel"));
			
			$("#updateUserPopupWindow").i18n();
			
			$("#addUserPopupWindow").i18n();
		},

		parseUIDataToUserData:function (record) {
			if(record != null){
				for(var attr in record) {
					if(attr == "user.sex") {
						record[attr] = AppUtil.findSexValue(record[attr]);
					}else if(attr == "user.birthday") {
						record[attr] = record[attr].getTime();
					}else if(attr == "user.department") {
						record[attr] = AppUtil.findDepartmentValue(record[attr]);
					}else if(attr == "user.roles") {
						var roles = AppUtil.findRoleStringArray(record[attr]);
						if(roles.length > 0) {
							
							for(var i = 0;i< roles.length;i++) {
								record["user.role_Details[" + i + "].role.type"] = roles[i];
							}
							record[attr] = undefined;
						}
					}
				}
				return record;
			}
			return null;
		},

		parseUserDataToUIData:function (user) {
			if(user != null){
				for(var attr in user) {
					if(attr == "department") {
						user[attr] = AppUtil.findDepartmentString(user[attr]);
					}else if(attr == "sex") {
						user[attr] = AppUtil.findSexString(user[attr]);
					}else if(attr == "birthday") {
						user[attr] = new Date(user[attr]).Format("yyyy-MM-dd");
					}else if(attr == "user.department") {
						record[attr] = AppUtil.findDepartmentString(record[attr]);
					}else if(attr == "role_Details") {
						
						var roleDetails = user[attr];
						var roles = "";
						for(var i=0;i< roleDetails.length;i++) {
							var roleDetail = roleDetails[i];
							for(var item in roleDetail) {
								if(item == "role") {
									var role = roleDetail[item];
									if(role != null && role != "undefined" && role["type"] != null){
										roles = roles + AppUtil.findRoleString(role["type"]) + ",";
									}
								}
							}
						}
						if(roles.length > 0) {
							roles = roles.substring(0, roles.length - 1);
						}
						
						user["roles"] = roles;
						user[attr] = undefined;
					}
				}
				return user;
			}
			return null;
		}
};

var userRegister = {
		
		leave : function (){
			$("#registerUserRegisterButton").unbind('click');
			$("#registerUserSexRadioButton1").unbind('change');
			$("#registerUserSexRadioButton2").unbind('change');
			$("#registerUserCancelButton").unbind('click');
			$("#registerUserDepartmentCombobox").unbind('select');
			$("#registerUserInfoForm").unbind('validationSuccess');
		},

		visit : function (){
			this.init();
		},
		init : function(){
			var me = this;
			me.initUserRegisterLocaleElements();
			me.initUserRegisterElements();
			me.addUserRegisterEventListeners();
		},
		
		emptyRegisterUserWindow:function (){
			
			//init registerUserWindow widget data
			$("#registerUserPhoneInput").jqxMaskedInput({value: null });
			$('#registerUserBirthdayInput').jqxDateTimeInput({value:AppUtil.findDateTime("2000-01-01 00:00:00")});
			
			$("#registerUserSexRadioButton1").jqxRadioButton({checked: true});
		    $("#registerUserSexRadioButton2").jqxRadioButton({checked: false});
		    $('#acceptInput').jqxCheckBox({checked: false});
		    
		    //init registerUserWindow store data
			$("registerUserSexInput").val(i18n.t("userManagement.sex.male"));
			$("#registerUserIdInput").val(null);
			$("#registerUserAccountInput").val(null);
			$("#registerUserRealNameInput").val(null);
			$("#registerUserPasswordInput").val(null);
			$("#registerUserPasswordConfirmInput").val(null);
			$("#registerUserAddressInput").val(null);
			
			$("#registerUserDepartmentInput").val(null);
			
			$("#registerUserEmailInput").val(null);
			$("#registerUserResult").text("");
		},
		
		initUserRegisterLocaleElements:function () {
			var option = {
					fallbackLng: 'zh',
					lng: 'zh',
			//		lng: 'zh-CN',
					resGetPath: 'resources/locales/__lng__/__ns__.json',
					getAsync: false,
					ns: 'bookingnow.view'
				};
			 
			i18n.init(option);
			
			$("#registerUserSexRadioButton1").text(i18n.t("userManagement.sex.male"));
			$("#registerUserSexRadioButton2").text(i18n.t("userManagement.sex.female"));
			$("#registerUserSexInput").val(i18n.t("userManagement.sex.male"));
			
			$("#acceptInput").text(i18n.t("userManagement.service.accept"));
			
			$("#registerUserRegisterButton").val(i18n.t("userManagement.button.register"));
			$("#registerUserResetButton").val(i18n.t("userManagement.button.reset"));
			$("#registerUserCancelButton").val(i18n.t("userManagement.button.cancel"));
			$("#addUserPopupWindow").i18n();
		},

		initUserRegisterElements:function () {

		    $("#registerUserDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
		    $('#registerUserRegisterButton').jqxButton({ width: 60, height: 25, theme: theme });
		    $('#registerUserResetButton').jqxButton({ width: 60, height: 25, theme: theme });
		    $('#registerUserCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
		    
		    $('#acceptInput').jqxCheckBox({ width: 130, theme: theme ,checked: false});
		    
		    
		    $("#registerUserAddressInput").jqxMaskedInput({ mask: '省-市-区-街道-门牌号', width: 150, height: 25, theme: theme });
		    $("#registerUserPhoneInput").jqxMaskedInput({ mask: '### #### ####', width: 150, height: 25, theme: theme });
		    $('.registerUserTextInput').jqxInput({width: 150, height: 25, theme: theme });
		    var date = AppUtil.findDateTime("2000-01-01 00:00:00");
		    $('#registerUserBirthdayInput').jqxDateTimeInput({ theme: theme,width: 150, height: 25,formatString: "yyyy/MM/dd HH:mm:ss", value: $.jqx._jqxDateTimeInput.getDateTime(date) });
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
				selectedIndex: 0, 
				source: userDepartmentData, 
				displayMember: "label", 
				valueMember: "value", 
				width: 150, 
				height: 25, 
				theme: theme 
			});
			
		    // initialize validator.
		    $('#registerUserInfoForm').jqxValidator({
		     rules: [
		            { input: '#registerUserAccountInput', message: i18n.t("userManagement.validation.message.requireAccount"), action: 'keyup, blur', rule: 'required' },
		            { input: '#registerUserAccountInput', message: i18n.t("userManagement.validation.message.accountLength"), action: 'keyup, blur', rule: 'length=3,12' },
		            { input: '#registerUserRealNameInput', message: i18n.t("userManagement.validation.message.requireUsername"), action: 'keyup, blur', rule: 'required' },
		            { input: '#registerUserRealNameInput', message: i18n.t("userManagement.validation.message.requireUsernameLetter"), action: 'keyup', rule: 'notNumber' },
		            { input: '#registerUserRealNameInput', message: i18n.t("userManagement.validation.message.usernameLength"), action: 'keyup', rule: 'length=3,12' },
		            
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
			
			
		},
			
		addUserRegisterEventListeners:function () {
			var me = this;
			
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
		    	window.location.href="index.html";
		        
		    });
		        
		    $("#registerUserDepartmentCombobox").bind('select', function (event) {
		            if (event.args) {
		                var item = event.args.item;
		                if (item) {
		                	$("#registerUserDepartmentInput").val(item.value);
		                }
		            }
		        });
		    
		    
		    $('#registerUserInfoForm').bind('validationSuccess', function (event) { 
		    	// Some code here. 
		    	me.registerUser();
		    });
        
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
				"user.email" : $("#registerUserEmailInput").val(),
				"user.phone" : $("#registerUserPhoneInput").val(),
				"user.sex" : $("#registerUserSexInput").val()
			};

			var registerUserData = me.parseUIDataToUserData(registerUserUIData);
			
			$.post("registerUser.action", registerUserData, function(result) {
		    	
				if (result != null && result["id"] != null) {
					me.emptyRegisterUserWindow();
					$('#registerUserInfoForm').jqxValidator("hide");
					window.location.href="index.html";
					$("#eventLog").text("您已经注册成功，请登录！");
				} else if (result != null && result["executeResult"] != null
						&& result["executeResult"] == false) {
					$("#registerUserResult").text(i18n.t("userManagement.result.insertFail"));
				}
			});
		}
};

	
	
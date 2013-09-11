var userRegister = {
		
		leave : function (){
			$("#registerUserRegisterButton").unbind('click');
			$("#registerUserCancelButton").unbind('click');
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
			
		    $('#acceptInput').jqxCheckBox({checked: false});
		    
		    //init registerUserWindow store data
			$("#registerUserIdInput").val(null);
			$("#registerUserAccountInput").val(null);
			$("#registerUserRealNameInput").val(null);
			$("#registerUserPasswordInput").val(null);
			$("#registerUserPasswordConfirmInput").val(null);
			
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
			
			$("#registerUserDiv").i18n();
			$("#acceptInput").text(i18n.t("userManagement.service.accept"));
			
			$("#registerUserRegisterButton").val(i18n.t("userManagement.button.register"));
			$("#registerUserResetButton").val(i18n.t("userManagement.button.reset"));
			$("#registerUserCancelButton").val(i18n.t("userManagement.button.cancel"));
			
		},

		initUserRegisterElements:function () {
			var me = this;
			$('.registerUserTextInput').jqxInput({width: 150, height: 25, theme: theme });
			$('#acceptInput').jqxCheckBox({ width: 130, theme: theme ,checked: false});

		//    $("#registerUserDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
		    $('#registerUserRegisterButton').jqxButton({ width: 60, height: 25, theme: theme });
		    $('#registerUserResetButton').jqxButton({ width: 60, height: 25, theme: theme });
		    $('#registerUserCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
		    
		                        
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
		    
		    $("#registerUserCancelButton").bind('click', function (event) {
		    	window.location.href="index.html";
		        
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
			};

			var registerUserData = userManagement.parseUIDataToUserData(registerUserUIData);
			
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

	
	
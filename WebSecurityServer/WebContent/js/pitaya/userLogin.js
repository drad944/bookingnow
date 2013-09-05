var userLogin = {
		
		leave : function (){
			$("#user_register_button").unbind('click');
			$("#user_login_button").unbind('click');
			$("#userLoginForm").unbind('validationSuccess');
		},

		visit : function (){
			this.init();
		},
		init : function(){
			var me = this;
			me.initUserLoginLocaleElements();
			me.initUserLoginElements();
			me.addUserLoginEventListeners();
		},
		initUserLoginLocaleElements:function () {
			var option = {
					fallbackLng: 'zh',
					lng: 'zh',
			//		lng: 'zh-CN',
					resGetPath: 'resources/locales/__lng__/__ns__.json',
					getAsync: false,
					ns: 'bookingnow.view'
				};
			 
			i18n.init(option);
			$("#userLoginDiv").i18n();
			$("#user_login_button").val(i18n.t("userLogin.loginButtonLabel"));
			$("#user_register_button").val(i18n.t("userLogin.registerButtonLabel"));
		},

		initUserLoginElements:function () {
			
		    $("#userLoginDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
		    $('#user_login_button').jqxButton({ width: 60, height: 25, theme: theme });
		    $('#user_register_button').jqxButton({ width: 60, height: 25, theme: theme });
		    
			$('#user_account').jqxInput({width: 120, height: 20, theme: theme });
			$('#user_password').jqxInput({width: 120, height: 20, theme: theme });
		    
		    $('#userLoginForm').jqxValidator({
		     rules: [
		            { input: '#user_account', message: i18n.t("userLogin.validation.message.requireAccount"), action: 'keyup, blur', rule: 'required' },
		            { input: '#user_account', message: i18n.t("userLogin.validation.message.accountLength"), action: 'keyup, blur', rule: 'length=3,15' },
		            { input: '#user_password', message: i18n.t("userLogin.validation.message.requirePassword"), action: 'keyup, blur', rule: 'required' },
		            { input: '#user_password', message: i18n.t("userLogin.validation.message.passwordLength"), action: 'keyup, blur', rule: 'length=6,20' }
		            ], 
		            theme: theme
		    });
			
		},
			
		addUserLoginEventListeners:function () {
			$("#user_register_button").bind('click', function(event) {
				$('#userLoginForm').jqxValidator('hide');
			});

			$("#user_login_button").bind('click', function() {
				$('#userLoginForm').jqxValidator('validate');
			});

			$('#userLoginForm').bind('validationSuccess', function(event) {
				var userAccount = $('#user_account').val();
				var userPassword = $('#user_password').val();
				if (userAccount != null && userPassword != null) {
					var loginUser = {};
					loginUser["user.account"] = userAccount;
					loginUser["user.password"] = userPassword;
					AppUtil.request("loginUser.action", loginUser, function(result){
				    	
						if(result != null && result["id"] != null) {
							window.location.href="main.html";
						} else {
							$('#user_password').val(null);
							$("#eventLog").text("注册失败");
						}
					},function(){
						alert("Fail to login!");
					});
					
				}
			});

		}
};

	
	
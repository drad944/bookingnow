function initUserLoginLocaleElements() {
	var option = {
			fallbackLng: 'zh',
			lng: 'en-US',
	//		lng: 'zh-CN',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.top.menu'
		};
	 
	i18n.init(option);
	$("#userLoginDiv").i18n();
}

function initUserLoginElements() {
	
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
	
};

	
function addUserLoginEventListeners() {
	$("#user_register_button").on('click', function() {
		$('#userLoginForm').jqxValidator('hide');
	});

	$("#user_login_button").on('click', function() {
		$('#userLoginForm').jqxValidator('validate');
	});

	$('#userLoginForm').on('validationSuccess', function(event) {
		var userAccount = $('#user_account').val();
		var userPassword = $('#user_password').val();
		if (userAccount != null && userPassword != null) {

			$.post("loginUser.action", 
				{"user.account" : userAccount,"user.password" : userPassword},
				function(user) {
					if (user != null && user["id"] != null) {
						window.location.href="/main.html";
					} else {
						$('#user_password').val(null);
						$("#eventLog").text("注册失败");
					}
				});
			
		}
	});

}
	
	
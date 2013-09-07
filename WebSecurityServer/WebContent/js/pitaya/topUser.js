var topUser = {
		
		leave : function (){
			$("#topUserAccount").text(null);
			$("#topUserRoles").text(null);
			$("#topUserLoginAndOut").text("登录");
		//	$("#topUserLoginAndOut").attr("href","index.html");
		},

		visit : function (user){
			this.init(user);
		},
		
		init : function(user){
			var me = this;
			me.showTopUserInfo(user);
		},
		
		showTopUserInfo : function(user){
			if(user) {
				$("#topUserAccount").text(user["account"]);
				$("#topUserRoles").text(user["roles"]);
				$("#topUserLoginAndOut").text("注销");
			//	$("#topUserLoginAndOut").attr("href","logoutUser.action");
			}else {
				$("#topUserAccount").text(null);
				$("#topUserRoles").text(null);
				$("#topUserLoginAndOut").text("登录");
			//	$("#topUserLoginAndOut").attr("href","index.html");
			}
		}
		
};

	
	
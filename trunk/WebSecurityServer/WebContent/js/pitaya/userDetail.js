var userDetail = {
		
		leave : function (){
					$("#showUsreTable #account").text(null);
					$("#showUsreTable #name").text(null);
					$("#showUsreTable #password").text(null);
					$("#showUsreTable #address").text(null);
					$("#showUsreTable #birthday").text(null);
					$("#showUsreTable #department").text(null);
					$("#showUsreTable #email").text(null);
					$("#showUsreTable #image_relative_path").attr("src",null);
					$("#showUsreTable #phone").text(null);
					$("#showUsreTable #sex").text(null);
		},

		visit : function (){
			this.init();
		},
		
		init : function(){
			var me = this;
			me.showUserDetailInfo();
		},
		
		
		showUserDetailInfo:function () {
			$.post("findLoginUser.action", function(user) {
				
				user = userManagement.parseUserDataToUIData(user);
				if (user != null && user.id != null) {
					$("#showUsreTable #account").text(user["account"]);
					$("#showUsreTable #name").text(user["name"]);
					$("#showUsreTable #password").text(user["password"]);
					$("#showUsreTable #address").text(user["address"]);
					$("#showUsreTable #birthday").text(user["birthday"]);
					$("#showUsreTable #department").text(user["department"]);
					$("#showUsreTable #email").text(user["email"]);
					$("#showUsreTable #image_relative_path").attr("src",user["image_relative_path"]);
					$("#showUsreTable #phone").text(user["phone"]);
					$("#showUsreTable #sex").text(user["sex"]);
				}
			});
		}
		
};

	
	
var topUser = {
		
		leave : function (){
			$("#topUserLoginAndOut").val("登录");
			$("#topUserAccount").val("匿名");
			$("#topUserRoles").val("空角色");
			$("#topUserLoginAndOut").unbind("click");
		},

		visit : function (user){
			this.init(user);
		},
		
		init : function(user){
			var me = this;
			me.initTopUserElement(user);
		},
		
		initTopUserElement : function(user){
			var me = this;
			
			if(user) {
				$("#jqxMenu").jqxMenu({ width: '150px',autoOpen: false, showTopLevelArrows: true, height: '30px', theme: theme });
				
				$("#topUserLoginAndOut").text("退出");
				$("#topUserAccount").text(user["account"]);
				$("#topUserRoles").text(user["roles"]);
				
				$("#topUserLoginAndOut").bind('click', function () {
	                AppUtil.request("logoutUser.action", null, function(result){
	    				if(result == true){
	    					window.location.href="index.html";
	    				}
	    			}, function(){
	    				alert("Fail to get logout from server!");
	    			});
                });
				
			}else {
				$("#topUserLoginAndOut").jqxButton({ width: '80', theme: theme });
				
				$("#topUserLoginAndOut").val("登录");
				$("#topUserAccount").val("匿名");
				$("#topUserRoles").val("无角色");
				
				$("#topUserLoginAndOut").bind('click', function () {
					window.location.href="index.html";
                });
			}
			
		}
		
};

	
	
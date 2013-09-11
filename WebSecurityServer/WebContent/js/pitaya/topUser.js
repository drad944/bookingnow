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
				$("#topUserLoginAndOut").jqxButton({ width: '80', theme: theme });
				$("#topUserAccount").jqxButton({ width: '80', theme: theme });
				$("#topUserRoles").jqxButton({ width: '80', theme: theme });
				
				$("#topUserLoginAndOut").val("退出");
				$("#topUserAccount").val(user["account"]);
				$("#topUserRoles").val(user["roles"]);
				
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
				$("#topUserAccount").jqxButton({ disabled: true,width: '80', theme: theme });
				$("#topUserRoles").jqxButton({ disabled: true,width: '80', theme: theme });
				
				$("#topUserLoginAndOut").val("登录");
				$("#topUserAccount").val("匿名");
				$("#topUserRoles").val("无角色");
				
				$("#topUserLoginAndOut").bind('click', function () {
					window.location.href="index.html";
                });
			}
			
		}
		
};

	
	
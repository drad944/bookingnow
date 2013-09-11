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
				
				$("#jqxMenu").bind('itemclick', function (event) {
			        if(event.args.id == 1) {
			        	
			        }else if(event.args.id == 2) {
			        	AppUtil.request("logoutUser.action", null, function(result){
		    				if(result == true){
		    					window.location.href="index.html";
		    				}
		    			}, function(){
		    				alert("Fail to get logout from server!");
		    			});
			        }
			    });
				
			}else {
				$("#topUserLoginAndOut").text("登录");
				$("#topUserAccount").text("匿名");
				$("#topUserRoles").text("无角色");
				
				$("#jqxMenu").bind('itemclick', function (event) {
			        if(event.args.id == 1) {
			        	
			        }else if(event.args.id == 2) {
			        	AppUtil.request("logoutUser.action", null, function(result){
		    				if(result == true){
		    					window.location.href="index.html";
		    				}
		    			}, function(){
		    				alert("Fail to get logout from server!");
		    			});
			        }
			    });
			}
			
		}
		
};

	
	
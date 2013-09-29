var topUser = {
		
		leave : function (){
			$("#jqxMenu").unbind('itemclick');
			$('#jqxMenu').jqxMenu('destroy'); 
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
			var data = {};
			if(user) {
				
				data = [
			    {
			        "id": "1",
			        "text": user["account"],
			        "parentid": "-1",
			        "subMenuWidth": '100px'
			    }
			    ,
			    {
			        "text": user["roles"],
			        "id": "2",
			        "parentid": "-1"
			    }
			    , {
			        "id": "3",
			        "parentid": "1",
			        "text": "修改用户信息"
			    }, {
			        "id": "4",
			        "parentid": "1",
			        "text": "退出"
			    }];
			    
				
			}else {
				data = [
			    {
			        "id": "1",
			        "text": "匿名",
			        "parentid": "-1",
			        "subMenuWidth": '100px'
			    }
			    ,
			    {
			        "text": "无角色",
			        "id": "2",
			        "parentid": "-1"
			    }
			    , {
			        "id": "3",
			        "parentid": "1",
			        "text": "登录"
			    }];
			    
			}
			
		    var source =
		    {
		        datatype: "json",
		        datafields: [
		            { name: 'id' },
		            { name: 'parentid' },
		            { name: 'text' },
		            { name: 'subMenuWidth' }
		        ],
		        id: 'id',
		        localdata: data
		    };
		    var dataAdapter = new $.jqx.dataAdapter(source);
		    dataAdapter.dataBind();
		    var records = dataAdapter.getRecordsHierarchy('id', 'parentid', 'items', [{ name: 'text', map: 'label'}]);
		    $('#jqxMenu').jqxMenu({ 
		    	source: records, 
		    	height: 30, 
		    	autoOpen: true,
		    	showTopLevelArrows: true, 
		    	theme: theme, 
		    	width: '200px' 
		    });
		   	
		    if(user) {
		   		$("#jqxMenu").bind('itemclick', function (event) {
		        if(event.args.id == 1) {
		        	
		        }else if(event.args.id == 2) {
		        	
		        }else if(event.args.id == 3) {
		        	openSubPage('framework_main','page/security/showUserInfo.html','content',userDetail,null);
		        }else if(event.args.id == 4) {
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
		   		 $("#jqxMenu").bind('itemclick', function (event) {
			        if(event.args.id == 1) {
			        	
			        }else if(event.args.id == 2) {
			        	
			        }else if(event.args.id == 3) {
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

	
	
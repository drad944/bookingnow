var menuManagement = {
		
		leave : function (){
					$("#eventLog").text(null);
					$("#myMenu").unbind('itemclick');
		},

		visit : function (){
			this.init();
		},
		
		init : function(){
			var me = this;
			me.parseMenuHtml();
		},
		
		parseMenuHtml:function () {
			/*
			var setLng = $.url().param('setLng');
		    if (setLng)
		    {
		      language_complete = setLng.split("-");
		    }
		    else
		    {
		      language_complete = navigator.language.split("-");
		    }
		    language = (language_complete[0]);
		    */
			
			var option = {
			//		fallbackLng: 'en-US',
			//		lng: 'en-US',
					lng: 'zh',
					fallbackLng: 'zh',
					resGetPath: 'resources/locales/__lng__/__ns__.json',
					getAsync: false,
					ns: 'bookingnow.view',
					fallbackToDefaultNS: true,
					load:'current'
				};
			i18n.init(option);
			
		 //   var theme = getDemoTheme();
		    var data = [
		    {
		        "id": "1",
		        "text": i18n.t("menu.orderManagement.home"),
		        "parentid": "-1",
		        "subMenuWidth": '250px'
		    }
		    ,
		    {
		        "text": i18n.t("menu.foodManagement.home"),
		        "id": "2",
		        "parentid": "-1",
		        "subMenuWidth": '250px'
		    }
		    , {
		        "id": "3",
		        "parentid": "-1",
		        "text": i18n.t("menu.userManagement.home")
		    }, {
		        "id": "4",
		        "parentid": "-1",
		        "text": i18n.t("menu.tableManagement.home")
		    }, {
		        "id": "5",
		        "parentid": "-1",
		        "text": i18n.t("menu.map.home")
		    }, {
		        "id": "6",
		        "parentid": "3",
		        "text": i18n.t("menu.userManagement.showAllUsers")
		    }, {
		        "id": "8",
		        "parentid": "3",
		        "text": i18n.t("menu.userManagement.userDetailInfo")
		    }, {
		        "id": "9",
		        "parentid": "3",
		        "text": i18n.t("menu.userManagement.changeUserImage")
		    }, {
		        "id": "10",
		        "parentid": "-1",
		        "text": i18n.t("menu.admin")
		    }];
		    // prepare the data
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
		    // create data adapter.
		    var dataAdapter = new $.jqx.dataAdapter(source);
		    // perform Data Binding.
		    dataAdapter.dataBind();
		    // get the menu items. The first parameter is the item's id. The second parameter is the parent item's id. The 'items' parameter represents 
		    // the sub items collection name. Each jqxTree item has a 'label' property, but in the JSON data, we have a 'text' field. The last parameter 
		    // specifies the mapping between the 'text' and 'label' fields.  
		    var records = dataAdapter.getRecordsHierarchy('id', 'parentid', 'items', [{ name: 'text', map: 'label'}]);
		    $('#myMenu').jqxMenu({ source: records, height: 30, autoOpen: true,showTopLevelArrows: true, theme: theme, width: '800px' });
		    $("#myMenu").bind('itemclick', function (event) {
		    	if(currentPage.leave){
		    		currentPage.leave();
		    	}
		        $("#eventLog").text("Id: " + event.args.id + ", Text: " + $(event.args).text());
		        if(event.args.id == 1) {
		        	openContentPage('framework_main','page/common/orderManagement.html','content');
		        	currentPage = orderManagement;
		        }else if(event.args.id == 2) {
		        	openContentPage('framework_main','page/common/foodManagement.html','content');
		        	currentPage = foodManagement;
		        }else if(event.args.id == 3) {
		        	openContentPage('framework_main','page/common/userManagement.html','content');
		        	currentPage = userManagement;
		        	
		        }else if(event.args.id == 4) {
		        	openContentPage('framework_main','page/common/tableManagement.html','content');
		        	currentPage = tableManagement;
		        }else if(event.args.id == 5) {
		        	//openContentPage('page/common/map.html')
		        }else if(event.args.id == 6) {
		        	openContentPage('framework_main','page/common/userManagement.html','content');
		        	currentPage = userManagement;
		        }else if(event.args.id == 7) {
		        	openContentPage('framework_main','page/common/registerUser.html','content');
		        	currentPage = {};
		        }else if(event.args.id == 8) {
		        	openContentPage('framework_main','page/security/showUserInfo.html','content');
		        	currentPage = userDetail;
		        }else if(event.args.id == 9) {
		        	openContentPage('framework_main','page/security/updateUserPicture.html','content');
		        	uploadUserImage();
		        	currentPage = {};
		        }else if(event.args.id == 10) {
		        	openContentPage('framework_main','page/common/admin.html','content');
		        	currentPage = adminManagement;
		        }else if(event.args.id == 11) {
		        	openContentPage('framework_main','page/common/checkoutManagement.html','content');
		        	currentPage = checkoutManagement;
		        }
		    	if(currentPage.visit){
		    		currentPage.visit();
		    	}
		    });
		    var centerItems = function () {
		        var firstItem = $($("#myMenu ul:first").children()[0]);
		        firstItem.css('margin-left', 0);
		        var width = 0;
		        var borderOffset = 2;
		        $.each($("#myMenu ul:first").children(), function () {
		            width += $(this).outerWidth(true) + borderOffset;
		        });
		        var menuWidth = $("#myMenu").outerWidth();
		        firstItem.css('margin-left', (menuWidth / 2 ) - (width / 2));
		    };
		    
		    
		    centerItems();
		    
		    $(window).resize(function () {
		        centerItems();
		    });
		}

		
};

	
	
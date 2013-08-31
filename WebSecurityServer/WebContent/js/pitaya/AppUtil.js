var currentPage = {};

var AppUtil = {
		
	setStyle : function(domObj, parameters){
		 for(var p in parameters){ 
			 domObj.style[p] = parameters[p];
		 }
	},

	request : function(url, parameter, success, fail, method){
		if(method == null || method == "post"){
			$.post(url, parameter, function(data){
				if(success){
					success(data);
				}
			}, "json").fail(function(){
				if(fail){
				  fail();
				}
			});
		} else {
			$.get(url, parameter, function(data){
				if(success){
					success(data);
				}
			}, "json").fail(function(){
				if(fail){
				  fail();
				}
			});
		}
	},
	
	getFoodStatus : function(status){
		switch(status){
			case Constants.FOOD_STATUS_OK:
				return "在售";
			case Constants.FOOD_STATUS_NO:
				return "售完";
			default:
				return "未知";
		};
	},
	
	getRoleName : function(role){
		for(var i=0; i < Constants.ROLES.length; i++){
			if(Constants.ROLES[i].value == role){
				return Constants.ROLES[i].name;
			}
		}
		return "未知";
	}
};

function userlogin(){
	var user = {
			"user.account":$("#account").val(),
			"user.password":$("#password").val()
		};
	
	$.post("loginUser.action", 
			user, 
		    function(result){
				if(result != null && result["id"] != null) {
					
				}
				
		    },
	"json");
}

function callAction(actionName,parameter) {
		var result = {};
		$.post(actionName, parameter,function(data){
			
			result = data;
			});
		
		return result;
}

String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substr(0,str.length)==str)
	  return true;
	else
	  return false;
	return true;
	};

function openContentPage(sourceDiv,url,targetDiv) {
	var contentPage = $('#' + sourceDiv);
//	$.post(url,function(data) {
//        var tmp = $('<div></div>').html(data);
// 
//        data = tmp.find('#' + targetDiv).html();
//        tmp.remove();
//         
//        contentPage.html(data);
//        data = null;
//    });
	contentPage.load(url, null, function(data) {
			var tmp = $('<div></div>').html(data);
		 
	        data = tmp.find('#' + targetDiv).html();
	        tmp.remove();
		         
		    contentPage.html(data);
		    data = null;
	    });
}

function removeElementFromPage(targetDiv) {
	var contentPage = $('#' + targetDiv);
	contentPage.remove();
}

function parseMenuHtml() {
	var option = {
	//		fallbackLng: 'en-US',
	//		lng: 'en-US',
			lng: 'zh-CN',
			fallbackLng: 'zh-CN',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.top.menu',
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
        "id": "7",
        "parentid": "3",
        "text": i18n.t("menu.userManagement.registerUser")
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
    }, {
        "id": "11",
        "parentid": "1",
        "text": i18n.t("menu.orderManagement.checkoutOrder")
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
    $("#myMenu").on('itemclick', function (event) {
    	if(currentPage.leave){
    		currentPage.leave();
    	}
        $("#eventLog").text("Id: " + event.args.id + ", Text: " + $(event.args).text());
        if(event.args.id == 1) {
        	openContentPage('framework_main','page/common/orderManagement.html','content');
        	parseOrderGridHtml();
        }else if(event.args.id == 2) {
        	openContentPage('framework_main','page/common/foodManagement.html','content');
        	currentPage = foodManagement;
        }else if(event.args.id == 3) {
        	openContentPage('framework_main','page/common/userManagement.html','content');
        	parseUserGridHtml();
        	/*
        	var bodyhtml = $("body").html();
        	bodyhtml = null;
        	$("table+div").remove();
        	
        	var bodyhtml = $("body").html();
        	bodyhtml = null;
        	 */
        	
        	
        }else if(event.args.id == 4) {
        	openContentPage('framework_main','page/common/tableManagement.html','content');
        	parseTableGridHtml();
        }else if(event.args.id == 5) {
        	//openContentPage('page/common/map.html')
        }else if(event.args.id == 6) {
        	openContentPage('framework_main','page/common/userManagement.html','content');
        	parseUserGridHtml();
        }else if(event.args.id == 7) {
        	openContentPage('framework_main','page/common/registerUser.html','content');
        	
        }else if(event.args.id == 8) {
        	openContentPage('framework_main','page/security/showUserInfo.html','content');
        	showUserDetailInfo();
        }else if(event.args.id == 9) {
        	openContentPage('framework_main','page/security/updateUserPicture.html','content');
        	uploadUserImage();
        }else if(event.args.id == 10) {
        	openContentPage('framework_main','page/common/admin.html','content');
        	init();
        }else if(event.args.id == 11) {
        	openContentPage('framework_main','page/common/checkoutManagement.html','content');
        	parseCheckOrderGridHtml();
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


function findSexString(value) {
	var option = {
			fallbackLng: 'zh',
			lng: 'en-US',
	//		lng: 'zh-CN',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.content.userManagement'
		};
	 
	i18n.init(option);
	
	if(value == 2) {
		return i18n.t("sex.male");
	}else if(value == 3){
		return i18n.t("sex.female");
	}
	return i18n.t("sex.other");
}

function findSexValue(label) {
	var option = {
			fallbackLng: 'zh',
			lng: 'en-US',
	//		lng: 'zh-CN',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.content.userManagement'
		};
	 
	i18n.init(option);
	
	if(label == i18n.t("sex.male")) {
		return 2;
	}else if(label == i18n.t("sex.female")){
		return 3;
	}
	return 1;
}

function findDateTime(datetimeString) {
	//var s = "2005-02-05 01:02:03";   
	var d = new Date(Date.parse(datetimeString.replace(/-/g,"/")));
	return d;
}

function trim(myString){
	if(myString != null && myString != "") {
		return myString.replace(/(^\s*)|(\s*$)/g, "");
	}
	return myString;
}

Date.prototype.Format = function (fmt) {
	//how to call it:
	//var time1 = new Date().Format("yyyy-MM-dd");
	//var time2 = new Date().Format("yyyy-MM-dd HH:mm:ss");
	
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "H+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) {
    	fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o){
    	if (new RegExp("(" + k + ")").test(fmt)) {
    		fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    	}
    }
    
    return fmt;
};

function findRoleStringArray(value) {
	var values = null;
	if(value != null) {
		values = value.split(",");
	}
	if(values != null){
		for(var i = values.length - 1;i >= 0;i--) {
			values[i] = trim(values[i]);
		}
	}
	 
	 return values;
}

function findRoleString(value) {
	var option = {
			fallbackLng: 'zh',
			lng: 'en-US',
	//		lng: 'zh-CN',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.content.userManagement'
		};
	 
	i18n.init(option);
	var values = null;
	if(value.toString.length > 0) {
		values = value.split(",");
	}
	if(values != null&& values.length > 0){
		for(var i = values.length;i >= 0;i--) {
			values[i] = trim(values[i]);
		}
	}
	
	var userRoleData = [
	    				{ value: 2, label: i18n.t("role.ANONYMOUS") },
	    				{ value: 3, label: i18n.t("role.CUSTOMER") },
	    				{ value: 4, label: i18n.t("role.CUSTOMER_VIP1") },
	    				{ value: 5, label: i18n.t("role.CUSTOMER_VIP2") },
	    				{ value: 6, label: i18n.t("role.WELCOMER") },
	    				{ value: 7, label: i18n.t("role.CHEF") },
	    				{ value: 8, label: i18n.t("role.WAITER") },
	    				{ value: 9, label: i18n.t("role.CASHIER") },
	    				{ value: 10, label: i18n.t("role.MANAGER") },
	    				{ value: 11, label: i18n.t("role.ADMIN") }
	            ];
	var valueString = "";
	if(values != null && values.length > 0){
		for(var j=0;j < values.length;j++) {
			 var tempValue = values[j];
			 for(var i = 0;i < userRoleData.length;i++) {
				 if(userRoleData[i].value == tempValue) {
					 valueString = valueString + userRoleData[i].label + ",";
				 }
			 }
		 }
		 if(valueString.length > 0) {
			 valueString = valueString.substring(0, valueString.length - 2);
		 }
	}
	 
	 return valueString;
}

function findRoleValueArray(label) {
	var labels = null;
	if(label != null) {
		labels = label.split(",");
	}
	if(labels != null) {
		for(var i = labels.length - 1;i >= 0;i--) {
			labels[i] = trim(labels[i]);
		}
	}
	 
	 return labels;
}

function findRoleValue(label) {
	var option = {
			fallbackLng: 'zh',
			lng: 'en-US',
	//		lng: 'zh-CN',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.content.userManagement'
		};
	 
	i18n.init(option);
	var labels = null;
	if(label.length > 0) {
		labels = label.split(",");
	}
	if(labels != null && labels.length > 0) {
		for(var i = labels.length;i >= 0;i--) {
			labels[i] = trim(labels[i]);
		}
	}
	
	
	var userRoleData = [
	    				{ value: 2, label: i18n.t("role.ANONYMOUS") },
	    				{ value: 3, label: i18n.t("role.CUSTOMER") },
	    				{ value: 4, label: i18n.t("role.CUSTOMER_VIP1") },
	    				{ value: 5, label: i18n.t("role.CUSTOMER_VIP2") },
	    				{ value: 6, label: i18n.t("role.WELCOMER") },
	    				{ value: 7, label: i18n.t("role.CHEF") },
	    				{ value: 8, label: i18n.t("role.WAITER") },
	    				{ value: 9, label: i18n.t("role.CASHIER") },
	    				{ value: 10, label: i18n.t("role.MANAGER") },
	    				{ value: 11, label: i18n.t("role.ADMIN") }
	            ];
	var labelString = new Array();
	if(labels != null && labels.length > 0) {
		for(var j=0;j < labels.length;j++) {
			 var tempLabel = labels[j];
			 for(var i = 0;i < userRoleData.length;i++) {
				 if(userRoleData[i].label == tempLabel) {
					 labelString[j] = userRoleData[i].value;
					 break;
				 }
			 }
		 }
	}
	 
	 return labelString;
}

function findDepartmentString(value) {
	var option = {
			fallbackLng: 'zh',
			lng: 'en-US',
	//		lng: 'zh-CN',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.content.userManagement'
		};
	 
	i18n.init(option);
	
	 var userDepartmentData = [
	                                { value: 2, label: i18n.t("department.BUSSINESS") },
	                                { value: 3, label: i18n.t("department.PRODUCTION") },
	                                { value: 4, label: i18n.t("department.FINANCE") },
	                                { value: 5, label: i18n.t("department.PERSONNEL") },
	                                { value: 6, label: i18n.t("department.DEVERLOPE") },
	                                { value: 7, label: i18n.t("department.MANAGEMENT") }
	                            ];
	 
	 for(var i = 0;i < userDepartmentData.length;i++) {
		 if(userDepartmentData[i].value == value) {
			 return userDepartmentData[i].label;
		 }
	 }
	 
	 return value;
}

function findDepartmentValue(label) {
	var option = {
			fallbackLng: 'zh',
			lng: 'en-US',
	//		lng: 'zh-CN',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.content.userManagement'
		};
	 
	i18n.init(option);
	
	 var userDepartmentData = [
									{ value: 2, label: i18n.t("department.BUSSINESS") },
									{ value: 3, label: i18n.t("department.PRODUCTION") },
									{ value: 4, label: i18n.t("department.FINANCE") },
									{ value: 5, label: i18n.t("department.PERSONNEL") },
									{ value: 6, label: i18n.t("department.DEVERLOPE") },
									{ value: 7, label: i18n.t("department.MANAGEMENT") }
	                            ];
	 for(var i = 0;i < userDepartmentData.length;i++) {
		 if(userDepartmentData[i].label == label) {
			 return userDepartmentData[i].value;
		 }
	 }
	 
	 return label;
}

function findTableStatusValue(label) {
	var option = {
			fallbackLng: 'zh',
			lng: 'en-US',
	//		lng: 'zh-CN',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.content.tableManagement'
		};
	 
	i18n.init(option);
	
	 var tableStatusData = [
	                                { value: 2, label: i18n.t("status.EMPTY") },
	                                { value: 3, label: i18n.t("status.BOOKING") },
	                                { value: 4, label: i18n.t("status.USING") }
	                            ];
	 for(var i = 0;i < tableStatusData.length;i++) {
		 if(tableStatusData[i].label == label) {
			 return tableStatusData[i].value;
		 }
	 }
	 
	 return label;
}

function findTableStatusLable(value) {
	var option = {
			fallbackLng: 'zh',
			lng: 'en-US',
	//		lng: 'zh-CN',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.content.tableManagement'
		};
	 
	i18n.init(option);
	 var tableStatusData = [
	                                { value: 2, label: i18n.t("status.EMPTY") },
	                                { value: 3, label: i18n.t("status.BOOKING") },
	                                { value: 4, label: i18n.t("status.USING") }
	                            ];
	 for(var i = 0;i < tableStatusData.length;i++) {
		 if(tableStatusData[i].value == value) {
			 return tableStatusData[i].label;
		 }
	 }
	 
	 return value;
}

function findOrderStatusValue(label) {
	var option = {
			fallbackLng: 'zh',
			lng: 'en-US',
	//		lng: 'zh-CN',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.content.orderManagement'
		};
	 
	i18n.init(option);
	 var orderStatusData = [
	                                { value: 1, label: i18n.t("status.NEW") },
	                                { value: 2, label: i18n.t("status.WELCOMER_NEW") },
	                                { value: 3, label: i18n.t("status.WAITING") },
	                                { value: 4, label: i18n.t("status.COMMITED") },
	                                { value: 5, label: i18n.t("status.PAYING") },
	                                { value: 6, label: i18n.t("status.FINISHED") },
	                                { value: 7, label: i18n.t("status.UNAVAILABLE") },
	                                { value: 8, label: i18n.t("status.AVAILABLE") }
	                            ];
	 for(var i = 0;i < orderStatusData.length;i++) {
		 if(orderStatusData[i].label == label) {
			 return orderStatusData[i].value;
		 }
	 }
	 
	 return label;
}

function findOrderStatusLable(value) {
	var option = {
			fallbackLng: 'zh',
			lng: 'en-US',
	//		lng: 'zh-CN',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.content.orderManagement'
		};
	 
	i18n.init(option);
	 var orderStatusData = [
	                                { value: 1, label: i18n.t("status.NEW") },
	                                { value: 2, label: i18n.t("status.WELCOMER_NEW") },
	                                { value: 3, label: i18n.t("status.WAITING") },
	                                { value: 4, label: i18n.t("status.COMMITED") },
	                                { value: 5, label: i18n.t("status.PAYING") },
	                                { value: 6, label: i18n.t("status.FINISHED") },
	                                { value: 7, label: i18n.t("status.UNAVAILABLE") },
	                                { value: 8, label: i18n.t("status.AVAILABLE") }
	                            ];
	 for(var i = 0;i < orderStatusData.length;i++) {
		 if(orderStatusData[i].value == value) {
			 return orderStatusData[i].label;
		 }
	 }
	 
	 return value;
}

function findImgOriginalSize(src) {
	var imageSize = {};
	if(src != null) {
		var img = new Image();
		img.src = src;
		 
		if(img.complete){
		    getImgOriginalSize.call(img);
		    img = null;
		}else{
		    img.onload=function(){
		        getImgOriginalSize.call(img);
		        img = null;
		    };
		}
		
		function getImgOriginalSize(){
		    imageSize["width"] = this.width;
		    imageSize["height"] = this.height;
		}
	}
	return imageSize;
}


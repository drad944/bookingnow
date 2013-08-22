/**
 * 
 */
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
	$.post(url,function(data) {
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

function parseOrderToGridData(matchedOrders) {
	if(matchedOrders != null && matchedOrders.result != null){
		orders = matchedOrders.result;
	}
	
	var obj = { width: 700, height: 400, title: "Grid From matchedOrders", flexHeight: true };
	var orderSize = orders.length;
	var dataPrefixString="{";
	var dataSuffixString = "}";
	var dataDelimiter = ",";
	var startString="[";
	var endString = "]";
	var data= startString;
	for(var i = 0;i < orderSize; i++) {
		order = orders[i];
		if(i==0) {
			/*
			 obj.colModel = startString;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Id" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataType:" + "integer" + dataDelimiter + "dataIndx:" + "rank" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Allowance" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataType:" + "float" + dataDelimiter + "dataIndx:" + "allowance" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Customer_count" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataType:" + "integer" + dataDelimiter + "dataIndx:" + "customer_count" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Customer_id" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataType:" + "integer" + dataDelimiter + "dataIndx:" + "customer_id" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "ModifyTime" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataType:" + "integer" + dataDelimiter + "dataIndx:" + "modifyTime" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "PrePay" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataType:" + "integer" + dataDelimiter + "dataIndx:" + "prePay" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Status" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataType:" + "integer" + dataDelimiter + "dataIndx:" + "status" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Submit_time" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataType:" + "integer" + dataDelimiter + "dataIndx:" + "submit_time" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Total_price" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataType:" + "float" + dataDelimiter + "dataIndx:" + "total_price" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "User_id" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataType:" + "integer" + dataDelimiter + "dataIndx:" + "user_id" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Enabled" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataType:" + "integer" + dataDelimiter + "dataIndx:" + "enabled" + dataSuffixString;
			 obj.colModel = obj.colModel + endString;
			 
			 
			 */
			 obj.colModel = startString;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Id" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataIndx:" + "rank" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Allowance" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataIndx:" + "allowance" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Customer_count" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataIndx:" + "customer_count" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Customer_id" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataIndx:" + "customer_id" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "ModifyTime" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataIndx:" + "modifyTime" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "PrePay" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataIndx:" + "prePay" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Status" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataIndx:" + "status" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Submit_time" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataIndx:" + "submit_time" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Total_price" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataIndx:" + "total_price" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "User_id" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataIndx:" + "user_id" + dataSuffixString + dataDelimiter;
			 obj.colModel = obj.colModel + dataPrefixString + "title:" + "Enabled" + dataDelimiter + "width:" + "100" + dataDelimiter + "dataIndx:" + "enabled" + dataSuffixString;
			 obj.colModel = obj.colModel + endString;
		}
		
		var rowData = dataPrefixString;
		rowData = rowData + "id:" + order.id + dataDelimiter;
		rowData = rowData + "allowance:" + order.allowance + dataDelimiter;
		rowData = rowData + "customer_count:" + order.id + dataDelimiter;
		rowData = rowData + "customer_id:" + order.id + dataDelimiter;
		rowData = rowData + "modifyTime:" + order.id + dataDelimiter;
		rowData = rowData + "prePay:" + order.id + dataDelimiter;
		rowData = rowData + "status:" + order.id + dataDelimiter;
		rowData = rowData + "submit_time:" + order.id + dataDelimiter;
		rowData = rowData + "total_price:" + order.id + dataDelimiter;
		rowData = rowData + "user_id:" + order.id + dataDelimiter;
		rowData = rowData + "enabled:" + order.id;
		rowData = rowData + dataSuffixString;
		
		if(i == (orderSize - 1)) {
			data = data + rowData + endString;
		}else {
			data = data + rowData + dataDelimiter;
		}
	}
	
     obj.dataModel = {
         data: data,
         location: "local",
         sorting: "local",
         paging: "local",
         curPage: 1,
         rPP: 10,
         sortIndx: "status",
         sortDir: "up",
         rPPOptions: [1, 10, 20, 30, 40, 50, 100, 500, 1000]
     };
     return obj;
} 

function parseOrderToGrid(matchedOrders) {
	
} 


function parseMenuHtml() {
	var option = {
			fallbackLng: 'zh',
			lng: 'en-US',
	//		lng: 'zh-CN',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.top.menu'
		};
	 
	i18n.init(option);
	
    var theme = getDemoTheme();
    var data = [
    {
        "id": "1",
        "text": i18n.t("menu.orderManagement.home"),
        "parentid": "-1",
        "subMenuWidth": '250px'
    },
    {
        "text": i18n.t("menu.foodManagement.home"),
        "id": "2",
        "parentid": "-1",
        "subMenuWidth": '250px'
    }, {
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
    $('#myMenu').jqxMenu({ source: records, height: 30, autoOpen: true, theme: theme, width: '800px' });
    $("#myMenu").on('itemclick', function (event) {
        $("#eventLog").text("Id: " + event.args.id + ", Text: " + $(event.args).text());
        if(event.args.id == 1) {
        	openContentPage('framework_main','page/common/orderManagement.html','content');
        	parseOrderGridHtml();
        }else if(event.args.id == 2) {
        	openContentPage('framework_main','page/common/foodManagement.html','content');
        }else if(event.args.id == 3) {
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
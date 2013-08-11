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

function openContentPage(url) {
	var contentPage = $('#framework_main');
	$.post(url,function(data) {
        var tmp = $('<div></div>').html(data);
 
        data = tmp.find('#content').html();
        tmp.remove();
         
        contentPage.html(data);
    });
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

function loadProperties(){
	jQuery.i18n.properties({
		name:'bookingNow',
		path:'../../resources/i18n/',
		//language:'en_US',
		mode:'map',
		callback: function() {
			$('#user_account').text($.i18n.prop('user_account')); 
			$('#user_password').text($.i18n.prop('user_password')); 
			$('#user_login').val($.i18n.prop('user_login'));
			$('#user_reset').val($.i18n.prop('user_reset'));
		} 
	});
}


function parseMenuHtml() {
    var theme = getDemoTheme();
    var data = [
    {
        "id": "1",
        "text": "订单管理",
        "parentid": "-1",
        "subMenuWidth": '250px'
    },
    {
        "text": "菜单管理",
        "id": "2",
        "parentid": "-1",
        "subMenuWidth": '250px'
    }, {
        "id": "3",
        "parentid": "-1",
        "text": "用户管理"
    }, {
        "id": "4",
        "parentid": "-1",
        "text": "餐桌管理"
    }, {
        "id": "5",
        "parentid": "-1",
        "text": "地图"
    }, {
        "id": "6",
        "parentid": "3",
        "text": "注册用户"
    }, {
        "id": "7",
        "parentid": "3",
        "text": "用户详情"
    }, {
        "id": "8",
        "parentid": "3",
        "text": "修改头像"
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
    $('#myMenu').jqxMenu({ source: records, height: 30, theme: theme, width: '800px' });
    $("#myMenu").on('itemclick', function (event) {
        $("#eventLog").text("Id: " + event.args.id + ", Text: " + $(event.args).text());
        if(event.args.id == 1) {
        	openContentPage('page/common/orderManagement.html');
        }else if(event.args.id == 2) {
        	openContentPage('page/common/foodManagement.html');
        }else if(event.args.id == 3) {
        	openContentPage('page/common/userManagement.html');
        	parseUserGridHtml();
        	
        }else if(event.args.id == 4) {
        	openContentPage('page/common/tableManagement.html');
        }else if(event.args.id == 5) {
        	//openContentPage('page/common/map.html')
        }else if(event.args.id == 6) {
        	openContentPage('page/common/registerUser.html');
        	
        }else if(event.args.id == 7) {
        	openContentPage('page/security/showUserInfo.html');
        	showUserDetailInfo();
        }else if(event.args.id == 8) {
        	openContentPage('page/security/updateUserPicture.html');
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





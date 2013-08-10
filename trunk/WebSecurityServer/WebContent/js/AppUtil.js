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

function parseMenu() {
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
        	parseUserGrid();
        	
        }else if(event.args.id == 4) {
        	openContentPage('page/common/tableManagement.html');
        }else if(event.args.id == 5) {
        	//openContentPage('page/common/map.html')
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

function parseUserGrid() {
		$.post("findUser.action", 
			{"user.enabled": true}, 
			function(matchedusers){
	if(matchedusers != null && matchedusers.result != null){
	users = matchedusers.result;
	}
	
	var imageRenderer = function (row, datafield, value) {
	return '<img style="margin-left: 5px;" height="60" width="50" src="' + value + '"/>';
	};
	var userSize = users.length;
	var userData = {};
	
	var columns = {};
	
	var datafields = {};
	for(var i = 0;i < userSize; i++) {
	user = users[i];
	if(i==0) {
	var j=0;
	
	for(var item in user) {
		var datafield = {};
		var column = {};
		
		if(item == "id"){
			datafield["type"] = "number";
			column["text"] = "Id";
			
		}else if(item == "modifyTime") {
			datafield["type"] = "number";
			column["text"] = "ModifyTime";
			
		}else if(item == "image_size") {
			datafield["type"] = "number";
			column["text"] = "Image_size";
			
		}else if(item == "image_relative_path") {
			datafield["type"] = "string";
			column["text"] = "Image_relative_path";
			column["cellsrenderer"] = imageRenderer;
		}else if(item == "account") {
			datafield["type"] = "string";
			column["text"] = "Account";
			
		}else if(item == "name") {
			datafield["type"] = "string";
			column["text"] = "Name";
			
		}else if(item == "password") {
			datafield["type"] = "string";
			column["text"] = "Password";
			
		}else if(item == "phone") {
			datafield["type"] = "string";
			column["text"] = "Phone";
			
		}else if(item == "sex") {
			datafield["type"] = "number";
			column["text"] = "Sex";
			
		}else if(item == "email") {
			datafield["type"] = "string";
			column["text"] = "Email";
			
		}else if(item == "address") {
			datafield["type"] = "string";
			column["text"] = "Address";
			
		}else if(item == "birthday") {
			datafield["type"] = "number";
			column["text"] = "Birthday";
			
		}else if(item == "description") {
			datafield["type"] = "string";
			column["text"] = "Description";
			
		}else if(item == "department") {
			datafield["type"] = "number";
			column["text"] = "Department";
			
		}else if(item == "sub_system") {
			datafield["type"] = "number";
			column["text"] = "Sub_system";
			
		}else if(item == "role_Details" || item == "image" || item == "image_absolute_path" || item == "enabled"){
			//do nothing
		}else {
			datafield["type"] = "string";
			column["text"] = "XX";
		}
		
		if(item == "role_Details" || item == "image" || item == "image_absolute_path" || item == "enabled"){
			
		}else {
			column["datafield"] = item;
			if(item == "id") {
				column["width"] = "50";
			}
			
			columns[j] = column;
			
			datafields[j] = datafield;
			j++;
		}
	}
	}
	
	var rowData = {};
	for(var item in user) {
	if(item == "role_Details" || item == "image" || item == "image_absolute_path" || item == "enabled"){
			
	}else {
		rowData[item] = user[item];
	}
	
	}
	
	userData[i] = rowData;
	}
	
	
	var theme = getDemoTheme();
	
	var source =
	{
	    localdata: userData,
	    datatype: "local",
	    datafields:datafields,
	    addrow: function (rowid, rowdata, position, commit) {
	        // synchronize with the server - send insert command
	        // call commit with parameter true if the synchronization with the server is successful 
	        //and with parameter false if the synchronization failed.
	        // you can pass additional argument to the commit callback which represents the new ID if it is generated from a DB.
	        commit(true);
	    },
	    deleterow: function (rowid, commit) {
	        // synchronize with the server - send delete command
	        // call commit with parameter true if the synchronization with the server is successful 
	        //and with parameter false if the synchronization failed.
	        commit(true);
	    },
	    updaterow: function (rowid, newdata, commit) {
	        // synchronize with the server - send update command
	        // call commit with parameter true if the synchronization with the server is successful 
	        // and with parameter false if the synchronization failed.
	        commit(true);
	    }
	};
	var dataAdapter = new $.jqx.dataAdapter(source);
	// initialize jqxGrid
	$("#userDataGrid").jqxGrid(
	{
	    width: 1000,
	    height: 350,
	    source: dataAdapter,
	    theme: theme,
	    selectionmode: 'multiplerowsextended',
	    sortable: true,
	    pageable: true,
	    autoheight: true,
	    columnsresize: true,
	  //  columnsreorder: true,
	    columns: columns
	});
	$("#addUserRowButton").jqxButton({ theme: theme });
	$("#deleteUserRowButton").jqxButton({ theme: theme });
	$("#updateUserRowButton").jqxButton({ theme: theme });
	$("#addUserTable #cancel").jqxButton({ theme: theme });
	$("#updateUserTable #cancel").jqxButton({ theme: theme });
	$("#saveUserButton").jqxButton({ theme: theme });
	$("#addUserButton").jqxButton({ theme: theme });
	// update row.
	$("#updateUserRowButton").on('click', function () {
		selectedupdaterowindex = $("#userDataGrid").jqxGrid('getselectedrowindex');
		//id = $("#userDataGrid").jqxGrid('getrowid', selectedrowindex);
	    rowData = $('#userDataGrid').jqxGrid('getrowdata', selectedupdaterowindex);
	    
		
		if(rowData != null) {
			var offset = $("#userDataGrid").offset();
			$("#updateUserPopupWindow").jqxWindow({ position: { x: parseInt(offset.left) + 60, y: parseInt(offset.top) + 60 } });
			
			//get old data in popupwindow
			$("#updateUserTable #id").val(rowData["id"]);
			$("#updateUserTable #account").val(rowData["account"]);
			$("#updateUserTable #name").val(rowData["name"]);
			$("#updateUserTable #password").val(rowData["password"]);
			$("#updateUserTable #address").val(rowData["address"]);
			$("#updateUserTable #birthday").val(rowData["birthday"]);
			$("#updateUserTable #department").val(rowData["department"]);
			$("#updateUserTable #email").val(rowData["email"]);
			$("#updateUserTable #user_image").attr("src","../../" + rowData["image_relative_path"]);
			
			$("#updateUserTable #image_relative_path").val(rowData["image_relative_path"]);
			$("#updateUserTable #image_size").val(rowData["image_size"]);
			$("#updateUserTable #phone").val(rowData["phone"]);
			$("#updateUserTable #sex").val(rowData["sex"]);
			
			// show the popup window.
			$("#updateUserPopupWindow").jqxWindow('open');
		}
	    
	    
	});
	// show the popup window
	$("#addUserRowButton").on('click', function () {
		var offset = $("#userDataGrid").offset();
			$("#addUserPopupWindow").jqxWindow({ position: { x: parseInt(offset.left) + 60, y: parseInt(offset.top) + 60 } });
			
			// show the popup window.
			$("#addUserPopupWindow").jqxWindow('open');
	});
	
	// create new row
	$("#addUserButton").on('click', function () {
	    if($("#addUserTable #account").val() != null && $("#addUserTable #password").val() != null){
	    	var newrow = {
	    		"account": $("#addUserTable #account").val(), 
	        	"name": $("#addUserTable #name").val(),
	            "password": $("#addUserTable #password").val(),
	           	"address": $("#addUserTable #address").val(),
	            "birthday": $("#addUserTable #birthday").val(),
	            "department": $("#addUserTable #department").val(),
	            "email": $("#addUserTable #email").val(),
	            "image_relative_path": $("#addUserTable #image_relative_path").val(),
	            "image_size": $("#addUserTable #image_size").val(),
	            "phone": $("#addUserTable #phone").val(),
	            "sex": $("#addUserTable #sex").val()
	        };
	        var addUserData = { 
	        	"user.account": $("#addUserTable #account").val(), 
	        	"user.name": $("#addUserTable #name").val(),
	            "user.password": $("#addUserTable #password").val(),
	           	"user.address": $("#addUserTable #address").val(),
	            "user.birthday": $("#addUserTable #birthday").val(),
	            "user.department": $("#addUserTable #department").val(),
	            "user.email": $("#addUserTable #email").val(),
	            "user.image_relative_path": $("#addUserTable #image_relative_path").val(),
	            "user.image_size": $("#addUserTable #image_size").val(),
	            "user.phone": $("#addUserTable #phone").val(),
	            "user.sex": $("#addUserTable #sex").val()
	        };
	        
	        $.post("registerUser.action", addUserData,function(result){
				if(result != null && result["id"] != null){
	            	//var rowID = $('#userDataGrid').jqxGrid('getrowid', rowData);
	            	newrow["id"] = result["id"];
	                $('#userDataGrid').jqxGrid('addrow', null, newrow);
	                $("#addUserPopupWindow").jqxWindow('hide');
	                
	                addUserData = null;
	                newrow = null;
	                
	                //empty input value
	                $("#addUserTable #account").val("");
	                $("#addUserTable #name").val("");
	                $("#addUserTable #password").val("");
	                $("#addUserTable #address").val("");
	                $("#addUserTable #birthday").val("");
	                $("#addUserTable #department").val("");
	                $("#addUserTable #email").val("");
	                $("#addUserTable #image_relative_path").val("");
	                $("#addUserTable #image_size").val("");
	                $("#addUserTable #phone").val("");
	                $("#addUserTable #sex").val("");
	                
	            }else if(result != null && result["executeResult"] != null && result["executeResult"] == false){
	            	//do nothing
	            }
			});
	    }
	});
	
	// delete row.
	$("#deleteUserRowButton").on('click', function () {
	    var selectedrowindex = $("#userDataGrid").jqxGrid('getselectedrowindex');
	    var rowscount = $("#userDataGrid").jqxGrid('getdatainformation').rowscount;
	    if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
	        //var id = $("#userDataGrid").jqxGrid('getrowid', selectedrowindex);
	        
	        var rowData = $('#userDataGrid').jqxGrid('getrowdata', selectedrowindex);
	        $.post("removeUser.action", {"user.id": rowData["id"]},function(result){
				if(result != null && result["executeResult"] != null && result["executeResult"] == true){
	            	var commit = $("#userDataGrid").jqxGrid('deleterow', selectedrowindex);
	            }
			});
	        
	    }
	});
	
	 // initialize the popup window and buttons.
	$("#addUserPopupWindow").jqxWindow({
	    width: 350, resizable: false, theme: theme, isModal: true, autoOpen: false, cancelButton: $("#addUserTable #cancel"), modalOpacity: 0.01           
	});
	$("#addUserPopupWindow").on('open', function () {
	    //$("#id").jqxInput('selectAll');
	});
	
	 // initialize the popup window and buttons.
	$("#updateUserPopupWindow").jqxWindow({
	    width: 350, resizable: false, theme: theme, isModal: true, autoOpen: false, cancelButton: $("#updateUserTable #cancel"), modalOpacity: 0.01           
	});
	$("#updateUserPopupWindow").on('open', function () {
	    $("#id").jqxInput('selectAll');
	});
	
	// update the edited row when the user clicks the 'Save' button.
	$("#saveUserButton").click(function () {
	    if (rowData != null && rowData["id"] != null) {
			
	        var newrow = { 
	        	"id": $("#updateUserTable #id").val(), 
	        	"account": $("#updateUserTable #account").val(), 
	        	"name": $("#updateUserTable #name").val(),
	            "password": $("#updateUserTable #password").val(),
	           	"address": $("#updateUserTable #address").val(),
	            "birthday": $("#updateUserTable #birthday").val(),
	            "department": $("#updateUserTable #department").val(),
	            "email": $("#updateUserTable #email").val(),
	            "image_relative_path": $("#updateUserTable #image_relative_path").val(),
	            "image_size": $("#updateUserTable #image_size").val(),
	            "phone": $("#updateUserTable #phone").val(),
	            "sex": $("#updateUserTable #sex").val()
	        };
	        var updateUserData = { 
	        	"user.id": $("#updateUserTable #id").val(), 
	        	"user.account": $("#updateUserTable #account").val(), 
	        	"user.name": $("#updateUserTable #name").val(),
	            "user.password": $("#updateUserTable #password").val(),
	           	"user.address": $("#updateUserTable #address").val(),
	            "user.birthday": $("#updateUserTable #birthday").val(),
	            "user.department": $("#updateUserTable #department").val(),
	            "user.email": $("#updateUserTable #email").val(),
	            "user.image_relative_path": $("#updateUserTable #image_relative_path").val(),
	            "user.image_size": $("#updateUserTable #image_size").val(),
	            "user.phone": $("#updateUserTable #phone").val(),
	            "user.sex": $("#updateUserTable #sex").val()
	        };
	        
	        $.post("updateUser.action", updateUserData,function(result){
				if(result != null && result["id"] != null){
	            	//var rowID = $('#userDataGrid').jqxGrid('getrowid', rowData);
	                $('#userDataGrid').jqxGrid('updaterow', selectedupdaterowindex, newrow);
	                $("#updateUserPopupWindow").jqxWindow('hide');
	            }else if(result != null && result["executeResult"] != null && result["executeResult"] == false){
	            	//do nothing
	            }
			});
	         
	        
	    }
	});
	/*
	 $("#userDataGrid").on('columnreordered', function (event) {
	    var column = event.args.columntext;
	    var newindex = event.args.newindex
	    var oldindex = event.args.oldindex;
	});
	*/
	});
}



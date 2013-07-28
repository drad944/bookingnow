/**
 * 
 */
var AppUtil = {
		
	setStyle : function(domObj, parameters){
		 for(var p in parameters){ 
			 domObj.style[p] = parameters[p];
		 }
	}
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



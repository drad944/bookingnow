/**
 * 
 */

function emptyRegisterTableWindow(){
	//init registerTableWindow widget data
	$("#registerTableStatusCombobox").jqxComboBox({ selectedIndex: 0});
	$("#registerTableMinCustomerCountInput").jqxNumberInput('val',0);
	$("#registerTableMaxCustomerCountInput").jqxNumberInput('val',0);
	$("#registerTableIndoorPriceInput").jqxNumberInput('val',0);
	
	//init registerTableWindow store data
	$("#registerTableAddressInput").val(null);
	$("#registerTableMinCustomerCountInput").val(null);
	$("#registerTableMaxCustomerCountInput").val(null);
	$("#registerTableIndoorPriceInput").val(null);
	$("#registerTableStatusInput").val(null);
	$("#registerTableResult").text("");
}

function emptyUpdateTableWindow(){
	$("#updateTableStatusCombobox").jqxComboBox({ selectedIndex: 0});
	$("#registerTableMinCustomerCountInput").jqxNumberInput('val',0);
	$("#registerTableMaxCustomerCountInput").jqxNumberInput('val',0);
	$("#registerTableIndoorPriceInput").jqxNumberInput('val',0);
	
	$("#updateTableAddressInput").val(null);
	$("#updateTableMinCustomerCountInput").val(null);
	$("#updateTableMaxCustomerCountInput").val(null);
	$("#updateTableIndoorPriceInput").val(null);
	$("#updateTableStatusInput").val(null);
	$("#updateTableResult").text("");
}

function initUpdateTableElements() {
	
    $("#updateTableDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
    $('#updateTableUpdateButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#updateTableResetButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#updateTableCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
    
    $('.updateTableTextInput').jqxInput({ theme: theme });
    
	$('#updateTableAddressInput').jqxInput({width: 120, height: 20, theme: theme });
    $('#updateTableMinCustomerCountInput').jqxNumberInput({ width: 120, height: 20,min: 1, max: 20, decimalDigits:0, digits: 2, theme: theme, spinButtons: true});
    $('#updateTableMaxCustomerCountInput').jqxNumberInput({ width: 120, height: 20,min: 1, max: 20, decimalDigits:0, digits: 2, theme: theme, spinButtons: true});
    $('#updateTableIndoorPriceInput').jqxNumberInput({ width: 120, height: 20, digits: 3,symbolPosition: 'right', symbol: '￥', theme: theme, spinButtons: true});
    var tableStatusData = [
                                { value: 2, label: i18n.t("status.EMPTY") },
                                { value: 3, label: i18n.t("status.BOOKING") },
                                { value: 4, label: i18n.t("status.USING") }
                             ];
     
 	$("#updateTableStatusCombobox").jqxComboBox({ 
 		selectedIndex: 0, 
 		source: tableStatusData, 
 		displayMember: "label", 
 		valueMember: "value", 
 		width: 120, 
 		height: 20, 
 		theme: theme 
 	});
    
    
    
    // initialize validator.
    $('#updateTableInfoForm').jqxValidator({
     rules: [
             
             /*
            {input: '#updateTableMaxCustomerCountInput', message: 'Max Customer Count should large than 0', action: 'keyup, focus', rule: function (input, commit) {
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
	                if (0 < $('#updateTableMaxCustomerCountInput').val()) {
	                    return true;
	                }
	                return false;
	        	}
            },
            {input: '#updateTableMaxCustomerCountInput', message: 'Max Customer Count should large than minCustomerCount', action: 'keyup, focus', rule: function (input, commit) {
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
	                if ($('#updateTableMinCustomerCountInput').val() < $('#updateTableMaxCustomerCountInput').val()) {
	                    return true;
	                }
	                return false;
	        	}
            },
            */
            { input: '#updateTableAddressInput', message: 'address is required!', action: 'keyup, blur', rule: 'required' }
            
            /*,
            { input: '#updateTableMinCustomerCountInput', message: 'Min Customer Count should large than 0', action: 'keyup, focus', rule: function (input, commit) {
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
                    if (0 < $('#updateTableMinCustomerCountInput').val()) {
                        return true;
                    }
                    return false;
            	}
            }*/
            ], 
            theme: theme
    });
	
};

function formatUpdateTableElements(rowData) {
    $("#updateTableIdInput").val(rowData["id"]);
    $("#updateTableAddressInput").val(rowData["address"]);
    $("#updateTableMinCustomerCountInput").val(rowData["minCustomerCount"]);
	$("#updateTableMaxCustomerCountInput").val(rowData["maxCustomerCount"]);
	$("#updateTableIndoorPriceInput").val(rowData["indoorPrice"]);
	$("#updateTableStatusInput").val(rowData["status"]);
	
	$("#updateTableMinCustomerCountInput").jqxNumberInput('val',rowData["minCustomerCount"]);
	$("#updateTableMaxCustomerCountInput").jqxNumberInput('val',rowData["maxCustomerCount"]);
	$("#updateTableIndoorPriceInput").jqxNumberInput('val',rowData["indoorPrice"]);
	$("#updateTableStatusCombobox").jqxComboBox({ selectedIndex: findTableStatusValue($("#updateTableStatusInput").val()) - 2});
}

function addUpdateTableEventListeners() {
	$('#updateTablePopupWindow').on('close', function (event) { 
		emptyUpdateTableWindow();
		$('#updateTableInfoForm').jqxValidator('hide');
      //  $('#updateTablePopupWindow').jqxWindow('close');
	});
	$('#updateTableUpdateButton').on('click', function () {
        $('#updateTableInfoForm').jqxValidator('validate');
    });
    
    
    $("#updateTableCancelButton").on('click', function (event) {
    	$('#updateTableInfoForm').jqxValidator('hide');
        $('#updateTablePopupWindow').jqxWindow('close');
    });
        
    $("#updateTableStatusCombobox").on('select', function (event) {
            if (event.args) {
                var item = event.args.item;
                if (item) {
                	$("#updateTableStatusInput").val(item.value);
                }
            }
        });
    
    $('#updateTableInfoForm').on('validationSuccess', function (event) { 
    	// Some code here. 
    	updateTable();
    });
}

function initUpdateTableWindow(rowData,position) {
	$("#updateTablePopupWindow").removeAttr("style");
	
	formatUpdateTableElements(rowData);
	
	// initialize the popup window and buttons.
    $("#updateTablePopupWindow").jqxWindow({
    	position:position, isModal: true,width: 350, height: 250, resizable: false, theme: theme, cancelButton: $("#updateTableCancelButton"), modalOpacity: 0.01,
    	initContent: function () {
            $('#updateTablePopupWindow').jqxWindow('focus');
        }
    });
    
    $("#updateTablePopupWindow").jqxWindow('open');
}

function updateTable() {

	var updateTableUIData = {
		"table.id" : $("#updateTableIdInput").val(),
		"table.address" : $("#updateTableAddressInput").val(),
		"table.minCustomerCount" : $('#updateTableMinCustomerCountInput').val(),
		"table.maxCustomerCount" : $('#updateTableMaxCustomerCountInput').val(),
		"table.status" : $("#updateTableStatusInput").val(),
		"table.indoorPrice" : $("#updateTableIndoorPriceInput").val()
	};
	
	var updateTableData = parseUIDataToTableData(updateTableUIData);

	$.post("updateTable.action", updateTableData, function(result) {
		
		if (result != null && result["id"] != null) {
			
			$("#updateTableResult").text(i18n.t("result.updateSuccess"));
			$("#updateTablePopupWindow").jqxWindow('close');
			
			var tableUIResult = parseTableDataToUIData(result);
			
			//update row in grid
			var selectedrowindex = $("#tableDataGrid").jqxGrid('getselectedrowindex');
            var rowscount = $("#tableDataGrid").jqxGrid('getdatainformation').rowscount;
            if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
            	var id = $("#tableDataGrid").jqxGrid('getrowid', selectedrowindex);
                var commit = $("#tableDataGrid").jqxGrid('updaterow', id, tableUIResult);
                $("#tableDataGrid").jqxGrid('ensurerowvisible', selectedrowindex);
            }
			
			$("#eventLog").text(i18n.t("result.updateSuccess"));

		} else if (result != null && result["executeResult"] != null
				&& result["executeResult"] == false) {
			$("#updateTableResult").text(i18n.t("result.updateFail"));
		}
	});
}



function initRegisterTableElements() {
    
    $("#registerTableDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
    $('#registerTableRegisterButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#registerTableResetButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#registerTableCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
    
    $('.registerTableTextInput').jqxInput({width: 120, height: 20, theme: theme });
    
	$('#registerTableAddressInput').jqxInput({width: 120, height: 20, theme: theme });
    $('#registerTableMinCustomerCountInput').jqxNumberInput({ width: 120, height: 20,min: 1, max: 20, decimalDigits:0, digits: 2, theme: theme, spinButtons: true});
    $('#registerTableMaxCustomerCountInput').jqxNumberInput({ width: 120, height: 20,min: 1, max: 20, decimalDigits:0, digits: 2, theme: theme, spinButtons: true});
    $('#registerTableIndoorPriceInput').jqxNumberInput({ width: 120, height: 20, digits: 3,symbolPosition: 'right', symbol: '￥', theme: theme, spinButtons: true});
    var tableStatusData = [
                                { value: 2, label: i18n.t("status.EMPTY") },
                                { value: 3, label: i18n.t("status.BOOKING") },
                                { value: 4, label: i18n.t("status.USING") }
                             ];
    
    
	// Create a jqxComboBox
	$("#registerTableStatusCombobox").jqxComboBox({ 
		selectedIndex: 0, 
		source: tableStatusData, 
		displayMember: "label", 
		valueMember: "value", 
		width: 120, 
		height: 20, 
		theme: theme 
	});
    
    // initialize validator.
    $('#registerTableInfoForm').jqxValidator({
     rules: [
            { input: '#registerTableAddressInput', message: 'address is required!', action: 'keyup, blur', rule: 'required' }
            ], 
            theme: theme
    });
    
	
};

function addRegisterTableEventListeners() {
	$('#addTablePopupWindow').on('close', function (event) { 
		emptyRegisterTableWindow();
		$('#registerTableInfoForm').jqxValidator('hide');
      //  $('#updateTablePopupWindow').jqxWindow('close');
	});
	
	$('#registerTableRegisterButton').on('click', function () {
        $('#registerTableInfoForm').jqxValidator('validate');
    });
    
    
    $("#registerTableCancelButton").on('click', function (event) {
        $('#addTablePopupWindow').jqxWindow('close');
        
    });
        
    $("#registerTableStatusCombobox").on('select', function (event) {
            if (event.args) {
                var item = event.args.item;
                if (item) {
                	$("#registerTableStatusInput").val(item.value);
                }
            }
        });
    
    $('#registerTableInfoForm').on('validationSuccess', function (event) { 
    	// Some code here. 
    	registerTable();
    });
}
function initRegisterTableWindow(position) {
	emptyRegisterTableWindow();
	$("#addTablePopupWindow").removeAttr("style");
	$("#addTablePopupWindow").attr("style","overflow:hidden");
	
	$("#addTablePopupWindow").jqxWindow({
		position:position,isModal: true,width: 350, height: 250, resizable: false, theme: theme, cancelButton: $("#registerTableCancelButton"), 
    	modalOpacity: 0.01,
    	
    	initContent: function () {
            $('#addTablePopupWindow').jqxWindow('focus');
        }
        
    });
    $("#addTablePopupWindow").jqxWindow('open');
}


function registerTable() {
	var registerTableUIData = {
		"table.id" : $("#registerTableIdInput").val(),
		"table.address" : $("#registerTableAddressInput").val(),
		"table.minCustomerCount" : $('#registerTableMinCustomerCountInput').val(),
		"table.maxCustomerCount" : $('#registerTableMaxCustomerCountInput').val(),
		"table.status" : $("#registerTableStatusInput").val(),
		"table.indoorPrice" : $("#registerTableIndoorPriceInput").val()
	};
	
	var registerTableData = parseUIDataToTableData(registerTableUIData);
	
	
	$.post("addTable.action", registerTableData, function(result) {
		
		if (result != null && result["id"] != null) {
			result = parseTableDataToUIData(result);
			
			$("#registerTableResult").text(i18n.t("result.insertSuccess"));
			$('#addTablePopupWindow').jqxWindow('close');
			
			var commit = $("#tableDataGrid").jqxGrid('addrow', null, result);
			
			$("#eventLog").text(i18n.t("result.insertSuccess"));
		} else if (result != null && result["executeResult"] != null
				&& result["executeResult"] == false) {
			$("#registerTableResult").text(i18n.t("result.insertFail"));
		}
	});
}



function initOperateTableGridElements() {
	$("#addTableRowButton").jqxButton({ theme: theme });
	$("#deleteTableRowButton").jqxButton({ theme: theme });
	$("#updateTableRowButton").jqxButton({ theme: theme });
}	

function addOperateTableGridEventListeners() {
	// update row.
	$("#updateTableRowButton").on('click', function () {
		selectedupdaterowindex = $("#tableDataGrid").jqxGrid('getselectedrowindex');
		if(selectedupdaterowindex != -1) {
			rowData = $('#tableDataGrid').jqxGrid('getrowdata', selectedupdaterowindex);
		    
			
			if(rowData != null) {
				var offset = $("#tableDataGrid").offset();
				var position = {};
				position.x = parseInt(offset.left) + 200;
				position.y = parseInt(offset.top) - 150;
				
				initUpdateTableWindow(rowData,position);
			}
		}else {
			var option = {
					fallbackLng: 'en-US',
					lng: 'en-US',
			//		lng: 'zh-CN',
					resGetPath: 'resources/locales/__lng__/__ns__.json',
					getAsync: false,
					ns: 'bookingnow.content.tableManagement'
				};
			 
			i18n.init(option);
			$("#eventLog").text(i18n.t("message.requireSelectOneRow"));
		}
	    
	});
    
	// show the popup window
	$("#addTableRowButton").on('click', function () {
	//	openContentPage('addTablePopupWindowDiv','page/common/addTablePopupWindow.html','content');
		
		var offset = $("#tableDataGrid").offset();
		var position = {};
		position.x = parseInt(offset.left) + 200;
		position.y = parseInt(offset.top) - 150;
		
		
		// show the popup window.
		initRegisterTableWindow(position);
		
			
	});
	
	// delete row.
	$("#deleteTableRowButton").on('click', function () {
	    var selectedrowindex = $("#tableDataGrid").jqxGrid('getselectedrowindex');
	    var rowscount = $("#tableDataGrid").jqxGrid('getdatainformation').rowscount;
	    if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
	        //var id = $("#tableDataGrid").jqxGrid('getrowid', selectedrowindex);
	        
	        var rowData = $('#tableDataGrid').jqxGrid('getrowdata', selectedrowindex);
	        $.post("removeTable.action", {"table.id": rowData["id"]},function(result){
				if(result != null && result["executeResult"] != null && result["executeResult"] == true){
					var id = $("#tableDataGrid").jqxGrid('getrowid', selectedrowindex);
                    var commit = $("#tableDataGrid").jqxGrid('deleterow', id);
	            	
	            	if(commit != null) {
	            		
	            	}
	            }
			});
	        
	    }
	});
}


function parseTableGridHtml() {
		$.post("searchTable.action", 
			{"table.enabled": true}, 
			function(matchedtables){
				var option = {
						fallbackLng: 'en-US',
						lng: 'en-US',
				//		lng: 'zh-CN',
						resGetPath: 'resources/locales/__lng__/__ns__.json',
						getAsync: false,
						ns: 'bookingnow.content.tableManagement',
						load:'current'
					};
				 
				i18n.init(option);
				
				if(matchedtables != null && matchedtables.result != null){
					tables = matchedtables.result;
				}
				
				var tableSize = tables.length;
				var tableData = {};
				
				var columns = {};
				
				var datafields = {};
				for(var i = 0;i < tableSize; i++) {
					table = parseTableDataToUIData(tables[i]);
					if(i==0) {
						var j=0;
						for(var item in table) {
							var datafield = {};
							var column = {};
							
							if(item == "id"){
								datafield["type"] = "number";
								column["text"] = i18n.t("field.id");
								column["filtertype"] = 'number';
							}else if(item == "status") {
								datafield["type"] = "string";
								column["text"] = i18n.t("field.status");
								column["filtertype"] = 'textbox';
							}else if(item == "minCustomerCount") {
								datafield["type"] = "number";
								column["text"] = i18n.t("field.minCustomerCount");
								column["filtertype"] = 'number';
							}else if(item == "maxCustomerCount") {
								datafield["type"] = "number";
								column["text"] = i18n.t("field.maxCustomerCount");
								column["filtertype"] = 'number';
							}else if(item == "address") {
								datafield["type"] = "number";
								column["text"] = i18n.t("field.address");
								column["filtertype"] = 'textbox';
							}else if(item == "indoorPrice") {
								datafield["type"] = "number";
								column["text"] = i18n.t("field.indoorPrice");
								column["filtertype"] = 'number';
							}else if(item == "enabled"){
								//do nothing
							}else {
								datafield["type"] = "string";
								column["text"] = i18n.t("field.xx");
							}
							
							if(item == "enabled"){
								
							}else {
								column["datafield"] = item;
								
								if(item == "id"){
									column["width"] = "130";
								}else if(item == "status") {
									column["width"] = "140";
								}else if(item == "minCustomerCount") {
									column["width"] = "140";
								}else if(item == "maxCustomerCount") {
									column["width"] = "130";
								}else if(item == "address") {
									column["width"] = "130";
								}else if(item == "indoorPrice") {
									column["width"] = "130";
								}else if(item == "enabled"){
									//do nothing
								}else {
									column["width"] = "130";
								}
								
								columns[j] = column;
								
								datafields[j] = datafield;
								j++;
							}
						}
					}
					
					var rowData = {};
					for(var item in table) {
						if(item == "enabled"){
								
						}else {
							rowData[item] = table[item];
						}
					
					}
					
					tableData[i] = rowData;
				}
	
	
	var source =
	{
	    localdata: tableData,
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
	$("#tableDataGrid").jqxGrid(
	{
	    width: 800,
	    height: 350,
	    source: dataAdapter,
	    theme: theme,
	    selectionmode: 'multiplerowsextended',
	    sortable: true,
	    showfilterrow: true,
        filterable: true,
	    pageable: true,
	    autoheight: true,
	    selectionmode:'singlerow',
	    columnsresize: true,
	  //  columnsreorder: true,
	    columns: columns
	});
	
	// display selected row index.
    $("#tableDataGrid").on('rowselect', function (event) {
        $("#eventLog").text(i18n.t("grid.selectRow", {index: event.args.rowindex}));
    });
    initTableManagementLocaleElements();
	initOperateTableGridElements();
	addOperateTableGridEventListeners();
	
	initUpdateTableElements();
	addUpdateTableEventListeners();
	
	// initialize the popup window and buttons.
	initRegisterTableElements();
	addRegisterTableEventListeners();
	
	/*
	 $("#tableDataGrid").on('columnreordered', function (event) {
	    var column = event.args.columntext;
	    var newindex = event.args.newindex
	    var oldindex = event.args.oldindex;
	});
	*/
	});
		
}

function initTableManagementLocaleElements() {
	$("#addTableRowButton").val(i18n.t("button.operationTableGrid.addTableRow"));
	$("#updateTableRowButton").val(i18n.t("button.operationTableGrid.updateTableRow"));
	$("#deleteTableRowButton").val(i18n.t("button.operationTableGrid.deleteTableRow"));
	$("#updateTableUpdateButton").val(i18n.t("button.update"));
	$("#updateTableResetButton").val(i18n.t("button.reset"));
	$("#updateTableCancelButton").val(i18n.t("button.cancel"));
	$("#registerTableRegisterButton").val(i18n.t("button.register"));
	$("#registerTableResetButton").val(i18n.t("button.reset"));
	$("#registerTableCancelButton").val(i18n.t("button.cancel"));
	
	$("#updateTablePopupWindow").i18n();
	$("#addTablePopupWindow").i18n();
}

function parseUIDataToTableData(record) {
	if(record != null){
		for(var attr in record) {
			if(attr == "table.status") {
				record[attr] = findTableStatusValue(record[attr]);
			}
		}
		return record;
	}
	return null;
}

function parseTableDataToUIData(table) {
	if(table != null){
		for(var attr in table) {
			if(attr == "status") {
				table[attr] = findTableStatusLable(table[attr]);
			}
		}
		return table;
	}
	return null;
}
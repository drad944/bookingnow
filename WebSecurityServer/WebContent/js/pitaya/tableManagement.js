var tableManagement = {
		
		leave : function (){
			$("#updateTablePopupWindow").unbind('close');
			$("#updateTableUpdateButton").unbind('click');
			$("#updateTableCancelButton").unbind('click');
			$("#updateTableStatusCombobox").unbind('select');
			$("#updateTableInfoForm").unbind('validationSuccess');
			
			$("#addTablePopupWindow").unbind('close');
			$("#registerTableRegisterButton").unbind('click');
			$("#registerTableCancelButton").unbind('click');
			$("#registerTableStatusCombobox").unbind('select');
			$("#registerTableInfoForm").unbind('validationSuccess');
			
			$("#updateTableRowButton").unbind('click');
			$("#addTableRowButton").unbind('click');
			$("#deleteTableRowButton").unbind('click');
			$("#tableDataGrid").unbind('rowselect');
		},

		visit : function (){
			
			this.init();
		},
		init : function(){
			var me = this;
			me.parseTableGridHtml();
		},
		emptyRegisterTableWindow:function (){
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
		},

		emptyUpdateTableWindow:function (){
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
		},

		initUpdateTableElements:function () {
			
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
		                                { value: 2, label: i18n.t("tableManagement.status.EMPTY") },
		                                { value: 3, label: i18n.t("tableManagement.status.BOOKING") },
		                                { value: 4, label: i18n.t("tableManagement.status.USING") }
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
		            { input: '#updateTableAddressInput', message: 'address is required!', action: 'keyup, blur', rule: 'required' }
		            ], 
		            theme: theme
		    });
		    
		    
			
		},

		formatUpdateTableElements:function (rowData) {
		    $("#updateTableIdInput").val(rowData["id"]);
		    $("#updateTableAddressInput").val(rowData["address"]);
		    $("#updateTableMinCustomerCountInput").val(rowData["minCustomerCount"]);
			$("#updateTableMaxCustomerCountInput").val(rowData["maxCustomerCount"]);
			$("#updateTableIndoorPriceInput").val(rowData["indoorPrice"]);
			$("#updateTableStatusInput").val(rowData["status"]);
			
			$("#updateTableMinCustomerCountInput").jqxNumberInput('val',rowData["minCustomerCount"]);
			$("#updateTableMaxCustomerCountInput").jqxNumberInput('val',rowData["maxCustomerCount"]);
			$("#updateTableIndoorPriceInput").jqxNumberInput('val',rowData["indoorPrice"]);
			$("#updateTableStatusCombobox").jqxComboBox({ selectedIndex: AppUtil.findTableStatusValue($("#updateTableStatusInput").val()) - 2});
		},

		addUpdateTableEventListeners:function () {
			var me = this;
			$('#updateTablePopupWindow').bind('close', function (event) { 
				me.emptyUpdateTableWindow();
				$('#updateTableInfoForm').jqxValidator('hide');
		      //  $('#updateTablePopupWindow').jqxWindow('close');
			});
			$('#updateTableUpdateButton').bind('click', function () {
		        $('#updateTableInfoForm').jqxValidator('validate');
		    });
		    
		    
		    $("#updateTableCancelButton").bind('click', function (event) {
		    	$('#updateTableInfoForm').jqxValidator('hide');
		        $('#updateTablePopupWindow').jqxWindow('close');
		    });
		        
		    $("#updateTableStatusCombobox").bind('select', function (event) {
		            if (event.args) {
		                var item = event.args.item;
		                if (item) {
		                	$("#updateTableStatusInput").val(item.value);
		                }
		            }
		        });
		    
		    $('#updateTableInfoForm').bind('validationSuccess', function (event) { 
		    	// Some code here. 
		    	me.updateTable();
		    });
		},

		initUpdateTableWindow:function (rowData,position) {
			var me = this;
			$("#updateTablePopupWindow").removeAttr("style");
			
			me.formatUpdateTableElements(rowData);
			
			// initialize the popup window and buttons.
			
		    $("#updateTablePopupWindow").jqxWindow({
		    	position:position, isModal: true,width: 280, height: 250, resizable: false, theme: theme, cancelButton: $("#updateTableCancelButton"), modalOpacity: 0.01,
		    	initContent: function () {
		            $('#updateTablePopupWindow').jqxWindow('focus');
		        }
		    });
		    
		    $("#updateTablePopupWindow").jqxWindow('open');
		},

		updateTable:function () {
			var me = this;

			var updateTableUIData = {
				"table.id" : $("#updateTableIdInput").val(),
				"table.address" : $("#updateTableAddressInput").val(),
				"table.minCustomerCount" : $('#updateTableMinCustomerCountInput').val(),
				"table.maxCustomerCount" : $('#updateTableMaxCustomerCountInput').val(),
				"table.status" : $("#updateTableStatusInput").val(),
				"table.indoorPrice" : $("#updateTableIndoorPriceInput").val()
			};
			
			var updateTableData = me.parseUIDataToTableData(updateTableUIData);

			$.post("updateTable.action", updateTableData, function(result) {
				
				if (result != null && result["id"] != null) {
					
					$("#updateTableResult").text(i18n.t("tableManagement.result.updateSuccess"));
					$("#updateTablePopupWindow").jqxWindow('close');
					
					var tableUIResult = me.parseTableDataToUIData(result);
					
					//update row in grid
					var selectedrowindex = $("#tableDataGrid").jqxGrid('getselectedrowindex');
		            var rowscount = $("#tableDataGrid").jqxGrid('getdatainformation').rowscount;
		            if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
		            	var id = $("#tableDataGrid").jqxGrid('getrowid', selectedrowindex);
		                var commit = $("#tableDataGrid").jqxGrid('updaterow', id, tableUIResult);
		                $("#tableDataGrid").jqxGrid('ensurerowvisible', selectedrowindex);
		            }
					
					$("#eventLog").text(i18n.t("tableManagement.result.updateSuccess"));

				} else if (result != null && result["executeResult"] != null
						&& result["executeResult"] == false) {
					$("#updateTableResult").text(i18n.t("tableManagement.result.updateFail"));
				}
			});
		},

		initRegisterTableElements:function () {
		    
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
		                                { value: 2, label: i18n.t("tableManagement.status.EMPTY") },
		                                { value: 3, label: i18n.t("tableManagement.status.BOOKING") },
		                                { value: 4, label: i18n.t("tableManagement.status.USING") }
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
		    
			
		},

		addRegisterTableEventListeners:function () {
			var me = this;
			$('#addTablePopupWindow').bind('close', function (event) { 
				me.emptyRegisterTableWindow();
				$('#registerTableInfoForm').jqxValidator('hide');
		      //  $('#updateTablePopupWindow').jqxWindow('close');
			});
			
			$('#registerTableRegisterButton').bind('click', function () {
		        $('#registerTableInfoForm').jqxValidator('validate');
		    });
		    
		    
		    $("#registerTableCancelButton").bind('click', function (event) {
		        $('#addTablePopupWindow').jqxWindow('close');
		        
		    });
		        
		    $("#registerTableStatusCombobox").bind('select', function (event) {
		            if (event.args) {
		                var item = event.args.item;
		                if (item) {
		                	$("#registerTableStatusInput").val(item.value);
		                }
		            }
		        });
		    
		    $('#registerTableInfoForm').bind('validationSuccess', function (event) { 
		    	// Some code here. 
		    	me.registerTable();
		    });
		},
		
		initRegisterTableWindow: function (position) {
			var me = this;
			me.emptyRegisterTableWindow();
			$("#addTablePopupWindow").removeAttr("style");
			$("#addTablePopupWindow").attr("style","overflow:hidden");
			
			$("#addTablePopupWindow").jqxWindow({
				position:position,isModal: true,width: 280, height: 250, resizable: false, theme: theme, cancelButton: $("#registerTableCancelButton"), 
		    	modalOpacity: 0.01,
		    	
		    	initContent: function () {
		            $('#addTablePopupWindow').jqxWindow('focus');
		        }
		        
		    });
		    $("#addTablePopupWindow").jqxWindow('open');
		},


		registerTable:function () {
			var me = this;
			var registerTableUIData = {
				"table.id" : $("#registerTableIdInput").val(),
				"table.address" : $("#registerTableAddressInput").val(),
				"table.minCustomerCount" : $('#registerTableMinCustomerCountInput').val(),
				"table.maxCustomerCount" : $('#registerTableMaxCustomerCountInput').val(),
				"table.status" : $("#registerTableStatusInput").val(),
				"table.indoorPrice" : $("#registerTableIndoorPriceInput").val()
			};
			
			var registerTableData = me.parseUIDataToTableData(registerTableUIData);
			
			
			$.post("addTable.action", registerTableData, function(result) {
				
				if (result != null && result["id"] != null) {
					result = me.parseTableDataToUIData(result);
					
					$("#registerTableResult").text(i18n.t("tableManagement.result.insertSuccess"));
					$('#addTablePopupWindow').jqxWindow('close');
					
					var commit = $("#tableDataGrid").jqxGrid('addrow', null, result);
					
					$("#eventLog").text(i18n.t("tableManagement.result.insertSuccess"));
				} else if (result != null && result["executeResult"] != null
						&& result["executeResult"] == false) {
					$("#registerTableResult").text(i18n.t("tableManagement.result.insertFail"));
				}
			});
		},

		initOperateTableGridElements:function () {
			$("#addTableRowButton").jqxButton({ height: 25,theme: theme });
			$("#deleteTableRowButton").jqxButton({ height: 25,theme: theme });
			$("#updateTableRowButton").jqxButton({ height: 25,theme: theme });
		},	

		addOperateTableGridEventListeners:function () {
			var me = this;
			// update row.
			$("#updateTableRowButton").bind('click', function () {
				selectedupdaterowindex = $("#tableDataGrid").jqxGrid('getselectedrowindex');
				if(selectedupdaterowindex != -1) {
					rowData = $('#tableDataGrid').jqxGrid('getrowdata', selectedupdaterowindex);
				    
					
					if(rowData != null) {
						var offset = $("#tableDataGrid").offset();
						var position = {};
						position.x = parseInt(offset.left) + 200;
						position.y = parseInt(offset.top) - 120;
						
						me.initUpdateTableWindow(rowData,position);
					}
				}else {
					$("#eventLog").text(i18n.t("tableManagement.message.requireSelectOneRow"));
				}
			    
			});
		    
			// show the popup window
			$("#addTableRowButton").bind('click', function () {
			//	openContentPage('addTablePopupWindowDiv','page/common/addTablePopupWindow.html','content');
				
				var offset = $("#tableDataGrid").offset();
				var position = {};
				position.x = parseInt(offset.left) + 200;
				position.y = parseInt(offset.top) - 120;
				
				
				// show the popup window.
				me.initRegisterTableWindow(position);
				
					
			});
			
			$("#tableDataGrid").bind('rowselect', function (event) {
		        $("#eventLog").text(i18n.t("tableManagement.grid.selectRow", {index: event.args.rowindex}));
		    });
			
			// delete row.
			$("#deleteTableRowButton").bind('click', function () {
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
		},


		parseTableGridHtml:function () {
			var me = this;
				$.post("searchTable.action", 
					{"table.enabled": true}, 
					function(matchedtables){
						
						var option = {
								lng: 'zh',
								fallbackLng: 'zh',
						//		fallbackLng: 'en-US',
						//		lng: 'en-US',
								resGetPath: 'resources/locales/__lng__/__ns__.json',
						//		resPostPath: 'resources/locales/__lng__/__ns__.json',
								getAsync: false,
						//		postAsync: false,
								ns: 'bookingnow.view',
								fallbackToDefaultNS: true,
								load:'current',
								useCookie: false
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
							table = me.parseTableDataToUIData(tables[i]);
							if(i==0) {
								var j=0;
								for(var item in table) {
									var datafield = {};
									var column = {};
									
									if(item == "id" || item == "enabled"){
										
									}else if(item == "status") {
										datafield["type"] = "string";
										column["text"] = i18n.t("tableManagement.field.status");
										column["filtertype"] = 'textbox';
									}else if(item == "minCustomerCount") {
										datafield["type"] = "number";
										column["text"] = i18n.t("tableManagement.field.minCustomerCount");
										column["filtertype"] = 'number';
									}else if(item == "maxCustomerCount") {
										datafield["type"] = "number";
										column["text"] = i18n.t("tableManagement.field.maxCustomerCount");
										column["filtertype"] = 'number';
									}else if(item == "address") {
										datafield["type"] = "number";
										column["text"] = i18n.t("tableManagement.field.address");
										column["filtertype"] = 'textbox';
									}else if(item == "indoorPrice") {
										datafield["type"] = "number";
										column["text"] = i18n.t("tableManagement.field.indoorPrice");
										column["filtertype"] = 'number';
									}else {
										datafield["type"] = "string";
										column["text"] = i18n.t("tableManagement.field.xx");
									}
									
									if(item == "id" || item == "enabled"){
										
									}else {
										column["datafield"] = item;
										
										if(item == "status") {
											column["width"] = "160";
										}else if(item == "minCustomerCount") {
											column["width"] = "160";
										}else if(item == "maxCustomerCount") {
											column["width"] = "160";
										}else if(item == "address") {
											column["width"] = "160";
										}else if(item == "indoorPrice") {
											column["width"] = "160";
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
								if(item == "enabled" ){
										
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
		    
		    me.initTableManagementLocaleElements();
		    me.initOperateTableGridElements();
		    me.addOperateTableGridEventListeners();
			
		    me.initUpdateTableElements();
		    me.addUpdateTableEventListeners();
			
			// initialize the popup window and buttons.
		    me.initRegisterTableElements();
		    me.addRegisterTableEventListeners();
			
			/*
			 $("#tableDataGrid").on('columnreordered', function (event) {
			    var column = event.args.columntext;
			    var newindex = event.args.newindex
			    var oldindex = event.args.oldindex;
			});
			*/
			});
				
		},

		initTableManagementLocaleElements:function () {
			$("#addTableRowButton").val(i18n.t("tableManagement.button.operationTableGrid.addTableRow"));
			$("#updateTableRowButton").val(i18n.t("tableManagement.button.operationTableGrid.updateTableRow"));
			$("#deleteTableRowButton").val(i18n.t("tableManagement.button.operationTableGrid.deleteTableRow"));
			$("#updateTableUpdateButton").val(i18n.t("tableManagement.button.update"));
			$("#updateTableResetButton").val(i18n.t("tableManagement.button.reset"));
			$("#updateTableCancelButton").val(i18n.t("tableManagement.button.cancel"));
			$("#registerTableRegisterButton").val(i18n.t("tableManagement.button.register"));
			$("#registerTableResetButton").val(i18n.t("tableManagement.button.reset"));
			$("#registerTableCancelButton").val(i18n.t("tableManagement.button.cancel"));
			
			$("#updateTablePopupWindow").i18n();
			$("#addTablePopupWindow").i18n();
		},

		parseUIDataToTableData:function (record) {
			if(record != null){
				for(var attr in record) {
					if(attr == "table.status") {
						record[attr] = AppUtil.findTableStatusValue(record[attr]);
					}
				}
				return record;
			}
			return null;
		},

		parseTableDataToUIData:function (table) {
			if(table != null){
				for(var attr in table) {
					if(attr == "status") {
						table[attr] = AppUtil.findTableStatusLable(table[attr]);
					}
				}
				return table;
			}
			return null;
		}
};

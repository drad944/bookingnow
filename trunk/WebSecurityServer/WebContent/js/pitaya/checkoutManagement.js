var checkoutManagement = {
		highlightRows:[],
		leave : function (){
			$("#checkOrderDataGrid").unbind('rowselect');
			$("#backToCheckoutManagement").unbind('click');
			$("#checkoutOrderButton").unbind('click');
			$("#checkOrderCancelButton").unbind('click');
			$("#checkOrderRowButton").unbind('click');
			
			$("#checkOrderRowButton").jqxButton("destroy");
			
			$('#checkOrderDataGrid').jqxGrid('removesort');
			$("#checkOrderDataGrid").jqxGrid('clearfilters');
			$("#checkOrderDataGrid").jqxGrid('clear');
			$('#checkOrderDataGrid').jqxGrid({ filterable: false});
			$('#checkOrderDataGrid').jqxGrid('disabled');
		//	$("#checkOrderDataGrid").jqxGrid("destroy");
			
			checkOrderAllowance.leave();
		},

		visit : function (){
			this.init();
		},
		init : function(){
			var me = this;
			me.parseCheckOrderGridHtml();
		},
		emptyCheckOrderWindow:function (){
			$("#checkOrderDiv").val(null);
			$("#checkOrderDiv").html(null);
			$("#checkOrderResult").text("");
		},
		
		
		


		findDetailOrderTable:function (rowData) {
			if(currentPage.leave){
	    		currentPage.leave();
	    	}
			
			currentPage = checkOrderDetail;
			var initParam = {"order.id": rowData["id"],"order.allowance":rowData["allowance"]};
			openSubPage('framework_main','page/common/orderDetail.html','content',checkOrderDetail,initParam);
			
		},

		initOperateCheckOrderGridElements:function () {
//			var theme = getDemoTheme();
			$("#checkOrderRowButton").jqxButton({ height: 25,theme: theme });
		},	

		addOperateCheckOrderGridEventListeners:function () {
			var me = this;
			
			notification.subscribeTopic("order", function(event){
				var newOrder = me.parseCheckOrderDataToUIData(strToJson(event));
				var commit = $("#checkOrderDataGrid").jqxGrid('addrow', null, newOrder);
				
				$("#eventLog").text("您有新的订单要结账啦");
				//add new order in data grid here,then highlight it
				
			});
			
			$("#checkOrderDataGrid").bind('rowselect', function (event) {
		        $("#eventLog").text("select row index : " + event.args.rowindex);
		        for (var i = 0; i < checkoutManagement.highlightRows.length; i++) {
                    if (checkoutManagement.highlightRows[i].data["id"] == event.args.row["id"]) {
                        checkoutManagement.highlightRows.remove(i);
                        $('#checkOrderDataGrid').jqxGrid('render');
                        break;
                    }
                }
		        
		    });
			// update row.
			$("#checkOrderRowButton").bind('click', function () {
				
				selectedupdaterowindex = $("#checkOrderDataGrid").jqxGrid('getselectedrowindex');
				if(selectedupdaterowindex != -1) {
					
					rowData = $('#checkOrderDataGrid').jqxGrid('getrowdata', selectedupdaterowindex);
					
					if(rowData != null) {
						var offset = $("#checkOrderDataGrid").offset();
						var position = {};
						position.x = parseInt(offset.left) + 200;
						position.y = parseInt(offset.top) - 120;
						checkOrderAllowance.init();
						checkOrderAllowance.initCheckoutOrderAllowanceWindow(rowData,position);
					}
				}else {
					$("#eventLog").text(i18n.t("checkoutOrderManagement.message.requireSelectOneRow"));
				}
				
				
			});
		    
		},

		parseUIDataToCheckOrderData:function (record) {
			if(record != null){
				for(var attr in record) {
					if(attr == "checkOrder.status") {
						record[attr] = findCheckOrderStatusValue(record[attr]);
					}else if(attr == "checkOrder.modifyTime") {
						record[attr] = record[attr].getTime();
					}else if(attr == "checkOrder.submit_time") {
						record[attr] = record[attr].getTime();
					}
				}
				return record;
			}
			return null;
		},

		parseCheckOrderDataToUIData:function (checkOrder) {
			if(checkOrder != null){
				for(var attr in checkOrder) {
					if(attr == "status") {
						checkOrder[attr] = AppUtil.findOrderStatusLable(checkOrder[attr]);
					}else if(attr == "modifyTime") {
						checkOrder[attr] = new Date(checkOrder[attr]).Format("yyyy-MM-dd HH:mm:ss");
					}else if(attr == "submit_time") {
						checkOrder[attr] = new Date(checkOrder[attr]).Format("yyyy-MM-dd HH:mm:ss");
					}else if(attr == "table_details") {
						
						var tableDetails = checkOrder[attr];
						var tables = "";
						for(var i=0;i< tableDetails.length;i++) {
							var tableDetail = tableDetails[i];
							for(var item in tableDetail) {
								if(item == "table") {
									var table = tableDetail[item];
									if(table != null && table["address"] != null){
										tables = tables + table["address"] + ",";
									}
								}
							}
						}
						if(tables.length > 0) {
							tables = tables.substring(0, tables.length - 1);
						}
						
						checkOrder["tables"] = tables;
						checkOrder[attr] = undefined;
					}else if(attr == "food_details") {
						
						var foodDetails = checkOrder[attr];
						var foods = "";
						for(var i=0;i< foodDetails.length;i++) {
							var foodDetail = foodDetails[i];
							for(var item in foodDetail) {
								if(item == "food") {
									var food = foodDetail[item];
									if(food != null && food["name"] != null){
										foods = foods + food["name"] + ",";
									}
								}
							}
						}
						if(foods.length > 0) {
							foods = foods.substring(0, foods.length - 1);
						}
						
						checkOrder["foods"] = foods;
						checkOrder[attr] = undefined;
					}else if(attr == "customer") {
						var customer = checkOrder[attr];
						if(customer != null && customer["name"] != null) {
							checkOrder[attr] = customer["name"];
						}else {
							checkOrder[attr] = null;
						}
						
					}
				}
				return checkOrder;
			}
			return null;
		},
		
		parseCheckOrderGridHtml:function () {
			var me = this;
			me.highlightRows = [];
			$.post("searchOrder.action", 
				{"order.enabled": true,"order.status":5}, 
				function(matchedcheckOrders){
		var option = {
				lng: 'zh',
				fallbackLng: 'zh',
		//		fallbackLng: 'en-US',
		//		lng: 'en-US',
				resGetPath: 'resources/locales/__lng__/__ns__.json',
				getAsync: false,
				ns: 'bookingnow.view',
				load:'current'
			};
		i18n.init(option);
		
		var cellclass = function (row, datafield, value, rowdata) {
            for (var i = 0; i < me.highlightRows.length; i++) {
                if (me.highlightRows[i].data["id"] == rowdata["id"]) {
                    return "highlightRow";
                }
            }
        };
					
		if(matchedcheckOrders != null && matchedcheckOrders.result != null){
			checkOrders = matchedcheckOrders.result;
		}
		
		var checkOrderSize = checkOrders.length;
		var checkOrderData = {};
		
		var columns = [
						{ cellclassname: cellclass, text: i18n.t("checkoutOrderManagement.field.allowance"), datafield: 'allowance',filtertype:'number', width: 80 },
						{ cellclassname: cellclass, text: i18n.t("checkoutOrderManagement.field.customer_count"), datafield: 'customer_count',filtertype:'number', width: 80 },
						{ cellclassname: cellclass, text: i18n.t("checkoutOrderManagement.field.modifyTime"), datafield: 'modifyTime',filtertype:'string', width: 150 },
						{ cellclassname: cellclass, text: i18n.t("checkoutOrderManagement.field.status"), datafield: 'status',filtertype:'textbox', width: 60 },
						{ cellclassname: cellclass, text: i18n.t("checkoutOrderManagement.field.total_price"), datafield: 'total_price',filtertype:'number', width: 80 },
						{ cellclassname: cellclass, text: i18n.t("checkoutOrderManagement.field.foods"), datafield: 'foods',filtertype:'textbox', width: 190 },
						{ cellclassname: cellclass, text: i18n.t("checkoutOrderManagement.field.tables"), datafield: 'tables',filtertype:'textbox', width: 80 },
						{ cellclassname: cellclass, text: i18n.t("checkoutOrderManagement.field.customer"), datafield: 'customer',filtertype:'textbox', width: 80 }
							    ];
		var datafields = [
						{name: 'id',type:"number"},
		                  {name: 'allowance',type:"number"},
		                  {name: 'customer_count',type:"number"},
		                  {name: 'modifyTime',type:"string"},
		                  {name: 'status',type:"string"},
		                  {name: 'total_price',type:"number"},
		                  {name: 'foods',type:"string"},
		                  {name: 'tables',type:"string"},
		                  {name: 'customer',type:"string"}
		                  ];
					
		for(var i = 0;i < checkOrderSize; i++) {
			checkOrder = me.parseCheckOrderDataToUIData(checkOrders[i]);
			
			var rowData = {};
			for(var item in checkOrder) {
				if(item == "customer_id" || item == "food_details" || item == "table_details" || item == "user" || item == "enabled"){
						
				}else {
					rowData[item] = checkOrder[item];
				}
				
			}
			
			checkOrderData[i] = rowData;
		}
		
		
//		var theme = getDemoTheme();
		
		var source =
		{
		    localdata: checkOrderData,
		    datatype: "local",
		    datafields:datafields,
		    addrow: function (rowid, rowdata, position, commit) {
				var rowindex = ($('#checkOrderDataGrid').jqxGrid('getboundrows')).length;
                me.highlightRows.push({ index: rowindex, data: rowdata });
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
		    	var rowindex = $("#checkOrderDataGrid").jqxGrid('getrowboundindexbyid', rowid);          
				me.highlightRows.push({ index: rowindex, data: newdata });
		        // synchronize with the server - send update command
		        // call commit with parameter true if the synchronization with the server is successful 
		        // and with parameter false if the synchronization failed.
		        commit(true);
		    }
		};
		var dataAdapter = new $.jqx.dataAdapter(source);
		// initialize jqxGrid
		$("#checkOrderDataGrid").jqxGrid(
		{
		    width: 800,
		    height: 350,
		    source: dataAdapter,
		    theme: theme,
		    sortable: true,
		    showfilterrow: true,
	        filterable: true,
	        altrows: true,
	        altstart: 0,
	        altstep: 1,
	        enableellipsis: true,
		    pageable: true,
		    enabletooltips: true,
		    autoheight: true,
		    selectionmode:'singlerow',
		    columnsresize: true,
		  //  columnsrecheckOrder: true,
		    columns: columns
		});
		
		me.initOperateCheckOrderGridElements();
		me.addOperateCheckOrderGridEventListeners();
		
		});
			
	}
};


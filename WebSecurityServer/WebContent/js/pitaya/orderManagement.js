var orderManagement = {
		
		leave : function (){
			$("#orderDataGrid").unbind('rowselect');
		},

		visit : function (){
			this.init();
		},
		init : function(){
			var me = this;
			me.parseOrderGridHtml();
		},
		addOrderGridEventListeners:function() {
			$("#orderDataGrid").bind('rowselect', function (event) {
		        $("#eventLog").text("select row index : " + event.args.rowindex);
		    });
		},
		
		parseUIDataToOrderData:function (record) {
			if(record != null){
				for(var attr in record) {
					if(attr == "order.status") {
						record[attr] = findOrderStatusValue(record[attr]);
					}else if(attr == "order.modifyTime") {
						record[attr] = record[attr].getTime();
					}else if(attr == "order.submit_time") {
						record[attr] = record[attr].getTime();
					}
				}
				return record;
			}
			return null;
		},
		parseOrderDataToUIData:	function (order) {
			if(order != null){
				for(var attr in order) {
					if(attr == "status") {
						order[attr] = AppUtil.findOrderStatusLable(order[attr]);
					}else if(attr == "modifyTime") {
						order[attr] = new Date(order[attr]).Format("yyyy-MM-dd HH:mm:ss");
					}else if(attr == "submit_time") {
						order[attr] = new Date(order[attr]).Format("yyyy-MM-dd HH:mm:ss");
					}
				}
				return order;
			}
			return null;
		},

		parseOrderGridHtml:function () {
			var me = this;
			$.post("searchOrder.action", 
				{"order.enabled": true}, 
				function(matchedorders){
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
					
					
		if(matchedorders != null && matchedorders.result != null){
			orders = matchedorders.result;
		}
		
		var orderSize = orders.length;
		var orderData = {};
		
		var columns = {};
		
		var datafields = {};
		for(var i = 0;i < orderSize; i++) {
			order = me.parseOrderDataToUIData(orders[i]);
			if(i==0) {
				var j=0;
				
				for(var item in order) {
					var datafield = {};
					var column = {};
					
					if(item == "id"){
						/*
						datafield["type"] = "number";
						column["text"] = i18n.t("orderManagement.field.id");
						column["filtertype"] = 'number';
						*/
					}else if(item == "allowance") {
						datafield["type"] = "number";
						column["text"] = i18n.t("orderManagement.field.allowance");
						column["filtertype"] = 'number';
					}else if(item == "customer_count") {
						datafield["type"] = "number";
						column["text"] = i18n.t("orderManagement.field.customer_count");
						column["filtertype"] = 'number';
					}else if(item == "customer_id") {
						/*
						datafield["type"] = "number";
						column["text"] = i18n.t("orderManagement.field.customer_id");
						column["filtertype"] = 'number';
						*/
					}else if(item == "modifyTime") {
						datafield["type"] = "number";
						column["text"] = i18n.t("orderManagement.field.modifyTime");
						
					}else if(item == "prePay") {
						datafield["type"] = "number";
						column["text"] = i18n.t("orderManagement.field.prePay");
						column["filtertype"] = 'number';
					}else if(item == "status") {
						datafield["type"] = "string";
						column["text"] = i18n.t("orderManagement.field.status");
						column["filtertype"] = 'textbox';
					}else if(item == "submit_time") {
						datafield["type"] = "number";
						column["text"] = i18n.t("orderManagement.field.submit_time");
						
					}else if(item == "total_price") {
						datafield["type"] = "number";
						column["text"] = i18n.t("orderManagement.field.total_price");
						column["filtertype"] = 'number';
					}else if(item == "user_id") {
						/*
						datafield["type"] = "number";
						column["text"] = i18n.t("orderManagement.field.user_id");
						column["filtertype"] = 'number';
						*/
					}else if(item == "customer" || item == "food_details" ||item == "table_details" ||item == "user" || item == "enabled"){
						//do nothing
					}else {
						datafield["type"] = "string";
						column["text"] = i18n.t("orderManagement.field.xx");
					}
					
					if(item == "customer" || item == "food_details" ||item == "table_details" ||item == "user" || item == "enabled"){
						
					}else if(item == "id" || item == "customer_id" ||item == "user_id"){
						
					}else {
						column["datafield"] = item;
						
						if(item == "id"){
		//					column["width"] = "50";
						}else if(item == "allowance") {
							column["width"] = "120";
						}else if(item == "customer_count") {
							column["width"] = "100";
						}else if(item == "customer_id") {
		//					column["width"] = "80";
						}else if(item == "modifyTime") {
							column["width"] = "130";
						}else if(item == "prePay") {
							column["width"] = "100";
						}else if(item == "status") {
							column["width"] = "100";
						}else if(item == "submit_time") {
							column["width"] = "130";
						}else if(item == "total_price") {
							column["width"] = "120";
						}else if(item == "user_id") {
		//					column["width"] = "50";
						}else if(item == "customer" || item == "food_details" ||item == "table_details" ||item == "user" || item == "enabled"){
							//do nothing
						}else {
							column["width"] = "50";
						}
						
						columns[j] = column;
						
						datafields[j] = datafield;
						j++;
					}
				}
			}
			
			var rowData = {};
			for(var item in order) {
				if(item == "customer" || item == "food_details" ||item == "table_details" ||item == "user" || item == "enabled"){
						
				}else {
					rowData[item] = order[item];
				}
				
			}
			
			orderData[i] = rowData;
		}
		
		
//		var theme = getDemoTheme();
		
		var source =
		{
		    localdata: orderData,
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
		$("#orderDataGrid").jqxGrid(
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
	    
		me.addOrderGridEventListeners();
		/*
		 $("#orderDataGrid").on('columnreordered', function (event) {
		    var column = event.args.columntext;
		    var newindex = event.args.newindex
		    var oldindex = event.args.oldindex;
		});
		*/
		});
			
	}
};








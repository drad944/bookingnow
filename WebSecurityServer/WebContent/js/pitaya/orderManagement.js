var orderManagement = {
		
		leave : function (){
			$("#orderDataGrid").unbind('rowselect');
		},

		visit : function (searchOption){
			this.init(searchOption);
		},
		init : function(searchOption){
			var me = this;
			me.parseOrderGridHtml(searchOption);
		},
		
		initOrderGridLocale:function() {
			$("#backToSearchOrderOptionBar").val(i18n.t("orderManagement.button.back"));
		},
		
		initOrderGridElement:function() {
			$("#backToSearchOrderOptionBar").jqxButton({ width: 60, height: 25, theme: theme });
		},
		
		orderGridEventListeners:function() {
			$("#backToSearchOrderOptionBar").bind('click', function (event) {
		        openSubPage('framework_main','page/common/orderSearchOptionBar.html','content',orderSearchOptionBar,null);
		    });
			
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

		parseOrderGridHtml:function (searchOption) {
			var me = this;
				
			$.post("powerSearchOrder.action", 
				searchOption, 
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
		
		var columns = [
						{ text: i18n.t("orderManagement.field.allowance"), datafield: 'allowance',filtertype:'number', width: 80 },
						{ text: i18n.t("orderManagement.field.customer_count"), datafield: 'customer_count',filtertype:'number', width: 80 },
						{ text: i18n.t("orderManagement.field.modifyTime"), datafield: 'modifyTime',filtertype:'textbox', width: 160 },
						{ text: i18n.t("orderManagement.field.prePay"), datafield: 'prePay',filtertype:'number', width: 100 },
						{ text: i18n.t("orderManagement.field.submit_time"), datafield: 'submit_time',filtertype:'textbox', width: 160 },
						{ text: i18n.t("orderManagement.field.status"), datafield: 'status',filtertype:'textbox', width: 100 },
						{ text: i18n.t("orderManagement.field.total_price"), datafield: 'total_price',filtertype:'number', width: 120 }
						
							    ];
		var datafields = [
						{name: 'id',type:"number"},
		                  {name: 'allowance',type:"number"},
		                  {name: 'customer_count',type:"number"},
		                  {name: 'modifyTime',type:"string"},
		                  {name: 'prePay',type:"number"},
		                  {name: 'submit_time',type:"string"},
		                  {name: 'status',type:"string"},
		                  {name: 'total_price',type:"number"},
		                  {name: 'foods',type:"string"},
		                  {name: 'tables',type:"string"},
		                  {name: 'customer',type:"string"}
		                  ];
		
		for(var i = 0;i < orderSize; i++) {
			order = me.parseOrderDataToUIData(orders[i]);
			
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
		  //  columnsreorder: true,
		    columns: columns
		});
		
		// display selected row index.
	    me.initOrderGridLocale();
	    me.initOrderGridElement();
		me.orderGridEventListeners();
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








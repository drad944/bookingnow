var checkOrderAllowance = {
		
		leave : function (){
			$("#checkoutOrderAllowanceWindow").unbind('close');
			$("#checkoutOrderAllowanceCancelButton").unbind('click');
			$("#checkoutOrderAllowanceButton").unbind('click');
		},

		visit : function (){
			this.init();
		},
		init : function(){
			var me = this;
			me.initcheckoutOrderAllowanceElements();
			me.addcheckoutOrderAllowanceEventListeners();
		},
		initcheckoutOrderAllowanceElements:function () {
			$('#checkoutOrderAllowanceButton').jqxButton({ width: 60, height: 25, theme: theme });
			$('#checkoutOrderAllowanceCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
			$('#checkoutOrderAllowanceInput').jqxNumberInput({ width: 120, height: 20,min: 0.01, max: 1, decimalDigits:2, digits: 1, theme: theme, spinButtons: true});
			
		},
		emptyCheckoutOrderAllowanceWindow:function () {
			$("#checkoutOrderAllowanceInput").val(null);
			$("#checkoutOrderId").val(null);
			$("#checkoutOrderTableAddress").val(null);
			$("#checkOrderResult").text("");
		},
		addcheckoutOrderAllowanceEventListeners:function () {
			var me = this;
			$('#checkoutOrderAllowanceWindow').bind('close', function (event) { 
				me.emptyCheckoutOrderAllowanceWindow();
				me.leave();
			});
			
			$("#checkoutOrderAllowanceCancelButton").bind('click', function (event) {
				//$('#updateCheckOrderInfoForm').jqxValidator('hide');
			    $('#checkoutOrderAllowanceWindow').jqxWindow('close');
			});
			
			$('#checkoutOrderAllowanceButton').bind('click', function () {
				$('#checkoutOrderAllowanceWindow').jqxWindow('close');
				
				selectedupdaterowindex = $("#checkOrderDataGrid").jqxGrid('getselectedrowindex');
				//id = $("#checkOrderDataGrid").jqxGrid('getrowid', selectedrowindex);
			    rowData = $('#checkOrderDataGrid').jqxGrid('getrowdata', selectedupdaterowindex);
			    rowData["allowance"] = $('#checkoutOrderAllowanceInput').jqxNumberInput('val');
				
				if(rowData != null) {
					
					checkoutManagement.findDetailOrderTable(rowData);
				}
		    });
		},
		formatCheckoutOrderAllowanceElements:function (rowData){
			
			$("#checkoutOrderId").text(rowData["id"]);
			$("#checkoutOrderTableAddress").text(rowData["tables"]);
			
			$("#checkoutOrderAllowanceInput").jqxNumberInput('val',rowData["allowance"]);
		},

		initCheckoutOrderAllowanceWindow:function (rowData,position) {
			var me = this;
			$("#checkoutOrderAllowanceWindow").removeAttr("style");
			
			me.formatCheckoutOrderAllowanceElements(rowData);
			
			
			$("#checkoutOrderAllowanceWindow").jqxWindow({
		    	position:position, isModal: true,width: 250, height: 180, resizable: false, theme: theme, cancelButton: $("#checkoutOrderAllowanceCancelButton"), modalOpacity: 0.01,
		    	initContent: function () {
		            $('#checkoutOrderAllowanceWindow').jqxWindow('focus');
		        }
		    });
		    
		    $("#checkoutOrderAllowanceWindow").jqxWindow('open');
			
		}
};


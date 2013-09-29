var orderSearchOptionBar = {
		
		leave : function (){
			var me = this;
			me.emptyOrderSearchOptionBar();
		},

		visit : function (){
			this.init();
		},
		
		init : function(){
			var me = this;
			me.initLocaleOrderSearchOptionBar();
			me.initOrderSearchOptionBarElements();
			me.formatOrderSearchOptionBar();
			me.orderSearchOptionBarEventListeners();
		},
		formatOrderSearchOptionBar:function() {
			var currentTime = new Date();
			$("#toDateTime").val(currentTime.Format('yyyy-MM-dd HH:mm:ss'));
			
			var previousTime = new Date(currentTime.getTime() - 86400000);
			$("#fromDateTime").val(previousTime.Format('yyyy-MM-dd HH:mm:ss'));
			
		},
		
		initLocaleOrderSearchOptionBar:function (){
			$("#orderFromDateTimeLabel").text(i18n.t("orderManagement.searchOptionBar.fromDateTime"));
			$("#orderToDateTimeLabel").text(i18n.t("orderManagement.searchOptionBar.toDateTime"));
			$("#searchOptionBution").val(i18n.t("orderManagement.searchOptionBar.button.search"));
			$("#searchOptionClearBution").val(i18n.t("orderManagement.searchOptionBar.button.clear"));
			
		},
		
		emptyOrderSearchOptionBar:function (){
			$("#fromDateTime").val(null);
			$("#toDateTime").val(null);
		},

		initOrderSearchOptionBarElements:function () {
			
		    $('#searchOptionBution').jqxButton({ width: 100, height: 25, theme: theme });
		    $('#searchOptionClearBution').jqxButton({ width: 100, height: 25, theme: theme });
		    
		    
		    $('#orderSearchOptionForm').jqxValidator({
		     rules: [
		            { input: '#toDateTime', message: i18n.t("orderManagement.searchOptionBar.validation.message.toTimeLTFromTime",{year:((new Date()).getFullYear() + 1)}), action: 'blur', rule: function (input, commit) {
		                var fromDateTime = $('#fromDateTime').val();
		                var toDateTime = $('#toDateTime').val();
		                if(fromDateTime && toDateTime && fromDateTime != "" && toDateTime != "") {
		                	var from = new Date(Date.parse(fromDateTime.replace(/-/g,"/")));
		                	var to = new Date(Date.parse(toDateTime.replace(/-/g,"/")));
		                	
		                	var result = from < to;
		                	return result;
		                }
		                
		                return true;
		            	}
		            }
		            ], 
		            theme: theme
		    });
		},
		
		doSearch:function() {
			var searchOption = {};
				var fromDateTime = $('#fromDateTime').val();
		        var toDateTime = $('#toDateTime').val();
		        if(fromDateTime && fromDateTime != "") {
		        	
		        	searchOption["params.fromDateTime"] = (new Date(Date.parse(fromDateTime.replace(/-/g,"/")))).getTime();
		        }
		        if(toDateTime && toDateTime != "") {
		        	searchOption["params.toDateTime"] = (new Date(Date.parse(toDateTime.replace(/-/g,"/")))).getTime();
		        }        
				
				openSubPage('framework_main','page/common/orderManagement.html','content',orderManagement,searchOption);
		},
		
		orderSearchOptionBarEventListeners:function () {
			var me = this;
			$('#orderSearchOptionForm').bind('validationSuccess', function (event) { 
		    	
		    	me.doSearch();
		    });
			
			$('#searchOptionBution').bind('click', function () {
				$('#orderSearchOptionForm').jqxValidator('validate');
		    });
			
			$('#searchOptionClearBution').bind('click', function () {
				$("#fromDateTime").val(null);
				$("#toDateTime").val(null);
				
		    });
		    
		}

		
		
};

	
	
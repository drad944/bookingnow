var currentPage = {};

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
	},
	
	getRoleName : function(role){
		for(var i=0; i < Constants.ROLES.length; i++){
			if(Constants.ROLES[i].value == role){
				return Constants.ROLES[i].name;
			}
		}
		return "未知";
	},
	
	findSexString:function (value) {
		var option = {
				fallbackLng: 'zh',
				lng: 'en-US',
		//		lng: 'zh-CN',
				resGetPath: 'resources/locales/__lng__/__ns__.json',
				getAsync: false,
				ns: 'bookingnow.content.userManagement'
			};
		 
		i18n.init(option);
		
		if(value == 2) {
			return i18n.t("sex.male");
		}else if(value == 3){
			return i18n.t("sex.female");
		}
		return i18n.t("sex.other");
	},
	
	findSexValue:function (label) {
		var option = {
				fallbackLng: 'zh',
				lng: 'en-US',
		//		lng: 'zh-CN',
				resGetPath: 'resources/locales/__lng__/__ns__.json',
				getAsync: false,
				ns: 'bookingnow.content.userManagement'
			};
		 
		i18n.init(option);
		
		if(label == i18n.t("sex.male")) {
			return 2;
		}else if(label == i18n.t("sex.female")){
			return 3;
		}
		return 1;
	},
	
	findDateTime:function (datetimeString) {
		//var s = "2005-02-05 01:02:03";   
		var d = new Date(Date.parse(datetimeString.replace(/-/g,"/")));
		return d;
	},
	
	findRoleStringArray:function (value) {
		var values = null;
		if(value != null) {
			values = value.split(",");
		}
		if(values != null){
			for(var i = values.length - 1;i >= 0;i--) {
				values[i] = trim(values[i]);
			}
		}
		 
		 return values;
	},
	
	findRoleString:function (value) {
		var option = {
				fallbackLng: 'zh',
				lng: 'en-US',
		//		lng: 'zh-CN',
				resGetPath: 'resources/locales/__lng__/__ns__.json',
				getAsync: false,
				ns: 'bookingnow.content.userManagement'
			};
		 
		i18n.init(option);
		var values = null;
		if(value.toString.length > 0) {
			values = value.split(",");
		}
		if(values != null&& values.length > 0){
			for(var i = values.length;i >= 0;i--) {
				values[i] = trim(values[i]);
			}
		}
		
		var userRoleData = [
		    				{ value: 2, label: i18n.t("role.ANONYMOUS") },
		    				{ value: 3, label: i18n.t("role.CUSTOMER") },
		    				{ value: 4, label: i18n.t("role.CUSTOMER_VIP1") },
		    				{ value: 5, label: i18n.t("role.CUSTOMER_VIP2") },
		    				{ value: 6, label: i18n.t("role.WELCOMER") },
		    				{ value: 7, label: i18n.t("role.CHEF") },
		    				{ value: 8, label: i18n.t("role.WAITER") },
		    				{ value: 9, label: i18n.t("role.CASHIER") },
		    				{ value: 10, label: i18n.t("role.MANAGER") },
		    				{ value: 11, label: i18n.t("role.ADMIN") }
		            ];
		var valueString = "";
		if(values != null && values.length > 0){
			for(var j=0;j < values.length;j++) {
				 var tempValue = values[j];
				 for(var i = 0;i < userRoleData.length;i++) {
					 if(userRoleData[i].value == tempValue) {
						 valueString = valueString + userRoleData[i].label + ",";
					 }
				 }
			 }
			 if(valueString.length > 0) {
				 valueString = valueString.substring(0, valueString.length - 2);
			 }
		}
		 
		 return valueString;
	},
	
	findRoleValueArray:function (label) {
		var labels = null;
		if(label != null) {
			labels = label.split(",");
		}
		if(labels != null) {
			for(var i = labels.length - 1;i >= 0;i--) {
				labels[i] = trim(labels[i]);
			}
		}
		 
		 return labels;
	},
	
	findRoleValue:function (label) {
		var option = {
				fallbackLng: 'zh',
				lng: 'en-US',
		//		lng: 'zh-CN',
				resGetPath: 'resources/locales/__lng__/__ns__.json',
				getAsync: false,
				ns: 'bookingnow.content.userManagement'
			};
		 
		i18n.init(option);
		var labels = null;
		if(label.length > 0) {
			labels = label.split(",");
		}
		if(labels != null && labels.length > 0) {
			for(var i = labels.length;i >= 0;i--) {
				labels[i] = trim(labels[i]);
			}
		}
		
		
		var userRoleData = [
		    				{ value: 2, label: i18n.t("role.ANONYMOUS") },
		    				{ value: 3, label: i18n.t("role.CUSTOMER") },
		    				{ value: 4, label: i18n.t("role.CUSTOMER_VIP1") },
		    				{ value: 5, label: i18n.t("role.CUSTOMER_VIP2") },
		    				{ value: 6, label: i18n.t("role.WELCOMER") },
		    				{ value: 7, label: i18n.t("role.CHEF") },
		    				{ value: 8, label: i18n.t("role.WAITER") },
		    				{ value: 9, label: i18n.t("role.CASHIER") },
		    				{ value: 10, label: i18n.t("role.MANAGER") },
		    				{ value: 11, label: i18n.t("role.ADMIN") }
		            ];
		var labelString = new Array();
		if(labels != null && labels.length > 0) {
			for(var j=0;j < labels.length;j++) {
				 var tempLabel = labels[j];
				 for(var i = 0;i < userRoleData.length;i++) {
					 if(userRoleData[i].label == tempLabel) {
						 labelString[j] = userRoleData[i].value;
						 break;
					 }
				 }
			 }
		}
		 
		 return labelString;
	},
	
	findDepartmentString:function (value) {
		var option = {
				fallbackLng: 'zh',
				lng: 'en-US',
		//		lng: 'zh-CN',
				resGetPath: 'resources/locales/__lng__/__ns__.json',
				getAsync: false,
				ns: 'bookingnow.content.userManagement'
			};
		 
		i18n.init(option);
		
		 var userDepartmentData = [
		                                { value: 2, label: i18n.t("department.BUSSINESS") },
		                                { value: 3, label: i18n.t("department.PRODUCTION") },
		                                { value: 4, label: i18n.t("department.FINANCE") },
		                                { value: 5, label: i18n.t("department.PERSONNEL") },
		                                { value: 6, label: i18n.t("department.DEVERLOPE") },
		                                { value: 7, label: i18n.t("department.MANAGEMENT") }
		                            ];
		 
		 for(var i = 0;i < userDepartmentData.length;i++) {
			 if(userDepartmentData[i].value == value) {
				 return userDepartmentData[i].label;
			 }
		 }
		 
		 return value;
	},
	
	findDepartmentValue:function (label) {
		var option = {
				fallbackLng: 'zh',
				lng: 'en-US',
		//		lng: 'zh-CN',
				resGetPath: 'resources/locales/__lng__/__ns__.json',
				getAsync: false,
				ns: 'bookingnow.content.userManagement'
			};
		 
		i18n.init(option);
		
		 var userDepartmentData = [
										{ value: 2, label: i18n.t("department.BUSSINESS") },
										{ value: 3, label: i18n.t("department.PRODUCTION") },
										{ value: 4, label: i18n.t("department.FINANCE") },
										{ value: 5, label: i18n.t("department.PERSONNEL") },
										{ value: 6, label: i18n.t("department.DEVERLOPE") },
										{ value: 7, label: i18n.t("department.MANAGEMENT") }
		                            ];
		 for(var i = 0;i < userDepartmentData.length;i++) {
			 if(userDepartmentData[i].label == label) {
				 return userDepartmentData[i].value;
			 }
		 }
		 
		 return label;
	},
	
	findTableStatusValue:function (label) {
		var option = {
				fallbackLng: 'zh',
				lng: 'en-US',
		//		lng: 'zh-CN',
				resGetPath: 'resources/locales/__lng__/__ns__.json',
				getAsync: false,
				ns: 'bookingnow.content.tableManagement'
			};
		 
		i18n.init(option);
		
		 var tableStatusData = [
		                                { value: 2, label: i18n.t("status.EMPTY") },
		                                { value: 3, label: i18n.t("status.BOOKING") },
		                                { value: 4, label: i18n.t("status.USING") }
		                            ];
		 for(var i = 0;i < tableStatusData.length;i++) {
			 if(tableStatusData[i].label == label) {
				 return tableStatusData[i].value;
			 }
		 }
		 
		 return label;
	},
	
	findTableStatusLable:function (value) {
		var option = {
				fallbackLng: 'zh',
				lng: 'en-US',
		//		lng: 'zh-CN',
				resGetPath: 'resources/locales/__lng__/__ns__.json',
				getAsync: false,
				ns: 'bookingnow.content.tableManagement'
			};
		 
		i18n.init(option);
		 var tableStatusData = [
		                                { value: 2, label: i18n.t("status.EMPTY") },
		                                { value: 3, label: i18n.t("status.BOOKING") },
		                                { value: 4, label: i18n.t("status.USING") }
		                            ];
		 for(var i = 0;i < tableStatusData.length;i++) {
			 if(tableStatusData[i].value == value) {
				 return tableStatusData[i].label;
			 }
		 }
		 
		 return value;
	},
	
	findOrderStatusValue:function (label) {
		var option = {
				fallbackLng: 'zh',
				lng: 'en-US',
		//		lng: 'zh-CN',
				resGetPath: 'resources/locales/__lng__/__ns__.json',
				getAsync: false,
				ns: 'bookingnow.content.orderManagement'
			};
		 
		i18n.init(option);
		 var orderStatusData = [
		                                { value: 1, label: i18n.t("status.NEW") },
		                                { value: 2, label: i18n.t("status.WELCOMER_NEW") },
		                                { value: 3, label: i18n.t("status.WAITING") },
		                                { value: 4, label: i18n.t("status.COMMITED") },
		                                { value: 5, label: i18n.t("status.PAYING") },
		                                { value: 6, label: i18n.t("status.FINISHED") },
		                                { value: 7, label: i18n.t("status.UNAVAILABLE") },
		                                { value: 8, label: i18n.t("status.AVAILABLE") }
		                            ];
		 for(var i = 0;i < orderStatusData.length;i++) {
			 if(orderStatusData[i].label == label) {
				 return orderStatusData[i].value;
			 }
		 }
		 
		 return label;
	},
	
	findOrderStatusLable:function (value) {
		var option = {
				fallbackLng: 'zh',
				lng: 'en-US',
		//		lng: 'zh-CN',
				resGetPath: 'resources/locales/__lng__/__ns__.json',
				getAsync: false,
				ns: 'bookingnow.content.orderManagement'
			};
		 
		i18n.init(option);
		 var orderStatusData = [
		                                { value: 1, label: i18n.t("status.NEW") },
		                                { value: 2, label: i18n.t("status.WELCOMER_NEW") },
		                                { value: 3, label: i18n.t("status.WAITING") },
		                                { value: 4, label: i18n.t("status.COMMITED") },
		                                { value: 5, label: i18n.t("status.PAYING") },
		                                { value: 6, label: i18n.t("status.FINISHED") },
		                                { value: 7, label: i18n.t("status.UNAVAILABLE") },
		                                { value: 8, label: i18n.t("status.AVAILABLE") }
		                            ];
		 for(var i = 0;i < orderStatusData.length;i++) {
			 if(orderStatusData[i].value == value) {
				 return orderStatusData[i].label;
			 }
		 }
		 
		 return value;
	},
	
	findImgOriginalSize:function (src) {
		var imageSize = {};
		if(src != null) {
			var img = new Image();
			img.src = src;
			 
			if(img.complete){
			    getImgOriginalSize.call(img);
			    img = null;
			}else{
			    img.onload=function(){
			        getImgOriginalSize.call(img);
			        img = null;
			    };
			}
			
			function getImgOriginalSize(){
			    imageSize["width"] = this.width;
			    imageSize["height"] = this.height;
			}
		}
		return imageSize;
	}
	
	
	

};


var foodManagement = {

	foodeditrow: -1,
	
	leave : function (){
		$("#savebtn").unbind('click');
		$("#cancelbtn").unbind('click');
		$("#groupbtn").unbind('click');
		$("#addbtn").unbind('click');
		$("#delbtn").unbind('click');
		$("#updatemenubtn").unbind('click');
		$("#category").unbind('change');
	},

	visit : function (){
		this.init();
	},
	
	init : function(){
		var data = [];
		var categories = [];
		var me = this;
		AppUtil.request("searchFood.action", null, function(result){
			if(result && result.length > 0){
		    	for(var i=0; i < result.length; i++){
		    		var rowdata = {};
		    		var item = result[i];
		    		rowdata.id = item.id;
		    		rowdata.name =  item.name;
		    		rowdata.category = item.category;
		    		me.addCategory(item.category, categories);
		    		rowdata.desc = item.description;
		    		rowdata.price = item.price;
		    		rowdata.status = AppUtil.getFoodStatus(item.status);
		    		rowdata.imgpath = item.large_image_relative_path;
		    		if(item.status == 1){
		    			rowdata.available = true;
		    		} else {
		    			rowdata.available = false;
		    		}
		    		data.push(rowdata);
		    	}
		    	me.setupFoodView(data, categories);
			}
		}, function(){
			alert("Fail to get data!");
		});
		
	},

	groupByCategory: function(flag){
		if(flag){
			$("#jqxgrid").jqxGrid('addgroup', 'category');
		} else {
			$("#jqxgrid").jqxGrid('removegroup', 'category');
		}
	},
		
	addCategory: function(category, categories){
		for(var i = 0; i < categories.length; i++){
			if(categories[i] == category){
				return;
			}
		}
		categories.push(category);
	},

	setupFoodView: function (fooddata, categories) {
		var me = this;
		var source = {
			localdata : fooddata,
			datatype : "array",
			addrow : function(rowid, newdata, position, commit) {
				var status = newdata.available == true ? Constants.FOOD_STATUS_OK
						: Constants.FOOD_STATUS_NO;
				var param = {
					"food.name" : newdata.name,
					"food.category" : newdata.category,
					"food.description" : newdata.desc,
					"food.price" : newdata.price,
					"food.status" : status,
					"food.large_image_relative_path" : newdata.fileId
				};
				AppUtil.request("addFood.action", param, function(data) {
					if (data) {
						var result = data.isSuccess;
						if (result == true && data.detail && data.detail != "") {
							var details = data.detail.split("###");
							newdata.id = details[0];
							newdata.imgpath = details[1];
							commit(true);
						} else {
							alert("操作失败:" + data.detail);
						}
					} else {
						alert("操作失败");
					}
					commit(false);
					newdata.fileId = null;
					$("#popupWindow").jqxWindow('hide');
				}, function() {
					alert("操作失败");
					commit(false);
					newdata.fileId = null;
					$("#popupWindow").jqxWindow('hide');
				});
			},
			deleterow : function(rowid, commit) {
				var rowData = $('#jqxgrid').jqxGrid('getrowdata', rowid);
				var param = {
					"food.id" : rowData.id,
				};
				AppUtil.request("removeFood.action", param, function(data) {
					if (data) {
						var result = data.isSuccess;
						if (result == true) {
							commit(true);
						} else {
							alert("操作失败:" + data.detail);
						}
					} else {
						alert("操作失败");
					}
					commit(false);
				}, function() {
					alert("操作失败");
					commit(false);
				});
			},
			updaterow : function(rowid, newdata, commit) {
				var status = newdata.available == true ? Constants.FOOD_STATUS_OK
						: Constants.FOOD_STATUS_NO;
				var param = {
					"food.id" : newdata.id,
					"food.name" : newdata.name,
					"food.category" : newdata.category,
					"food.description" : newdata.desc,
					"food.price" : newdata.price,
					"food.status" : status,
					"food.large_image_relative_path" : newdata.fileId
				};
				AppUtil.request("updateFood.action", param, function(data) {
					if (data) {
						var result = data.isSuccess;
						if (result == true) {
							if (data.detail && data.detail != "") {
								newdata.imgpath = data.detail;
							}
							commit(true);
						} else {
							alert("操作失败:" + data.detail);
						}
					} else {
						alert("操作失败");
					}
					commit(false);
					newdata.fileId = null;
					$("#popupWindow").jqxWindow('hide');
				}, function() {
					alert("操作失败");
					commit(false);
					newdata.fileId = null;
					$("#popupWindow").jqxWindow('hide');
				});
			}
		};

		$("#popupWindow").jqxWindow({
			width : 700,
			height : 630,
			maxHeight : 750,
			resizable : false,
			theme : theme,
			autoOpen : false,
			isModal : true
		});
		
		$("#category").jqxComboBox({
			source : categories,
			selectedIndex : 0,
			width : '160px',
			height : '25px',
			theme : theme
		});
		
		$("#price").jqxNumberInput({
			symbol : 'rmb',
			width : 200,
			height : 23,
			theme : theme,
			spinButtons : true
		});
		
		$("#status").jqxCheckBox({
			width : 120,
			theme : theme
		});

		var dataAdapter = new $.jqx.dataAdapter(source);
		$("#jqxgrid").jqxGrid({
			theme : theme,
			autoheight : true,
			width : 800,
			showgroupmenuitems : false,
			sortable : true,
			pageable : true,
			source : dataAdapter,
			pagesizeoptions : [ '10', '20', '30' ],
			pagesize : 20,
			columns : [
					{
						text : '菜名',
						datafield : 'name',
						width : 250
					},
					{
						text : '类别',
						datafield : 'category',
						width : 100
					},
					{
						text : '描述',
						datafield : 'desc',
						width : 200
					},
					{
						text : '价格',
						datafield : 'price',
						width : 100
					},
					{
						text : '状态',
						datafield : 'status',
						width : 60
					},
					{
						text : '操作',
						datafield : 'Edit',
						columntype : 'button',
						cellsrenderer : function() {
							return "修改";
						},
						buttonclick : function(row) {
							me.foodeditrow = row;
							var dataRecord = $("#jqxgrid").jqxGrid(
									'getrowdata', me.foodeditrow);
							$("#name").val(dataRecord.name);
							$("#category").val(dataRecord.category);
							$("#desc").val(dataRecord.desc);
							$("#price").jqxNumberInput({
								decimal : dataRecord.price
							});
							$("#status").jqxCheckBox({
								checked : dataRecord.available
							});
							Uploader.init("picture", dataRecord.imgpath, function(picid) {
								dataRecord.fileId = picid;
							});
							$("#popupWindow").jqxWindow('open');
						}
					} ],
			groupable : true,
			showgroupsheader : false,
		});
		
		var localizationobj = {};
		localizationobj.pagergotopagestring = "跳转至";
		localizationobj.pagershowrowsstring = " 显示";
		localizationobj.pagerrangestring = " 共 ";
		localizationobj.sortascendingstring = "从低到高排序";
		localizationobj.sortdescendingstring = "从高到低排序";
		localizationobj.sortremovestring = "取消排序";
		$("#jqxgrid").jqxGrid('localizestrings', localizationobj);
		
		
		$("#savebtn").jqxButton({
			theme : theme,
			width : '50',
			height : '25'
		});
		$("#cancelbtn").jqxButton({
			theme : theme,
			width : '50',
			height : '25'
		});
		
		$("#groupbtn").jqxButton({
			theme : theme,
			width : '80',
			height : '25'
		});
		$("#addbtn").jqxButton({
			theme : theme,
			width : '50',
			height : '25'
		});
		$("#delbtn").jqxButton({
			theme : theme,
			width : '50',
			height : '25'
		});
		$("#updatemenubtn").jqxButton({
			theme : theme,
			width : '80',
			height : '25'
		});
		
		$("#savebtn").bind('click', function(event) {
			var name = $("#name").val();
			var category = $("#category").val();
			var desc = $("#desc").val();
			var price = $('#price').jqxNumberInput('decimal');
			var status = -1;
			var available = true;
			if ($("#status").jqxCheckBox('checked') == true) {
				status = AppUtil.getFoodStatus(Constants.FOOD_STATUS_OK);
				available = true;
			} else {
				status = AppUtil.getFoodStatus(Constants.FOOD_STATUS_NO);
				available = false;
			}
			var newData = {
				name : name,
				category : category,
				desc : desc,
				price : price,
				status : status,
				available : available,
			};
			if (me.foodeditrow >= 0) {
				var rowID = $('#jqxgrid').jqxGrid('getrowid', me.foodeditrow);
				var rowData = $('#jqxgrid').jqxGrid('getrowdata', rowID);
				newData.id = rowData.id;
				newData.imgpath = rowData.imgpath;
				if (rowData.fileId != null) {
					newData.fileId = rowData.fileId;
				}
				$('#jqxgrid').jqxGrid('updaterow', rowID, newData);
			} else {
				newData.fileId = me.newaddimgid;
				$('#jqxgrid').jqxGrid('addrow', null, newData);
			}
		});

		$("#cancelbtn").bind('click', function() {
			if (this.foodeditrow >= 0) {
				var rowID = $('#jqxgrid').jqxGrid('getrowid', this.foodeditrow);
				var rowData = $('#jqxgrid').jqxGrid('getrowdata', rowID);
				rowData.fileId = null;
			}
			$("#popupWindow").jqxWindow('hide');
		});

		$("#category").bind('change',function(event) {
			if (event.args == null) {
				var newcategory = $("#category").val();
				if (newcategory != null && newcategory.trim() != ""
						&& $("#category").jqxComboBox('getItemByValue', newcategory) == null) {
					$("#category").jqxComboBox('addItem', newcategory);
					$("#category").val(newcategory);
				}
			}
		});

		$("#groupbtn").bind('click', function() {
			if ($("#groupbtn")[0].value == "按分类显示") {
				me.groupByCategory(true);
				$("#groupbtn")[0].value = "显示全部";
			} else {
				me.groupByCategory(false);
				$("#groupbtn")[0].value = "按分类显示";
			}
		});

		$("#addbtn").bind('click', function() {
			var me = this;
			me.foodeditrow = -1;
			me.newaddimgid = null;
			$("#name").val("");
			$("#desc").val("");
			$("#category").jqxComboBox('selectIndex', 0);
			$("#price").jqxNumberInput({
				decimal : 0
			});
			$("#status").jqxCheckBox({
				checked : true
			});
			Uploader.init("picture", null, function(picid) {
				me.newaddimgid = picid;
			});
			$("#popupWindow").jqxWindow('open');
		});

		$("#delbtn").bind('click', function() {
			var selectedrowindex = $("#jqxgrid").jqxGrid('getselectedrowindex');
			$('#jqxgrid').jqxGrid('deleterow', selectedrowindex);
		});

		$("#updatemenubtn").bind('click', function() {
			AppUtil.request("updateClientsFood.action", null, function(result) {
				if (result && result.isSuccess == true) {
					alert("成功向客户端推送更新通知!");
				}
			});
		});
	}
};
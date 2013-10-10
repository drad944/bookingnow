var adminManagement = {
		
	setupAdminView : function(result){
		var data = [];
		for(var i=0; i < result.length; i++){
			var rowdata = {};
			var item = result[i];
			rowdata.auth = item.isAuth;
			if(rowdata.auth == true){
				rowdata.id = item.userid;
				rowdata.name = item.name;
				rowdata.role =  AppUtil.getRoleName(item.roleType);
			} else {
				rowdata.id = "未认证";
				rowdata.name = "未认证用户";
				rowdata.role = "无";
			}
			data.push(rowdata);
		}
		var source = {
			localdata : data,
			datatype: "array",
		};
		var dataAdapter = new $.jqx.dataAdapter(source);
		$("#jqxAdminGrid").jqxGrid(
			{
				theme : theme,
				autoheight: true,
				width: 300,
				showgroupmenuitems: false,
	            sortable: true,
	            pageable: true,
			    source: dataAdapter,
			    pagesizeoptions: ['10', '20', '30'],
			    pagesize: 10,
			    columns: [
			        { text: '名称', datafield: 'name', width: 150 },
			        { text: '角色', datafield: 'role', width: 150 },
			    ],
			});
	 	var localizationobj = {};
	 	localizationobj.pagergotopagestring = "跳转至";
	 	localizationobj.pagershowrowsstring = " 显示";
	 	localizationobj.pagerrangestring = " 共 ";
	 	localizationobj.sortascendingstring = "从低到高排序";
        localizationobj.sortdescendingstring = "从高到低排序";
        localizationobj.sortremovestring = "取消排序";
    	$("#jqxAdminGrid").jqxGrid('localizestrings', localizationobj);
	},

	visit : function(){
		var me = this;
		AppUtil.request("getConnectionInfo.action", null, function(result){
			if(result){
				me.setupAdminView(result);
			}
		}, function(){
			alert("网络错误!");
		});
	}
	
};

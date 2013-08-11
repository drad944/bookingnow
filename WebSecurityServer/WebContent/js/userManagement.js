/**
 * 
 */


function showUserDetailInfo() {
	 $.post("findLoginUser.action", 
			function(user){
				if(user != null && user.id != null) {
					$("#showUsreTable #account").text(user["account"]);
					$("#showUsreTable #name").text(user["name"]);
					$("#showUsreTable #password").text(user["password"]);
					$("#showUsreTable #address").text(user["address"]);
					$("#showUsreTable #birthday").text(user["birthday"]);
					$("#showUsreTable #department").text(user["department"]);
					$("#showUsreTable #email").text(user["email"]);
					$("#showUsreTable #image_relative_path").attr("src",user["image_relative_path"]);
					
					$("#showUsreTable #phone").text(user["phone"]);
					$("#showUsreTable #sex").text(user["sex"]);
				}
			});
}

function uploadUserImage(){
	$.post("findLoginUser.action", 
   			function(user){
				if(user != null && user.id != null) {
					$("#crop_now").attr("src",user.image_relative_path);
					$("#crop_preview").attr("src",user.image_relative_path);
					
					$("#user_id").val(user.id);
					$("#image_relative_path").val(user.image_relative_path);
				}
					
    		}
	);
	
	$("#file_upload").uploadify({
        //开启调试
        'debug' : false,
        //是否自动上传
        'auto':false,
        //超时时间
        'successTimeout':99999,
        //附带值
        'formData':{
            'userid':'用户id',
            'username':'用户名',
            'rnd':'加密密文'
        },
        //flash
        'swf': "js/uploadify.swf",
        //不执行默认的onSelect事件
        'overrideEvents' : ['onDialogClose'],
        //文件选择后的容器ID
        'queueID':'uploadfileQueue',
        //服务器端脚本使用的文件对象的名称 $_FILES个['upload']
        'fileObjName':'uploadImage',
        //上传处理程序
        'uploader':'uploadImageForUser.action',
        //浏览按钮的背景图片路径
    //    'buttonImage':'../../css/upbutton.png',
        //浏览按钮的宽度
        'buttonText': '选择图片',
		'buttonClass': 'browser_file',
		'removeCompleted': true,
        'width':'100',
        //浏览按钮的高度
        'height':'32',
        'cancelImg': 'WebSecurityServer/css/uploadify-cancel.png',//取消图片路径
        //expressInstall.swf文件的路径。
        'expressInstall':'js/expressInstall.swf',
        //在浏览窗口底部的文件类型下拉菜单中显示的文本
        'fileTypeDesc':'支持的格式：',
        //允许上传的文件后缀
        'fileTypeExts':'*.jpg;*.jpge;*.gif;*.png',
        //上传文件的大小限制
        'fileSizeLimit':'3MB',
        //上传数量
        'queueSizeLimit' : 1,
        //每次更新上载的文件的进展
        'onUploadProgress' : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
             //有时候上传进度什么想自己个性化控制，可以利用这个方法
             //使用方法见官方说明
        },
        //选择上传文件后调用
        'onSelect' : function(file) {
                alert(file);   
        },
        //返回一个错误，选择文件的时候触发
        'onSelectError':function(file, errorCode, errorMsg){
            switch(errorCode) {
                case -100:
                    alert("上传的文件数量已经超出系统限制的"+$('#file_upload').uploadify('settings','queueSizeLimit')+"个文件！");
                    break;
                case -110:
                    alert("文件 ["+file.name+"] 大小超出系统限制的"+$('#file_upload').uploadify('settings','fileSizeLimit')+"大小！");
                    break;
                case -120:
                    alert("文件 ["+file.name+"] 大小异常！");
                    break;
                case -130:
                    alert("文件 ["+file.name+"] 类型不正确！");
                    break;
            }
        },
        //检测FLASH失败调用
        'onFallback':function(){
            alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
        },
        //上传到服务器，服务器返回相应信息到data里
        'onUploadSuccess':function(file, data, response){
        	//data is string here,need to parse to json object.
        	 var jsonData = eval('(' + data + ')');
        	
            if(jsonData != null && jsonData.image_relative_path != null) {
            	var user = jsonData;
            	
            	//reload jcrop
            	//jcrop_api.disable();
            	if(jcrop_api == null || jcrop_api == undefined) {
            		jQuery('#crop_now').Jcrop({
	        			aspectRatio:4/4,
	        			onChange:showCoords,
	        			onSelect:showCoords,
	        			onRelease:releaseCrop,
	        			maxSize:[200,200]
	        		},function(){
	        		    jcrop_api = this;
	        		  });	
            	}
            	
            	jcrop_api.destroy();
            	
            	
            	$("#crop_now").attr("src",user.image_relative_path);
            	$("#crop_preview").attr("src",user.image_relative_path);
            	$("#image_relative_path").val(user.image_relative_path);
            	$("#user_id").val(user.id);
            	
            	//init again
            	jQuery('#crop_now').Jcrop({
        			aspectRatio:4/4,
        			onChange:showCoords,
        			onSelect:showCoords,
        			onRelease:releaseCrop,
        			maxSize:[200,200]
        		},function(){
        		    jcrop_api = this;
        		  });	
            	jcrop_api.enable();
            }else {
            	//failed to upload image.
            	
            }
        }
    });
	
	
	jcrop_api = {};
	//记得放在jQuery(window).load(...)内调用，否则Jcrop无法正确初始化
	$("#crop_now").Jcrop({
		aspectRatio:4/4,
		onChange:showCoords,
		onSelect:showCoords,
		onRelease:releaseCrop,
		maxSize:[200,200]
	},function(){
	    jcrop_api = this;
	  });	
	//简单的事件处理程序，响应自onChange,onSelect事件，按照上面的Jcrop调用
	function showCoords(obj){
		$("#x").val(obj.x);
		$("#y").val(obj.y);
		$("#w").val(obj.w);
		$("#h").val(obj.h);
		if(parseInt(obj.w) > 0){
			//计算预览区域图片缩放的比例，通过计算显示区域的宽度(与高度)与剪裁的宽度(与高度)之比得到
			var rx = $("#preview_box").width() / obj.w; 
			var ry = $("#preview_box").height() / obj.h;
			//通过比例值控制图片的样式与显示
			$("#crop_preview").css({
				width:Math.round(rx * $("#crop_now").width()) + "px",	//预览图片宽度为计算比例值与原图片宽度的乘积
				height:Math.round(rx * $("#crop_now").height()) + "px",	//预览图片高度为计算比例值与原图片高度的乘积
				marginLeft:"-" + Math.round(rx * obj.x) + "px",
				marginTop:"-" + Math.round(ry * obj.y) + "px"
			});
		}
	}
	
	function releaseCrop(obj){
		
	}
	
	$("#crop_submit").click(function(){
		if(parseInt($("#x").val())){
			jcrop_api.release();
			$("#crop_form").submit();	
		}else{
			alert("要先在图片上划一个选区再单击确认剪裁的按钮哦！");	
		}
	});
	
}


function parseUserGridHtml() {
		$.post("findUser.action", 
			{"user.enabled": true}, 
			function(matchedusers){
	if(matchedusers != null && matchedusers.result != null){
	users = matchedusers.result;
	}
	
	var imageRenderer = function (row, datafield, value) {
	return '<img style="margin-left: 5px;" height="60" width="50" src="' + value + '"/>';
	};
	var userSize = users.length;
	var userData = {};
	
	var columns = {};
	
	var datafields = {};
	for(var i = 0;i < userSize; i++) {
	user = users[i];
	if(i==0) {
	var j=0;
	
	for(var item in user) {
		var datafield = {};
		var column = {};
		
		if(item == "id"){
			datafield["type"] = "number";
			column["text"] = "Id";
			
		}else if(item == "modifyTime") {
			datafield["type"] = "number";
			column["text"] = "ModifyTime";
			
		}else if(item == "image_size") {
			datafield["type"] = "number";
			column["text"] = "Image_size";
			
		}else if(item == "image_relative_path") {
			datafield["type"] = "string";
			column["text"] = "Image_relative_path";
			column["cellsrenderer"] = imageRenderer;
		}else if(item == "account") {
			datafield["type"] = "string";
			column["text"] = "Account";
			
		}else if(item == "name") {
			datafield["type"] = "string";
			column["text"] = "Name";
			
		}else if(item == "password") {
			datafield["type"] = "string";
			column["text"] = "Password";
			
		}else if(item == "phone") {
			datafield["type"] = "string";
			column["text"] = "Phone";
			
		}else if(item == "sex") {
			datafield["type"] = "number";
			column["text"] = "Sex";
			
		}else if(item == "email") {
			datafield["type"] = "string";
			column["text"] = "Email";
			
		}else if(item == "address") {
			datafield["type"] = "string";
			column["text"] = "Address";
			
		}else if(item == "birthday") {
			datafield["type"] = "number";
			column["text"] = "Birthday";
			
		}else if(item == "description") {
			datafield["type"] = "string";
			column["text"] = "Description";
			
		}else if(item == "department") {
			datafield["type"] = "number";
			column["text"] = "Department";
			
		}else if(item == "sub_system") {
			datafield["type"] = "number";
			column["text"] = "Sub_system";
			
		}else if(item == "role_Details" || item == "image" || item == "image_absolute_path" || item == "enabled"){
			//do nothing
		}else {
			datafield["type"] = "string";
			column["text"] = "XX";
		}
		
		if(item == "role_Details" || item == "image" || item == "image_absolute_path" || item == "enabled"){
			
		}else {
			column["datafield"] = item;
			if(item == "id") {
				column["width"] = "50";
			}
			
			columns[j] = column;
			
			datafields[j] = datafield;
			j++;
		}
	}
	}
	
	var rowData = {};
	for(var item in user) {
	if(item == "role_Details" || item == "image" || item == "image_absolute_path" || item == "enabled"){
			
	}else {
		rowData[item] = user[item];
	}
	
	}
	
	userData[i] = rowData;
	}
	
	
	var theme = getDemoTheme();
	
	var source =
	{
	    localdata: userData,
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
	$("#userDataGrid").jqxGrid(
	{
	    width: 1000,
	    height: 350,
	    source: dataAdapter,
	    theme: theme,
	    selectionmode: 'multiplerowsextended',
	    sortable: true,
	    pageable: true,
	    autoheight: true,
	    columnsresize: true,
	  //  columnsreorder: true,
	    columns: columns
	});
	$("#addUserRowButton").jqxButton({ theme: theme });
	$("#deleteUserRowButton").jqxButton({ theme: theme });
	$("#updateUserRowButton").jqxButton({ theme: theme });
	$("#addUserTable #cancel").jqxButton({ theme: theme });
	$("#updateUserTable #cancel").jqxButton({ theme: theme });
	$("#saveUserButton").jqxButton({ theme: theme });
	$("#addUserButton").jqxButton({ theme: theme });
	// update row.
	$("#updateUserRowButton").on('click', function () {
		selectedupdaterowindex = $("#userDataGrid").jqxGrid('getselectedrowindex');
		//id = $("#userDataGrid").jqxGrid('getrowid', selectedrowindex);
	    rowData = $('#userDataGrid').jqxGrid('getrowdata', selectedupdaterowindex);
	    
		
		if(rowData != null) {
			var offset = $("#userDataGrid").offset();
			$("#updateUserPopupWindow").jqxWindow({ position: { x: parseInt(offset.left) + 60, y: parseInt(offset.top) + 60 } });
			
			//get old data in popupwindow
			$("#updateUserTable #id").val(rowData["id"]);
			$("#updateUserTable #account").val(rowData["account"]);
			$("#updateUserTable #name").val(rowData["name"]);
			$("#updateUserTable #password").val(rowData["password"]);
			$("#updateUserTable #address").val(rowData["address"]);
			$("#updateUserTable #birthday").val(rowData["birthday"]);
			$("#updateUserTable #department").val(rowData["department"]);
			$("#updateUserTable #email").val(rowData["email"]);
			$("#updateUserTable #user_image").attr("src","../../" + rowData["image_relative_path"]);
			
			$("#updateUserTable #image_relative_path").val(rowData["image_relative_path"]);
			$("#updateUserTable #image_size").val(rowData["image_size"]);
			$("#updateUserTable #phone").val(rowData["phone"]);
			$("#updateUserTable #sex").val(rowData["sex"]);
			
			// show the popup window.
			$("#updateUserPopupWindow").jqxWindow('open');
		}
	    
	    
	});
	// show the popup window
	$("#addUserRowButton").on('click', function () {
		var offset = $("#userDataGrid").offset();
			$("#addUserPopupWindow").jqxWindow({ position: { x: parseInt(offset.left) + 60, y: parseInt(offset.top) + 60 } });
			
			// show the popup window.
			$("#addUserPopupWindow").jqxWindow('open');
	});
	
	// create new row
	$("#addUserButton").on('click', function () {
	    if($("#addUserTable #account").val() != null && $("#addUserTable #password").val() != null){
	    	var newrow = {
	    		"account": $("#addUserTable #account").val(), 
	        	"name": $("#addUserTable #name").val(),
	            "password": $("#addUserTable #password").val(),
	           	"address": $("#addUserTable #address").val(),
	            "birthday": $("#addUserTable #birthday").val(),
	            "department": $("#addUserTable #department").val(),
	            "email": $("#addUserTable #email").val(),
	            "image_relative_path": $("#addUserTable #image_relative_path").val(),
	            "image_size": $("#addUserTable #image_size").val(),
	            "phone": $("#addUserTable #phone").val(),
	            "sex": $("#addUserTable #sex").val()
	        };
	        var addUserData = { 
	        	"user.account": $("#addUserTable #account").val(), 
	        	"user.name": $("#addUserTable #name").val(),
	            "user.password": $("#addUserTable #password").val(),
	           	"user.address": $("#addUserTable #address").val(),
	            "user.birthday": $("#addUserTable #birthday").val(),
	            "user.department": $("#addUserTable #department").val(),
	            "user.email": $("#addUserTable #email").val(),
	            "user.image_relative_path": $("#addUserTable #image_relative_path").val(),
	            "user.image_size": $("#addUserTable #image_size").val(),
	            "user.phone": $("#addUserTable #phone").val(),
	            "user.sex": $("#addUserTable #sex").val()
	        };
	        
	        $.post("registerUser.action", addUserData,function(result){
				if(result != null && result["id"] != null){
	            	//var rowID = $('#userDataGrid').jqxGrid('getrowid', rowData);
	            	newrow["id"] = result["id"];
	                $('#userDataGrid').jqxGrid('addrow', null, newrow);
	                $("#addUserPopupWindow").jqxWindow('hide');
	                
	                addUserData = null;
	                newrow = null;
	                
	                //empty input value
	                $("#addUserTable #account").val("");
	                $("#addUserTable #name").val("");
	                $("#addUserTable #password").val("");
	                $("#addUserTable #address").val("");
	                $("#addUserTable #birthday").val("");
	                $("#addUserTable #department").val("");
	                $("#addUserTable #email").val("");
	                $("#addUserTable #image_relative_path").val("");
	                $("#addUserTable #image_size").val("");
	                $("#addUserTable #phone").val("");
	                $("#addUserTable #sex").val("");
	                
	            }else if(result != null && result["executeResult"] != null && result["executeResult"] == false){
	            	//do nothing
	            }
			});
	    }
	});
	
	// delete row.
	$("#deleteUserRowButton").on('click', function () {
	    var selectedrowindex = $("#userDataGrid").jqxGrid('getselectedrowindex');
	    var rowscount = $("#userDataGrid").jqxGrid('getdatainformation').rowscount;
	    if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
	        //var id = $("#userDataGrid").jqxGrid('getrowid', selectedrowindex);
	        
	        var rowData = $('#userDataGrid').jqxGrid('getrowdata', selectedrowindex);
	        $.post("removeUser.action", {"user.id": rowData["id"]},function(result){
				if(result != null && result["executeResult"] != null && result["executeResult"] == true){
	            	var commit = $("#userDataGrid").jqxGrid('deleterow', selectedrowindex);
	            }
			});
	        
	    }
	});
	
	 // initialize the popup window and buttons.
	$("#addUserPopupWindow").jqxWindow({
	    width: 350, resizable: false, theme: theme, isModal: true, autoOpen: false, cancelButton: $("#addUserTable #cancel"), modalOpacity: 0.01           
	});
	$("#addUserPopupWindow").on('open', function () {
	    //$("#id").jqxInput('selectAll');
	});
	
	 // initialize the popup window and buttons.
	$("#updateUserPopupWindow").jqxWindow({
	    width: 350, resizable: false, theme: theme, isModal: true, autoOpen: false, cancelButton: $("#updateUserTable #cancel"), modalOpacity: 0.01           
	});
	$("#updateUserPopupWindow").on('open', function () {
	    $("#id").jqxInput('selectAll');
	});
	
	// update the edited row when the user clicks the 'Save' button.
	$("#saveUserButton").click(function () {
	    if (rowData != null && rowData["id"] != null) {
			
	        var newrow = { 
	        	"id": $("#updateUserTable #id").val(), 
	        	"account": $("#updateUserTable #account").val(), 
	        	"name": $("#updateUserTable #name").val(),
	            "password": $("#updateUserTable #password").val(),
	           	"address": $("#updateUserTable #address").val(),
	            "birthday": $("#updateUserTable #birthday").val(),
	            "department": $("#updateUserTable #department").val(),
	            "email": $("#updateUserTable #email").val(),
	            "image_relative_path": $("#updateUserTable #image_relative_path").val(),
	            "image_size": $("#updateUserTable #image_size").val(),
	            "phone": $("#updateUserTable #phone").val(),
	            "sex": $("#updateUserTable #sex").val()
	        };
	        var updateUserData = { 
	        	"user.id": $("#updateUserTable #id").val(), 
	        	"user.account": $("#updateUserTable #account").val(), 
	        	"user.name": $("#updateUserTable #name").val(),
	            "user.password": $("#updateUserTable #password").val(),
	           	"user.address": $("#updateUserTable #address").val(),
	            "user.birthday": $("#updateUserTable #birthday").val(),
	            "user.department": $("#updateUserTable #department").val(),
	            "user.email": $("#updateUserTable #email").val(),
	            "user.image_relative_path": $("#updateUserTable #image_relative_path").val(),
	            "user.image_size": $("#updateUserTable #image_size").val(),
	            "user.phone": $("#updateUserTable #phone").val(),
	            "user.sex": $("#updateUserTable #sex").val()
	        };
	        
	        $.post("updateUser.action", updateUserData,function(result){
				if(result != null && result["id"] != null){
	            	//var rowID = $('#userDataGrid').jqxGrid('getrowid', rowData);
	                $('#userDataGrid').jqxGrid('updaterow', selectedupdaterowindex, newrow);
	                $("#updateUserPopupWindow").jqxWindow('hide');
	            }else if(result != null && result["executeResult"] != null && result["executeResult"] == false){
	            	//do nothing
	            }
			});
	         
	        
	    }
	});
	/*
	 $("#userDataGrid").on('columnreordered', function (event) {
	    var column = event.args.columntext;
	    var newindex = event.args.newindex
	    var oldindex = event.args.oldindex;
	});
	*/
	});
}



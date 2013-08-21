/**
 * 
 */

function emptyRegisterUserWindow(){
	//init registerUserWindow widget data
	$("#registerUserPhoneInput").jqxMaskedInput({value: null });
	$('#registerUserBirthdayInput').jqxDateTimeInput({value:findDateTime("2000-01-01 00:00:00")});
	
	$("#registerUserSexRadioButton1").jqxRadioButton({checked: true});
    $("#registerUserSexRadioButton2").jqxRadioButton({checked: false});
    $('#acceptInput').jqxCheckBox({checked: false});

    //init registerUserWindow store data
	$("registerUserSexInput").val("男");
	$("#registerUserIdInput").val(null);
	$("#registerUserAccountInput").val(null);
	$("#registerUserRealNameInput").val(null);
	$("#registerUserPasswordInput").val(null);
	$("#registerUserPasswordConfirmInput").val(null);
	$("#registerUserAddressInput").val(null);
	
	$("#registerUserDepartmentInput").val(null);
	
	$("#registerUserEmailInput").val(null);
	$("#registerUserResult").text("");
}

function emptyUpdateUserWindow(){
	//init registerUserWindow widget data
	$("#updateUserPhoneInput").jqxMaskedInput({value: null });
	$('#updateUserBirthdayInput').jqxDateTimeInput({value:findDateTime("2000-01-01 00:00:00")});
	
	$("#updateUserSexRadioButton1").jqxRadioButton({checked: true});
    $("#updateUserSexRadioButton2").jqxRadioButton({checked: false});
    $('#acceptInput').jqxCheckBox({checked: false});

	$("updateUserSexInput").val("男");
	$("#updateUserIdInput").val(null);
	$("#updateUserAccountInput").val(null);
	$("#updateUserRealNameInput").val(null);
	$("#updateUserPasswordInput").val(null);
	$("#updateUserPasswordConfirmInput").val(null);
	$("#updateUserAddressInput").val(null);
	
	$("#updateUserDepartmentInput").val(null);
	
	$("#updateUserEmailInput").val(null);
	$("#updateUserResult").text("");
}

function initUpdateUserElements() {
	var theme = getDemoTheme();
    $("#updateUserDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
    $('#updateUserUpdateButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#updateUserResetButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#updateUserCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
    

    
  //  $("#updateUserAddressInput").jqxMaskedInput({ mask: '省-市-区-街道-门牌号', width: 150, height: 22, theme: theme });
    $("#updateUserPhoneInput").jqxMaskedInput({ mask: '### #### ####', width: 150, height: 25, theme: theme });
    $('.updateUserTextInput').jqxInput({ theme: theme });
    
    var d1 = new Date();
    $('#updateUserBirthdayInput').jqxDateTimeInput({ theme: theme,width: 180, height: 22,formatString: "yyyy/MM/dd HH:mm:ss", value: d1 });
    
	$("#updateUserSexRadioButton1").jqxRadioButton({ width: 70, height: 25,checked: true, theme: theme });
	$("#updateUserSexRadioButton2").jqxRadioButton({ width: 70, height: 25,  theme: theme });
	$("updateUserSexInput").val("男");
    
    
  
    
    var userDepartmentData = [
          //  { value: 1, label: "USER_DEPARTMENT" },
            { value: 2, label: "USER_DEPARTMENT_BUSSINESS" },
            { value: 3, label: "USER_DEPARTMENT_PRODUCTION" },
            { value: 4, label: "USER_DEPARTMENT_FINANCE" },
            { value: 5, label: "USER_DEPARTMENT_PERSONNEL" },
            { value: 6, label: "USER_DEPARTMENT_DEVERLOPE" },
            { value: 7, label: "USER_DEPARTMENT_MANAGEMENT" }
        ];
    
	// Create a jqxComboBox
	$("#updateUserDepartmentCombobox").jqxComboBox({ 
		selectedIndex: 0, 
		source: userDepartmentData, 
		displayMember: "label", 
		valueMember: "value", 
		width: 150, 
		height: 25, 
		theme: theme 
	});
    
    // initialize validator.
    $('#updateUserInfoForm').jqxValidator({
     rules: [
            { input: '#updateUserAccountInput', message: 'Username is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateUserAccountInput', message: 'Your username must be between 3 and 12 characters!', action: 'keyup, blur', rule: 'length=3,12' },
            { input: '#updateUserRealNameInput', message: 'Real Name is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateUserRealNameInput', message: 'Your real name must contain only letters!', action: 'keyup', rule: 'notNumber' },
            { input: '#updateUserRealNameInput', message: 'Your real name must be between 3 and 12 characters!', action: 'keyup', rule: 'length=3,12' },
            
            { input: '#updateUserPasswordInput', message: 'Password is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateUserPasswordInput', message: 'Your password must be between 6 and 20 characters!', action: 'keyup, blur', rule: 'length=6,20' },
            { input: '#updateUserPasswordConfirmInput', message: 'Confirm Password is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateUserPasswordConfirmInput', message: 'Confirm Passwords doesn\'t match!', action: 'keyup, focus', rule: function (input, commit) {
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
                    if (input.val() === $('#updateUserPasswordInput').val()) {
                        return true;
                    }
                    return false;
            	}
            },
            { input: '#updateUserBirthdayInput', message: 'Your birth date must be between 1/1/1900 and ' + ((new Date()).getFullYear() + 1), action: 'valuechanged', rule: function (input, commit) {
                var date = $('#updateUserBirthdayInput').jqxDateTimeInput('value');
                var result = date.getFullYear() >= 1900 && date.getFullYear() <= (new Date()).getFullYear();
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
                return result;
            	}
            },
            { input: '#updateUserEmailInput', message: 'E-mail is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateUserEmailInput', message: 'Invalid e-mail!', action: 'keyup', rule: 'email' }
            ], 
            theme: theme
    });
	
};

function formatUpdateUserElements(rowData) {
    
    var d1 = {};
	if (rowData["birthday"] != null) {
		d1 = findDateTime(rowData["birthday"]);
	}else {
		d1= new Date();
	}
    $('#updateUserBirthdayInput').jqxDateTimeInput({formatString: "yyyy/MM/dd HH:mm:ss", value: d1 });
    
    if(rowData["sex"] != null && rowData["sex"] == '女') {
    	$("#updateUserSexRadioButton1").jqxRadioButton({checked: false});
    	$("#updateUserSexRadioButton2").jqxRadioButton({checked: true});
    }else {
    	$("#updateUserSexRadioButton1").jqxRadioButton({checked: true});
    	$("#updateUserSexRadioButton2").jqxRadioButton({checked: false});
    }
    
    
    $("#updateUserIdInput").val(rowData["id"]);
	$("#updateUserAccountInput").val(rowData["account"]);
	$("#updateUserRealNameInput").val(rowData["name"]);
	$("#updateUserPasswordInput").val(rowData["password"]);
	$("#updateUserPasswordConfirmInput").val(rowData["password"]);
	$("#updateUserAddressInput").val(rowData["address"]);
	
	$("#updateUserDepartmentInput").val(rowData["department"]);
	
	$("#updateUserEmailInput").val(rowData["email"]);
	$("#updateUserPhoneInput").val(rowData["phone"]);
	$("#updateUserSexInput").val(rowData["sex"]);
    
	// Create a jqxComboBox
	$("#updateUserDepartmentCombobox").jqxComboBox({ selectedIndex: findDepartmentValue($("#updateUserDepartmentInput").val()) - 2});
}

function addUpdateUserEventListeners() {
	$('#updateUserPopupWindow').on('close', function (event) { 
		emptyUpdateUserWindow();
		$('#updateUserInfoForm').jqxValidator('hide');
      //  $('#updateUserPopupWindow').jqxWindow('close');
	});
	$('#updateUserUpdateButton').on('click', function () {
        $('#updateUserInfoForm').jqxValidator('validate');
    });
    
    $("#updateUserSexRadioButton1").on('change', function (event) {
        var checked = event.args.checked;
        if (checked) {
        	 $("#updateUserSexInput").val("男");
        }
    });
    $("#updateUserSexRadioButton2").on('change', function (event) {
        var checked = event.args.checked;
        if (checked) {
        	$("#updateUserSexInput").val("女");
        }
    });
    
    $("#updateUserCancelButton").on('click', function (event) {
    	$('#updateUserInfoForm').jqxValidator('hide');
        $('#updateUserPopupWindow').jqxWindow('close');
    });
        
    $("#updateUserDepartmentCombobox").on('select', function (event) {
            if (event.args) {
                var item = event.args.item;
                if (item) {
                	$("#updateUserDepartmentInput").val(item.value);
                }
            }
        });
    
    $('#updateUserInfoForm').on('validationSuccess', function (event) { 
    	// Some code here. 
    	updateUser();
    });
}

function initUpdateUserWindow(rowData,position) {
	$("#updateUserPopupWindow").removeAttr("style");
	
	var theme = getDemoTheme();
	
	formatUpdateUserElements(rowData);
	
	// initialize the popup window and buttons.
    $("#updateUserPopupWindow").jqxWindow({
    	position:position, isModal: true,width: 350, height: 450, resizable: true, theme: theme, cancelButton: $("#updateUserCancelButton"), modalOpacity: 0.01,
    	initContent: function () {
            $('#updateUserPopupWindow').jqxWindow('focus');
        }
    });
    
    $("#updateUserPopupWindow").jqxWindow('open');
}

function updateUser() {

	var updateUserUIData = {
		"user.id" : $("#updateUserIdInput").val(),
		"user.account" : $("#updateUserAccountInput").val(),
		"user.name" : $("#updateUserRealNameInput").val(),
		"user.password" : $("#updateUserPasswordConfirmInput").val(),
		"user.address" : $("#updateUserAddressInput").val(),
		"user.birthday" : $('#updateUserBirthdayInput').jqxDateTimeInput('value'),
		"user.department" : $("#updateUserDepartmentInput").val(),
		"user.email" : $("#updateUserEmailInput").val(),
		"user.phone" : $("#updateUserPhoneInput").val(),
		"user.sex" : $("#updateUserSexInput").val()
	};
	
	var updateUserData = parseUIDataToUserData(updateUserUIData);

	$.post("updateUser.action", updateUserData, function(result) {
		if (result != null && result["id"] != null) {
			
			$("#updateUserResult").text("update user successfully!");
			$("#updateUserPopupWindow").jqxWindow('close');
			
			var userUIResult = parseUserDataToUIData(result);
			
			//update row in grid
			var selectedrowindex = $("#userDataGrid").jqxGrid('getselectedrowindex');
            var rowscount = $("#userDataGrid").jqxGrid('getdatainformation').rowscount;
            if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
            	var id = $("#userDataGrid").jqxGrid('getrowid', selectedrowindex);
                var commit = $("#userDataGrid").jqxGrid('updaterow', id, userUIResult);
                $("#userDataGrid").jqxGrid('ensurerowvisible', selectedrowindex);
            }
			
			$("#eventLog").text("update user successfully!");

		} else if (result != null && result["executeResult"] != null
				&& result["executeResult"] == false) {
			$("#updateUserResult").text("update user failed,please check user info!");
		}
	});
}



function initRegisterUserElements() {
	var theme = getDemoTheme();
    $("#registerUserDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
    $('#registerUserRegisterButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#registerUserResetButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#registerUserCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
    
    $('#acceptInput').jqxCheckBox({ width: 130, theme: theme ,checked: false});
    
    
    $("#registerUserAddressInput").jqxMaskedInput({ mask: '省-市-区-街道-门牌号', width: 150, height: 22, theme: theme });
    $("#registerUserPhoneInput").jqxMaskedInput({ mask: '### #### ####', width: 150, height: 22, theme: theme });
    $('.registerUserTextInput').jqxInput({ theme: theme });
    var date = findDateTime("2000-01-01 00:00:00");
    $('#registerUserBirthdayInput').jqxDateTimeInput({ theme: theme,width: 180, height: 22,formatString: "yyyy/MM/dd HH:mm:ss", value: $.jqx._jqxDateTimeInput.getDateTime(date) });
    $("#registerUserSexRadioButton1").jqxRadioButton({ width: 70, height: 25, checked: true, theme: theme });
    $("#registerUserSexRadioButton2").jqxRadioButton({ width: 70, height: 25, theme: theme });
    
    var userDepartmentData = [
                              //  { value: 1, label: "USER_DEPARTMENT" },
                                { value: 2, label: "USER_DEPARTMENT_BUSSINESS" },
                                { value: 3, label: "USER_DEPARTMENT_PRODUCTION" },
                                { value: 4, label: "USER_DEPARTMENT_FINANCE" },
                                { value: 5, label: "USER_DEPARTMENT_PERSONNEL" },
                                { value: 6, label: "USER_DEPARTMENT_DEVERLOPE" },
                                { value: 7, label: "USER_DEPARTMENT_MANAGEMENT" }
                            ];
                        
	// Create a jqxComboBox
	$("#registerUserDepartmentCombobox").jqxComboBox({ 
		selectedIndex: 0, 
		source: userDepartmentData, 
		displayMember: "label", 
		valueMember: "value", 
		width: 150, 
		height: 25, 
		theme: theme 
	});
    
    // initialize validator.
    $('#registerUserInfoForm').jqxValidator({
     rules: [
            { input: '#registerUserAccountInput', message: 'Username is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#registerUserAccountInput', message: 'Your username must be between 3 and 12 characters!', action: 'keyup, blur', rule: 'length=3,12' },
            { input: '#registerUserRealNameInput', message: 'Real Name is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#registerUserRealNameInput', message: 'Your real name must contain only letters!', action: 'keyup', rule: 'notNumber' },
            { input: '#registerUserRealNameInput', message: 'Your real name must be between 3 and 12 characters!', action: 'keyup', rule: 'length=3,12' },
            
            { input: '#registerUserPasswordInput', message: 'Password is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#registerUserPasswordInput', message: 'Your password must be between 6 and 20 characters!', action: 'keyup, blur', rule: 'length=6,20' },
            { input: '#registerUserPasswordConfirmInput', message: 'Confirm Password is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#registerUserPasswordConfirmInput', message: 'Confirm Passwords doesn\'t match!', action: 'keyup, focus', rule: function (input, commit) {
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
                    if (input.val() === $('#registerUserPasswordInput').val()) {
                        return true;
                    }
                    return false;
            	}
            },
            { input: '#registerUserBirthdayInput', message: 'Your birth date must be between 1/1/1900 and ' + ((new Date()).getFullYear() + 1), action: 'valuechanged', rule: function (input, commit) {
                var date = $('#registerUserBirthdayInput').jqxDateTimeInput('value');
                var result = date.getFullYear() >= 1900 && date.getFullYear() <= (new Date()).getFullYear();
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
                return result;
            	}
            },
            { input: '#registerUserEmailInput', message: 'E-mail is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#registerUserEmailInput', message: 'Invalid e-mail!', action: 'keyup', rule: 'email' },
            { input: '#acceptInput', message: 'You have to accept the terms', action: 'change', rule: 'required', position: 'right:0,0'}
            ], 
            theme: theme
    });
    
	
};

function addRegisterUserEventListeners() {
	$('#addUserPopupWindow').on('close', function (event) { 
		emptyRegisterUserWindow();
		$('#registerUserInfoForm').jqxValidator('hide');
      //  $('#updateUserPopupWindow').jqxWindow('close');
	});
	
	$('#registerUserRegisterButton').on('click', function () {
        $('#registerUserInfoForm').jqxValidator('validate');
    });
    
    $("#registerUserSexRadioButton1").on('change', function (event) {
        var checked = event.args.checked;
        if (checked) {
        	 $("#registerUserSexInput").val("男");
        }
    });
    $("#registerUserSexRadioButton2").on('change', function (event) {
        var checked = event.args.checked;
        if (checked) {
        	$("#registerUserSexInput").val("女");
        }
    });
    
    $("#registerUserCancelButton").on('click', function (event) {
        $('#addUserPopupWindow').jqxWindow('close');
        
    });
        
    $("#registerUserDepartmentCombobox").on('select', function (event) {
            if (event.args) {
                var item = event.args.item;
                if (item) {
                	$("#registerUserDepartmentInput").val(item.value);
                }
            }
        });
    
    $('#registerUserInfoForm').on('validationSuccess', function (event) { 
    	// Some code here. 
    	registerUser();
    });
}
function initRegisterUserWindow(position) {
	emptyRegisterUserWindow();
	$("#addUserPopupWindow").removeAttr("style");
	$("#addUserPopupWindow").attr("style","overflow:hidden");
	
	var theme = getDemoTheme();
	$("#addUserPopupWindow").jqxWindow({
		position:position,isModal: true,width: 350, height: 450, resizable: true, theme: theme, cancelButton: $("#registerUserCancelButton"), 
    	modalOpacity: 0.01,
    	
    	initContent: function () {
            $('#addUserPopupWindow').jqxWindow('focus');
        }
        
    });
    $("#addUserPopupWindow").jqxWindow('open');
}


function registerUser() {

	var registerUserUIData = {
		"user.account" : $("#registerUserAccountInput").val(),
		"user.name" : $("#registerUserRealNameInput").val(),
		"user.password" : $("#registerUserPasswordConfirmInput").val(),
		"user.address" : $("#registerUserAddressInput").val(),
		"user.birthday" : $("#registerUserBirthdayInput").jqxDateTimeInput('value'),
		"user.department" : $("#registerUserDepartmentInput").val(),
		"user.email" : $("#registerUserEmailInput").val(),
		"user.phone" : $("#registerUserPhoneInput").val(),
		"user.sex" : $("#registerUserSexInput").val()
	};

	var registerUserData = parseUIDataToUserData(registerUserUIData);
	
	$.post("registerUser.action", registerUserData, function(result) {
		if (result != null && result["id"] != null) {
			result = parseUserDataToUIData(result);
			
			$("#registerUserResult").text("register user successfully!");
			$('#addUserPopupWindow').jqxWindow('close');
			
			var commit = $("#userDataGrid").jqxGrid('addrow', null, result);
			
			$("#eventLog").text("register user successfully!");
		} else if (result != null && result["executeResult"] != null
				&& result["executeResult"] == false) {
			$("#registerUserResult").text("register user failed,please check user info!");
		}
	});
}

function showUserDetailInfo() {
	$.post("findLoginUser.action", function(user) {
		if (user != null && user.id != null) {
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

function uploadUserImage() {
	$.post("findLoginUser.action", function(user) {
		if (user != null && user.id != null) {
			$("#crop_now").attr("src", user.image_relative_path);
			$("#crop_preview").attr("src", user.image_relative_path);

			$("#user_id").val(user.id);
			$("#image_relative_path").val(user.image_relative_path);

			initUploadUserImage();
			initJCrop();
		}

	});
};

function initUploadUserImage(){
	
	$(function(){
		$("#file_upload").uploadify({
	        //开启调试
	        'debug' : false,
	        //是否自动上传
	        'auto':true,
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
	        'uploadLimit':100,
	        //服务器端脚本使用的文件对象的名称 $_FILES个['upload']
	        'fileObjName':'uploadFile',
	        //上传处理程序
	        'uploader':'uploadFile.action',
	        //浏览按钮的背景图片路径
	    //    'buttonImage':'../../css/upbutton.png',
	        //浏览按钮的宽度
	        'buttonText': '选择图片',
			'buttonClass': 'browser_file',
			'removeCompleted': true,
			'removeTimeout':1,
	        'width':'100',
	        //浏览按钮的高度
	        'height':'25',
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
	        'multi':false,
	        //每次更新上载的文件的进展
	        'onUploadProgress' : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
	             //有时候上传进度什么想自己个性化控制，可以利用这个方法
	             //使用方法见官方说明
	        },
	        'onUploadError' : function(file, errorCode, errorMsg, errorString) {
	            alert('The file ' + file.name + ' could not be uploaded: ' + errorString);
	        },
	        //选择上传文件后调用
	        'onSelect' : function(file) {
	              //  alert(file);   
	        },
	        'onCancel': function(file) {
	        	//clear all upload files in queue
	        	$('#file_upload').uploadify('cancel','*');
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
	        	
	            if(jsonData != null && jsonData.fileRelativePath != null && response == true) {
	            	var user = jsonData;
	            	
	            	//reload jcrop
	            	//jcrop_api.disable();
	            	if(jcrop_api == null || jcrop_api == undefined) {
	            		initJCrop();
	            	}
	            	
	            	jcrop_api.destroy();
	            	
	            	
	            	$("#crop_now").attr("src",user.fileRelativePath);
	            	$("#crop_preview").attr("src",user.fileRelativePath);
	            	$("#image_relative_path").val(user.fileRelativePath);
	 //           	$("#user_id").val(user.id);
	            	
	            	//init again
	            	jQuery('#crop_now').Jcrop({
	        			aspectRatio:4/4,
	        			onChange:showPreview,
	        			onSelect:showPreview,
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
	});
};

function initJCrop() {
		jcrop_api = {};
		//记得放在jQuery(window).load(...)内调用，否则Jcrop无法正确初始化
		$("#crop_now").Jcrop({
			aspectRatio:4/4,
			onChange:showPreview,
			onSelect:showPreview,
			onRelease:releaseCrop,
			maxSize:[200,200]
		},function(){
		    jcrop_api = this;
		  });
		
	$("#crop_submit").click(function(){
		if(parseInt($("#x").val())){
			jcrop_api.release();
			$("#crop_form").submit();	
		}else{
			alert("要先在图片上划一个选区再单击确认剪裁的按钮哦！");	
		}
	});

};

function showPreview(coords){
	$("#x").val(coords.x);
	$("#y").val(coords.y);
	$("#w").val(coords.w);
	$("#h").val(coords.h);
        if(parseInt(coords.w) > 0){
            //计算预览区域图片缩放的比例，通过计算显示区域的宽度(与高度)与剪裁的宽度(与高度)之比得到
            var rx = $("#preview_box").width() / coords.w; 
            var ry = $("#preview_box").height() / coords.h;
            //通过比例值控制图片的样式与显示
            $("#crop_preview").css({
                width:Math.round(rx * $("#crop_now").width()) + "px",	//预览图片宽度为计算比例值与原图片宽度的乘积
                height:Math.round(rx * $("#crop_now").height()) + "px",  //预览图片高度为计算比例值与原图片高度的乘积
                marginLeft:"-" + Math.round(rx * coords.x) + "px",
                marginTop:"-" + Math.round(ry * coords.y) + "px"
            });
        }
    }

function releaseCrop(obj){
	
}

function initOperateUserGridElements() {
	var theme = getDemoTheme();
	$("#addUserRowButton").jqxButton({ theme: theme });
	$("#deleteUserRowButton").jqxButton({ theme: theme });
	$("#updateUserRowButton").jqxButton({ theme: theme });
}	

function addOperateUserGridEventListeners() {
	// update row.
	$("#updateUserRowButton").on('click', function () {
		selectedupdaterowindex = $("#userDataGrid").jqxGrid('getselectedrowindex');
		//id = $("#userDataGrid").jqxGrid('getrowid', selectedrowindex);
	    rowData = $('#userDataGrid').jqxGrid('getrowdata', selectedupdaterowindex);
	    
		
		if(rowData != null) {
			var offset = $("#userDataGrid").offset();
			var position = {};
			position.x = parseInt(offset.left) + 200;
			position.y = parseInt(offset.top) - 200;
			
			initUpdateUserWindow(rowData,position);
		}
	    
	    
	});
    
	// show the popup window
	$("#addUserRowButton").on('click', function () {
	//	openContentPage('addUserPopupWindowDiv','page/common/addUserPopupWindow.html','content');
		
		var offset = $("#userDataGrid").offset();
		var position = {};
		position.x = parseInt(offset.left) + 200;
		position.y = parseInt(offset.top) - 200;
		
		
		// show the popup window.
		initRegisterUserWindow(position);
		
			
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
					var id = $("#userDataGrid").jqxGrid('getrowid', selectedrowindex);
                    var commit = $("#userDataGrid").jqxGrid('deleterow', id);
	            	
	            	if(commit != null) {
	            		
	            	}
	            }
			});
	        
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
		user = parseUserDataToUIData(users[i]);
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
					datafield["type"] = "string";
					column["text"] = "Sex";
					
				}else if(item == "email") {
					datafield["type"] = "string";
					column["text"] = "Email";
					
				}else if(item == "address") {
					datafield["type"] = "string";
					column["text"] = "Address";
					
				}else if(item == "birthday") {
					datafield["type"] = "string";
					column["text"] = "Birthday";
					
				}else if(item == "description") {
					datafield["type"] = "string";
					column["text"] = "Description";
					
				}else if(item == "department") {
					datafield["type"] = "string";
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
	    selectionmode:'singlerow',
	    columnsresize: true,
	  //  columnsreorder: true,
	    columns: columns
	});
	
	// display selected row index.
    $("#userDataGrid").on('rowselect', function (event) {
        $("#eventLog").text("select row index : " + event.args.rowindex);
    });
	
	initOperateUserGridElements();
	addOperateUserGridEventListeners();
	
	initUpdateUserElements();
	addUpdateUserEventListeners();
	
	// initialize the popup window and buttons.
	initRegisterUserElements();
	addRegisterUserEventListeners();
	
	/*
	 $("#userDataGrid").on('columnreordered', function (event) {
	    var column = event.args.columntext;
	    var newindex = event.args.newindex
	    var oldindex = event.args.oldindex;
	});
	*/
	});
		
}



function parseUIDataToUserData(record) {
	if(record != null){
		for(var attr in record) {
			if(attr == "user.sex") {
				record[attr] = findSexValue(record[attr]);
			}else if(attr == "user.birthday") {
				record[attr] = record[attr].getTime();
			}
		}
		return record;
	}
	return null;
}

function parseUserDataToUIData(user) {
	if(user != null){
		for(var attr in user) {
			if(attr == "department") {
				user[attr] = findDepartmentString(user[attr]);
			}else if(attr == "sex") {
				user[attr] = findSexString(user[attr]);
			}else if(attr == "birthday") {
				user[attr] = new Date(user[attr]).Format("yyyy-MM-dd HH:mm:ss");
			}
		}
		return user;
	}
	return null;
}
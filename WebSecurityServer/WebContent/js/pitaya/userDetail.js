var userDetail = {
		
		leave : function (){
			var me = this;
			me.emptyUpdateUserTab();
		},

		visit : function (){
			this.init();
		},
		
		init : function(){
			var me = this;
			
			me.initLocaleUserTab();
			me.initUpdateUserElements();
			me.addUpdateUserEventListeners();
			me.showUserDetailInfo();
			
			$('#jqxTabs').jqxTabs('select', 2);
			cropUserPicture.visit();
			$('#jqxTabs').jqxTabs('select', 0);
		},
		
		
		initLocaleUserTab:function (){
			
			$(".updateUserTable").i18n();
			
			$('#updateUserSexRadioButton1').text(i18n.t("userManagement.sex.male"));
			$('#updateUserSexRadioButton2').text(i18n.t("userManagement.sex.female"));
			
			$('#userBasicInfoLabel').text(i18n.t("userManagement.page.userDetail.basicInfo"));
			$('#userAdvancedInfoLabel').text(i18n.t("userManagement.page.userDetail.advancedInfo"));
			$('#userPictrueLabel').text(i18n.t("userManagement.page.userDetail.userPictrue"));
			
			$('#updateUserBasicInfoButton').val(i18n.t("userManagement.button.saveBasicInfo"));
		    $('#updateUserAdvancedInfoButton').val(i18n.t("userManagement.button.saveAdvancedInfo"));
		},
		
		emptyUpdateUserTab:function (){
			
			$("#updateUserPhoneInput").jqxMaskedInput({value: null });
			$('#updateUserBirthdayInput').jqxDateTimeInput({value:AppUtil.findDateTime("2000-01-01 00:00:00")});
			
			$("#updateUserSexRadioButton1").jqxRadioButton({checked: true});
		    $("#updateUserSexRadioButton2").jqxRadioButton({checked: false});
		    
			$("updateUserSexInput").val(i18n.t("userManagement.sex.male"));
			$("#updateUserIdInput").val(null);
			$("#updateUserAccountInput").val(null);
			$("#updateUserRealNameInput").val(null);
			$("#updateUserPasswordInput").val(null);
			$("#updateUserPasswordConfirmInput").val(null);
			$("#updateUserAddressInput").val(null);
			
			$("#updateUserResult").text("");
		},

		initUpdateUserElements:function () {
			
		    $('#updateUserBasicInfoButton').jqxButton({ width: 100, height: 25, theme: theme });
		    $('#updateUserAdvancedInfoButton').jqxButton({ width: 100, height: 25, theme: theme });
		    
		  //  $("#updateUserAddressInput").jqxMaskedInput({ mask: '省-市-区-街道-门牌号', width: 150, height: 22, theme: theme });
		    $("#updateUserPhoneInput").jqxMaskedInput({ mask: '### #### ####', width: 150, height: 25, theme: theme });
		    $('.updateUserTextInput').jqxInput({width: 150, height: 25, theme: theme });
		    $('#updateUserAccountInput').jqxInput({disabled: true });
		    
		    var d1 = new Date();
		    $('#updateUserBirthdayInput').jqxDateTimeInput({ theme: theme,width: 150, height: 22,formatString: "yyyy/MM/dd", value: d1 });
		    
			$("#updateUserSexRadioButton1").jqxRadioButton({ width: 70, height: 25,checked: true, theme: theme });
			$("#updateUserSexRadioButton2").jqxRadioButton({ width: 70, height: 25,  theme: theme });
			$("updateUserSexInput").val(i18n.t("userManagement.sex.male"));
		    
		    // initialize validator.
		    $('#updateUserInfoForm').jqxValidator({
		     rules: [
		            
		            { input: '#updateUserPasswordInput', message: i18n.t("userManagement.validation.message.requirePassword"), action: 'keyup, blur', rule: 'required' },
		            { input: '#updateUserPasswordInput', message: i18n.t("userManagement.validation.message.passwordLength"), action: 'keyup, blur', rule: 'length=6,20' },
		            { input: '#updateUserPasswordConfirmInput', message: i18n.t("userManagement.validation.message.requireConfirmPassword"), action: 'keyup, blur', rule: 'required' },
		            { input: '#updateUserPasswordConfirmInput', message: i18n.t("userManagement.validation.message.NomatchPassword"), action: 'keyup, focus', rule: function (input, commit) {
		                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
		                    if (input.val() === $('#updateUserPasswordInput').val()) {
		                        return true;
		                    }
		                    return false;
		            	}
		            },
		            { input: '#updateUserBirthdayInput', message: i18n.t("userManagement.validation.message.birthdayRange",{year:((new Date()).getFullYear() + 1)}), action: 'valuechanged', rule: function (input, commit) {
		                var date = $('#updateUserBirthdayInput').jqxDateTimeInput('value');
		                var result = date.getFullYear() >= 1900 && date.getFullYear() <= (new Date()).getFullYear();
		                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
		                return result;
		            	}
		            },
		            { input: '#updateUserEmailInput', message: i18n.t("userManagement.validation.message.requireEmail"), action: 'keyup, blur', rule: 'required' },
		            { input: '#updateUserEmailInput', message: i18n.t("userManagement.validation.message.invalidEmail"), action: 'keyup', rule: 'email' }
		            ], 
		            theme: theme
		    });
		    
		    $('#jqxTabs').jqxTabs({ 
		 //   	selectionTracker: true, 
		    	width: '800', 
		    	height: 600, 
		 //   	position: 'top', 
		    	theme: theme 
		    });
		},
		
		formatUpdateUserElements:function (rowData) {
			
		    var d1 = {};
			if (rowData["birthday"] != null) {
				d1 = AppUtil.findDateTime(rowData["birthday"]);
			}else {
				d1= new Date();
			}
		    $('#updateUserBirthdayInput').jqxDateTimeInput({formatString: "yyyy/MM/dd", value: d1 });
		    
		    if(rowData["sex"] != null && rowData["sex"] == i18n.t("userManagement.sex.female")) {
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
			
			$("#updateUserEmailInput").val(rowData["email"]);
			$("#updateUserPhoneInput").val(rowData["phone"]);
			$("#updateUserSexInput").val(rowData["sex"]);
			
			/*
			if(rowData["image_relative_path"] && rowData["image_relative_path"] != '') {
				$("#userPictureFile").val(rowData["image_relative_path"]);
			}else {
				$("#userPictureFile").val('css/no_image.jpg');
			}
		    */
		},

		addUpdateUserEventListeners:function () {
			var me = this;
			$('#uploadImageForUserButton').bind('click', function () {
				var uploadPluginOptions = {
					    url: 'uploadImageForUser.action',
					    dataType : "json",
					    success: function(result) {
					    	if(result && result["id"] != null) {
								alert("恭喜您,上传成功!");
							}else {
								alert("对不起,上传失败!");
							}
							
					    },
					    error : function(result) {  
			                  
			                alert("对不起,上传失败!");  
			            }   
				};
				
				$("#uploadImageForUserForm").ajaxSubmit(uploadPluginOptions);
				
		    });
			
			$('#updateUserBasicInfoButton').bind('click', function () {
		        me.updateUser('basic');
		    });
			
			$('#updateUserAdvancedInfoButton').bind('click', function () {
		        me.updateUser('advanced');
		    });
			
		    
		    $("#updateUserSexRadioButton1").bind('change', function (event) {
		        var checked = event.args.checked;
		        if (checked) {
		        	 $("#updateUserSexInput").val(i18n.t("userManagement.sex.male"));
		        }
		    });
		    $("#updateUserSexRadioButton2").bind('change', function (event) {
		        var checked = event.args.checked;
		        if (checked) {
		        	
		        	$("#updateUserSexInput").val(i18n.t("userManagement.sex.female"));
		        }
		    });
		    
		    
		},

		updateUser:function (infoType) {
			var me = this;
			var updateUserUIData = {};
			if(infoType == "basic") {
				updateUserUIData = {
					"user.id" : $("#updateUserIdInput").val(),
					"user.account" : $("#updateUserAccountInput").val(),
					"user.name" : $("#updateUserRealNameInput").val(),
					"user.password" : $("#updateUserPasswordConfirmInput").val(),
					"user.birthday" : $('#updateUserBirthdayInput').jqxDateTimeInput('value'),
					"user.sex" : $("#updateUserSexInput").val()
				};
			}else if(infoType == "advanced") {
				updateUserUIData = {
					"user.id" : $("#updateUserIdInput").val(),
					"user.address" : $("#updateUserAddressInput").val(),
					"user.email" : $("#updateUserEmailInput").val(),
					"user.phone" : $("#updateUserPhoneInput").val()
				};
			}else if(infoType == "pictrue") {
				
			}else {
				updateUserUIData = {
					"user.id" : $("#updateUserIdInput").val(),
					"user.account" : $("#updateUserAccountInput").val(),
					"user.name" : $("#updateUserRealNameInput").val(),
					"user.password" : $("#updateUserPasswordConfirmInput").val(),
					"user.address" : $("#updateUserAddressInput").val(),
					"user.birthday" : $('#updateUserBirthdayInput').jqxDateTimeInput('value'),
					"user.email" : $("#updateUserEmailInput").val(),
					"user.phone" : $("#updateUserPhoneInput").val(),
					"user.sex" : $("#updateUserSexInput").val()
				};
			}
			
			var updateUserData = userManagement.parseUIDataToUserData(updateUserUIData);
			
			if(infoType == "basic" || infoType == "advanced") {
				AppUtil.request("updateUser.action", updateUserData, function(result){
					if(result && result.id != null){
				    	$("#eventLog").text(i18n.t("userManagement.result.updateSuccess"));
					}else {
						$("#eventLog").text(i18n.t("userManagement.result.updateFail"));
					}
					
				}, function(){
					alert("Fail to update user info!");
				});
				
			}else if(infoType == "pictrue") {
				
			}else {
				
			}
			
		},
		
		showUserDetailInfo:function () {
			var me = this;
			$.post("findLoginUser.action", function(user) {
				
				user = userManagement.parseUserDataToUIData(user);
				
				me.formatUpdateUserElements(user);
			});
		}
		
};

	
	
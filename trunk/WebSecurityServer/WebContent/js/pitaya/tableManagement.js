/**
 * 
 */

function emptyRegisterTableWindow(){
	//init registerTableWindow widget data
	$("#registerTableStatusCombobox").jqxComboBox({ selectedIndex: 0});
	$("#registerTableMinCustomerCountInput").jqxNumberInput('val',0);
	$("#registerTableMaxCustomerCountInput").jqxNumberInput('val',0);
	$("#registerTableIndoorPriceInput").jqxNumberInput('val',0);
	
	//init registerTableWindow store data
	$("#registerTableAddressInput").val(null);
	$("#registerTableMinCustomerCountInput").val(null);
	$("#registerTableMaxCustomerCountInput").val(null);
	$("#registerTableIndoorPriceInput").val(null);
	$("#registerTableStatusInput").val(null);
	$("#registerTableResult").text("");
}

function emptyUpdateTableWindow(){
	$("#updateTableStatusCombobox").jqxComboBox({ selectedIndex: 0});
	$("#registerTableMinCustomerCountInput").jqxNumberInput('val',0);
	$("#registerTableMaxCustomerCountInput").jqxNumberInput('val',0);
	$("#registerTableIndoorPriceInput").jqxNumberInput('val',0);
	
	$("#updateTableAddressInput").val(null);
	$("#updateTableMinCustomerCountInput").val(null);
	$("#updateTableMaxCustomerCountInput").val(null);
	$("#updateTableIndoorPriceInput").val(null);
	$("#updateTableStatusInput").val(null);
	$("#updateTableResult").text("");
}

function initUpdateTableElements() {
	var theme = getDemoTheme();
    $("#updateTableDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
    $('#updateTableUpdateButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#updateTableResetButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#updateTableCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
    
    $('.updateTableTextInput').jqxInput({ theme: theme });
    
	$('#updateTableAddressInput').jqxInput({ theme: theme });
    $('#updateTableMinCustomerCountInput').jqxNumberInput({ width: 70, height: 25,min: 1, max: 20, decimalDigits:0, digits: 2, theme: theme, spinButtons: true});
    $('#updateTableMaxCustomerCountInput').jqxNumberInput({ width: 70, height: 25,min: 1, max: 20, decimalDigits:0, digits: 2, theme: theme, spinButtons: true});
    $('#updateTableIndoorPriceInput').jqxNumberInput({ width: 70, height: 25, digits: 3,symbolPosition: 'right', symbol: '￥', theme: theme, spinButtons: true});
    var tableStatusData = [
                                 { value: 2, label: "TABLE_EMPTY"},
                                 { value: 3, label: "TABLE_BOOKING"},
                                 { value: 4, label: "TABLE_USING"}
                             ];
     
 	$("#updateTableStatusCombobox").jqxComboBox({ 
 		selectedIndex: 0, 
 		source: tableStatusData, 
 		displayMember: "label", 
 		valueMember: "value", 
 		width: 150, 
 		height: 25, 
 		theme: theme 
 	});
    
    
    
    // initialize validator.
    $('#updateTableInfoForm').jqxValidator({
     rules: [
             
             /*
            {input: '#updateTableMaxCustomerCountInput', message: 'Max Customer Count should large than 0', action: 'keyup, focus', rule: function (input, commit) {
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
	                if (0 < $('#updateTableMaxCustomerCountInput').val()) {
	                    return true;
	                }
	                return false;
	        	}
            },
            {input: '#updateTableMaxCustomerCountInput', message: 'Max Customer Count should large than minCustomerCount', action: 'keyup, focus', rule: function (input, commit) {
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
	                if ($('#updateTableMinCustomerCountInput').val() < $('#updateTableMaxCustomerCountInput').val()) {
	                    return true;
	                }
	                return false;
	        	}
            },
            */
            { input: '#updateTableAddressInput', message: 'address is required!', action: 'keyup, blur', rule: 'required' }
            
            /*,
            { input: '#updateTableMinCustomerCountInput', message: 'Min Customer Count should large than 0', action: 'keyup, focus', rule: function (input, commit) {
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
                    if (0 < $('#updateTableMinCustomerCountInput').val()) {
                        return true;
                    }
                    return false;
            	}
            }*/
            ], 
            theme: theme
    });
	
};

function formatUpdateTableElements(rowData) {
    $("#updateTableIdInput").val(rowData["id"]);
    $("#updateTableAddressInput").val(rowData["address"]);
    $("#updateTableMinCustomerCountInput").val(rowData["minCustomerCount"]);
	$("#updateTableMaxCustomerCountInput").val(rowData["maxCustomerCount"]);
	$("#updateTableIndoorPriceInput").val(rowData["indoorPrice"]);
	$("#updateTableStatusInput").val(rowData["status"]);
	
	$("#updateTableMinCustomerCountInput").jqxNumberInput('val',rowData["minCustomerCount"]);
	$("#updateTableMaxCustomerCountInput").jqxNumberInput('val',rowData["maxCustomerCount"]);
	$("#updateTableIndoorPriceInput").jqxNumberInput('val',rowData["indoorPrice"]);
	$("#updateTableStatusCombobox").jqxComboBox({ selectedIndex: findTableStatusValue($("#updateTableStatusInput").val()) - 2});
}

function addUpdateTableEventListeners() {
	$('#updateTablePopupWindow').on('close', function (event) { 
		emptyUpdateTableWindow();
		$('#updateTableInfoForm').jqxValidator('hide');
      //  $('#updateTablePopupWindow').jqxWindow('close');
	});
	$('#updateTableUpdateButton').on('click', function () {
        $('#updateTableInfoForm').jqxValidator('validate');
    });
    
    
    $("#updateTableCancelButton").on('click', function (event) {
    	$('#updateTableInfoForm').jqxValidator('hide');
        $('#updateTablePopupWindow').jqxWindow('close');
    });
        
    $("#updateTableStatusCombobox").on('select', function (event) {
            if (event.args) {
                var item = event.args.item;
                if (item) {
                	$("#updateTableStatusInput").val(item.value);
                }
            }
        });
    
    $('#updateTableInfoForm').on('validationSuccess', function (event) { 
    	// Some code here. 
    	updateTable();
    });
}

function initUpdateTableWindow(rowData,position) {
	$("#updateTablePopupWindow").removeAttr("style");
	
	var theme = getDemoTheme();
	
	formatUpdateTableElements(rowData);
	
	// initialize the popup window and buttons.
    $("#updateTablePopupWindow").jqxWindow({
    	position:position, isModal: true,width: 350, height: 300, resizable: true, theme: theme, cancelButton: $("#updateTableCancelButton"), modalOpacity: 0.01,
    	initContent: function () {
            $('#updateTablePopupWindow').jqxWindow('focus');
        }
    });
    
    $("#updateTablePopupWindow").jqxWindow('open');
}

function updateTable() {

	var updateTableUIData = {
		"table.id" : $("#updateTableIdInput").val(),
		"table.address" : $("#updateTableAddressInput").val(),
		"table.minCustomerCount" : $('#updateTableMinCustomerCountInput').val(),
		"table.maxCustomerCount" : $('#updateTableMaxCustomerCountInput').val(),
		"table.status" : $("#updateTableStatusInput").val(),
		"table.indoorPrice" : $("#updateTableIndoorPriceInput").val()
	};
	
	var updateTableData = parseUIDataToTableData(updateTableUIData);

	$.post("updateTable.action", updateTableData, function(result) {
		if (result != null && result["id"] != null) {
			
			$("#updateTableResult").text("update table successfully!");
			$("#updateTablePopupWindow").jqxWindow('close');
			
			var tableUIResult = parseTableDataToUIData(result);
			
			//update row in grid
			var selectedrowindex = $("#tableDataGrid").jqxGrid('getselectedrowindex');
            var rowscount = $("#tableDataGrid").jqxGrid('getdatainformation').rowscount;
            if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
            	var id = $("#tableDataGrid").jqxGrid('getrowid', selectedrowindex);
                var commit = $("#tableDataGrid").jqxGrid('updaterow', id, tableUIResult);
                $("#tableDataGrid").jqxGrid('ensurerowvisible', selectedrowindex);
            }
			
			$("#eventLog").text("update table successfully!");

		} else if (result != null && result["executeResult"] != null
				&& result["executeResult"] == false) {
			$("#updateTableResult").text("update table failed,please check table info!");
		}
	});
}



function initRegisterTableElements() {
    
	var theme = getDemoTheme();
    $("#registerTableDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
    $('#registerTableRegisterButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#registerTableResetButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#registerTableCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
    
    $('.registerTableTextInput').jqxInput({ theme: theme });
    
	$('#registerTableAddressInput').jqxInput({ theme: theme });
    $('#registerTableMinCustomerCountInput').jqxNumberInput({ width: 70, height: 25,min: 1, max: 20, decimalDigits:0, digits: 2, theme: theme, spinButtons: true});
    $('#registerTableMaxCustomerCountInput').jqxNumberInput({ width: 70, height: 25,min: 1, max: 20, decimalDigits:0, digits: 2, theme: theme, spinButtons: true});
    $('#registerTableIndoorPriceInput').jqxNumberInput({ width: 70, height: 25, digits: 3,symbolPosition: 'right', symbol: '￥', theme: theme, spinButtons: true});
    var tableStatusData = [
                                 { value: 2, label: "TABLE_EMPTY"},
                                 { value: 3, label: "TABLE_BOOKING"},
                                 { value: 4, label: "TABLE_USING"}
                             ];
    
    
	// Create a jqxComboBox
	$("#registerTableStatusCombobox").jqxComboBox({ 
		selectedIndex: 0, 
		source: tableStatusData, 
		displayMember: "label", 
		valueMember: "value", 
		width: 150, 
		height: 25, 
		theme: theme 
	});
    
    // initialize validator.
    $('#registerTableInfoForm').jqxValidator({
     rules: [
            { input: '#registerTableAddressInput', message: 'address is required!', action: 'keyup, blur', rule: 'required' }
            ], 
            theme: theme
    });
    
	
};

function addRegisterTableEventListeners() {
	$('#addTablePopupWindow').on('close', function (event) { 
		emptyRegisterTableWindow();
		$('#registerTableInfoForm').jqxValidator('hide');
      //  $('#updateTablePopupWindow').jqxWindow('close');
	});
	
	$('#registerTableRegisterButton').on('click', function () {
        $('#registerTableInfoForm').jqxValidator('validate');
    });
    
    
    $("#registerTableCancelButton").on('click', function (event) {
        $('#addTablePopupWindow').jqxWindow('close');
        
    });
        
    $("#registerTableStatusCombobox").on('select', function (event) {
            if (event.args) {
                var item = event.args.item;
                if (item) {
                	$("#registerTableStatusInput").val(item.value);
                }
            }
        });
    
    $('#registerTableInfoForm').on('validationSuccess', function (event) { 
    	// Some code here. 
    	registerTable();
    });
}
function initRegisterTableWindow(position) {
	emptyRegisterTableWindow();
	$("#addTablePopupWindow").removeAttr("style");
	$("#addTablePopupWindow").attr("style","overflow:hidden");
	
	var theme = getDemoTheme();
	$("#addTablePopupWindow").jqxWindow({
		position:position,isModal: true,width: 350, height: 300, resizable: true, theme: theme, cancelButton: $("#registerTableCancelButton"), 
    	modalOpacity: 0.01,
    	
    	initContent: function () {
            $('#addTablePopupWindow').jqxWindow('focus');
        }
        
    });
    $("#addTablePopupWindow").jqxWindow('open');
}


function registerTable() {
	var registerTableUIData = {
		"table.id" : $("#registerTableIdInput").val(),
		"table.address" : $("#registerTableAddressInput").val(),
		"table.minCustomerCount" : $('#registerTableMinCustomerCountInput').val(),
		"table.maxCustomerCount" : $('#registerTableMaxCustomerCountInput').val(),
		"table.status" : $("#registerTableStatusInput").val(),
		"table.indoorPrice" : $("#registerTableIndoorPriceInput").val()
	};
	
	var registerTableData = parseUIDataToTableData(registerTableUIData);
	
	
	$.post("addTable.action", registerTableData, function(result) {
		if (result != null && result["id"] != null) {
			result = parseTableDataToUIData(result);
			
			$("#registerTableResult").text("register table successfully!");
			$('#addTablePopupWindow').jqxWindow('close');
			
			var commit = $("#tableDataGrid").jqxGrid('addrow', null, result);
			
			$("#eventLog").text("register table successfully!");
		} else if (result != null && result["executeResult"] != null
				&& result["executeResult"] == false) {
			$("#registerTableResult").text("register table failed,please check table info!");
		}
	});
}

function showTableDetailInfo() {
	$.post("findLoginTable.action", function(table) {
		if (table != null && table.id != null) {
			$("#showUsreTable #account").text(table["account"]);
			$("#showUsreTable #name").text(table["name"]);
			$("#showUsreTable #password").text(table["password"]);
			$("#showUsreTable #address").text(table["address"]);
			$("#showUsreTable #birthday").text(table["birthday"]);
			$("#showUsreTable #department").text(table["department"]);
			$("#showUsreTable #email").text(table["email"]);
			$("#showUsreTable #image_relative_path").attr("src",table["image_relative_path"]);
			$("#showUsreTable #phone").text(table["phone"]);
			$("#showUsreTable #sex").text(table["sex"]);
		}
	});
}

function uploadTableImage() {
	$.post("findLoginTable.action", function(table) {
		if (table != null && table.id != null) {
			$("#crop_now").attr("src", table.image_relative_path);
			$("#crop_preview").attr("src", table.image_relative_path);

			$("#table_id").val(table.id);
			$("#image_relative_path").val(table.image_relative_path);

			initUploadTableImage();
			initJCrop();
		}

	});
};

function initUploadTableImage(){
	
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
	            'tableid':'用户id',
	            'tablename':'用户名',
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
	            	var table = jsonData;
	            	
	            	//reload jcrop
	            	//jcrop_api.disable();
	            	if(jcrop_api == null || jcrop_api == undefined) {
	            		initJCrop();
	            	}
	            	
	            	jcrop_api.destroy();
	            	
	            	
	            	$("#crop_now").attr("src",table.fileRelativePath);
	            	$("#crop_preview").attr("src",table.fileRelativePath);
	            	$("#image_relative_path").val(table.fileRelativePath);
	 //           	$("#table_id").val(table.id);
	            	
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

function initOperateTableGridElements() {
	var theme = getDemoTheme();
	$("#addTableRowButton").jqxButton({ theme: theme });
	$("#deleteTableRowButton").jqxButton({ theme: theme });
	$("#updateTableRowButton").jqxButton({ theme: theme });
}	

function addOperateTableGridEventListeners() {
	// update row.
	$("#updateTableRowButton").on('click', function () {
		selectedupdaterowindex = $("#tableDataGrid").jqxGrid('getselectedrowindex');
		//id = $("#tableDataGrid").jqxGrid('getrowid', selectedrowindex);
	    rowData = $('#tableDataGrid').jqxGrid('getrowdata', selectedupdaterowindex);
	    
		
		if(rowData != null) {
			var offset = $("#tableDataGrid").offset();
			var position = {};
			position.x = parseInt(offset.left) + 200;
			position.y = parseInt(offset.top) - 200;
			
			initUpdateTableWindow(rowData,position);
		}
	    
	    
	});
    
	// show the popup window
	$("#addTableRowButton").on('click', function () {
	//	openContentPage('addTablePopupWindowDiv','page/common/addTablePopupWindow.html','content');
		
		var offset = $("#tableDataGrid").offset();
		var position = {};
		position.x = parseInt(offset.left) + 200;
		position.y = parseInt(offset.top) - 200;
		
		
		// show the popup window.
		initRegisterTableWindow(position);
		
			
	});
	
	// delete row.
	$("#deleteTableRowButton").on('click', function () {
	    var selectedrowindex = $("#tableDataGrid").jqxGrid('getselectedrowindex');
	    var rowscount = $("#tableDataGrid").jqxGrid('getdatainformation').rowscount;
	    if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
	        //var id = $("#tableDataGrid").jqxGrid('getrowid', selectedrowindex);
	        
	        var rowData = $('#tableDataGrid').jqxGrid('getrowdata', selectedrowindex);
	        $.post("removeTable.action", {"table.id": rowData["id"]},function(result){
				if(result != null && result["executeResult"] != null && result["executeResult"] == true){
					var id = $("#tableDataGrid").jqxGrid('getrowid', selectedrowindex);
                    var commit = $("#tableDataGrid").jqxGrid('deleterow', id);
	            	
	            	if(commit != null) {
	            		
	            	}
	            }
			});
	        
	    }
	});
}


function parseTableGridHtml() {
		$.post("searchTable.action", 
			{"table.enabled": true}, 
			function(matchedtables){
				if(matchedtables != null && matchedtables.result != null){
					tables = matchedtables.result;
				}
				
				var tableSize = tables.length;
				var tableData = {};
				
				var columns = {};
				
				var datafields = {};
				for(var i = 0;i < tableSize; i++) {
					table = parseTableDataToUIData(tables[i]);
					if(i==0) {
						var j=0;
						for(var item in table) {
							var datafield = {};
							var column = {};
							
							if(item == "id"){
								datafield["type"] = "number";
								column["text"] = "Id";
								
							}else if(item == "status") {
								datafield["type"] = "string";
								column["text"] = "Staus";
								
							}else if(item == "minCustomerCount") {
								datafield["type"] = "number";
								column["text"] = "MinCustomerCount";
								
							}else if(item == "maxCustomerCount") {
								datafield["type"] = "number";
								column["text"] = "MaxCustomerCount";
							}else if(item == "address") {
								datafield["type"] = "number";
								column["text"] = "Address";
								
							}else if(item == "indoorPrice") {
								datafield["type"] = "string";
								column["text"] = "indoorPrice";
								
							}else if(item == "enabled"){
								//do nothing
							}else {
								datafield["type"] = "string";
								column["text"] = "XX";
							}
							
							if(item == "enabled"){
								
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
					for(var item in table) {
						if(item == "enabled"){
								
						}else {
							rowData[item] = table[item];
						}
					
					}
					
					tableData[i] = rowData;
				}
	
	
	var theme = getDemoTheme();
	
	var source =
	{
	    localdata: tableData,
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
	$("#tableDataGrid").jqxGrid(
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
    $("#tableDataGrid").on('rowselect', function (event) {
        $("#eventLog").text("select row index : " + event.args.rowindex);
    });
	
	initOperateTableGridElements();
	addOperateTableGridEventListeners();
	
	initUpdateTableElements();
	addUpdateTableEventListeners();
	
	// initialize the popup window and buttons.
	initRegisterTableElements();
	addRegisterTableEventListeners();
	
	/*
	 $("#tableDataGrid").on('columnreordered', function (event) {
	    var column = event.args.columntext;
	    var newindex = event.args.newindex
	    var oldindex = event.args.oldindex;
	});
	*/
	});
		
}



function parseUIDataToTableData(record) {
	if(record != null){
		for(var attr in record) {
			if(attr == "table.status") {
				record[attr] = findTableStatusValue(record[attr]);
			}
		}
		return record;
	}
	return null;
}

function parseTableDataToUIData(table) {
	if(table != null){
		for(var attr in table) {
			if(attr == "status") {
				table[attr] = findTableStatusLable(table[attr]);
			}
		}
		return table;
	}
	return null;
}
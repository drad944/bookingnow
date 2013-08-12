var Uploader = {
	
	init : function(filepicker, callback){
		var me = this;
		var config = {		
			//开启调试
	        'debug' : false,
	        //是否自动上传
	        'auto':false,
	        //超时时间
	        'successTimeout':99999,
	        'formData':{
	            'fileId': '',
        	},
	        //flash
	        'swf': "../../js/uploadify.swf",
	        //不执行默认的onSelect事件
	        'overrideEvents' : ['onDialogClose'],
	        //文件选择后的容器ID
	        'queueID':'uploadfileQueue',
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
	        'width':'100',
	        //浏览按钮的高度
	        'height':'32',
	        'cancelImg': '../../css/uploadify-cancel.png',//取消图片路径
	        //expressInstall.swf文件的路径。
	        'expressInstall':'../../js/expressInstall.swf',
	        //在浏览窗口底部的文件类型下拉菜单中显示的文本
	        'fileTypeDesc':'支持的格式：',
	        //允许上传的文件后缀
	        'fileTypeExts':'*.jpg;*.jpge;*.gif;*.png',
	        //上传文件的大小限制
	        'fileSizeLimit':'1MB',
	        //上传数量
	        'queueSizeLimit' : 1,
	        //每次更新上载的文件的进展
	        'onUploadProgress' : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
	             //有时候上传进度什么想自己个性化控制，可以利用这个方法
	             //使用方法见官方说明
	        },
	        //选择上传文件后调用
	        'onSelect' : function(file) {
	              //  alert(file);   
	        },
	        //返回一个错误，选择文件的时候触发
	        'onSelectError':function(file, errorCode, errorMsg){
	            switch(errorCode) {
	                case -100:
	                    alert("上传的文件数量已经超出系统限制的"+$('#'+filepicker).uploadify('settings','queueSizeLimit')+"个文件！");
	                    break;
	                case -110:
	                    alert("文件 ["+file.name+"] 大小超出系统限制的"+$('#'+filepicker).uploadify('settings','fileSizeLimit')+"大小！");
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
	        'onUploadStart' : function(file) {  
	        	if(me.fileId != null && me.fileId != ''){
	        		$("#file_upload").uploadify("settings", "formData", {'fileId': me.fileId});  
	        	}
	        },
	        //上传到服务器，服务器返回相应信息到data里
	        'onUploadSuccess':function(file, data, response){
	        	//data is string here,need to parse to json object.
	        	 var jsonData = eval('(' + data + ')');
	        	
	             if(jsonData != null && jsonData.fileId != "null") {
	            	if(me.jcrop_api != null ) {
	            		me.jcrop_api.disable();
	            		me.jcrop_api.destroy();
	            	}
	            	$("#crop_now").attr("src", "../../images/temp/" + jsonData.fileId);
	            	me.fileId = jsonData.fileId;
	            	me.initJCrop();
	            	if(callback){
	            		callback();
	            	}
	            }else {
	            	//failed to upload image.
	            	
	            }
	        }
	    };
		$("#" + filepicker).uploadify(config);
	},

	initJCrop : function() {
		this.jcrop_api = {};
		//记得放在jQuery(window).load(...)内调用，否则Jcrop无法正确初始化
		var me = this;
		$("#crop_now").Jcrop({
			aspectRatio:16/10,
			onChange: this.showPreview,
			onSelect: this.showPreview,
			onRelease: this.releaseCrop,
			maxSize:[200,200]
		},function(){
		    me.jcrop_api = this;
		});
			
		$("#crop_submit").click(function(){
			if(parseInt($("#x").val())){
				me.jcrop_api.release();
				$("#crop_form").submit();	
			}else{
				alert("要先在图片上划一个选区再单击确认剪裁的按钮哦！");	
			}
		});

	},

	showPreview : function(coords){
		$("#x").val(coords.x);
		$("#y").val(coords.y);
		$("#w").val(coords.w);
		$("#h").val(coords.h);
    },

	releaseCrop : function(obj){
		
	},
	
	clean : function(){
		this.fileId = null;
		if(me.jcrop_api != null ) {
    		me.jcrop_api.disable();
    		me.jcrop_api.destroy();
    	}
	}
	
};
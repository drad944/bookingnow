var cropUserPicture = {
		realImageHeight:-1,
		realImageWidth:-1,
		leave : function (){
			
		},

		visit : function (){
			this.init();
		},
		
		init : function(){
			var me = this;
			me.initCropUserPicture();
		},
		
		initCropUserPicture:function() {
			imagepreview(document.getElementById("file"), document.getElementById("preview"), function(info){
				//alert("文件名:" + info.name + "\r\n图片原始高度:" + info.height + "\r\n图片原始宽度:" + info.width);
				//这里若return false则不预览图片
				cropUserPicture.realImageHeight = info.height;
				cropUserPicture.realImageWidth = info.width;
				
				$("#preview").css({
					background: "none"
				});
			
				$("#preview").crop( function(e){
					if(e.aeraHeight < e.aeraWidth ) {
						$("#scaleRatio").val(cropUserPicture.realImageWidth / e.aeraWidth);
					}else {
						$("#scaleRatio").val(cropUserPicture.realImageHeight / e.aeraHeight);
					}
					$("#x").val(e.left);
					$("#y").val(e.top);
					$("#w").val(e.width);
					$("#h").val(e.height);
					
					//$("input[type='hidden']").val([e.top, e.left, e.height, e.width].toString());
				}, ".thumb");
			});
		}
		
};

	
	
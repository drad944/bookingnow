
function callAction(actionName,parameter) {
		var result = {};
		$.post(actionName, parameter,function(data){
			
			result = data;
			});
		
		return result;
};
	
function openContentPage(sourceDiv,url,targetDiv) {
	var contentPage = $('#' + sourceDiv);
	contentPage.load(url, null, function(data) {
			var tmp = $('<div></div>').html(data);
		 
	        data = tmp.find('#' + targetDiv).html();
	        tmp.remove();
		         
		    contentPage.html(data);
		    data = null;
	    });
};

function removeElementFromPage(targetDiv) {
	var contentPage = $('#' + targetDiv);
	contentPage.remove();
};
	
String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substr(0,str.length)==str)
	  return true;
	else
	  return false;
	return true;
};


function trim(myString){
	if(myString != null && myString != "") {
		return myString.replace(/(^\s*)|(\s*$)/g, "");
	}
	return myString;
};

Date.prototype.Format = function (fmt) {
	//how to call it:
	//var time1 = new Date().Format("yyyy-MM-dd");
	//var time2 = new Date().Format("yyyy-MM-dd HH:mm:ss");
	
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "H+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) {
    	fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o){
    	if (new RegExp("(" + k + ")").test(fmt)) {
    		fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    	}
    }
    
    return fmt;
};



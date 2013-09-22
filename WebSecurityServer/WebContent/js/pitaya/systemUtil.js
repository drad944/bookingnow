function strToJson(str){  
     var json = eval('(' + str + ')');  
     return json;  
}

function getURLParameters(key) {
	var url=location.href; 
	var urlParams = url.split("?")[1]; 
	if(urlParams != null) {
		var params = urlParams.split("&");
		for(var i = 0;i< params.length;i++) {
			var item = params[i];
			if(item != null) {
				var paramMap = item.split("=");
				if(paramMap != null && paramMap.length == 2) {
					if(key == paramMap[0]) {
						return paramMap[1];
					}
				}
			}
		}
	}
	return null;
}

function contains(string,substr,isIgnoreCase)
{
	if(string && substr) {
		
	}else {
		return false;
	}
    if(isIgnoreCase)
    {
     string=string.toLowerCase();
     substr=substr.toLowerCase();
    }
     var startChar=substr.substring(0,1);
     var strLen=substr.length;
         for(var j=0;j<string.length-strLen+1;j++)
         {
             if(string.charAt(j)==startChar)
             {
                   if(string.substring(j,j+strLen)==substr)
                   {
                         return true;
                   }   
             }
         }
         return false;
}

function callAction(actionName,parameter) {
		var result = {};
		$.post(actionName, parameter,function(data){
			
			result = data;
			});
		
		return result;
};

function openSubPage(sourceDiv,url,targetDiv,initFunction,initParam,synchronous) {
	//try to use post instead of load
	if(synchronous != null && synchronous == true) {
		var contentPage = $('#' + sourceDiv);
		$.ajaxSetup({ async: false });
		contentPage.load(url, function(data) {
			var tmp = $('<div></div>').html(data);
		 
	        data = tmp.find('#' + targetDiv).html();
	        tmp.remove();
	        contentPage.html(null);     
		    contentPage.html(data);
		    data = null;
		    if(initFunction) {
		    	if(initParam) {
		    		initFunction.visit(initParam);
		    	}else {
		    		initFunction.visit();
		    	}
		    }
	    });
		$.ajaxSetup({ async: true });
	}else {
		var contentPage = $('#' + sourceDiv);
		contentPage.load(url, function(data) {
			var tmp = $('<div></div>').html(data);
		 
	        data = tmp.find('#' + targetDiv).html();
	        tmp.remove();
	        contentPage.html(null);     
		    contentPage.html(data);
		    data = null;
		    if(initFunction) {
		    	if(initParam) {
		    		initFunction.visit(initParam);
		    	}else {
		    		initFunction.visit();
		    	}
		    }
	    });
	}
	
};
	
function openContentPage(sourceDiv,url,targetDiv) {
	var contentPage = $('#' + sourceDiv);
	contentPage.load(url, function(data) {
			var tmp = $('<div></div>').html(data);
		 
	        data = tmp.find('#' + targetDiv).html();
	        tmp.remove();
	        contentPage.html(null);    
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

Array.prototype.remove=function(dx) 
{ 
    if(isNaN(dx)||dx>this.length){return false;} 
    for(var i=0,n=0;i<this.length;i++) 
    { 
        if(this[i]!=this[dx]) 
        { 
            this[n++]=this[i]; 
        } 
    } 
    this.length-=1;
} 

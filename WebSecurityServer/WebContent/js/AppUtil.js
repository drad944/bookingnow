/**
 * 
 */
var AppUtil = {
		
	setStyle : function(domObj, parameters){
		 for(var p in parameters){ 
			 domObj.style[p] = parameters[p];
		 }
	}
};

function openContentPage(url) {
	var contentPage = $('#framework_main');
	$.post(url,function(data) {
        var tmp = $('<div></div>').html(data);
 
        data = tmp.find('#content').html();
        tmp.remove();
         
        contentPage.html(data);
    });
} 
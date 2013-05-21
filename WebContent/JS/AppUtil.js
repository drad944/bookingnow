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
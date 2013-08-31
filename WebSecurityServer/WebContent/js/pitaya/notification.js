var notification = {
	
	init: function(){
	},
	
	subscribeTopic : function(topic, callback){
		var me = this;
		$.get('subscribeEventOfOrder', null, function(data){
			callback(data);
			me.subscribeTopic(topic, callback);
		});
	},
	
		
    onData : function(event) {   
        if(this.onDataCallback){
        	this.onDataCallback(event);
        }
    },
    
    getXMLHttpRequest : function() {
         var xhr = false;
         if (window.XMLHttpRequest){
             xhr = new XMLHttpRequest();
             if (xhr.overrideMimeType){
                 xhr.overrideMimeType("text/xml");
             }
         }else if (window.ActiveXObject){
             try{
                 xhr = new ActiveXObject("Msxml2.XMLHTTP");
             }catch(e){
                 try{
                     xhr = new ActiceXObject("Microsoft.XMLHTTP");
                 }catch(e){
                	 xhr = false;
                 }
             }
         }
         return xhr;
    }
};
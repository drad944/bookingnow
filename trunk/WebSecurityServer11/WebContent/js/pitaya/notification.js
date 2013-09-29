var notification = {
	
	topics : {},	
		
	subscribeTopic : function(topic, callback){
		if(this.topics[topic] != null){
			return;
		}
		this.topics[topic] = "";
		var me = this;
		$.get('subscribeTopic', {
			'topic' : topic
		}, function(data){
			callback(data);
			me.topics[topic] = null;
			me.subscribeTopic(topic, callback);
		});
	},
	
	unsubscribeTopic : function(topic){
		$.get('unsubscribeTopic', {
			'topic' : topic
		});
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
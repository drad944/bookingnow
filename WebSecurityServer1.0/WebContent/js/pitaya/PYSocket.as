package {
	import flash.display.Sprite;
	import flash.events.*;
	import flash.external.*;
	import flash.net.Socket;
	import flash.system.Security;
	import flash.utils.Timer;

	public class PYSocket extends Sprite
	{
		private var socket:Socket;
		
		public function PYSocket()
		{
			this.jsReadyHandler();
		}
		
		private function isJavaScriptReady():Boolean {
	  		var isReady:Boolean = ExternalInterface.call("isJSReady");
	  	  	return isReady;
	    }
	  	  	  
	    private function jsReadyHandler():void {
	  	    ExternalInterface.addCallback("connect", connect);
	  	    ExternalInterface.addCallback("sendData", sendData);
	  	    ExternalInterface.call("flashReadyHandler");
	    }
		
		private function addExternalInterface():void {
  	  	  	//if(ExternalInterface.available){
  	  	  	  try {
  	  	  	  	  if (isJavaScriptReady()) {
  	  	  	  	  	  jsReadyHandler();
  	  	  	  	  }else{
  	  	  	  	  	  var timerReady:Timer = new Timer(100, 0);
  	  	  	  	  	  timerReady.addEventListener(TimerEvent.TIMER, function(evt:TimerEvent):void {
  	  	  	  	  	  	  trace("checking...");
  	  	  	  	  	  	  if (isJavaScriptReady()) {
  	  	  	  	  	  	  	  Timer(evt.target).stop();
  	  	  	  	  	  	  	  jsReadyHandler();
  	  	  	  	  	  	  }
  	  	  	  	  	  });
  	  	  	  	  	  timerReady.start();
  	  	  	  	  }
  	  	  	  } catch (err:SecurityError) {
  	  	  	  	  trace(err.message );
  	  	  	  } catch (err:Error) {
  	  	  	  	  trace(err.message );
  	  	  	  }
  	  	   //}
  	  	}
		
		public function connect(addr:String, port:int):void{
			if((this.socket == null || this.socket.connected == false)
				&& addr != null && addr != "" && port > 0){
				if(this.socket != null){
					this.socket.close();
					this.socket = null;
				}
				Security.loadPolicyFile("xmlsocket://"+addr+":"+843);
				this.socket = new Socket();
				this.socket.addEventListener(Event.CONNECT, onConnect);
				this.socket.addEventListener(IOErrorEvent.NETWORK_ERROR, onFail);
				this.socket.addEventListener(IOErrorEvent.IO_ERROR, onFail);
				this.socket.addEventListener(SecurityErrorEvent.SECURITY_ERROR, onSecurityFail);
				this.socket.addEventListener(ProgressEvent.SOCKET_DATA, onData);
				this.socket.connect(addr, port);
			}
		}
		
		private function onConnect(e:Event):void {
			ExternalInterface.call("onSuccess");
		}
		
		private function onFail(e:IOErrorEvent):void {
			ExternalInterface.call("onFail", e.text);
		}
		
		private function onSecurityFail(e:SecurityErrorEvent):void {
			ExternalInterface.call("onFail", "Security error" + e.text);
		}
		
		private function onSendFail(msg:String):void {
			ExternalInterface.call("onFail", msg);
		}
		
		private function onData(e:ProgressEvent):void {
			var data:String = socket.readUTFBytes(socket.bytesAvailable);
			ExternalInterface.call("onData", data);
		}
		
		private function sendData(msg:String):void {
			if(this.socket == null || this.socket.connected == false){
				this.onSendFail("Fail to send message");
			} else {
				this.socket.writeUTFBytes(msg+"\r\n");
				this.socket.flush()
			}
		}
	}
}

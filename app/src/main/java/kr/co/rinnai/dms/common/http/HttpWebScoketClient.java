package kr.co.rinnai.dms.common.http;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import kr.co.rinnai.dms.common.callback.WebSocketCallBack;

public class HttpWebScoketClient {
	
	private WebSocketClient webSocket;
	private WebSocketCallBack callBack;
	
	public HttpWebScoketClient(String host) {
		
		try {
			webSocket = new WebSocketClient(new URI(host)) {
				
				@Override
				public void onOpen(ServerHandshake arg0) {

					if (null != callBack)
						callBack.onOpen(arg0);
				}
				
				@Override
				public void onMessage(String arg0) {

					if (null != callBack)
						callBack.onReceive(arg0);
				}
				
				@Override
				public void onError(Exception arg0) {

					if (null != callBack)
						callBack.onError(arg0.toString());
				}
				
				@Override
				public void onClose(int arg0, String arg1, boolean arg2) {

					if (null != callBack)
						callBack.onClose(arg0, arg1, arg2);
				}
			};
		} catch (URISyntaxException e) {
			
			callBack.onError(e.toString());			
		}
	}
	
	public void Connect() {
		webSocket.setConnectionLostTimeout(5000);
		webSocket.connect();
	}
	
	public void Close() {
		webSocket.close();	
	}
	
	public void Send(String msg) {	
		webSocket.send(msg);
	}
	
	public void SetCallBack(WebSocketCallBack callBack) {		
		this.callBack = callBack;
	}
}
 
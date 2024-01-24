package kr.co.rinnai.dms.common.callback;

import org.java_websocket.handshake.ServerHandshake;

public interface WebSocketCallBack {

	public void onOpen(ServerHandshake arg);
	public void onReceive(String msg);
	public void onError(String error);
	public void onClose(int arg0, String arg1, boolean arg2);
	
}

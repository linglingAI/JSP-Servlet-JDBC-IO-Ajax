package edu.bitzh.webSocket;

public class ChatInfoDTO {
	int pos=0;
	String newMessage=null;
	WebSocketServer aScoket=null;
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public String getNewMessage() {
		return newMessage;
	}
	public void setNewMessage(String newMessage) {
		this.newMessage = newMessage;
	}
	public WebSocketServer getaScoket() {
		return aScoket;
	}
	public void setaScoket(WebSocketServer aScoket) {
		this.aScoket = aScoket;
	}
}

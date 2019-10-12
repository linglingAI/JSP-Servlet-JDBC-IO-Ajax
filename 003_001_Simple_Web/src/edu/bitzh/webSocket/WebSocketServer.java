package edu.bitzh.webSocket;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/WebSocketServer")
public class WebSocketServer{
    private Session session;
    private int myPos=0;
    private static ConcurrentHashMap<String,WebSocketServer> webSocketHashMap=new ConcurrentHashMap<>();
    
    public void setPos(int pos) {
    	this.myPos=pos;
    }
    @OnOpen 
    public void onOpen(Session session, EndpointConfig config){ 
        this.session=session;
    	System.out.println("Open httpSession as id="+session.getId()+", 开始连接..."); 
    	//一个定时器，定时查看聊天室是否有新内容？
    	ChatInfoDTO dto=new ChatInfoDTO();
    	dto.setPos(myPos);
        TimerTask timerTask = new ChatRoomTimerCheckTask(dto);
        //running timer task as daemon thread
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 500);
     }
    @OnClose
    public void onClose(){
        System.out.println("WEBCLOSE "+this.session.getId());
    }
    @OnMessage
    public void onMessage(Session session,String msg){
    	this.session=session;
    	
        System.out.println("received message"+msg);
        if(session.isOpen()){
            try {
            	ChatRoom.addMessage(msg);
            	ChatInfoDTO dto=new ChatInfoDTO();
            	dto.setPos(myPos);
            	dto=ChatRoom.getNewMessage(dto);
            	this.myPos=dto.getPos();
            	String newMessage=dto.getNewMessage();
                //将newMessage返回回去
            	sendMessage("聊天室最新的发言 ： "+newMessage);
            	
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);  //同步方法
    }
    public static void callByHttpSession(String httpSid,String msg) throws IOException {
    	WebSocketServer socket=webSocketHashMap.values().iterator().next();
		socket.sendMessage(msg);
		/*
    	if (webSocketHashMap.contains(httpSid)) {
    		WebScoketWorker socket=webSocketHashMap.get(httpSid);
    		socket.sendMessage(msg);
    	}
    	*/ 
    }
}
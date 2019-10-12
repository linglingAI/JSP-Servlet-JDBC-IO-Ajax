package edu.bitzh.webSocket;

import java.util.TimerTask;

public class ChatRoomTimerCheckTask extends TimerTask {
	ChatInfoDTO dto=null;
	ChatRoomTimerCheckTask(ChatInfoDTO dto){
		this.dto=dto;
	}
    @Override
    public void run() {
        completeTask();  
    }
    private void completeTask() {
        try {      	
        	dto=ChatRoom.getNewMessage(this.dto);
        	int newPos=dto.getPos();
        	String newMessage=dto.getNewMessage();
            
        	//将newMessage返回回去
        	this.dto.getaScoket().sendMessage("聊天室最新的发言 ： "+newMessage);   
        	this.dto.setPos(newPos);
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
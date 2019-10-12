package edu.bitzh.webSocket;
import java.util.ArrayList;    
public class ChatRoom {
	public static ArrayList<String> roomMessage=new ArrayList<String>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
   public static void  addMessage(String userName, String msg) {
	   roomMessage.add("用户 ："+userName+ "发言 : "+msg);
   }
   public static void  addMessage(String userNameAndMsg) {
	   roomMessage.add(userNameAndMsg);
   }
   public synchronized static ChatInfoDTO getNewMessage( ChatInfoDTO dto) {
	   String rs=null;

	   for (int i=dto.getPos();i<roomMessage.size();i++) {
			  rs="<br/>"+roomMessage.get(i);
	   } 
       dto.setNewMessage(rs);
	   return dto;
   }
}

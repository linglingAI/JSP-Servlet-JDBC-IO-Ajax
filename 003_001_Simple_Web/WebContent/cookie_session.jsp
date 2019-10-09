<%
/*********************************************************
 * 哪里是cookie ？  假设你哟些数据需要存放在浏览器，当成记录
 */
String cooieStr="Cookie :"; 
Cookie[] cookies=request.getCookies();
if (cookies!=null && cookies.length>0){
	cooieStr=cooieStr+"空";
	// 如果没有cookie， 我们就加一个
	Cookie cookie=new Cookie("key1","第一个cookie");
	response.addCookie(cookie);
}else{
	String name,value=null;
	for(Cookie cookie : cookies){
		name=cookie.getName();
		value=cookie.getValue();
		cooieStr=cooieStr+" | "+name+"="+value;
	}
	// 追加一个吧
	Cookie cookie=new Cookie("K-"+System.currentTimeMillis(),System.currentTimeMillis()+"");
	response.addCookie(cookie);
}
 
 /**********************************
  * 哪里是session ？ 
  */
  String[] names=session.getValueNames();


%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
</head>
<body>

</body>
</html>
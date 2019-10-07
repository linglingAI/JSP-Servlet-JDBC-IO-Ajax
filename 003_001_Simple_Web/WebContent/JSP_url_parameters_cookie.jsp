<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
   测试 1 ：   从客户端浏览器的 url 送参数给服务器端的 Tomcat 的 JSP <br/>
<%
	/***************************************************
	 *   下面是纯粹的 Java 代码， 读出url里面的参数     
	****************************************************/
	String urlStr="";
	Enumeration<String> all=request.getParameterNames();
    String key=null;
    while(all.hasMoreElements()){
    	key=all.nextElement();
    	urlStr=urlStr+"|"+key+"="+request.getParameter(key);
    }
   /***************************************************
         *   下面是纯粹的 Java 代码， 读出cookie，
        *      如果一个cookie都没有，我们就自己生成一个
        *，   然后 加个计数器， 返回去浏览器（response），下次调用到这里
        *      就有了。      
   ****************************************************/
    String cookieStr="";
    Cookie[] cookies=request.getCookies();
    if (cookies==null||cookies.length==0){
    	cookieStr="目前没有Cookie";
    	Cookie cookie=new Cookie("cookie_key","1");
    	response.addCookie(cookie);
    }else{
    	for(Cookie cookie: cookies){
    		cookieStr=cookieStr+"|"+cookie.getName()+"="+cookie.getValue();
    	}
    }   
 
%>
  我们收到 Url参数 : <%=urlStr%>,  <br/> Cookie : <%=cookieStr %>    
</body>
</html>
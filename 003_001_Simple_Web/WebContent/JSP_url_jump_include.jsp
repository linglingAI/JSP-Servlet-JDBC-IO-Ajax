<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP_url_jump_include.jsp</title>
</head>
<body>
 <H1>如果没有带jump=1 或 redirect=1 的参数， 本页就包括了<Strong>Hello.jsp</Strong>的内容了。</H1>
<% 
	/*******************************************************************
	 *   内部 转跳 （forwrd）  JSP_url_parameters_cookie.jsp， 如果 url参数有 jump=1
	********************************************************************/
	String isJump=request.getParameter("forward");
    if (isJump!=null&& isJump.equals("1")){
    	request.getRequestDispatcher("/JSP_url_parameters_cookie.jsp").forward(request, response);
    }
	/****************************************************************************
	 *  外部重定向  （redirect） 到   JSP_url_parameters_cookie.jsp， 如果 url参数有 redirect=1
	* redirect 和 forward的最大不同适， redirect 将新的url传回浏览器， 浏览器据此再发
	*   一次http请求到 tomcat 服务器，而 forward只是在内部转发 ，所以大家思考一下性能上的差别？
	*  当然redirect 也可以发一个第三方外部的url，这是redirect最重要的应用！
	*****************************************************************************/
	String isRedirect=request.getParameter("redirect");
    if (isRedirect!=null&& isRedirect.equals("1")){
    	response.sendRedirect("http://www.bitzh.edu.cn");
    }
   /***************************************************
         *   包含网页 （include） :  
    *       
   ****************************************************/
%>
<jsp:include page="/Hello.jsp" flush="true"/>
 
</body>
</html>
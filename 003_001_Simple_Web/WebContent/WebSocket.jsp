<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
服务器返回的信息：
<input type="text" id="show"/>
 
浏览器发送的信息：
<input type="text" id="msg"/>
<input type="button" value="send" id="send" onclick="q()"/>
 
 
<script>
    var ws = null ;
    var cookies=document.cookie;
    var target="ws://localhost:8080/websocket/WebSocketServer?roomid=room1234";
    
    if ('WebSocket' in window) {
        ws = new WebSocket(target);
    } else if ('MozWebSocket' in window) {
        ws = new MozWebSocket(target);
    } else {
        alert('WebSocket is not supported by this browser.');
    }
    
    ws.onopen = function(obj){
        console.info('open') ;
        console.info(obj) ;
    } ;
    
    ws.onclose = function (obj) {
        console.info('close') ;
        console.info(obj) ;
    } ;
    ws.onmessage = function(obj){
        console.info(obj) ;
        document.getElementById('show').value=obj.data;
    } ;
    function q(){
    	jsonObj={"cookies":getCookies(),'content': document.getElementById('msg').value};
    	var rs=JSON.stringify(jsonObj);
        ws.send(rs);
    }
    function getCookies(){
    	return document.cookie;
    }
</script>
</body>
</html>
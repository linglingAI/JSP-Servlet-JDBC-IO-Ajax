<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ajax_JS_debug.jsp</title>
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript">

function clickToGetValue(){
	var txt=$("#browser").val();
	var dataPara={ "key": txt};
	
	ajaxAccessServer(dataPara);
	
	alert("我在客户端完成任务...");
}
function ajaxAccessServer(dataPara){
    if (!dataPara){
	   	dataPara={};
	}
	$.ajax({
	    crossDomain : true,
	    url: "/AjaxServlet",
	    type: "POST",
	    data: {"AjaxData":JSON.stringify(dataPara)} , 
	    dataType: "json",
	    success: function (data) {
	    	listenToServer(data);
	    },
	    complete : function (){
            alert("我(Ajax)完成了向服务器发送数据 :" +dataPara);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
       		   alert("Encounter Error in ajaxAccessServer(), Detail is : " +thrownError);
	    }
	});
}
function listenToServer(data){
	$("#server").html(data);
}
</script>
</head>
<body>
  <div id="server" style="min-height: 50px;max-width:100px; border:1px solid blue; marging: 20px; padding:20px"> 这里用来显示服务器送回的内容</div>
  <br/>
  <input type=text id="browser" maxlength="150" value="输入送往服务器的内容"> </input>
  <br/>
  <br/>
  <button onclick="clickToGetValue();return false;"> 点击将input id=content 的内容送去服务器</button>
  
</body>
</html>
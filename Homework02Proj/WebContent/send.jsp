<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<style>
#send{
	height:50px;
	margin:10px 10px 10px 10px;
}
#data{
	margin:10px 10px 10px 10px;
}
</style>
<script src="jquery/jquery-1.12.4.js"></script>
<script type="text/javascript">

	var n=0;
	function ajaxSend(num)
	{
		$.ajax({
			url:"/daoServlet",
			type:"POST",
			data:{"AjaxData":JSON.stringify(num)},
			success:function(result){
				$("#data").html(result);
			}, 
			complete:function(){
				alert("ajax已完成发送数据");
			},
			error:function (xhr, ajaxOptions, thrownError) {
	    		   alert("Encounter Error in ajaxAccessServer(), Detail is : " +thrownError);
	    		   alert(JSON.stringify(data));
			}
		});
		
		}
	function clickToSend(){
		n++;
		ajaxSend(n);
	}

</script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>send.jsp</title>
</head>
<body>
<div id="data">
</div>
<div>
<input id="send" type="submit" value="send"  onclick="clickToSend();">
</div>
<jsp:include page="list.jsp"></jsp:include>
</body>
</html>
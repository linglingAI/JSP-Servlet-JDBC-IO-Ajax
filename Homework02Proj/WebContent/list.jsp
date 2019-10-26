<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<style>
#toJDBC{
	height:50px;
	margin:10px 10px 10px 10px;
	
}
#fromJDBC{
	height:50px;
	margin:10px 10px 10px 10px;
}
</style>
<script type="text/javascript" src="jquery/jquery-1.12.4.js"></script>
<script type="text/javascript">

function toJDBC_Ajax(){
	$.ajax({
		url:"/daoServlet",
		type:"post",
		data:{"AjaxData_list":JSON.stringify(1)},
		success:function(result){
			$("#data").html(result);
		}, 
		complete:function(){
			alert("ajax已完成发送数据");
		},
		error:function (xhr, ajaxOptions, thrownError) {
    		   alert("Encounter Error in ajaxAccessServer(), Detail is : " +thrownError);
    		   
		}
	});
}
function fromJDBC_Ajax(){
	$.ajax({
		url:"/daoServlet",
		type:"post",
		data:{"AjaxData_list":"2"},
		success:function(result){
			$("#JDBCdata").html(result);
		}, 
		complete:function(){
			alert("ajax已完成发送数据");
		},
		error:function (xhr, ajaxOptions, thrownError) {
    		   alert("Encounter Error in ajaxAccessServer(), Detail is : " +thrownError);
    		   
		}
	});
}
$(document).ready(function(){
	$("#toJDBC").click(function() {
		toJDBC_Ajax();
	});
	$("#fromJDBC").click(function() {
		fromJDBC_Ajax();
	});
});
</script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>list.jsp</title>
</head>
<body>
<div>
<input type="submit" id="toJDBC" value="写数据库并清零 Session" ><br>
<input type="submit" id="fromJDBC" value="访问数据库"  >
</div>
<div id="JDBCdata">

</div>
</body>
</html>
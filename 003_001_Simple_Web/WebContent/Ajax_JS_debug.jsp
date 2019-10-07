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
	
}
function ajaxAccessServer(dataPara){
    if (!dataPara){
	   	dataPara={};
	}
	$.ajax({
	    crossDomain : true,
	    url: "/AjaxServlet",
	    type: "POST",
	    data: {"AjaxData":JSON.stringify(dataPara)} , // {myPostData : dataPara }
	    dataType: "json",
	    success: function (data) {
	    	listenToServer(data);
	    },
	    complete : function (){
            alert("我完成了向服务器发送数据 :" +dataPara);
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
       		   alert("Encounter Error in ajaxAccessServer(), Detail is : " +thrownError);
	    }
	});
}

function listenToServer(data){
	
	
}


</script>
</head>
<body>

</body>
</html>
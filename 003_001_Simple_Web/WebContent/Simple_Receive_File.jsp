<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*,java.util.*,javax.servlet.*,javax.servlet.http.*" %>
<%@ page import="org.apache.commons.fileupload.FileItem," %>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>

<html>
<head>
    <title>Simple_Receive_File.jsp</title>
</head>
<body>
<%

int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

/************************************
*	 检测是否为多媒体上传
************************************/
if (!ServletFileUpload.isMultipartContent(request)) {
  
 /************************************
  *	如果不是则停止
  ************************************/
    PrintWriter writer = response.getWriter();
    writer.println("Error: 表单必须包含 enctype=multipart/form-data");
    writer.flush();
    return;
}

/************************************
*	配置上传参数
************************************/
DiskFileItemFactory factory = new DiskFileItemFactory();

/************************************
*	设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
************************************/
factory.setSizeThreshold(MEMORY_THRESHOLD);

/************************************
*	设置临时存储目录
************************************/
factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

ServletFileUpload upload = new ServletFileUpload(factory);

/************************************
*	设置最大文件上传值
************************************/ 
upload.setFileSizeMax(MAX_FILE_SIZE);

/************************************
*	设置最大请求值 (包含文件和表单数据)
************************************/ 
upload.setSizeMax(MAX_REQUEST_SIZE);

/************************************
*	中文处理
************************************/
upload.setHeaderEncoding("UTF-8"); 

/************************************
*	 构造临时路径来存储上传的文件
*       这个路径相对当前应用的目录
************************************/
String realPath = request.getSession().getServletContext().getRealPath("/");
// 
int findpos=realPath.indexOf("\\sct");
if (findpos<0){
	findpos=realPath.indexOf("\\SCT");
}
String uploadPath  = realPath.substring(0, findpos+3)+"\\tomcat\\web\\upload";

if (findpos<0){
	uploadPath="c:\\SCT\\tomcat\\web\\upload\\";
}
/************************************
 *	如果目录不存在则创建
 ************************************/
File uploadDir = new File(uploadPath);
if (!uploadDir.exists()) {
    uploadDir.mkdir();
}

try {
/************************************
*	解析请求的内容提取文件数据
************************************/
    @SuppressWarnings("unchecked")
    List<FileItem> formItems = upload.parseRequest(request);

    if (formItems != null && formItems.size() > 0) {
        // 
        /************************************
         *	迭代表单数据
         ************************************/
        for (FileItem item : formItems) {
            // 
            /************************************
             *	处理不在表单中的字段
             ************************************/
            if (!item.isFormField()) {
                String fileName = new File(item.getName()).getName();
                String filePath = uploadPath + File.separator + fileName;
                File storeFile = new File(filePath);
              
                /************************************
                *	在控制台输出文件的上传路径
                ************************************/
                System.out.println(filePath);
                /************************************
                *	保存文件到硬盘
                ************************************/
                item.write(storeFile);
                out.print("<H1>文件上传成功 :</H1> "+filePath);
            }
        }
    }
} catch (Exception ex) {
	out.print("错误信息: " + ex.getMessage());
}
%>
</body>
</html>
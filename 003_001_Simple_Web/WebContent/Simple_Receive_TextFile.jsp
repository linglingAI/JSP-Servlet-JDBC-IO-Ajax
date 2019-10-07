<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*,java.util.*,javax.servlet.*,javax.servlet.http.*" %>
<%@ page import="java.rmi.ServerException,java.util.Base64;" %>
<html>
<head>
    <title></title>
</head>
<body>
<%
    /***************************************
           *          定义上传文件的最大字节 ------1
     ***************************************/
    int MAX_SIZE = 102400 * 102400;
    /***************************************
          *   创建根路径的保存变量  -----2
    ***************************************/
    String rootPath;
    
    /***************************************
          *   声明文件读入类  -----3
    ***************************************/
    
    DataInputStream in = null;
    FileOutputStream fileOut = null;
    /***************************************
           *   取得互联网程序的绝对地址  ----4
     ***************************************/

    String realPath = request.getSession().getServletContext().getRealPath("/");
    // 
    int findpos=realPath.indexOf("\\sct");
    if (findpos<0){
    	findpos=realPath.indexOf("\\SCT");
    }
    realPath = realPath.substring(0, findpos+3);
    
    if (findpos<0){
    	rootPath="c:\\SCT\\tomcat\\web\\upload\\";
    }

    /***************************************
          *   创建文件的保存目录  ----5
    ***************************************/

    rootPath = realPath + "\\tomcat\\web\\upload\\";
    
    /***************************************
           *   取得客户端上传的数据类型  ----6
     ***************************************/
    String contentType = request.getContentType();
    try {
        if (contentType.indexOf("multipart/form-data") >= 0) {
        	
            /***************************************
                              *   读入上传数据  ----7
             ***************************************/
            //
            in = new DataInputStream(request.getInputStream());
            int formDataLength = request.getContentLength();
            if (formDataLength > MAX_SIZE) {
                out.print("上传的字节不可以超过" + MAX_SIZE + "字节");
                return;
            }
            
            /***************************************
                             *   保存上传文件的数据  ----8
            ***************************************/     
            byte dataBytes[] = new byte[formDataLength];
            int byteRead = 0;
            int totalBytesRead = 0;
            
            /***************************************
                              *   上传的数据保存在byte数组里面   ---9
            ***************************************/
            while (totalBytesRead < formDataLength) {
                byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
                totalBytesRead += byteRead;
            }
            
            /***************************************
                             *  根据byte数组创建字符串  ------10
            ***************************************/
            //String file = new String(dataBytes, "utf-8");
    		//Base64 enc = new Base64();
    		//String file = enc.encodeBuffer(dataBytes); // 使用BASE64编码
    		String file = Base64.getEncoder().encodeToString(dataBytes);
            
            /***************************************
                             *   取得上传数据的文件名  ------11
            ***************************************/
            String saveFile = file.substring(file.indexOf("filename=\"") + 10);
            saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
            saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1, saveFile.indexOf("\""));
            int lastIndex = contentType.lastIndexOf("=");
            
            /***************************************
                             *   取得数据的分隔字符串  -----12
            ***************************************/
            String boundary = contentType.substring(lastIndex + 1, contentType.length());
            
            /***************************************
                              *   创建保存路径的文件名   ------13
             ***************************************/
            String fileName = rootPath + saveFile;
            int pos;
            pos = file.indexOf("filename = \"");
            pos = file.indexOf("\n", pos) + 1;
            pos = file.indexOf("\n", pos) + 1;
            pos = file.indexOf("\n", pos) + 1;
            int boundaryLocation = file.indexOf(boundary, pos) - 4;
            
            /***************************************
                              *   取得文件数据的开始的位置  ------14
             ***************************************/
            int startPos = ((file.substring(0, pos)).getBytes()).length;
            int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;
            File checkFile = new File(fileName);
            if (checkFile.exists()) {
                out.println("<p>" + saveFile + "文件已经存在.</p>");
                return;
            }
            
            /***************************************
                               *   检查上传文件的目录是否存在   ------15
             ***************************************/
            File fileDir = new File(rootPath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            
            /***************************************
                             *   创建文件的输出类    -------16
            ***************************************/
            
            fileOut = new FileOutputStream(fileName);
            
            /***************************************
                              *   保存文件的数据    -------17
       		 ***************************************/
            
            fileOut.write(dataBytes, startPos, (endPos - startPos));
            fileOut.close();
            out.print("<b>文件上传成功, 文件为本地的 ： </b>"+fileName);
            
        } else {
            String content = request.getContentType();
            out.print("上传的文件类型是" + content + "类型的，请上传目录mutipart/form-data类型的文件");
        }
    } catch (Exception ex) {
    	out.println("<p>出错 : </p>"); 
    	out.println(ex.getMessage());
    	// throw new ServerException(ex.getMessage());
    }
%>
</body>
</html>
package edu.bitzh.simple;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.Enumeration;
/**
 * Servlet implementation class daoServlet
 */
@WebServlet("/daoServlet")
public class daoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// mysql JDBC URL��ʽ���£�
				// jdbc:mysql://[host:port]/[database][?������1][=����ֵ1][&������2][=����ֵ2]...
				public final static String URL = "jdbc:mysql://localhost:3306/sct?useUnicode=true&characterEncoding=utf-8";
				// public final static String URL =
				// "jdbc:mysql://localhost:3306/sct?user=root&sct2019XS&useUnicode=true&characterEncoding=utf-8&useOldAliasMetadataBehavior=true";
				public final static String DRIVER = "com.mysql.cj.jdbc.Driver"; // com.mysql.cj.jdbc.Driver //com.mysql.jdbc.Driver

				// ��¼���ݿ���˺ź�����
				public final static String USER = "root";
				public final static String PWD = "password";
				
				//��
				public final static String Data = "data";
				
				
				// �� Data ���ֶ�
				public final static String ID = "ID";
				public final static String NAME = "Name";
				public final static String Amount = "��Ŀ";
    /**
     * @see HttpServlet#HttpServlet()
     */ 
    public daoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
//------------------------------------send.jsp ��ȡdata.txt---------------------------------------------		
		
		String AjaxData=request.getParameter("AjaxData");
		int AjaxData_int=0;
		if(AjaxData!=null)
		{
			 AjaxData_int=Integer.parseInt(AjaxData);
		}
		
		String filestr=null;
		String filestr_all="";
		HttpSession session=request.getSession();
		BufferedReader reader=new BufferedReader(new FileReader("D:\\SE001\\eclipse-WS\\clone-student\\Homework02Proj\\data2.txt"));
		for(int i=0;i<AjaxData_int;i++)
			{
				filestr=reader.readLine();
				if(filestr==null)
				{
					response.getWriter().write("�����ݣ�"+"<br>");
				}
				else {
					filestr_all=filestr_all+filestr+"|";
					session.setAttribute("abc", filestr_all);
				}
				
			}
		reader.close();
		if(filestr!=null)
		{
			Enumeration<String> enumeration=session.getAttributeNames();
			String key="";
			while(enumeration.hasMoreElements())
			{
				key=enumeration.nextElement();
				String namestr="session_key:  "+key;
				response.getWriter().write(namestr+"<br>");
			}
			String s=(String)session.getAttribute("abc");
			response.getWriter().write("session_value:  "+s+"<br>");
			
		}
//-----------------------------------------list.jsp-----------------------------------------------------
		
		
		String AjaxData_list=request.getParameter("AjaxData_list");
		if(AjaxData_list!=null)
		{
		//--------------------д���ݿⲢ���� Session(start)------------------------
			if(AjaxData_list.equals("1")) {
			String session_value=(String)session.getAttribute("abc");
			String[] all=session_value.split("\\|");
			String lastall="";
			String newall ="";
			String sqlData="";
			for(int i=0;i<all.length;i++)
			{
				String middle="";
				System.out.println(all[i]);
				String[] new2=all[i].split(" ");
				for(int j=0;j<new2.length;j++)
				{
					if(j==2) {
						middle=middle+new2[j]; 
					}
					else {
						middle=middle+"\""+new2[j]+"\","; 
					}
					
				}
				System.out.println(middle);
				if(i==(all.length-1))
				{
					sqlData=sqlData+"("+middle+")";
				}
				else {
					sqlData=sqlData+"("+middle+"),";
				}
				
			}
			System.out.println(sqlData);
			connectJDBC(sqlData);
			session.setAttribute("abc", "");
			response.getWriter().write("д���ݿⲢ���� Session(�����)<br>");
			response.getWriter().write("session_key:abc<br>session_value:"+(String)session.getAttribute("abc"));
			
		}
	//--------------------------------------д���ݿⲢ���� Session(end)------------------------------------
	//-------------------------------------------�������ݿ�(start)-------------------------------------------
			if(AjaxData_list.equals("2"))
			{
				String string=fromJDBC();
				response.getWriter().write(string);
			}
		}
//-------------------------------------list.jsp �������ݿ�-------------------------------------------------------------
		
	}
public static void connectJDBC(String fileData) {
			
	Connection con=null;
	Statement st=null;

	//����JDBC driver
	try {
		Class.forName(DRIVER);
	}
	catch (ClassNotFoundException e) {
		// TODO: handle exception
		System.out.println("DRIVER not found");
	}
	//�������ݿ�
	try {
		con=DriverManager.getConnection(URL,USER,PWD);
		st=con.createStatement();
		String sql="INSERT INTO "+Data+"("+ID+","+NAME+","+Amount+")"+"\n"+ "values"+fileData+";";
		System.out.println(sql);
		st.execute(sql);
		st.close();
		con.close();
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println("connect fail:"+e.getMessage());
	}
}

public static String fromJDBC()
{
	Connection con=null;
	Statement st=null;
	ResultSet rs=null;
	String allstr="";
	//����DRIVER	
	try {
		Class.forName(DRIVER);
	} catch (ClassNotFoundException e) {
		// TODO: handle exception
		System.out.println("DRIVER not found");
	}
	//�������ݿ�
	try {
		con=DriverManager.getConnection(URL,USER,PWD);
		st=con.createStatement();
		String sql="SELECT * FROM "+Data+";";
		rs=st.executeQuery(sql);
		if(rs!=null)
		{
			ResultSetMetaData rsmd=rs.getMetaData();
			int count=rsmd.getColumnCount();
			for(int i =1;i<=count;i++)
			{
				allstr=allstr+rsmd.getColumnName(i)+" ";
			}
			allstr=allstr+"<br>";
			
			while(rs.next())
			{
				allstr=allstr+rs.getString(ID)+" ";
				allstr=allstr+rs.getString(NAME)+" ";
				allstr=allstr+rs.getString(Amount)+" <br>";
			}
		}
		rs.close();
		st.close();
		con.close();
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println("connect is false:"+e.getMessage());
	}
	return allstr;
}
}

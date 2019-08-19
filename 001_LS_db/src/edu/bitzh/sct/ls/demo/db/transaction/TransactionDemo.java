package edu.bitzh.sct.ls.demo.db.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class TransactionDemo {
	// mysql JDBC URL格式如下：
	// jdbc:mysql://[host:port]/[database][?参数名1][=参数值1][&参数名2][=参数值2]...
	public final static String URL = "jdbc:mysql://localhost:3306/sct?useUnicode=true&characterEncoding=utf-8";
	// public final static String URL =
	// "jdbc:mysql://localhost:3306/sct?user=root&sct2019XS&useUnicode=true&characterEncoding=utf-8";
	public final static String DRIVER = "com.mysql.cj.jdbc.Driver"; // com.mysql.cj.jdbc.Driver //com.mysql.jdbc.Driver

	// 登录数据库的账号和密码
	public final static String USER = "root";
	public final static String PWD = "sct2019XS";
	
	public final static String TBL_SECKILL = "seckill_tbl_01";
	public final static String TBL_TBL_02 = "tbl_02";
	
	// 数据库 seckill_tbl_01, tabl_02 的字段
	public final static String COLUMN_ID = "id";
	public final static String COLUMN_NAME = "name";
	public final static String COLUMN_CATEGORY = "category";
	public final static String COLUMN_AMOUNT = "amount";

	public static void main(String[] args) throws Exception {

		
		//testJDBC();
		
		//testNoTransaction();
		
		//testTransactionFail();
		
		//testTransactionCommitOrRollback(-1);
		
	    //testTransactionCommitOrRollback(0);
		
		testTransactionCommitOrRollback(1);
		
	}

	public static void testJDBC() {

		Connection con = null;
		ResultSet rs = null;
		Statement st = null;

		
		/***************************************************
		 * 第一步 ： 加载 JDBC driver 进 JVM
		 */
		try {
			Class.forName(DRIVER);
		} catch (java.lang.ClassNotFoundException e) {
			// System.out.println("Connect Successfull.");
			System.out.println("Cant't load Driver");
		}
		/***************************************************
		 * 第二步 ： 从JVM的 JDBC driver 中获得 某个数据库（在URL配置）的连接，
		 */
		try {
			
			con = DriverManager.getConnection(URL, USER, PWD);
			st = con.createStatement();
			
			String sql = "select * from "+TBL_SECKILL;
			rs = st.executeQuery(sql);

			if (rs != null) {
				System.out.println("---获取表的字段---");
				ResultSetMetaData rsmd = rs.getMetaData();
				int countcols = rsmd.getColumnCount();
				for (int i = 1; i <= countcols; i++) {
					if (i > 1)
						System.out.print(";");
					System.out.print(rsmd.getColumnName(i) + " ");
				}
				System.out.println("");
				System.out.println("---获取所有记录---");

				while (rs.next()) {
					System.out.println(rs.getString(COLUMN_ID) + "， ");
					System.out.print(rs.getString(COLUMN_NAME) + "，");
					System.out.print(rs.getString(COLUMN_CATEGORY) + "， ");
					System.out.print(rs.getString(COLUMN_AMOUNT) );
					System.out.println("");
				}
			}
			/*****************************************************************
			 * 最后关掉连接以及相应的对象，释放内存空间，注意这是非常最要的，而且容易被忽略，
			 */
			rs.close();
			st.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Connect fail:" + e.getMessage());
		}
	}

	public static void testNoTransaction() throws Exception {
		Connection con = null;
		//ResultSet rs = null;
		Statement st = null;
		
		con=DatabaseAccessFactory.getConnection(DatabaseConstants.URL, DatabaseConstants.USER, DatabaseConstants.PWD);
		
		System.out.println("请在Navicat中先查看执行前的数据，并记住，以便对比----");
		
		// 从 seckill_tabl_01 （id=1，name=xa_a，amount）转 2000元  到 tabl_02 （id=1，name=xa_a， amount）， 两张表修改
		String getMoneySQL="update "+DatabaseConstants.TBL_SECKILL+" set "+DatabaseConstants.COLUMN_AMOUNT+" ="+DatabaseConstants.COLUMN_AMOUNT+"-2000 where "+DatabaseConstants.COLUMN_ID+"=1;";
		String setMoneySQL="update "+DatabaseConstants.TBL_TBL_02+" set "+DatabaseConstants.COLUMN_AMOUNT+" ="+DatabaseConstants.COLUMN_AMOUNT+"+2000 where "+DatabaseConstants.COLUMN_ID+"=1;";
		
		st = con.createStatement();
		System.out.println("执行 mysql--1 ："+getMoneySQL);
		st.execute(getMoneySQL);// 第一条天数据库更新命令
		
		System.out.println("执行 mysql--2 ："+setMoneySQL);
		st.execute(setMoneySQL);// 第二条数据库更新命令
		
		System.out.println("两张表修改的修改完毕， 请在Navicat中查看结果----");
		
		//rs.close();
		st.close();
		con.close();		
				
	}

	public static void testWithTransaction() {

	}

	public static void testTransactionFail() throws Exception {
		Connection con = null;
		//ResultSet rs = null;
		Statement st = null;
		
		con=DatabaseAccessFactory.getConnection(DatabaseConstants.URL, DatabaseConstants.USER, DatabaseConstants.PWD);
		
		System.out.println("请在Navicat中先查看执行前的数据，并记住，以便对比----");
		
		// 从 seckill_tabl_01 （id=1，name=xa_a，amount）转 2000元  到 tabl_02 （id=1，name=xa_a， amount）， 两张表修改
		String getMoneySQL="update "+DatabaseConstants.TBL_SECKILL+" set "+DatabaseConstants.COLUMN_AMOUNT+" ="+DatabaseConstants.COLUMN_AMOUNT+"-2000 where "+DatabaseConstants.COLUMN_ID+"=1;";
		String setMoneySQL="update "+DatabaseConstants.TBL_TBL_02+" set "+DatabaseConstants.COLUMN_AMOUNT+" ="+DatabaseConstants.COLUMN_AMOUNT+"+2000 where "+DatabaseConstants.COLUMN_ID+"=1;";
		
		st = con.createStatement();
		System.out.println("执行 mysql ："+getMoneySQL);
		st.execute(getMoneySQL); // 第一条天数据库更新命令
		
		/***************************************************
		 * 在这里故意抛出一个exception，  跳过“第二天数据库更新命令”
		 * 
		 */
		if (true) {
			System.out.println("中断执行， 请在Navicat中查看结果----");
			throw new Exception();
		}
		System.out.println("执行 mysql ："+setMoneySQL);
		st.execute(setMoneySQL); // 第二条数据库更新命令
		
		System.out.println("两张表修改的修改完毕， 请在Navicat中查看结果----");
		
		//rs.close();
		st.close();
		con.close();
	}

	public static void testTransactionCommitOrRollback(int callCommit) throws Exception {
		Connection con = null;
		//ResultSet rs = null;
		Statement st = null;
		
		con=DatabaseAccessFactory.getConnection(DatabaseConstants.URL, DatabaseConstants.USER, DatabaseConstants.PWD);
		
		/******************************************************************************************************************
		 * 在这里设置本次连接 con 的事物部位自动 commit， 注意如果不调用 con.setAutoCommit（）， JDBC的连接时缺省 con.setAutoCommit（true）的
		 * 
		 */
		con.setAutoCommit(false);
	
		
		System.out.println("请在Navicat中先查看执行前的数据，并记住，以便对比----");
		
		// 从 seckill_tabl_01 （id=1，name=xa_a，amount）转 2000元  到 tabl_02 （id=1，name=xa_a， amount）， 两张表修改
		String getMoneySQL="update "+DatabaseConstants.TBL_SECKILL+" set "+DatabaseConstants.COLUMN_AMOUNT+" ="+DatabaseConstants.COLUMN_AMOUNT+"-2000 where "+DatabaseConstants.COLUMN_ID+"=1;";
		String setMoneySQL="update "+DatabaseConstants.TBL_TBL_02+" set "+DatabaseConstants.COLUMN_AMOUNT+" ="+DatabaseConstants.COLUMN_AMOUNT+"+2000 where "+DatabaseConstants.COLUMN_ID+"=1;";
		
		st = con.createStatement();
		System.out.println("执行 mysql--1 ："+getMoneySQL);
		st.execute(getMoneySQL);// 第一条天数据库更新命令
		
		System.out.println("执行 mysql--2 ："+setMoneySQL);
		st.execute(setMoneySQL);// 第二条数据库更新命令
		

		/******************************************************************************************************************
		 * callCommit=-1  no commit, no rollback, 不做任何事
		 * callCommit=1 调用 con.commit
		 * callCommit=1 调用 con.commit
		 */
		
		if (callCommit==1) {
			System.out.println("两张表修改的修改完毕，对事物进行 commit ， 请在Navicat中查看结果----");
			con.commit();
		}else if (callCommit==0) {
			con.rollback();
			System.out.println("两张表修改的修改完毕，事物进行 了rollback ， 请在Navicat中查看结果----");
		}else {
			// 不做任何事
			System.out.println("两张表修改的修改完毕，但没有对事物进行 commit 或 rollback ， 请在Navicat中查看结果----");
		}

		//rs.close();
		st.close();
		con.close();
	}
}

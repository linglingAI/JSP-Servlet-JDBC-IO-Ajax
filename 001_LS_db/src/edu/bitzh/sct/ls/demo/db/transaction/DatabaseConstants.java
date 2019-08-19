package edu.bitzh.sct.ls.demo.db.transaction;

public class DatabaseConstants {
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
}

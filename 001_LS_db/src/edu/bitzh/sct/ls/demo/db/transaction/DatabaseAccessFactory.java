package edu.bitzh.sct.ls.demo.db.transaction;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.zaxxer.hikari.HikariDataSource;

import edu.bitzh.sct.ls.demo.db.lock.MultiThreadLockDemo;

public class DatabaseAccessFactory {

	public static String connPrefix = null;

	public static int tryTimes = 10;
	public static int waitMills = 100;

	public static int poolMaxSize = 150;
	public static int poolInitSize = 150;
	public static int poolMaxIdleSize = 10;
	public static int poolMinIdleSize = 20;
	
	static DataSource tomcatPoolDS = null;
	static HikariDataSource hikariPoolDS = null;

	public static boolean isUsingPool = false; // 切换连接池和一般的JDBC
	public static boolean isUsingTomcatPool = true; //

	public static String isoLevel = null;

	public static Connection getConnection(String url, String userName, String password) throws Exception {
		if (isUsingPool) {

			return getConnectionFromPool(url, userName, password);
		}
		/***************************************************
		 * 第一步 ： 加载 JDBC driver 进 JVM
		 */

		Class.forName(DatabaseConstants.DRIVER);

		/***************************************************
		 * 第二步 ： 从JVM的 JDBC driver 中获得 某个数据库（在URL配置）的连接，
		 */
		return DriverManager.getConnection(url, userName, password);
	}

	public static Connection getConnectionFromPool(String url, String userName, String password) throws Exception {
		if (isUsingTomcatPool) {
			if (tomcatPoolDS == null) {
				try {
					MultiThreadLockDemo.s = System.currentTimeMillis();

					buildConnectionPoolOfTomcatDBCP(url, userName, password);

					connPrefix = " Using  Tomcat Pool, pool max size=" + poolMaxSize + ", Min idle size=" + poolMinIdleSize;

					if (tomcatPoolDS == null) {
						System.exit(1);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
			if (tomcatPoolDS != null) {
				return tomcatPoolDS.getConnection();
			} else {
				return null;
			}
		} else {
			if (hikariPoolDS == null) {
				try {
					MultiThreadLockDemo.s = System.currentTimeMillis();

					buildConnectionPoolOfHikariCP(url, userName, password);

					connPrefix = "Using  hikari Pool, pool size=" + poolMaxSize + ", Min idle size=" + poolMinIdleSize;

					if (hikariPoolDS == null) {
						System.exit(1);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
			if (hikariPoolDS != null) {
				return hikariPoolDS.getConnection();
			} else {
				return null;
			}
		}
	}

	public static synchronized void buildConnectionPoolOfTomcatDBCP(String url, String userName, String password)
			throws Exception {
		// String driver = "com.mysql.jdbc.Driver";
		if (tomcatPoolDS != null) {
			return;
		}
		long s = System.currentTimeMillis();

		DataSource ds = new DataSource();
		ds.setUrl(url);
		ds.setDriverClassName(DatabaseConstants.DRIVER);
		ds.setUsername(userName);
		ds.setPassword(password);
		ds.setJmxEnabled(true);
		ds.setTestWhileIdle(false);
		ds.setTestOnBorrow(false);
		ds.setValidationQuery("SELECT 1");
		ds.setTestOnReturn(false);
		ds.setValidationInterval(30000);
		ds.setTimeBetweenEvictionRunsMillis(30000);

		/**********************************************
		 * 
		 */
		ds.setMaxActive(poolMaxSize); // 最大有效JDBC连接数目
		ds.setInitialSize(poolInitSize); // 初始化的JDBC连接数目
		
		ds.setMinIdle(poolMinIdleSize); // 最小的可以空闲的JDBC连接数目
		ds.setMaxIdle(500); // 最大的可以空闲的JDBC连接数目
		
		ds.setMaxWait(10000); // 最大可以等待的毫秒数， 1000毫秒=1秒， 数值为 10000=10秒

		ds.setRemoveAbandonedTimeout(60);
		ds.setMinEvictableIdleTimeMillis(30000);
		ds.setMinIdle(10);
		ds.setLogAbandoned(true);
		ds.setRemoveAbandoned(true);
		ds.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

		// tomcatPool=ds.createPool();

		tomcatPoolDS = ds;
        /*
		Connection con = ds.getConnection();
		int is = con.getTransactionIsolation();
		if (is == Connection.TRANSACTION_READ_UNCOMMITTED) {
			isoLevel = " TRANSACTION_READ_UNCOMMITTED ";
		} else if (is == Connection.TRANSACTION_NONE) {
			isoLevel = " TRANSACTION_NONE ";
		} else if (is == Connection.TRANSACTION_READ_COMMITTED) {
			isoLevel = " TRANSACTION_READ_COMMITTED ";
		} else if (is == Connection.TRANSACTION_REPEATABLE_READ) {
			isoLevel = " TRANSACTION_REPEATABLE_READ ";
		} else if (is == Connection.TRANSACTION_SERIALIZABLE) {
			isoLevel = " TRANSACTION_SERIALIZABLE ";
		}
		con.close();
		*/

		System.err.println("========== created Tomcat pool 时间花费 ： " + (System.currentTimeMillis() - s) / 1000
				+ " 秒 , pool.size=" + ds.getPoolSize());

	}

	public static synchronized void buildConnectionPoolOfHikariCP(String url, String userName, String password)
			throws Exception {
		// String driver = "com.mysql.jdbc.Driver";
		if (hikariPoolDS != null) {
			return;
		}
		long s = System.currentTimeMillis();

		hikariPoolDS = new HikariDataSource();
		hikariPoolDS.setDriverClassName("com.mysql.cj.jdbc.Driver");
		hikariPoolDS.setJdbcUrl(url);
		hikariPoolDS.setUsername(userName);
		hikariPoolDS.setPassword(password);

		hikariPoolDS.addDataSourceProperty("cachePrepStmts", "true");
		hikariPoolDS.addDataSourceProperty("prepStmtCacheSize", "250");
		hikariPoolDS.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		hikariPoolDS.setMaximumPoolSize(poolMaxSize);
		hikariPoolDS.setMinimumIdle(poolMinIdleSize);
		
		hikariPoolDS.setConnectionTestQuery("SELECT 1;");
		hikariPoolDS.setIdleTimeout(10000);
		

		
        /*
		Connection con = hikariPoolDS.getConnection();

		int is = con.getTransactionIsolation();
		if (is == Connection.TRANSACTION_READ_UNCOMMITTED) {
			isoLevel = " TRANSACTION_READ_UNCOMMITTED ";
		} else if (is == Connection.TRANSACTION_NONE) {
			isoLevel = " TRANSACTION_NONE ";
		} else if (is == Connection.TRANSACTION_READ_COMMITTED) {
			isoLevel = " TRANSACTION_READ_COMMITTED ";
		} else if (is == Connection.TRANSACTION_REPEATABLE_READ) {
			isoLevel = " TRANSACTION_REPEATABLE_READ ";
		} else if (is == Connection.TRANSACTION_SERIALIZABLE) {
			isoLevel = " TRANSACTION_SERIALIZABLE ";
		}
		con.close();
        */
		System.err.println("========== created HikariCPpool 时间花费 ： " + (System.currentTimeMillis() - s) / 1000 + " 秒 ");

	}
}

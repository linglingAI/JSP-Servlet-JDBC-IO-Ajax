package edu.bitzh.sct.ls.demo.db.lock;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import edu.bitzh.sct.ls.demo.db.transaction.DatabaseAccessFactory;
import edu.bitzh.sct.ls.demo.db.transaction.DatabaseConstants;

class ThreadOne extends Thread {
	int No = -1;
	String sql;
	String threadPrefix;

	public ThreadOne(int No,String sql,String threadPrefix) {
		this.No = No;
		this.sql=sql;
		this.threadPrefix=threadPrefix;
	}

	public void run() {
		Connection con = null;
		Statement st = null;
		int tryTimes = DatabaseAccessFactory.tryTimes;
		int waitMills = DatabaseAccessFactory.waitMills;
		try {
			boolean getGoodConnection = false;
			while (tryTimes-- > -1) {
				try {
					con = DatabaseAccessFactory.getConnection(DatabaseConstants.URL, DatabaseConstants.USER,
							DatabaseConstants.PWD); 
					if (con != null && !con.isClosed() && con.isValid(1)) {
						getGoodConnection = true;
						break;
					}
					Thread.sleep(waitMills);
				} catch (Exception e) {
					Thread.sleep(waitMills);
					continue;
				}
			}
			if (!getGoodConnection) {
				MultiThreadLockDemo.markNullConn();
				System.err.println(this.No + this.threadPrefix+" 线程无法得到 JDBC connection ---- 只能退出 ");
				return;
			}
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			con.setAutoCommit(false); // �??交事务 方便看到阻塞结果
			st = con.createStatement();

			st.execute(MultiThreadLockDemo.currrentSql); // 第一�?�天数�?�库更新命令

			con.commit();
			
			MultiThreadLockDemo.markCommit();
			
			System.out.println(this.No + this.threadPrefix+" 线程完 commit----剩下多少 =" + MultiThreadLockDemo.ThreadAmt);

		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String msg = e.getMessage();
			String msgLock = "Lock wait timeout exceeded";
			String msgClose = "Connection has already been closed";
			String msgAbandon = " Connection has been abandoned";
			String msgTooMannyCon="Too many connections";
			if (msg != null && msg.indexOf(msgLock) > -1) {
				MultiThreadLockDemo.markLockException();
			} else if (msg != null && msg.indexOf(msgAbandon) > -1) {
				MultiThreadLockDemo.marAbandonException();
			} else if (msg != null && msg.indexOf(msgClose) > -1) {
				MultiThreadLockDemo.markClosedExceptionn();
			} else if (msg != null && msg.indexOf(msgTooMannyCon) > -1) {
				MultiThreadLockDemo.markClosedExceptionn();
			} else {
				MultiThreadLockDemo.markOtherExceptionn();
			}
			System.out.println(this.No + this.threadPrefix+" 线程 Error ----------->" + e.getMessage());
			// e.printStackTrace();
		} finally {

			MultiThreadLockDemo.markDone();

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			MultiThreadLockDemo.checkDone();

		}
	}
}

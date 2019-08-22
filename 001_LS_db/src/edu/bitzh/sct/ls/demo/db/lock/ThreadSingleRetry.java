package edu.bitzh.sct.ls.demo.db.lock;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import edu.bitzh.sct.ls.demo.db.transaction.DatabaseAccessFactory;
import edu.bitzh.sct.ls.demo.db.transaction.DatabaseConstants;

class ThreadSingleRetry extends Thread {
	int No = -1;
	String sql;
	String threadPrefix;

	public ThreadSingleRetry(int No,String sql,String threadPrefix) {
		this.No = No;
		this.sql=sql;
		this.threadPrefix=threadPrefix;
	}
	public void run() {
		
		long startT=System.currentTimeMillis();;
		
		Connection con = null;
		Statement st = null;
		int tryTimes = DatabaseAccessFactory.tryTimes;
		int waitMills = DatabaseAccessFactory.waitMills;

		int tryTimesForException = DatabaseAccessFactory.tryTimes;
		int waitMillsForException = DatabaseAccessFactory.waitMills;

		while (tryTimesForException-- > -1) {
			try {
				boolean getGoodConnection = false;
				while (tryTimes-- > -1) {
					try {
						con = DatabaseAccessFactory.getConnection(DatabaseConstants.URL, DatabaseConstants.USER,
								DatabaseConstants.PWD); // connection连接的代�?就�?�??供了注�?�?�?使用�?�例
						if (con != null && !con.isClosed() && con.isValid(1)) {
							getGoodConnection = true;
							break;
						}
						Thread.sleep(waitMills);
					} catch (Exception e) {
						Thread.sleep(waitMills);
						continue; // 跳到 while (tryTimes-- > -1) {
					}
				}
				if (!getGoodConnection) {
					
					MultiThreadLockDemo.markNullConn();
					
					System.err.println(this.No + this.threadPrefix+" 线程无法得到 JDBC connection ---- 只能退出 ： ");
					
					MultiThreadLockDemo.checkDone();
					
					return;
				}
				con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				con.setAutoCommit(false); // �??交事务 方便看到阻塞结果

				st = con.createStatement();

				st.execute(MultiThreadLockDemo.currrentSql); // 第一�?�天数�?�库更新命令

				con.commit();
				
				//性能跟踪
				
				long spendT= System.currentTimeMillis()-startT;
				if (spendT<=5000) {
					MultiThreadLockDemo.ThreadMaxUpdateTimeL5++;
				}
				if (spendT>5000 & spendT<8000) {
					MultiThreadLockDemo.ThreadMaxUpdateTimeG5L8++;
				}
				if (spendT>MultiThreadLockDemo.ThreadMaxUpdateTime) {
					MultiThreadLockDemo.ThreadMaxUpdateTime=spendT;
				}
				
				
				MultiThreadLockDemo.markCommit();
				
				//System.out.println(this.No + this.threadPrefix+"  线程完 commit---- 剩下多少 =" + MultiThreadLockDemo.ThreadAmt+"  | "+spendT);
				
				break;
				
			} catch (Exception e) {
				
				
				try {
					con.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block
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
				System.out.println(this.No + this.threadPrefix+"  线程 Error ----------->" + e.getMessage());
				// e.printStackTrace();
				
				if (MultiThreadLockDemo.isExceptionOfTransactionRetry) {
					try {
						Thread.sleep(waitMillsForException);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					continue;  // 跳到"while (tryTimesForException-- > -1) {
				}
				
			} finally {
				
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
			}
			
		}// end of "while (tryTimesForException-- > -1) {"

		MultiThreadLockDemo.checkDone();
		return;
		
	}
}

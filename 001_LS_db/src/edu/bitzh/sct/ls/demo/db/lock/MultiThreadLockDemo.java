package edu.bitzh.sct.ls.demo.db.lock;

import edu.bitzh.sct.ls.demo.db.transaction.DatabaseAccessFactory;
import edu.bitzh.sct.ls.demo.db.transaction.DatabaseConstants;

public class MultiThreadLockDemo {
	
	public static final String seondKillSQL = "update " + DatabaseConstants.TBL_SECKILL + " set " + DatabaseConstants.COLUMN_AMOUNT + " ="
			+ DatabaseConstants.COLUMN_AMOUNT + "-1 where " + DatabaseConstants.COLUMN_NAME + "='手机01' AND "
			+ DatabaseConstants.COLUMN_CATEGORY + "='秒杀' AND " + DatabaseConstants.COLUMN_AMOUNT + " >0 ;";
	
	public static final String insertSQL = "insert " + DatabaseConstants.TBL_SECKILL + " values( " + DatabaseConstants.COLUMN_AMOUNT + " ="
			+ DatabaseConstants.COLUMN_AMOUNT + "-1 where " + DatabaseConstants.COLUMN_NAME + "='手机01' AND "
			+ DatabaseConstants.COLUMN_CATEGORY + "='秒杀' AND " + DatabaseConstants.COLUMN_AMOUNT + " >0 ;";
	
	public static String currrentSql=null;
	
	public static int ThreadAmtOfNullConn=0;
	
	public static int ThreadAmtOfExceptionLock=0;
	public static int ThreadAmtOfExceptionConnAbandon=0;
	public static int ThreadAmtOfExceptionConnClosed=0;
	public static int ThreadAmtOfExceptionTooManyConn=0;
	public static int ThreadAmtOfException=0;
	
	public static int ThreadAmtOfGoodToCommit=0;
	
	public static int ThreadAmtOLD=5000;  // 设置多少 Thread 线程并发测试
	public static int ThreadAmt=-1;
	public static long s=-1;
	
	
	public static boolean isExceptionOfTransactionRetry=true;
	
	public static void main(String[] args) throws Exception {
	
		DatabaseAccessFactory.isUsingPool=true;
		DatabaseAccessFactory.isUsingTomcatPool=false;
		
		testDeadLoad();
	
	}
	
	public static synchronized void markDone()  {
		ThreadAmt--;
	}
	public static synchronized void markCommit()  {
		ThreadAmtOfGoodToCommit++;
	}
	public static synchronized void markLockException()  {
		ThreadAmtOfExceptionLock++;
	}
	public static synchronized void markNullConn()  {
		ThreadAmtOfNullConn++;
	}
	public static synchronized void marAbandonException()  {
		ThreadAmtOfExceptionConnAbandon++;
	}
	public static synchronized void markClosedExceptionn()  {
		ThreadAmtOfExceptionConnClosed++;
	}
	public static synchronized void markTooManyConExceptionn()  {
		ThreadAmtOfExceptionTooManyConn++;
	}
	public static synchronized void markOtherExceptionn()  {
		ThreadAmtOfException++;
	}
	public static void testDeadLoad() throws Exception {
		
		ThreadAmt=ThreadAmtOLD;
		
		s=System.currentTimeMillis();
		
		System.err.println("========== 开始 =========");
		//Thread1 t1 = null;
		
		ThreadSingleRetry t= null;
		
		int threads=ThreadAmt;
		
		currrentSql=seondKillSQL;
		
		for(int i=0; i<threads; i++) {
			
			t = new ThreadSingleRetry(i,currrentSql,"序号：");
			t.start();
		}	
		
		//CheckJVM.display();
	}
	public static void checkDone() {
		
		markDone();
		
		if (MultiThreadLockDemo.ThreadAmt<=0) {
			System.err.println("===="+DatabaseAccessFactory.connPrefix
					+" | "+DatabaseAccessFactory.tryTimes+"次/"+DatabaseAccessFactory.waitMills+"ms | 全部  "+MultiThreadLockDemo.ThreadAmtOLD
					+" Thread 执行完毕  Commit: "+ThreadAmtOfGoodToCommit
					+"  事物成功率 ： "+Math.floorDiv(ThreadAmtOfGoodToCommit*100,ThreadAmtOLD)
					+" | 时间花费 ： "+(System.currentTimeMillis()-MultiThreadLockDemo.s)/1000+" 秒 "
					+"| 每秒成功笔数 ：  "+Math.floorDiv(ThreadAmtOfGoodToCommit,(System.currentTimeMillis()-MultiThreadLockDemo.s)/1000)
					+"| lock exception= "+MultiThreadLockDemo.ThreadAmtOfExceptionLock
					+"| Closed Exception ="+MultiThreadLockDemo.ThreadAmtOfExceptionConnClosed
					+"| Abandon Exception ="+MultiThreadLockDemo.ThreadAmtOfExceptionConnAbandon
			        +"| Null Connection= "+MultiThreadLockDemo.ThreadAmtOfNullConn
			        +"| Exception="+MultiThreadLockDemo.ThreadAmtOfException
			        +"| Too Many Connection Exception = "+MultiThreadLockDemo.ThreadAmtOfExceptionTooManyConn
			        +" || Isolation level= "+DatabaseAccessFactory.isoLevel
					);  
			System.err.println("==== "+currrentSql);
		}
	}
}

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
	
	public static int ThreadAmtOfTryTimes=0;
	public static int ThreadAmtOfNullConn=0;
	
	public static int ThreadAmtOfExceptionLock=0;
	public static int ThreadAmtOfExceptionConnAbandon=0;
	public static int ThreadAmtOfExceptionConnClosed=0;
	public static int ThreadAmtOfExceptionTooManyConn=0;
	public static int ThreadAmtOfException=0;
	
	public static int ThreadAmtOfGoodToCommit=0;
	
	public static int ThreadAmtOLD=1000;  // 设置多少 Thread 线程并发测试
	public static int ThreadAmt=-1;
	
	public static long ThreadMaxUpdateTime500=0;
	public static long ThreadMaxUpdateTimeL1=0;
	
	public static long ThreadMaxUpdateTime=0;
	public static long ThreadMaxUpdateTimeL2=0;
	public static long ThreadMaxUpdateTimeL5=0;
	public static long ThreadMaxUpdateTimeG5L8=0;
	
	public static long s=-1;
	
	
	public static int T1000W=10000*1000;
	public static int T100W=10000*100;
	public static int T10W=10000*10;
	public static int T1W=10000;
	public static int T1000=1000;
	
	public static boolean isExceptionOfTransactionRetry=true;
	
	public static void main(String[] args) throws Exception {
		
		/********************************************************
		 *           设置需要测试获得 JDBC 连接的方法， 一般JDBC， Tomcat Pool， 
		 *           hikariPoolDS， 详见 DatabaseAccessFactory.java
		*********************************************************/
	
		DatabaseAccessFactory.isUsingPool=true;
		
		DatabaseAccessFactory.isUsingTomcatPool=false;
		
		/********************************************************
		 *           设置需要测试的线程数与 ThreadAmtOLD
		*********************************************************/
		ThreadAmtOLD=5;
		
		ThreadAmt=ThreadAmtOLD;	

		testDeadLoad();
	
	}
	public static void testDeadLoad() throws Exception {
		

		
		s=System.currentTimeMillis();
		
		System.err.println("========== MultiThreadLockDemo 开始启动 thread =========");
		//Thread1 t1 = null;
		
		ThreadSingleRetry t= null;
		
		int threads=ThreadAmt;
		
		currrentSql=seondKillSQL;
		
		for(int i=0; i<threads; i++) {
			
			t = new ThreadSingleRetry(i,currrentSql,"序号：");
			t.start();
		}	
		System.err.println("==========    End        =========");
		//CheckJVM.display();
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
	
	public static synchronized void markDuration(long spendT)  {
		if (spendT<=500) {
			ThreadMaxUpdateTime500++;
		}
		if (spendT<=1000) {
			ThreadMaxUpdateTimeL1++;
		}
		
		if (spendT<=2000) {
			ThreadMaxUpdateTimeL2++;
		}
		if (spendT<=5000) {
			ThreadMaxUpdateTimeL5++;
		}
		if (spendT>5000 & spendT<8000) {
			ThreadMaxUpdateTimeG5L8++;
		}
		if (spendT>ThreadMaxUpdateTime) {
			ThreadMaxUpdateTime=spendT;
		}
	}
	public static void checkDone() {
		
		markDone();
		if (Math.floorMod(ThreadAmt, 10000)==0){
			System.err.print("---Finished ");
			System.err.println("commit的线程数目 （每1万) :   "+ (ThreadAmtOLD-ThreadAmt));
		}
		if (ThreadAmt<=0) {
			System.err.println("====  "+DatabaseAccessFactory.connPrefix
					+" |"+DatabaseAccessFactory.tryTimes+"次/"+DatabaseAccessFactory.waitMills+"ms | 全部  "+ThreadAmtOLD
					+" |"+" Thread 执行完毕  Commit: "+ThreadAmtOfGoodToCommit
					+" |"+"   事物成功率 "+Math.floorDiv(ThreadAmtOfGoodToCommit*100,ThreadAmtOLD)
					+" |"+"   时间花费 ： "+(System.currentTimeMillis()-s)/1000+" 秒 "
					+" |"+"    每秒成功笔数  ：  "+Math.floorDiv(ThreadAmtOfGoodToCommit,(System.currentTimeMillis()-s)/1000)
					+" |"+" <= 500  毫秒的笔数 ：  "+ThreadMaxUpdateTime500
					+" |"+" <= 1  秒的笔数 ：  "+ThreadMaxUpdateTimeL1
					+" |"+" <= 2  秒的笔数 ：  "+ThreadMaxUpdateTimeL2
				    +" |"+" <=5 秒的笔数 ：  "+ThreadMaxUpdateTimeL5
					+" |"+" <8 秒 >5 秒的笔数 ：  "+ThreadMaxUpdateTimeG5L8
					+" |"+"  最长操作时间== "+ThreadMaxUpdateTime/1000+" (秒)"	
					+" | lock exception= "+ThreadAmtOfExceptionLock
					+" | Closed Exception ="+ThreadAmtOfExceptionConnClosed
					+" | Abandon Exception ="+ThreadAmtOfExceptionConnAbandon
			        +" | Null Connection/try times= "+ThreadAmtOfNullConn+"/"+ThreadAmtOfTryTimes
			        +" | Exception="+ThreadAmtOfException
			        +" | Too Many Connection Exception = "+ThreadAmtOfExceptionTooManyConn
			        				
			        );  
			System.err.println("==== "+currrentSql);
		}
	}
	public static void clean() {
		ThreadAmtOfTryTimes=0;
		ThreadAmtOfNullConn=0;
		
		ThreadAmtOfExceptionLock=0;
		ThreadAmtOfExceptionConnAbandon=0;
		ThreadAmtOfExceptionConnClosed=0;
		ThreadAmtOfExceptionTooManyConn=0;
		ThreadAmtOfException=0;
		
		ThreadAmtOfGoodToCommit=0;
		
		ThreadAmtOLD=1000;  // 设置多少 Thread 线程并发测试
		ThreadAmt=-1;
		
		ThreadMaxUpdateTime500=0;
		ThreadMaxUpdateTimeL1=0;
		
		ThreadMaxUpdateTime=0;
		ThreadMaxUpdateTimeL2=0;
		ThreadMaxUpdateTimeL5=0;
		ThreadMaxUpdateTimeG5L8=0;
	}
}

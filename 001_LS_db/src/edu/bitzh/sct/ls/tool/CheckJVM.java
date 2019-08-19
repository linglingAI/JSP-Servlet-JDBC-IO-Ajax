package edu.bitzh.sct.ls.tool;

import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

public class CheckJVM {

	public static void main(String[] args) {
		display();
	}
	public static void display() {
		// TODO Auto-generated method stub
		
		boolean needMoreDetail=false;
		int byteToMb = 1024 * 1024;
		
		MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();   
	    MemoryUsage usage = memorymbean.getHeapMemoryUsage();   
	    System.out.println("INIT HEAP: " + usage.getInit());   
	    System.out.println("MAX HEAP: " + usage.getMax());   
	    System.out.println("USE HEAP: " + usage.getUsed());   
	    System.out.println("\nFull Information:");   
	    System.out.println("Heap Memory Usage: "   
	    + memorymbean.getHeapMemoryUsage());   
	    System.out.println("Non-Heap Memory Usage: "   
	    + memorymbean.getNonHeapMemoryUsage());   
	      
	    List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();   
	    System.err.println("===================java options=============== ");  
	    System.out.println(inputArguments);  
	  
	      
	      
	    System.err.println("=======================通过java来获取相关系统状态============================ ");  
	    
	    int i = (int)Runtime.getRuntime().totalMemory()/byteToMb;//Java 虚拟机中的内存总量,以字节为单位  
	    
	    System.out.println("JVM内存总的内存量 i is "+i+" MB");  
	    
	    int j = (int)Runtime.getRuntime().freeMemory()/byteToMb;//Java 虚拟机中的空闲内存量  
	    System.out.println("JVM内存空闲内存量 j is "+j +" MB");  
	    
	    System.out.println("JVM内存最大内存量 is "+Runtime.getRuntime().maxMemory()/byteToMb+" MB");  
	  
	    if (! needMoreDetail) {
	    	return;
	    }
	    
	    System.err.println("       获取操作系统相关信息 =======================OperatingSystemMXBean============================ ");  
	    OperatingSystemMXBean osm = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();  
       
	    
	      
	    //  
	    System.out.println("osm.getArch() "+osm.getArch());  
	    System.out.println("osm.getAvailableProcessors() "+osm.getAvailableProcessors());  
	    //System.out.println("osm.getCommittedVirtualMemorySize() "+osm.getCommittedVirtualMemorySize());  
	    System.out.println("osm.getName() "+osm.getName());  
	    //System.out.println("osm.getProcessCpuTime() "+osm.getProcessCpuTime());  
	    System.out.println("osm.getVersion() "+osm.getVersion());  
	    
	    //获取整个虚拟机内存使用情况  
	    System.err.println("      获取整个虚拟机内存使用情况 =======================MemoryMXBean============================ ");  
	    MemoryMXBean mm=(MemoryMXBean)ManagementFactory.getMemoryMXBean();  
	    System.out.println("getHeapMemoryUsage "+mm.getHeapMemoryUsage());  
	    System.out.println("getNonHeapMemoryUsage "+mm.getNonHeapMemoryUsage());  
	    
	    //获取各个线程的各种状态，CPU 占用情况，以及整个系统中的线程状况  
	    System.err.println("      获取各个线程的各种状态 =======================ThreadMXBean============================ ");  
	    ThreadMXBean tm=(ThreadMXBean)ManagementFactory.getThreadMXBean();  
	    System.out.println("getThreadCount "+tm.getThreadCount());  
	    System.out.println("getPeakThreadCount "+tm.getPeakThreadCount());  
	    System.out.println("getCurrentThreadCpuTime "+tm.getCurrentThreadCpuTime());  
	    System.out.println("getDaemonThreadCount "+tm.getDaemonThreadCount());  
	    System.out.println("getCurrentThreadUserTime "+tm.getCurrentThreadUserTime());  
	      
	    //当前编译器情况  
	    System.err.println("      当前编译器情况 =======================CompilationMXBean============================ ");  
	    CompilationMXBean gm=(CompilationMXBean)ManagementFactory.getCompilationMXBean();  
	    System.out.println("getName "+gm.getName());  
	    System.out.println("getTotalCompilationTime "+gm.getTotalCompilationTime());  
	      
	    //获取多个内存池的使用情况  
	    System.err.println("       获取多个内存池的使用情况  =======================MemoryPoolMXBean============================ ");  
	    List<MemoryPoolMXBean> mpmList=ManagementFactory.getMemoryPoolMXBeans();  
	    for(MemoryPoolMXBean mpm:mpmList){  
	        System.out.println("getUsage "+mpm.getUsage());  
	        System.out.println("getMemoryManagerNames "+mpm.getMemoryManagerNames().toString());  
	    }  
	    //获取GC的次数以及花费时间之类的信息  
	    System.err.println("      获取GC的次数以及花费时间之类的信息   =======================MemoryPoolMXBean============================ ");  
	    List<GarbageCollectorMXBean> gcmList=ManagementFactory.getGarbageCollectorMXBeans();  
	    for(GarbageCollectorMXBean gcm:gcmList){  
	        System.out.println("getName "+gcm.getName());  
	        System.out.println("getMemoryPoolNames "+gcm.getMemoryPoolNames());  
	    }  
	    //获取运行时信息  
	    System.err.println("     获取运行时信息 =======================RuntimeMXBean============================ ");  
	    RuntimeMXBean rmb=(RuntimeMXBean)ManagementFactory.getRuntimeMXBean();  
	    System.out.println("getClassPath "+rmb.getClassPath());  
	    System.out.println("getLibraryPath "+rmb.getLibraryPath());  
	    System.out.println("getVmVersion "+rmb.getVmVersion()); 
	}

}

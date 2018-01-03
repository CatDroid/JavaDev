package useZip;

import java.util.zip.ZipFile;
import static java.lang.System.out;

public class useZip {

	
	public static void main(String args[] ) {
		
	
		out.println("Java运行时环境版本:"+System.getProperty("java.version"));   	  
		out.println("Java 运行时环境供应商:"+System.getProperty("java.vendor"));   
		out.println("Java 供应商的URL:"+System.getProperty("java.vendor.url"));   
		out.println("Java安装目录:"+System.getProperty("java.home"));   
		out.println("Java 虚拟机规范版本:"+System.getProperty("java.vm.specification.version"));   
		out.println("Java 类格式版本号:"+System.getProperty("java.class.version"));   
		out.println("Java类路径："+System.getProperty("java.class.path"));   
		out.println("加载库时搜索的路径列表:"+System.getProperty("java.library.path"));   
		out.println("默认的临时文件路径:"+System.getProperty("java.io.tmpdir"));   
		out.println("要使用的 JIT 编译器的名称:"+System.getProperty("java.compiler"));   
		out.println("一个或多个扩展目录的路径:"+System.getProperty("java.ext.dirs"));   
		out.println("操作系统的名称:"+System.getProperty("os.name"));   
		out.println("操作系统的架构:"+System.getProperty("os.arch"));   
		out.println("操作系统的版本:"+System.getProperty("os.version"));   
		out.println("文件分隔符（在 UNIX 系统中是“/”）:"+System.getProperty("file.separator"));   
		out.println("路径分隔符（在 UNIX 系统中是“:”）:"+System.getProperty("path.separator"));   
		out.println("行分隔符（在 UNIX 系统中是“/n”）:"+System.getProperty("line.separator"));   
		out.println("用户的账户名称:"+System.getProperty("user.name"));   
		out.println("用户的主目录:"+System.getProperty("user.home"));   
		out.println("用户的当前工作目录:"+System.getProperty("user.dir"));  // 工作目录 
		
		   
		//new File("");
		//ZipFile apkZipFile = new ZipFile(apkFile);
		
		
		
		
		return ;
	}
}

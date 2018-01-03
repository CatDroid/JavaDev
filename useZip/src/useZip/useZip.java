package useZip;

import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.System.out;

public class useZip {

	public static void main(String args[]) {

		out.println("Java运行时环境版本:" + System.getProperty("java.version"));
		out.println("Java 运行时环境供应商:" + System.getProperty("java.vendor"));
		out.println("Java 供应商的URL:" + System.getProperty("java.vendor.url"));
		out.println("Java安装目录:" + System.getProperty("java.home"));
		out.println("Java 虚拟机规范版本:" + System.getProperty("java.vm.specification.version"));
		out.println("Java 类格式版本号:" + System.getProperty("java.class.version"));
		out.println("Java类路径：" + System.getProperty("java.class.path"));
		out.println("加载库时搜索的路径列表:" + System.getProperty("java.library.path"));
		out.println("默认的临时文件路径:" + System.getProperty("java.io.tmpdir"));
		out.println("要使用的 JIT 编译器的名称:" + System.getProperty("java.compiler"));
		out.println("一个或多个扩展目录的路径:" + System.getProperty("java.ext.dirs"));
		out.println("操作系统的名称:" + System.getProperty("os.name"));
		out.println("操作系统的架构:" + System.getProperty("os.arch"));
		out.println("操作系统的版本:" + System.getProperty("os.version"));
		out.println("文件分隔符（在 UNIX 系统中是“/”）:" + System.getProperty("file.separator"));
		out.println("路径分隔符（在 UNIX 系统中是“:”）:" + System.getProperty("path.separator"));
		out.println("行分隔符（在 UNIX 系统中是“/n”）:" + System.getProperty("line.separator"));
		out.println("用户的账户名称:" + System.getProperty("user.name"));
		out.println("用户的主目录:" + System.getProperty("user.home"));
		out.println("用户的当前工作目录:" + System.getProperty("user.dir")); // 工作目录

		try {
			File apkFile = new File("myzip.zip");
			ZipFile apkZipFile = new ZipFile(apkFile);

			Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) apkZipFile.entries();

			boolean unzipOneFile = false ;
			ZipEntry entry = null;
			while (entries.hasMoreElements()) {
				entry = entries.nextElement();
				if (entry.isDirectory()) {
					out.println(entry.getName() + " is dir ");
				}else {
					out.println(entry.getName() + " is File ");
					if(!unzipOneFile) {
						String file_path = entry.getName().substring( entry.getName().lastIndexOf('/') + 1 ,
								entry.getName().length() 
							)  ; // zip包内用 '/' 而不是 File.separator
						File outFile = new File( file_path );
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
						BufferedInputStream  bis = new BufferedInputStream(apkZipFile.getInputStream(entry));
						int count = 0 ;
						int bufferSize = 1024;
						byte[] buffer = new byte[bufferSize];
						while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
							bos.write(buffer, 0, count);
						}
						bos.flush();
						bos.close();
						bis.close();
						unzipOneFile = true ;
					}
				}
			}
			

			
			/*
			 * 
					myzip/ is dir 
					myzip/temp.jpg is File 
					myzip/zlib/ is dir 
					myzip/zlib/libok.so is File 
					myzip/zlib/ok.txt is File 
			 * */
			apkZipFile.close();
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}

	@SuppressWarnings("unchecked")
	public static void unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName){

		if (isEmpty(zipFilePath) || isEmpty(unzipFilePath)) {
			out.println("unzip zipFilePath is null ");
			return ;
		}

		File zipFile = new File(zipFilePath);
		// 是否在给定解压后的目录unzipFilePath,增加压缩包的名字  
		if (includeZipFileName) {
			String fileName = zipFile.getName();
			if (isNotEmpty(fileName)) {
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
			}
			unzipFilePath = unzipFilePath + File.separator + fileName;
		}

		// 创建解压缩文件保存的路径
		File unzipFileDir = new File(unzipFilePath);
		if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
			unzipFileDir.mkdirs();
		}

		// 开始解压
		ZipEntry entry = null;
 
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		ZipFile zip;
		try {
			zip = new ZipFile(zipFile);
		} catch (ZipException e) {
			out.println("unzip file " + entry.getName() + " exception :" + e);
			e.printStackTrace();
			return ;
		} catch (IOException e) {
			out.println("unzip file " + entry.getName() + " exception :" + e);
			e.printStackTrace();
			return ;
		} 
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
		
		
		// 循环对压缩包里的每一个文件进行解压
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			if (entry.isDirectory()) {
				continue;
			}
			// 构建压缩包中一个文件解压后保存的文件全路径
			String entryFilePath = unzipFilePath + File.separator + entry.getName();
			String entryDirPath = null;
			// 构建解压后保存的文件夹路径
			int index = entryFilePath.lastIndexOf(File.separator) > entryFilePath.lastIndexOf("/")
					? entryFilePath.lastIndexOf(File.separator)
					: entryFilePath.lastIndexOf("/");
			if (index != -1) {
				entryDirPath = entryFilePath.substring(0, index);
			} else {
				entryDirPath = "";
			}
			File entryDir = new File(entryDirPath);
			// 如果文件夹路径不存在，则创建文件夹
			if (!entryDir.exists() || !entryDir.isDirectory()) {
				entryDir.mkdirs();
			}

			// 创建解压文件
			File entryFile = new File(entryFilePath);
			if (entryFile.exists()) {
				/*
				 * // 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException SecurityManager securityManager =
				 * new SecurityManager(); securityManager.checkDelete(entryFilePath);
				 */
				// 删除已存在的目标文件
				entryFile.delete();
			}

			// 写入文件
			try {
				int count = 0 ;
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(entryFile));
				BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));
				while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
					bos.write(buffer, 0, count);
				}
				bos.flush();
				bos.close();
				bis.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				out.println("unzip file " + entry.getName() + " exception :" + e);
			}

		}

	}

	@SuppressWarnings("unchecked")
	public static void unPartZip(String zipFilePath, String unzipFilePath) throws Exception {
		if (isEmpty(zipFilePath) || isEmpty(unzipFilePath)) {
			throw new Exception("PARAMETER_IS_NULL");
		}
		File zipFile = new File(zipFilePath);

		// 创建解压缩文件保存的路径
		File unzipFileDir = new File(unzipFilePath);
		if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
			unzipFileDir.mkdirs();
		}

		// 开始解压
		ZipEntry entry = null;
		String entryFilePath = null, entryDirPath = null;
		File entryFile = null, entryDir = null;
		int index = 0, count = 0, bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		// E:\projectTest\downloadPath\payseat_1.0
		ZipFile zip = new ZipFile(zipFile);
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
		// 循环对压缩包里的每一个文件进行解压
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			if (entry.isDirectory()) {
				continue;
			}
			// 构建压缩包中一个文件解压后保存的文件全路径
			entryFilePath = unzipFilePath + File.separator + entry.getName();
			// 构建解压后保存的文件夹路径
			index = entryFilePath.lastIndexOf(File.separator) > entryFilePath.lastIndexOf("/")
					? entryFilePath.lastIndexOf(File.separator)
					: entryFilePath.lastIndexOf("/");
			if (index != -1) {
				entryDirPath = entryFilePath.substring(0, index);
			} else {
				entryDirPath = "";
			}
			entryDir = new File(entryDirPath);
			// 如果文件夹路径不存在，则创建文件夹
			if (!entryDir.exists() || !entryDir.isDirectory()) {
				entryDir.mkdirs();
			}

			// 创建解压文件
			entryFile = new File(entryFilePath);
			if (entryFile.exists()) {
				/*
				 * // 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException SecurityManager securityManager =
				 * new SecurityManager(); securityManager.checkDelete(entryFilePath);
				 */
				// 删除已存在的目标文件
				entryFile.delete();
			}
			try {
				// 写入文件
				bos = new BufferedOutputStream(new FileOutputStream(entryFile));
				bis = new BufferedInputStream(zip.getInputStream(entry));
				while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
					bos.write(buffer, 0, count);
				}
				bos.flush();
				bos.close();
				bis.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				out.println("unzip file " + entry.getName() + " exception :" + e);
			}

		}

	}

	public static boolean isEmpty(String s) {
		if (s == null || s.equals(""))
			return true;
		else
			return false;
	}

	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}

}

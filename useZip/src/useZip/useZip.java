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

		out.println("Java����ʱ�����汾:" + System.getProperty("java.version"));
		out.println("Java ����ʱ������Ӧ��:" + System.getProperty("java.vendor"));
		out.println("Java ��Ӧ�̵�URL:" + System.getProperty("java.vendor.url"));
		out.println("Java��װĿ¼:" + System.getProperty("java.home"));
		out.println("Java ������淶�汾:" + System.getProperty("java.vm.specification.version"));
		out.println("Java ���ʽ�汾��:" + System.getProperty("java.class.version"));
		out.println("Java��·����" + System.getProperty("java.class.path"));
		out.println("���ؿ�ʱ������·���б�:" + System.getProperty("java.library.path"));
		out.println("Ĭ�ϵ���ʱ�ļ�·��:" + System.getProperty("java.io.tmpdir"));
		out.println("Ҫʹ�õ� JIT ������������:" + System.getProperty("java.compiler"));
		out.println("һ��������չĿ¼��·��:" + System.getProperty("java.ext.dirs"));
		out.println("����ϵͳ������:" + System.getProperty("os.name"));
		out.println("����ϵͳ�ļܹ�:" + System.getProperty("os.arch"));
		out.println("����ϵͳ�İ汾:" + System.getProperty("os.version"));
		out.println("�ļ��ָ������� UNIX ϵͳ���ǡ�/����:" + System.getProperty("file.separator"));
		out.println("·���ָ������� UNIX ϵͳ���ǡ�:����:" + System.getProperty("path.separator"));
		out.println("�зָ������� UNIX ϵͳ���ǡ�/n����:" + System.getProperty("line.separator"));
		out.println("�û����˻�����:" + System.getProperty("user.name"));
		out.println("�û�����Ŀ¼:" + System.getProperty("user.home"));
		out.println("�û��ĵ�ǰ����Ŀ¼:" + System.getProperty("user.dir")); // ����Ŀ¼

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
							)  ; // zip������ '/' ������ File.separator
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
		// �Ƿ��ڸ�����ѹ���Ŀ¼unzipFilePath,����ѹ����������  
		if (includeZipFileName) {
			String fileName = zipFile.getName();
			if (isNotEmpty(fileName)) {
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
			}
			unzipFilePath = unzipFilePath + File.separator + fileName;
		}

		// ������ѹ���ļ������·��
		File unzipFileDir = new File(unzipFilePath);
		if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
			unzipFileDir.mkdirs();
		}

		// ��ʼ��ѹ
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
		
		
		// ѭ����ѹ�������ÿһ���ļ����н�ѹ
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			if (entry.isDirectory()) {
				continue;
			}
			// ����ѹ������һ���ļ���ѹ�󱣴���ļ�ȫ·��
			String entryFilePath = unzipFilePath + File.separator + entry.getName();
			String entryDirPath = null;
			// ������ѹ�󱣴���ļ���·��
			int index = entryFilePath.lastIndexOf(File.separator) > entryFilePath.lastIndexOf("/")
					? entryFilePath.lastIndexOf(File.separator)
					: entryFilePath.lastIndexOf("/");
			if (index != -1) {
				entryDirPath = entryFilePath.substring(0, index);
			} else {
				entryDirPath = "";
			}
			File entryDir = new File(entryDirPath);
			// ����ļ���·�������ڣ��򴴽��ļ���
			if (!entryDir.exists() || !entryDir.isDirectory()) {
				entryDir.mkdirs();
			}

			// ������ѹ�ļ�
			File entryFile = new File(entryFilePath);
			if (entryFile.exists()) {
				/*
				 * // ����ļ��Ƿ�����ɾ�������������ɾ���������׳�SecurityException SecurityManager securityManager =
				 * new SecurityManager(); securityManager.checkDelete(entryFilePath);
				 */
				// ɾ���Ѵ��ڵ�Ŀ���ļ�
				entryFile.delete();
			}

			// д���ļ�
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

		// ������ѹ���ļ������·��
		File unzipFileDir = new File(unzipFilePath);
		if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
			unzipFileDir.mkdirs();
		}

		// ��ʼ��ѹ
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
		// ѭ����ѹ�������ÿһ���ļ����н�ѹ
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			if (entry.isDirectory()) {
				continue;
			}
			// ����ѹ������һ���ļ���ѹ�󱣴���ļ�ȫ·��
			entryFilePath = unzipFilePath + File.separator + entry.getName();
			// ������ѹ�󱣴���ļ���·��
			index = entryFilePath.lastIndexOf(File.separator) > entryFilePath.lastIndexOf("/")
					? entryFilePath.lastIndexOf(File.separator)
					: entryFilePath.lastIndexOf("/");
			if (index != -1) {
				entryDirPath = entryFilePath.substring(0, index);
			} else {
				entryDirPath = "";
			}
			entryDir = new File(entryDirPath);
			// ����ļ���·�������ڣ��򴴽��ļ���
			if (!entryDir.exists() || !entryDir.isDirectory()) {
				entryDir.mkdirs();
			}

			// ������ѹ�ļ�
			entryFile = new File(entryFilePath);
			if (entryFile.exists()) {
				/*
				 * // ����ļ��Ƿ�����ɾ�������������ɾ���������׳�SecurityException SecurityManager securityManager =
				 * new SecurityManager(); securityManager.checkDelete(entryFilePath);
				 */
				// ɾ���Ѵ��ڵ�Ŀ���ļ�
				entryFile.delete();
			}
			try {
				// д���ļ�
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

package ģ����ͷ��ͷ���;

import static java.lang.System.out;


public class Demo<T> { // ������
	
	void dump( T  t) { // ���ͷ���  ģ��������͸��桮��ʵ����
		out.println( "non-static dump = " + t );
	}
	
	//static void sdump( T t ) // ��̬����  ����ʹ�����ģ�����(��̬���������Է������϶���ķ���)
	static <T> void sdump(T t) // ���ͷ���  ���ݵ���ʱ��Ĳ���
	{
		out.println( "static dump = " + t ) ;
	}
}
package 模版类和泛型方法;

import static java.lang.System.out;


public class Demo<T> { // 泛型类
	
	void dump( T  t) { // 泛型方法  模版参数类型跟随‘类实例’
		out.println( "non-static dump = " + t );
	}
	
	//static void sdump( T t ) // 静态方法  不能使用类的模版参数(静态方法不可以访问类上定义的泛型)
	static <T> void sdump(T t) // 泛型方法  根据调用时候的参数
	{
		out.println( "static dump = " + t ) ;
	}
}
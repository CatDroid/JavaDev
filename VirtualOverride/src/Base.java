
import static java.lang.System.out;

public class Base {

	Base(){
		out.println("Base");
	}
	
	int call(int input) {
		out.println("Base input = " + input );
		return input+1 ;
	}
	
	/*
	 	Java中其实没有虚函数的概念，它的普通函数就相当于C++的虚函数，动态绑定是Java的默认行为
	 	如果Java中不希望某个函数具有虚函数特性，可以加上final关键字变成非虚函数
	 */
	final int callFinal(int input) { // 不能Override 
		out.println("Base callFinal = " + input );
		return input+1 ;
	}
}

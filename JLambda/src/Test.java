import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.lang.System.out;

import java.security.PrivilegedAction;


public class Test {

	// 编译器会根据接口的结构自行判断
	// 判断过程并非简单的对接口方法计数：
	// 一个接口可能冗余的定义了一个Object已经提供的方法，比如toString()，或者定义了静态方法或默认方法，
	// 这些都不属于函数式接口方法的范畴

	// 可以通过@FunctionalInterface注解来显式指定一个接口是函数式接口
	
	// 这种接口叫做  函数式接口 
	@FunctionalInterface
	interface TestListener{ 
		public int callback(String... strings);
	}
	
	@FunctionalInterface
	interface ValueLister{
		public float returnVal();
	}
	
	// 传递函数 作为 参数 
	public static void TransferFunction(TestListener tl) {
		tl.callback("TransferFunction","arg1","arg2");
	}
	
	public static void TransferFunction2(ValueLister val) {
		out.println("函数式接口lambda = " + val.returnVal() );
	}
	
	
	// 函数返回闭包
	public static TestListener ReturnClosure(String func_arg ) {
		String local_arg = "This Is Local Arg In Function";
		// Lambda   闭包特性: 可记录(包含)  创建它的时候 所在区域的信息(变量)
		TestListener a =		(String... strings)->{
					out.println("闭包   " + local_arg );  
					out.println("闭包   " + func_arg  );
					for(String str : strings) {
						out.println("lambda = " + str);
					}
					return 2 ; // 如果函数式接口定义返回值  lambda必须也返回值
				} ;
		
		// Java SE 7中，编译器对内部类中引用的外部变量（即捕获的变量）要求非常严格：如果捕获的变量没有被声明为final就会产生一个编译错误
		// Java SE 8中，捕获那些符合有效只读（Effectively final）的局部变量
		
		//local_arg = "123"; // local_arg 必须是 final 或者 等效final    容易引起race condition
		return  a ;
	}
	
	// Predicate是java.util包中的  模板 + 函数式接口 
	public static void evaluate(List<Integer> list, Predicate<Integer> predicate) {
	    for(Integer n: list)  {
	        if(predicate.test(n)) {
	            System.out.println(n + " ");
	        }
	    }
	}
	
	
	public static void main(String[] args) {
		
		int[] a = new int[12];
		for(int temp : a) {
			out.printf("k:%d\n", temp  ); // 跟C++一样的输出
		}
		 
		// 遍历Map
		Map<String, Integer> items = new HashMap<String, Integer>();
		items.put("A", 10);
		items.put("B", 20);
		items.put("C", 30);
		items.put("D", 40);
		items.put("E", 50);
		items.put("F", 60);
		
//		long normal_cost = 0;
//		long lambda_cost = 0;	
//		long time = 0 ;
//		time = System.currentTimeMillis();
//		for(int i = 0 ; i < 10000 ; i++ )
		
			//  Java8 之前的方式 
			for (Map.Entry<String, Integer> entry : items.entrySet()  ) {
			    out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
			}

//		
//		normal_cost =  System.currentTimeMillis() - time ;
//		time = System.currentTimeMillis(); 
//		for(int i = 0 ; i < 10000 ; i++ )
			
			// Java 8 lambda
			items.forEach(  
					(k,v)->{
						out.println("Item : " + k + " Count : " + v);
					}
			);		

			
		// 实际测试 普通的比lambda要快
//		lambda_cost = System.currentTimeMillis() - time ;
//		out.printf("normal %d lambda %d best %s",normal_cost , lambda_cost , 
//				normal_cost > lambda_cost?"normal":"lambda"
//				);
		
	
		items.forEach(
				(k,v)->{
					out.println("Item : " + k + " Count : " + v);
					if("E".equals(k)){
						out.println("Hello E");
					}
				}
		);
		
		
		/* 在 Java 中定义的函数或方法不可能完全独立，也不能将方法作为参数或返回一个方法给实例
		       我们总是通过匿名类(new某个Interface并override实现其方法)给方法传递函数功能 
		
		  	函数对 Java 而言并不重要，在 Java 的世界里，""函数无法独立存在""
			在函数式编程语言中，函数是一等公民，它们可以独立存在，你可以将其赋值给一个变量，或将他们当做参数传给其他函数
			函数式语言，提供了一种强大的功能——""闭包""  
			闭包是一个可调用的""对象""，它记录了一些信息，这些信息来自于""创建它的作用域""
		
			@python 
			def func():
				print "this is func"
				pass
				print (type(func)) 		### <type 'function'>  
			在python中 func是一个函数类型的对象 
		
			在函数式编程语言中，Lambda表达式的"""类型是函数"""
			在Java中，Lambda表达式是对象，他们必须依附于一种特别的对象"""类型---函数式接口"""(functional interface)
		
			现代编程语言必须包含闭包这类特性  ???
			
			lambda表达：  
			1. 类似匿名函数，没有声明的方法，即没有访问修饰符、返回值声明和名字
			2. 只有一个参数 ()  只有一个语句{} 都可以省略  
			3. 参数类型可以省略自动推导
			4. 返回类型不用写    返回类型与代码块的返回类型一致  
		*/
		TransferFunction(new TestListener() {  // 匿名类 
			@Override
			public int callback(String... strings) {
				for(String str : strings) {
					out.println("new 匿名类 = " + str);
				}
				return 1 ;
			}
			
		});
		
		TransferFunction( ReturnClosure("This is Funciton Arg in Function"));
		
		TransferFunction2( ()->3.1415f ); // 简洁写法， 相当于  ()-->{return 43}; 
		
		// 每个Lambda表达式都能隐式地赋值给函数式接口
		// """lambda --> 函数式接口"""
		Runnable r = () ->  out.println("函数式接口=lambda表达式");

		// 当不指明函数式接口时，"""编译器会自动解释这种转化"""		
		new Thread(
					() -> out.println("This is Lambda--> 函数式接口(编译器自动解释)")
				).start();
		
		
		// Consumer 是一个模板接口+函数式接口
		Consumer<Integer>  c = (Integer x) -> {  out.println("模板接口+函数式接口 = " + x ); };
		c.accept(12);
		BiConsumer<Integer, String> b = 
							(Integer x, String y) -> System.out.println("模板接口+函数式接口 = " + x + " : " + y);
		b.accept(13,"str12");

		
		
		{// lambda作为函数的参数，非常简洁
			List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
			list.forEach( n -> out.println(n) );
			System.out.println("Print even numbers:");
			evaluate( list,  (n)-> n % 2 == 0 );
			System.out.println("Print odd numbers:");
			evaluate( list,  (n)-> n % 2 == 1 );
			out.println("Print numbers greater than 5:");
			evaluate( list,  (n)-> n > 5 );
		}
		
		{
			List<Integer> list = Arrays.asList(1,2,3,4,5,6,7);
			list.forEach( x -> out.println("List.forEach with Lambda " + x));
		}

		{// 求平方和
			//Old way:
			List<Integer> list = Arrays.asList(1,2,3,4,5,6,7);
			int sum1 = 0;
			for(Integer n : list) {
			    int x = n * n;
			    sum1 = sum1 + x;
			}
			out.println("普通方式求平方和  = " + sum1);

			//New way:  Java8 java.util.stream.Stream  
			int sum2 = list.stream()
						.map( x -> { return x * x ; } )
						.reduce( 
							(sum,next) -> { // reduce 
								out.printf("sum = %d next = %d \n", sum ,next) ;
								return sum + next; // 返回作为下一次的sum
							} 
						).get();
			//int sum2 = list2.stream().map( x->x*x ).reduce((x,y)->x+y).get(); 更加简洁   x*x 表达式 就是返回的值 return x*x --> 简写成 x*x 
			out.println("Lambda + Stream 求平方和  = " + sum2);
			
			
			// Java lambda中引用的局部变量必须是final或者等效final
			//int sum3 = 0 ;
			//list.forEach( x-> { sum3 += x*x ;} );	
			
			list.forEach( x-> { mFinalSum += x*x ;} );	
			out.println("Lambda 求平方和 = " + mFinalSum );
			 
			// lambda表达式"""不支持修改捕获变量"""
			// 我们可以使用更好的方式来实现同样的效果：使用规约（reduction）
			
			// java.util.stream包提供了各种通用的和专用的规约操作（例如sum、min和max）
			// 就上面的例子而言，我们可以使用规约操作（在串行和并行下都是安全的）来代替forEach

		}
		
		{
			class MyClass{
				public MyClass(int temp) {
					mPrivateData = temp;
				}
				
				private int mPrivateData ;
				public Runnable getLambdaWithThis(String tag){
					return ()->{ out.println("lambda runnig with 'this' " + tag  + this.mPrivateData ) ; };
				}
			}
			// 匿名类和Lambda区别
			// 1. Lambda中"""this解读为定义lambda的外部类的实例"""  但是匿名类是自己实例
			// 2. Java 编译器编译 Lambda 表达式并将他们转化为"""类里面的私有函数"""
			
			
			// list.forEach( x-> { out.println("this" + this ); } );
			MyClass my = new MyClass(1);
			Runnable run = my.getLambdaWithThis("private:");
			new Thread(run).start();
			// 包含此类引用的lambda表达式相当于捕获了this实例
			// 在其它情况下，lambda对象不会保留任何对this的引用。
			// 这个特性对内存管理是一件好事：内部类实例会一直保留一个对其外部类实例的强引用，
			// 而那些没有捕获外部类成员的lambda表达式则不会保留对外部类实例的引用。
			// 要知道内部类的这个特性往往会造成内存泄露。
		}
		
		{	// 函数式接口的名称并不是lambda表达式的一部分
			// 对于给定的lambda表达式，它的类型是  上下文所期待的类型进行推导
			// 编译器会检查lambda表达式的类型和目标类型的方法签名（method signature）是否一致
			/*
			 * 当且仅当下面所有条件均满足时，lambda表达式才可以被赋给目标类型T：
				1. T是一个函数式接口
				2. lambda表达式的参数和T的方法参数在数量和类型上一一对应
				3. lambda表达式的返回值和T的方法返回值相兼容（Compatible）
				4. lambda表达式内所抛出的异常和T的方法throws类型相兼容
			 */
			Callable<String> target_type_a = () -> "done";
			PrivilegedAction<String> target_type_b = () -> "done";
			// 由于目标类型（函数式接口）已经“知道”lambda表达式的形式参数（Formal parameter）类型，所以我们没有必要把已知类型再重复一遍
			// 也就是说，lambda表达式的参数类型可以从目标类型中得出
			Comparator<String> target_type_c = (s1, s2) -> s1.compareToIgnoreCase(s2);// 编译器可以推导出s1和s2的类型是String
			// 不要把"""高度问题"""转化成"""宽度问题"""!! 匿名内部类有高度问题
		}
		
		return ;
	}
	static int mFinalSum = 0 ;
}

/*

Java SE 7中已经存在的函数式接口
java.lang.Runnable
java.util.concurrent.Callable
java.security.PrivilegedAction
java.util.Comparator
java.io.FileFilter
java.beans.PropertyChangeListener

Java SE 8中增加了一个新的包：java.util.function，它里面包含了常用的函数式接口
Predicate<T>	——	接收T对象并返回boolean
Consumer<T>		——	接收T对象，不返回值
Function<T, R>	——	接收T对象，返回R对象
Supplier<T>		——	提供T对象（例如工厂），不接收值
UnaryOperator<T>——	接收T对象，返回T对象
BinaryOperator<T>——	接收两个T对象，返回T对象

两篇比较好的文章
http://www.importnew.com/16436.html
http://www.cnblogs.com/figure9/archive/2014/10/24/4048421.html
 */

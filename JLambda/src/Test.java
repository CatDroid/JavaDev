import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.lang.System.out;


public class Test {

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
				
		//local_arg = "123"; // local_arg 必须是 final 或者 等效final 
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
			 

		}
		
		{
			// 1. Lambda中"""this解读为定义lambda的外部类的实例"""  但是匿名类是自己实例
			// 2. Java 编译器编译 Lambda 表达式并将他们转化为"""类里面的私有函数"""
			// list.forEach( x-> { out.println("this" + this ); } );
		}
		
		
		
		return ;
	}
	static int mFinalSum = 0 ;
}

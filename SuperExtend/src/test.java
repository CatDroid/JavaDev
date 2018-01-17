
public class test {

	static class Plate<T>{
	    private T item;
	    public Plate(T t){item=t;}
	    public void set(T t){item=t;}
	    public T get(){return item;}
	}
	
	//Lev 1
	static class Food{}

	//Lev 2
	static class Fruit extends Food{}
	static class Meat extends Food{}

	//Lev 3
	static class Apple extends Fruit{}
	static class Banana extends Fruit{}
	static class Pork extends Meat{}
	static class Beef extends Meat{}

	//Lev 4
	static class RedApple extends Apple{}
	static class GreenApple extends Apple{}
	
	
	public static void main(String[] arg)
	{
		// ?是java泛型中的通配符,它代表java中的某一个类
		// 那么<? extends T>就代表类型T的某个子类,<? super T>就代表类型T的某个父类.
		// <? extends T>或是<? super T>代表的是范围内的某个特定的类,而不是范围内的所有类
		{
			//Plate<? super Fruit> p = new Plate<Apple>(new Apple());  // Error  <? super Fruit> 只能引用Fruit及其父类的
			Plate<? super Fruit> p1 = new Plate<Object>(new Object());
			Plate<? super Fruit> p2 = new Plate<Fruit>(new Fruit());
			
			Plate<? extends Fruit> p= new Plate<Apple>(new Apple());
			//Plate<? extends Fruit> p= new Plate<Object>(new Object()); // Error <？extends Fruit> 只能引用Fruit及其派生类的
		}
		
		 
		/*
		   Number:
		   		Java语言为每一个内置数据类型提供了对应的包装类
		   		 所有的包装类(Integer、Long、Byte、Double、Float、Short)都是抽象类Number的子类

		   
		   	List<? extends E>
		   		表示该list集合中存放的都是E的子类型(包括E自身)，由于E的子类型可能有很多，
		   		 但是我们存放元素时，实际上只能存放其中的一种子类型
		   		(这是为了泛型安全, 因为其会在编译期间生成桥接方法<Bridge Methods>该方法中会出现强制转换, 若出现多种子类型, 则会强制转换失败）
		   		
		   		List<? extends Number> list=new ArrayList<Number>();
 			list.add(4.0);	// 编译错误
 			list.add(3);	// 编译错误			 
		   					 
		   
	 		List<? extends E>不能添加元素，但是由于其中的元素都有一个共性
	 			--有共同的父类，
	 			因此我们在获取元素时可以将他们统一强制转换为E类型，我们称之为GET原则
	 			
	 			Apple f = new Fruit(); // 不能   --> 所以  List<? extends Fruit> .add( new Apple ) 错误
	 			
	 			get是可以  容器里面可能是Apple Banana 可以get到给 Fruit 
	 			
	 		List<? super E>其list中存放的都是E的父类型元素(包括E) 
	 			我们在向其添加元素时，只能向其添加E的子类型元素(包括E类型)
	 			这样在编译期间将其强制转换为E类型时是类型安全的，因此可以添加元素
	 		
	 		也就是 List<? super Fruit> .add( new Apple ) 是正常的    Fruit f = new Apple();
		 */
		{
			Plate<? extends Fruit> p= new Plate<Apple>(new Apple());
			//不能存入任何元素
			//p.set(new Fruit());    //Error
			//p.set(new Apple());    //Error
		 
			// 读取出来的东西只能存放在Fruit或它的基类里  , 具体是什么类型不知道 可能是Fruit Apple Banana RedApple GreenApple
			// OK --> Fruit 引用   <? extends Fruit>范围内 某个特定类 的对象 
			Fruit  newFruit1 = p.get();
			Object newFruit2 = p.get();
			// Apple  newFruit3 = p.get();    //Error
		}
		
		{
			Plate<? super Fruit> p = new Plate<Fruit>(new Fruit());
			// p 引用了 Fruit及其父类 范围内 特定类型 的 对象

			//存入元素正常
			p.set(new Fruit()); 
			p.set(new Apple()); // Apple --> Fruit 不用显式强制转换  
			// p.set(new Object() ); // Error  Object --> Fruit  !!!  因为super T限制里 面实例最低应该是T类
 
			//读取出来的东西只能存放在Object类里。
			//Apple newFruit3=(Apple) p.get();    	// Error
			//Fruit newFruit1=  p.get();    		// Error
			Object newFruit2=p.get();				// Ok
		}
		
		
		
	}
}

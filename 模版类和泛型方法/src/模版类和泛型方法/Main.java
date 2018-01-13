package 模版类和泛型方法;

public class Main {

	public static void main(String[] args) {
		
		Demo.sdump(12);	// 静态泛型方法: 根据调用时的参数 
		
		
		Demo<Integer> d = new Demo<Integer>();
		d.dump(13);		// 普通泛型方法: 根据类实例化时候的模版参数
		
			
	}

}

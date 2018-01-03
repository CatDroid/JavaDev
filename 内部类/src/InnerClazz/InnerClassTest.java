package InnerClazz;

 
 


public class InnerClassTest {

	static int a;

	int b;

	private static void test() {
		System.out.println("outer class static function");
	}

	public static void main(String[] args) {
		InnerClassTest oc = new InnerClassTest();
        // new一个外部类
		InnerClassTest oc1 = new InnerClassTest();
        // 通过外部类的对象new一个非静态的内部类
		InnerClassTest.InnerClass no_static_inner = oc1.new InnerClass();
        // 调用非静态内部类的方法
        System.out.println(no_static_inner.getKey());

        // 调用静态内部类的静态变量
        System.out.println(InnerClassTest.InnerStaticClass.static_value);
        // 不依赖于外部类实例,直接实例化内部静态类
        InnerClassTest.InnerStaticClass inner = new InnerClassTest.InnerStaticClass();
        // 调用静态内部类的非静态方法
        System.out.println(inner.getValue());
        // 调用内部静态类的静态方法
        System.out.println(InnerClassTest.InnerStaticClass.getMessage());
    }

    private class InnerClass {
        // 只有在静态内部类中才能够声明或定义静态成员
        // private static String tt = "0";
    	private int flag = 0;

        public InnerClass() {
            // 三.非静态内部类的非静态成员可以访问外部类的非静态变量和静态变量
            System.out.println("InnerClass create a:" + a);
            System.out.println("InnerClass create b:" + b);
            System.out.println("InnerClass create flag:" + flag);
            //
            System.out.println("InnerClass call outer static function");
            // 调用外部类的静态方法
            test();
        }

        public  String getKey() {
            return "no-static-inner";
        }
    }

    private static class InnerStaticClass {
        // 静态内部类可以有静态成员，而非静态内部类则不能有静态成员。
        private static String static_value = "0";

        private int flag = 0;

        public InnerStaticClass() {
            System.out.println("InnerClass create a:" + a);
            // 静态内部类不能够访问外部类的非静态成员
            // System.out.println("InnerClass create b:" + b);
            System.out.println("InnerStaticClass flag is " + flag);
            System.out.println("InnerStaticClass tt is " + static_value);
        }

        public int getValue() {
            // 静态内部类访问外部类的静态方法
            test();
            return 1;
        }

        public static String getMessage() {
            return "static-inner";
        }
    }

    public InnerClassTest() {
        // new一个非静态的内部类
        InnerClass ic = new InnerClass();
        System.out.println("OuterClass create");
    }
	    
}

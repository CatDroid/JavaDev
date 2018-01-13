import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.lang.System.out;


public class Test {

	// ���ֽӿڽ���  ����ʽ�ӿ� 
	@FunctionalInterface
	interface TestListener{ 
		public int callback(String... strings);
	}
	
	@FunctionalInterface
	interface ValueLister{
		public float returnVal();
	}
	
	// ���ݺ��� ��Ϊ ���� 
	public static void TransferFunction(TestListener tl) {
		tl.callback("TransferFunction","arg1","arg2");
	}
	
	public static void TransferFunction2(ValueLister val) {
		out.println("����ʽ�ӿ�lambda = " + val.returnVal() );
	}
	
	
	// �������رհ�
	public static TestListener ReturnClosure(String func_arg ) {
		String local_arg = "This Is Local Arg In Function";
		return 	// Lambda   �հ�����: �ɼ�¼(����)  ��������ʱ�� �����������Ϣ(����)
				(String... strings)->{
					out.println("�հ�   " + local_arg );  
					out.println("�հ�   " + func_arg  );
					for(String str : strings) {
						out.println("lambda = " + str);
					}
					return 2 ; // �������ʽ�ӿڶ��巵��ֵ  lambda����Ҳ����ֵ
				} ;
	}
	
	public static void main(String[] args) {
		
		int[] a = new int[12];
		for(int temp : a) {
			out.printf("k:%d\n", temp  ); // ��C++һ�������
		}
		 
		// ����Map
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
		
			//  Java8 ֮ǰ�ķ�ʽ 
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

	
		// ʵ�ʲ��� ��ͨ�ı�lambdaҪ��
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
		
		
		/* �� Java �ж���ĺ����򷽷���������ȫ������Ҳ���ܽ�������Ϊ�����򷵻�һ��������ʵ��
		       ��������ͨ��������(newĳ��Interface��overrideʵ���䷽��)���������ݺ������� 
		
		  	������ Java ���Բ�����Ҫ���� Java �������""�����޷���������""
			�ں���ʽ��������У�������һ�ȹ������ǿ��Զ������ڣ�����Խ��丳ֵ��һ�������������ǵ�������������������
			����ʽ���ԣ��ṩ��һ��ǿ��Ĺ��ܡ���""�հ�""  
			�հ���һ���ɵ��õ�""����""������¼��һЩ��Ϣ����Щ��Ϣ������""��������������""
		
			@python 
			def func():
				print "this is func"
				pass
				print (type(func)) 		### <type 'function'>  
			��python�� func��һ���������͵Ķ��� 
		
			�ں���ʽ��������У�Lambda���ʽ��"""�����Ǻ���"""
			��Java�У�Lambda���ʽ�Ƕ������Ǳ���������һ���ر�Ķ���"""����---����ʽ�ӿ�"""(functional interface)
		
			�ִ�������Ա�������հ���������  ???
			
			lambda��  
			1. ��������������û�������ķ�������û�з������η�������ֵ����������
			2. ֻ��һ������ ()  ֻ��һ�����{} ������ʡ��  
			3. �������Ϳ���ʡ���Զ��Ƶ�
			4. �������Ͳ���д    ��������������ķ�������һ��  
		*/
		TransferFunction(new TestListener() {  // ������ 
			@Override
			public int callback(String... strings) {
				for(String str : strings) {
					out.println("new ������ = " + str);
				}
				return 1 ;
			}
			
		});
		
		TransferFunction( ReturnClosure("This is Funciton Arg in Function"));
		
		TransferFunction2( ()->3.1415f ); // ���д���� �൱��  ()-->{return 43}; 
		
		// ÿ��Lambda���ʽ������ʽ�ظ�ֵ������ʽ�ӿ�
		// """lambda --> ����ʽ�ӿ�"""
		Runnable r = () ->  out.println("����ʽ�ӿ�=lambda���ʽ");

		// ����ָ������ʽ�ӿ�ʱ��"""���������Զ���������ת��"""		
		new Thread(
					() -> out.println("This is Lambda--> ����ʽ�ӿ�(�������Զ�����)")
				).start();
		
		
		// Consumer ��һ��ģ��ӿ�+����ʽ�ӿ�
		Consumer<Integer>  c = (Integer x) -> {  out.println("ģ��ӿ�+����ʽ�ӿ� = " + x )  };
		c.accept(12);
		BiConsumer<Integer, String> b = 
							(Integer x, String y) -> System.out.println("ģ��ӿ�+����ʽ�ӿ� = " + x + " : " + y);
		b.accept(13,"str13");

		return ;
	}
}

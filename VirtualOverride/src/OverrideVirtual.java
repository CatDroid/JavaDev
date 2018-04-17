


public class OverrideVirtual {

	
	public static void main(String[] args) {
		 
		Base bb = new Derive1();
		
		bb.call(11); // 调用的是派生类Override的!!
		
		bb.callFinal(12);// Java没有虚函数概念 默认动态绑定 如果加上final派生类不能Override
		
		// bb.callFinal(123.0); // 错误 除非显式强制转换
		Derive1 dd = new Derive1();  
		dd.callFinal(11.0f);
		//dd.callFinal( 11.0);// float -> double是收缩变换 需要显示   浮点数字面值是double类型
		dd.callDouble(11);// int -> double 是提升 不需要显示强制类型转换 
		
	}

}

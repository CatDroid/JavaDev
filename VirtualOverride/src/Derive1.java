import static java.lang.System.out;

public class Derive1 extends Base {

	Derive1(){
		out.println("Derive1");
	}
	
	@Override 
	int call(int input) {
		out.println("Derive1 input = " + input );
		return input+1 ;
	}
	
//	int callFinal(int input) { // 错误
//	}
	
	int callFinal(float input) {
		out.println("callFinal(float input) input = " + input );
		return 0;
	}
	
	int callDouble(double input) {
		out.println("callFinal(float input) input = " + input );
		return 0;
	}
	
//	Object callFinal(int input) {
//		return new Object();
//	}
	
	int callFinal(int input, int input2) {
		out.println("Derive1 callFinal(int,int) = " + input );
		return input + input2 ;
	}
}

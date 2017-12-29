
import static java.lang.System.out ;
 

public class TryFinally {

	void callfinally(int i){
		try {
			if(i > 100) {
				out.println(">100");
				return ;
			}else {
				out.println("<=100");
				return ;
			}
			// out.println("here ??"); // unreachable code
		}finally {
			 out.println("finally done");
		}
		
		
		
	}
	
	  
	
	
	public static void main(String args[]) { 
		
		TryFinally t = new TryFinally();
		t.callfinally(1);
		t.callfinally(101);
		
		 
	}
}

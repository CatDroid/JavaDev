import rx.Observable;
import rx.Subscriber;

public class RxSimpleTest {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
	
		
		// 在 1.7中  局部变量  用于  匿名内部类中 需要 finally ; 在1.8中  局部变量 用于 匿名内部类或者Lambda中  只需要等效finally 
		int[] datas = new int[]{2,5,3,1,7,4,8,3,2};
		int sleepTime = 1000;
		Observable<Integer> obs = Observable.create(new Observable.OnSubscribe<Integer>() {
		    @Override
		    public void call(Subscriber<? super Integer> subscriber) {
		        try{
		            for(int data : datas){
		                if (!subscriber.isUnsubscribed())  subscriber.onNext(data);
		                else return;  // 检查观察者的isUnsubscribed状态,以便在没有观察者的时候 让你的Observable停止发射数据或者做昂贵的运算
		                Thread.sleep(sleepTime);
		            }
		            if (!subscriber.isUnsubscribed()) subscriber.onCompleted();
		           
		        }catch (Exception e){
		            if (!subscriber.isUnsubscribed()) subscriber.onError(e);
		            
		        }
		    }

		});
		
		datas[1] = 2 ; // OK
		//datas = new int[]{1,2,3,4};  // Error

	}

	
	
}

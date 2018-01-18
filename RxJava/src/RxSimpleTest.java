import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader; 

public class RxSimpleTest {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
	
		
		// 在 1.7中  局部变量  用于  匿名内部类中 需要 finally ; 在1.8中  局部变量 用于 匿名内部类或者Lambda中  只需要等效finally 
		int[] datas = new int[]{2,5,3,1,7,4,8,3,2};
		int sleepTime = 1000;
		Observable<Integer> obs = Observable.create(new Observable.OnSubscribe<Integer>() {
		    @Override
		    public void call(Subscriber<? super Integer> subscriber) {
		    	out.println("OnSubscribe call = " + Thread.currentThread().getName() );
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
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		out.println("start time:" + sdf.format(new Date()));
		
		obs.subscribeOn( Schedulers.newThread() )
		    .observeOn( Schedulers.immediate() ) // 这个跟之前同个线程
		    .subscribe(
		    		new Subscriber<Integer>() {
				        @Override
				        public void onCompleted() {
				    		out.println("onCompleted");
		
				        }
				        @Override
				        public void onError(Throwable e) {
				        	out.println("onError:"+e.getMessage());
				        }
				        @Override
				        public void onNext(Integer integer) {
				        	out.println("subsriber onNext = " + Thread.currentThread().getName() );
				        	out.println("onNext:"+integer+" time:"+sdf.format(new Date()));
				        }
		    		}
		    );

		
		// 等待用户输入 
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
        String str=null;  
        try {
        	out.println("wait for exit or quit");
			while((str=br.readLine())!=null)  
			{   
			    if(str.equals("quit") || str.equals("exit") ) {
			    	return ;
			    }else {
			    	out.println("input:"+str);
			    }
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		 
	}
}

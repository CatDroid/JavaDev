import static java.lang.System.out;


import rx.Observable;
import rx.Subscriber;
import rx.Observable.OnSubscribe;
import rx.functions.Func1;

public class RxMapTest {

	public static interface MyOperator<Ttar, Tsrc> extends Func1<Subscriber<? super Ttar>, Subscriber<? super Tsrc>> 
	{
		// Ttar call(Tsrc); 
	}
	
	public static class MapObservable<Tsrc ,Ttar> implements OnSubscribe<Ttar> {
		private Observable<Tsrc> src ;
		private MyOperator<Ttar,Tsrc> op ;
		public MapObservable(Observable<Tsrc> src , MyOperator<Ttar,Tsrc> op) {
			this.src = src ;
			this.op = op ;
		}
		
	
		@Override
		public void call(Subscriber<? super Ttar> t) {
			this.src.subscribe( op.call(t /*这个是外面的subscriber 目标/初始的subscriber */) );
		}
	}
	
	public static void main(String[] args) {


		Observable<Integer> obserInt = Observable.from(new Integer[] {1,2,3,4,5});
		Observable<String> obserStr =  Observable.create(
				new MapObservable(obserInt,
						new MyOperator<String,Integer>() {

							// 返回的 Subsriber给到 包含在内的 obserInt 原来的计划 
							@Override
							public Subscriber<? super Integer> call(Subscriber<? super String> subscriber) {
								return new Subscriber<Integer>() {  
									            @Override
									            public void onNext(Integer integer) {
									                subscriber.onNext("MyMap-" + integer);
									            }
							
									            @Override
									            public void onCompleted() {
									                subscriber.onCompleted();
									            }
							
									            @Override
									            public void onError(Throwable e) {
									                subscriber.onError(e);
									            }
						        		};
							}// end of call 
					})
				 );
		obserStr.subscribe(
				(String str)-> out.println("onNext str = " + str),
				(Throwable ex)-> out.println("onError ex = " + ex.getMessage() ),
				()-> out.println("onComplete") 
				);
			
	 


	}

}

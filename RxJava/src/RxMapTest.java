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
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		{
			Observable<Integer> obserInt = Observable.from(new Integer[] {1,2,3,4,5});
			Observable<String> obserStr =  Observable.create(
					new MapObservable<Integer,String>(obserInt,
							new MyOperator<String,Integer>() {
	
								// 返回的 Subsriber 订阅  原来的计划/任务
								// 拦截原始Observable(计划/任务)发射的数据
								// 新生成的Observable就相当于一个代理，它拦截原始Observable发射的数据，然后对数据做一些处理，再发射给观察者
								@Override
								public Subscriber<? super Integer> call(Subscriber<? super String> subscriber) {
									out.println("MyOperator call "); // 只调用了一次 
									return new Subscriber<Integer>() {  
										            @Override
										            public void onNext(Integer integer) {
										            	// 这里做了转换!!
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


		{
			Observable.just(1,2,3)
			.map( (Integer in) -> { // Func1  有参数   有返回值(与Action1区别) 
				switch(in) {
				case 1:
					return "111" ; 
				case 2:
					return "222" ;
				case 3:
					return "333";
				default:
					return "000";
				}
			} ).subscribe((String o )->{  out.println( "变换事件的参数类型 --->" + o ); });
//			
//			Integer[] array = new Integer[] {1,2,3,4};
//			Observable.from(array).map( (Integer in)->{ //  map() 是一对一的转化，
//				switch(in) {
//				case 1:
//					return "111";
//				case 2:
//					return "222";
//				case 3:
//					return "333";
//				case 4:
//					return "444";
//				default:
//					return "000";
//				}
//			}).subscribe((String o )->{  out.println( "变换事件的参数类型 --->" + o ); });
//			
		}
 
		//	.lift OnSubscribeLift	实现是一个Func1  给定外面  目标类型的Subscriber 返回外面 原类型'计划'的 Subscriber,返回的Subscriber往'原类型计划上subscribe'订阅,并且在自己的onNext中,调用目标类型的Subscriber 
		// 	.map  OnSubscribeMap	实现是一个Func1  给定原类型 转换成 目标类型
		
		{	
			/*
					RxJava 都不建议开发者自定义 Operator 来直接使用 lift() 
					而是建议尽量使用已有的 lift() 包装方法(如 map() flatMap() 等)进行组合来实现需求 
					因为直接使用 lift(),非常容易发生一些难以发现的错误
			 */
			Observable<Integer> observable = Observable.from(new Integer[] {1,2,3,4,5});
			observable.lift(new Observable.Operator<String, Integer>() {
				
				// Operator只是 extends Func1,  这里 的 call就是Func1的接口 
				// lift会创建新的Observable和 对应新的事件 OnSubscribeLift返回     下面的call其实就是新的事件!
			    @Override
			    public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
			       
			        return new Subscriber<Integer>() { // 将事件序列中的 Integer 对象转换为 String 对象
			            @Override
			            public void onNext(Integer integer) {
			            	out.println("Inner Subscriber " + integer );
			                subscriber.onNext("T" + integer);
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
			    }
			    
			    
			}).subscribe( (String s)-> out.println("Target Subscriber " + s )   ) ;
	 

		}
	}

}

import rx.Observable;
import rx.Subscriber;
import rx.Observable.OnSubscribe;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;

import static java.lang.System.out;

import java.util.concurrent.TimeUnit;


// http://reactivex.io/
// http://reactivex.io/languages.html


public class RxJavaTest {

	
	private static void logThread(Object obj, Thread thread){
		out.println( "onNext: obj= " + obj 
				+ "; running="+ Thread.currentThread().getName() 
				+ "; thread=" + thread.getName() );
	}
	
 
	
	
	public static void main(String[] args) {
			
		
		Observable.OnSubscribe<Integer> onSub = new Observable.OnSubscribe<Integer>() {
		    @Override
		    public void call(Subscriber<? super Integer> subscriber) {	//	Math.random() --> [0，1) 
		    	int temp = (int)( 4 + Math.random()*( 10-4 + 1) ) ;  	//	从4到10的int型随数 =  (数据类型)(最小值+Math.random()*(最大值-最小值+1))
		        out.println(  "OnSubscribe.call = " + Thread.currentThread().getName() + ";random = " + temp );
		        subscriber.onNext( temp );
		        subscriber.onCompleted();
		    }
		};
		

//		out.println( "--------------1-------------");
//		Observable.create(onSub).subscribe( 
//				(arg_i) -> {
//					logThread(arg_i, Thread.currentThread());
//				}
//			);
//
//		
//		out.println( "--------------2-------------");
//		Observable.create(onSub) // observeOn指示一个Observable在一个特定的调度器上调用观察者的onNext, onError和onCompleted方法
//		        .subscribeOn(Schedulers.io()) 	//	subscribeOn则指示Observable将全部的处理过程（包括发射数据和通知）放在特定的调度器上执行
//		        								//	指定Observable中OnSubscribe(计划表)的call方法在那个线程发射数据
//		        								//	默认在当前线程（主线程）中执行任务流，并在当前线程观察
//		        								//  subscribeOn 返回的是一个  新的Observable 
//		        								// 		新的Observable包含  新的计划表OperatorSubscribeOn
//		        								//		新的计划表  包含   原始的Observable 和 调度器scheduler
//		        .subscribe( 
//		        			( arg_i ) -> {
//		        				logThread( arg_i, Thread.currentThread() ) ; 
//		        			}
//		        		);
		
		
//		out.println( "--------------3-------------");
//		Observable.create(onSub)
//			.subscribeOn(Schedulers.newThread())
//			.subscribe(arg_i->logThread(arg_i, Thread.currentThread()));
	
		
//		out.println( "--------------4-------------");
//		Observable.create(onSub)
//		        .subscribeOn(Schedulers.newThread())s
//		        .observeOn(AndroidSchedulers.mainThread()) // Android上新增  会跑在main线程 
//		        .subscribe(integer->logThread(integer, Thread.currentThread()));
		
		
//		out.println( "--------------5-------------");
//		Observable.create(onSub)
//		        .subscribeOn(Schedulers.newThread()) // 里面会创建一个新的Observable对应新的计划(onSub)，新计划的call调用scheduler的work线程上 执行原计划
//		        .subscribeOn(Schedulers.newThread()) 
//		        //.subscribeOn(Schedulers.newThread())
//		        .observeOn(Schedulers.newThread())
//				.subscribe(arg_i->logThread(arg_i, Thread.currentThread())); // 调用了subsribe 最外层的Observale对象就会调用计划onSub.call 
		
		Integer arrays[] = {1,2,3,4}; 
		Observable.from( arrays )  // array给到OnSubscribeFromArray计划中  然后 OnSubscribeFromArray.call相当于实现了三次 subsribe.onNext() onNext() onNext() 最后onComplete
			//.subscribeOn(Schedulers.newThread()) 
			.subscribe( arg_i-> out.println("观察者 调用一次 onNext  Observable from " + arg_i),
						error-> out.println("观察者 调用一次 onError  Observable from " + error.getMessage() ),
						()-> out.println("观察者  调用一次  Complete ")
					);
		
		// 等效例子
		class ArraysOnSubsribe<T> implements Observable.OnSubscribe<T>{
			
			private T[] array ; 
			
			public ArraysOnSubsribe(T[] array ){
				this.array = array ;
			}
			
			@Override
			public void call(Subscriber<? super T> t) {
				for(T item: array) {
					t.onNext( item );
				}
				t.onCompleted(); 
			}
		}
		
		Observable.create( new ArraysOnSubsribe<Integer>(arrays) )
		.subscribe( arg_i-> out.println("[自定义ArraysOnSubsribe] 观察者 调用一次 onNext  Observable from " + arg_i),
					error-> out.println("[自定义ArraysOnSubsribe] 观察者 调用一次 onError  Observable from " + error.getMessage() ),
					()-> out.println("[自定义ArraysOnSubsribe] 观察者  调用一次  Complete ")
				);
		
		
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		out.println( "....>....");
		//ob.subscribe(arg_i->logThread(arg_i, Thread.currentThread()));
//		out.println( "....<....");
//		
		
//		out.println( "--------------6-------------");
//		Observable.interval(100, TimeUnit.MILLISECONDS)
//		        .take(2)
//		        .subscribe(arg_i->logThread(arg_i, Thread.currentThread()));
		
//		{
//			Observable.just(1,2,3)
//			.map( (Integer in) -> { // Func1  有参数   有返回值(与Action1区别) 
//				switch(in) {
//				case 1:
//					return "111" ; 
//				case 2:
//					return "222" ;
//				case 3:
//					return "333";
//				default:
//					return "000";
//				}
//			} ).subscribe((String o )->{  out.println( "变换事件的参数类型 --->" + o ); });
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
//		}
	
		{
			
			/*
					RxJava 都不建议开发者自定义 Operator 来直接使用 lift() 
					而是建议尽量使用已有的 lift() 包装方法(如 map() flatMap() 等)进行组合来实现需求 
					因为直接使用 lift(),非常容易发生一些难以发现的错误
			 * */
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
		

		
		// 写在这里避免主进程退出了
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("-----main-end------");
	}
}

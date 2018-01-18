import rx.Observable;

import static java.lang.System.out;

public class RxOnNext {

	public static void main(String arg[]) {
		Observable.just(1).doOnNext( 
					arg_i -> out.println("doOnNext " + arg_i )
			).subscribe( 
					arg_i -> out.println( "subscribe onNext " + arg_i ) 
			);
	}
	
	// 内部流程是   @ OnSubscribeDoOnEach.java 
	// doOnEachObserver.onNext(value);
	// subscriber.onNext(value); // 紧接着执行   注意：类型是相同的!
}

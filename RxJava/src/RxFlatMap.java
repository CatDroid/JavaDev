import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static java.lang.System.out;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List; 


public class RxFlatMap {

	public static String[] getWordsByLetter(String letter ) {
		return letter.split(" ");
	}
	
	public static void main(String[] args) {
		
		List<String> letters =  Arrays.asList("this is a sentence","second line","come to the end");
		
		Observable.from( letters )
		//.observeOn(Schedulers.newThread())
		.flatMap( 			// lift的转换 是把 目标类型Subscriber 返回 源类型的Subscriber 然后源类型的Subscriber转换后 调用目标类型的Subsriber.onNext
			new Func1<String,Observable<String>>(){	// flatMap 是 返回的Observable 被  目标类型Subscriber 订阅 ; flatMap中的subscriber在onNext中拿到数据String后， 调用这个func.call返回新的Observable， 并用  MergeSubsriber订阅， 最后给到目标类型Subscriber
				public Observable<String> call(String letter){
					out.println(Thread.currentThread().getName() + ">>  flatMap call letter = " + letter );
					Observable<String> words= Observable.from(getWordsByLetter(letter));
					return words;  
				}      
			} 
		)
		.observeOn(Schedulers.newThread())
		.subscribe( new Action1<String>(){
			public void call(String word){
				out.println(Thread.currentThread().getName() + ">>  word=" + word);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		/*
		 * obser : Observable  
		 * suber : Subscriber
		 * onSub : OnSubscribe
		 
		 	Observable.from
					OnSub:OnSubscribeFromIterable								
						call{ onNext -- onNext -- onNext -- onComplete}      List<String> --> String is sentence

			map
					OnSub:OnSubscribeMap										Suber:MapSubscriber		
						call{ p = new MapSubscriber ,  obser.subscribe(p)  }	onNext(){ r=mapper.call(t), suber.onNext(r) }	

			lift
					OnSub:OnSubscribeLift										Suber:MergeSubscriber (实际是调用提供的Operator.call返回的)
						call{ Subscriber new_suber = operator.call( old_suber); onNext(){ t.unsafeSubscribe(inner) } t就是上面suber给的Observable
							  old_onSub.call( new_suber  ); } 

		 * */

		InputStreamReader f = new InputStreamReader(System.in);
		try {
			out.print("wait for exit");
			f.read();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		out.println("exit program");
		 
		
	}

}

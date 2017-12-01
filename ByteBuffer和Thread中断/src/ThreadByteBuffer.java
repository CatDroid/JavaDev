

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

enum _MSG_TYPE {
	MSG_TYPE_STATUS  ,
	MSG_TYPE_H264_DATA,
	MSG_TYPE_YUV_DATA,
	MSG_TYPE_RGB_DATA,
	MSG_TYPE_PCM_DATA,
	MSG_TYPE_VOD_CURRENT_POSITION,
	MSG_TYPE_MAX		
};


///////////////////////////////////////////////////////////////////

class Top
{
	private int m1 = -12 ;
	private int m2 = -13 ; 
	
	final public void setM(int x , int y){
		System.out.println("Top> m1 " + m1 + " m2 " + m2 ); // m1 -12 m2 -13 
		m1 = x + 1; // 修改的是自己的m1 m2 不是继承类的
		m2 = y + 1 ;
		System.out.println("Top> m1 " + m1 + " m2 " + m2 ); // m1 11 m2 16
	}
}

class TopSub extends Top
{
	private int m1 = 0 ; // 在派生类范围内  覆盖掉基类的 
	private int m2 = 0;
	
	public void set(int x , int y){ // p.set(10, 15);  
		System.out.println("TopSub> m1 " + m1 + " m2 " + m2 ); // m1 0 m2 0   
		m1 = x ; m2 = y;
		setM(m1 , m2 );
		System.out.println("TopSub> m1 " + m1 + " m2 " + m2 ); // m1 10 m2 15
	}

}



///////////////////////////////////////////////////////////////////////
class Main {
	
}

class Second extends Main
{
	
}

class Third extends Second
{
	
}
///////////////////////////////////////////////////////////////////////

public class ThreadByteBuffer {

	public static void getSPS(byte[] sps){
		if(sps == null){
			System.out.println("empty arg input ");
			sps = new byte[10];
		}else{
			System.out.println("full arg input ");
		}
	}
	
	static String byteArray2Hex(byte[] array)
	{	
		StringBuilder sb = new StringBuilder();
		for( int j = 0 ; j < array.length ; j++ ){
			sb.append( Integer.toHexString( 0xFF & array[j] ) ) ;
			if( j != array.length -1 ) sb.append(",");
		}
		return sb.toString();
	}
	
	public static void main(String args[]) { 
		
		{
			// XXX.class.isInstance(obj) 对象能不能被转化为这个类
			// 1. 一个对象是本身类XXX.class的一个对象 
			// 2. 一个对象能被转化为 基类或者 接口
			// 3. 所有对象都能转为Object 
			// 4. XXX.class.isInstance(null) 总是false NULL对象不能转换成任何类
			Main main = new Second();
			System.out.println( "Third "	+ Third.class.isInstance(main) );	// false
			System.out.println( "Second "	+ Second.class.isInstance(main) );  // true   
			System.out.println( "Main "		+ Main.class.isInstance(main) );	// true  本类及其派生类   		
		}

		{
			System.out.println();
			try {
				FileInputStream inputstream;
				inputstream = new FileInputStream("text.txt");
			 
				// inputstream.skip(n)
				// inputstream.mark(n) 
				// 	希望在读出这么多个字符之前，这个mark保持有效
				//	读过这么多字符之后  系统可以使mark不再有效  而你不能觉得奇怪或怪罪它
				// 	这跟buffer有关，如果你需要很长的距离，那么系统就必须分配很大的buffer来保持你的mark
				//  mark(10) 那么在read()10个以内的字符时  reset() 操作后可以重新读取已经读出的数据
				//  如果已经读取的数据超过10个，那reset()操作后，就不能正确读取以前的数据了，因为此时mark标记已经失效
				// inputstream.reset()
				
				// FileChannel 和 FileStream 读操作都会影响当前位置 
				
				byte[] buffer = new byte[100];
				FileChannel channel = inputstream.getChannel();
				System.out.println( String.format("before channel position is %d , available =  %d ", 
													channel.position() ,  inputstream.available()   )  ) ; // 0 112 

				int read=inputstream.read(buffer);
				System.out.println( String.format("after read (%d) by FileInputStream  channel position is %d , available =  %d ", 
						read, channel.position() ,  inputstream.available()   )  ) ;  // read 100 pos 100 avail 12 
			
				ByteBuffer rdBuffer = ByteBuffer.allocateDirect(213);
				rdBuffer.clear(); read = channel.read(rdBuffer); rdBuffer.flip();
				System.out.println( String.format("after read by (%d) FileChannel channel position is %d , available =  %d ", 
						read, channel.position() ,  inputstream.available()   )  ) ;  //  read 12 pos 112 avail  0 
				
				channel.close();
				inputstream.close();
				
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				System.out.println("ERROR " +  e1.getMessage() );
				return ;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("ERROR " +  e.getMessage() );
				return ;
			}
		}
		
		{
			System.out.println();
			TopSub p = new TopSub();
			p.set(10, 15);
		}

		{
			System.out.println();
			byte[] src = new byte[20] ;
			ByteBuffer dest = ByteBuffer.allocateDirect(100);
			dest.put(src, 0, src.length ) ;  // put()  拷贝数据到byte[] :  从当前ByteBuffer.position开始，填充数据 到src的offset开始处， 修改ByteBuffer.postion
			System.out.println("ByteBuffer.put(byte[] src, int offset, int length) " + dest.position() ) ; // 20 
			
			ByteBuffer destShared = dest.duplicate() ;	// 共享buffer 但是 position limit mark 独立  ,新Buffer的postion初始值 为原buffer的postion当前值
			System.out.println("Shared ByteBuffer postioin(independent) after just initialize " + destShared.position()  ) ; // 20 
			
			dest.put(0, (byte) 12) ; 		//  put()  buffer指定位置写入值 :  不管当前postion 索引从buffer的0开始 不修改postion
			System.out.println("ByteBuffer.put(int index, int length) " + dest.position() ) ; 	// 20 
			System.out.println("                                      " + dest.get(0) ) ; 		// 12 
			
			System.out.println("Shared ByteBuffer postioin(independent) " + destShared.position()  ) ; // 0 
			System.out.println("Shared ByteBuffer postioin(independent) " + destShared.get(0)  ) ;     // 12
		}
		
		{
			System.out.println();
		}

		int  video_frame_index = 100 ;
		ByteBuffer snddata = ByteBuffer.allocateDirect(1300);
		snddata.put(0, (byte) ( video_frame_index & 0x000000FF  )        );
		snddata.put(1, (byte) ( (video_frame_index & 0x0000FF00 ) >> 8 ) );
		snddata.put(2, (byte) ( (video_frame_index & 0x00FF0000 ) >> 16) );
		snddata.put(3, (byte) ( (video_frame_index & 0xFF000000 ) >> 24) );

		System.out.println(" " + snddata.remaining() + " " + snddata.position() +  " " + snddata.capacity() );
		
		//String range = " npt= 0.000 -";
		String range = " npt=   22.000   -";
		 
		int start = range.indexOf("=");
		int end = range.indexOf("-");
		String sub = (String) range.subSequence(start + 1, end);
		System.out.println( sub );
		float rangef = Float.parseFloat(sub) ;
		System.out.println( rangef );;
		
		
		ByteBuffer dbb = ByteBuffer.allocateDirect(10);
		dbb.put((byte) 0);
		dbb.put((byte) 0);
		dbb.put((byte) 0);
		dbb.put((byte) 0);
		dbb.put((byte) 0);
		dbb.put((byte) 0);
		dbb.put((byte) 0);
		dbb.put((byte) 0);
		dbb.put((byte) 0);
		dbb.put((byte) 0);
		System.out.println(">>>> " + dbb.position());;
		//dbb.put((byte) 0); // java.nio.BufferOverflowException
		

		ArrayList<ByteBuffer> direct_array  = new ArrayList<ByteBuffer>();
		
		for(int j = 0 ; j < 257 ; j++){
			ByteBuffer bbu = ByteBuffer.allocateDirect(1000000); // 也会导致 OOM Davlik Heap超过256 ?? 为什么是在Davlik 而不是 Native Heap ??
			//ByteBuffer bbu = ByteBuffer.allocate(1000000); // 都会导致 java.lang.OutOfMemoryError
			System.out.println( "bbu direct = " + bbu.isDirect() + " j = " + j );
			byte[] bba = bbu.array();
			bba[0] = 1 ;
			direct_array.add(bbu);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		for( int n = 0 ; n < 10000 ; n ++ ){
			ByteBuffer mall = ByteBuffer.allocateDirect(1000000);
		}
     
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String file_uri = String.format("/mnt/sdcard/%s.mp4",   df.format(new Date())  ) ;   
		
		System.out.println("ll " + file_uri ); 
		
		byte[] my_sps = null;
		System.out.println("before getSPS my_sps = " + my_sps );
		getSPS(my_sps);
		System.out.println("after getSPS my_sps = " + my_sps );
		
		byte[] test_sps = new byte[]{0,1,78,48,47};
		System.out.println("" + byteArray2Hex(test_sps) );
		
		ByteBuffer directbb = ByteBuffer.allocateDirect(10);
		directbb.put((byte) 18);
		directbb.flip();
		System.out.println("bb = " + directbb.get(0)) ;
		
		ByteBuffer bbtemp = ByteBuffer.allocate(directbb.remaining());
		bbtemp.put(directbb);
		bbtemp.flip();
		System.out.println("bb = " + bbtemp.get(0)) ;
		
		
		
		ByteBuffer allocate = ByteBuffer.allocate(20);
		System.out.println("1 pos " + allocate.position() 
								+ " remain " + allocate.remaining() 
								+ " limit " + allocate.limit() 
								+ " cap " + allocate.capacity() );
		allocate.put((byte) 12) ;
		allocate.put((byte) 13) ;
		System.out.println("2 pos " + allocate.position() 
								+ " remain " + allocate.remaining() 
								+ " limit " + allocate.limit() 
								+ " cap " + allocate.capacity() );
		
		byte[] arr = allocate.array();
		System.out.println("1 array " + Arrays.toString( arr ));
		arr[0] = 18 ; // 会直接修改ByteBuffer里面的数据
		System.out.println("2 array " + Arrays.toString( arr ));
		
		 
		
		ByteBuffer bb = ByteBuffer.wrap( new byte[]{0,1,2,3,4,5,6} ) ;
		Iterator i ;
		bb.position(4);
		byte[] bbarray =   bb.array() ;
        byte[] data = new byte[bb.remaining()];
        bb.get(data, 0, bb.remaining());
        Map.Entry m;

		System.out.println("Hello World!" +  Arrays.toString(bbarray)); 
 		System.out.println("= " + Arrays.toString( data ));
 		
 	
 		int abc = Integer.parseInt("1080p");

 		byte[] temp = new byte[]{ 0x68,(byte) 0xca,0x43,(byte) 0xc8 };
 		 
 		int cast = 5;
 		switch(_MSG_TYPE.values()[cast]){
 		case MSG_TYPE_STATUS:
 			break;
 		default:
 			break;
 		}
 		
 		
 		
 		List<Integer> mList = new LinkedList<Integer>();
 		mList.add(1);
 		mList.add(2);
 		mList.add(3);
 		mList.add(4);
 		
 		System.out.println("total");
 		for(Integer loo : mList){
 			System.out.println(" loo " + loo);
 		}
 		
 		System.out.println("after remove 0 ");
 		mList.remove(0);
 		for(Integer loo : mList){
 			System.out.println(" loo " + loo);
 		}
 		
 		System.out.println("after remove 0 ");
 		mList.remove(0);
 		for(Integer loo : mList){
 			System.out.println(" loo " + loo);
 		}
 		
 		final Thread th1 = new Thread(){

			@Override
			public void run() {
				
				this.interrupt();
				System.out.println(" this.isInterrupted()  " + this.isInterrupted()  );
				System.out.println(" interrupted ? " +  Thread.interrupted()  );
			
				int i = 5; 
				while( i-- != 0 ){
					try {
						sleep(2000);
						// Thread.interrupted()假如当前的中断标志为true，则调完后会将中断标志位设置成false
						// 清除中断标记 返回原来中断状态
						System.out.println(" i " + i + " interrupted ? " +  this.isInterrupted()  );
					} catch (InterruptedException e) {
						//	sleep抛出异常后 Interrputed状态标记也会清除
						//	当有别的线程调用了本线程的interrupt( )时  会设置一个标记以表示这个这个线程被打断了
						//	当本线程捕获这个异常的时候，会清除这个标志  所以catch语句会永远报告说isInterrupted( )是false
						System.out.println("Thread.interrupted() ? " + this.isInterrupted());
						e.printStackTrace();
					}
				}
				super.run();
			}
 			
 		};
 		th1.start();
 		
 		new Thread(){
 			@Override
			public void run() {
 				try {
					sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 				th1.interrupt();
 				System.out.println("th1.interrupted() ? " + th1.isInterrupted());
 				
 			}
 		}.start();
 		
	}
	
}

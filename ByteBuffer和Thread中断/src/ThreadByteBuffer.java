

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
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
			sps = new byte[10]; // 不会修改 调用函数者给的byte[]实参 
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
			int  video_frame_index = 100 ;
			ByteBuffer snddata = ByteBuffer.allocateDirect(1300);
			snddata.put(0, (byte) ( video_frame_index & 0x000000FF  )        );
			snddata.put(1, (byte) ( (video_frame_index & 0x0000FF00 ) >> 8 ) );
			snddata.put(2, (byte) ( (video_frame_index & 0x00FF0000 ) >> 16) );
			snddata.put(3, (byte) ( (video_frame_index & 0xFF000000 ) >> 24) ); // 小端方式 
			snddata.order( ByteOrder.LITTLE_ENDIAN );  
			IntBuffer ibuf = snddata.asIntBuffer();
			ibuf.put(1, 100);
			System.out.println( String.format( "%x %x %x %x " , 
								snddata.get(7) , snddata.get(6) , snddata.get(5) , snddata.get(4) )
								); //  如果是 LILLTE_ENDIAN 0 0 0 64 ; 如果是 BIG_ENDIAN  64 0 0 0 
			// 1300 0 1300  put不会影响 position 
			System.out.println(" " + snddata.remaining() + " " + snddata.position() +  " " + snddata.capacity() );
		}

		{
			//String range = " npt= 0.000 -";
			String range = " npt=   22.000   -";
			 
			int start = range.indexOf("=");
			int end = range.indexOf("-");
			String sub = (String) range.subSequence(start + 1, end); // 范围是 [startIndex, endIndex)
			System.out.println( "=与-之间的子串:" + sub );
			float rangef = Float.parseFloat(sub) ;
			System.out.println( "解析出浮点数 :" + rangef );;
		}
		

		{
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
			//dbb.put((byte) 0); // 越界了会抛出异常 java.nio.BufferOverflowException	
		}
		

		{
			// 在 PC上  JVM allocateDirect的ByteBuffer 是不能  array()成 byte[]数组的 
			// 在Android上  allocateDirect的ByteBuffer 是可以转成数组的
			//            但是 由Native层  NewDirectByteBuffer 的 不能转成数组 
			ByteBuffer bbu = ByteBuffer.allocateDirect(100); 
			System.out.println( "bbu direct = " + bbu.isDirect() );
			//byte[] bba = bbu.array();
			//bba[0] = 1 ;
			//System.out.println( "bba = " + bba[0] );
		}

		{
			// 在Android 上 ByteBuffer.allocateDirect 是分配到 Dalvik Heap上的 , Native Heap不会增长
			ArrayList<ByteBuffer> direct_array  = new ArrayList<ByteBuffer>();
			for(int j = 0 ; j < 10 /*2048*/ ; j++){
				ByteBuffer bbu = ByteBuffer.allocateDirect(10000000); // 导致 java.lang.OutOfMemoryError: Direct buffer memory
				//ByteBuffer bbu = ByteBuffer.allocate(10000000); 	  // 导致  java.lang.OutOfMemoryError: Java heap space
				direct_array.add(bbu);
				System.out.println("add " + j + " into " + (bbu.isDirect()?"DirectByteBuffer ":"ByteBuffer ")+ " Array ");
				/*
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					System.out.println("Thread.sleep InterruptedException");;
					e.printStackTrace();
				}
				*/
			}
		}
		
		{	// ByteBuffer.allocateDirect 不用自己回收  没有引用自动释放
			
//			System.out.println("allocateDirect loop Entry ");
//			for( int n = 0 ; n < 2048 ; n ++ ){
//				ByteBuffer mall = ByteBuffer.allocateDirect(10000000);
//			}	
//			System.out.println("allocateDirect loop Done ");
		}
		
		{	// “带当前时间”的文件名字生成方法 
			SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			String file_uri = String.format("/mnt/sdcard/%s.mp4",   df.format(new Date())  ) ;   
			System.out.println("文件名字生成: " + file_uri );  // /mnt/sdcard/2017_12_04_10_38_50.mp4
		}

		{
			byte[] my_sps = null;
			System.out.println("before getSPS my_sps = " + my_sps );
			getSPS(my_sps); 
			System.out.println("after getSPS my_sps = " + my_sps ); // 依旧是空的
			
		}
		
		{	// byte[] 数组  转成  hex 显示
			byte[] test_sps = new byte[]{0,1,78,48,47};
			System.out.println("" + byteArray2Hex(test_sps) );
		}
		
		
		{
			ByteBuffer directbb = ByteBuffer.allocateDirect(10);
			// channel-read和put(把数据放到ByteBuffer中) 之前需要clear()  position设置为0  limit是容量  mark被抛弃
			directbb.clear(); 				// 准备"写数据到ByteBuffer" 
			directbb.put((byte) 18);		// 会影响postion
			directbb.put((byte) 12);
			System.out.println( String.format("before flip: capacity %d remaining %d limit %d ",
					directbb.capacity(),
					directbb.remaining(),
					directbb.limit()
					)); // capacity 10 remaining 8 limit 10
			
			// channel-wirte和get(把数据从ByteBuffer移出来)  limit设置为当前 position  position设置为0 容量  mark被抛弃
			directbb.flip();  				  // 准备读 
			System.out.println("directbb = " + directbb.get(0)) ; // 18  ByteBuffer.get(index) 不会影响postion 
			System.out.println( String.format("after flip  : capacity %d remaining %d limit %d ",
					directbb.capacity(),
					directbb.remaining(),
					directbb.limit()
					)); // capacity 10 remaining 2 limit 2 
			
			
			ByteBuffer bbtemp = ByteBuffer.allocate(directbb.remaining()); // 2 
			System.out.println( String.format("拷贝到另外一个ByteBuffer  : capacity %d remaining %d limit %d ",
					bbtemp.capacity(),
					bbtemp.remaining(), // clear之后，开始写入数据到ByteBuffer(channel-read)  代表 还有多少有限数据 
					bbtemp.limit()		// flip之后，开始从ByteBuffer读取数据(channel-write) 代表 还有剩余空闲空间 
										// 调用clear和flip都会discard mark
										// mark和reset配合使用
					)); // capacity 2 remaining 2 limit 2 
	 
			bbtemp.put(directbb); // put DirecBuffer 
			System.out.println("bbtemp = " + bbtemp.get(0)) ;	 // 18 
		}
	
		
		{
			ByteBuffer allocate = ByteBuffer.allocate(20);
			// allocate.clear(); // 刚刚allocate之后,不需要clear()
			System.out.println( 
					String.format("1 pos %d remain %d limit %d cap %d ",
							allocate.position() ,
							allocate.remaining() ,
							allocate.limit() ,
							allocate.capacity() )  ); 	// 1 pos 0 remain 20 limit 20 cap 20 
 
			allocate.put((byte) 12) ;
			allocate.put((byte) 13) ;
			System.out.println( 
					String.format("2 pos %d remain %d limit %d cap %d ",
							allocate.position() ,
							allocate.remaining() ,
							allocate.limit() ,
							allocate.capacity() )  );	// 2 pos 2 remain 18 limit 20 cap 20 
			
			byte[] arr = allocate.array();
			System.out.println("1 array " + Arrays.toString( arr ));
			System.out.println("1 buffer.get(1) " + allocate.get(1) ); // 13 
			arr[1] = 18 ; // 会直接修改ByteBuffer里面的数据
			System.out.println("2 array " + Arrays.toString( arr ));
			System.out.println("2 buffer.get(1) " + allocate.get(1) ); // 18 
		}
		
		
		{
			ByteBuffer bb = ByteBuffer.wrap( new byte[]{0,1,2,3,4,5,6} ) ;
			Iterator i ;
			bb.position(4);
			byte[] bbarray =   bb.array() ;
	        byte[] data = new byte[bb.remaining()];
	        bb.get(data, 0, bb.remaining());
	        Map.Entry m;

			System.out.println("Hello World!" +  Arrays.toString(bbarray)); 
	 		System.out.println("= " + Arrays.toString( data ));
		}
		
	
		{
	 		int abc = Integer.parseInt("1080p");

	 		byte[] temp = new byte[]{ 0x68,(byte) 0xca,0x43,(byte) 0xc8 };
	 		 
	 		int cast = 5;
	 		switch(_MSG_TYPE.values()[cast]){
	 		case MSG_TYPE_STATUS:
	 			break;
	 		default:
	 			break;
	 		}	
		}
 	

		{
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
		}
 		
		{
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
	
}

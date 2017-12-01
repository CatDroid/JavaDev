

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
	private int m1 = 0 ;
	private int m2 = 0; 
	
	final public void setM(int x , int y){
		System.out.println("Top> m1 " + m1 + " m2 " + m2 );
		m1 = x + 1; // �޸ĵ����Լ���m1 m2 ���Ǽ̳����
		m2 = y + 1 ;
		System.out.println("Top> m1 " + m1 + " m2 " + m2 );
	}
}

class TopSub extends Top
{
	private int m1 = 0 ;
	private int m2 = 0;
	
	public void set(int x , int y){
		System.out.println("TopSub> m1 " + m1 + " m2 " + m2 );
		m1 = x ; m2 = y;
		setM(m1 , m2 );
		System.out.println("TopSub> m1 " + m1 + " m2 " + m2 );
	}
	/*
		TopSub> m1 0 m2 0
		Top> m1 0 m2 0
		Top> m1 11 m2 16
		TopSub> m1 10 m2 15
	 */
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
		
		// XXX.class.isInstance(obj) �����ܲ��ܱ�ת��Ϊ�����
		// 1. һ�������Ǳ�����XXX.class��һ������ 
		// 2. һ�������ܱ�ת��Ϊ ������� �ӿ�
		// 3. ���ж�����תΪObject 
		// 4. XXX.class.isInstance(null) ����false NULL������ת�����κ���
		Main main = new Second();
		System.out.println( "Third "	+ Third.class.isInstance(main) );	// false
		System.out.println( "Second "	+ Second.class.isInstance(main) );  // true   
		System.out.println( "Main "		+ Main.class.isInstance(main) );	// true  ���༰��������   
		
		try {
			FileInputStream inputstream;
			inputstream = new FileInputStream("C:\\Users\\rd0394\\Desktop\\vrcamera_lut__\\rightlut.dat");
		 
			
			byte[] buffer = new byte[100];
			FileChannel channel = inputstream.getChannel();
			System.out.println("before channel position is " +channel.position() + ", available = " + inputstream.available()  );
			int read=inputstream.read(buffer);
			System.out.println("after read by FileInputStream " + read + ", channel position is " + channel.position() );
		
			ByteBuffer rdBuffer = ByteBuffer.allocateDirect(213);
			rdBuffer.clear(); read = channel.read(rdBuffer); rdBuffer.flip();
			System.out.println("after read by FileChannel " + read + ", channel position is " + channel.position() );
			
			//byte[] array = rdBuffer.array();
			//rdBuffer.arrayOffset() ;
			
			channel.close();
			inputstream.close();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("ERROR " +  e1.getMessage() );
			return ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR " +  e.getMessage() );
			return ;
		}
		
		TopSub p = new TopSub();
		p.set(10, 15);
		
		byte[] src = new byte[20] ;
		
		ByteBuffer dest = ByteBuffer.allocateDirect(100);
		ByteBuffer destShared = dest.duplicate() ;
		dest.put(src, 0, src.length ) ;  // �ӵ�ǰposition��ʼ  �޸�postion
		System.out.println("ByteBuffer.put(byte[] src, int offset, int length) " + dest.position() ) ;
		dest.put(0, (byte) 12) ; // ���ܵ�ǰpostion ������buffer��0��ʼ
		System.out.println("ByteBuffer.put(int index, byte b) alway start from  0 : " + dest.get(0) ) ;
		System.out.println("Shared ByteBuffer postioin(independent) " + destShared.position()  ) ;
		System.out.println("Shared ByteBuffer postioin(independent) " + destShared.get(0)  ) ;

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
			ByteBuffer bbu = ByteBuffer.allocateDirect(1000000); // Ҳ�ᵼ�� OOM Davlik Heap����256 ?? Ϊʲô����Davlik ������ Native Heap ??
			//ByteBuffer bbu = ByteBuffer.allocate(1000000); // ���ᵼ�� java.lang.OutOfMemoryError
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
		arr[0] = 18 ; // ��ֱ���޸�ByteBuffer���������
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
						// Thread.interrupted()���統ǰ���жϱ�־Ϊtrue��������Ὣ�жϱ�־λ���ó�false
						// ����жϱ�� ����ԭ���ж�״̬
						System.out.println(" i " + i + " interrupted ? " +  this.isInterrupted()  );
					} catch (InterruptedException e) {
						//	sleep�׳��쳣�� Interrputed״̬���Ҳ�����
						//	���б���̵߳����˱��̵߳�interrupt( )ʱ  ������һ������Ա�ʾ�������̱߳������
						//	�����̲߳�������쳣��ʱ�򣬻���������־  ����catch������Զ����˵isInterrupted( )��false
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

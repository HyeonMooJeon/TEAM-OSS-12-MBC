package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class ParentsThread extends Thread{
	private Socket socket = null;
	public Hashtable<String, String> ht;

	  private static BufferedReader mIn;
	  private static PrintWriter mOut;
	  static String inputLine = null;
	  static String datasend = null;
	
	  static String Tag = null;
	  static String Loca = null; // 아이클라가처음에 null값 보내서 오류방지 
	  static String ParentTag = null;
	  static String[] Sending;
	  static String key = null;

	  OutputStream os = null;
	  OutputStreamWriter osw = null;
	  BufferedWriter bw = null;
	
	  InputStream is = null;
	  InputStreamReader isr = null;
	  BufferedReader br = null;
	
	  Socket clientSocket = null;
	  PrintWriter out = null;
	  BufferedReader in = null;
	  
	
	 public ParentsThread(Socket socket, Hashtable <String, String> ht) {
		    this.socket = socket;
		    this.ht = ht;
		  }
	 public void sleep(int time)
	  {
	    try {
	      Thread.sleep(time); // 브레이크를 걸어주기위해. 다른 쓰레드의 작업을 처리하기위해 사용 
	    }
	    catch (InterruptedException localInterruptedException)
	    {
	    }
	  }
	
	  public void run() //synchronized 로 동기화 
	  {
	    try
	    {
	      OutputStream stream = socket.getOutputStream();
	
	      mIn = new BufferedReader(
	        new InputStreamReader(socket.getInputStream()));
	
	      mOut = new PrintWriter(socket.getOutputStream());
	      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	
	     out = new PrintWriter(socket.getOutputStream(), true);
	    //  os = this.socket.getOutputStream();
	   //   osw = new OutputStreamWriter(os);
	    //  bw = new BufferedWriter(osw);
	      is = socket.getInputStream();
	
	      isr = new InputStreamReader(is);
	      br = new BufferedReader(isr);
	     
	      
	      System.out.println("페어런트 와일문 전");

	     
		      while (true) 
		     {
		    	  this.sleep(1000);
				System.out.println("서버에서 보낸 데이터 : " + Loca);
				os = socket.getOutputStream();
				osw = new OutputStreamWriter(os);
				bw = new BufferedWriter(osw);
				
	            bw = new BufferedWriter(osw);
		
	            Enumeration HT = ht.keys(); //각각의 객체를 하나씩 처리 
				
		        System.out.println("while문");
		        key = HT.nextElement().toString();
		
		        System.out.println("해시테이블 get key값 : " +	ht.get(key));

		       
				bw.write(ht.get(key));
				bw.newLine();
				bw.flush();

				this.sleep(1000);
		     } //while문
	     
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	    finally
	    {
	      try
	      { 
	        socket.close();
	        System.out.println("▶ 클라이언트 연결이 종료되었습니다.");
	      }
	      catch (IOException e)
	      {
	        e.printStackTrace();
	      }
	    }
	  }
}
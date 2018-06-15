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

public class ChildThread extends Thread{
	private Socket socket = null;
	public Hashtable<String, String> ht;
	  private static BufferedReader mIn;
	  private static PrintWriter mOut;
	  static String inputLine = null;
	  static String datasend = null;
	
	  static String Tag = null;
	  static String Loca = "36.799210, 127.074920"; // 아이클라가처음에 null값 보내서 오류방지 
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
	  
	 public ChildThread(Socket socket, Hashtable <String, String> ht) {
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
	 
	      
	      System.out.println("차일드 와일문 전");

		      while (true) 
		     {
			      ParentTag = mIn.readLine();
			        	
					          Sending = this.ParentTag.split(","); // 태그명,위도/경도 형태로 들어오는 데이터를  ','를 기준으로 받은 데이터를 나눔
					          Tag = Sending[0];  // 태그를  sending[0]에
					          Loca = Sending[1];  // 위치정보를 sending[1]에 저장
					          
					          ht.put(Sending[0], Sending[1]); //해쉬테이블에 태그 , 위치값을 넣음
					
					          
					          Enumeration HT = ht.keys(); //각각의 객체를 하나씩 처리 
					
					          System.out.println("while문 -  more element ");
					          key = HT.nextElement().toString();
					
					          System.out.println("▷ key 값 : " + key + "  ▷ value 값 : " + ht.get(key)); //키값과 밸유값을 출력
					          
					      	this.sleep(10);
					       
		     }//while
	   
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

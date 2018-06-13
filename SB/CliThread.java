package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class CliThread extends Thread
{
  private Socket socket = null;
  private static BufferedReader mIn;
  private static PrintWriter mOut;
  static String inputLine = null;
  static String datasend = null;

  static String Tag = null;
  static String Loca = "36.799210, 127.074920"; // 아이클라가처음에 null값 보내서 오류방지 
  String ParentTag = null;
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
  Hashtable<String, String> table = new Hashtable(1); //해쉬테이블 생성

  public CliThread(Socket socket) {
    this.socket = socket;
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

  public synchronized void run() //synchronized 로 동기화 
  {
    try
    {
      OutputStream stream = socket.getOutputStream();

      mIn = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));

      mOut = new PrintWriter(socket.getOutputStream());
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

     out = new PrintWriter(socket.getOutputStream(), true);
      os = this.socket.getOutputStream();
      osw = new OutputStreamWriter(os);
      bw = new BufferedWriter(osw);
      is = socket.getInputStream();

      isr = new InputStreamReader(is);
      br = new BufferedReader(isr);
      synchronized(this)
      {
      while ((ParentTag = mIn.readLine()) != null) //읽어들일데이터가 없을떄까지 동작 
	      {
	        
    	  System.out.println("while문 집입 후 "+ParentTag);
	
	        if (ParentTag.contains(",")) // ','가있는 데이터 즉 아이클라가 보낸 데이터 처리
		    {
		          
		          Sending = ParentTag.split(","); // 태그명,위도/경도 형태로 들어오는 데이터를  ','를 기준으로 받은 데이터를 나눔
		          Tag = Sending[0];  // 태그를  sending[0]에
		          Loca = Sending[1];  // 위치정보를 sending[1]에 저장
		
		          table.put(Sending[0], Sending[1]); //해쉬테이블에 태그 , 위치값을 넣음
		
		          
		          Enumeration HT = table.keys(); //각각의 객체를 하나씩 처리 
		
		          System.out.println("while문 -  more element ");
		          key = HT.nextElement().toString();
		
		          System.out.println("▷ key 값 : " + key + "  ▷ value 값 : " + table.get(key)); //키값과 밸유값을 출력
		          
		    }
	        else if (ParentTag.equals(key)) //부모클라가 태그를 보내고 아이클라와 태그가 동일할시 작동
		        {
		          System.out.println("else if 문 들어옴 ");
		
		          while((ParentTag = mIn.readLine()) != null)
		          //while (true) //조건문 돔
		          {
		            System.out.println("서버에서 보낸 데이터 : " + Loca);
		            inputLine = Loca;
		
		            bw.write(inputLine); //부모클라에게 써줌
		            bw.newLine();
		            bw.flush();
		            System.out.println("부모 클라이언트로부터 받은 데이터 : " + ParentTag);
		            this.sleep(10); //다른쓰레드로 넘기기위해
		          }
		        }
	        else
	        {
	          System.out.println("else문. 위 조건 안걸림 ");
	        }
	
	        this.sleep(10); //다른쓰레드로 넘기기위해
	      } //while문
      }
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
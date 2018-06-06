package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class Thread implements Runnable{

	private Socket socket = null;
	private static BufferedReader mIn;    // 들어오는 통로
	private static PrintWriter mOut;  // 나가는 통로
	
	public Thread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {
			// 응답을 위해 스트림을 얻음
			OutputStream stream = socket.getOutputStream();
			//InputStream Istream = clientSocket.getInputStream();//수정 0607
						
			 mIn = new BufferedReader( // 클라에서 정보를 읽어들임 
	                 new InputStreamReader(socket.getInputStream()));

	         mOut = new PrintWriter(socket.getOutputStream());
	          
	         
	         while(!(mIn.readLine()==null)) 
	         { //아이 클라에게서 받은 문자열이 null일때까지 돌림
	        	 String[] Sending = mIn.readLine().split("/");  // Sending배열 만듦. 이건 부모 클라에게 쏴줄 데이터, "/"를 기준으로 아이클라에게서 받은 클라를 스플릿함
	        	 String Tag = Sending[0]; // 스플릿한 부분중 앞부분을 tag로
	        	 String Loca = Sending[1]; //스플릿한 부분중 뒷부분을 loca (location)으로
	        	 
	        	 Hashtable<String, String> table = new Hashtable<String, String>(); // 키값으로 string, value 값으로 string 객체를 가지는 해시 테이블 생성
	        	 table.put(Sending[0], Sending[1]); // tag, loca를 키, value값으로 집어넣음
	        	 //System.out.println(Tag);
	        	 //System.out.println(Loca);
	        	 //System.out.println(mIn.readLine());
	             

	        	 Enumeration HT = table.keys(); //Enumeration 인터페이스는 객체들의 집합(Vector)에서 각각의 객체를 한순간에 하나씩 처리할 수 있는 메소드를 제공
	        	 
	        	 while(HT.hasMoreElements()){
	        		 String key = HT.nextElement().toString();
	        		 System.out.println("▷ key 값 : " + key + "  ▷ value 값 : " + table.get(key));
	        		 
	        		 mOut.write(key + table.get(key)); // key, value 받은걸 클라이언트로 쏴줌.
	        	 }
	        	 table.clear(); // hashtable 초기화
	      	}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close(); // 소켓 종료.
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
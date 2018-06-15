package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class Server_Main {
   private static final int PORT = 8079; //포트 지정
   
 //버퍼 다해놓음 
   public void go() throws IOException{
	 
       Socket clientSocket = null; // 클라소켓   0610
       ServerSocket serverSocket = null; //서버소켓  0610
     
       ///
       InputStream is = null;
 	  InputStreamReader isr = null;
 	 
 	 BufferedReader in = null;
	
       ///
       
       
       System.out.println("┌─────────────────────────────────────────────┐"); 
       System.out.println("│               서버 소켓이 생성되었습니다                                          │");        
       System.out.println("│               클라이언트 연결 대기중입니다                                       │");  
       System.out.println("└─────────────────────────────────────────────┘"); 
  
       
      try {
         
    	 
    	 serverSocket = new ServerSocket(PORT); // 서버소켓 생성
    	 Hashtable<String, String> ht = new Hashtable<String, String>(); //해쉬테이블 생성
    
    	 while(true){ //프로그램 종료까지 루프
    		 //클라소켓 접속대기
             System.out.println("▶ 클라이언트 접속 대기중."); 
             clientSocket = serverSocket.accept(); //클라이언트로부터 데이터가 오는것을 감지c
                                                                                                       //전송한다는 뜻.
             
             InetAddress ia = clientSocket.getInetAddress();
             String ip = ia.getHostAddress(); // 접속된 원격 Client IP 
             System.out.println("▶ 클라이언트가 연결되었습니다.");
             
             
             int port = clientSocket.getLocalPort();// 접속에 사용된 서버측 PORT 
             int client_port = clientSocket.getPort(); // 원격 Client PORT 0610수정내용

             System.out.println("▶ 클라이언트가 연결되었습니다."); 
             System.out.println("▶ Server Log    Local Port : " + port); //접속에 사용된 서버포트 0610
             System.out.print("▶ Client Log    Client Port : "+ client_port); //클라이언트 로그, 접속 ip, port
             System.out.println(" IP : " + ip);
          
            
             
             //읽은태그구분
             //페어런츠인지 차일드인지 구분해서 스레드 각자생성
             //위도 경도 태그 정보는 이 클래스에서 저장
             //사용할곳은 각 스레드
             //저장은 메인에 
             in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             
             String data = in.readLine(); 
             	//한번 리드라인 여기서 읽어옴
            	
             if( data.contains(",")){
            	 //스플릿 나눈다. 0번째는 태그 여기서 ,콤마가있으면 자식스레드
            	 // ,가없으면 부모스레드    
            	 ChildThread childThread = new ChildThread(clientSocket, ht);
            	 childThread.start();
            	 //CliThread cliThread = new CliThread(clientSocket);
                 //cliThread.start();
             }else{
            	 ParentsThread parentThread = new ParentsThread(clientSocket, ht);
            	 parentThread.start();
             }
             
          
    	 }//while문 
         
      }finally{
   	   if (clientSocket != null)
		   clientSocket.close();
		   if (serverSocket != null)
			   serverSocket.close();
		   System.out.println("**서버 종료**");   
		  }
		 }
		 public static void main(String[] args) {
			 Server_Main ts = new Server_Main();
		  try {
		   ts.go();
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		  
		 }
	}

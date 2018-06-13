package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server_Main {
   private static final int PORT = 8070; //포트 지정

  // private static final int THREAD_CNT = 30; //쓰레드 풀 최대 쓰레드 개수 지정. 쓰레드 풀은 미리 생성해놓은 쓰레드로 작업 할당. 30번의 쓰레드가 돌 수있음. 즉 30명의 클라 접속.
 //  private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_CNT);
   
   //static String inputLine = "37.056203,127.06970060000003"; // 좌표주소 ㅡ 송탄고

   public static void main(String[] args) {
	 
       Socket clientSocket = null; // 클라소켓   0610
       ServerSocket serverSocket = null; //서버소켓  0610
       //int connectCount = 0; //연결된 소켓수를 확인하기 위한 변수   0610
     
       
       System.out.println("┌─────────────────────────────────────────────┐"); 
       System.out.println("│               서버 소켓이 생성되었습니다                                          │");        
       System.out.println("│               클라이언트 연결 대기중입니다                                       │");  
       System.out.println("└─────────────────────────────────────────────┘"); 
  
       
      try {
         
    	 
    	 serverSocket = new ServerSocket(PORT); // 서버소켓 생성
         
    
    	 while(true){ //프로그램 종료까지 루프
    		 //클라소켓 접속대기
             System.out.println("▶ 클라이언트 접속 대기중."); 
             clientSocket = serverSocket.accept(); //클라이언트로부터 데이터가 오는것을 감지
                                                                                                       //전송한다는 뜻.
             
          
            
             InetAddress ia = clientSocket.getInetAddress();
             String ip = ia.getHostAddress(); // 접속된 원격 Client IP 
             System.out.println("▶ 클라이언트가 연결되었습니다.");
             
             
             int port = clientSocket.getLocalPort();// 접속에 사용된 서버측 PORT 
            
             
             int client_port = clientSocket.getPort(); // 원격 Client PORT 0610수정내용

            // ++connectCount;  //접속자수 카운트를 위한 변수      
             System.out.println("▶ 클라이언트가 연결되었습니다."); 
             //System.out.println("▶ " + connectCount + "명의 클라이언트가 접속되어 있습니다.");
             System.out.println("▶ Server Log    Local Port : " + port); //접속에 사용된 서버포트 0610

             System.out.print("▶ Client Log    Client Port : "+ client_port); //클라이언트 로그, 접속 ip, port
             System.out.println(" IP : " + ip);
          
             
             
           CliThread cliThread = new CliThread(clientSocket);
           cliThread.start();
             
          
    	 }//while문 
         
      } catch (IOException e) {
         e.printStackTrace();
      }
      
   }  
}

package Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server_Main {
   private static final int PORT = 9043; //포트 지정
   private static final int THREAD_CNT = 30; //쓰레드 풀 최대 쓰레드 개수 지정. 쓰레드 풀은 미리 생성해놓은 쓰레드로 작업 할당
   private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_CNT);
   
   static String inputLine = "37.056203,127.06970060000003"; // 좌표주소 ㅡ 송탄고
   static String datasend = null;
   
   public static void main(String[] args) {

      Socket clientSocket = null; // 서버소켓이 지정한 포트를 타고온 상대 ip를 저장할 수 있다.
       PrintWriter out = null;  // String 타입의 문자를 보낼수 있는 함수.
       BufferedReader in = null;//stream 타입의 문자를 읽어서 저장할 수 있는 함수.
       ArrayList<String> arrayList = new ArrayList<>();
      
       /*추가구간
       // 데이터를 보내는 레퍼런스
       OutputStream out = null; 
       DataOutputStream dos = null; 
       
       // 데이터를 받을 레퍼런스 
       InputStream in = null;
       DataInputStream din = null; 
       
       Scanner scanner = new Scanner(System.in);
		추가구간*/
    
       
      try {
         
         ServerSocket serverSocket = new ServerSocket(PORT); // 서버소켓 생성
         
         System.out.println("┌─────────────────────────────────────────────┐"); 
         System.out.println("│               서버 소켓이 생성되었습니다                                          │");        
         System.out.println("│               클라이언트 연결 대기중입니다                                       │");  
         System.out.println("└─────────────────────────────────────────────┘"); 

         clientSocket = serverSocket.accept(); //클라이언트로부터 데이터가 오는것을 감지 //위쪽 try부분에 있었음 
         String reip = clientSocket.getInetAddress().getHostAddress(); //위쪽 try부분에 있었음 
         System.out.println("▷클라이언트가 연결되었습니다.");  //위쪽 try부분에 있었음 
         System.out.println("▷Client Log : " +reip); //클라이언트 주소 받아옴            //위쪽 try부분에 있었음  
         
        /*추가 구간
        // 데이터를 보낼 준비        
         out = clientSocket.getOutputStream(); 
         dos = new DataOutputStream(out); 
         
         // 데이터를 받을 준비 
         in = clientSocket.getInputStream(); 
         din = new DataInputStream(in);
                      추가구간*/
         
         //datasend = inputLine;   
         
          // 수정부분 out = new PrintWriter(clientSocket.getOutputStream(), true); 
          // 수정부분 in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
         //소켓에서 넘오는 stream 형태의 문자를 얻은 후 읽어 들어서                                                                                              
           //bufferstream 형태로 in 에 저장.
                   
         //소켓 서버 종료될때까지 무한 루프.
         while(true){
            // 소켓 접속 요청이 올때까지 대기.
            Socket socket = serverSocket.accept();
           
            /*추가부분
            String userMsg = din.readUTF(); 
            System.out.println("사용자 메시지:" + userMsg); 
            
            if(userMsg.equals("EXIT"))break; // 받을 메시지 다시 전송 유저가 종료 입력하면 종료를 다시 유저에게 반환하여
            dos.writeUTF(userMsg);          // 유저의 while문을 탈출함 
            dos.flush();//버퍼 제거 
      		추가부분      */
            
            try{
               // 요청이 오면 스레드 풀의 스레드로 소켓을 넣어줌.
               // 이후는 스레드 내에서 처리.
               //datasend=inputLine;
                  
                 // out.println(inputLine); //돌아온값을 다시 되돌려 보낸다. //String이 stream으로 변환되어 전송됨.
                  //out.flush();
                  //if (inputLine.equals("null")) { //만약 받은 값이 null 일경우 종료
                  //serverSocket.close();
                  //break;
                  
               threadPool.execute(new Thread(socket));
            }catch(Exception e){
               e.printStackTrace();
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      /*추가부분
      finally { 
    	  // 읽는 스트림 종료
    	  if( din != null ){ 
    		  try{din.close();} 
    		  catch(Exception e){} 
    	  } 
    	  if( in != null ){ 
    		  try{in.close();} 
    		  catch(Exception e){} 
    		  } 
    	  
    	  // 쓰는 스트림 종료
    	  if( dos != null ){ 
    		  try{din.close();} 
    		  catch(Exception e){} 
    		  } 
    	  if( out != null ){ 
    		  try{in.close();} 
    		  catch(Exception e){} 
    		  } 
    	  // 네트워크 종료 
    	  if( clientSocket != null ){ 
    		  try{clientSocket.close();}
    		  catch(Exception e){} }
    	  }// end finally
      		추가부분*/
      
 }
   
 
public static String data_send() {
         return datasend;
         }
}


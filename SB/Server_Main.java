package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server_Main {
   private static final int PORT = 9049; //포트 지정
   private static final int THREAD_CNT = 30; //쓰레드 풀 최대 쓰레드 개수 지정. 쓰레드 풀은 미리 생성해놓은 쓰레드로 작업 할당
   private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_CNT);
   
   static String inputLine = "37.056203,127.06970060000003"; // 좌표주소 ㅡ 송탄고
   static String datasend = null;
   //추가부분
   private static BufferedReader mIn;    // 들어오는 통로
   private static PrintWriter mOut;  // 나가는 통로
   //추가부분
  
   public static void main(String[] args) {

      Socket clientSocket = null; // 서버소켓이 지정한 포트를 타고온 상대 ip를 저장할 수 있다.
       //수정부분 PrintWriter out = null;  // String 타입의 문자를 보낼수 있는 함수.
       //수정부분 BufferedReader in = null;//stream 타입의 문자를 읽어서 저장할 수 있는 함수.
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

         clientSocket = serverSocket.accept(); //클라이언트로부터 데이터가 오는것을 감지 
         String reip = clientSocket.getInetAddress().getHostAddress(); 
         System.out.println("▷클라이언트가 연결되었습니다.");  
         System.out.println("▷Client Log : " +reip); //클라이언트 주소 받아옴         
         
         mIn = new BufferedReader(
                 new InputStreamReader(clientSocket.getInputStream()));

         mOut = new PrintWriter(clientSocket.getOutputStream());
         
         
         
         // 클라이언트에서 보낸 문자열 출력
         while(!(mIn.readLine()==null)){ //아이 클라에게서 받은 문자열이 null일때까지 돌림
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
        		 System.out.println("▶키 값 : " + key + "  ▶value 값 : " + table.get(key));
        	 }
        	 table.clear(); // hashtable 초기화
      
        	
         }
         
        /*추가 구간
        // 데이터를 보낼 준비        
         out = clientSocket.getOutputStream(); 
         dos = new DataOutputStream(out); 
         
         // 데이터를 받을 준비 
         in = clientSocket.getInputStream(); 
         din = new DataInputStream(in);
                      추가구간*/
         
         //datasend = inputLine;   
         
         
         // ******  data outputstream 사용해보기 
          //out = new PrintWriter(clientSocket.getOutputStream(), true); 
          //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

         //소켓에서 넘오는 stream 형태의 문자를 얻은 후 읽어 들어서                                                                                              
           //bufferstream 형태로 in 에 저장.
                
       
         
         //}
         
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
      
      
 }
   
 
public static String data_send() {
         return datasend;
         }
}


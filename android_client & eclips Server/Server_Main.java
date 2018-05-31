package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server_Main {
	//static ClientGUI clientgui;
	static String inputLine = null;
	static String datasend = null;
	public static void main(String[] args) throws IOException {  // 메인함수, IOException
	
		ServerSocket serverSocket = null; //서버소켓 생성
		Socket clientSocket = null; // 서버소켓이 지정한 포트를 타고온 상대 ip를 저장할 수 있다.
		PrintWriter out = null;  // String 타입의 문자를 보낼수 있는 함수.
		BufferedReader in = null;//stream 타입의 문자를 읽어서 저장할 수 있는 함수.
		ArrayList<String> arrayList = new ArrayList<>();
		
		OutputStream os = null;
	    OutputStreamWriter osw =null;
	    BufferedWriter bw = null;
	        
	    InputStream is =null;
	    InputStreamReader isr = null;
	    BufferedReader br = null;

		serverSocket = new ServerSocket(8846);//서버소켓 생성
		
		System.out.println("서버 소켓 생성");
		
		//ClientGUI clientgui = new ClientGUI();
		 inputLine= "37.056203,127.06970060000003";

		try {
			// 서버 소켓을 만들고 연결을 기다린다.
			System.out.println("클라이언트 연결 기다리는중");
			clientSocket = serverSocket.accept(); //클라이언트로부터 데이터가 오는것을 감지한다.
			String reip = clientSocket.getInetAddress().getHostAddress();
			 System.out.println("Log : " +reip);
			System.out.println("클라이언트 연결");

			// 클라이언트로 부터 데이터를 받는다.
		    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //소켓에서 넘오는 stream 형태의 문자를 얻은 후 읽어 들어서                                                                                            
		       //bufferstream 형태로 in 에 저장.
			out = new PrintWriter(clientSocket.getOutputStream(), true); //String 타입을 stream 형태로 변환하여 
			os =clientSocket.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			is = clientSocket.getInputStream();
	        isr = new InputStreamReader(is);
	        br = new BufferedReader(isr);        // 서버로부터 Data를 받음
	        
	        while(true) {
	        	bw.write(inputLine);
				bw.newLine();
	            bw.flush();          
	            
	            String receiveData = "";
	            receiveData = br.readLine();        // 서버로부터 데이터 한줄 읽음
	            System.out.println("서버로부터 받은 데이터 : " + receiveData);
		   
	        }      
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
            	System.out.println("소켓 종료 ");
                bw.close();
                osw.close();
                os.close();
                br.close();
                isr.close();
                is.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }    
        
    }
    
}

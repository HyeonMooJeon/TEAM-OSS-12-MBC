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
   private static final int PORT = 9043; //��Ʈ ����
   private static final int THREAD_CNT = 30; //������ Ǯ �ִ� ������ ���� ����. ������ Ǯ�� �̸� �����س��� ������� �۾� �Ҵ�
   private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_CNT);
   
   static String inputLine = "37.056203,127.06970060000003"; // ��ǥ�ּ� �� ��ź��
   static String datasend = null;
   
   public static void main(String[] args) {

      Socket clientSocket = null; // ���������� ������ ��Ʈ�� Ÿ��� ��� ip�� ������ �� �ִ�.
       PrintWriter out = null;  // String Ÿ���� ���ڸ� ������ �ִ� �Լ�.
       BufferedReader in = null;//stream Ÿ���� ���ڸ� �о ������ �� �ִ� �Լ�.
       ArrayList<String> arrayList = new ArrayList<>();
      
       /*�߰�����
       // �����͸� ������ ���۷���
       OutputStream out = null; 
       DataOutputStream dos = null; 
       
       // �����͸� ���� ���۷��� 
       InputStream in = null;
       DataInputStream din = null; 
       
       Scanner scanner = new Scanner(System.in);
		�߰�����*/
    
       
      try {
         
         ServerSocket serverSocket = new ServerSocket(PORT); // �������� ����
         
         System.out.println("����������������������������������������������������������������������������������������������"); 
         System.out.println("��               ���� ������ �����Ǿ����ϴ�                                          ��");        
         System.out.println("��               Ŭ���̾�Ʈ ���� ������Դϴ�                                       ��");  
         System.out.println("����������������������������������������������������������������������������������������������"); 

         clientSocket = serverSocket.accept(); //Ŭ���̾�Ʈ�κ��� �����Ͱ� ���°��� ���� //���� try�κп� �־��� 
         String reip = clientSocket.getInetAddress().getHostAddress(); //���� try�κп� �־��� 
         System.out.println("��Ŭ���̾�Ʈ�� ����Ǿ����ϴ�.");  //���� try�κп� �־��� 
         System.out.println("��Client Log : " +reip); //Ŭ���̾�Ʈ �ּ� �޾ƿ�            //���� try�κп� �־���  
         
        /*�߰� ����
        // �����͸� ���� �غ�        
         out = clientSocket.getOutputStream(); 
         dos = new DataOutputStream(out); 
         
         // �����͸� ���� �غ� 
         in = clientSocket.getInputStream(); 
         din = new DataInputStream(in);
                      �߰�����*/
         
         //datasend = inputLine;   
         
          // �����κ� out = new PrintWriter(clientSocket.getOutputStream(), true); 
          // �����κ� in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
         //���Ͽ��� �ѿ��� stream ������ ���ڸ� ���� �� �о� ��                                                                                              
           //bufferstream ���·� in �� ����.
                   
         //���� ���� ����ɶ����� ���� ����.
         while(true){
            // ���� ���� ��û�� �ö����� ���.
            Socket socket = serverSocket.accept();
           
            /*�߰��κ�
            String userMsg = din.readUTF(); 
            System.out.println("����� �޽���:" + userMsg); 
            
            if(userMsg.equals("EXIT"))break; // ���� �޽��� �ٽ� ���� ������ ���� �Է��ϸ� ���Ḧ �ٽ� �������� ��ȯ�Ͽ�
            dos.writeUTF(userMsg);          // ������ while���� Ż���� 
            dos.flush();//���� ���� 
      		�߰��κ�      */
            
            try{
               // ��û�� ���� ������ Ǯ�� ������� ������ �־���.
               // ���Ĵ� ������ ������ ó��.
               //datasend=inputLine;
                  
                 // out.println(inputLine); //���ƿ°��� �ٽ� �ǵ��� ������. //String�� stream���� ��ȯ�Ǿ� ���۵�.
                  //out.flush();
                  //if (inputLine.equals("null")) { //���� ���� ���� null �ϰ�� ����
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
      /*�߰��κ�
      finally { 
    	  // �д� ��Ʈ�� ����
    	  if( din != null ){ 
    		  try{din.close();} 
    		  catch(Exception e){} 
    	  } 
    	  if( in != null ){ 
    		  try{in.close();} 
    		  catch(Exception e){} 
    		  } 
    	  
    	  // ���� ��Ʈ�� ����
    	  if( dos != null ){ 
    		  try{din.close();} 
    		  catch(Exception e){} 
    		  } 
    	  if( out != null ){ 
    		  try{in.close();} 
    		  catch(Exception e){} 
    		  } 
    	  // ��Ʈ��ũ ���� 
    	  if( clientSocket != null ){ 
    		  try{clientSocket.close();}
    		  catch(Exception e){} }
    	  }// end finally
      		�߰��κ�*/
      
 }
   
 
public static String data_send() {
         return datasend;
         }
}


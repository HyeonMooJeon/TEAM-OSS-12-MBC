package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server_Main {
   private static final int PORT = 9066; //��Ʈ ����
   private static final int THREAD_CNT = 30; //������ Ǯ �ִ� ������ ���� ����. ������ Ǯ�� �̸� �����س��� ������� �۾� �Ҵ�. 30���� �����尡 �� ������. �� 30���� Ŭ�� ����.
   private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_CNT);
   
   static String inputLine = "37.056203,127.06970060000003"; // ��ǥ�ּ� �� ��ź��
  
   public static void main(String[] args) {
	 
       Socket clientSocket = null; // Ŭ����� 
       ServerSocket serverSocket = null; //��������  0608
       int connectCount = 0; //����� ���ϼ��� Ȯ���ϱ� ���� ����  0607
     
       System.out.println("����������������������������������������������������������������������������������������������"); 
       System.out.println("��               ���� ������ �����Ǿ����ϴ�                                          ��");        
       System.out.println("��               Ŭ���̾�Ʈ ���� ������Դϴ�                                       ��");  
       System.out.println("����������������������������������������������������������������������������������������������"); 
  
       
      try {
         
    	 
    	 serverSocket = new ServerSocket(PORT); // �������� ����
         
    	 //0607 �߰��κ�
    	 while(true){ //���α׷� ������� ����
             clientSocket = serverSocket.accept(); //Ŭ���̾�Ʈ�κ��� �����Ͱ� ���°��� ���� 
             threadPool.execute(new Thread(clientSocket)); //0608 �������� 
             InetAddress ia = clientSocket.getInetAddress();
             int port = clientSocket.getLocalPort();// ���ӿ� ���� ������ PORT 
             String ip = ia.getHostAddress(); // ���ӵ� ���� Client IP 
             
             ++connectCount;  //�����ڼ� ī��Ʈ�� ���� ����      
             System.out.println("�� Ŭ���̾�Ʈ�� ����Ǿ����ϴ�."); 
             System.out.println("�� " + connectCount + "���� Ŭ���̾�Ʈ�� ���ӵǾ� �ֽ��ϴ�.");
             System.out.print("�� Client Log    Port : "+ port);
             System.out.println(" IP : " + ip);
             
             
             /* 0608 �������� 
             try{//��û�� ���� ������ Ǯ ������� �ѱ�   ���Ĵ� �����忡�� ó��
            	 threadPool.execute(new Thread(clientSocket));
            	 
            	
             }catch(Exception e){
            	 e.printStackTrace();
             }
            0608 �������� */
    	 }//while�� 
         
      } catch (IOException e) {
         e.printStackTrace();
      }
      //0608 �߰�
      finally { //�������� ���������߰� 
          try {
            if( serverSocket != null && !serverSocket.isClosed() ) {
            	serverSocket.close();
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
      }//finally�� 
      //0608 �߰�
      
      
   }  
}



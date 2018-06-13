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
   private static final int PORT = 8070; //��Ʈ ����

  // private static final int THREAD_CNT = 30; //������ Ǯ �ִ� ������ ���� ����. ������ Ǯ�� �̸� �����س��� ������� �۾� �Ҵ�. 30���� �����尡 �� ������. �� 30���� Ŭ�� ����.
 //  private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_CNT);
   
   //static String inputLine = "37.056203,127.06970060000003"; // ��ǥ�ּ� �� ��ź��

   public static void main(String[] args) {
	 
       Socket clientSocket = null; // Ŭ�����   0610
       ServerSocket serverSocket = null; //��������  0610
       //int connectCount = 0; //����� ���ϼ��� Ȯ���ϱ� ���� ����   0610
     
       
       System.out.println("����������������������������������������������������������������������������������������������"); 
       System.out.println("��               ���� ������ �����Ǿ����ϴ�                                          ��");        
       System.out.println("��               Ŭ���̾�Ʈ ���� ������Դϴ�                                       ��");  
       System.out.println("����������������������������������������������������������������������������������������������"); 
  
       
      try {
         
    	 
    	 serverSocket = new ServerSocket(PORT); // �������� ����
         
    
    	 while(true){ //���α׷� ������� ����
    		 //Ŭ����� ���Ӵ��
             System.out.println("�� Ŭ���̾�Ʈ ���� �����."); 
             clientSocket = serverSocket.accept(); //Ŭ���̾�Ʈ�κ��� �����Ͱ� ���°��� ����
                                                                                                       //�����Ѵٴ� ��.
             
          
            
             InetAddress ia = clientSocket.getInetAddress();
             String ip = ia.getHostAddress(); // ���ӵ� ���� Client IP 
             System.out.println("�� Ŭ���̾�Ʈ�� ����Ǿ����ϴ�.");
             
             
             int port = clientSocket.getLocalPort();// ���ӿ� ���� ������ PORT 
            
             
             int client_port = clientSocket.getPort(); // ���� Client PORT 0610��������

            // ++connectCount;  //�����ڼ� ī��Ʈ�� ���� ����      
             System.out.println("�� Ŭ���̾�Ʈ�� ����Ǿ����ϴ�."); 
             //System.out.println("�� " + connectCount + "���� Ŭ���̾�Ʈ�� ���ӵǾ� �ֽ��ϴ�.");
             System.out.println("�� Server Log    Local Port : " + port); //���ӿ� ���� ������Ʈ 0610

             System.out.print("�� Client Log    Client Port : "+ client_port); //Ŭ���̾�Ʈ �α�, ���� ip, port
             System.out.println(" IP : " + ip);
          
             
             
           CliThread cliThread = new CliThread(clientSocket);
           cliThread.start();
             
          
    	 }//while�� 
         
      } catch (IOException e) {
         e.printStackTrace();
      }
      
   }  
}

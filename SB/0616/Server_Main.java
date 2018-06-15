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
   private static final int PORT = 8079; //��Ʈ ����
   
 //���� ���س��� 
   public void go() throws IOException{
	 
       Socket clientSocket = null; // Ŭ�����   0610
       ServerSocket serverSocket = null; //��������  0610
     
       ///
       InputStream is = null;
 	  InputStreamReader isr = null;
 	 
 	 BufferedReader in = null;
	
       ///
       
       
       System.out.println("����������������������������������������������������������������������������������������������"); 
       System.out.println("��               ���� ������ �����Ǿ����ϴ�                                          ��");        
       System.out.println("��               Ŭ���̾�Ʈ ���� ������Դϴ�                                       ��");  
       System.out.println("����������������������������������������������������������������������������������������������"); 
  
       
      try {
         
    	 
    	 serverSocket = new ServerSocket(PORT); // �������� ����
    	 Hashtable<String, String> ht = new Hashtable<String, String>(); //�ؽ����̺� ����
    
    	 while(true){ //���α׷� ������� ����
    		 //Ŭ����� ���Ӵ��
             System.out.println("�� Ŭ���̾�Ʈ ���� �����."); 
             clientSocket = serverSocket.accept(); //Ŭ���̾�Ʈ�κ��� �����Ͱ� ���°��� ����c
                                                                                                       //�����Ѵٴ� ��.
             
             InetAddress ia = clientSocket.getInetAddress();
             String ip = ia.getHostAddress(); // ���ӵ� ���� Client IP 
             System.out.println("�� Ŭ���̾�Ʈ�� ����Ǿ����ϴ�.");
             
             
             int port = clientSocket.getLocalPort();// ���ӿ� ���� ������ PORT 
             int client_port = clientSocket.getPort(); // ���� Client PORT 0610��������

             System.out.println("�� Ŭ���̾�Ʈ�� ����Ǿ����ϴ�."); 
             System.out.println("�� Server Log    Local Port : " + port); //���ӿ� ���� ������Ʈ 0610
             System.out.print("�� Client Log    Client Port : "+ client_port); //Ŭ���̾�Ʈ �α�, ���� ip, port
             System.out.println(" IP : " + ip);
          
            
             
             //�����±ױ���
             //�������� ���ϵ����� �����ؼ� ������ ���ڻ���
             //���� �浵 �±� ������ �� Ŭ�������� ����
             //����Ұ��� �� ������
             //������ ���ο� 
             in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             
             String data = in.readLine(); 
             	//�ѹ� ������� ���⼭ �о��
            	
             if( data.contains(",")){
            	 //���ø� ������. 0��°�� �±� ���⼭ ,�޸��������� �ڽĽ�����
            	 // ,�������� �θ𽺷���    
            	 ChildThread childThread = new ChildThread(clientSocket, ht);
            	 childThread.start();
            	 //CliThread cliThread = new CliThread(clientSocket);
                 //cliThread.start();
             }else{
            	 ParentsThread parentThread = new ParentsThread(clientSocket, ht);
            	 parentThread.start();
             }
             
          
    	 }//while�� 
         
      }finally{
   	   if (clientSocket != null)
		   clientSocket.close();
		   if (serverSocket != null)
			   serverSocket.close();
		   System.out.println("**���� ����**");   
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

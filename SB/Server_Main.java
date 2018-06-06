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
   private static final int PORT = 9049; //��Ʈ ����
   private static final int THREAD_CNT = 30; //������ Ǯ �ִ� ������ ���� ����. ������ Ǯ�� �̸� �����س��� ������� �۾� �Ҵ�
   private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_CNT);
   
   static String inputLine = "37.056203,127.06970060000003"; // ��ǥ�ּ� �� ��ź��
   static String datasend = null;
   //�߰��κ�
   private static BufferedReader mIn;    // ������ ���
   private static PrintWriter mOut;  // ������ ���
   //�߰��κ�
  
   public static void main(String[] args) {

      Socket clientSocket = null; // ���������� ������ ��Ʈ�� Ÿ��� ��� ip�� ������ �� �ִ�.
       //�����κ� PrintWriter out = null;  // String Ÿ���� ���ڸ� ������ �ִ� �Լ�.
       //�����κ� BufferedReader in = null;//stream Ÿ���� ���ڸ� �о ������ �� �ִ� �Լ�.
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

         clientSocket = serverSocket.accept(); //Ŭ���̾�Ʈ�κ��� �����Ͱ� ���°��� ���� 
         String reip = clientSocket.getInetAddress().getHostAddress(); 
         System.out.println("��Ŭ���̾�Ʈ�� ����Ǿ����ϴ�.");  
         System.out.println("��Client Log : " +reip); //Ŭ���̾�Ʈ �ּ� �޾ƿ�         
         
         mIn = new BufferedReader(
                 new InputStreamReader(clientSocket.getInputStream()));

         mOut = new PrintWriter(clientSocket.getOutputStream());
         
         
         
         // Ŭ���̾�Ʈ���� ���� ���ڿ� ���
         while(!(mIn.readLine()==null)){ //���� Ŭ�󿡰Լ� ���� ���ڿ��� null�϶����� ����
        	 String[] Sending = mIn.readLine().split("/");  // Sending�迭 ����. �̰� �θ� Ŭ�󿡰� ���� ������, "/"�� �������� ����Ŭ�󿡰Լ� ���� Ŭ�� ���ø���
        	 String Tag = Sending[0]; // ���ø��� �κ��� �պκ��� tag��
        	 String Loca = Sending[1]; //���ø��� �κ��� �޺κ��� loca (location)����
        	 
        	 Hashtable<String, String> table = new Hashtable<String, String>(); // Ű������ string, value ������ string ��ü�� ������ �ؽ� ���̺� ����
        	 table.put(Sending[0], Sending[1]); // tag, loca�� Ű, value������ �������
        	 //System.out.println(Tag);
        	 //System.out.println(Loca);
        	 //System.out.println(mIn.readLine());
        	 
        	 Enumeration HT = table.keys(); //Enumeration �������̽��� ��ü���� ����(Vector)���� ������ ��ü�� �Ѽ����� �ϳ��� ó���� �� �ִ� �޼ҵ带 ����
        	 
        	 while(HT.hasMoreElements()){
        		 String key = HT.nextElement().toString();
        		 System.out.println("��Ű �� : " + key + "  ��value �� : " + table.get(key));
        	 }
        	 table.clear(); // hashtable �ʱ�ȭ
      
        	
         }
         
        /*�߰� ����
        // �����͸� ���� �غ�        
         out = clientSocket.getOutputStream(); 
         dos = new DataOutputStream(out); 
         
         // �����͸� ���� �غ� 
         in = clientSocket.getInputStream(); 
         din = new DataInputStream(in);
                      �߰�����*/
         
         //datasend = inputLine;   
         
         
         // ******  data outputstream ����غ��� 
          //out = new PrintWriter(clientSocket.getOutputStream(), true); 
          //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

         //���Ͽ��� �ѿ��� stream ������ ���ڸ� ���� �� �о� ��                                                                                              
           //bufferstream ���·� in �� ����.
                
       
         
         //}
         
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
      
      
 }
   
 
public static String data_send() {
         return datasend;
         }
}


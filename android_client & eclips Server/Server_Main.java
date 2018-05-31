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
	public static void main(String[] args) throws IOException {  // �����Լ�, IOException
	
		ServerSocket serverSocket = null; //�������� ����
		Socket clientSocket = null; // ���������� ������ ��Ʈ�� Ÿ��� ��� ip�� ������ �� �ִ�.
		PrintWriter out = null;  // String Ÿ���� ���ڸ� ������ �ִ� �Լ�.
		BufferedReader in = null;//stream Ÿ���� ���ڸ� �о ������ �� �ִ� �Լ�.
		ArrayList<String> arrayList = new ArrayList<>();
		
		OutputStream os = null;
	    OutputStreamWriter osw =null;
	    BufferedWriter bw = null;
	        
	    InputStream is =null;
	    InputStreamReader isr = null;
	    BufferedReader br = null;

		serverSocket = new ServerSocket(8846);//�������� ����
		
		System.out.println("���� ���� ����");
		
		//ClientGUI clientgui = new ClientGUI();
		 inputLine= "37.056203,127.06970060000003";

		try {
			// ���� ������ ����� ������ ��ٸ���.
			System.out.println("Ŭ���̾�Ʈ ���� ��ٸ�����");
			clientSocket = serverSocket.accept(); //Ŭ���̾�Ʈ�κ��� �����Ͱ� ���°��� �����Ѵ�.
			String reip = clientSocket.getInetAddress().getHostAddress();
			 System.out.println("Log : " +reip);
			System.out.println("Ŭ���̾�Ʈ ����");

			// Ŭ���̾�Ʈ�� ���� �����͸� �޴´�.
		    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //���Ͽ��� �ѿ��� stream ������ ���ڸ� ���� �� �о� ��                                                                                            
		       //bufferstream ���·� in �� ����.
			out = new PrintWriter(clientSocket.getOutputStream(), true); //String Ÿ���� stream ���·� ��ȯ�Ͽ� 
			os =clientSocket.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			is = clientSocket.getInputStream();
	        isr = new InputStreamReader(is);
	        br = new BufferedReader(isr);        // �����κ��� Data�� ����
	        
	        while(true) {
	        	bw.write(inputLine);
				bw.newLine();
	            bw.flush();          
	            
	            String receiveData = "";
	            receiveData = br.readLine();        // �����κ��� ������ ���� ����
	            System.out.println("�����κ��� ���� ������ : " + receiveData);
		   
	        }      
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
            	System.out.println("���� ���� ");
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

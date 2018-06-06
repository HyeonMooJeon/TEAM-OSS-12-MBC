package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class Thread implements Runnable{

	private Socket socket = null;
	private static BufferedReader mIn;    // ������ ���
	private static PrintWriter mOut;  // ������ ���
	
	public Thread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {
			// ������ ���� ��Ʈ���� ����
			OutputStream stream = socket.getOutputStream();
			//InputStream Istream = clientSocket.getInputStream();//���� 0607
						
			 mIn = new BufferedReader( // Ŭ�󿡼� ������ �о���� 
	                 new InputStreamReader(socket.getInputStream()));

	         mOut = new PrintWriter(socket.getOutputStream());
	          
	         
	         while(!(mIn.readLine()==null)) 
	         { //���� Ŭ�󿡰Լ� ���� ���ڿ��� null�϶����� ����
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
	        		 System.out.println("�� key �� : " + key + "  �� value �� : " + table.get(key));
	        		 
	        		 mOut.write(key + table.get(key)); // key, value ������ Ŭ���̾�Ʈ�� ����.
	        	 }
	        	 table.clear(); // hashtable �ʱ�ȭ
	      	}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close(); // ���� ����.
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
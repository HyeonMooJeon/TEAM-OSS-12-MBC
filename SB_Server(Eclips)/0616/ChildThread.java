package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class ChildThread extends Thread{
	private Socket socket = null;
	public Hashtable<String, String> ht;
	  private static BufferedReader mIn;
	  private static PrintWriter mOut;
	  static String inputLine = null;
	  static String datasend = null;
	
	  static String Tag = null;
	  static String Loca = "36.799210, 127.074920"; // ����Ŭ��ó���� null�� ������ �������� 
	  static String ParentTag = null;
	  static String[] Sending;
	  static String key = null;

	  OutputStream os = null;
	  OutputStreamWriter osw = null;
	  BufferedWriter bw = null;
	
	  InputStream is = null;
	  InputStreamReader isr = null;
	  BufferedReader br = null;
	
	  Socket clientSocket = null;
	  PrintWriter out = null;
	  BufferedReader in = null;
	  
	 public ChildThread(Socket socket, Hashtable <String, String> ht) {
		    this.socket = socket;
		    this.ht = ht;
		  }
	 public void sleep(int time)
	  {
	    try {
	      Thread.sleep(time); // �극��ũ�� �ɾ��ֱ�����. �ٸ� �������� �۾��� ó���ϱ����� ��� 
	    }
	    catch (InterruptedException localInterruptedException)
	    {
	    }
	  }
	
	 
	  public void run() //synchronized �� ����ȭ 
	  {
	    try
	    {
	      OutputStream stream = socket.getOutputStream();
	
	      mIn = new BufferedReader(
	        new InputStreamReader(socket.getInputStream()));
	
	      mOut = new PrintWriter(socket.getOutputStream());
	      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	
	     out = new PrintWriter(socket.getOutputStream(), true);
	 
	      
	      System.out.println("���ϵ� ���Ϲ� ��");

		      while (true) 
		     {
			      ParentTag = mIn.readLine();
			        	
					          Sending = this.ParentTag.split(","); // �±׸�,����/�浵 ���·� ������ �����͸�  ','�� �������� ���� �����͸� ����
					          Tag = Sending[0];  // �±׸�  sending[0]��
					          Loca = Sending[1];  // ��ġ������ sending[1]�� ����
					          
					          ht.put(Sending[0], Sending[1]); //�ؽ����̺� �±� , ��ġ���� ����
					
					          
					          Enumeration HT = ht.keys(); //������ ��ü�� �ϳ��� ó�� 
					
					          System.out.println("while�� -  more element ");
					          key = HT.nextElement().toString();
					
					          System.out.println("�� key �� : " + key + "  �� value �� : " + ht.get(key)); //Ű���� �������� ���
					          
					      	this.sleep(10);
					       
		     }//while
	   
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	    finally
	    {
	      try
	      { 
	        socket.close();
	        System.out.println("�� Ŭ���̾�Ʈ ������ ����Ǿ����ϴ�.");
	      }
	      catch (IOException e)
	      {
	        e.printStackTrace();
	      }
	    }
	  }	 
}

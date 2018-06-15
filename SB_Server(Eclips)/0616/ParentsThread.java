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

public class ParentsThread extends Thread{
	private Socket socket = null;
	public Hashtable<String, String> ht;

	  private static BufferedReader mIn;
	  private static PrintWriter mOut;
	  static String inputLine = null;
	  static String datasend = null;
	
	  static String Tag = null;
	  static String Loca = null; // ����Ŭ��ó���� null�� ������ �������� 
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
	  
	
	 public ParentsThread(Socket socket, Hashtable <String, String> ht) {
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
	    //  os = this.socket.getOutputStream();
	   //   osw = new OutputStreamWriter(os);
	    //  bw = new BufferedWriter(osw);
	      is = socket.getInputStream();
	
	      isr = new InputStreamReader(is);
	      br = new BufferedReader(isr);
	     
	      
	      System.out.println("��Ʈ ���Ϲ� ��");

	     
		      while (true) 
		     {
		    	  this.sleep(1000);
				System.out.println("�������� ���� ������ : " + Loca);
				os = socket.getOutputStream();
				osw = new OutputStreamWriter(os);
				bw = new BufferedWriter(osw);
				
	            bw = new BufferedWriter(osw);
		
	            Enumeration HT = ht.keys(); //������ ��ü�� �ϳ��� ó�� 
				
		        System.out.println("while��");
		        key = HT.nextElement().toString();
		
		        System.out.println("�ؽ����̺� get key�� : " +	ht.get(key));

		       
				bw.write(ht.get(key));
				bw.newLine();
				bw.flush();

				this.sleep(1000);
		     } //while��
	     
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
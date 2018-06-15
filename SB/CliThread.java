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

public class CliThread extends Thread
{
  private Socket socket = null;
  private static BufferedReader mIn;
  private static PrintWriter mOut;
  static String inputLine = null;
  static String datasend = null;

  static String Tag = null;
  static String Loca = "36.799210, 127.074920"; // ����Ŭ��ó���� null�� ������ �������� 
  String ParentTag = null;
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
  Hashtable<String, String> table = new Hashtable(1); //�ؽ����̺� ����

  public CliThread(Socket socket) {
    this.socket = socket;
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

  public synchronized void run() //synchronized �� ����ȭ 
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
    //  synchronized(this)
    // {
      //ParentTag = mIn.readLine();
      /*if(ParentTag == null)
      {
    	  ParentTag = "re";
      }*/
      synchronized(this)
      {
    	  //while(true)
	      while (true) //�о���ϵ����Ͱ� ���������� ����
	     {
		        System.out.println("while�� ���� �� "+ParentTag);
		        ParentTag = mIn.readLine();
		        System.out.println("�о�°� :" + ParentTag);
		        if(ParentTag != null){
			        if (ParentTag.contains(",")) // ','���ִ� ������ �� ����Ŭ�� ���� ������ ó��
				    {
				          System.out.println("���̺κ� ó�� if��");
				          Sending = ParentTag.split(","); // �±׸�,����/�浵 ���·� ������ �����͸�  ','�� �������� ���� �����͸� ����
				          Tag = Sending[0];  // �±׸�  sending[0]��
				          Loca = Sending[1];  // ��ġ������ sending[1]�� ����
				
				          table.put(Sending[0], Sending[1]); //�ؽ����̺� �±� , ��ġ���� ����
				
				          
				          Enumeration HT = table.keys(); //������ ��ü�� �ϳ��� ó�� 
				
				          System.out.println("while�� -  more element ");
				          key = HT.nextElement().toString();
				
				          System.out.println("�� key �� : " + key + "  �� value �� : " + table.get(key)); //Ű���� �������� ���
				          
				    }
			        else if (ParentTag.equals(key)) //�θ�Ŭ�� �±׸� ������ ����Ŭ��� �±װ� �����ҽ� �۵�
				    {
				          System.out.println("�θ� �κ� ó�� if�� ");
				
				          //while((ParentTag = mIn.readLine()) != null)
				          //while (true) //���ǹ� ��
				          //{
				            System.out.println("�������� ���� ������ : " + Loca);
				          
				            //mOut.write(Loca);
				            bw.write(Loca); //�θ�Ŭ�󿡰� ����
				            bw.newLine();
				            bw.flush();
				            System.out.println("�θ� Ŭ���̾�Ʈ�κ��� ���� ������ : " + ParentTag);
				            this.sleep(1); //�ٸ�������� �ѱ������
				          //
				     }
			        else
			        {
			        	System.out.println("����,�θ� �±װ� ��ġ���� ������ :"+ParentTag+ " \n �����ִ°�: "+Loca);
			        	
			        	os = socket.getOutputStream();
			            osw = new OutputStreamWriter(os);
			            bw = new BufferedWriter(osw);
		
		
				
			          System.out.println("else��. �� ���� �Ȱɸ� ");
			         bw.write(Loca); //�θ�Ŭ�󿡰� ���� ������
			          bw.newLine();
			           bw.flush();
			            this.sleep(1); //�ٸ�������� �ѱ������
		
			        }
			
			        this.sleep(1); //�ٸ�������� �ѱ������
			  }
	     } //while��
      
      }//s
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
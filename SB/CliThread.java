package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
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
  static String Loca = "36.799210, 127.074920";
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
      OutputStream stream = this.socket.getOutputStream();

      mIn = new BufferedReader(
        new InputStreamReader(this.socket.getInputStream()));

      mOut = new PrintWriter(this.socket.getOutputStream());
      in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

     out = new PrintWriter(this.socket.getOutputStream(), true);
      os = this.socket.getOutputStream();
      osw = new OutputStreamWriter(this.os);
      bw = new BufferedWriter(this.osw);
      is = this.socket.getInputStream();

      isr = new InputStreamReader(this.is);
      br = new BufferedReader(this.isr);

      System.out.println("while �� �� ");

      while ((ParentTag = mIn.readLine()) != null) //�о���ϵ����Ͱ� ���������� ���� 
      {
        System.out.println("while �� �� ");
        System.out.println(ParentTag);

        if (ParentTag.contains("/")) // '/'���ִ� ������ �� ����Ŭ�� ���� ������ ó��
        {
          System.out.println("if �� �� ");

          Sending = ParentTag.split("/"); // �±׸�/����,�浵 ���·� ������ �����͸�  '/'�� �������� ���� �����͸� ����
          Tag = Sending[0];  // �±׸�  sending[0]��
          Loca = Sending[1];  // ��ġ������ sending[1]�� ����

          this.table.put(Sending[0], Sending[1]); //�ؽ����̺��� �±� , ��ġ���� ����

          System.out.println("Loca�Դϴ�" + Loca);

          Enumeration HT = this.table.keys(); //������ ��ü�� �ϳ��� ó�� 

          System.out.println("while�� -  more element ");
          key = HT.nextElement().toString();

          System.out.println("�� key �� : " + key + "  �� value �� : " + (String)this.table.get(key)); //Ű���� �������� ���
        }
        else if (ParentTag.equals(key)) //�θ�Ŭ�� �±׸� ������ ����Ŭ��� �±װ� �����ҽ� �۵�
        {
          System.out.println("else if �� ���� ");

          while (true) //���ǹ� ��
          {
            System.out.println("�������� ���� ������ : " + Loca);
            inputLine = Loca;

            bw.write(inputLine); //�θ�Ŭ�󿡰� ����
            bw.newLine();
            bw.flush();
            System.out.println("�θ� Ŭ���̾�Ʈ�κ��� ���� ������ : " + ParentTag);
            this.sleep(1000); //�ٸ�������� �ѱ������
          }
        }
        else
        {
          System.out.println("else��. �� ���� �Ȱɸ�~  ");
        }

        this.sleep(10); //�ٸ�������� �ѱ������
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
        this.socket.close();
        System.out.println("�� Ŭ���̾�Ʈ ������ ����Ǿ����ϴ�.");
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
}
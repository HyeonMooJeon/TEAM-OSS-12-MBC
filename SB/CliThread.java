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
  Hashtable<String, String> table = new Hashtable(1);

  public CliThread(Socket socket) {
    this.socket = socket;
  }

  public void sleep(int time)
  {
    try {
      Thread.sleep(time);
    }
    catch (InterruptedException localInterruptedException)
    {
    }
  }

  public synchronized void run()
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

      System.out.println("while 문 밖 ");

      while ((ParentTag = mIn.readLine()) != null)
      {
        System.out.println("while 문 안 ");
        System.out.println(ParentTag);

        if (ParentTag.contains("/"))
        {
          System.out.println("if 문 안 ");

          Sending = ParentTag.split("/");
          Tag = Sending[0];
          Loca = Sending[1];

          this.table.put(Sending[0], Sending[1]);

          System.out.println("Loca입니다" + Loca);

          Enumeration HT = this.table.keys();

          System.out.println("while문 -  more element ");
          key = HT.nextElement().toString();

          System.out.println("▷ key 값 : " + key + "  ▷ value 값 : " + (String)this.table.get(key));
        }
        else if (ParentTag.equals(key))
        {
          System.out.println("else if 문 들어옴 ");

          while ((CliThread.ParentTag = mIn.readLine()) != null)
          {
            System.out.println("서버에서 보낸 데이터 : " + Loca);
            inputLine = Loca;

            bw.write(inputLine);
            bw.newLine();
            bw.flush();
            System.out.println("부모 클라이언트로부터 받은 데이터 : " + ParentTag);
            this.sleep(10);
          }
        }
        else
        {
          System.out.println("else문. 위 조건 안걸림~  ");
        }

        this.sleep(10);
      } //while문
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
        System.out.println("▶ 클라이언트 연결이 종료되었습니다.");
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
}
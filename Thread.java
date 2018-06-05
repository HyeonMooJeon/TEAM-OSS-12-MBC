package Server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Thread implements Runnable{

	private Socket socket = null;

	public Thread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {
			// ������ ���� ��Ʈ���� ���ɴϴ�.
			OutputStream stream = socket.getOutputStream();
			System.out.println("��Ŭ���̾�Ʈ�� ����Ǿ� �ֽ��ϴ�.");
			//in ���� �޾Ƶ��� �����͸� ������ string ����
            //inputLine = in.readLine(); //in�� ����� �����͸� String ���·� ��ȯ �� �о�� String�� ����
            //arrayList.add(inputLine); //���������� �迭����Ʈ�� ������
            //System.out.println("Ŭ���̾�Ʈ�� ���� ���� GPS ��,�浵��:" + inputLine); //����Ȱ� �ܼ� ���
			
			
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
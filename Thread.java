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
			// 응답을 위해 스트림을 얻어옵니다.
			OutputStream stream = socket.getOutputStream();
			System.out.println("▶클라이언트가 연결되어 있습니다.");
			//in 으로 받아들인 데이터를 저장할 string 생성
            //inputLine = in.readLine(); //in에 저장된 데이터를 String 형태로 변환 후 읽어들어서 String에 저장
            //arrayList.add(inputLine); //지속적으로 배열리스트에 저장함
            //System.out.println("클라이언트로 부터 받은 GPS 위,경도순:" + inputLine); //저장된값 콘솔 출력
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close(); // 소켓 종료.
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
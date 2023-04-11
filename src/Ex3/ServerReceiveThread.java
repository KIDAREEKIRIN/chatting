package Ex3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReceiveThread extends Thread{
    private final Socket socket;

    private String userId;

    public ServerReceiveThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String readData = "";

            while(true) {
                readData = br.readLine();
                if (readData == null) {
                    System.out.println("클라이언트가 접속을 종료했습니다.");
                    break;
                }
                System.out.println("클라이언트가 보낸 메시지: " + readData);
            }
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

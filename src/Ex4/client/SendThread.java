package Ex4.client;

import java.io.*;
import java.net.Socket;

public class SendThread extends Thread{

    private Socket m_Socket;

    @Override
    public void run() {
        super.run();

        try {
            BufferedReader tmpbuf = new BufferedReader(new InputStreamReader(System.in));

            PrintWriter sendWriter = new PrintWriter(m_Socket.getOutputStream());

            String sendString;

            System.out.println("사용할 ID를 입력해주세요 : ");
            ChatClient.UserID = tmpbuf.readLine();

            sendWriter.println(ChatClient.UserID);
            sendWriter.flush();

            while (true) {

                sendString = tmpbuf.readLine();

                if(sendString.equals("exit")) {
                    break;
                }

                sendWriter.println(sendString);
                sendWriter.flush();
            }

            sendWriter.close();
            tmpbuf.close();
            m_Socket.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
                // 특정 유저에게만 메시지를 뿌리기.
        // 특정 유저를 구별하는 방법은..?
        // 공간을 만들고, 공간의 고유값을 통해 채팅하기 -> 방 나누기
        // 방의 key 값을 통해 메세지를 구분지어서 보낼 숭 ㅣㅆ음.
         // 고유값을 가진 리스트를 통해 메세지를 나눈다.
    }

    public void setSocket(Socket _socket) {
        m_Socket = _socket;
    }
}

package Ex4.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientManagerThread extends Thread{

    private Socket m_socket; // 소켓.
    private String m_ID; // ID.

    HashMap<String, Object> hash; // 채팅방.
    boolean initFlag = false; // 방이름을 받았는지 확인하는 플래그.
    String text; // 채팅 내용.
    String roomName; // 방이름.

    @Override
    public void run() {
        super.run();

        // 채팅방을 나눈다. Room. // HashMap을 사용해야한다.
        try {
            // 클라이언트가 보낸 내용을 읽는다.
            BufferedReader tmpbuffer = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));

            String text = null; // 클라이언트가 보낸 내용을 저장할 변수.

            // 클라이언트가 보낸 내용을 읽는다.
            while(true) {
                text = tmpbuffer.readLine();

                // 읽는 내용이 없다면. -> 클라이언트가 나갔다면.
                if(text == null) {
                    System.out.println(m_ID + "이(가) 나갔습니다."); // 콘솔에 출력.
                    // 모든 클라이언트에게 메세지를 보낸다.
                    for (int i = 0; i < ChatServer.m_OutputList.size(); i++) {
                        ChatServer.m_OutputList.get(i).println(m_ID + " > " + "이(가) 나갔습니다.");
                        ChatServer.m_OutputList.get(i).flush();
                    }
                    break;
                }

                String[] split = text.split("highkrs12345"); // 방이름을 받는다.
                if(split.length == 2 && split[0].equals("ID")) { // 방이름을 받았다면.
                    m_ID = split[1]; // ID를 저장한다. // 방이름을 저장한다.
                    System.out.println(m_ID + "이(가) 입장하였습니다.");
                    // 모든 클라이언트에게 메시지를 보낸다.
                    for (int i = 0; i < ChatServer.m_OutputList.size(); i++) {
                        ChatServer.m_OutputList.get(i).println(m_ID + "이(가) 입장하였습니다.");
                        ChatServer.m_OutputList.get(i).flush();
                    }
                    continue; // 다시 while문으로 돌아간다.
                }

                // 모든 클라이언트에게 메세지를 보낸다.
                for (int i = 0; i < ChatServer.m_OutputList.size(); i++) {
                    ChatServer.m_OutputList.get(i).println(m_ID + " > " + text); // ID와 채팅내용을 보낸다.
                    ChatServer.m_OutputList.get(i).flush(); // 버퍼를 비운다.
                }

            }

            // 채팅방 나가면 채팅내역 지우기.
            ChatServer.m_OutputList.remove(new PrintWriter(m_socket.getOutputStream()));
            // 클라이언트와의 연결을 끊는다.
            m_socket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 소켓을 받는닫.
    public void setSocket(Socket _socket) {
        m_socket = _socket;
    }
}

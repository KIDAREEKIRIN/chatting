package Ex4.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatServer {

    public static ArrayList<PrintWriter> m_OutputList; // 채팅 내역.

    public static HashMap<String, Object> hash; // 채팅방.
    private static int PORT = 8888;
    public static void main(String[] args) {
        // 채팅방을 나눈다. Room.
        // HashMap을 사용해야한다.

        m_OutputList = new ArrayList<PrintWriter>(); // 채팅 내역.

        try {

            ServerSocket s_socket = new ServerSocket(PORT); // 서버소켓 포트에 연결.
            hash = new HashMap<String,Object>(); // 채팅방. // 방이름, 방 안에 있는 사람들.
            System.out.println("클라이언트 연결 대기중.");

            // 서버가 계속 돌아가야한다.
            while (true) {
                System.out.println("현재 서버에 " + hash.size() + "명 ..");

                Socket c_socket = s_socket.accept(); // 클라이언트 소켓 연결.
                System.out.println("클라이언트 연결 완료. 채팅을 시작하세요."); // 클라이언트 연결 완료.
                // 서버 thread.
                ClientManagerThread c_thread = new ClientManagerThread(); // 쓰레드 생성.
                c_thread.setSocket(c_socket); // 소켓을 넘겨준다.
                m_OutputList.add(new PrintWriter(c_socket.getOutputStream())); // 채팅 내역을 넘겨준다.
                c_thread.start(); // 쓰레드 시작.
            }

        } catch (IOException e) {
            e.printStackTrace(); // 에러 출력.
        }

    }
}

package Ex4.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    public static ArrayList<PrintWriter> m_OutputList; // 채팅 내역.
    private static int PORT = 8888;
    public static void main(String[] args) {

        m_OutputList = new ArrayList<PrintWriter>();

        try {

            ServerSocket s_socket = new ServerSocket(PORT); // 서버소켓 포트에 연결.
            System.out.println("클라이언트 연결 대기중.");

            while (true) {
                Socket c_socket = s_socket.accept();
                System.out.println("클라이언트 연결 완료. 채팅을 시작하세요.");
                // 서버 thread.
                ClientManagerThread c_thread = new ClientManagerThread();
                c_thread.setSocket(c_socket); //

                m_OutputList.add(new PrintWriter(c_socket.getOutputStream()));
                c_thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

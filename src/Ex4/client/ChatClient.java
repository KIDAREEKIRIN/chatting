package Ex4.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
    public static String UserID;
    private static String IP = "127.0.0.1"; // localhost.
    private static int PORT = 8888;

    public static void main(String[] args) {
        try {
            //소켓에 IP 포트 연결하기.
            Socket c_socket = new Socket(IP, PORT);
            System.out.println("현재 주소 getInetAddress() : " + c_socket.getInetAddress());
            System.out.println("현재 주소 getLocalAddress() : " + c_socket.getLocalAddress());
            System.out.println("현재 포트 : " + c_socket.getLocalPort());
//            c_socket.getLocalPort();

            // 수신 Thread.
            ReceiveThread rec_thread = new ReceiveThread();
            rec_thread.setSocket(c_socket);

            // 송신 Thread.
            SendThread send_thread = new SendThread();
            send_thread.setSocket(c_socket);

            // thread 시작.
            send_thread.start();
            rec_thread.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

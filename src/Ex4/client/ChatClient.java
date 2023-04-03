package Ex4.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
    public static String UserID;
    private static String IP = "172.30.1.48";
    private static int PORT = 8888;

    public static void main(String[] args) {
        try {

            Socket c_socket = new Socket(IP, PORT);

            ReceiveThread rec_thread = new ReceiveThread();
            rec_thread.setSocket(c_socket);

            SendThread send_thread = new SendThread();
            send_thread.setSocket(c_socket);

            send_thread.start();
            rec_thread.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

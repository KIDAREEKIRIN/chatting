package Ex3.Client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private static String IP = "127.0.0.1";
    private static int PORT = 8888;
    public static void main(String[] args) {



        try {
            Socket socket = new Socket(IP, PORT);

            System.out.println("서버에 연결되었습니다.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
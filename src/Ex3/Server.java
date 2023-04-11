package Ex3;

import Ex4.client.ReceiveThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static ServerSocket serverSocket;
    public static Socket socket;
    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(8888);
            System.out.println("서버가 시작되었습니다.");

            while (true) {
                socket = serverSocket.accept();
                System.out.println("클라이언트가 접속했습니다.");

                ServerReceiveThread serverReceiveThread = new ServerReceiveThread(socket);
                serverReceiveThread.start();

//                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                PrintWriter pw = new PrintWriter(socket.getOutputStream());
//
//                String readData = "";
//                while((readData = br.readLine()) != null) {
//                    System.out.println("클라이언트가 보낸 메시지: " + readData);
//                    pw.println(readData);
//                    pw.flush();
//                }
//                socket.close();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

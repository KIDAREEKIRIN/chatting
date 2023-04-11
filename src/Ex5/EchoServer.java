package Ex5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static ServerSocket serverSocket;
    public static Socket socket;
    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(8888);
            System.out.println("서버가 시작되었습니다. 클라이언트 연결 대기중.");

            // serverSocket 연결.
            socket = serverSocket.accept(); // 클라이언트가 접속할 때까지 대기.
            System.out.println("클라이언트가 접속했습니다."); // 클라이언트가 접속했다는 메시지를 출력.
            System.out.println("클라이언트 IP: " + socket.getInetAddress().getHostAddress()); // 클라이언트의 IP 주소를 출력.
            System.out.println("클라이언트 Port: " + socket.getPort()); // 클라이언트의 포트 번호를 출력.
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 서버로부터 데이터를 받기 위한 BufferedReader 객체 생성.
            PrintWriter pw = new PrintWriter(socket.getOutputStream()); // 서버로 데이터를 보내기 위한 PrintWriter 객체 생성.

            //
            String readData = ""; // 클라이언트가 보낸 메시지를 저장할 변수.
            while((readData = br.readLine()) != null) { // 클라이언트가 보낸 메시지를 읽음.
                System.out.println("클라이언트가 보낸 메시지: " + readData); // 클라이언트가 보낸 메시지를 출력.
                pw.println(readData); // 클라이언트가 보낸 메시지를 그대로 다시 보냄.
                pw.flush(); // 버퍼를 비움.
            }
            socket.close(); // 클라이언트와의 연결을 끊음.
            serverSocket.close(); // 서버 소켓을 닫음.

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

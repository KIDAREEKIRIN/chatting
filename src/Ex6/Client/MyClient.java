package Ex6.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {
    private String serverAddress; // 서버의 IP 주소
    private int serverPort; // 서버의 포트 번호
    private String nickname; // 클라이언트의 닉네임
    private Socket socket; // 서버와 통신하기 위한 Socket
    private BufferedReader input; // 서버로부터 데이터를 받기 위한 BufferedReader
    private PrintWriter output; // 서버로 데이터를 전송하기 위한 PrintWriter

    // 생성자
    public MyClient(String serverAddress, int serverPort, String nickname) {
        this.serverAddress = serverAddress; // 서버의 IP 주소 설정
        this.serverPort = serverPort; // 서버의 포트 번호 설정
        this.nickname = nickname; // 클라이언트의 닉네임 설정
    }

    public void run() throws IOException { //
        socket = new Socket("172.30.1.3", 8888); // 서버에 접속
        input = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 서버로부터 데이터를 받기 위한 BufferedReader 생성
        output = new PrintWriter(socket.getOutputStream(), true); // 서버로 데이터를 전송하기 위한 PrintWriter 생성

        // 서버로 닉네임 전송
        output.println(nickname);

        // 메시지 송신 스레드 시작
        Thread sendMessageThread = new Thread(new SendMessageThread());
        sendMessageThread.start();

        // 메시지 수신 스레드 시작
        Thread receiveMessageThread = new Thread(new ReceiveMessageThread());
        receiveMessageThread.start();
    }

    // 메시지 송신 스레드
    private class SendMessageThread implements Runnable {
        @Override
        public void run() {
//            BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); // 키보드 입력을 받기 위한 BufferedReader
            Scanner scanner = new Scanner(System.in); // 키보드 입력을 받기 위한 Scanner
            while (true) {
                // 채팅 메시지 입력
                String messageToSend = scanner.nextLine(); // 키보드 입력을 받음

                // 소켓을 통해 메시지 전송
                output.println(messageToSend);
            }
        }
    }

    // 메시지 수신 스레드
    private class ReceiveMessageThread implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    // 소켓을 통해 메시지 수신
                    String receivedMessage = input.readLine();

                    // 수신된 메시지 출력
                    System.out.println(receivedMessage);
                }
            } catch (IOException e) { // 예외 발생 시
                e.printStackTrace(); // 예외 출력
            }
        }
    }

    // main 메소드
    public static void main(String[] args) throws IOException {
        String serverAddress = "SERVER_ADDRESS"; // 서버의 IP 주소
        int serverPort = 8888; // 서버의 포트 번호
        String nickname = "DEFAULT_NICKNAME"; // 클라이언트의 닉네임

        // 명령행 인자가 3개가 아닌 경우
        MyClient client = new MyClient(serverAddress, serverPort, nickname);
        client.run(); // 클라이언트 실행
    }
}


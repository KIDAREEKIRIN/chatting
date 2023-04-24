package Ex6.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {
    private String serverAddress;
    private int serverPort;
    private String nickname;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public MyClient(String serverAddress, int serverPort, String nickname) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.nickname = nickname;
    }

    public void run() throws IOException {
        socket = new Socket("172.30.1.3", 8888);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);

        // 닉네임 전송
        output.println(nickname);

//        // 이름 입력
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            System.out.println("Enter your name:");
//            String name = scanner.nextLine();
//            if (name.length() > 0) {
//                nickname = name;
//                break;
//            }
//            // 닉네임 전송
//            output.println(nickname);
//        }


        // 메시지 송신 스레드 시작
        Thread sendMessageThread = new Thread(new SendMessageThread());
        sendMessageThread.start();

        // 메시지 수신 스레드 시작
        Thread receiveMessageThread = new Thread(new ReceiveMessageThread());
        receiveMessageThread.start();
    }

    private class SendMessageThread implements Runnable {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                // 채팅 메시지 입력
                String messageToSend = scanner.nextLine();

                // 소켓을 통해 메시지 전송
                output.println(messageToSend);
            }
        }
    }

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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String serverAddress = "SERVER_ADDRESS";
        int serverPort = 8888;
        String nickname = "DEFAULT_NICKNAME";

        MyClient client = new MyClient(serverAddress, serverPort, nickname);
        client.run();
    }
}


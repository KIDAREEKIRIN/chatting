package Ex6.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientManagerThread extends Thread{

    private Socket clientSocket; // 클라이언트와 통신하기 위한 Socket
    private MyServer server; // 서버의 정보를 저장하기 위한 변수
    private PrintWriter out; // 클라이언트에게 데이터를 전송하기 위한 PrintWriter
    private BufferedReader in; // 클라이언트로부터 데이터를 받기 위한 BufferedReader
    private String clientName = ""; // 클라이언트의 이름
    private ChatRoom chatRoom; // 클라이언트가 속한 채팅방
    private String managerName = "asdf"; // 채팅방 관리자의 이름

    // 생성자
    public ClientManagerThread(Socket clientSocket, MyServer server) {
        this.clientSocket = clientSocket; // 클라이언트와 통신하기 위한 Socket
        this.server = server; // 서버의 정보를 저장하기 위한 변수
    }

    // @Override 사용해도 되고, 안해도 됨. -> 사용하는 것이 조금 더 안정적.
    @Override
    public void run(){ // Thread의 run() 메소드를 오버라이딩
        super.run(); // Thread의 run() 메소드를 호출

        try { // 데이터를 주고받는 과정에서 오류가 발생할 수 있으므로 try-catch문 사용
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // 클라이언트로부터 데이터를 받기 위한 InputStream
            out = new PrintWriter(clientSocket.getOutputStream(), true); // 클라이언트로 데이터를 전송하기 위한 PrintWriter

            // 클라이언트의 이름이 비어있거나, 이미 존재하는 이름인 경우
            while(clientName.length() == 0 || server.clients.containsKey(clientName)){
                out.println("Enter your name:"); // 클라이언트에게 이름을 입력하라는 메시지 전송
                clientName = in.readLine().trim(); // 클라이언트로부터 이름을 받아옴
            }
            server.addClient(clientName, out); // 서버에 클라이언트의 이름과 PrintWriter를 저장
            out.println("Name accepted: " + clientName); // 클라이언트에게 이름이 저장되었음을 알림

            // 채팅방 선택
            while(true){ // 채팅방을 선택할 때까지 반복
                out.println("Enter room name:"); // 클라이언트에게 채팅방 이름을 입력하라는 메시지 전송
                String roomName = in.readLine().trim(); // 클라이언트로부터 채팅방 이름을 받아옴
                chatRoom = server.getChatRoom(roomName); // 서버에서 채팅방을 찾아옴
                if(chatRoom != null){ // 채팅방이 존재하는 경우
                    chatRoom.addClient(clientName, out); // 채팅방에 클라이언트 추가
                    out.println("Room joined: " + roomName); // 클라이언트에게 채팅방에 참여했음을 알림
                    break; // 채팅방 선택 반복문 종료
                } else { // 채팅방이 존재하지 않는 경우
                    out.println("Room does not exist."); // 클라이언트에게 채팅방이 존재하지 않음을 알림
                }
            }

            // 클라이언트로부터 메시지 받아서 브로드캐스트
            String message; // 클라이언트로부터 받은 메시지를 저장할 변수
            while((message = in.readLine()) != null){ // 클라이언트로부터 메시지를 받아옴
                chatRoom.broadcast(clientName + ": " + message); // 채팅방에 메시지를 브로드캐스트
            }

        } catch(IOException e){ // 데이터를 주고받는 과정에서 오류가 발생한 경우
            System.out.println("Error handling client " + e.getMessage()); // 오류 메시지 출력
        } finally { // 클라이언트와 통신이 종료되었을 때 실행되는 코드 (try-catch문을 빠져나가기 전에 실행됨)
            try{
                clientSocket.close(); // 클라이언트와 통신을 위한 Socket을 닫음
                server.removeClient(clientName); // 서버에서 클라이언트를 제거
                chatRoom.removeClient(clientName); // 채팅방에서 클라이언트를 제거
                System.out.println(clientName + " has left the room.");
            } catch (IOException e) { // Socket을 닫는 과정에서 오류가 발생한 경우
                System.out.println("Error closing socket " + e.getMessage()); // 오류 메시지 출력
            }
        }
    }
}

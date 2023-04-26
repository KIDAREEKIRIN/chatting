package Ex6.Server;
import Ex6.Server.ClientManagerThread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyServer {

    private ArrayList<ChatRoom> chatRooms; // 채팅방 목록을 저장하는 ArrayList
    public HashMap<String, PrintWriter> clients; // 클라이언트 목록을 저장하는 HashMap
    private ServerSocket serverSocket; // 서버 소켓

    // 생성자
    public MyServer(int port) { // 포트 번호를 매개변수로 받음
        chatRooms = new ArrayList<>(); // 채팅방 목록을 저장하는 ArrayList 생성
        clients = new HashMap<>(); // 클라이언트 목록을 저장하는 HashMap 생성
        try {
            serverSocket = new ServerSocket(port); // 서버 소켓 생성
            System.out.println("Server Start on Port " + port); // 서버 시작 메시지 출력
            while (true) { // 무한 반복
                Socket clientSocket = serverSocket.accept(); // 클라이언트 접속 대기
                System.out.println("New client connected: " + clientSocket); // 클라이언트 접속 메시지 출력

                // 새로운 클라이언트를 위한 쓰레드 생성
                ClientManagerThread handler = new ClientManagerThread(clientSocket, this);
                handler.start(); // 쓰레드 시작
            }
        } catch (IOException e) { // 예외 처리
            System.out.println("Error starting server on port " + port); // 서버 시작 오류 메시지 출력
            e.printStackTrace(); // 예외 출력
        }
    }

    // 채팅방 목록 반환
    public synchronized void addClient(String clientName, PrintWriter out) {
        clients.put(clientName, out); // 클라이언트 목록에 클라이언트 추가
    }

    // 클라이언트 목록에서 클라이언트 삭제
    public synchronized void removeClient(String clientName) {
        clients.remove(clientName); // 클라이언트 목록에서 클라이언트 삭제
    }

    public synchronized void broadcast(String message) {
        for (PrintWriter out : clients.values()) { // 클라이언트 목록에 있는 모든 클라이언트에게 메시지 전송
            out.println(message); // 메시지 전송
        }
    }

    // 채팅방 목록 반환
    public ChatRoom getChatRoom(String roomName) {
        // 채팅방 목록에서 채팅방 이름이 일치하는 채팅방 찾기
        for (ChatRoom room : chatRooms) {
            if (room.getRoomName().equals(roomName)) { // 채팅방 이름이 일치하면
                return room; // 채팅방 반환
            }
        }
        // 채팅방 목록에서 일치하는 채팅방이 없으면
        ChatRoom room = new ChatRoom(roomName); // 채팅방 생성
        chatRooms.add(room); // 채팅방 목록에 채팅방 추가
        return room; // 채팅방 반환
    }

    public static void main(String[] args) {
        // 서버를 생성하고 시작
        MyServer server = new MyServer(8888);

    }
}

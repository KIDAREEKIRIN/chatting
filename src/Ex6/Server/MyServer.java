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
//    public static ArrayList<PrintWriter> m_OutputList; // 연결된 모든 클라이언트에게 데이터를 전송하기 위한 PrintWriter를 저장하는 ArrayList


    public MyServer(int port) {
        chatRooms = new ArrayList<>();
        clients = new HashMap<>();
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server Start on Port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // 새로운 클라이언트를 위한 쓰레드 생성
                ClientManagerThread handler = new ClientManagerThread(clientSocket, this);
                handler.start();
            }
        } catch (IOException e) {
            System.out.println("Error starting server on port " + port);
            e.printStackTrace();
        }
    }

    public synchronized void addClient(String clientName, PrintWriter out) {
        clients.put(clientName, out);
    }

    public synchronized void removeClient(String clientName) {
        clients.remove(clientName);
    }

    public synchronized void broadcast(String message) {
        for (PrintWriter out : clients.values()) {
            out.println(message);
        }
    }

    public ChatRoom getChatRoom(String roomName) {
        for (ChatRoom room : chatRooms) {
            if (room.getRoomName().equals(roomName)) {
                return room;
            }
        }
        ChatRoom room = new ChatRoom(roomName);
        chatRooms.add(room);
        return room;
    }

    public static void main(String[] args) {

        // 서버를 생성하고 시작
        MyServer server = new MyServer(8888);

    }
}

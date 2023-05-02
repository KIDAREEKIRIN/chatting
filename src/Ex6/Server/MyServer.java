package Ex6.Server;
import Ex6.Server.ClientManagerThread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
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

    // 클라이언트 목록에 있는 모든 클라이언트에게 메시지 전송 -> 전체 메시지 전송.
    public synchronized void broadcast(String message) {
        for (PrintWriter out : clients.values()) {
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

    // clientName 클라이언트에게 메시지 전송
    public void sendToClient(String clientName, String message) {
        clients.get(clientName).println(message);
    }

    // roomName 채팅방에 있는 모든 클라이언트에게 메시지 전송
    public void sendToRoom(String roomName, String message) {
        ChatRoom room = getChatRoom(roomName);
        room.broadcast(message);
    }

    public static void main(String[] args) {

        // JDBC 부분.
        Connection connection = null; //    데이터베이스와 연결을 위한 객체.
        Statement statement = null; //  sql문을 실행하기 위한 객체.
        ResultSet resultSet = null;

        try {
            String url = "jdbc:mysql://3.37.249.79:3306/test5"; // test5는 데이터베이스 이름 //
            String user = "test"; // user 이름.
            String password = "test"; // 비밀번호.

//            String sql = "SELECT * FROM default_duty_name"; // 실행할 sql문.

            Class.forName("com.mysql.cj.jdbc.Driver"); // 드라이버 로딩
            // 드라이버 버전 확인
//            System.out.println("JDBC Driver Version: " + DriverManager.getDriver("jdbc:mysql://3.37.249.79:3306/test5").getMajorVersion() + "." + DriverManager.getDriver("jdbc:mysql://3.37.249.79:3306/test5").getMinorVersion());
            System.out.println("url" + url);
            System.out.println("user" + user);
            System.out.println("password" + password);
            connection = DriverManager.getConnection(url, user, password); //

            statement = connection.createStatement(); //
            resultSet = statement.executeQuery("SELECT * FROM default_duty_name");

            while(resultSet.next()) { //
//                int id = resultSet.getInt("duty_id");
                String name = resultSet.getString("duty_name");
//                int age = resultSet.getInt("age");
                System.out.println(name);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(resultSet != null)
                    resultSet.close();
                if(statement != null)
                    statement.close();
                if(connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // 서버를 생성하고 시작
        MyServer server = new MyServer(8888);

    }
}

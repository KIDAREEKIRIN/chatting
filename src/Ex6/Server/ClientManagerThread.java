package Ex6.Server;

import com.mysql.cj.jdbc.ConnectionImpl;
import com.mysql.cj.xdevapi.JsonArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ClientManagerThread extends Thread {

    private final Socket clientSocket; // 클라이언트와 통신하기 위한 Socket
    private final MyServer server; // 서버의 정보를 저장하기 위한 변수
    private PrintWriter out; // 클라이언트에게 데이터를 전송하기 위한 PrintWriter
    private BufferedReader in; // 클라이언트로부터 데이터를 받기 위한 BufferedReader
    private String clientName = ""; // 클라이언트의 이름
    private ChatRoom chatRoom; // 클라이언트가 속한 채팅방
    private String managerName = "asdf"; // 채팅방 관리자의 이름

    Connection connection;

    // 생성자
    public ClientManagerThread(Socket clientSocket, MyServer server) {
        this.clientSocket = clientSocket; // 클라이언트와 통신하기 위한 Socket
        this.server = server; // 서버의 정보를 저장하기 위한 변수
    }

    // @Override 사용해도 되고, 안해도 됨. -> 사용하는 것이 조금 더 안정적.
    @Override
    public void run() { // Thread의 run() 메소드를 오버라이딩
        super.run(); // Thread의 run() 메소드를 호출
        // 데이터를 주고받는 과정에서 오류가 발생할 수 있으므로 try-catch문 사용
        try {
            // 클라이언트로부터 데이터를 받기 위한 InputStream
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // 클라이언트로 데이터를 전송하기 위한 PrintWriter
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // 해당 채팅방의 이름으로 불러오는 JDBC 채팅방 목록 코드 생성은 어떨까?
            selectRoom(clientName);

            // 클라이언트의 이름이 비어있거나, 이미 존재하는 이름인 경우
            while (clientName.length() == 0 || server.clients.containsKey(clientName)) { // 클라이언트의 이름이 비어있거나, 이미 존재하는 이름인 경우
                // 클라이언트에게 이름을 입력하라는 메시지 전송 (println() 메소드를 사용하여 개행)
//                out.println("Enter your name:");
                // 클라이언트로부터 이름을 받아옴 (trim() 메소드를 사용하여 공백 제거)
                clientName = in.readLine().trim();
            }
            // 서버에 클라이언트의 이름과 PrintWriter를 저장 (추가)
            server.addClient(clientName, out);
            // 클라이언트에게 이름이 저장되었음을 알림 (println() 메소드를 사용하여 개행)
            out.println("Name accepted: " + clientName);
            // 서버 콘솔에 클라이언트의 이름이 저장되었음을 알림 (println() 메소드를 사용하여 개행)
            System.out.println("Name accepted: " + clientName);
            // clientName -> 채팅방을 만든 아이디.

            // 채팅방 선택
            while (true) { // 채팅방을 선택할 때까지 반복
                // 채팅방 목록 조회
//                selectRoom(clientName);
                // 클라이언트에게 채팅방 이름을 입력하라는 메시지 전송
//                out.println("Enter room name:");
                // 클라이언트로부터 채팅방 이름을 받아옴
                String roomName = in.readLine().trim();
                // 채팅방이 존재하는지 확인
                chatRoom = server.getChatRoom(roomName);
                // 채팅방의 메시지 읽기 -> 출력.
                readMsg(roomName);
                // 채팅방이 존재하는 경우
                if (chatRoom != null) {
                    // 채팅방에 클라이언트 추가
                    chatRoom.addClient(clientName, out);
                    // 서버에 채팅방 이름 출력. (채팅방이 존재하지 않는 경우에도 출력됨)
                    System.out.println("Room joined: " + roomName);
                    // 채팅방에 있는 클라이언트들의 이름 모두 출력 -> [] 형태로 출력됨.
                    System.out.println(chatRoom.clients.keySet());
                    // 클라이언트에게 채팅방에 입장했음을 알림
                    out.println(clientName + "님, " + roomName + "에 입장하셨습니다.");
                    // 방이름이 존재하면 채팅방 만들면 안됨 -> 서버에서 불러오기.
//                    if(chatRoom.getRoomName() == roomName){
//                        System.out.println("이미 존재하는 방입니다.");
//                        System.out.println(chatRoom.getRoomName()); // 방이름 출력
//                        break;
//                    } else {
//
//                        // 채팅방 선택 반복문 종료
////                        break;
//                    }
//                    // 채팅방 생성
//                    createRoom(roomName, clientName, managerName);
//                    // 채팅방의 메시지 읽기 -> 출력.
//                    readMsg(roomName);
//                    // 채팅방 선택 반복문 종료
                    break;

                } else { // 채팅방이 존재하지 않는 경우
                    out.println("채팅방이 없어요.."); // 클라이언트에게 채팅방이 존재하지 않음을 알림
                    // 채팅방 생성 -> 채팅방이 없으니 채팅방을 생성.
                    createRoom(roomName, clientName, managerName);
                }

            }

            // 클라이언트로부터 메시지 받아서 브로드캐스트
            String message; // 클라이언트로부터 받은 메시지를 저장할 변수.
            // 클라이언트로부터 메시지를 받아오는 과정에서 오류가 발생할 수 있으므로 try-catch문 사용
            try {
                while ((message = in.readLine()) != null) { // 클라이언트로부터 메시지를 받아옴
                    // DB 추가하기
                    // 채팅방에 메시지를 브로드캐스트 -> 클라이언트에게 전송.
                    chatRoom.broadcast(clientName + ": " + message);
                    // 서버에서 클라이언트로부터 받는 메시지 출력.
                    System.out.println(clientName + ": " + message);
                    // DB에 메시지 저장하기. Create.
                    insertMsg(chatRoom.getRoomName(), message, clientName);
                    // 채팅방 이름 출력하기.
//                    System.out.println(chatRoom.getRoomName());
//                    insertMsg(chatRoom.getRoomName(), message, clientName);
                    // 클라이언트에게 메시지를 전송
//                    out.println(clientName + ": " + message);
                }
            } catch (IOException e) { // 데이터를 주고받는 과정에서 오류가 발생할 수 있으므로 try-catch문 사용
                e.printStackTrace(); // 오류 발생 지점을 출력
            }
//            while ((message = in.readLine()) != null) { // 클라이언트로부터 메시지를 받아옴
//                chatRoom.broadcast(clientName + ": " + message); // 채팅방에 메시지를 브로드캐스트 -> 전송.
////                 out.println(clientName + ": " + message); // 클라이언트에게 메시지를 전송
////                chatRoom.broadcastNotMe(clientName + ": " + message, clientName); // 채팅방에 메시지를 브로드캐스트 -> 전송.
////                chatRoom.broadcastNotMe(clientName + ": " + message, clientName);
//                // 서버에 메시지를 출력 // clientName : 보내는 닉네임. message : 보내는 메시지
//                System.out.println(clientName + ": " + message);
//            }

        } catch (IOException e) { // 데이터를 주고받는 과정에서 오류가 발생한 경우
            System.out.println("Error handling client " + e.getMessage()); // 오류 메시지 출력
        } finally { // 클라이언트와 통신이 종료되었을 때 실행되는 코드 (try-catch문을 빠져나가기 전에 실행됨)
            try {
                clientSocket.close(); // 클라이언트와 통신을 위한 Socket을 닫음
                server.removeClient(clientName); // 서버에서 클라이언트를 제거
                chatRoom.removeClient(clientName); // 채팅방에서 클라이언트를 제거
                System.out.println(clientName + " 님이 채팅방을 나갔습니다.");
            } catch (IOException e) { // Socket을 닫는 과정에서 오류가 발생한 경우
                System.out.println("Error closing socket " + e.getMessage()); // 오류 메시지 출력
            }
        }
    }

    // 채팅방 목록 조회.
    public void selectRoom(String clientName) {
        // 채팅방 목록 조회
        try {
            String url = "jdbc:mysql://3.37.249.79:3306/test5"; //
            String user = "test"; //
            String password = "test";

            // SQL문 (채팅방 목록 조회)
//            String clientName;
            String sql = "SELECT * FROM chat_room WHERE from_nick = '" + clientName + "' OR to_nick = '" + clientName + "'";

            Class.forName("com.mysql.cj.jdbc.Driver"); // 드라이버 로딩

            connection = DriverManager.getConnection(url, user, password); // DB 연결
            Statement statement = connection.createStatement(); //
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String roomName = resultSet.getString("room_name");
                String from_nick = resultSet.getString("from_nick");
                String to_nick = resultSet.getString("to_nick");
                String last_sendMsg = resultSet.getString("last_sendMsg");
                String readCount = resultSet.getString("readCount");
                String room_Date = resultSet.getString("room_Date");

                // 서버에서 채팅방 목록을 출력
                System.out.println(roomName + " " + from_nick + " " + to_nick + " " + last_sendMsg + " " + readCount + " " + room_Date);
                // 클라이언트에게 채팅방 목록을 전송
                out.println(roomName + " " + from_nick + " " + to_nick + " " + last_sendMsg + " " + readCount + " " + room_Date);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DB에 채팅방 생성
    public void createRoom(String roomName, String clientName, String managerName) {
        // 채팅방 JDBC에 저장 -> DB 연동.
        try {
            String url = "jdbc:mysql://3.37.249.79:3306/test5"; //
            String user = "test"; //
            String password = "test";

            // SQL문 (채팅방 생성)
            String sql = "INSERT INTO chat_room (room_name, from_nick, to_nick, last_sendMsg, readCount, room_Date ) VALUES (?, ?, ?, ?, ?, ?)";

            Class.forName("com.mysql.cj.jdbc.Driver"); // 드라이버 로딩

            connection = DriverManager.getConnection(url, user, password); // DB 연결

            PreparedStatement statement = connection.prepareStatement(sql); // SQL문 준비
            statement.setString(1, roomName); // roomName -> 방 이름.
            statement.setString(2, clientName); // clientName -> 채팅방을 만든 아이디.
            statement.setString(3, managerName); // managerName -> 채팅방 관리자의 이름
            statement.setString(4, "000"); // "" -> 마지막으로 보낸 메시지
            // "" -> 메시지 읽음 처리 여부
            statement.setInt(5, 2);
            LocalDateTime currentTime = LocalDateTime.now(); // 자바 현재시각 구하는 코드.
            statement.setString(6, currentTime.toString()); // "" -> 채팅방 생성 시각.

            int rowsInserted = statement.executeUpdate(); // SQL문 실행
            if (rowsInserted > 0) { // SQL문 실행 결과가 0보다 큰 경우
                System.out.println("DB에 추가 성공!"); // 성공 메시지 출력
            } else { // SQL문 실행 결과가 0인 경우
                System.out.println("DB에 추가 실패!"); // 실패 메시지 출력
            }

        } catch (ClassNotFoundException e) { // 드라이버 로딩 실패 시
            System.out.println("드라이버 로딩 실패");
//                    e.printStackTrace();
        } catch (SQLException e) { // SQL문 실행 실패 시
            System.out.println("SQL문 에러: " + e);
        } catch (Exception e) { // 그 외의 에러 발생 시
            System.out.println("알 수 없는 에러: " + e);
        }
    }

    // 채팅메시지 DB에 저장
    public void insertMsg(String room_name, String message, String clientName) {
        // 채팅방 JDBC에 저장 -> DB 연동.
        try {
            String url = "jdbc:mysql://3.37.249.79:3306/test5"; //
            String user = "test"; //
            String password = "test";

            // SQL문 (메시지 생성)
            String insertMsgSql = "INSERT INTO chat_msg (room_name, content, sender_nick, msg_time, msg_read) VALUES (?, ?, ?, ?, ?)";
            // 드라이버 로딩
            Class.forName("com.mysql.cj.jdbc.Driver");
            // DB 연결
            connection = DriverManager.getConnection(url, user, password);
            // SQL문 준비
            PreparedStatement statement = connection.prepareStatement(insertMsgSql);
            // roomName -> 방 이름.
            statement.setString(1, room_name);
            // message -> 메시지 내용
            statement.setString(2, message);
            // clientName -> 메시지 보낸 사람
            statement.setString(3, clientName);
            // "" -> 메시지 보낸 시각
            statement.setString(4, LocalDateTime.now().toString());
            // "" -> 메시지 읽음 처리 여부 -> 둘 다 있으면 : 0 / 관리자가 있으면 : 1 / 유저가 있으면 : 2
            statement.setString(5, "0");

            int rowsInserted = statement.executeUpdate(); // SQL문 실행
            if (rowsInserted > 0) { // SQL문 실행 결과가 0보다 큰 경우
                System.out.println("DB에 채팅방 추가 성공!"); // 성공 메시지 출력
            } else { // SQL문 실행 결과가 0인 경우
                System.out.println("DB에 채팅방 추가 실패!"); // 실패 메시지 출력
            }

        } catch (ClassNotFoundException | SQLException e) { // 드라이버 로딩 실패 시
            System.out.println("드라이버 로딩 실패");
            e.printStackTrace();
        }
    }

    // 불러오고자 하는 채팅방의 채팅메시지 조회
    public void readMsg(String room_name) {
        // 채팅방 JDBC에 저장 -> DB 연동.
        try {
            String url = "jdbc:mysql://3.37.249.79:3306/test5"; //
            String user = "test"; //
            String password = "test";
            // SQL문 (메시지 조회)
            String readMsgSql = "SELECT * FROM chat_msg WHERE room_name = '" + room_name + "' ORDER BY msg_time ASC";
            // 드라이버 로딩
            Class.forName("com.mysql.cj.jdbc.Driver");
            // DB 연결
            connection = DriverManager.getConnection(url, user, password);
            // SQL문 준비
            Statement statement = connection.createStatement(); //
            // SQL문 실행
            ResultSet resultSet = statement.executeQuery(readMsgSql);

            while (resultSet.next()) {
                // 메시지 내용.
                String content = resultSet.getString("content");
                // 메시지 보낸 사람.
                String sender_nick = resultSet.getString("sender_nick");
                // 메시지 보낸 시각.
//                String msg_time = resultSet.getString("msg_time");
                // 서버 콘솔창에 출력하기.
                System.out.println(sender_nick + " : " + content);
                // 클라이언트에 보내기 -> Json 파싱 해야할 듯.
                out.println(sender_nick + " : " + content);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
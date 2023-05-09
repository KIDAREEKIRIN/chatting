package Ex6.Server;

import com.mysql.cj.jdbc.ConnectionImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.util.Scanner;

public class ClientManagerThread extends Thread{

    private final Socket clientSocket; // 클라이언트와 통신하기 위한 Socket
    private final MyServer server; // 서버의 정보를 저장하기 위한 변수
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
            while(clientName.length() == 0 || server.clients.containsKey(clientName)){ // 클라이언트의 이름이 비어있거나, 이미 존재하는 이름인 경우
                out.println("Enter your name:"); // 클라이언트에게 이름을 입력하라는 메시지 전송 (println() 메소드를 사용하여 개행)
                clientName = in.readLine().trim(); // 클라이언트로부터 이름을 받아옴 (trim() 메소드를 사용하여 공백 제거)
            }
            server.addClient(clientName, out); // 서버에 클라이언트의 이름과 PrintWriter를 저장 (추가)
            out.println("Name accepted: " + clientName); // 클라이언트에게 이름이 저장되었음을 알림 (println() 메소드를 사용하여 개행)
            System.out.println("Name accepted: " + clientName); // 서버 콘솔에 클라이언트의 이름이 저장되었음을 알림 (println() 메소드를 사용하여 개행)
            // clientName -> 채팅방을 만든 아이디.

            // 채팅방 선택
            while(true){ // 채팅방을 선택할 때까지 반복
                out.println("Enter room name:"); // 클라이언트에게 채팅방 이름을 입력하라는 메시지 전송
                String roomName = in.readLine().trim(); // 클라이언트로부터 채팅방 이름을 받아옴
                chatRoom = server.getChatRoom(roomName); // 서버에서 채팅방을 찾아옴
                    chatRoom.addClient(managerName, out); // 채팅방에 관리자 asdf 추가
                if(chatRoom != null){ // 채팅방이 존재하는 경우
                    chatRoom.addClient(clientName, out); // 채팅방에 클라이언트 추가
                    out.println("Room joined: " + roomName); // 클라이언트에게 채팅방에 참여했음을 알림
                    // 서버에 채팅방 이름 출력. (채팅방이 존재하지 않는 경우에도 출력됨)
                    System.out.println("Room joined: " + roomName);
                    System.out.println(chatRoom.clients.keySet()); // 채팅방에 있는 클라이언트들의 이름 출력
                    out.println(clientName + "님, " + roomName + "에 입장하셨습니다.");
//                    out.println(managerName + "님, " + roomName + "에 입장하셨습니다.");

                    // 채팅방 JDBC에 저장 -> DB 연동.
                    try {
                        String url = "jdbc:mysql://3.37.249.79:3306/test5"; //
                        String user = "test"; //
                        String password = "test";

                        // SQL문 (채팅방 생성)
                        String sql = "INSERT INTO chat_room (room_name, from_nick, to_nick, last_sendMsg, last_sender_id, last_sender_msg_id) VALUES (?, ?, ?, ?, ?, ?)";

                        Class.forName("com.mysql.cj.jdbc.Driver"); // 드라이버 로딩

                        Connection connection = DriverManager.getConnection(url, user, password); // DB 연결

                        PreparedStatement statement = connection.prepareStatement(sql); // SQL문 준비
                        statement.setString(1, roomName); // roomName -> 방 이름.
                        statement.setString(2, clientName); // clientName -> 채팅방을 만든 아이디.
                        statement.setString(3, managerName); // managerName -> 채팅방 관리자의 이름
                        statement.setString(4, "000"); // "" -> 마지막으로 보낸 메시지
                        statement.setInt(5, 1); // "" -> 마지막으로 보낸 메시지를 보낸 사람의 아이디
                        statement.setInt(6, 2); // "" -> 마지막으로 보낸 메시지의 아이디

                        int rowsInserted = statement.executeUpdate(); // SQL문 실행
                        if (rowsInserted > 0) { // SQL문 실행 결과가 0보다 큰 경우
                            System.out.println("A new employee was inserted successfully!"); // 성공 메시지 출력
                        } else { // SQL문 실행 결과가 0인 경우
                            System.out.println("A new employee was inserted failed!"); // 실패 메시지 출력
                        }

                    } catch (ClassNotFoundException e) { // 드라이버 로딩 실패 시
                        System.out.println("드라이버 로딩 실패");
//                    e.printStackTrace();
                    } catch (SQLException e) { // SQL문 실행 실패 시
                        System.out.println("SQL문 에러: " + e);
                    } catch (Exception e) { // 그 외의 에러 발생 시
                        System.out.println("알 수 없는 에러: " + e);
                    }
                    // roomName -> 방 이름.
                    break; // 채팅방 선택 반복문 종료
                } else { // 채팅방이 존재하지 않는 경우
                    out.println("Room does not exist."); // 클라이언트에게 채팅방이 존재하지 않음을 알림
                }

            }

            // 클라이언트로부터 메시지 받아서 브로드캐스트 -> 채팅방 메시지 입력단계.
           String message; // 클라이언트로부터 받은 메시지를 저장할 변수
            while((message = in.readLine()) != null){ // 클라이언트로부터 메시지를 받아옴
//                chatRoom.broadcast(clientName + ": " + message); // 채팅방에 메시지를 브로드캐스트 -> 전송.
                chatRoom.broadcastNotMe(clientName + ": " + message, clientName);
                // 서버에 메시지를 출력 // clientName : 보내는 닉네임. message : 보내는 메시지
                System.out.println(clientName + ": " + message);
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

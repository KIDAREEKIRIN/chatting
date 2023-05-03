package Ex6.Server;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatRoom {
    private final String roomName; // 채팅방 이름 저장
    final HashMap<String, PrintWriter> clients; // 채팅방에 접속한 클라이언트 목록을 저장하는 HashMap 객체

    public ChatRoom(String roomName) { // 생성자 호출 시 채팅방 이름을 매개변수로 받음
        this.roomName = roomName; // 채팅방 이름 설정 (생성자 호출 시 매개변수로 받은 채팅방 이름으로 설정)
        clients = new HashMap<>(); // 클라이언트 목록을 저장하는 HashMap 생성 (생성자 호출 시 생성)
    }

    // 클라이언트 목록에 클라이언트 추가
    public synchronized void addClient(String clientName, PrintWriter out) {
        clients.put(clientName, out);
    }

    // 클라이언트 목록에서 클라이언트 삭제
    public synchronized void removeClient(String clientName) {
        clients.remove(clientName);
    }


    // 클라이언트 목록에 있는 모든 클라이언트에게 메시지 전송
    public synchronized void broadcast(String message) {
        for (PrintWriter out : clients.values()) { // 클라이언트 목록에 있는 모든 클라이언트에게 메시지 전송
            out.println(message); // 메시지 전송
        }
    }

    // 클라이언트 목록에 있는 모든 클라이언트에게 메시지 전송(나만 제외)
    public synchronized void broadcastNotMe(String message, String sender) {
        for (Map.Entry<String, PrintWriter> entry : clients.entrySet()) {
            if (!entry.getKey().equals(sender)) {
                entry.getValue().println(message);
            }
        }
    }

    // 채팅방 이름 반환
    public String getRoomName() {
        return roomName;
    }
}


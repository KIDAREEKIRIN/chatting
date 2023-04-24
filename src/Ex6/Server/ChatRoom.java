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
    private String roomName;
    private HashMap<String, PrintWriter> clients;

    public ChatRoom(String roomName) {
        this.roomName = roomName;
        clients = new HashMap<>();
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

    public String getRoomName() {
        return roomName;
    }
}


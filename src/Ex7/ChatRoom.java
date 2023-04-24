package Ex7;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatRoom {
    private String roomName;
    private ArrayList<String> clients;
    private HashMap<String, PrintWriter> writers;
    private ArrayList<String> messages;

    public ChatRoom(String roomName) {
        this.roomName = roomName;
        clients = new ArrayList<>();
        writers = new HashMap<>();
        messages = new ArrayList<>();
    }

    public synchronized void addClient(String clientName, PrintWriter writer) {
        clients.add(clientName);
        writers.put(clientName, writer);
        broadcast(clientName + " has joined the chat");
    }

    public synchronized void removeClient(String clientName) {
        clients.remove(clientName);
        writers.remove(clientName);
        broadcast(clientName + " has left the chat");
    }

    public synchronized void broadcast(String message) {
        messages.add(message);
        for (PrintWriter writer : writers.values()) {
            writer.println(message);
        }
    }

    public synchronized void sendMessage(String sender, String receiver, String message) {
        if (clients.contains(sender) && clients.contains(receiver)) {
            PrintWriter writer = writers.get(receiver);
            writer.println(message);
        }
    }


    public String getRoomName() {
        return roomName;
    }

    public ArrayList<String> getClients() {
        return clients;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }
}


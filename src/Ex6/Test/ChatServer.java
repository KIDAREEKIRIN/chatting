package Ex6.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {

    private Map<String, String> users = new HashMap<>();
    private Map<String, PrintWriter> writers = new HashMap<>();

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket);
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            System.out.println("Error in the chat server: " + e.getMessage());
        }
    }

    private void broadcast(String message) {
        for (PrintWriter writer : writers.values()) {
            writer.println(message);
            writer.flush();
        }
    }

    private void addUser(String username, String displayName, PrintWriter writer) {
        users.put(username, displayName);
        writers.put(username, writer);
    }

    private void removeUser(String username) {
        users.remove(username);
        writers.remove(username);
    }

    private class ClientHandler extends Thread {

        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String username;
        private String displayName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);

                writer.println("SUBMITNAME");
                username = reader.readLine();
                displayName = username;

                while (users.containsKey(username)) {
                    writer.println("NAMEALREADYEXISTS");
                    username = reader.readLine();
                }

                writer.println("NAMEACCEPTED " + displayName);
                broadcast("USERNAME " + username + " " + displayName);
                addUser(username, displayName, writer);

                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.startsWith("MESSAGE")) {
                        broadcast("MESSAGE " + username + " " + message.substring(8));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error handling client: " + e.getMessage());
            } finally {
                removeUser(username);
                broadcast("USERLEFT " + username);
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start(8080);
    }
}


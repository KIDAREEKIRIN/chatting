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
    private String clientName = ""; // 클라이언트의 이름
    private ChatRoom chatRoom; // 클라이언트가 속한 채팅방

    // 생성자
    public ClientManagerThread(Socket clientSocket, MyServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    // @Override 사용해도 되고, 안해도 됨.
    @Override
    public void run(){
        super.run();
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // 클라이언트로부터 데이터를 받기 위한 InputStream
            out = new PrintWriter(clientSocket.getOutputStream(), true); // 클라이언트로 데이터를 전송하기 위한 PrintWriter


            // 클라이언트의 이름을 받아옴
            while(clientName.length() == 0 || server.clients.containsKey(clientName)){
                out.println("Enter your name:");
                clientName = in.readLine().trim();
            }
            server.addClient(clientName, out);
            out.println("Name accepted: " + clientName);

            // 채팅방 선택
            while(true){
                out.println("Enter room name:");
                String roomName = in.readLine().trim();
                chatRoom = server.getChatRoom(roomName);
                if(chatRoom != null){
                    chatRoom.addClient(clientName, out);
                    out.println("Room joined: " + roomName);
                    break;
                }
            }

            // 클라이언트로부터 메시지 받아서 브로드캐스트
            String message;
            while((message = in.readLine()) != null){
                chatRoom.broadcast(clientName + ": " + message);
            }


        }catch(IOException e){ // 데이터를 주고받는 과정에서 오류가 발생한 경우
            System.out.println("Error handling client " + e.getMessage());
        } finally {
            try{
                clientSocket.close();
                server.removeClient(clientName);
                chatRoom.removeClient(clientName);
            } catch (IOException e) {
                System.out.println("Error closing socket " + e.getMessage());
            }
        }
    }
}

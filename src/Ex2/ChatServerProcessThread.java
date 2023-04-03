package Ex2;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatServerProcessThread extends Thread{
    private String nickName = null;
    private Socket socket = null;
    List<PrintWriter> listWriters = null;


    public ChatServerProcessThread(Socket socket, List<PrintWriter> listWriters){
        this.socket = socket;
        this.listWriters = listWriters;
    }

    public void run() {
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            while (true) {
                String request = bufferedReader.readLine();

                if(request == null ) {
                    consoleLog("클라이언트로부터 연결 끊김.");
                    doQuit(printWriter);
                    break;
                }

                String[] tokens = request.split(":");
                if("join".equals(tokens[0])) {
                    doJoin(tokens[1], printWriter);
                } else if("message".equals(tokens[0])) {
                    doMessage(tokens[1]);
                } else if ("quit".equals(tokens[0])) {
                    doQuit(printWriter);
                }
            }
        } catch (IOException e) {
            consoleLog(this.nickName + "님이 채팅방을 나갔습니다.");
        }
    }
    private void doQuit(PrintWriter writer) {
        removeWriter(writer);

        String data = this.nickName + "님이 퇴장했습니다.";
        broadcast(data);
    }

    private void removeWriter(PrintWriter writer) {
        synchronized (listWriters) {
            listWriters.remove(writer);
        }
    }

    private void doMessage(String data) {
        broadcast(this.nickName + ":" + data);
    }

    private void doJoin(String nickname, PrintWriter writer) {
        this.nickName = nickname;

        String data = nickname + "님이 입장하였습니다.";
        broadcast(data);

        // writer pool에 저장
        addWriter(writer);
    }

    private void addWriter(PrintWriter writer) {
        synchronized (listWriters) {
            listWriters.add(writer);
        }
    }

    // 서버에 연결된 모든 클라이언트에게 메시지를 전달하기 위한 메서드.
    private void broadcast(String data) {
        synchronized (listWriters) {
            for(PrintWriter writer : listWriters) {
                writer.println(data);
                writer.flush();
            }
        }
    }

    private void consoleLog(String log) {
        System.out.println(log);
    }
}

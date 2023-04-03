package Ex4.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientManagerThread extends Thread{

    private Socket m_socket;
    private String m_ID;

    @Override
    public void run() {
        super.run();

        try {
            BufferedReader tmpbuffer = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));

            String text;

            while(true) {
                text = tmpbuffer.readLine();

                if(text == null) {
                    System.out.println(m_ID + "이(가) 나갔습니다.");
                    for (int i = 0; i < ChatServer.m_OutputList.size(); i++) {
                        ChatServer.m_OutputList.get(i).println(m_ID + " > " + text);
                        ChatServer.m_OutputList.get(i).flush();
                    }
                    continue;
                }
                for (int i = 0; i < ChatServer.m_OutputList.size(); i++) {
                    ChatServer.m_OutputList.get(i).println(m_ID + " > " + text);
                    ChatServer.m_OutputList.get(i).flush();
                }

                ChatServer.m_OutputList.remove(new PrintWriter(m_socket.getOutputStream()));
                m_socket.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setSocket(Socket _socket) {
        m_socket = _socket;
    }
}

package Ex7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientManagerThread extends Thread{

    private Socket m_socket; // 클라이언트와 통신하기 위한 Socket
    private String m_ID; // 클라이언트의 ID

    @Override
    public void run(){
        super.run();
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(m_socket.getInputStream())); // 클라이언트로부터 데이터를 받기 위한 InputStream
            String text; // 클라이언트로부터 받은 데이터를 저장할 변수

            while(true){ // 클라이언트로부터 데이터를 받는 부분
                text = in.readLine(); // 클라이언트로부터 데이터를 읽어옴
                if(text!=null) { // 데이터를 받은 경우
                    for(int i = 0; i< MyServer.m_OutputList.size(); ++i){ // 연결된 모든 클라이언트에게 데이터를 전송하는 부분
                        MyServer.m_OutputList.get(i).println(text); // 데이터를 전송함
                        MyServer.m_OutputList.get(i).flush(); // 데이터를 즉시 전송함
                    }
                }
            }


        }catch(IOException e){ // 데이터를 주고받는 과정에서 오류가 발생한 경우
            e.printStackTrace(); // 에러 메시지를 출력함
        }
    }
    public void setSocket(Socket _socket){ // 소켓을 설정하는 부분
        m_socket = _socket; // 소켓을 설정함
    }
}

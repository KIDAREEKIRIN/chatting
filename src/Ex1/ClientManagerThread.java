package Ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

// Thread 상속받기.
public class ClientManagerThread extends Thread{

    private Socket m_socket;
    private String m_ID;

    // 실행 -> run 메소드.
    @Override
    public void run(){
        super.run();
        // 실행하면.
        try{
            // BufferedReader 인스턴스 화 -> InputStreamReader 의 소켓에 넣은 inputStream을 받아서 파라미터로 받기.
            BufferedReader in = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
            // text -> String 값.
            String text;

            // 만약 위와 같다면,
            while(true){
                // text -> BufferedReader의 값을 한 줄씩 읽기.
                text = in.readLine();
                // null(빈 값)이 아니라면,
                if(text!=null) {
                    // 반복문 돌리기 -> MyServer의 m_OutputList (ArrayList)의 사이즈만큼.
                    // 클라이언트가 입력한 값 받기.
                    System.out.println(text);
                    for(int i=0;i<MyServer.m_OutputList.size();++i){
                        // ArrayList의 갯수를 찍고
                        MyServer.m_OutputList.get(i).println(text);
                        // 그 해당하는 값을 보내기 -> flush();
                        MyServer.m_OutputList.get(i).flush();
                    }
                }
            }
            // 예외 처리 스택 코드 생성.
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    // setSocket의 정의 -> 메소드.
    public void setSocket(Socket _socket){
        m_socket = _socket;
    }
}

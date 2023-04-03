package Ex1;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyServer {
    public static ArrayList<PrintWriter> m_OutputList;

//    private static final int PORT = 8888;

    public static void main(String[] args){
//        ServerSocket serverSocket = null;

        m_OutputList = new ArrayList<PrintWriter>();

        try{

//            serverSocket = new ServerSocket();
            ServerSocket s_socket = new ServerSocket(8888); // ServerSocket 을 포트번호 8888로 생성한다.
            System.out.println(getTime()+"서버 준비 완료...");
            System.out.println(getTime()+"클라이언트 접속 대기중...");
            // 생성하면.
            while(true){
                // socket을 서버소켓에 연결시킨다.
                Socket c_socket = s_socket.accept();
                System.out.println(getTime() + "클라이언트 접속.");
                // 클라이언트 thread 인스턴스화 -> 객체 생성.
                ClientManagerThread c_thread = new ClientManagerThread();
                // c_thread 의 소켓은 Socket의 socket으로 파라미터 받기.
                c_thread.setSocket(c_socket);

                // m_OutputList 의 ArrayList에 추가하기 -> PrintWriter 를 하는데 -> 쓰기를 하는데
                // c_socket.의 (소켓) 출력하는 stream의 값을 받아와서,
                m_OutputList.add(new PrintWriter(c_socket.getOutputStream()));
                // 해당 사이즈만큼 찍어낸다.
                System.out.println(m_OutputList.size()); // 왜 존재하는 지 모르겠음.
//                System.out.println(c_socket.getOutputStream());
                c_thread.start(); // thread 시작하기.
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    // 현재 시간 알아보기.
    static String getTime(){
        SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
        return f.format(new Date());
    }
}

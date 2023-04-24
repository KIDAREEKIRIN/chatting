package Ex7;

import Ex6.Server.ClientManagerThread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyServer {

    public static ArrayList<PrintWriter> m_OutputList;
    public static void main(String[] args) {

        m_OutputList = new ArrayList<PrintWriter>();

        try{
            ServerSocket s_socket = new ServerSocket(8888);
            System.out.println("Server Start on Port " + s_socket.getLocalPort());
            while(true){
                Socket c_socket = s_socket.accept();
                System.out.println("Client Connect");
//                ClientManagerThread c_thread = new ClientManagerThread();
//                c_thread.setSocket(c_socket);

                m_OutputList.add(new PrintWriter(c_socket.getOutputStream()));
                System.out.println(m_OutputList.size());
//                c_thread.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}

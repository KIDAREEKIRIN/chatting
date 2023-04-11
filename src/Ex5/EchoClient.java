package Ex5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8888); // 서버에 접속.
            System.out.println("서버에 접속되었습니다."); // 서버에 접속되었다는 메시지를 출력.

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 서버로부터 데이터를 받기 위한 BufferedReader 객체 생성.
            PrintWriter pw = new PrintWriter(socket.getOutputStream()); // 서버로 데이터를 보내기 위한 PrintWriter 객체 생성.

            Scanner scan = new Scanner(System.in); // 사용자 입력을 받기 위한 Scanner 객체 생성.
            String inputData = ""; // 사용자가 입력한 데이터를 저장할 변수.
            while(!inputData.equals("exit")) { // 사용자가 exit를 입력할 때까지 반복.
                System.out.print("to Server > "); // 사용자에게 입력을 요구하는 메시지 출력.
                inputData = scan.nextLine(); // 사용자가 입력한 데이터를 읽음.
                pw.println(inputData); // 사용자가 입력한 데이터를 서버로 전송.
                pw.flush(); // 버퍼를 비움.
                System.out.println("from Server > " + br.readLine()); // 서버가 보낸 데이터를 읽어서 출력.
            }
            socket.close(); // 클라이언트와의 연결을 끊음.

        } catch (IOException e) {
            e.printStackTrace(); // 예외 발생 시 예외 메시지를 출력.
        }
    }
}

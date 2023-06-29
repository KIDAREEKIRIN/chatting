package Ex8;

import java.lang.module.Configuration;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

public class ChatServer {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(8080);

        final SocketIOServer server = new SocketIOServer(config);

        // 이벤트 리스너 등록
        server.addEventListener("chat-message", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String message, AckRequest ackRequest) {
                // 클라이언트로부터 메시지를 받았을 때 동작할 코드
                System.out.println("Received message: " + message);

                // 클라이언트에게 응답을 보낼 수도 있음
                server.getBroadcastOperations().sendEvent("chat-message", message);
            }
        });

        server.start();

        // 서버 종료 시 리소스 정리
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
        }));

        System.out.println("Socket.IO server started");
    }
}

package Ex8;

import io.socket.server.SocketIoServer;
import io.socket.server.SocketIoServerOptions;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WebSocketServer {

    public static void main(String[] args) {
        int port = 8080; // 사용할 포트 번호 선택

        // Java-WebSocket을 사용하여 WebSocket 서버 생성
        WebSocketServer webSocketServer = new WebSocketServer(new InetSocketAddress(port)) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                // 클라이언트 연결이 열릴 때 동작할 코드
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                // 클라이언트 연결이 닫힐 때 동작할 코드
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                // 클라이언트로부터 메시지를 받았을 때 동작할 코드
            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                // 에러가 발생했을 때 동작할 코드
            }

            @Override
            public void onStart() {
                // 서버가 시작될 때 동작할 코드
            }
        };

        // Socket.IO 서버 생성 및 WebSocket 서버 등록
        SocketIoServerOptions options = new SocketIoServerOptions();
        SocketIoServer socketIoServer = new SocketIoServer(options);
        socketIoServer.addWebSocketServer(webSocketServer);

        // 서버 시작
        socketIoServer.start();
    }
}


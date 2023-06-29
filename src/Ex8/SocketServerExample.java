package Ex8;

import io.socket.server.SocketIoServer;
import io.socket.server.SocketIoServerOptions;
import io.socket.server.SocketIoNamespace;
import io.socket.server.SocketIoServerBuilder;
import io.socket.server.SocketIoSocket;

public class SocketServerExample {

    public static void main(String[] args) {
        // Socket.io 서버 옵션 설정
        SocketIoServerOptions options = new SocketIoServerOptions();
        options.setPingTimeout(60000); // Ping 타임아웃 설정

        // Socket.io 서버 생성
        SocketIoServer server = new SocketIoServerBuilder()
                .setPort(3000) // 포트 설정
                .setOptions(options)
                .build();

        // 네임스페이스 생성
        SocketIoNamespace namespace = server.createNamespace("/chat");

        // 연결 이벤트 처리
        namespace.onConnection(socket -> {
            System.out.println("클라이언트가 연결되었습니다: " + socket.getId());

            // 채팅 메시지 이벤트 처리
            socket.on("chat message", (data, callback) -> {
                System.out.println("받은 메시지: " + data);
                callback.call("메시지가 전달되었습니다.");
                // 클라이언트에게 메시지 전송
                socket.emit("chat message", "서버에서 보낸 메시지");
            });

            // 연결 종료 이벤트 처리
            socket.onDisconnect(event -> {
                System.out.println("클라이언트가 연결을 종료했습니다: " + socket.getId());
            });
        });

        // 서버 시작
        server.start();
        System.out.println("Socket.io 서버가 시작되었습니다.");

        // 서버 종료 시 처리
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            System.out.println("Socket.io 서버가 종료되었습니다.");
        }));
    }
}


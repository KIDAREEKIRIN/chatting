package Ex6.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection conn = null;

        try {
            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // MySQL 데이터베이스 연결
            String url = "jdbc:mysql://3.37.249.79:3306/test5";
            String user = "test";
            String password = "test";
            conn = DriverManager.getConnection(url, user, password);

            // 연결 확인
            if (conn != null) {
                System.out.println("MySQL 데이터베이스에 연결되었습니다.");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("MySQL 데이터베이스에 연결할 수 없습니다.");
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}


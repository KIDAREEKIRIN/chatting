package Ex6.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExample {
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            String url = "jdbc:mysql://3.37.249.79:3306/test5"; // test5는 데이터베이스 이름
            String user = "test";
            String password = "test";
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 드라이버 버전 확인
//            System.out.println("JDBC Driver Version: " + DriverManager.getDriver("jdbc:mysql://3.37.249.79:3306/test5").getMajorVersion() + "." + DriverManager.getDriver("jdbc:mysql://3.37.249.79:3306/test5").getMinorVersion());
            System.out.println("url" + url);
            System.out.println("user" + user);
            System.out.println("password" + password);
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM default_duty_name");

            while(resultSet.next()) { //
//                int id = resultSet.getInt("duty_id");
                String name = resultSet.getString("duty_name");
//                int age = resultSet.getInt("age");
                System.out.println(name);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(resultSet != null)
                    resultSet.close();
                if(statement != null)
                    statement.close();
                if(connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

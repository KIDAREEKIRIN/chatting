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
//        jdbc:mysql://localhost:3306
        String url = "jdbc:mysql://localhost:3306/test5"; // test5는 데이터베이스 이름
        String user = "test";
        String password = "test";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM default_duty_name");

            while(resultSet.next()) {
                int id = resultSet.getInt("duty_id");
                String name = resultSet.getString("duty_name");
//                int age = resultSet.getInt("age");
                System.out.println(id + ", " + name);
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

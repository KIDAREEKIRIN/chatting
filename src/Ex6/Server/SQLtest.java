package Ex6.Server;

public class SQLtest {
//    // JDBC 부분.
//    Connection connection = null; //    데이터베이스와 연결을 위한 객체.
//    Statement statement = null; //  sql문을 실행하기 위한 객체.
//    ResultSet resultSet = null; // sql문을 실행한 결과를 담는 객체.
//
//        try {
//        String url = "jdbc:mysql://3.37.249.79:3306/test5"; // test5는 데이터베이스 이름 //
//        String user = "test"; // user 이름.
//        String password = "test"; // 비밀번호.
//
//        // 실행할 sql문.
////            String sql = "SELECT * FROM duty_name";
//        // 채팅방의 정보를 SELECT 한다.
//        String sql = "SELECT * FROM chat_room";
//
//        Class.forName("com.mysql.cj.jdbc.Driver"); // 드라이버 로딩
//
////            System.out.println("url" + url);
////            System.out.println("user" + user);
////            System.out.println("password" + password);
//        connection = DriverManager.getConnection(url, user, password); //
//
//        statement = connection.createStatement(); //
//        resultSet = statement.executeQuery(sql);
//
//        while(resultSet.next()) {
//            // resultSet.next() : 다음 행으로 이동. 행이 존재하면 true, 없으면 false.
////                Integer id = resultSet.getInt("duty_id"); // duty_code라는 컬럼의 값을 가져옴.
////                String name = resultSet.getString("duty_name"); // duty_name이라는 컬럼의 값을 가져옴.
////                String cate_name = resultSet.getString("cate_name"); // cate_name이라는 컬럼의 값을 가져옴.
//            Integer room_id = resultSet.getInt("room_id"); // room_id라는 컬럼의 값을 가져옴.
////                String room_name = resultSet.getString("room_name"); // room_name이라는 컬럼의 값을 가져옴.
//            String from_nick = resultSet.getString("from_nick"); // from_nick이라는 컬럼의 값을 가져옴.
//            String to_nick = resultSet.getString("to_nick"); // to_nick이라는 컬럼의 값을 가져옴.
//            String last_sendMsg = resultSet.getString("last_sendMsg"); // last_sendMsg이라는 컬럼의 값을 가져옴.
////                String last_sendTime = resultSet.getString("last_sendTime"); // last_sendTime이라는 컬럼의 값을 가져옴.
//            System.out.println(room_id + from_nick + to_nick + last_sendMsg);
////                System.out.println(id + name + cate_name); // 가져온 값을 출력.
//        }
//
//    } catch (ClassNotFoundException e) { // 드라이버 로딩 오류.
//        e.printStackTrace();
//    } catch (SQLException e) {
//        e.printStackTrace();
//    } finally {
//        try {
//            if(resultSet != null)
//                resultSet.close();
//            if(statement != null)
//                statement.close();
//            if(connection != null)
//                connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}

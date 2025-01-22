package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public UserDAO() {
        try {
            String dbURL = "jdbc:mysql://localhost:3306/BBS";
            String dbID = "root";
            String dbPassword = "root";
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dbURL, dbID, dbPassword);
        } catch (Exception e) {
             e.printStackTrace();
        }
    }

    public int login(String userID, String userPassword) {
        String sql = "select userPassword from user where userID = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getString(1).equals(userPassword)) {
                    return 1;  // 로그인 성공
                } else {
                    return 0;  // 비밀번호 불일치
                }
            }

            return -1;  // 아이디 없음
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -2;  // 데이터베이스 오류
    }

    public int join(User user) {
        String sql = "insert into user values(?,?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUserID());
            preparedStatement.setString(2, user.getUserPassword());
            preparedStatement.setString(3, user.getUserName());
            preparedStatement.setString(4, user.getUserGender());
            preparedStatement.setString(5, user.getUserEmail());

            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;  // 데이터베이스 오류
    }
}

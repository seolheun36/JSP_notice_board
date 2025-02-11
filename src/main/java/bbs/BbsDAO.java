package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BbsDAO {

    private Connection connection;
    private ResultSet resultSet;

    public BbsDAO() {
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

    public String getDate() {
        String sql = "select now()";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";  // 데이터베이스 오류
    }

    public int getNext() {
        String sql = "select bbsID from bbs order by bbsID desc";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) + 1;
            }

            return 1;  // 첫 번째 게시물인 경우
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;  // 데이터베이스 오류
    }

    public int write(String bbsTitle, String userID, String bbsContent) {
        String sql = "insert into bbs values (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, getNext());
            preparedStatement.setString(2, bbsTitle);
            preparedStatement.setString(3, userID);
            preparedStatement.setString(4, getDate());
            preparedStatement.setString(5, bbsContent);
            preparedStatement.setInt(6, 1);

            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;  // 데이터베이스 오류
    }

}

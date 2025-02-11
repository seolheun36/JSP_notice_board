package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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

    public ArrayList<Bbs> getList(int pageNumber) {
        String sql = "select * from bbs where bbsID < ? and bbsAvailable = 1 order by bbsID desc limit 10";
        ArrayList<Bbs> list = new ArrayList<Bbs>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, getNext() - (pageNumber - 1) * 10);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Bbs bbs = new Bbs();
                bbs.setBbsID(resultSet.getInt(1));
                bbs.setBbsTitle(resultSet.getString(2));
                bbs.setUserID(resultSet.getString(3));
                bbs.setBbsDate(resultSet.getString(4));
                bbs.setBbsContent(resultSet.getString(5));
                bbs.setBbsAvailable(resultSet.getInt(6));
                list.add(bbs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean nextPage(int pageNumber) {
        String sql = "select * from bbs where bbsID < ? and bbsAvailable = 1";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, getNext() - (pageNumber - 1) * 10);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

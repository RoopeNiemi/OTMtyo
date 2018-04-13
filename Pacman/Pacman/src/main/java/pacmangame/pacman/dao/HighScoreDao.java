/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.dao;

import java.sql.*;

/**
 *
 * @author User
 */
public class HighScoreDao {

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:db/highscore.db");
    }

    public int getHighScore() throws SQLException {
        Connection connection = getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT*FROM HIGHSCORE");
        rs.next();

        int highScore = rs.getInt("score");

        stmt.close();
        rs.close();
        connection.close();
        return highScore;
    }

    public void updateHighScore(int newHighScore) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE HIGHSCORE SET score = ? WHERE scoreid = 1");
        stmt.setInt(1, newHighScore);
        stmt.execute();
        System.out.println("stmt executed");
        stmt.close();
        connection.close();
    }
}

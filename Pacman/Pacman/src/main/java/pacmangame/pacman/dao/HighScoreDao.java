/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.dao;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class HighScoreDao {

    private String databaseName;

    public HighScoreDao(String databaseName) {
        this.databaseName = databaseName;
        createTableIfNecessary();
    }

    private void createTableIfNecessary() {
        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("Create table if not exists highscore(scoreid integer, score integer)");
        } catch (SQLException ex) {
            Logger.getLogger(HighScoreDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + databaseName);
    }

    public int getHighScore() throws SQLException {
        Connection connection = getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT*FROM HIGHSCORE");
        if (!rs.next()) {
            return 0;
        }
        int highScore = rs.getInt("score");

        stmt.close();
        rs.close();
        connection.close();
        return highScore;
    }

    public void updateOrSetHighScore(int newHighScore) throws SQLException {
        Connection connection = getConnection();
        if (getHighScore() == 0) {
            insertToDatabase(connection, newHighScore);
            return;
        }
        PreparedStatement stmt = connection.prepareStatement("UPDATE HIGHSCORE SET score = ? WHERE scoreid = 1");
        stmt.setInt(1, newHighScore);
        stmt.execute();
        System.out.println("stmt executed");
        stmt.close();
        connection.close();
    }

    private void insertToDatabase(Connection connection, int newHighScore) {
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement("INSERT INTO HIGHSCORE(scoreid,score) Values(1,?)");
            stmt.setInt(1, newHighScore);
            stmt.execute();
            stmt.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(HighScoreDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

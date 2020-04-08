package com.alpha.backend.srcCode.DB;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("DBConnector")
public class DBConnector {

    private Connection connection;
    private Statement statement;

    public Statement getStatement() {
        return statement;
    }

    public DBConnector(String placeholder) throws SQLException {
    }

    public DBConnector() throws SQLException {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://165.22.78.137:3306/test", "root", "");
            this.statement = connection.createStatement();

        } catch (Exception e) {
            System.out.println(e.toString());
        }


    }

    public List loadUserFromDatabase(String username, Statement statement) throws SQLException {
        final List resultForReturn = new ArrayList();
        ResultSet resultSet = statement.executeQuery("select * from usertabelle where nutzername='" + username + "';");
        while (resultSet.next()) {
            resultForReturn.add(resultSet.getString("passwort"));
            resultForReturn.add(resultSet.getInt("id"));
            resultForReturn.add(resultSet.getString("email"));
        }
        return resultForReturn;
    }

    public List<List<String>> loadAllTablesFromUser(int userId, Statement statement) throws SQLException {
        final List<List<String>> notesFromUserForReturnment = new ArrayList<>();
        final List<String> singleNoteFromUser = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("select * from dokumententabelle where id='" + userId + "';");
        while (resultSet.next()) {
            singleNoteFromUser.add(0, resultSet.getString("title"));
            singleNoteFromUser.add(1, resultSet.getString("inhalt"));
            singleNoteFromUser.add(2, resultSet.getDate("datum").toString());
            notesFromUserForReturnment.add(singleNoteFromUser);
        }
        return notesFromUserForReturnment;
    }

    public boolean addNewNote(String username, String title, String content, Date date, Statement statement) throws SQLException {
        statement.addBatch("INSERT INTO dokumententabelle (title, datum, inhalt, eigentuemerid) VALUES('" + title + "','" + date + "','" + content + "','" + loadUserFromDatabase(username, statement).get(1) + "')");
        statement.executeBatch();
        if (statement.executeQuery("select title from dokumententabelle where title='" + title + "';").toString() == title) {
            return true;
        } else return false;
    }

    public boolean addNewUser(String username, String password, String email, Statement statement) throws SQLException {

        statement.addBatch("INSERT INTO usertabelle (nutzername, passwort, email) VALUES ('" + username + "','" + password + "','" + email + "')");
        statement.executeBatch();
        if (statement.executeQuery("select nutzername from usertabelle where nutzername='" + username + "';").toString() == username) {
            return true;
        } else return false;
    }

    public void deleteNote(int eintrag_id, Statement statement) throws SQLException {
        statement.addBatch("DELETE * FROM dokumententabelle WHERE eigentuemerid='" + eintrag_id + "';");
        statement.executeBatch();
    }
}

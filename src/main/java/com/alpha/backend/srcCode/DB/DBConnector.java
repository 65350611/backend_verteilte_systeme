package com.alpha.backend.srcCode.DB;
/*
 *********************************************************************************
 * Copyright 2020 JONATHAN SMITH
 *
 * You may not use this file except in compliance
 * with the License. You may obtain a Copy of the License by asking the
 * Copyright Owner JONATHAN SMITH
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *********************************************************************************
 */

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
            this.connection = (Connection) DriverManager.getConnection("jdbc:mysql://165.22.78.137:3306/test?" + "user=root&password=thisProgramWllBeLit123");
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
            singleNoteFromUser.add(2, resultSet.getString("datum"));

//            //
//            Date date = resultSet.getDate("datum");
//            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//            String datumDesEintrags = dateFormat.format(date);
//            singleNoteFromUser.add(2, datumDesEintrags);
//            //
            notesFromUserForReturnment.add(singleNoteFromUser);
        }
        return notesFromUserForReturnment;
    }

    public boolean addNewNote(String username, String title, String content, Date date, Statement statement) throws SQLException {
        statement.addBatch("INSERT INTO dokumententabelle (title, datum, inhalt, eigentuemerid) VALUES('" + title + "','" + date + "','" + content +
                "','" + loadUserFromDatabase(username, statement).get(1) + "')");
        statement.executeBatch();
        if (statement.executeQuery("select title from dokumententabelle where title='" + title + "';").toString() == title) {
            return true;
        } else return false;
    }


    public void deleteNote(int eintrag_id, Statement statement) throws SQLException {
        statement.addBatch("DELETE * FROM dokumententabelle WHERE eigentuemerid='" + eintrag_id + "';");
        statement.executeBatch();
    }


    public boolean addNewUser(String username, String password, String email, Statement statement) throws SQLException{

        statement.addBatch("INSERT INTO usertabelle (nutzername, passwort, email) VALUES ('" + username + "','" + password + "','" + email + "')");
        statement.executeBatch();
        return checkIfNewUserWasCreated(username);
    }

    private boolean checkIfNewUserWasCreated(String username) throws SQLException{
        Statement statement1 = connection.createStatement();
        ResultSet rs = statement1.executeQuery("select nutzername from usertabelle where nutzername='" + username + "';");
        String nutzername = "";
        if (rs.next()) {
            nutzername = rs.getString("nutzername");
        }

        if (nutzername.contentEquals(username)) {
            return true;
        } else return false;
    }
}

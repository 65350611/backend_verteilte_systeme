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

import com.alpha.backend.srcCode.DTOs.Note;
import com.alpha.backend.srcCode.DTOs.NoteId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Connection;
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

    public DBConnector(String placeholder) {
    }

    /**
     * @throws SQLException
     */
    public DBConnector() throws SQLException {
        try {
            this.connection = (Connection) DriverManager.getConnection("jdbc:mysql://165.22.78.137:3306/test?" + "user=root&password=thisProgramWllBeLit123");
            this.statement = connection.createStatement();

        } catch (Exception e) {
            System.out.println(e.toString());
        }


    }

    /**
     * @param username
     * @return
     * @throws SQLException
     */
    public List loadUserFromDatabase(String username) throws SQLException {
        final List resultForReturn = new ArrayList();
        Statement st = connection.createStatement();
        ResultSet resultSet = st.executeQuery("select * from usertabelle where nutzername='" + username + "';");
        while (resultSet.next()) {
            resultForReturn.add(resultSet.getString("passwort"));
            resultForReturn.add(resultSet.getInt("id"));
            resultForReturn.add(resultSet.getString("email"));
        }
        return resultForReturn;
    }

    /**
     * @param userId
     * @return
     * @throws SQLException
     */
    public List<List<String>> loadAllTablesFromUser(int userId) throws SQLException {
        final List<List<String>> notesFromUserForReturnment = new ArrayList<>();
        Statement st = connection.createStatement();
        ResultSet resultSet = st.executeQuery("select * from dokumententabelle where eigentuemerid='" + userId + "';");
        int i = 0;
        while (resultSet.next()) {
            List<String> singleNoteFromUser = new ArrayList<>();

            singleNoteFromUser.add(0, resultSet.getString("title"));
            singleNoteFromUser.add(1, resultSet.getString("id"));
            singleNoteFromUser.add(2, resultSet.getString("datum"));

            notesFromUserForReturnment.add(singleNoteFromUser);


        }
        return notesFromUserForReturnment;
    }

    /**
     * @param elementId
     * @param ownerId
     * @throws SQLException
     */
    public void deleteNote(String elementId, int ownerId) throws SQLException {
        Statement st = connection.createStatement();
        st.addBatch("delete from dokumententabelle where id='" + elementId + "' and eigentuemerid='" + ownerId + "';");
        st.executeBatch();
    }

    /**
     * @param username
     * @param password
     * @param email
     * @return
     * @throws SQLException
     */
    public boolean addNewUser(String username, String password, String email) throws SQLException {
        Statement st = connection.createStatement();
        st.addBatch("INSERT INTO usertabelle (nutzername, passwort, email) VALUES ('" + username + "','" + password + "','" + email + "')");
        st.executeBatch();
        return checkIfNewUserWasCreated(username);
    }

    /**
     * @param username
     * @return
     * @throws SQLException
     */
    private boolean checkIfNewUserWasCreated(String username) throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select nutzername from usertabelle where nutzername='" + username + "';");
        String nutzername = "";
        if (rs.next()) {
            nutzername = rs.getString("nutzername");
        }

        if (nutzername.contentEquals(username)) {
            return true;
        } else return false;
    }

    /**
     * @param username
     * @param title
     * @param content
     * @param date
     * @return
     * @throws SQLException
     */
    public boolean addNewNote(String username, String title, String content, String date) throws SQLException {
        Statement st = connection.createStatement();
        st.addBatch("INSERT INTO dokumententabelle (title, datum, inhalt, eigentuemerid) VALUES('" + title + "','" + date + "','" + content +
                "','" + loadUserFromDatabase(username).get(1) + "')");
        st.executeBatch();
        return checkIfNoteWasSaved(title);
    }

    /**
     * @param title
     * @return
     * @throws SQLException
     */
    private boolean checkIfNoteWasSaved(String title) throws SQLException {
        Statement st = connection.createStatement();

        ResultSet rs = st.executeQuery("select title from dokumententabelle where title='" + title + "';");
        String titel = "";
        if (rs.next()) {
            titel = rs.getString("title");
        }
        if (titel.contentEquals(title)) {
            return true;
        } else return false;
    }

    /**
     * @return
     * @throws SQLException
     */
    public NoteId getIdFromNewlyCreatedNote() throws SQLException {
        Statement st = connection.createStatement();
        Integer noteId = 0;
        ResultSet rs = st.executeQuery("select max(id) from dokumententabelle;");
        if (rs.next()) {
            noteId = rs.getInt("max(id)");
        }
        NoteId id = new NoteId();
        id.setElement_id(noteId);

        return id;
    }

    /**
     * @param userId
     * @param element_id
     * @return
     * @throws SQLException
     */
    public Note getContentFromExistingNote(int userId, String element_id) throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select * from dokumententabelle where eigentuemerid='" + userId + "' and id='" + element_id + "';");
        if (rs.next()) {
            return new Note(rs.getString("datum"), rs.getString("title"), rs.getString("inhalt"), rs.getString("id"));
        }
        return null;
    }

    /**
     * @param userId
     * @param title
     * @param content
     * @param element_id
     * @return
     * @throws SQLException
     */
    public boolean updateExistingResource(int userId, String title, String content, String element_id) throws SQLException {
        Statement st = connection.createStatement();
        st.addBatch("update dokumententabelle set title='" + title + "'," + " inhalt='" + content + "' where id='" + element_id + "';");
        st.executeBatch();
        if (checkIfNoteWasSaved(title)) {
            return true;
        } else return false;

    }
}

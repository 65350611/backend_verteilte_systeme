package com.alpha.backend.srcCode.Controller;
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

import com.alpha.backend.srcCode.B64Decoder;
import com.alpha.backend.srcCode.DB.DBConnector;
import com.alpha.backend.srcCode.DTOs.Note;
import com.alpha.backend.srcCode.DTOs.NoteId;
import com.alpha.backend.srcCode.DTOs.Password;
import com.alpha.backend.srcCode.DTOs.User;
import com.alpha.backend.srcCode.DTOs.UserToken;
import com.alpha.backend.srcCode.UserTokenCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Null;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class Controller {


    @Autowired
    @Qualifier("UserTokenCreator")
    final UserTokenCreator userTokenCreator = new UserTokenCreator();

    @Autowired
    @Qualifier("B64Decoder")
    final B64Decoder b64Decoder = new B64Decoder();

    @Autowired
    @Qualifier("DBConnector")
    DBConnector dbConnector = new DBConnector();

    public Controller() throws SQLException {
    }


    @RequestMapping("/hello")
    public String hello() {
        return "Hello!!!";
    }




    // LOGIN CHECKED
    @RequestMapping(value = "/users/login", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<UserToken> userTokenResponseEntity(@RequestParam String username, @RequestParam String password) throws SQLException {

        if (dbConnector.loadUserFromDatabase(username).get(0).toString().contentEquals(password)) {
            return ResponseEntity.status(200).body(userTokenCreator.createUserToken(username));
        } else return ResponseEntity.status(403).body(null);
    }


    // SEND PASSWORD CHECKED
    @RequestMapping(value = "/users/password", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Password> forgetPassword(@RequestParam String username, @RequestParam String email) throws SQLException {

        if (dbConnector.loadUserFromDatabase(username).get(2).toString().contentEquals(email)) {
            Password password = new Password(dbConnector.loadUserFromDatabase(username).get(0).toString());
            return ResponseEntity.status(200).body(password);
        } else return ResponseEntity.status(403).body(null);

    }

    // Registrieren CHECKED
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Null> registrateResponseEntity(@RequestBody User user) throws SQLException {
        if (dbConnector.addNewUser(user.getUsername(), user.getPassword(), user.getEmail())) {
            return ResponseEntity.status(201).build();
        } else return ResponseEntity.status(422).build();
    }

    // Main Paige Get documents CHECKED
    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<List<Note>> noteResponseEntity(@RequestHeader String user_token) throws SQLException {
        User user = new User(b64Decoder.b64Decoder(user_token));
        user.setUserId((Integer) dbConnector.loadUserFromDatabase(user.getUsername()).get(1));
        List<Note> notes = new ArrayList<>();
        List<List<String>> documents = dbConnector.loadAllTablesFromUser(user.getUserId());
        for (List<String> notesFromTheDatabase : documents) {
            notes.add(new Note(notesFromTheDatabase.get(0), notesFromTheDatabase.get(1), notesFromTheDatabase.get(2)));
        }
        // TODO: 07.04.2020 FALLS DIE FOREACH NICHT FUNKTIONIERT DIE FOR SCHLEIFE VERWENDEN!

//        for (int i = 0; i < documents.size(); i++) {
//            notes.add(new Note(documents.get(i).get(0), documents.get(i).get(1), documents.get(i).get(2)));
//        }
        return ResponseEntity.status(200).body(notes);
    }

    // DELETE Eintrag von User CHECKED
    @RequestMapping(value = "/documents", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Null> deleteResponseEntity(@RequestHeader String user_token, @RequestParam String eintrag_id) throws SQLException {
        User user = new User(b64Decoder.b64Decoder(user_token));
        user.setUserId((Integer) dbConnector.loadUserFromDatabase(user.getUsername()).get(1));
        //Durch diese Abfrage wird sichergestellt, dass der user_token valide ist.
        if (dbConnector.loadUserFromDatabase(user.getUsername()).get(1) != null) {
            dbConnector.deleteNote(eintrag_id, user.getUserId());
        }
        return ResponseEntity.status(204).build();

    }

    // Neuen Eintrag Speichern. CHECKED
    @RequestMapping(value = "/documents", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<NoteId> createNewNote(@RequestHeader String user_token, @RequestBody Note note) throws SQLException {
        User user = new User(b64Decoder.b64Decoder(user_token));
        if (dbConnector.addNewNote(user.getUsername(), note.getTitel(), note.getInhalt(), note.getDatum())) {
            NoteId noteId = dbConnector.getIdFromNewlyCreatedNote();
            return ResponseEntity.status(200).body(noteId);
        } else return ResponseEntity.status(403).build();
    }

    // Aus dem Frontend kommt ein Parameter <elemet_id> über diesen das entsprechende Dokument in der DB überschreiben.
    // Vorhandenen Eintrag überschreiben
    @RequestMapping(value = "/documents", method = RequestMethod.PUT)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Null> patchExistingNote(@RequestHeader String user_token, @RequestBody Note note, @RequestParam String element_id) throws SQLException {
        // TODO: 07.04.2020 NICHT FERTIG UND FUNKTIONIERT NICHT RICHTIG!
        User user = new User(b64Decoder.b64Decoder(user_token));
        user.setUserId((Integer) dbConnector.loadUserFromDatabase(user.getUsername()).get(1));
        if (dbConnector.updateExistingResource(user.getUserId(), note.getTitel(), note.getInhalt(), element_id)) {
            return ResponseEntity.status(204).build();
        } else return ResponseEntity.status(400).build();
    }

    // Inhalt von vorhandenem Eintrag ans Frontend liefern CHECKED
    @RequestMapping(value = "/document", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Note> getContentFromExistingNote(@RequestHeader String user_token, @RequestParam String element_id) throws SQLException {
        User user = new User(b64Decoder.b64Decoder(user_token));
        user.setUserId((Integer) dbConnector.loadUserFromDatabase(user.getUsername()).get(1));
        Note noteFromDatabase = dbConnector.getContentFromExistingNote(user.getUserId(), element_id);
        if (noteFromDatabase != null) {
            return ResponseEntity.status(200).body(noteFromDatabase);
        } else return ResponseEntity.status(404).build();
    }


}

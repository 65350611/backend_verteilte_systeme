package com.alpha.backend.srcCode.Controller;

import com.alpha.backend.srcCode.B64Decoder;
import com.alpha.backend.srcCode.DTOs.Note;
import com.alpha.backend.srcCode.DTOs.Password;
import com.alpha.backend.srcCode.DTOs.User;
import com.alpha.backend.srcCode.DTOs.UserToken;
import com.alpha.backend.srcCode.UserTokenCreator;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class Controller {


    @Autowired
    @Qualifier("UserTokenCreator")
    UserTokenCreator userTokenCreator;

    @Autowired
    @Qualifier("B64Decoder")
    final B64Decoder b64Decoder = new B64Decoder();


    List<User> userList = new ArrayList<>();


    @RequestMapping("/hello")
    public String hello() {
        return "Hello!!!";
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String createUser(@RequestBody User user) {

        userList.add(user);
        return user.toString();
    }

    @RequestMapping("/returnuser")
    public String returnuser() {
        String forReturn = ".";
        forReturn = forReturn + userList.get(0);
        userList.remove(0);
        return forReturn;
    }

//    Hier beginnt der spaßige Teil.
//    gibt standardmäßig ein Json zurück

    // LOGIN
    @RequestMapping(value = "/users/login", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<UserToken> userTokenResponseEntity(@RequestParam String username, @RequestParam String pwd) {
        // TODO: 02.04.2020 Nach DB Anbindung muss das eingehende passwort mit dem in der DB abgeglichen werden. Falls korrekt wird der Usertoken zurückgegeben.
        if (pwd.contentEquals("testpwd")) {
            return ResponseEntity.status(200).body(userTokenCreator.createUserToken(username));

        } else return ResponseEntity.status(403).body(null);
    }


    // SEND PASSWORD
    @RequestMapping(value = "/users/password", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Password> forgetPassword(@RequestParam String username, @RequestParam String email) {
        // TODO: 02.04.2020 Nach DB Anbindung mit username und email das korrekte passwort aus DB abfragen und zurücksenden. Bescheuert? Ja.
        if (username.contentEquals("Testuser") && email.contentEquals("Testmail@test.de")) {
           // String pwd = "Hier sollte eigentlich aus der DB das korrekte Passwort geholt werden und weiterverschickt werden.";
            Password password = new Password("start123");

            return ResponseEntity.status(200).body(password);
        } else return ResponseEntity.status(403).body(null);
    }

    // Registrieren
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<Null> registrateResponseEntity(@RequestBody User user) {
        // TODO: 03.04.2020 if (DB.registrateUser(user)){return ResponseEntity.status(200).build();}else{return ResponseEntity.status(400).build();}

        return ResponseEntity.status(200).build();
    }

    // Main Paige Get documents
    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    public ResponseEntity<List<Note>> notizResponseEntity(@RequestParam String user_token) {
        User user = new User(b64Decoder.b64Decoder(user_token));
        // TODO: 03.04.2020 Aus der DB die Notizen des user holen und in den Response Body packen. Eventuell als Liste oder als Map.
        Note note = new Note("1", "Einkaufsliste", "18.02.1994", "Testinhalt");
        List<Note> notes = new ArrayList<>();
        notes.add(note);
        return ResponseEntity.status(200).body(notes);
    }

    // DELETE Eintrag von User
    @RequestMapping(value = "/documents", method = RequestMethod.DELETE)
    public ResponseEntity<Null> deleteResponseEntity(@RequestParam String user_token, @RequestParam String eintrag_id) {
        User user = new User(b64Decoder.b64Decoder(user_token));
        // TODO: 03.04.2020 Delete Eintrag mit eintrag_id von User user aus DB
        return ResponseEntity.status(204).build();

    }

    // Neuen Eintrag Speichern.
    @RequestMapping(value = "/documents", method = RequestMethod.POST)
    public ResponseEntity<Null> createNewNote(@RequestParam String user_token, @RequestBody Note note) {
        User user = new User(b64Decoder.b64Decoder(user_token));
        // TODO: 03.04.2020 DB.createNewNote(user, note);
        if (user.getUsername().contentEquals("Testuser")) {
            return ResponseEntity.status(201).build();
        } else return ResponseEntity.status(403).build();
    }


    // Vorhandenen Eintrag überschreiben
    @RequestMapping(value = "/documents", method = RequestMethod.PUT)
    public ResponseEntity<Null> patchExistingNote(@RequestParam String user_token, @RequestBody Note note) {
        User user = new User(b64Decoder.b64Decoder(user_token));
        // TODO: 03.04.2020  DB.patchExistingNode(user, note);
        return ResponseEntity.status(204).build();
    }


}

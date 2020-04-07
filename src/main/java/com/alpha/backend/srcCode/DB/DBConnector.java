package com.alpha.backend.srcCode.DB;

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

@Component
@Qualifier("DBConnector")
public class DBConnector {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private int Id;
    private String title;
    private String content;

    public static void main(String[] args) {
        Connection con;
        Statement stmt = null;
        ResultSet ut, dt;
        int id = 1;

        boolean passwort = false; // wird aus dem front end übergeben
        String username = "test"; // wird aus dem front end übergeben

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.print("Fehler: ");
        }

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", "");

            try {
                stmt = con.createStatement();
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            ut = stmt.executeQuery("select * from usertabelle where nutzername='" + username + "';"); // tabellezeile
            // des users
            // wird geladen
            while (ut.next()) {
                String pw = ut.getString("passwort"); // passwort des users wird übergeben
                id = ut.getInt("id"); // id des users wird übergeben
                String mail = ut.getString("email"); // e mail des users wird übergeben
                if (pw == "Passwort Front end") { // passwortabfrage
                    passwort = true; // frontend eingabe des pw wird mit db abgeglichen
                }
            }

            dt = stmt.executeQuery("select * from dokumententabelle where id='" + id + "';"); // alle notizen mit
            // zugehöriger ID (id
            // des angemeldeten
            // nutzers) werden
            // geladen
            String[][] notizen = new String[1000][3]; // array zum speichern der notizen, 3 spalten breit (titel,
            // inhalt, datum), bis zu 1000 einträge
            int i = 0;
            while (dt.next()) { // wird solange wiederholt bis die Tabelle leer ist
                notizen[i][0] = dt.getString("title"); // Titel aus der Dokumententabelle
                notizen[i][1] = dt.getString("inhalt"); // Inhalt aus der Dokumententabelle

                Date d = dt.getDate("datum");
                DateFormat df = new SimpleDateFormat();
                String dateString = df.format(d); // Datum in einen String umwandeln, wegen Strng array
                notizen[i][2] = dateString;

                i++;
            }

            // neue notizen anlegen
            String title = ""; // eingabe aus dem Front end
            String inhalt = "";// eingabe aus dem front end
            stmt.addBatch("insert into dokumententabelle (title, datum, inhalt, eigentuemerid)  values('" + title
                    + "','2018-01-01' ,'" + inhalt + "'," + id + ");");
            stmt.executeBatch();

            // neue User anlegen
            String nutzername = ""; // eingabe aus dem frontend
            String passwort1 = ""; // eingabe aus dem frontend
            String email = ""; // eingabe aus dem frontend
            stmt.addBatch("INSERT INTO usertabelle (nutzername, passwort, email) VALUES ('" + nutzername + "','"
                    + passwort1 + "','" + email + "');");
            stmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}

package com.alpha.backend.srcCode.DTOs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("Note")
public class Note {

    private String titel;
    private String datum;
    private String inhalt;

    public Note() {
    }

    public Note(String titel, String inhalt, String datum) {
        this.titel = titel;
        this.datum = datum;
        this.inhalt = inhalt;
    }


    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }


}


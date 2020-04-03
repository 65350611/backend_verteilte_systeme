package com.alpha.backend.srcCode.DTOs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("Notiz")
public class Note {
    private String id;
    private String titel;
    private String datum;
    private String inhalt;

    public Note(){}

    public Note(String id, String titel, String datum, String inhalt) {
        this.id = id;
        this.titel = titel;
        this.datum = datum;
        this.inhalt = inhalt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


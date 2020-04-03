package com.alpha.backend.srcCode.DTOs;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("User")
public class User {

    private String email;

    private String username;

    private String password;

    public User(){}

    public User(String username){
        this.username = username;
    }
    // TODO: 02.04.2020 ist ein get passwort überhaupt notwendig?
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString(/* no-op */) {
        return ("User: " + this.username + " Usermail: " + this.email);
    }
}
package com.alpha.backend.srcCode.DTOs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
@Qualifier("UserToken")
public class UserToken {

    private String user_token;

    public UserToken() {
    }

    public UserToken(String user_token) {
        this.user_token = user_token;
    }


    public String getUser_token() {
        return user_token;
    }

}
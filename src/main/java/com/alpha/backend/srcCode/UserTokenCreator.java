package com.alpha.backend.srcCode;

import com.alpha.backend.srcCode.DTOs.UserToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Base64;


@Component
@Qualifier("UserTokenCreator")
public class UserTokenCreator {

    //Verschlusselt den Usernamen zu Base64 und gibt einen UserToken zurück.
    public UserToken createUserToken(String username) {
        return new UserToken(Base64.getEncoder().encodeToString(username.getBytes()));
    }

}

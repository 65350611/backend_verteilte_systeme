package com.alpha.backend.srcCode;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Qualifier("B64Decoder")
public class B64Decoder {

    public String b64Decoder(String b64Code) {
        return new String(Base64.getDecoder().decode(b64Code.getBytes()));
    }


}

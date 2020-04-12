package com.alpha.backend.srcCode.DTOs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("NoteId")
public class NoteId {
    Integer element_id;
    public NoteId(){}
    public Integer getElement_id() {
        return element_id;
    }

    public void setElement_id(Integer element_id) {
        this.element_id = element_id;
    }
}

package com.example.loader.client_options.grant_mode.providing;

import java.io.Serializable;

public class Admission implements Serializable {

    private static final long serialVersionUID=23434349234L;

    private String filename,admitted,fromUser;

    void setFileName(String filename) {
        this.filename=filename;
    }

    void setAdmitted(String admitted) {
        this.admitted=admitted;
    }

    void setFrom(String fromUser) {
        this.fromUser=fromUser;
    }
}

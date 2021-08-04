package com.example.loader.data;

import java.io.Serializable;

public class InfoObject implements Serializable {

    private static final long serialVersionUID=23434349235L;

    private String name,type,size,creation,lastChanging;

    String getName() {
        return this.name;
    }

    String getType() {
        return this.type;
    }

    String getSize() {
        return this.size;
    }

    String getCreation() {
        return this.creation;
    }

    String getLastChanging() {
        return this.lastChanging;
    }
}


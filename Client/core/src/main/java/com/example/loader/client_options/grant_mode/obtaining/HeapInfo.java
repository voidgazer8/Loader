package com.example.loader.client_options.grant_mode.obtaining;

import java.io.Serializable;
import java.util.ArrayList;

public class HeapInfo implements Serializable {

    private static final long serialVersionUID=23434349236L;

    private ArrayList<String> listFiles,listSenders;

    ArrayList<String> getListFiles() {
        return this.listFiles;
    }

    ArrayList<String> getListSenders() {
        return this.listSenders;
    }
}

package com.example.loader.interfaces;

import android.content.Context;

import com.example.loader.client_options.grant_mode.providing.Admission;

import java.io.File;

public interface CallBack {

    void callback(File chosen, Context baseContext, Admission admission);

    void callNewFragment(boolean stateOfPossibility, Admission admission);
}

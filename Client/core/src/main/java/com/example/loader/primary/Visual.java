package com.example.loader.primary;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.loader.client_options.ordinary.Entry;
import com.example.loader.client_options.ordinary.Registration;
import com.example.loader.realization.Actual;
@RequiresApi(api = Build.VERSION_CODES.O)

public class Visual extends Start {

    private final String answer;

    public Visual(String answer) {
        super();
        this.answer=answer;
    }

    public void select(Context context){
        Intent intent;
        switch(answer) {
            case "no_users":
                intent=new Intent(context, Registration.class);
                context.startActivity(intent);
                break;
            case "users_present":
                intent = new Intent(context, Entry.class);
                context.startActivity(intent);
                break;
            case "connectEx":
                Start.showMessage("Нет подключения к серверу",context);
                Start.conf.dischargeAll();
                Start.conf.setElementsBlocked();
                Actual.state=false;
                break;
        }
    }
}
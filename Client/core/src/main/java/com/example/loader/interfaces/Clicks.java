package com.example.loader.interfaces;

import android.content.Context;
import android.os.Build;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;

public interface Clicks {

    void onClick(ListView listView, TextView dest, Context context);

    void onLongClick(ListView listView, Context context);

    @RequiresApi(api = Build.VERSION_CODES.O)
    default String sendRequest(String textOnElement) {
        Requests req = new Requests(Start.StandartQuerys.kind_of
                + "&user=" + Start.user.getUserName()
                + "&nameOf=" + textOnElement);
        req.init("simple", req);
        return req.getAnswer();
    }
}

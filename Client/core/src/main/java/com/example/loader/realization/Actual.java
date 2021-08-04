package com.example.loader.realization;

import android.content.SharedPreferences;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;

import java.util.ArrayList;
import java.util.Collections;

@RequiresApi(api = Build.VERSION_CODES.O)

public class Actual {

    public static boolean state;
    private String userName, folderName = "root";

    public void setUserName(String user_name) {
        this.userName = user_name;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setNote(String paramName, String cont) {
        SharedPreferences.Editor editor = Start.preferences.edit();
        editor.putString(paramName, cont);
        editor.apply();
    }

    public String getNote(String paramName) {
        return Start.preferences.getString(paramName, "default");
    }

    public void removeNote(String paramName) {
        SharedPreferences.Editor editor = Start.preferences.edit();
        editor.remove(paramName);
        editor.apply();
    }

    public boolean control() {
        String s1 = getNote("lastEntering");
        String s2 = getNote("remindMe");
        return s1.equals(s2) && !s1.equals("default");
    }

    public String[] parse(String s) {
        return s.split("--");
    }

    public ArrayList<String> getArrList(String[] answers) {
        ArrayList<String> ready = new ArrayList<>();
        Collections.addAll(ready, answers);

        if (ready.size() > 0) ready.remove(0);

        if (ready.size() == 0 || (ready.size()==1 && ready.get(0).contains("size->"))) ready.add(0, "Ничего нет");

        return ready;
    }

    public String getUsedSize(String s) {
        return s.substring(s.lastIndexOf("size->") + 6);
    }

    public void ifDir(TextView dest, String element) {
        dest.setText(element);
        Start.user.setFolderName(element);

        Requests req = new Requests(Start.StandartQuerys.get_list
                + "&user=" + Start.user.getUserName()
                + "&folder_to_get=" + element);
        req.init("simple",req);

        Start.conf.setElementsUnblocked();
        Start.conf.setList(Start.user.getArrList(Start.user.parse(req.getAnswer())),true,null);
    }

    public class ReDisplay {

        public void display() {
            Start.user.setFolderName("root");
            configureList();
            configureUserSize();
            getInfo(userName);
        }

        private void configureUserSize() {
            Requests req = new Requests(Start.StandartQuerys.get_size + "&user=" + userName);
            req.init("simple",req);
            Start.conf.installView("size->" + req.getAnswer());
        }

        private void configureList() {
            Requests req = new Requests(Start.StandartQuerys.get_list + "&user=" + userName);
            req.init("simple",req);
            Start.conf.setList(Start.user.getArrList(Start.user.parse(req.getAnswer())),true,null);
        }

        public void getInfo(String user) {
            Requests req = new Requests(Start.StandartQuerys.get_generalized
                    + "&user=" + user
                    + "&directory=" + Start.user.getFolderName());
            req.init("simple",req);

            if (!req.getAnswer().equals("connectEx"))
                Start.conf.installForwardInfo(parse(req.getAnswer()));
        }
    }
}

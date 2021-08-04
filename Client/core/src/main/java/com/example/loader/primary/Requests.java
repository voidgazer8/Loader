package com.example.loader.primary;

import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.loader.interfaces.IRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import pl.droidsonroids.gif.GifTextView;

@RequiresApi(api = Build.VERSION_CODES.O)

public class Requests {

    Path path;
    String login, neededFile, param,answer;
    ProgressBar bar;
    Button button;
    ImageView downloadPic;
    TextView percentsView, justView, text;
    GifTextView downloadingGif;

    byte[] encbytes;
    int status;
    long allSize;
    double reduced, percent;

    public Requests(String param) {
        this.param = param;
    }

    public Requests(byte[] encbytes) {
        this.encbytes=encbytes;
    }

    public Requests(String login, String neededFile, GifTextView downloadingGif,
                    TextView text, Button button, ImageView downloadPic) {
        this.login = login;
        this.neededFile = neededFile;
        this.downloadingGif = downloadingGif;
        this.text = text;
        this.button = button;
        this.downloadPic = downloadPic;
    }

    public Requests(Path path, ProgressBar bar, TextView percentsView, TextView justView) {
        this.path = path;
        this.bar = bar;
        this.percentsView = percentsView;
        this.justView = justView;
    }

     void setAnswer(HttpURLConnection connection) throws IOException {
        try (
                Reader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader buf = new BufferedReader(reader)
        ) {
            String line;
            while ((line = buf.readLine()) != null)
                answer = line;
        }
    }

    public void init(String happened,Requests reference) {
        IRequest request;
        try {
            switch (happened) {
                case "simple":
                    request=new SetConnection(param);
                    request.running(reference,request);
                    break;
                case "upload":
                    request = new SetUploading();
                    request.running(reference,request);
                    break;
                case "download":
                    request = new SetDownloading();
                    request.running(reference,request);
                    break;
                case "serialized":
                    request=new SendSerialized();
                    request.running(reference,request);
            }
        } catch (InterruptedException ex1_6) {
            Start.exTreatment(ex1_6, "[1.6]");
        }
    }

    public String getAnswer() {
        return this.answer;
    }
}

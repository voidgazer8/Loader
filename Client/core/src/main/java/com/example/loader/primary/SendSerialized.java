package com.example.loader.primary;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.loader.interfaces.IRequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SendSerialized extends Thread implements IRequest {

    private Requests locally;

    @Override
    public void run() {

        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://128.70.1.96:8080/Loader/ReceiveSerialized");
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Loader.sendObject");
            connection.setRequestProperty("Accept-Language", "ru, ru-RU");
            connection.setChunkedStreamingMode(4096);

            try (BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream())) {
                out.write(locally.encbytes);
            }
            connection.connect();
            locally.setAnswer(connection);
        } catch (IOException ex1_10) {
            Start.exTreatment(ex1_10, "[1.10]");
        } finally {
            assert connection != null;
            connection.disconnect();
        }
    }

    @Override
    public void running(Requests reference, IRequest request) throws InterruptedException {
        locally=reference;
        ((SendSerialized)request).start();
        ((SendSerialized)request).join();
    }
}

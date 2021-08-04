package com.example.loader.primary;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.loader.interfaces.IRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@RequiresApi(api = Build.VERSION_CODES.O)
class SetConnection extends Thread implements IRequest {

    private final String param;
    private  HttpURLConnection connection;
    private Requests locally;

    public SetConnection(String param) {
        this.param=param;
    }

    @Override
    public void run() {
        try {
            URL url = new URL("http://128.70.1.96:8080/Loader/RServlet?type=" + param);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Loader.call");
            connection.setRequestProperty("Accept", "text/html, text/plain");
            connection.setRequestProperty("Accept-Language", "ru, ru-RU");
            connection.connect();
            locally.setAnswer(connection);
        } catch (IOException ex1_3) {
            locally.answer = "connectEx";
            Start.exTreatment(ex1_3, "[1.3]");
        } finally {
            assert connection != null;
            connection.disconnect();
        }
    }

    @Override
    public void running(Requests reference, IRequest request) throws InterruptedException {
        locally=reference;
        ((SetConnection)request).start();
        ((SetConnection)request).join();
    }
}
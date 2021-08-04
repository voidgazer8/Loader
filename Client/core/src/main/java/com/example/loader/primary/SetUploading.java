package com.example.loader.primary;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.loader.R;
import com.example.loader.interfaces.IRequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RequiresApi(api = Build.VERSION_CODES.O)
class SetUploading extends Thread implements IRequest {

    private Requests locally;

    @Override
    public void run() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://128.70.1.96:8080/Loader/Uploading");
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Loader.receive");
            connection.setRequestProperty("Accept-Language", "ru, ru-RU");
            connection.setChunkedStreamingMode(4096);

            try (BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream())) {
                Thread upload = prepare(toInsert(), out);
                upload.start();
                upload.join();
            }
            connection.connect();
            locally.setAnswer(connection);

            locally.justView.post(() -> {
                if (locally.getAnswer().equals("success")) {
                    Start.conf.trackBar("Файл успешно загружен");
                    Start.user.new ReDisplay().display();
                    locally.justView.setText(R.string.title12);
                } else
                    Start.conf.trackBar("Возникла проблема");
            });

        } catch (IOException | InterruptedException ex1_4) {
            locally.answer = "connectEx";
            Start.exTreatment(ex1_4, "[1.4]");
        } finally {
            assert connection != null;
            connection.disconnect();
        }
    }

    private byte[] toInsert() {
        String key1 = "[~name~{",
                key2 = "+~user~{",
                key3 = "+~folder~{";

        String fileName = locally.path.getFileName().toString();
        String info = key1 + fileName
                + key2 + Start.user.getUserName()
                + key3 + Start.user.getFolderName()
                + "]";
        return info.getBytes();
    }

    private Thread prepare(byte[] info, BufferedOutputStream out) {
        return new Thread(() -> {
            try {
                File toDo = locally.path.toFile();
                InputStream stream = new FileInputStream(toDo);
                BufferedInputStream buf = new BufferedInputStream(stream);

                locally.allSize = toDo.length() / 1024 / 1024;
                locally.bar.setMax(100);
                locally.bar.setProgress(0);

                if (locally.allSize > 10)
                    confProgressView().start();

                locally.reduced = locally.allSize;
                byte[] bytes = new byte[4096];
                while ((locally.status = buf.read(bytes)) != -1) {
                    out.write(bytes, 0, locally.status);
                    locally.reduced = locally.reduced - 0.004;
                }
                out.write(info);
                out.close();
                buf.close();
            } catch (IOException ex1_5) {
                locally.answer = "connectEx";
                locally.status = -2;
                Start.exTreatment(ex1_5, "[1.5]");
            }
        });
    }

    private Thread confProgressView() {
        return new Thread(() -> {
            locally.justView.post(() -> locally.justView.setText(R.string.title11));

            while (locally.bar.getProgress() < 100) {
                locally.bar.post(() -> {
                    if (locally.status != -1 && locally.status != -2) {
                        locally.percent = (locally.reduced / locally.allSize) * 100;
                        int toShow = 100 - (int) locally.percent;
                        if (toShow > 100) toShow = 100;

                        locally.bar.setProgress(toShow);
                        locally.percentsView.setText(toShow + "%");
                    }
                });
            }
        });
    }

    @Override
    public void running(Requests reference, IRequest request) {
        locally = reference;
        ((SetUploading) request).start();
    }
}
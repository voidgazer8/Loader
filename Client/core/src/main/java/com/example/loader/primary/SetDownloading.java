package com.example.loader.primary;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.loader.R;
import com.example.loader.interfaces.IRequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RequiresApi(api = Build.VERSION_CODES.O)
class SetDownloading extends Thread implements IRequest {

    private File toReceive;
    private Requests locally;

    @Override
    public void run() {
        toReceive = new File(Start.way + "/Loader/Downloaded/" + locally.neededFile);
        try {
            toReceive.createNewFile();
        } catch (IOException e) { }

        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://128.70.1.96:8080/Loader/Downloading");
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Loader.send");
            connection.setRequestProperty("Accept-Language", "ru, ru-RU");
            connection.setChunkedStreamingMode(4096);

            try (BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream())) {
                out.write(("user{" + locally.login
                        + "}+needed{" + locally.neededFile
                        + "}+folder{" + Start.user.getFolderName()
                        + "}").getBytes());
            }

            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
            Thread download = dropping(in);
            download.start();
            download.join();

            connection.connect();
        } catch (IOException | InterruptedException ex1_8) {
            Start.exTreatment(ex1_8, "[1.8]");
        } finally {
            assert connection != null;
            connection.disconnect();
        }
    }

    private Thread dropping(BufferedInputStream in) {
        return new Thread(() -> {
            try {
                locally.downloadingGif.post(() -> {
                    locally.downloadPic.setVisibility(View.INVISIBLE);
                    locally.button.setVisibility(View.INVISIBLE);
                    locally.downloadingGif.setVisibility(View.VISIBLE);
                    locally.text.setVisibility(View.VISIBLE);
                });

                OutputStream outStream = new FileOutputStream(toReceive);
                BufferedOutputStream outBuf = new BufferedOutputStream(outStream);

                int n;
                byte[] bytes = new byte[4096];
                while ((n = in.read(bytes)) != -1)
                    outBuf.write(bytes, 0, n);

                outBuf.close();
                in.close();

                locally.downloadingGif.post(() -> {
                    locally.downloadingGif.setVisibility(View.INVISIBLE);
                    locally.text.setText(R.string.title34);
                });
                sleep(3000);

                locally.text.post(() -> {
                    locally.text.setVisibility(View.INVISIBLE);
                    locally.downloadPic.setVisibility(View.VISIBLE);
                    locally.button.setVisibility(View.VISIBLE);
                    locally.text.setText(R.string.title25);
                });
            } catch (IOException | InterruptedException ex1_9) {
                Start.exTreatment(ex1_9, "[1.9]");
            }
        });
    }

    @Override
    public void running(Requests reference, IRequest request) {
        locally=reference;
        ((SetDownloading)request).start();
    }
}
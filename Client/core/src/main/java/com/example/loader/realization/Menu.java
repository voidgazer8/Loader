package com.example.loader.realization;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loader.R;
import com.example.loader.client_options.ordinary.Renaming;
import com.example.loader.data.MainInfo;
import com.example.loader.interfaces.IMatch;
import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;

import pl.droidsonroids.gif.GifTextView;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Menu extends AppCompatActivity {
    
    private Button download;
    private ImageView downloadPic;
    private String typing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu_onclick);

        download = findViewById(R.id.download);
        downloadPic = findViewById(R.id.downloadView);
        GifTextView downloadingGif = findViewById(R.id.search);
        TextView inProcess = findViewById(R.id.inProcess);

        String naming = getIntent().getStringExtra("element");
        typing = getIntent().getStringExtra("type");

        if (typing.equals("directory")) {
            View[] v = {download, downloadPic, downloadingGif, inProcess};
            for (View view : v)
                view.setVisibility(View.GONE);
        }

        new OnDelete(naming).delete();
        onRename(naming);
        onDownload(naming);
        onGetInfo(naming);
    }

    private void onGetInfo(String naming) {
        Button getInfo=findViewById(R.id.getSomeInfo);
        getInfo.setOnClickListener(v -> {
            getInfo.startAnimation(Start.anim);
            Intent intent = new Intent(this, MainInfo.class);
            intent.putExtra("name", naming);
            startActivity(intent);
            finish();
        });
    }

    private void onRename(String file) {
        Button rename = findViewById(R.id.rename);
        rename.setOnClickListener(v -> {
            rename.startAnimation(Start.anim);
            Intent intent = new Intent(this, Renaming.class);
            intent.putExtra("oldName", file);
            intent.putExtra("typing", typing);
            startActivity(intent);
            finish();
        });
    }

    private void onDownload(String neededFile) {
        download.setOnClickListener(v -> {

            GifTextView gifView = findViewById(R.id.search);
            TextView textDisplaying = findViewById(R.id.inProcess);

            Requests req = new Requests(Start.user.getUserName(), neededFile, gifView, textDisplaying, download, downloadPic);
            req.init("download",req);
        });
    }


    private class OnDelete implements IMatch {
        private final String naming;

        public OnDelete(String naming) {
            this.naming = naming;
        }

        private void delete() {
            Button delete = findViewById(R.id.delete);
            delete.setOnClickListener(v -> {
                delete.startAnimation(Start.anim);

                Requests req = new Requests(Start.StandartQuerys.delete
                        + "&user=" + Start.user.getUserName()
                        + "&to_delete=" + naming
                        + "&object=" + typing
                        + "&currentFolder=" + Start.user.getFolderName());
                req.init("simple",req);

                Start.showMessage(match(req.getAnswer()), Menu.this);
            });
        }

        @Override
        public String match(String answer) {
            String msg = "Файл удален";
            if (typing.equals("directory"))
                msg = "Папка удалена";

            if (answer.equals("deleted")) {
                finish();
                Start.user.new ReDisplay().display();
                return msg;
            } else
                return "Не удалось удалить";
        }
    }
}

package com.example.loader.client_options.ordinary;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loader.R;
import com.example.loader.interfaces.IMatch;
import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;

@RequiresApi(api = Build.VERSION_CODES.O)

public class Renaming extends AppCompatActivity implements IMatch {

    private Requests req;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_name);

        EditText field = findViewById(R.id.newed);
        Button change = findViewById(R.id.change);

        String oldName = getIntent().getStringExtra("oldName");
        field.setText(oldName);
        field.setSelectAllOnFocus(true);

        String typeOfElement=getIntent().getStringExtra("typing");

        change.setOnClickListener(v -> {
            String newName = field.getText().toString();
            if (!newName.equals("")) {

                req = new Requests(Start.StandartQuerys.rename
                        + "&user="+Start.user.getUserName()
                        + "&to_rename=" + oldName
                        + "&set=" + newName
                        + "&object=" + typeOfElement
                        + "&currentFolder="+Start.user.getFolderName());
                req.init("simple",req);

                Start.showMessage(match(typeOfElement), this);
            } else
                Start.showMessage("Недопустимое имя", this);
        });
    }

    @Override
    public String match(String typeOfElement) {
        String msg="Файл переименован";
        if(typeOfElement.equals("directory"))
            msg="Папка переименована";

        if (req.getAnswer().equals("renamed")) {
            finish();
            Start.user.new ReDisplay().display();
            return msg;
        } else
            return "Не удалось переименовать";
    }
}

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
public class CreateFolder extends AppCompatActivity implements IMatch {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_folder);

        Button createFolder = findViewById(R.id.change);
        EditText field = findViewById(R.id.newed);

        createFolder.setOnClickListener(v -> {
            String folderName = field.getText().toString();
            if (!folderName.equals("")) {
                Requests req = new Requests(Start.StandartQuerys.create_folder
                        + "&user=" + Start.user.getUserName()
                        + "&folder=" + folderName);
                req.init("simple",req);
                Start.showMessage(match(req.getAnswer()), this);
            } else
                Start.showMessage("Недопустимое имя", this);
        });
    }

    @Override
    public String match(String answer) {
        switch (answer) {
            case "created":
                Start.user.new ReDisplay().display();
                finish();
                return "Папка создана";
            case "existing_folder":
                return "Папка с таким именем уже есть";
            case "went_wrong":
                return "Не удалось";
            default:
                return "impossible";
        }
    }
}

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
public class LoginReplacement extends AppCompatActivity implements IMatch {
    
    private String newLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_login);

        EditText view = findViewById(R.id.newed);
        Button change = findViewById(R.id.change);

        change.setOnClickListener(v -> {
            change.startAnimation(Start.anim);
            newLogin = view.getText().toString();

            if (newLogin.equals(""))
                Start.showMessage("Недопустимое имя", this);
            else {
                Requests req = new Requests(Start.StandartQuerys.change_login
                        + "&old_login=" + Start.user.getUserName()
                        + "&new_login=" + newLogin);
                req.init("simple", req);
                Start.showMessage(match(req.getAnswer()), this);
            }
        });
    }

    @Override
    public String match(String answer) {
        switch (answer) {
            case "changed":
                Start.user.setUserName(newLogin);
                Start.user.new ReDisplay().display();
                Start.user.setNote("remindMe", newLogin);
                Start.user.setNote("lastEntering", newLogin);
                finish();
                return "Логин успешно изменен";
            case "existed":
                return "Пользователь с таким логином уже существует";
            case "went_wrong":
                return "Что-то пошло не так (";
            default:
                return "impossible";
        }
    }
}

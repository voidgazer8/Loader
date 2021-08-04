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
import com.example.loader.realization.Frames;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PasswordReplacement extends AppCompatActivity implements IMatch {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_password);

        Frames functionality=new Frames();
        functionality.setFeature(this);

        Button change=findViewById(R.id.change2);
        EditText newPassword=findViewById(R.id.newPassword);
        EditText oldPassword=findViewById(R.id.oldPassword);

        change.setOnClickListener(v -> {
            change.startAnimation(Start.anim);

            String newOne=newPassword.getText().toString();
            String oldOne=oldPassword.getText().toString();

            if(newOne.equals("") || oldOne.equals(""))
                Start.showMessage("Заполните поля",this);
            else {
                Requests req=new Requests(Start.StandartQuerys.change_password
                        +"&user="+Start.user.getUserName()
                        +"&old_password="+oldOne
                        +"&new_password="+newOne);
                req.init("simple",req);
                Start.showMessage(match(req.getAnswer()),this);
            }
        });
    }

    @Override
    public String match(String answer) {
        switch(answer) {
            case "changed":
                finish();
                return "Пароль успешно изменен. При следующем входе используйте новый пароль";
            case "doesn't_match":
                return "Неверный пароль";
            case "went_wrong":
                return "Что-то пошло не так (";
            default:
                return "impossible";
        }
    }
}

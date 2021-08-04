package com.example.loader.client_options.ordinary;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loader.realization.Actual;
import com.example.loader.realization.Frames;
import com.example.loader.R;
import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;

@RequiresApi(api = Build.VERSION_CODES.O)

public class Registration extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.registration);
        setFinishOnTouchOutside(false);

        Frames functionality = new Frames();
        functionality.setFeature(this);

        BasicActionsKit listeners=new BasicActionsKit(findViewById(R.id.doing), this);
        listeners.basicSet();
    }
}

@RequiresApi(api = Build.VERSION_CODES.O)
class BasicActionsKit {
    Button reg;
    Context currentContext;
    Activity localActivity;
    String login, password;

    BasicActionsKit(Button reg, Context currentContext) {
        this.reg = reg;
        this.currentContext = currentContext;
        localActivity = (Activity) currentContext;
    }

    public void basicSet() {
        reg.setOnClickListener(v -> {
            reg.startAnimation(Start.anim);

            EditText loginView = localActivity.findViewById(R.id.login);
            EditText passwordView = localActivity.findViewById(R.id.password);
            login = loginView.getText().toString();
            password = passwordView.getText().toString();

            if(!login.equals("") && !password.equals(""))
                processing();
            else
                Start.showMessage("Не оставляйте пустые поля",currentContext);
        });
    }

    void processing() {
        Requests req = new Requests(Start.StandartQuerys.add_client
                + "&login=" + login
                + "&password=" + password);
        req.init("simple", req);
        Start.showMessage(switching(req.getAnswer()), currentContext);
    }

    String switching(String wholeAnswer) {
        String[] comps = Start.user.parse(wholeAnswer);
        switch (comps[0]) {
            case "well_done":
                return proceed(comps);
            case "went_wrong":
                return "Что-то пошло не так (. Попробуйте еще раз";
            case "existed":
                return "Пользователь с такими данными уже существует";
            default:
                return "impossible";
        }
    }

    String proceed(String[] answers) {
        Start.user.setUserName(login);
        Start.conf.setList(Start.user.getArrList(answers),true,null);
        Start.conf.installView(answers[answers.length - 1]);
        Start.conf.setElementsUnblocked();
        Actual.state = true;
        new Actual().new ReDisplay().getInfo(login);
        localActivity.finish();
        return "Аккаунт успешно создан";
    }
}
package com.example.loader.client_options.ordinary;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.loader.realization.Actual;
import com.example.loader.realization.Frames;
import com.example.loader.R;
import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;

@RequiresApi(api = Build.VERSION_CODES.O)

public class Entry extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.enter);
        setFinishOnTouchOutside(false);

        Frames functionality = new Frames();
        functionality.setFeature(this);

        EntryActions listeners = new EntryActions(findViewById(R.id.doing), this);
        listeners.secondarySet();
        listeners.basicSet();
    }
}

@RequiresApi(api = Build.VERSION_CODES.O)
class EntryActions extends BasicActionsKit {

    EntryActions(Button reg, Context currentContext) {
        super(reg, currentContext);
    }

    @Override
    void processing() {
        Requests req = new Requests(Start.StandartQuerys.make_enter
                + "&login=" + login
                + "&password=" + password);
        req.init("simple", req);
        Start.showMessage(switching(req.getAnswer()), currentContext);
    }

    @Override
    String switching(String wholeAnswer) {
        String[] comps = Start.user.parse(wholeAnswer);
        switch (comps[0]) {
            case "well_done":
                return proceed(comps);
            case "went_wrong":
                return "Что-то пошло не так (. Попробуйте еще раз";
            case "didn't_find":
                return "Пользователь с такими данными не найден";
            default:
                return "impossible";
        }
    }

    @Override
    String proceed(String[] answers) {
        Start.user.setUserName(login);
        Start.conf.installView(answers[answers.length - 1]);
        Start.conf.setList(Start.user.getArrList(answers),true,null);
        Start.conf.setElementsUnblocked();
        localActivity.finish();
        Actual.state = true;

        new Actual().new ReDisplay().getInfo(login);
        CheckBox box = localActivity.findViewById(R.id.checkBox);
        if (box.isChecked())
            Start.user.setNote("remindMe", login);

        Start.user.setNote("lastEntering", login);
        return "Вход успешно выполнен";
    }

    void secondarySet() {
        localActivity.findViewById(R.id.jump).setOnClickListener(v -> jump());
    }

    private void jump() {
        Intent intent = new Intent(currentContext, Registration.class);
        currentContext.startActivity(intent);
        localActivity.finish();
    }
}
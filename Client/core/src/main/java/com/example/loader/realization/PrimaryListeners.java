package com.example.loader.realization;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.loader.R;
import com.example.loader.client_options.ordinary.CreateFolder;
import com.example.loader.client_options.ordinary.Entry;
import com.example.loader.client_options.grant_mode.providing.Admission;
import com.example.loader.client_options.grant_mode.providing.GrantMode;
import com.example.loader.client_options.ordinary.LoginReplacement;
import com.example.loader.client_options.ordinary.PasswordReplacement;
import com.example.loader.client_options.ordinary.Registration;
import com.example.loader.client_options.ordinary.ToFolder;
import com.example.loader.client_options.grant_mode.obtaining.SpaceOfGrantedFiles;
import com.example.loader.data.FileExplorer;
import com.example.loader.interfaces.CallBack;
import com.example.loader.interfaces.Clicks;
import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.Serializable;


@RequiresApi(api = Build.VERSION_CODES.O)
public class PrimaryListeners implements CallBack, Clicks, Serializable {

    public void upload(ImageView touch, Context context) {
        touch.setOnClickListener(v -> {
            touch.startAnimation(Start.anim);
            Intent intent = new Intent(context, FileExplorer.class);
            intent.putExtra("object", this);
            intent.putExtra("rootDirectory", true);
            context.startActivity(intent);
        });
    }

    @Override
    public void onClick(ListView listView, TextView dest, Context context) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Clicks clicks = new PrimaryListeners();

            TextView element = view.findViewById(R.id.name);
            String textOnElement = element.getText().toString();

            if (clicks.sendRequest(textOnElement).equals("directory")) {
                Start.user.ifDir(dest, textOnElement);
                Start.user.new ReDisplay().getInfo(Start.user.getUserName());
            } else {
                Intent intent = new Intent(context, ToFolder.class);
                intent.putExtra("filename", textOnElement);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onLongClick(ListView listView, Context context) {
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Clicks clicks = new PrimaryListeners();

            TextView element = view.findViewById(R.id.name);
            String textOnElement = element.getText().toString();

            if (!textOnElement.equals("Ничего нет")) {
                Intent intent = new Intent(context, Menu.class);
                intent.putExtra("element", textOnElement);
                intent.putExtra("type", clicks.sendRequest(textOnElement));
                context.startActivity(intent);
            }
            return true;
        });
    }


    public void update(ImageView update, Context context) {
        update.setOnClickListener(v -> {
            update.startAnimation(Start.anim);

            Requests req = new Requests(Start.StandartQuerys.check_regs.toString());
            req.init("simple", req);

            if (req.getAnswer().equals("connectEx")) {
                Start.showMessage("Нет подключения к серверу", context);
                Start.conf.dischargeAll();
                Start.conf.setElementsBlocked();
            } else {

                if (req.getAnswer().equals("users_present") && !Actual.state && !Start.user.control())
                    context.startActivity(new Intent(context, Entry.class));
                else
                    Start.user.new ReDisplay().display();
            }
        });
    }

    public void toUp(ImageView up, ConstraintLayout layout) {
        up.setOnClickListener(v -> {
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(layout);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
    }

    public void changeLogin(Button changeLoginView, Context context) {
        changeLoginView.setOnClickListener(v -> {
            changeLoginView.startAnimation(Start.anim);
            Intent intent = new Intent(context, LoginReplacement.class);
            context.startActivity(intent);
        });
    }

    public void changePassword(Button changePasswordView, Context context) {
        changePasswordView.setOnClickListener(v -> {
            changePasswordView.startAnimation(Start.anim);
            Intent intent = new Intent(context, PasswordReplacement.class);
            context.startActivity(intent);
        });
    }

    public void createAccount(Button create, Context context) {
        create.setOnClickListener(v -> {
            create.startAnimation(Start.anim);
            Intent intent = new Intent(context, Registration.class);
            context.startActivity(intent);
        });
    }

    public void createNewFolder(ImageView folderView, Context context) {
        folderView.setOnClickListener(v -> {
            folderView.startAnimation(Start.anim);
            Intent intent = new Intent(context, CreateFolder.class);
            context.startActivity(intent);
        });
    }

    public void onBack(ImageView back, TextView dest) {
        back.setOnClickListener(v -> {
            back.startAnimation(Start.anim);

            Start.user.setFolderName("root");
            dest.setText("");
            Requests req = new Requests(Start.StandartQuerys.get_list + "&user=" + Start.user.getUserName());
            req.init("simple", req);
            Start.conf.setList(Start.user.getArrList(Start.user.parse(req.getAnswer())), true, null);
            Start.user.new ReDisplay().getInfo(Start.user.getUserName());
        });
    }

    public void onShow(Button showDownloadedView, Context context) {
        showDownloadedView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FileExplorer.class);
            intent.putExtra("object", this);
            intent.putExtra("rootDirectory", false);
            intent.putExtra("pathToDownloaded", Start.way + "/Loader/Downloaded");
            context.startActivity(intent);
        });
    }

    public void onAccessProvide(TextView title, Context context) {
        title.setOnClickListener(v1 -> {
            title.startAnimation(Start.anim);
            Intent intent = new Intent(context, GrantMode.class);
            context.startActivity(intent);
        });
    }

    public void onGettingAccess(ImageView title, Context context) {
        title.setOnClickListener(v1 -> {
            title.startAnimation(Start.anim);
            Intent intent=new Intent(context, SpaceOfGrantedFiles.class);
            context.startActivity(intent);
        });
    }

    @Override
    public void callback(File chosen, Context baseContext, Admission admission) {
        if (checkOut(chosen.getName()))
            Start.showMessage("Обнаружены недопустимые символы. Вероятны проблемы с загрузкой. Переименуйте файл", baseContext);
        else
            Start.conf.uploadWithChosenFile(chosen.getAbsolutePath());
    }

    @Override
    public void callNewFragment(boolean stateOfPossibility, Admission admission) {
    }

    private boolean checkOut(String toString) {
        String[] notAllowed = {"[", "]", "{", "}", "|"};
        String[] chars = toString.split("");

        for (String aChar : chars) {
            for (String s : notAllowed) {
                if (aChar.equals(s))
                    return true;
            }
        }
        return false;
    }
}

package com.example.loader.primary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.loader.R;
import com.example.loader.custom.Adapter;
import com.example.loader.custom.Item;
import com.example.loader.realization.Actual;
import com.example.loader.realization.Decisions;
import com.example.loader.realization.PrimaryListeners;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)

public class Start extends AppCompatActivity {

    public static Actual user;
    public static Configure conf;
    public static Animation anim;
    public static FragmentManager manager;
    public static SharedPreferences preferences;
    public static String way;

    private static final File log, dir1, dir2, dir3;
    private static long back_pressed;

    private Context context;

    static {
        way = Environment.getExternalStorageDirectory().toString();
        user = new Actual();
        log = new File(way + "/Loader/logs/log.txt");
        dir1 = new File(way + "/Loader");
        dir2 = new File(way + "/Loader/logs");
        dir3 = new File(way + "/Loader/Downloaded");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        manager = getSupportFragmentManager();

        context = this;
        anim = AnimationUtils.loadAnimation(context, R.anim.click_animation);
        preferences = getSharedPreferences("Current.setting", Context.MODE_PRIVATE);

        conf = new Configure() {

            private final TextView dest = findViewById(R.id.innerFolder), root = findViewById(R.id.destination);

            private final View[] arr1 = {findViewById(R.id.addingFile), findViewById(R.id.addingFolder),
                    findViewById(R.id.back), findViewById(R.id.tview), findViewById(R.id.currentGB),
                    root, dest, findViewById(R.id.showDownloaded),
                    findViewById(R.id.backPanel), findViewById(R.id.imageView4), findViewById(R.id.accessedTitle),
                    findViewById(R.id.accessGrantedPic),findViewById(R.id.accessPic)};


            private final View[] arr2 = {findViewById(R.id.exit), findViewById(R.id.changeLogin),
                    findViewById(R.id.changePassword), findViewById(R.id.deleteAccount),
                    findViewById(R.id.createNew)};

            private final ListView listView = findViewById(R.id.main_list);
            private final TextView userName = findViewById(R.id.login_view),
                    percents = findViewById(R.id.percents),
                    status = findViewById(R.id.statusView);

            private final ProgressBar bar = findViewById(R.id.progress);

            @Override
            public void installView(String answer) {
                userName.setText(user.getUserName());
                root.setText("Root/");
                dest.setText("");
                user.setFolderName("root");
                TextView gb = findViewById(R.id.currentGB);
                gb.setText(user.getUsedSize(answer));
            }

            @Override
            public void trackBar(String localMessage) {
                showMessage(localMessage, context);
                bar.setProgress(0);
                status.setText("");
                percents.setText("");
            }

            @Override
            public void setElementsUnblocked() {
                for (View v : arr1)
                    v.setVisibility(View.VISIBLE);
                for (View v : arr2)
                    v.setEnabled(true);
            }


            @Override
            public void setElementsBlocked() {
                for (View v : arr1)
                    v.setVisibility(View.INVISIBLE);
                for (View v : arr2)
                    v.setEnabled(false);
            }


            @Override
            public void dischargeAll() {
                listView.setAdapter(null);
                userName.setText("");
                dest.setText("");
                root.setText("");
                user.removeNote("remindMe");
                Actual.state = false;
            }


            @Override
            public void installForwardInfo(String[] parsed) {
                TextView all, files, folders;
                all = findViewById(R.id.allNumber);
                files = findViewById(R.id.filesNumber);
                folders = findViewById(R.id.foldersNumber);

                all.setText(parsed[0]);
                files.setText(parsed[1]);
                folders.setText(parsed[2]);
            }

            @Override
            public void uploadWithChosenFile(String absolutePath) {
                Requests req = new Requests(Paths.get(absolutePath), bar, percents, status);
                req.init("upload", req);
            }

            @Override
            public void setList(ArrayList<String> elements,boolean b,ListView given) {
                ArrayList<Item> list = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    if (!elements.get(i).contains("size->"))
                        list.add(new Item(elements.get(i)));
                }
                Adapter adapter = new Adapter(context, list);
                if(b)
                    listView.setAdapter(adapter);
                else
                    given.setAdapter(adapter);
            }
        };


        if (!check_perm()) {
            ActivityCompat.requestPermissions((Activity) context, new String[]
                    {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 0);
        } else
            continuation();
    }

    private void continuation() {
        setLocalDir();

        ImageView loadView, updateView, folderView, upView, backView,
                  accessPic;

        loadView = findViewById(R.id.addingFile);
        updateView = findViewById(R.id.update);
        folderView = findViewById(R.id.addingFolder);
        upView = findViewById(R.id.get_up);
        backView = findViewById(R.id.back);
        accessPic=findViewById(R.id.accessPic);

        Button exitView, changeLoginView, changePasswordView,
                createView, deleteView, showDownloadedView;

        exitView = findViewById(R.id.exit);
        changeLoginView = findViewById(R.id.changeLogin);
        changePasswordView = findViewById(R.id.changePassword);
        createView = findViewById(R.id.createNew);
        deleteView = findViewById(R.id.deleteAccount);
        showDownloadedView = findViewById(R.id.showDownloaded);

        ConstraintLayout layout = findViewById(R.id.sheet);
        ListView listView = findViewById(R.id.main_list);
        TextView dest = findViewById(R.id.innerFolder), title = findViewById(R.id.accessedTitle);

        Visual vis = new Visual(direction().getAnswer());
        vis.select(context);

        PrimaryListeners listen = new PrimaryListeners();
        listen.upload(loadView, this);
        listen.update(updateView, this);
        listen.toUp(upView, layout);
        listen.changeLogin(changeLoginView, this);
        listen.changePassword(changePasswordView, this);
        listen.createAccount(createView, this);
        listen.createNewFolder(folderView, this);
        listen.onClick(listView, dest, this);
        listen.onLongClick(listView, this);
        listen.onBack(backView, dest);
        listen.onShow(showDownloadedView, this);
        listen.onAccessProvide(title, this);
        listen.onGettingAccess(accessPic,this);

        delete(deleteView);
        exit(exitView);
    }

    protected Requests direction() {
        Requests req;
        if (user.control()) {
            req = new Requests(StandartQuerys.make_enter
                    + "&login=" + user.getNote("lastEntering")
                    + "&password=<>");
            req.init("simple", req);

            user.setUserName(user.getNote("remindMe"));
            user.new ReDisplay().getInfo(user.getUserName());
            Start.conf.installView(req.getAnswer());
            Start.conf.setElementsUnblocked();
            Start.conf.setList(user.getArrList(user.parse(req.getAnswer())),true,null);
        } else {
            req = new Requests(StandartQuerys.check_regs.toString());
            req.init("simple", req);
        }
        return req;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setLocalDir();
                continuation();
            } else {
                showMessage("Не были получены необходимые разрешения", context);
                finish();
            }
        }
    }

    private void setLocalDir() {
        try {

            if (!dir1.exists()) dir1.mkdirs();
            if (!dir2.exists()) dir2.mkdir();
            if (!dir3.exists()) dir3.mkdir();
            if (!log.exists()) log.createNewFile();

        } catch (IOException ex1_2) {
            exTreatment(ex1_2, "[1.2]");
        }
    }

    private boolean check_perm() {
        int status = ContextCompat.checkSelfPermission(Start.this.context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return status == PackageManager.PERMISSION_GRANTED;
    }

    private void exit(Button exitView) {
        exitView.setOnClickListener(v -> {
            exitView.startAnimation(Start.anim);

            Bundle arg = new Bundle();
            arg.putString("argument", "onExit");

            Decisions decision = new Decisions();
            decision.setArguments(arg);
            decision.show(manager, "dialog");
            Actual.state = false;
        });
    }

    private void delete(Button deleteView) {
        deleteView.setOnClickListener(v -> {
            deleteView.startAnimation(Start.anim);

            Bundle arg = new Bundle();
            arg.putString("argument", "onDelete");

            Decisions decision = new Decisions();
            decision.setArguments(arg);
            decision.show(manager, "dialog");
            Actual.state = false;
        });
    }

    public static void showMessage(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            showMessage("Нажмите еще раз для выхода", context);
        back_pressed = System.currentTimeMillis();
    }

    public static void exTreatment(Exception ex, String code) {
        try (
                OutputStream out = new FileOutputStream(Start.log, true);
                PrintStream drop = new PrintStream(out)
        ) {
            ex.printStackTrace(drop);
            drop.write(("\n" + "place code: at " + code + "\n").getBytes());
        } catch (IOException ex1_1) {
            exTreatment(ex1_1, "[1.1]");
        }
    }

    public enum StandartQuerys {
        check_regs, add_client, make_enter, get_list,
        get_size, change_login, change_password,
        delete_account, delete, rename,
        create_folder, kind_of, get_generalized,
        get_full, get_folder_list, move_to
    }

    public enum GrantingAccessQuerys {
        check_user,is_granted,get_granted_files
    }

    public static abstract class Configure {

        public abstract void setList(ArrayList<String> elements,boolean b,ListView v);

        public abstract void uploadWithChosenFile(String absolutePath);

        public abstract void installView(String answer);

        public abstract void installForwardInfo(String[] parsed);

        public abstract void dischargeAll();

        public abstract void setElementsBlocked();

        public abstract void setElementsUnblocked();

        public abstract void trackBar(String localMessage);
    }
}

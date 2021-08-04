package com.example.loader.data;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.loader.R;
import com.example.loader.custom.Adapter;
import com.example.loader.custom.Item;
import com.example.loader.custom.Storages;
import com.example.loader.interfaces.CallBack;
import com.example.loader.primary.Start;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileExplorer extends AppCompatActivity {

    private Button externalButton, internalButton;
    private ImageView block1, block2, search;
    private TextView totalPath;
    private ListView content;
    private List<String> storages;
    private String earlyPath;
    private CallBack call;
    private EditText input;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.own_file_explorer);

        externalButton = findViewById(R.id.external);
        internalButton = findViewById(R.id.internal);

        block1 = findViewById(R.id.block1);
        block2 = findViewById(R.id.block2);
        search = findViewById(R.id.search);
        totalPath = findViewById(R.id.totalPath);
        content = findViewById(R.id.content);
        input = findViewById(R.id.inputPath);

        storages = new Storages(this, true).getPaths();
        if (storages.size() == 1)
            externalButton.setVisibility(View.INVISIBLE);

        earlyPath = storages.get(0);
        setContent(earlyPath);
        totalPath.setText(earlyPath);

        call = (CallBack) getIntent().getSerializableExtra("object");
        boolean root = getIntent().getBooleanExtra("rootDirectory", false);

        Inner listeners = new FileExplorer.Inner(root);
        listeners.checkStartDisplay();
        listeners.onInternalStorage();
        listeners.onExternalStorage();
        listeners.onElementClick();
        listeners.onSearch();
    }

    private void setContent(String path) {
        ArrayList<String> files = new ArrayList<>();
        File dir = new File(path);
        File[] flist = dir.listFiles();
        if (flist != null && flist.length > 0) {
            for (File f : flist) {
                String s = f.getName();
                if (f.isDirectory())
                    s = s + "[folder]";
                files.add(s);
            }
        }

        ArrayList<Item> list = new ArrayList<>();
        for (int i = 0; i < files.size(); i++)
            list.add(new Item(files.get(i)));

        Adapter adapter = new Adapter(this, list);
        content.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        String s = totalPath.getText().toString();
        String changed = s.substring(0, s.lastIndexOf("/"));
        for (String h : storages)
            if (!h.contains(s)) {
                totalPath.setText(changed);
                setContent(changed);
            } else
                super.onBackPressed();
    }


    private class Inner {
        boolean root;

        public Inner(boolean root) {
            this.root = root;
        }

        private void checkStartDisplay() {
            if (!root) {
                earlyPath = getIntent().getStringExtra("pathToDownloaded");
                totalPath.setText(earlyPath);
                setContent(earlyPath);
            }
        }

        private void onInternalStorage() {
            internalButton.setOnClickListener(v -> {
                earlyPath = storages.get(0);
                setContent(earlyPath);
                block2.setVisibility(View.INVISIBLE);
                block1.setVisibility(View.VISIBLE);
                totalPath.setText(earlyPath);
            });
        }

        private void onExternalStorage() {
            externalButton.setOnClickListener(v -> {
                earlyPath = storages.get(1);
                setContent(earlyPath);
                block1.setVisibility(View.INVISIBLE);
                block2.setVisibility(View.VISIBLE);
                totalPath.setText(earlyPath);
            });
        }

        private void onElementClick() {
            if (root) {
                content.setOnItemClickListener((parent, view, position, id) -> {
                    TextView name = view.findViewById(R.id.name);
                    String text = name.getText().toString();
                    File file = new File(totalPath.getText().toString() + "/" + text);

                    if (!file.isDirectory()) {
                        call.callback(file, getBaseContext(),null);
                        finish();
                    } else {
                        String gotPath = totalPath.getText() + "/" + text;
                        totalPath.setText(gotPath);
                        setContent(gotPath);
                    }
                });
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void onSearch() {
            TextView letter = findViewById(R.id.letter);

            ConstraintLayout constraintLayout = findViewById(R.id.parent);
            ConstraintSet constraintSet = new ConstraintSet();

            search.setOnClickListener(v -> {
                letter.setVisibility(View.INVISIBLE);
                input.setVisibility(View.VISIBLE);

                constraintSet.clone(constraintLayout);
                constraintSet.clear(R.id.search, ConstraintSet.START);
                constraintSet.connect(R.id.search, ConstraintSet.START, R.id.inputPath, ConstraintSet.END);
                constraintSet.applyTo(constraintLayout);
            });

            input.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    letter.setVisibility(View.VISIBLE);
                    input.setVisibility(View.INVISIBLE);

                    constraintSet.clone(constraintLayout);
                    constraintSet.clear(R.id.search, ConstraintSet.START);
                    constraintSet.connect(R.id.search,ConstraintSet.START,R.id.letter,ConstraintSet.END);
                    constraintSet.applyTo(constraintLayout);

                    File file=new File(input.getText().toString());
                    if(file.canRead()) {
                        call.callback(file, getBaseContext(),null);
                        finish();
                    } else
                        Start.showMessage("Файл не существует или указан неверный адрес",getBaseContext());
                }
                return true;
            });

            input.setOnFocusChangeListener((v, hasFocus) -> {
                if(hasFocus)
                    input.setText(earlyPath);
            });
        }
    }
}

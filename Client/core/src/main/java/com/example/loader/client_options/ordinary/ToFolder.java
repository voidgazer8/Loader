package com.example.loader.client_options.ordinary;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loader.R;
import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;

import java.util.ArrayList;
import java.util.Collections;

@RequiresApi(api = Build.VERSION_CODES.O)

public class ToFolder extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_to_folder);

        Requests req = new Requests(Start.StandartQuerys.get_folder_list
                + "&user=" + Start.user.getUserName());
        req.init("simple", req);

        ListView folders = findViewById(R.id.folders);
        ArrayList<String> arr=new ArrayList<>();
        Collections.addAll(arr,Start.user.parse(req.getAnswer()));

        if(!Start.user.getFolderName().equals("root"))
            arr.add(0,"Корневой каталог");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                arr);
        adapter.notifyDataSetChanged();
        folders.setAdapter(adapter);

        onChoose(folders);
    }

    private void onChoose(ListView folders) {
        folders.setOnItemClickListener((parent, view, position, id) -> {
            String folder=((TextView)view).getText().toString();
            String file=getIntent().getStringExtra("filename");

            if(folder.equals("Корневой каталог"))
                folder="root";

            Requests query=new Requests(Start.StandartQuerys.move_to
                    +"&user="+Start.user.getUserName()
                    +"&filename="+file
                    +"&target_folder="+folder
                    +"&current_folder="+Start.user.getFolderName());
            query.init("simple",query);

            if(query.getAnswer().equals("moved")) {
                Start.showMessage("Файл успешно перемещен",this);
                Start.user.new ReDisplay().display();
                finish();
            } else
                Start.showMessage("Возникла проблема",this);
        });
    }
}

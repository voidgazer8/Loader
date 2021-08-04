package com.example.loader.client_options.grant_mode.obtaining;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loader.R;
import com.example.loader.client_options.grant_mode.providing.StartFragment;
import com.example.loader.data.HackedObjectInputStream;
import com.example.loader.data.InfoObject;
import com.example.loader.interfaces.Clicks;
import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SpaceOfGrantedFiles extends AppCompatActivity implements Clicks {

    private  HeapInfo heap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_of_granted);

        View[] arr1 = new View[]{findViewById(R.id.txt3), findViewById(R.id.img1)};
        View[] arr2 = new View[]{findViewById(R.id.txt4), findViewById(R.id.img2)};

        ListView list=findViewById(R.id.grantedList);

        Requests req = new Requests(Start.GrantingAccessQuerys.is_granted + "&user=" + Start.user.getUserName());
        req.init("simple", req);

        if (req.getAnswer().equals("is")) {
            StartFragment.setSight(arr1, View.VISIBLE);
            displayList(list);
            onClick(list,null,null);
        } else {
            StartFragment.setSight(arr1, View.INVISIBLE);
            StartFragment.setSight(arr2, View.VISIBLE);
        }
    }

    private void displayList(ListView list) {
        Requests req = new Requests(Start.GrantingAccessQuerys.get_granted_files + "&user=" + Start.user.getUserName());
        req.init("simple", req);

        try {
            byte[] gotBytes = Base64.getDecoder().decode(req.getAnswer().getBytes());

            try (
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(gotBytes);
                    HackedObjectInputStream objectInputStream = new HackedObjectInputStream(byteArrayInputStream,"root.grantingAccess.HeapInfo",HeapInfo.class)
            ) {
                heap = (HeapInfo) objectInputStream.readObject();
            }
            Start.conf.setList(heap.getListFiles(), false, list);
        } catch (IOException | ClassNotFoundException ex2_2) {
             Start.exTreatment(ex2_2,"[2.2]");
        }
    }

    @Override
    public void onClick(ListView listView, TextView dest, Context context) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            
        });
    }

    @Override
    public void onLongClick(ListView listView, Context context) {
    }
}

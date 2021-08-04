package com.example.loader.data;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loader.R;
import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainInfo extends AppCompatActivity {
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_info);

        TextView[] values = {findViewById(R.id.typeValue), findViewById(R.id.sizeValue), findViewById(R.id.dateOfCreateValue),
                findViewById(R.id.nameValue), findViewById(R.id.dateOfChangeValue)};
        try {
            infoCall(values, getIntent().getStringExtra("name"));
        } catch (IOException | ClassNotFoundException ex2_0) {
            Start.exTreatment(ex2_0,"[2.0]");
        }
    }

    private void infoCall(TextView[] values, String name) throws IOException, ClassNotFoundException {
        Requests req = new Requests(Start.StandartQuerys.get_full
                + "&user=" + Start.user.getUserName()
                + "&name=" + name);
        req.init("simple", req);

        byte[] gotBytes = Base64.getDecoder().decode(req.getAnswer().getBytes());

        InfoObject object;
        final String INFO_CONST="root.info.InfoObject";
        try (
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(gotBytes);
            HackedObjectInputStream objectInputStream = new HackedObjectInputStream(byteArrayInputStream,INFO_CONST,InfoObject.class)
        ) {
            object = (InfoObject) objectInputStream.readObject();
        }

        values[0].setText(object.getType());
        values[1].setText(object.getSize());
        values[2].setText(object.getCreation());
        values[3].setText(object.getName());
        values[4].setText(object.getLastChanging());

        if (object.getName().length() > 23)
            values[3].setTextSize(11);
    }
}

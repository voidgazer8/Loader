package com.example.loader.client_options.grant_mode.providing;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.loader.R;
import com.example.loader.interfaces.CallBack;
import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@RequiresApi(api = Build.VERSION_CODES.O)
public class GrantMode extends AppCompatActivity implements CallBack, Serializable {

    private Fragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grant_mode);
        fragment = new StartFragment(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentsCont, fragment);
        transaction.commit();
    }

    @Override
    public void callNewFragment(boolean stateOfPossibility, Admission admission) {
        if (stateOfPossibility) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = new NextStepFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("toBeCalled", this);
            bundle.putSerializable("toSaveAccessInfo", admission);
            fragment.setArguments(bundle);

            transaction.replace(R.id.fragmentsCont, fragment);
            transaction.commit();
        } else
            finish();
    }

    @Override
    public void callback(File chosen, Context baseContext, Admission admission) {
        byte[] encbytes = new byte[0];
        try {
            try (
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            ) {
                objectOutputStream.writeObject(admission);
                encbytes = byteArrayOutputStream.toByteArray();
            }
        } catch (IOException ex2_1) {
            Start.exTreatment(ex2_1,"[2.1]");
        }
        Requests req = new Requests(encbytes);
        req.init("serialized", req);
        if (req.getAnswer().equals("granted"))
            Start.showMessage("Теперь этот файл имеет общий доступ. Вы можете отозвать его в любое время", getBaseContext());
        else
            Start.showMessage("Что-то пошло не так(", getBaseContext());
        finish();
    }

    @Override
    public void onBackPressed() {
        if (fragment instanceof NextStepFragment) {
            ((NextStepFragment) fragment).setRootContent();
        } else
            super.onBackPressed();
    }

}

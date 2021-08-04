package com.example.loader.realization;

import android.app.Activity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.loader.R;

public class Frames {

    public void setFeature(Activity act) {
        ImageView show = act.findViewById(R.id.show);
        ImageView hide = act.findViewById(R.id.hide);
        ImageView view = act.findViewById(R.id.vis);

        EditText ordinaryPassword = act.findViewById(R.id.password);
        EditText newPassword = act.findViewById(R.id.newPassword);
        EditText oldPassword = act.findViewById(R.id.oldPassword);

        PasswordTransformationMethod method = new PasswordTransformationMethod();

        if (ordinaryPassword != null)
            ordinaryPassword.setTransformationMethod(method);
        else {
            newPassword.setTransformationMethod(method);
            oldPassword.setTransformationMethod(method);
        }

        {
            show.setOnClickListener(v -> {
                show.setVisibility(View.INVISIBLE);
                hide.setVisibility(View.VISIBLE);
                view.setBackgroundColor(act.getColor(R.color.col1));

                if (ordinaryPassword != null)
                    ordinaryPassword.setTransformationMethod(method);
                else {
                    newPassword.setTransformationMethod(method);
                    oldPassword.setTransformationMethod(method);
                }
            });
            hide.setOnClickListener(v -> {
                hide.setVisibility(View.INVISIBLE);
                show.setVisibility(View.VISIBLE);
                view.setBackgroundColor(act.getColor(R.color.col2));

                if (ordinaryPassword != null)
                    ordinaryPassword.setTransformationMethod(null);
                else {
                    newPassword.setTransformationMethod(null);
                    oldPassword.setTransformationMethod(null);
                }
            });
        }
    }
}

package com.example.loader.client_options.grant_mode.providing;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.loader.R;
import com.example.loader.client_options.grant_mode.providing.Admission;
import com.example.loader.interfaces.CallBack;
import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;

@RequiresApi(api = Build.VERSION_CODES.O)
public class StartFragment extends Fragment {

    private EditText input;
    private View[] arr;
    private final CallBack grantMode;
    private Admission admission;

    public StartFragment(CallBack grantMode) {
        this.grantMode = grantMode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.start_fragment, null);

        admission=new Admission();

        input = root.findViewById(R.id.user);
        TextView warning = root.findViewById(R.id.txt2), error = root.findViewById(R.id.error);

        Button nextStep, deny;
        nextStep = root.findViewById(R.id.continues);
        deny = root.findViewById(R.id.denied);

        arr = new View[]{warning, nextStep, deny};

        listenToInput(error);
        listenToNext(nextStep);
        listenToDeny(deny);

        return root;
    }

    private void listenToDeny(Button deny) {
        deny.setOnClickListener(v -> {
            grantMode.callNewFragment(false,null);
        });
    }

    private void listenToNext(Button nextStep) {
        nextStep.setOnClickListener(v -> {
            grantMode.callNewFragment(true,admission);
        });
    }

    private void listenToInput(TextView error) {
        input.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                String s = input.getText().toString();

                label: if (!s.equals("")) {

                    if(s.equals(Start.user.getUserName())) {
                        Start.showMessage("Хорошая шутка, bro)",getContext());
                        break label;
                    }

                    Requests req = new Requests(Start.GrantingAccessQuerys.check_user + "&user=" + s);
                    req.init("simple", req);
                    if (req.getAnswer().equals("user_exist")) {
                        setSight(arr, View.VISIBLE);
                        error.setVisibility(View.INVISIBLE);

                        admission.setAdmitted(s);
                    } else {
                        setSight(arr, View.INVISIBLE);
                        error.setVisibility(View.VISIBLE);
                    }
                } else
                    Start.showMessage("Введите имя", getContext());

            }
            return false;
        });
    }

    public static void setSight(View[] arr, int type) {
        for (View v : arr)
            v.setVisibility(type);
    }
}

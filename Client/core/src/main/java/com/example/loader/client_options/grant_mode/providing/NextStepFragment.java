package com.example.loader.client_options.grant_mode.providing;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.loader.R;
import com.example.loader.client_options.grant_mode.providing.Admission;
import com.example.loader.interfaces.CallBack;
import com.example.loader.interfaces.Clicks;
import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NextStepFragment extends Fragment implements Clicks {

    private CallBack callBack;
    private Admission admission;
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.next_step_fragment, null);
        list = root.findViewById(R.id.secondaryList);
        TextView out=root.findViewById(R.id.getOut);

        setRootContent();

        onClick(list, null, null);
        onLongClick(list, null);
        onGetOut(out);

        assert getArguments() != null;
        callBack = (CallBack) getArguments().getSerializable("toBeCalled");
        admission=(Admission)getArguments().getSerializable("toSaveAccessInfo");

        return root;
    }

    private void onGetOut(TextView out) {
        out.setOnClickListener(v -> {
            assert getActivity()!=null;
                getActivity().finish();
        });
    }

    void setRootContent() {
        Requests req = new Requests(Start.StandartQuerys.get_list + "&user=" + Start.user.getUserName());
        req.init("simple", req);
        Start.conf.setList(Start.user.getArrList(Start.user.parse(req.getAnswer())), false, list);
    }

    @Override
    public void onClick(ListView listView, TextView dest, Context context) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            TextView element = view.findViewById(R.id.name);
            String textOnElement = element.getText().toString();

            if (sendRequest(textOnElement).equals("directory")) {
                Requests req = new Requests(Start.StandartQuerys.get_list
                        + "&user=" + Start.user.getUserName()
                        + "&folder_to_get=" + textOnElement);
                req.init("simple", req);
                Start.conf.setList(Start.user.getArrList(Start.user.parse(req.getAnswer())), false, list);
            } else
                if (!textOnElement.equals("Ничего нет")){
                    admission.setFileName(textOnElement);
                    admission.setFrom(Start.user.getUserName());
                    callBack.callback(null, null,admission);
            }
        });
    }

    @Override
    public void onLongClick(ListView listView, Context context) {

    }
}

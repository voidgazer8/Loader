package com.example.loader.realization;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.loader.client_options.ordinary.Entry;
import com.example.loader.primary.Requests;
import com.example.loader.primary.Start;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Decisions extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String arg = getArguments().getString("argument");
        switch (arg) {
            case "onExit":
                return onExit();
            case "onDelete":
                return onDelete();
            default:
                return null;
        }
    }

    private Dialog onExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Покинуть аккаунт?")
                .setPositiveButton("Да", (dialog, id) -> onYes(dialog))
                .setNegativeButton("Нет", (dialog, id) -> dialog.cancel());
        return builder.create();
    }

    private Dialog onDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Удалить аккаунт? Все файлы будут потеряны")
                .setPositiveButton("Да", (dialog, id) -> onYes(dialog, Start.user.getUserName()))
                .setNegativeButton("Нет", (dialog, id) -> dialog.cancel());
        return builder.create();
    }

    private void onYes(DialogInterface dialog, String name) {
        Requests req = new Requests(Start.StandartQuerys.delete_account + "&user=" + name);
        req.init("simple",req);

        if (req.getAnswer().equals("deleted")) {
            Start.showMessage("Учетная запись удалена", getContext());
            startActivity(new Intent(this.getContext(), Entry.class));
            dialog.cancel();

            Start.conf.dischargeAll();
            Start.conf.setElementsUnblocked();
            Start.user.removeNote("remindMe");
            Start.user.removeNote("lastEntering");
        } else
            Start.showMessage("Не удалось", getContext());
    }

    private void onYes(DialogInterface dialog) {
        dialog.cancel();
        Start.conf.dischargeAll();
        Start.conf.setElementsBlocked();

        Intent intent = new Intent(getContext(), Entry.class);
        getContext().startActivity(intent);
    }
}

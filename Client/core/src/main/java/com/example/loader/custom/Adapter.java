package com.example.loader.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loader.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Adapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Item> objects;

    public Adapter(Context context, ArrayList<Item> list) {
        objects = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view = inflater.inflate(R.layout.items_view, parent, false);
        Item item = get_item(position);

        ImageView image = view.findViewById(R.id.img);
        TextView nameView = view.findViewById(R.id.name);

        String name = item.name;
        if (name.contains("[folder]")) {
            name = name.substring(0, name.indexOf("["));
            set_img(image, R.drawable.folder);
            nameView.setBackgroundColor(view.getResources().getColor(R.color.col4));
        }

        nameView.setText(name);
        byExpansion(image, view.findViewById(R.id.exp), item.name);
        return view;
    }

    Item get_item(int position) {
        return ((Item) getItem(position));
    }

    private void byExpansion(ImageView view, TextView exp, String fname) {
        boolean state = false;
        String expansion = null;
        String[] exp1 = {".txt", ".text", ".tex", ".ttf", ".sub", ".pwi", ".log", ".err", ".apt"};
        String[] exp2 = {".mp4", ".avi", ".mkv", ".mov", ".flv", ".vob", ".mpeg"};
        String[] exp3 = {".png", ".jpeg", ".jpg", ".raw", ".psd", ".gif", ".svg", ".swf"};
        String[] exp4 = {".mp3", ".aac", ".aiff", ".flac", ".wav", ".wma"};
        String[] exp5 = {".doc", ".docx"};
        String[] exp6 = {".pptx", ".ppt", ".pps", ".ppsx", ".pot", ".potx"};
        String[] exp7 = {".zip", ".jar", ".rar", ".arj", ".cab", ".tar", ".lzh"};
        try {
            expansion = fname.substring(fname.lastIndexOf('.')).toLowerCase();
            if (Arrays.asList(exp1).contains(expansion)) state = set_img(view, R.drawable.txt);
            if (Arrays.asList(exp2).contains(expansion)) state = set_img(view, R.drawable.video);
            if (Arrays.asList(exp3).contains(expansion)) state = set_img(view, R.drawable.picture);
            if (Arrays.asList(exp4).contains(expansion)) state = set_img(view, R.drawable.music);
            if (Arrays.asList(exp5).contains(expansion)) state = set_img(view, R.drawable.doc);
            if (Arrays.asList(exp6).contains(expansion)) state = set_img(view, R.drawable.pptx);
            if (Arrays.asList(exp7).contains(expansion)) state = set_img(view, R.drawable.archive);
            if (expansion.equals(".pdf")) state = set_img(view, R.drawable.pdf);
            if (expansion.equals(".jar")) state = set_img(view, R.drawable.jar);
            if (expansion.equals(".apk")) state = set_img(view, R.drawable.apk);
            if (expansion.equals(".exe")) state = set_img(view, R.drawable.exe);
            if (expansion.equals(".torrent")) state = set_img(view, R.drawable.torrent);
            if (expansion.equals(".psd")) state = set_img(view, R.drawable.psd);
        } catch (IndexOutOfBoundsException error) {
            if (!fname.equals("Ничего нет") && !fname.contains("[folder]"))
                state = set_img(view, R.drawable.unknown);
        }

        if ((expansion == null || expansion.equals("."))
                && !fname.equals("Ничего нет")
                && !fname.contains("[folder]"))
            state = set_img(view, R.drawable.unknown);

        if (!state && !fname.contains("[folder]"))
            exp.setText(expansion);
    }

    private boolean set_img(ImageView view, int res) {
        view.setImageResource(res);
        return true;
    }

}
package com.cogeek.alarm2.ui.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cogeek.alarm2.R;

import java.util.List;

public class MusicAdapter extends ArrayAdapter<String> {
    Context context;
    int resource;
    List<String> objects;
    public MusicAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        TextView txt = view.findViewById(R.id.txt_music_name);
        txt.setText(objects.get(position));

        return view;
    }
}

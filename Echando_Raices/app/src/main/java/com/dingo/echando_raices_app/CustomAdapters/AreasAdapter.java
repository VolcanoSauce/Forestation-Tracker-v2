package com.dingo.echando_raices_app.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dingo.echando_raices_app.Models.Area;
import com.dingo.echando_raices_app.R;

import java.util.ArrayList;

public class AreasAdapter extends ArrayAdapter<Area> {
    public AreasAdapter(Context context, ArrayList<Area> areas) {
        super(context, 0, areas);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Area area = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_area, parent, false);

        TextView tvName = (TextView)convertView.findViewById(R.id.item_area_tv_name);

        if(area != null)
            tvName.setText(area.getName());

        return convertView;
    }

}

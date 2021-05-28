package com.dingo.echando_raices_app.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dingo.echando_raices_app.Models.PlantType;
import com.dingo.echando_raices_app.R;

import java.util.ArrayList;

public class PlantTypesAdapter extends ArrayAdapter<PlantType> {
    public PlantTypesAdapter(Context context, ArrayList<PlantType> plantTypes) {
        super(context, 0, plantTypes);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        PlantType plantType = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_plant_type , parent, false);

        TextView tvName = (TextView)convertView.findViewById(R.id.ipt_tvName);
        if(plantType != null)
            tvName.setText(plantType.getName());

        return convertView;
    }
}

package com.dingo.echando_raices_app.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dingo.echando_raices_app.Models.AreaType;
import com.dingo.echando_raices_app.R;

import java.util.ArrayList;

public class AreaTypesAdapter extends ArrayAdapter<AreaType> {
    public AreaTypesAdapter(Context context, ArrayList<AreaType> areaTypes) {
        super(context, 0, areaTypes);
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
        AreaType areaType = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_area_type, parent, false);

        TextView tvName = (TextView)convertView.findViewById(R.id.iat_tvName);

        if(areaType != null)
            tvName.setText(areaType.getName());

        return convertView;
    }
}

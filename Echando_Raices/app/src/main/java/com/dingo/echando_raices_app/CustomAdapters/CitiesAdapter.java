package com.dingo.echando_raices_app.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dingo.echando_raices_app.Models.City;
import com.dingo.echando_raices_app.R;

import java.util.ArrayList;

public class CitiesAdapter extends ArrayAdapter<City> {
    public CitiesAdapter(Context context, ArrayList<City> cities) {
        super(context, 0, cities);
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
        City city = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city, parent, false);

        TextView tvName = (TextView)convertView.findViewById(R.id.ic_tvName);

        if(city != null)
            tvName.setText(city.getName());

        return convertView;
    }
}

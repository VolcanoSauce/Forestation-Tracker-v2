package com.dingo.echando_raices_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AreaFragment extends Fragment {
    String position;
    int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_space, container, false);

        TextView tv_spaceName = (TextView) view.findViewById(R.id.tv_spaceName);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            position = bundle.getString("item_key");
            id = bundle.getInt("id_key");
        }

        tv_spaceName.setText(position);

        return view;
    }
}
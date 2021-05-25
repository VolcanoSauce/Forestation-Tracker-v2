package com.dingo.echando_raices_app;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyForestationsFragment extends Fragment implements AdapterView.OnItemClickListener{
    private String tree_sapces[] = {"Universidad Autonoma de Baja California", "Preparatoria Federal Lazaro Cardenas",
            "Tecnologico de Tijuana", "COBACH Tijuana"};
    private String tree_ctd[] = {"5", "2", "10", "8"};
    private String tree_type[] = {"Arbusto", "Arbusto", "Arbusto", "Arbol"};
    double[] latitudes = {32.532574, 32.546138, 32.509015, 32.530127};
    double[] longitudes = {-116.965011, -116.976379, -116.992868, -117.023896};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_forestations, container, false);

        ListView lv_myForestations = (ListView) view.findViewById(R.id.lv_myForestations);

//        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.plants_array, android.R.layout.simple_list_item_1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, tree_sapces);

        lv_myForestations.setAdapter(adapter);

        lv_myForestations.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        String space = adapter.getItemAtPosition(position).toString();
        String ctd = tree_ctd[position];
        String type = tree_type[position];
        double latitude = latitudes[position];
        double longitude = longitudes[position];

        Bundle bundle = new Bundle();
        bundle.putInt("id_key", position);
        bundle.putString("space_key", space);
        bundle.putString("ctd_key", ctd);
        bundle.putString("type_key", type);
        bundle.putDouble("latitude_key", latitude);
        bundle.putDouble("longitude_key", longitude);

        Fragment fragment = new ForestationFragment();
        fragment.setArguments(bundle);
        MyForestationsFragment.this.getFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
    }
}
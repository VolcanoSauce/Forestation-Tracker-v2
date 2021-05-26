package com.dingo.echando_raices_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MySpacesFragment extends Fragment implements AdapterView.OnItemClickListener{

    private String spaces[] = {"Universidad Autonoma de Baja California", "Preparatoria Federal Lazaro Cardenas",
                                "Tecnologico de Tijuana", "COBACH Tijuana", "COBACH Rosarito"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_spaces, container, false);

        ListView lv_mySpaces = (ListView) view.findViewById(R.id.lv_mySpaces);

//        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.spaces_array, android.R.layout.simple_list_item_1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, spaces);

        lv_mySpaces.setAdapter(adapter);

        lv_mySpaces.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        String item = adapter.getItemAtPosition(position).toString();


        Bundle bundle = new Bundle();
        bundle.putString("item_key", item);
        bundle.putInt("id_key", position);


        Fragment fragment = new SpaceFragment();
        fragment.setArguments(bundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment_container, fragment).addToBackStack(null).commit();
    }
}
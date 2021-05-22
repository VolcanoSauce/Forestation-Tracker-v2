package com.dingo.echando_raices_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddSpaceFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner sp_addTreeCity;

    private ArrayAdapter<CharSequence> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_space, container, false);

        Spinner sp_addSpaceType = (Spinner) view.findViewById(R.id.sp_addSpaceType);
        Spinner sp_addTreeState = (Spinner) view.findViewById(R.id.sp_addSpaceState);

        sp_addTreeCity = (Spinner) view.findViewById(R.id.sp_addSpaceCity);

        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spaces_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_addSpaceType.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.states_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_addTreeState.setAdapter(adapter);

        sp_addTreeState.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onItemSelected(AdapterView<?> spinner, View view, int position, long id) {
        switch ((int) spinner.getSelectedItemId()){
            case 0: adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.citiesBC_array, android.R.layout.simple_spinner_item);
                    break;
            case 1: adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.citiesTamaulipas_array, android.R.layout.simple_spinner_item);
                    break;
            default:
                    break;
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_addTreeCity.setAdapter(adapter);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
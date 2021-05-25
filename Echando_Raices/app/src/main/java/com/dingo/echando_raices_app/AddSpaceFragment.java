package com.dingo.echando_raices_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dingo.echando_raices_app.CustomAdapters.AreaTypesAdapter;
import com.dingo.echando_raices_app.Models.AreaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class AddSpaceFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    String jwt;
    String user;
    int userId;
    ArrayList<AreaType> areaTypeArrayList;
    RequestQueue queue;
    private Spinner sp_addSpaceType;
    private Spinner sp_addTreeCity;

    private ArrayAdapter<CharSequence> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_space, container, false);

        queue = Volley.newRequestQueue(getContext());
        jwt = UtilitiesER.getStoredToken(getActivity());
        try {
            user = UtilitiesER.parseJwt(UtilitiesER.getStoredToken(getActivity())).getString("part_1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userId = Integer.parseInt(user.substring(user.indexOf(':') + 1, user.indexOf(',')));

        sp_addSpaceType = (Spinner) view.findViewById(R.id.sp_addSpaceType);
        Spinner sp_addTreeState = (Spinner) view.findViewById(R.id.sp_addSpaceState);
        sp_addTreeCity = (Spinner) view.findViewById(R.id.sp_addSpaceCity);


        areaTypeArrayList = new ArrayList<AreaType>();
        httpGetAreaTypes(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray areaTypesJsonArray = response.getJSONArray("area_types");
                    areaTypeArrayList = AreaType.fromJson(areaTypesJsonArray);
                    AreaTypesAdapter areaTypesAdapter = new AreaTypesAdapter(getContext(), areaTypeArrayList);
                    sp_addSpaceType.setAdapter(areaTypesAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.states_array, android.R.layout.simple_spinner_item);
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

    private void httpGetAreaTypes(VolleyCallback cb) {
        //String url = UtilitiesER.getApiBaseUrl() + "areas/props/area-types";
        String url = "http://10.0.2.2:3600/areas/props/area-types";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> cb.onSuccess(response), error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }
}
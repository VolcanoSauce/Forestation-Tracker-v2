package com.dingo.echando_raices_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dingo.echando_raices_app.CustomAdapters.AreaTypesAdapter;
import com.dingo.echando_raices_app.CustomAdapters.CitiesAdapter;
import com.dingo.echando_raices_app.Models.AreaType;
import com.dingo.echando_raices_app.Models.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddSpaceFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    String jwt;
    String user;
    int userId;
    int addressId;
    ArrayList<AreaType> areaTypeArrayList;
    ArrayList<City> cityArrayList;
    RequestQueue queue;
    private Spinner sp_addSpaceType;
    private Spinner sp_addSpaceCity;

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
        sp_addSpaceCity = (Spinner) view.findViewById(R.id.sp_addSpaceCity);
        Button btnSubmit = (Button)view.findViewById(R.id.as_btn_submit);

        areaTypeArrayList = new ArrayList<AreaType>();
        cityArrayList = new ArrayList<City>();

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

        httpGetCities(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray citiesJsonArray = response.getJSONArray("cities");
                    cityArrayList = City.fromJson(citiesJsonArray);
                    CitiesAdapter citiesAdapter = new CitiesAdapter(getContext(), cityArrayList);
                    sp_addSpaceCity.setAdapter(citiesAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

        btnSubmit.setOnClickListener(v -> {
            JSONObject jsonObj = new JSONObject();

            httpPostAddress(jsonObj, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        addressId = response.getJSONObject("createdAddress").getInt("_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onItemSelected(AdapterView<?> spinner, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void httpGetAreaTypes(VolleyCallback cb) {
        //String url = UtilitiesER.getApiBaseUrl() + "/areas/props/area-types";
        String url = "http://10.0.2.2:3600/areas/props/area-types";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> cb.onSuccess(response), error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

    private void httpGetCities(VolleyCallback cb) {
        //String url = UtilitiesER.getApiBaseUrl() + "/areas/props/cities";
        String url = "http://10.0.2.2:3600/areas/props/cities";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> cb.onSuccess(response), error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

    private void httpPostAddress(JSONObject reqJsonBody, VolleyCallback cb) {
        //String url = UtilitiesER.getApiBaseUrl() + "/areas/props/address";
        String url = "http://10.0.2.2:3600/areas/props/address";
        queue.add(UtilitiesER.verifiedHttpPostRequest(jwt, url, reqJsonBody, cb));
    }

    private void httpPostArea(JSONObject reqJsonBody, VolleyCallback cb) {
        //String url = UtilitiesER.getApiBaseUrl() + "/areas";
        String url = "http://10.0.2.2:3600/areas";
        queue.add(UtilitiesER.verifiedHttpPostRequest(jwt, url, reqJsonBody, cb));
    }

    private void httpPostUserAreaLink(JSONObject reqJsonBody, VolleyCallback cb) {
        //String url = UtilitiesER.getApiBaseUrl() + "/areas";
        String url = "http://10.0.2.2:3600/areas";
        queue.add(UtilitiesER.verifiedHttpPostRequest(jwt, url, reqJsonBody, cb));
    }

}
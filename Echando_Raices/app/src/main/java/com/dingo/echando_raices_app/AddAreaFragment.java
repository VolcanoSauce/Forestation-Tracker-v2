package com.dingo.echando_raices_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

public class AddAreaFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private String jwt;
    private String user;
    private int userId;
    private ArrayList<AreaType> areaTypeArrayList;
    private ArrayList<City> cityArrayList;
    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_area, container, false);

        queue = Volley.newRequestQueue(getContext());
        jwt = UtilitiesER.getStoredToken(getActivity());
        try {
            user = UtilitiesER.parseJwt(UtilitiesER.getStoredToken(getActivity())).getString("part_1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userId = Integer.parseInt(user.substring(user.indexOf(':') + 1, user.indexOf(',')));

        Spinner sp_addSpaceType = (Spinner) view.findViewById(R.id.sp_addSpaceType);
        Spinner sp_addSpaceCity = (Spinner) view.findViewById(R.id.sp_addSpaceCity);

        Button btnSubmit = (Button)view.findViewById(R.id.as_btn_submit);
        EditText etAreaName = (EditText)view.findViewById(R.id.et_addSpaceName);
        EditText etAddress = (EditText)view.findViewById(R.id.et_addSpaceAddress);
        EditText etEmail = (EditText)view.findViewById(R.id.et_addSpaceEmail);
        EditText etPhone = (EditText)view.findViewById(R.id.et_addSpacePhone);

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
            if(!TextUtils.isEmpty(etAreaName.getText()) && !TextUtils.isEmpty(etAddress.getText()) && !TextUtils.isEmpty(etEmail.getText()) && !sp_addSpaceCity.getSelectedItem().toString().isEmpty() && !sp_addSpaceType.getSelectedItem().toString().isEmpty()) {
                JSONObject newAddressJson = new JSONObject();
                City city = (City)sp_addSpaceCity.getSelectedItem();
                AreaType areaType = (AreaType)sp_addSpaceType.getSelectedItem();

                try {
                    newAddressJson.put("address", etAddress.getText().toString().trim());
                    newAddressJson.put("city", city.getId());

                    // Create Address entry in DB
                    httpPostAddress(newAddressJson, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                int addressId = response.getJSONObject("createdAddress").getInt("_id");
                                JSONObject newAreaJson = new JSONObject();
                                newAreaJson.put("name", etAreaName.getText().toString().trim());
                                newAreaJson.put("email", etEmail.getText().toString().trim());
                                newAreaJson.put("area_type", areaType.getId());
                                newAreaJson.put("address", addressId);
                                if(!TextUtils.isEmpty(etPhone.getText()))
                                    newAreaJson.put("phone_num", etPhone.getText().toString().trim());
                                // Create Area entry in DB
                                httpPostArea(newAreaJson, new VolleyCallback() {
                                    @Override
                                    public void onSuccess(JSONObject response2) {
                                        try {
                                            int areaId = response2.getJSONObject("createdArea").getInt("_id");
                                            JSONObject newUserAreaLink = new JSONObject();
                                            newUserAreaLink.put("userId", userId);
                                            newUserAreaLink.put("areaId", areaId);
                                            // Create User-Area link entry in DB
                                            httpPostUserAreaLink(newUserAreaLink, new VolleyCallback() {
                                                @Override
                                                public void onSuccess(JSONObject response3) {
                                                    Toast.makeText(getContext(), "Espacio creado exitosamente", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onError(String error) {
                                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void onError(String error) {
                                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(String error) {
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else
                Toast.makeText(getContext(), "Información necesaria faltante", Toast.LENGTH_SHORT).show();
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
        String url = UtilitiesER.getApiBaseUrl() + "/areas/props/area-types";
        //String url = "http://10.0.2.2:3600/areas/props/area-types";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> cb.onSuccess(response), error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

    private void httpGetCities(VolleyCallback cb) {
        String url = UtilitiesER.getApiBaseUrl() + "/areas/props/cities";
        //String url = "http://10.0.2.2:3600/areas/props/cities";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> cb.onSuccess(response), error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

    private void httpPostAddress(JSONObject reqJsonBody, VolleyCallback cb) {
        String url = UtilitiesER.getApiBaseUrl() + "/areas/props/addresses";
        //String url = "http://10.0.2.2:3600/areas/props/addresses";
        queue.add(UtilitiesER.verifiedHttpPostRequest(jwt, url, reqJsonBody, cb));
    }

    private void httpPostArea(JSONObject reqJsonBody, VolleyCallback cb) {
        String url = UtilitiesER.getApiBaseUrl() + "/areas";
        //String url = "http://10.0.2.2:3600/areas";
        queue.add(UtilitiesER.verifiedHttpPostRequest(jwt, url, reqJsonBody, cb));
    }

    private void httpPostUserAreaLink(JSONObject reqJsonBody, VolleyCallback cb) {
        String url = UtilitiesER.getApiBaseUrl() + "/users/" + userId + "/areas";
        //String url = "http://10.0.2.2:3600/users/" + userId + "/areas";
        queue.add(UtilitiesER.verifiedHttpPostRequest(jwt, url, reqJsonBody, cb));
    }

}
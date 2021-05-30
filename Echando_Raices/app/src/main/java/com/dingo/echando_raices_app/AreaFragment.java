package com.dingo.echando_raices_app;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AreaFragment extends Fragment {
    private int areaId;
    private int userId;
    private int areaTypeId;
    private int addressId;
    private int cityId;
    private RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_area, container, false);

        queue = Volley.newRequestQueue(getContext());
        ImageView btn_back = (ImageView) view.findViewById(R.id.btn_back);

        TextView tvAreaName = (TextView) view.findViewById(R.id.tv_areaName);
        TextView tvAreaType = (TextView) view.findViewById(R.id.tv_areaType);
        TextView tvCity = (TextView) view.findViewById(R.id.tv_areaCity);
        TextView tvState = (TextView) view.findViewById(R.id.tv_areaState);
        TextView tvAddress = (TextView) view.findViewById(R.id.tv_areaAddress);
        TextView tvEmail = (TextView) view.findViewById(R.id.tv_areaEmail);
        TextView tvPhone = (TextView) view.findViewById(R.id.tv_areaPhone);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            areaId = bundle.getInt("areaId");
            userId = bundle.getInt("userId");
        }

        httpGetAreaById(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject areaJson = response.getJSONObject("area");
                    tvAreaName.setText(areaJson.getString("name"));
                    tvEmail.setText(areaJson.getString("email"));
                    if(!areaJson.getString("phone_num").equals("null")) {
                        tvPhone.setText(areaJson.getString("phone_num"));
                    }

                    areaTypeId = areaJson.getInt("area_type");
                    httpGetAreaTypeById(new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                tvAreaType.setText(response.getJSONObject("area_type").getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });

                    addressId = areaJson.getInt("address");
                    httpGetAddressById(new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                tvAddress.setText(response.getJSONObject("address").getString("address"));
                                cityId = response.getJSONObject("address").getInt("city");
                                httpGetCityById(new VolleyCallback() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        try {
                                            tvCity.setText(response.getJSONObject("city").getString("name"));
                                            tvState.setText(response.getJSONObject("city").getString("state"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void onError(String error) {

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(String error) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String error) {
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AreaContainerFragment();
                AreaFragment.this.getFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
            }
        });

        return view;
    }

    private void httpGetAreaById(VolleyCallback cb) {
        String url = UtilitiesER.getApiBaseUrl() + "/areas/" + areaId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> cb.onSuccess(response), error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

    private void httpGetAreaTypeById(VolleyCallback  cb) {
        String url = UtilitiesER.getApiBaseUrl() + "/areas/props/area-types/" + areaTypeId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, cb::onSuccess, error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

    private void httpGetAddressById(VolleyCallback cb) {
        String url = UtilitiesER.getApiBaseUrl() + "/areas/props/addresses/" + addressId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, cb::onSuccess, error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

    private void httpGetCityById(VolleyCallback cb) {
        String url = UtilitiesER.getApiBaseUrl() + "/areas/props/cities/" + cityId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, cb::onSuccess, error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

}
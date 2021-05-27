package com.dingo.echando_raices_app;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AreaFragment extends Fragment {
    private int areaId;
    private int userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_area, container, false);

        TextView tvAreaName = (TextView) view.findViewById(R.id.tv_areaName);
        TextView tvAreaType = (TextView) view.findViewById(R.id.tv_areaType);
        TextView tvAddress = (TextView) view.findViewById(R.id.tv_areaAddress);
        TextView tvEmail = (TextView) view.findViewById(R.id.tv_areaEmail);
        TextView tvPhone = (TextView) view.findViewById(R.id.tv_areaPhone);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            areaId = bundle.getInt("areaId");
            userId = bundle.getInt("userId");
        }

        httpGetAreaById(getContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject areaJson = response.getJSONObject("area");
                    tvAreaName.setText(areaJson.getString("name"));
                    tvAreaType.setText(areaJson.getString("area_type"));
                    tvAddress.setText(areaJson.getString("address"));
                    tvEmail.setText(areaJson.getString("email"));
                    if(!areaJson.getString("phone_num").equals("null"))
                        tvPhone.setText(areaJson.getString("phone_num"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

        return view;
    }

    private void httpGetAreaById(Context ctx, VolleyCallback cb) {
        String url = UtilitiesER.getApiBaseUrl() + "/areas/" + areaId;
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> cb.onSuccess(response), error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }
}
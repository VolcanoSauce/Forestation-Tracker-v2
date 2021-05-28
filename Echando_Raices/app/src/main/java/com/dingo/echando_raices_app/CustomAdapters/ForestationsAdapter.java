package com.dingo.echando_raices_app.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dingo.echando_raices_app.Models.Forestation;
import com.dingo.echando_raices_app.R;
import com.dingo.echando_raices_app.UtilitiesER;
import com.dingo.echando_raices_app.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ForestationsAdapter extends ArrayAdapter<Forestation> {
    public ForestationsAdapter(Context context, ArrayList<Forestation> forestations) {
        super(context, 0, forestations);
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
        Forestation forestation = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_forestation, parent, false);

        TextView tvPlantType = (TextView)convertView.findViewById(R.id.item_forestation_tv_plant);
        TextView tvPlantCount = (TextView)convertView.findViewById(R.id.item_forestation_tv_plant_count);

        if(forestation != null) {
            tvPlantCount.setText(forestation.getPlant_count());
            String url = UtilitiesER.getApiBaseUrl() + "/forestations/props/plant-types/" + forestation.getPlant_type();
            httpGetPlantTypeById(getContext(), url, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        tvPlantType.setText(response.getJSONObject("plant_type").getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(String error) {
                }
            });
        }

        return convertView;
    }

    private void httpGetPlantTypeById(Context ctx, String url, VolleyCallback cb) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> cb.onSuccess(response), error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

}

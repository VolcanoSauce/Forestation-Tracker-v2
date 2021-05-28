package com.dingo.echando_raices_app;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dingo.echando_raices_app.CustomAdapters.ForestationsAdapter;
import com.dingo.echando_raices_app.Models.Area;
import com.dingo.echando_raices_app.Models.Forestation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyForestationsFragment extends Fragment implements AdapterView.OnItemClickListener {
    private String jwt;
    private String user;
    private int userId;
    private ArrayList<Forestation> forestationArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_forestations, container, false);

        jwt = UtilitiesER.getStoredToken(getActivity());
        try {
            user = UtilitiesER.parseJwt(UtilitiesER.getStoredToken(getActivity())).getString("part_1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userId = Integer.parseInt(user.substring(user.indexOf(':') + 1, user.indexOf(',')));

        forestationArrayList = new ArrayList<>();
        httpGetMyForestations(getContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray forestationsJsonArray = response.getJSONArray("forestations");
                    forestationArrayList = Forestation.fromJson(forestationsJsonArray);
                    ForestationsAdapter adapter = new ForestationsAdapter(getContext(), forestationArrayList);
                    ListView lv_myForestations = (ListView) view.findViewById(R.id.lv_myForestations);
                    lv_myForestations.setAdapter(adapter);
                    lv_myForestations.setOnItemClickListener(getSelf());
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

    private MyForestationsFragment getSelf() {
        return this;
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        Forestation item = (Forestation)adapter.getItemAtPosition(position);

        Bundle bundle = new Bundle();
        bundle.putInt("forestationId", item.getId());
        bundle.putInt("userId", userId);

        Fragment fragment = new ForestationFragment();
        fragment.setArguments(bundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment_container, fragment).addToBackStack(null).commit();
    }

    private void httpGetMyForestations(Context ctx, VolleyCallback cb) {
        String url = UtilitiesER.getApiBaseUrl() + "/users/" + userId + "/forestations";
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> cb.onSuccess(response), error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

}
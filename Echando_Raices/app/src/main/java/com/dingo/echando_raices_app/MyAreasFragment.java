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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dingo.echando_raices_app.CustomAdapters.AreasAdapter;
import com.dingo.echando_raices_app.Models.Area;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyAreasFragment extends Fragment implements AdapterView.OnItemClickListener{
    private String jwt;
    private String user;
    private int userId;
    private ArrayList<Area> areaArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_areas, container, false);

        jwt = UtilitiesER.getStoredToken(getActivity());
        try {
            user = UtilitiesER.parseJwt(UtilitiesER.getStoredToken(getActivity())).getString("part_1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userId = Integer.parseInt(user.substring(user.indexOf(':') + 1, user.indexOf(',')));

        areaArrayList = new ArrayList<>();
        httpGetMyAreas(getContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray areasJsonArray = response.getJSONArray("areas");
                    areaArrayList = Area.fromJson(areasJsonArray);
                    AreasAdapter adapter = new AreasAdapter(getContext(), areaArrayList);
                    ListView lv_mySpaces = (ListView) view.findViewById(R.id.lv_myAreas);
                    lv_mySpaces.setAdapter(adapter);
                    lv_mySpaces.setOnItemClickListener(getSelf());
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

    private MyAreasFragment getSelf() {
        return this;
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        Area item = (Area) adapter.getItemAtPosition(position);

        Bundle bundle = new Bundle();
        bundle.putInt("areaId", item.getId());
        bundle.putInt("userId", userId);

        Fragment fragment = new AreaFragment();
        fragment.setArguments(bundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment_container, fragment).addToBackStack(null).commit();
    }

    private void httpGetMyAreas(Context ctx, VolleyCallback cb) {
        String url = UtilitiesER.getApiBaseUrl() + "/users/" + userId + "/areas";
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> cb.onSuccess(response), error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }
}
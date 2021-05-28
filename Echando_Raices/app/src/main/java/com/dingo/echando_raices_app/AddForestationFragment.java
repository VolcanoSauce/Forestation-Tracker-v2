package com.dingo.echando_raices_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dingo.echando_raices_app.CustomAdapters.AreasAdapter;
import com.dingo.echando_raices_app.CustomAdapters.PlantTypesAdapter;
import com.dingo.echando_raices_app.Models.Area;
import com.dingo.echando_raices_app.Models.AreaType;
import com.dingo.echando_raices_app.Models.PlantType;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddForestationFragment extends Fragment implements OnMapReadyCallback {
    private String jwt;
    private String user;
    private int userId;
    private LatLng currCoords;
    private ArrayList<Area> areaArrayList;
    private ArrayList<PlantType> plantTypeArrayList;
    private RequestQueue queue;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_forestation, container, false);

        queue = Volley.newRequestQueue(getContext());
        jwt = UtilitiesER.getStoredToken(getActivity());
        try {
            user = UtilitiesER.parseJwt(UtilitiesER.getStoredToken(getActivity())).getString("part_1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userId = Integer.parseInt(user.substring(user.indexOf(':') + 1, user.indexOf(',')));

        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        ImageView ivMapTransparent = (ImageView) view.findViewById(R.id.ivMapTransparent);

        Spinner spPlantType = (Spinner) view.findViewById(R.id.sp_addForestationPlantType);
        Spinner spForestationArea = (Spinner) view.findViewById(R.id.sp_addForestationArea);
        TextView tvEncargado = (TextView) view.findViewById(R.id.tv_addForestationUser);
        EditText etPlantCount = (EditText) view.findViewById(R.id.et_addForestationCount);
        Button btnSubmit = (Button) view.findViewById(R.id.btn_addForestation);

        areaArrayList = new ArrayList<>();
        plantTypeArrayList = new ArrayList<>();

        httpGetValues(UtilitiesER.getApiBaseUrl() + "/users/" + userId, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject userInfo = response.getJSONObject("user");
                    String fullName = userInfo.getString("name") + " " + userInfo.getString("last_name");
                    tvEncargado.setText(fullName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String error) {
            }
        });

        httpGetValues(UtilitiesER.getApiBaseUrl() + "/forestations/props/plant-types", new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray plantTypesJsonArray = response.getJSONArray("plant_types");
                    plantTypeArrayList = PlantType.fromJson(plantTypesJsonArray);
                    PlantTypesAdapter plantTypesAdapter = new PlantTypesAdapter(getContext(), plantTypeArrayList);
                    spPlantType.setAdapter(plantTypesAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String error) {
            }
        });

        httpGetValues(UtilitiesER.getApiBaseUrl() + "/users/" + userId + "/areas", new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray areasJsonArray = response.getJSONArray("areas");
                    areaArrayList = Area.fromJson(areasJsonArray);
                    AreasAdapter areasAdapter = new AreasAdapter(getContext(), areaArrayList);
                    spForestationArea.setAdapter(areasAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String error) {
            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.add_map);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        // Disable Scrollview on Map (Zoom)
        ivMapTransparent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                    case MotionEvent.ACTION_MOVE:
                        // Disallow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    default:
                        return true;
                }
            }
        });
        btnSubmit.setOnClickListener(v -> {
            if(currCoords != null && !TextUtils.isEmpty(etPlantCount.getText()) && !spForestationArea.getSelectedItem().toString().isEmpty() && !spPlantType.getSelectedItem().toString().isEmpty()) {
                try {
                    PlantType plantType = (PlantType)spPlantType.getSelectedItem();
                    Area area = (Area)spForestationArea.getSelectedItem();
                    JSONObject coords = new JSONObject();
                    JSONObject newForestationJson = new JSONObject();
                    newForestationJson.put("plant_count", Integer.parseInt(etPlantCount.getText().toString().trim()));
                    newForestationJson.put("plant_type", plantType.getId());
                    newForestationJson.put("userId", userId);
                    newForestationJson.put("areaId", area.getId());
                    coords.put("x", currCoords.latitude);
                    coords.put("y", currCoords.longitude);
                    newForestationJson.put("coords", coords);

                    httpPostForestation(newForestationJson, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Toast.makeText(getContext(), "Forestacion Agregada", Toast.LENGTH_SHORT).show();
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
        });


        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng tijuana = new LatLng(32.5027, -117.00371);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tijuana, 11));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @SuppressLint({"MissingPermission", "SetTextI18n"})
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                currCoords = new LatLng(markerOptions.getPosition().latitude, markerOptions.getPosition().longitude);
                //markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                googleMap.addMarker(markerOptions);
            }
        });
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
    }

    private void httpGetValues(String url, VolleyCallback cb) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> cb.onSuccess(response), error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

    private void httpPostForestation(JSONObject reqJsonBody, VolleyCallback cb) {
        String url = UtilitiesER.getApiBaseUrl() + "/forestations";
        queue.add(UtilitiesER.verifiedHttpPostRequest(jwt, url, reqJsonBody, cb));
    }

}
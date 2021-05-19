package com.dingo.echando_raices_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback{
    double[] latitudes = {32.532574, 32.546138, 32.509015, 32.530127};
    double[] longitudes = {-116.965011, -116.976379, -116.992868, -117.023896};
    String[] plants = {"UABC", "Aeropuerto", "Hipodromo", "CECUT"};
    JSONObject forestations;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        forestations = new JSONObject();
        setForestations(getContext());

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng tijuana = new LatLng(32.5027, -117.00371);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tijuana,11));

        for(int i=0 ; i<latitudes.length ; i++) {
            LatLng store = new LatLng(latitudes[i], longitudes[i]);
            googleMap.addMarker(new MarkerOptions().position(store).title(plants[i])
                    .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_plant)));
        }

        googleMap.setOnMarkerClickListener(this);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                googleMap.addMarker(markerOptions.icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_plant)));
            }
        });
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();

        return false;
    }

    public void setForestations(Context ctx) {
        String url = "http://10.0.2.2:3600/forestations";
        //String url = UtilitiesER.getApiBaseUrl() + "forestations";
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                forestations = response;
                try {
                    Log.d("MyResponse", forestations.getJSONArray("forestations").getJSONObject(0).getJSONObject("coords").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MyErrorResponse", error.toString());
            }
        });
        queue.add(jsonArrayRequest);
    }
}
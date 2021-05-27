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

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

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

public class AddForestationFragment extends Fragment implements OnMapReadyCallback {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_forestation, container, false);

        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        ImageView ivMapTransparent = (ImageView) view.findViewById(R.id.ivMapTransparent);

        Spinner sp_addTreePlant = (Spinner) view.findViewById(R.id.sp_addTreePlant);
        Spinner sp_addTreeSpace = (Spinner) view.findViewById(R.id.sp_addTreeSpace);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.plants_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_addTreePlant.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spaces_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_addTreeSpace.setAdapter(adapter);

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
                        // Disallow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
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
                //markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                googleMap.addMarker(markerOptions);

                //tv_lat.setText(Double.toString(latLng.latitude));
                //tv_lng.setText(Double.toString(latLng.longitude));
            }
        });
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
    }

}
package com.dingo.echando_raices_app;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Objects;

public class AddTreeFragment extends Fragment implements OnMapReadyCallback {
    TextView tv_lat;
    TextView tv_lng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_tree, container, false);

        EditText et_dateTree = (EditText) view.findViewById(R.id.et_dateTree);

        et_dateTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Objects.requireNonNull(getContext()), android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month++;
                        et_dateTree.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });

        tv_lat = (TextView) view.findViewById(R.id.tv_lat);
        tv_lng = (TextView) view.findViewById(R.id.tv_lng);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.add_map);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        return view;
    }

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
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                googleMap.addMarker(markerOptions);

                tv_lat.setText(Double.toString(latLng.latitude));
                tv_lng.setText(Double.toString(latLng.longitude));
            }
        });
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
    }




}
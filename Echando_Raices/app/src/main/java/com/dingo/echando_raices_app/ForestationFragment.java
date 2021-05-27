package com.dingo.echando_raices_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class ForestationFragment extends Fragment implements OnMapReadyCallback {
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;

    String space, ctd, type;
    int id;
    double latitude, longitude;

    ImageView image_tree;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forestation, container, false);

        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        ImageView ivMapTransparent = (ImageView) view.findViewById(R.id.ivMapTransparent);

        ImageView btn_back = (ImageView) view.findViewById(R.id.btn_back);

        Button btn_camera = (Button) view.findViewById(R.id.btn_camera);
        image_tree = (ImageView) view.findViewById(R.id.img_tree);

        TextView tv_treeSpace = (TextView) view.findViewById(R.id.tv_treeSpace);
        TextView tv_treeCntd = (TextView) view.findViewById(R.id.tv_treeCtd);
        TextView tv_treeType = (TextView) view.findViewById(R.id.tv_treePlant);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            space = bundle.getString("space_key");
            id = bundle.getInt("id_key");
            ctd = bundle.getString("ctd_key");
            type = bundle.getString("type_key");
            latitude = bundle.getDouble("latitude_key");
            longitude = bundle.getDouble("longitude_key");
        }

        tv_treeSpace.setText(space);
        tv_treeCntd.setText(ctd);
        tv_treeType.setText(type);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.tree_map);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionCamera();
            }
        });

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

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ForestationContainerFragment();
                ForestationFragment.this.getFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
            }
        });

        return view;
    }

    private void permissionCamera(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        else
            openCamera();
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openCamera();
            else
                Toast.makeText(getContext(), "Permiso de la camara es requerida!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Bitmap image = (Bitmap) data.getExtras().get("data");

            image_tree.setBackgroundResource(0);
            image_tree.setImageBitmap(image);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng tijuana = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tijuana,14));

        googleMap.addMarker(new MarkerOptions().position(tijuana).icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_plant)));

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
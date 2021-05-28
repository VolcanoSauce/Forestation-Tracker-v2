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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;


public class ForestationFragment extends Fragment implements OnMapReadyCallback {
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;

    private int forestationId;
    private int userId;
    private LatLng currCoords;
    private ImageView image_tree;
    private RequestQueue queue;

    private TextView tv_treeUser;
    private TextView tv_treeSpace;
    private TextView tv_treeCount;
    private TextView tv_treeType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forestation, container, false);

        queue = Volley.newRequestQueue(getContext());

        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        ImageView ivMapTransparent = (ImageView) view.findViewById(R.id.ivMapTransparent);

        ImageView btn_back = (ImageView) view.findViewById(R.id.btn_back);

        Button btn_camera = (Button) view.findViewById(R.id.btn_camera);
        image_tree = (ImageView) view.findViewById(R.id.img_tree);

        tv_treeUser = (TextView) view.findViewById(R.id.tv_treeUser);
        tv_treeSpace = (TextView) view.findViewById(R.id.tv_treeSpace);
        tv_treeCount = (TextView) view.findViewById(R.id.tv_treeCtd);
        tv_treeType = (TextView) view.findViewById(R.id.tv_treePlant);

        currCoords = new LatLng(0, 0);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            forestationId = bundle.getInt("forestationId");
            userId = bundle.getInt("userId");
            httpGetValues(UtilitiesER.getApiBaseUrl() + "/users/" + userId, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject user = response.getJSONObject("user");
                        String fullname = user.getString("name") + " " + user.getString("last_name");
                        tv_treeUser.setText(fullname);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(String error) {
                }
            });
        }

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

        btn_back.setOnClickListener(v -> {
            Fragment fragment = new ForestationContainerFragment();
            ForestationFragment.this.getFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
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
        httpGetValues(UtilitiesER.getApiBaseUrl() + "/forestations/" + forestationId, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject forestation = response.getJSONObject("forestation");
                    tv_treeCount.setText(forestation.getString("plant_count"));

                    double latitude = forestation.getJSONObject("coords").getDouble("x");
                    double longitude = forestation.getJSONObject("coords").getDouble("y");
                    currCoords = new LatLng(latitude, longitude);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currCoords,14));
                    googleMap.addMarker(new MarkerOptions().position(currCoords).icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_plant)));
                    UiSettings uiSettings = googleMap.getUiSettings();
                    uiSettings.setZoomControlsEnabled(true);

                    httpGetValues(UtilitiesER.getApiBaseUrl() + "/areas/" + forestation.getString("areaId"), new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                JSONObject area = response.getJSONObject("area");
                                tv_treeSpace.setText(area.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(String error) {
                        }
                    });

                    httpGetValues(UtilitiesER.getApiBaseUrl() + "/forestations/props/plant-types/" + forestation.getString("plant_type"), new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                JSONObject plantType = response.getJSONObject("plant_type");
                                tv_treeType.setText(plantType.getString("name"));
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
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void httpGetValues(String url, VolleyCallback cb) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> cb.onSuccess(response), error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

}
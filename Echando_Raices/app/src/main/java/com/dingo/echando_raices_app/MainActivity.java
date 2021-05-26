package com.dingo.echando_raices_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // SET USER INFO IN NAV HEADER
        View navHeader = nv.getHeaderView(0);
        TextView tvEmail = navHeader.findViewById(R.id.navh_email);
        TextView tvUsername = navHeader.findViewById(R.id.navh_username);

        try {
            String userInfo = UtilitiesER.parseJwt(UtilitiesER.getStoredToken(this)).getString("part_1");
            int userId = Integer.parseInt(userInfo.substring(userInfo.indexOf(':') + 1, userInfo.indexOf(',')));
            setUserInfo(userId, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        String username = response.getJSONObject("user").getString("name");
                        if(!username.equals("null") && !username.isEmpty()) {
                            String lastName = response.getJSONObject("user").getString("last_name");
                            if(!lastName.equals("null") && !lastName.isEmpty())
                                username = username.concat(" " + lastName);
                            tvUsername.setText(username);
                        }
                        tvEmail.setText(response.getJSONObject("user").getString("email"));
                    } catch (JSONException e) { e.printStackTrace(); }
                }

                @Override
                public void onError(String error) {
                    Log.d("MyUserInfoError", error);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new HomeFragment()).commit();
            nv.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_area:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new AreaContainerFragment()).commit();
                break;
            case R.id.nav_add_tree:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new AddForestationFragment()).commit();
                break;
            case R.id.nav_my_forestations:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new MyForestationsFragment()).commit();
                break;
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new MapFragment()).commit();
                break;
            case R.id.nav_about:
                Toast.makeText(this, "0.1.0", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_exit:
                logout();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer((GravityCompat.START));
        } else {
            super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    private void setUserInfo(int userId, VolleyCallback callback) {
        String url = UtilitiesER.getApiBaseUrl() + "/users/" + userId;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, callback::onSuccess, error -> callback.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

    public void logout() {
        UtilitiesER.setStoredToken("", this);
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
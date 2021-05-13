package com.dingo.echando_raices_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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

        View navHeader = nv.getHeaderView(0);
        // cambiar texto en header

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
            case R.id.nav_add_tree:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new AddTreeFragment()).commit();
                break;
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new MapFragment()).commit();
                break;
            case R.id.nav_version:
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

   public void logout() {
       // USAR FUNCION setStoredToken cuando este la clase de Utils
       SharedPreferences prefs = this.getSharedPreferences("ECHANDO_RAICES_APP", Context.MODE_PRIVATE);
       prefs.edit().putString("JWT", "").apply(); // Borrar token de autenticacion (SIGUE SIENDO UN TOKEN VALIDO)
       // USAR FUNCION setStoredToken cuando este la clase de Utils

       Intent intent = new Intent(this, LoginActivity.class);
       startActivity(intent);
       Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
       finish();
   }
}
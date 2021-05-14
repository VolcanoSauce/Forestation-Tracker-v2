package com.dingo.echando_raices_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_login = (Button) findViewById(R.id.btn_login);
        TextView btn_register = (TextView) findViewById(R.id.btn_register);

        EditText etEmail = (EditText) findViewById(R.id.et_loginEmail);
        EditText etPassword = (EditText) findViewById(R.id.et_loginPassword);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* ===========USER AUTH=========== */
                //String url = "http://10.0.2.2:3600/users/login";  // <-- API at localhost
                String url = UtilitiesER.getApiBaseUrl() + "users/login";    // <-- API at AWS Host
                RequestQueue queue = Volley.newRequestQueue(v.getContext());
                JSONObject jsonAuth = new JSONObject();
                try {
                    jsonAuth.put("email", etEmail.getText().toString().trim());
                    jsonAuth.put("password", etPassword.getText().toString().trim());

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonAuth, response -> {
                        try {
                            String msg = response.getString("message");
                            if(msg.equals("Authentication Successful")) {
                                String token = response.getString("token");
                                UtilitiesER.setStoredToken(token, LoginActivity.this);
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Sesión iniciada", Toast.LENGTH_SHORT).show()); // runOnUniThread hace funcionar el toast dentro de un try-catch
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> Toast.makeText(getApplicationContext(), "Correo Electrónico o Contraseña incorrectos", Toast.LENGTH_LONG).show());

                    queue.add(jsonObjectRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /* ===========USER AUTH=========== */

                if(UtilitiesER.check4ValidToken(LoginActivity.this)) {
                    startNextActivity(MainActivity.class);
                    finish();
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(RegisterActivity.class);
            }
        });

        if(UtilitiesER.check4ValidToken(this)) {
            startNextActivity(MainActivity.class);
            finish();
        }
    }

    private void startNextActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

}

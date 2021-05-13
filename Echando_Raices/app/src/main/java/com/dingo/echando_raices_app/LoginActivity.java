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
                String url = "http://ec2-54-227-98-150.compute-1.amazonaws.com:3600/users/login";    // <-- API at AWS Host
                RequestQueue queue = Volley.newRequestQueue(v.getContext());
                JSONObject jsonAuth = new JSONObject();
                try {
                    jsonAuth.put("email", etEmail.getText().toString().trim());
                    jsonAuth.put("password", etPassword.getText().toString().trim());

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonAuth, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String msg = response.getString("message");
                                if(msg.equals("Authentication Successful")) {
                                    String token = response.getString("token");
                                    setStoredToken(token);
                                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Sesión iniciada", Toast.LENGTH_SHORT).show()); // runOnUniThread hace funcionar el toast dentro de un try-catch
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Correo Electrónico o Contraseña incorrectos", Toast.LENGTH_LONG).show();
                        }
                    });

                    queue.add(jsonObjectRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /* ===========USER AUTH=========== */

                if(check4ValidToken())  // COMENTAR ESTA LINEA PARA FORZAR ENTRADA A APP
                    startNextActivity(MainActivity.class);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(RegisterActivity.class);
            }
        });

        if(check4ValidToken())
            startNextActivity(MainActivity.class);
    }

    // TODO: MOVER ESTO A UNA CLASE DE UTILERIA
    public static JSONObject parseJwt(String token) throws JSONException {
        JSONObject rObj = new JSONObject();
        String[] parts = token.split("\\.", 0);
        for (int i = 0; i < parts.length; i++) {
            byte[] bytes = Base64.getUrlDecoder().decode(parts[i]);
            String decodedString = new String(bytes, StandardCharsets.UTF_8);
            rObj.put("part_" + i, decodedString);
        }
        return rObj;
    }

    public boolean check4ValidToken() {
        String storedToken = getStoredToken();
        if(storedToken != null) {
            JSONObject jwt;
            try {
                jwt = parseJwt(storedToken);
                String part_1 = jwt.getString("part_1");    // getJSONObject didn't work...
                long exp = Long.parseLong(part_1.substring(part_1.lastIndexOf(':') + 1, part_1.lastIndexOf('}')));
                long unixTime = System.currentTimeMillis() / 1000L;

                if(unixTime < exp) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private void startNextActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }

    private void setStoredToken(String token) {
        SharedPreferences prefs = this.getSharedPreferences("ECHANDO_RAICES_APP", Context.MODE_PRIVATE);
        prefs.edit().putString("JWT", token).apply();
    }

    private String getStoredToken() {
        SharedPreferences prefs = this.getSharedPreferences("ECHANDO_RAICES_APP", Context.MODE_PRIVATE);
       return prefs.getString("JWT", null);
    }
    // TODO: MOVER ESTO A UNA CLASE DE UTILERIA

}

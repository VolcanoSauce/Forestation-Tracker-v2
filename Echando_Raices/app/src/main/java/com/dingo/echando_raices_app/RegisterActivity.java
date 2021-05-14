package com.dingo.echando_raices_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPasswd;
    private EditText etConfPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btn_register = (Button) findViewById(R.id.btn_register);
        TextView btn_login = (TextView) findViewById(R.id.btn_login);

        etEmail = (EditText)findViewById(R.id.et_registerEmail);
        etPasswd = (EditText)findViewById(R.id.et_registerPassword);
        etConfPasswd = (EditText)findViewById(R.id.et_registerConfirmPassword);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAction();
                Toast.makeText(v.getContext(), "Registro exitoso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerAction() {
        String url = UtilitiesER.getApiBaseUrl() + "users/signup";
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();

        if(etPasswd.getText().toString().trim().equals(etConfPasswd.getText().toString().trim())) {
            try {
                jsonBody.put("email", etEmail.getText().toString().trim());
                jsonBody.put("password", etPasswd.getText().toString().trim());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, response -> {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Cuenta creada", Toast.LENGTH_SHORT).show());
                }, error -> {
                });
                queue.add(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
        }

    }
}
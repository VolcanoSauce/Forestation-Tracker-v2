package com.dingo.echando_raices_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.internal.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    String jwt;
    String user;
    RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        queue = Volley.newRequestQueue(getContext());

        EditText etEmail = (EditText)view.findViewById(R.id.et_profileEmail);
        EditText etName = (EditText)view.findViewById(R.id.et_profileName);
        EditText etLastName = (EditText)view.findViewById(R.id.et_profileLastname);
        EditText etPhone = (EditText)view.findViewById(R.id.et_profilePhone);
        EditText etPassword = (EditText)view.findViewById(R.id.et_profilePassword);
        EditText etNewPassword = (EditText)view.findViewById(R.id.et_profileNewPassword);
        EditText etConfirmNewPassword = (EditText)view.findViewById(R.id.et_profileConfirmNewPassword);

        Button btnUpdate = (Button)view.findViewById(R.id.btn_profileSubmit);

        jwt = UtilitiesER.getStoredToken(getActivity());
        try {
            user = UtilitiesER.parseJwt(UtilitiesER.getStoredToken(getActivity())).getString("part_1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int userId = Integer.parseInt(user.substring(user.indexOf(':') + 1, user.indexOf(',')));

        httpGetUser(userId, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject userObj = response.getJSONObject("user");
                    String email = userObj.getString("email");
                    String name = userObj.getString("name");
                    String lastName = userObj.getString("last_name");
                    String phone = userObj.getString("phone_num");

                    etEmail.setText(email);

                    if(!name.equals("null"))
                        etName.setText(name);

                    if(!lastName.equals("null"))
                        etLastName.setText(lastName);

                    if(!phone.equals("null"))
                        etPhone.setText(phone);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

        btnUpdate.setOnClickListener(v -> {
            JSONObject newUserData = new JSONObject();
            if(!etName.getText().toString().isEmpty()) {
                try {
                    newUserData.put("name", etName.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(!etLastName.getText().toString().isEmpty()) {
                try {
                    newUserData.put("last_name", etLastName.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(!etPhone.getText().toString().isEmpty()) {
                try {
                    newUserData.put("phone_num", etPhone.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // TODO: Verify Current Password
            if(!etNewPassword.getText().toString().isEmpty() && !etConfirmNewPassword.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                if(isNewPasswordConfirmed(etNewPassword.getText().toString().trim(), etConfirmNewPassword.getText().toString().trim())) {
                    try {
                        newUserData.put("password", etNewPassword.getText().toString().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(getContext(), "Contraseña no coincide", Toast.LENGTH_SHORT).show();
            }

            httpPatchUser(userId, newUserData, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    Toast.makeText(getContext(), "Información Actualizada", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    private boolean isNewPasswordConfirmed(String newPasswd, String confirmNewPasswd) {
        return newPasswd.equals(confirmNewPasswd);
    }

    // GET User Info
    private void httpGetUser(int userId, VolleyCallback cb) {
        String url = UtilitiesER.getApiBaseUrl() + "users/" + userId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, cb::onSuccess, error -> cb.onError(error.toString()));
        queue.add(jsonObjectRequest);
    }

    // PATCH User Info
    private void httpPatchUser(int userId, JSONObject reqJsonBody, VolleyCallback cb) {
        String url = UtilitiesER.getApiBaseUrl() + "users/" + userId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, reqJsonBody, cb::onSuccess, error -> cb.onError(error.toString())) {
           @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> params = new HashMap<String, String>();
               return params;
           }

           @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
               Map<String, String> headers = new HashMap<String, String>();
               headers.put("Authorization", "Bearer " + jwt);
               return headers;
           }
        };
        queue.add(jsonObjectRequest);
    }
}
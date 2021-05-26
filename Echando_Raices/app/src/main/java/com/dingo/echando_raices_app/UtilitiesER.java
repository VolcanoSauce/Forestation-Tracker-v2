package com.dingo.echando_raices_app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public final class UtilitiesER {

    public static String getApiBaseUrl() {
        return "http://ec2-54-227-98-150.compute-1.amazonaws.com:3600";
    }

    public static JsonObjectRequest verifiedHttpPostRequest(String jwt, String url, JSONObject reqJsonBody, VolleyCallback cb) {
        return new JsonObjectRequest(Request.Method.POST, url, reqJsonBody, cb::onSuccess, error -> cb.onError(error.toString())) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return new HashMap<String, String>();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + jwt);
                return headers;
            }
        };
    }

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

    public static boolean check4ValidToken(Activity currActivity) {
        String storedToken = getStoredToken(currActivity);
        if(storedToken != null && !storedToken.isEmpty()) {
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

    public static void setStoredToken(String token, Activity currActivity) {
        SharedPreferences prefs = currActivity.getSharedPreferences("ECHANDO_RAICES_APP", Context.MODE_PRIVATE);
        prefs.edit().putString("JWT", token).apply();
    }

    public static String getStoredToken(Activity currActivity) {
        SharedPreferences prefs = currActivity.getSharedPreferences("ECHANDO_RAICES_APP", Context.MODE_PRIVATE);
        return prefs.getString("JWT", null);
    }

}

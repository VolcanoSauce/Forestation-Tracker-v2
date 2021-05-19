package com.dingo.echando_raices_app;

import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject response);
    void onError(String error);
}

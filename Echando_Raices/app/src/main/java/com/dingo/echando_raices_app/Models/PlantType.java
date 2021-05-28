package com.dingo.echando_raices_app.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlantType {
    private int id;
    private String name;

    public PlantType(JSONObject object) {
        try {
            this.id = object.getInt("_id");
            this.name = object.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<PlantType> fromJson(JSONArray jsonArray) {
        ArrayList<PlantType> plantTypes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                plantTypes.add(new PlantType(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return plantTypes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

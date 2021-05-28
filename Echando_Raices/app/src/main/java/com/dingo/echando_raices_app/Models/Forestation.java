package com.dingo.echando_raices_app.Models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Forestation {
    private int id;
    private int plant_count;
    private LatLng coords;
    private int plant_type;
    private int userId;
    private int areaId;

    public Forestation(int id, int plant_count, LatLng coords, int plant_type, int userId, int areaId) {
        this.id = id;
        this.plant_count = plant_count;
        this.coords = coords;
        this.plant_type = plant_type;
        this.userId = userId;
        this.areaId = areaId;
    }

    public Forestation(JSONObject object) {
        try {
            this.id = object.getInt("_id");
            this.plant_count = object.getInt("plant_count");
            this.coords = new LatLng(object.getJSONObject("coords").getDouble("x"), object.getJSONObject("coords").getDouble("y"));
            this.plant_type = object.getInt("plant_type");
            this.userId = object.getInt("userId");
            this.areaId = object.getInt("areaId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Forestation> fromJson(JSONArray jsonArray) {
        ArrayList<Forestation> forestations = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                forestations.add(new Forestation(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return forestations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlant_count() {
        return plant_count;
    }

    public void setPlant_count(int plant_count) {
        this.plant_count = plant_count;
    }

    public LatLng getCoords() {
        return coords;
    }

    public void setCoords(LatLng coords) {
        this.coords = coords;
    }

    public int getPlant_type() {
        return plant_type;
    }

    public void setPlant_type(int plant_type) {
        this.plant_type = plant_type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }
}

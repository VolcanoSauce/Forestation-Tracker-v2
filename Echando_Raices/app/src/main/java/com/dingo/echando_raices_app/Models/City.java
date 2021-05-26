package com.dingo.echando_raices_app.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class City {
    private int id;
    private String name;
    private int stateId;

    public City(JSONObject object) {
        try {
            this.id = object.getInt("_id");
            this.name = object.getString("name");
            this.stateId = object.getInt("stateId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<City> fromJson(JSONArray jsonArray) {
        ArrayList<City> cities = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                cities.add(new City(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cities;
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

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }
}

package com.dingo.echando_raices_app.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Area {
    private int id;
    private String name;
    private String email;
    private String phone_num;
    private int area_type;
    private int address;

    public Area(int id, String name, String email, String phone_num, int area_type, int address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone_num = phone_num;
        this.area_type = area_type;
        this.address = address;
    }

    public Area(JSONObject object) {
        try {
            this.id = object.getInt("_id");
            this.name = object.getString("name");
            this.email = object.getString("email");
            this.phone_num = object.getString("phone_num");
            this.area_type = object.getInt("area_type");
            this.address = object.getInt("address");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Area> fromJson(JSONArray jsonArray) {
        ArrayList<Area> areas = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                areas.add(new Area(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return areas;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phone_num;
    }

    public void setPhoneNum(String phone_num) {
        this.phone_num = phone_num;
    }

    public int getAreaType() {
        return area_type;
    }

    public void setAreaType(int area_type) {
        this.area_type = area_type;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
}

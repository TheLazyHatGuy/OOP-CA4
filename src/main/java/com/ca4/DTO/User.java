package com.ca4.DTO;

import org.json.JSONObject;

public class User {
    private int id;
    private String email;
    private String password;

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", this.id);
        jsonObject.put("email", this.email);
        jsonObject.put("password", this.password);

        return jsonObject;
    }

    public String toJSONString(){
        return toJSONObject().toString(4);
    }

    public String toJSONString(int indentFactor){
        return toJSONObject().toString(indentFactor);
    }
}

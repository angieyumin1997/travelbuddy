package com.csf.travelbuddybackend.models;

import java.io.StringReader;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Local {
    private String location;
    private String activities;
    private String description;
    private String username;
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getActivities() {
        return activities;
    }
    public void setActivities(String activities) {
        this.activities = activities;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public static Local create(String json){
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();

        final Local local = new Local();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        local.setUsername(userDetails.getUsername());
        local.setDescription(data.getString("description"));
        local.setLocation(data.getString("location"));
        local.setActivities(data.getString("activities"));
        return local;
    }

    public static Local convert(SqlRowSet rs){
        Local local = new Local();
        local.setActivities(rs.getString("activities"));
        local.setDescription(rs.getString("description"));
        local.setLocation(rs.getString("location"));
        local.setUsername(rs.getString("username"));
        return local;
    }
}

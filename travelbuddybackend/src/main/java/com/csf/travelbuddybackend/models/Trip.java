package com.csf.travelbuddybackend.models;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Trip {
    private String tripId;
    private Date start;
    private Date end;
    private String type;
    private String location;
    private String description;
    private String username;

    public String getTripId() {
        return tripId;
    }
    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Date getStart() {
        return start;
    }
    public void setStart(Date start) {
        this.start = start;
    }
    public Date getEnd() {
        return end;
    }
    public void setEnd(Date end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public static Trip create(String json) throws ParseException{
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();

        final Trip trip = new Trip();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        trip.setUsername(userDetails.getUsername());
        trip.setDescription(data.getString("description"));
        trip.setType(data.getString("type"));
        trip.setLocation(data.getString("location"));
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date start = formatter.parse(data.getString("start"));
        Date end = formatter.parse(data.getString("end"));
        trip.setStart(start);
        trip.setEnd(end);
        trip.setType(data.getString("type"));
        return trip;
    }

    public static Trip convert(SqlRowSet rs){
        Trip trip = new Trip();
        trip.setTripId(rs.getString("trip_id"));
        trip.setLocation(rs.getString("location"));
        trip.setDescription(rs.getString("description"));
        trip.setStart(rs.getDate("start"));
        trip.setEnd(rs.getDate("end"));
        trip.setType(rs.getString("type"));
        trip.setUsername(rs.getString("username"));
        return trip;
    }

}

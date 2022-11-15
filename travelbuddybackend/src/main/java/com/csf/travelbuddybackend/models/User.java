package com.csf.travelbuddybackend.models;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class User {
    private String name;
    private String username;
    private String password;
    private Date dob;
    private byte[] image;
    private String gender;
    private String country;
    private String state;
    private String city;
    private String role;
    private String interests;
    private String introduction;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static User convertUserAuth(SqlRowSet rs) {
        User user = new User();
        user.setRole(rs.getString("role"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        return user;
    }

    public static User convertUserDetails(SqlRowSet rs) {
        User user = new User();
        user.setName(rs.getString("name"));
        user.setUsername(rs.getString("username"));
        user.setDob(rs.getDate("dob"));
        user.setGender(rs.getString("gender"));
        user.setCountry(rs.getString("country"));
        user.setState(rs.getString("state"));
        user.setCity(rs.getString("city"));
        user.setInterests(rs.getString("interests"));
        user.setIntroduction(rs.getString("introduction"));
        return user;
    }

    public static User create(String json) {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();
        final User user = new User();
        user.setUsername(data.getString("username"));
        user.setPassword(data.getString("password"));
        return user;
    }

    public static User editUserDetails(String json) throws ParseException {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();
        final User user = new User();
        user.setUsername(data.getString("name"));
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = formatter.parse(data.getString("dob"));
        user.setName(data.getString("name"));
        user.setDob(date);
        user.setGender(data.getString("gender"));
        user.setCountry(data.getString("country"));
        user.setState(data.getString("state"));
        user.setCity(data.getString("city"));
        user.setInterests(data.getString("interests"));
        user.setIntroduction(data.getString("introduction"));
        return user;
    }


}

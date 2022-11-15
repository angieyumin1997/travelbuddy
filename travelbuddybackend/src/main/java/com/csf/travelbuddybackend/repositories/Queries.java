package com.csf.travelbuddybackend.repositories;

public interface Queries {

    public static final String SQL_INSERT_REGISTRATION = "insert into users (name,username,password,dob,gender,image,country,state,city,interests,introduction) value (?,?,?,?,?,?,?,?,?,?,?)";

    public static final String SQL_AUTHENTICATE_USER = "select username,password,role from users where username=?";

    public static final String SQL_GET_ACCOUNT_IMAGE = "select image from users where username=?";

    public static final String SQL_CHECK_EXISTING_USERNAME = "select username from users where username=?";

    public static final String SQL_GET_USER_DETAILS = "select name,username,dob,gender,country,state,city,interests,introduction from users where username=?";

    public static final String SQL_INSERT_LOCAL = "insert into locals (location,activities,description,username) value (?,?,?,?)";

    public static final String SQL_INSERT_TRIP = "insert into trips (start,end,location,description,type,username) value (?,?,?,?,?,?)";

    public static final String SQL_UPDATE_USER = "update users set name=?,dob=?,gender=?,country=?,state=?,city=?,interests=?,introduction=? where username=?";

    public static final String SQL_GET_USER_TRIPS = "select * from trips where username=?";

    public static final String SQL_GET_USER_LOCAL = "select * from locals where username=?";

    public static final String SQL_DELETE_USER_TRIP = "delete from trips where trip_id=?";

    public static final String SQL_UPDATE_USER_LOCAL = "update locals set location=?,activities=?,description=? where username=?";

    public static final String SQL_DELETE_USER_LOCAL = "delete from locals where username=?";

    public static final String SQL_GET_ALL_TRIPS = "select * from trips";

    public static final String SQL_GET_ALL_LOCALS = "select * from locals";

    public static final String SQL_FILTER_TRIPS_LOCATION_DATE = "select * from trips where location like concat('%',?,'%') and ((start between ? and ?) or (end BETWEEN ? and ?) or (start <= ? and end >= ?))";

    public static final String SQL_FILTER_TRIPS_LOCATION = "select * from trips where location like concat('%',?,'%')";

    public static final String SQL_FILTER_LOCALS_BY_LOCATION_ACTIVITY = "select * from locals where location like concat('%',?,'%') and activities like concat('%',?,'%')";

    public static final String SQL_FILTER_LOCALS_BY_LOCATION = "select * from locals where location like concat('%',?,'%')";

    public static final String SQL_INSERT_TOKEN = "update users set token=? where username=?";

    public static final String SQL_GET_TOKEN = "select token from users where username=?";

}

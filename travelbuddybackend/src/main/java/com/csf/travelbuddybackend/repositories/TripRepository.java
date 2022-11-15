package com.csf.travelbuddybackend.repositories;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.csf.travelbuddybackend.models.Trip;

@Repository
public class TripRepository implements Queries {
    @Autowired
    private JdbcTemplate template;

    public boolean insertTrip(Trip trip) {
        int count = template.update(SQL_INSERT_TRIP,
                trip.getStart(),
                trip.getEnd(),
                trip.getLocation(),
                trip.getDescription(),
                trip.getType(),
                trip.getUsername());
        return count == 1;
    }

    public List<Trip> getUserTrip(String username) {
        List<Trip> trips = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(SQL_GET_USER_TRIPS,
                username);
        while (rs.next()) {
            Trip trip = Trip.convert(rs);
            trips.add(trip);
        }

        return trips;
    }

    public boolean deleteTrip(Integer tripId){
        int count = template.update(SQL_DELETE_USER_TRIP,
        tripId);
        return count == 1;
    }

    public List<Trip> getAllTrips() {
        List<Trip> trips = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(SQL_GET_ALL_TRIPS);
        while (rs.next()) {
            Trip trip = Trip.convert(rs);
            trips.add(trip);
        }
        return trips;
    }

    public List<Trip> filterTripsByLocationDate(String location, String start, String end){
        List<Trip> trips = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(SQL_FILTER_TRIPS_LOCATION_DATE, 
            location,
            start,
            end,
            start,
            end,
            start,
            end);
        while (rs.next()) {
            Trip trip = Trip.convert(rs);
            trips.add(trip);
        }
		 return trips;
    }

    public List<Trip> filterTripsByLocation(String location){
        List<Trip> trips = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(SQL_FILTER_TRIPS_LOCATION, 
            location);
        while (rs.next()) {
            Trip trip = Trip.convert(rs);
            trips.add(trip);
        }
		 return trips;
    }


}

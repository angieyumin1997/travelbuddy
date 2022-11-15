package com.csf.travelbuddybackend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csf.travelbuddybackend.models.Trip;
import com.csf.travelbuddybackend.repositories.TripRepository;

@Service
public class TripService {
    
    @Autowired TripRepository tripRepo;

    public void insertTrip(Trip trip){
        tripRepo.insertTrip(trip);
    }

    public List<Trip> getUserTrip(String username){
        return tripRepo.getUserTrip(username);
    }

    public void deleteTrip(Integer tripId){
        tripRepo.deleteTrip(tripId);
    }

    public List<Trip> getAllTrips(){
        return tripRepo.getAllTrips();
    }

    public List<Trip> filterTripsByLocationDate(String location, String start, String end){
        return tripRepo.filterTripsByLocationDate(location,start,end);
    }

    public List<Trip> filterTripsByLocation(String location){
        return tripRepo.filterTripsByLocation(location);
    }
}

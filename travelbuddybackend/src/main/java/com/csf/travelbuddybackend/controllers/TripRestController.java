package com.csf.travelbuddybackend.controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.csf.travelbuddybackend.models.MessageResponse;
import com.csf.travelbuddybackend.models.Trip;
import com.csf.travelbuddybackend.services.TripService;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class TripRestController {

    @Autowired
    private TripService tripSvc;

    @PostMapping(path = "/addtrip", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> addLocal(@RequestBody String payload) throws ParseException {
        Trip trip = Trip.create(payload);
        tripSvc.insertTrip(trip);
        return ResponseEntity.ok(new MessageResponse("Trip added successfully!"));
    }

    @GetMapping(path = "/trip/{username}")
    public ResponseEntity<List<Trip>> getUserTrip(@PathVariable String username) {
        return ResponseEntity.ok(tripSvc.getUserTrip(username));
    }

    @DeleteMapping(path = "/deletetrip/{tripId}")
    public ResponseEntity<MessageResponse> deleteUserTrip(@PathVariable Integer tripId) {
        tripSvc.deleteTrip(tripId);
        return ResponseEntity.ok(new MessageResponse("Trip deleted successfully"));
    }

    @GetMapping(path = "/alltrips")
    public ResponseEntity<List<Trip>> getAllTrips() {
        List<Trip> trips = tripSvc.getAllTrips();
        return ResponseEntity.ok(trips);
    }

    @GetMapping(path = "/filtertrips")
    public ResponseEntity<List<Trip>> filterTrips(
            @RequestParam String location, @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) throws ParseException {
        List<Trip> trips = new ArrayList<Trip>();
        if (!start.isEmpty() && !end.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date s1 = formatter.parse(start);
            Date e1 = formatter.parse(end);
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            String s2 = formatter2.format(s1);
            String e2 = formatter2.format(e1);
            trips = tripSvc.filterTripsByLocationDate(location, s2, e2);
        } else {
            trips = tripSvc.filterTripsByLocation(location);
        }

        return ResponseEntity.ok(trips);
    }
}

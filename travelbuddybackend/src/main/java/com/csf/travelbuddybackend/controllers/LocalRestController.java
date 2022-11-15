package com.csf.travelbuddybackend.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csf.travelbuddybackend.models.Local;
import com.csf.travelbuddybackend.models.MessageResponse;
import com.csf.travelbuddybackend.services.LocalService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocalRestController {

    @Autowired
    private LocalService localSvc;

    @PostMapping(path = "/addlocal", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> addLocal(@RequestBody String payload) {
        Local local = Local.create(payload);
        localSvc.insertLocal(local);
        return ResponseEntity.ok(new MessageResponse("Local added successfully!"));
    }

    @GetMapping(path = "/local/{username}")
    public ResponseEntity<Local> getUserTrip(@PathVariable String username) {
        Optional<Local> opt = localSvc.getUserLocal(username);
        Local empty = new Local();
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(empty);
        }
        Local local = opt.get();
        return ResponseEntity.ok(local);
    }

    @PostMapping(path = "/updatelocal", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> updateLocal(@RequestBody String payload) {
        Local local = Local.create(payload);
        localSvc.updateLocal(local);
        return ResponseEntity.ok(new MessageResponse("Local updated successfully!"));
    }

    @DeleteMapping(path = "/deletelocal/{username}")
    public ResponseEntity<MessageResponse> deleteUserLocal(@PathVariable String username) {
        localSvc.deleteLocal();
        return ResponseEntity.ok(new MessageResponse("Local deleted successfully"));
    }

    @GetMapping(path = "/alllocals")
    public ResponseEntity<List<Local>> getAllLocals() {
        List<Local> locals = localSvc.getAllLocals();
        return ResponseEntity.ok(locals);
    }

    @GetMapping(path = "/filterlocal")
    public ResponseEntity<List<Local>> filterLocal(
            @RequestParam String location, @RequestParam(required = false) String activity) {
        List<Local> locals = new ArrayList<Local>();
        if (activity.isEmpty()) {
            System.out.println(">>>>>>>>>>filter by location");
            locals = localSvc.filterLocalsByLocation(location);

        } else {
            System.out.println(">>>>>>>>>>filter by location and activity");

            locals = localSvc.filterLocalsByLocationActivity(location, activity);

        }
        return ResponseEntity.ok(locals);
    }
}

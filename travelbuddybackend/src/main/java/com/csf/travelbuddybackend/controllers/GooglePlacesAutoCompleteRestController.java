package com.csf.travelbuddybackend.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.JsonArray;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@RestController
@RequestMapping(path = "/api")
public class GooglePlacesAutoCompleteRestController {

    @Value("${googlePlacesAutoComplete.secret}")
    private String secret;

    private static final String URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json";

    @GetMapping(path = "/googleplacesautocomplete")
    public ResponseEntity<List<String>> googlePlacesAutoComplete(@RequestParam String input) throws IOException {
        String getPlacesAutoComplete = UriComponentsBuilder.fromUriString(URL)
        .queryParam("input", input)
        .queryParam("type", "geocode")
        .queryParam("language", "en")
        .queryParam("key",secret)
        .queryParam("offset", "3")
        .toUriString();
        
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.getForEntity(getPlacesAutoComplete, String.class);
        List<String> places = new ArrayList<String>();
        InputStream is = new ByteArrayInputStream(resp.getBody().getBytes());
        JsonReader r = Json.createReader(is);
        JsonObject o = r.readObject();
        JsonArray a1 = o.getJsonArray("predictions");
        for (int i = 0; i < a1.size(); i++){
            String place = a1.getJsonObject(i).getString("description");
            places.add(place);
        }
        return ResponseEntity.ok(places);
    }
}

package com.csf.travelbuddybackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.csf.travelbuddybackend.repositories.Queries;
import java.sql.ResultSet;

@RestController
@RequestMapping(path = "/api")
public class ImageRestController implements Queries {

    @Autowired
    private JdbcTemplate template;

    @GetMapping(path = "/auth/{username}")
    public ResponseEntity<byte[]> getPost(@PathVariable String username) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(template.query(SQL_GET_ACCOUNT_IMAGE,
                        (ResultSet rs) -> {
                            rs.next();
                            return rs.getBytes("image");
                        },
                        username));
    }
}

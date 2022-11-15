package com.csf.travelbuddybackend.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class FirebaseWebNotificationRepository implements Queries{
    @Autowired
    private JdbcTemplate template;

    public boolean insertToken(String token, String username){
        int count = template.update(SQL_INSERT_TOKEN,
        token, username);
        return count == 1;
    } 

    public Optional<String> getToken(String username){
        SqlRowSet rs = template.queryForRowSet(SQL_GET_TOKEN, username);
        String token = null;
        try{ 
            rs.next();
            token = rs.getString("token");
            return Optional.of(token);
        }catch(Exception e){
            return Optional.empty();
        }
    } 

}

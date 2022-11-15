package com.csf.travelbuddybackend.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.csf.travelbuddybackend.models.Local;

@Repository
public class LocalRepository implements Queries{
    @Autowired
    private JdbcTemplate template;

    public boolean insertLocal(Local local){
        int count = template.update(SQL_INSERT_LOCAL,
        local.getLocation(),
        local.getActivities(),
        local.getDescription(),
        local.getUsername());
        return count == 1;
    } 

    public Optional<Local> getUserLocal(String username){
        SqlRowSet rs = template.queryForRowSet(SQL_GET_USER_LOCAL,
                username);
        Local local = new Local();
        try{
            rs.next();
            local = Local.convert(rs);
            return Optional.of(local);
        }catch(Exception e){
            return Optional.empty();
        }
    }

    public boolean updateLocal(Local local){
        int count = template.update(SQL_UPDATE_USER_LOCAL,
        local.getLocation(),
        local.getActivities(),
        local.getDescription(),
        local.getUsername());
        return count == 1;
    } 

    public boolean deleteLocal(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int count = template.update(SQL_DELETE_USER_LOCAL, userDetails.getUsername());
        return count == 1;
    }

    public List<Local> getAllLocals(){
        List<Local> locals = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(SQL_GET_ALL_LOCALS);
        while (rs.next()) {
            Local local = Local.convert(rs);
            locals.add(local);
        }
		 return locals;
    }

    public List<Local> filterLocalsByLocationActivity(String location, String activity){
        List<Local> locals = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(SQL_FILTER_LOCALS_BY_LOCATION_ACTIVITY, 
            location,
            activity);
        while (rs.next()) {
            Local local = Local.convert(rs);
            locals.add(local);
        }
		 return locals;
    }

    public List<Local> filterLocalsByLocation(String location){
        List<Local> locals = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(SQL_FILTER_LOCALS_BY_LOCATION, 
            location);
        while (rs.next()) {
            Local local = Local.convert(rs);
            locals.add(local);
        }
		 return locals;
    }


    
}

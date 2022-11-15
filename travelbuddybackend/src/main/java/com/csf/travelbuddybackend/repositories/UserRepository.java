package com.csf.travelbuddybackend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.csf.travelbuddybackend.models.User;

@Repository
public class UserRepository implements Queries{
    
    @Autowired
    private JdbcTemplate template;

    public boolean insertUser(User user){
        int count = template.update(SQL_INSERT_REGISTRATION,
        user.getName(),
        user.getUsername(),
        user.getPassword(),
        user.getDob(),
        user.getGender(),
        user.getImage(),
        user.getCountry(),
        user.getState(),
        user.getCity(),
        user.getInterests(),
        user.getIntroduction());
        return count == 1;
    } 

    public User authenticateUser(String username){
        User user = new User();
        SqlRowSet rs = template.queryForRowSet(SQL_AUTHENTICATE_USER,
        username);
        rs.next();
        user = User.convertUserAuth(rs);
        return user;
    } 

    public Boolean checkExistingUsername(String username){
        SqlRowSet rs = template.queryForRowSet(SQL_CHECK_EXISTING_USERNAME,
        username);
        if(rs.next()){
            return true;
        }
        return false;
    } 

    public User getUserDetails(String username){
        User user = new User();
        SqlRowSet rs = template.queryForRowSet(SQL_GET_USER_DETAILS,
        username);
        rs.next();
        user = User.convertUserDetails(rs);
        return user;
    }

    public boolean editUser(User user,String username){
        int count = template.update(SQL_UPDATE_USER,
        user.getName(),
        user.getDob(),
        user.getGender(),
        user.getCountry(),
        user.getState(),
        user.getCity(),
        user.getInterests(),
        user.getIntroduction(),
        username);
        return count==1;
    }

}

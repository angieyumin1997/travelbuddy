package com.csf.travelbuddybackend.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.csf.travelbuddybackend.models.User;
import com.csf.travelbuddybackend.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepo;

    public void insertUser(User user){
        userRepo.insertUser(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.authenticateUser(username);

        if (user == null) {
            throw new UsernameNotFoundException("User " //
                    + username + " was not found in the database");
        }

        String role = user.getRole();

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        GrantedAuthority authority = new SimpleGrantedAuthority(role);

        grantList.add(authority);

        UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(user.getUsername(), //
        user.getPassword(),grantList);
        System.out.println(">>>>>> userDetails: " +userDetails);

        return userDetails;
    }

    public Boolean checkExistingUsername(String username){
        return userRepo.checkExistingUsername(username);
    } 

    public User getUserDetails(String username){
        return userRepo.getUserDetails(username);
    }

    public boolean editUser(User user, String username){
        return userRepo.editUser(user, username);
    }
    
}

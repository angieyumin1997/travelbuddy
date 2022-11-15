package com.csf.travelbuddybackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csf.travelbuddybackend.models.JwtResponse;
import com.csf.travelbuddybackend.models.MessageResponse;
import com.csf.travelbuddybackend.models.User;
import com.csf.travelbuddybackend.security.jwt.JwtUtils;
import com.csf.travelbuddybackend.services.UserService;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping(path = "/api", produces=MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserService userSvc;

    @Autowired
    JwtUtils jwtUtils;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path="auth/registration",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> newRegistration(
            @RequestPart MultipartFile image,
            @RequestPart String name,
            @RequestPart String username,
            @RequestPart String password,
            @RequestPart String gender,
            @RequestPart String dob,
            @RequestPart String country,
            @RequestPart(required = false) String state,
            @RequestPart(required = false) String city,
            @RequestPart String introduction,
            @RequestPart String interests) throws ParseException {

        if(userSvc.checkExistingUsername(username)){
            System.out.println("existing username");
            return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: Email is already in use!"));
        }
        byte[] buff = new byte[0];

        try {
            buff = image.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = new User();
        user.setImage(buff);
        user.setName(name);
        user.setUsername(username);
        String bycrptPassword = passwordEncoder.encode(password);
        user.setPassword(bycrptPassword);
        user.setGender(gender);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = formatter.parse(dob);
        user.setDob(date);
        user.setCountry(country);
        if(country!= null && !country.isEmpty()){
            user.setCountry(country);
        }
        if(city!= null && !city.isEmpty()){
            user.setCity(city);
        }
        user.setIntroduction(introduction);
        user.setInterests(interests);

        userSvc.insertUser(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("auth/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody String payload) {
      User user = User.create(payload);

      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
  
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateJwtToken(authentication);
      UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
  
      return ResponseEntity.ok(new JwtResponse(jwt, 
                           userDetails.getUsername(), 
                           roles));
    }

    @GetMapping("auth/user")
    public Principal user(Principal user) {
        return user;
    }

    @GetMapping("/user/{username}")
    public User getUser(@PathVariable String username) {
        User user = userSvc.getUserDetails(username);
        return user;
    }

    @PostMapping("/edituser")
    public ResponseEntity<MessageResponse> editUser(@RequestBody String json) throws ParseException{
        User user = User.editUserDetails(json);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userSvc.editUser(user, userDetails.getUsername());
        
        return ResponseEntity.ok(new MessageResponse("User edited successfully!"));
    }

}

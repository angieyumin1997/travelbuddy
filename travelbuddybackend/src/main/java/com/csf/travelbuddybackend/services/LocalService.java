package com.csf.travelbuddybackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csf.travelbuddybackend.models.Local;
import com.csf.travelbuddybackend.repositories.LocalRepository;

@Service
public class LocalService {

    @Autowired LocalRepository localRepo;

    public void insertLocal(Local local){
        localRepo.insertLocal(local);
    }

    public Optional<Local> getUserLocal(String username){
        Optional<Local> opt = localRepo.getUserLocal(username);
        if (opt.isEmpty())
        return Optional.empty();
        Local local = opt.get();
        return Optional.of(local);
    }

    public void updateLocal(Local local){
        localRepo.updateLocal(local);
    }

    public void deleteLocal(){
        localRepo.deleteLocal();
    }

    public List<Local> getAllLocals(){
        return localRepo.getAllLocals();
    }

    public List<Local> filterLocalsByLocationActivity(String location, String activity){
        return localRepo.filterLocalsByLocationActivity(location,activity);
    }

    public List<Local> filterLocalsByLocation(String location){
        return localRepo.filterLocalsByLocation(location);
    }
}

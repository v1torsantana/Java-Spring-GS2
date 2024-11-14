package com.vitor.java.gs2_spring.settings;


import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/facilities")
@Service
public class Facilities {
    @PostMapping("/register-facilities")
    public void setFacilitiesInfos(){
    }

    //GET
    @GetMapping("/show-facilities")
    public void getFacilitiesInfos(int clientID){
    }

    //PUT
    @PutMapping("/update-facilities")
    public void updateFacilitiesInfos(int clientID){

    }

    //DELETE
    @DeleteMapping("/delete-facilities")
    public void deleteFacilitiesInfos(int clientID){

    }
}

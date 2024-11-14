package com.vitor.java.gs2_spring.settings;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contracts")
@Service
public class Contracts {
    @PostMapping("/register-contracts")
    public void setContractsInfos(){
    }

    //GET
    @GetMapping("/show-contracts")
    public void getContractsInfos(int clientID){
    }

    //PUT
    @PutMapping("/update-contracts")
    public void updateContractsInfos(int clientID){

    }

    //DELETE
    @DeleteMapping("/delete-contracts")
    public void deleteContractsInfos(int clientID){

    }
}

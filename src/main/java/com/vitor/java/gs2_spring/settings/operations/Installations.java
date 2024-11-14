package com.vitor.java.gs2_spring.settings.operations;

import com.vitor.java.gs2_spring.settings.Installation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/installations")
@Service
public class Installations {
    private Map<Integer, Installation> installations_database = new HashMap<>();

    @PostMapping("/register-installation")
    public ResponseEntity<String> setInstallationInfos(@RequestBody Installation installation){
        installations_database.put(installation.getInstallation_number(), installation);
        return ResponseEntity.ok("Instalação adicionada com sucesso!");
    }

    @GetMapping("/show-installation/{installation_number}")
    public ResponseEntity<Installation> getInstallationInfos(@PathVariable int installation_number){
        Installation installation = installations_database.get(installation_number);
        if(installation != null){
            return ResponseEntity.ok(installation);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/show-installations")
    public ResponseEntity<List<Installation>> getInstallationsInfos(){
        if(installations_database.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        else{
            List<Installation> installationList = new ArrayList<>(installations_database.values());
            return ResponseEntity.ok(installationList);
        }
    }

    @PutMapping("/update-installation/{installation_number}")
    public ResponseEntity<String> updateInstallationInfos(@PathVariable int installation_number, @RequestBody Installation installation){
        if (installations_database.containsKey(installation_number)){
            installations_database.put(installation_number, installation);
            return ResponseEntity.ok("Instalação atualizada com sucesso!");
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete-installation/{installation_number}")
    public ResponseEntity<String> deleteInstallationInfos(@PathVariable int installation_number){
        if (installations_database.containsKey(installation_number)){
            installations_database.remove(installation_number);
            return ResponseEntity.ok("Instalação removida com sucesso!");
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}

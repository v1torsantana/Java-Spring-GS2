package com.vitor.java.gs2_spring.operations;

import com.vitor.java.gs2_spring.settings.Installation;
import org.springframework.http.HttpStatus;
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
        String installation_numberString = String.valueOf(installation.getInstallation_number());
        if(installation_numberString.length()==6){
            if(installation.getInstallation_CEP().length()==8){
                installations_database.put(installation.getInstallation_number(), installation);
                return ResponseEntity.ok("Instalação adicionada com sucesso!");
            }
            else{
                return ResponseEntity.badRequest().body("Número do CEP deve ter 8 dígitos");
            }
        }
        else{
            return ResponseEntity.badRequest().body("Número de instalação deve ter 6 dígitos");
        }

    }

    @GetMapping("/show-installation/{installation_number}")
    public ResponseEntity<Installation> getInstallationInfos(@PathVariable int installation_number) {
        Installation installation = installations_database.get(installation_number);
        if (installation != null) {
            return ResponseEntity.ok(installation);
        } else {
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
    public ResponseEntity<String> updateInstallationInfos(
            @PathVariable int installation_number,
            @RequestBody Installation installation) {

        String installation_numberString = String.valueOf(installation.getInstallation_number());
        if (installation_numberString.length() != 6) {
            return ResponseEntity.badRequest().body("O número da instalação deve ter 6 dígitos.");
        }

        if (installation.getInstallation_CEP().length() != 8) {
            return ResponseEntity.badRequest().body("O CEP deve ter 8 dígitos.");
        }

        if (!installations_database.containsKey(installation_number)) {
            return ResponseEntity.notFound().build();
        }

        // Atualiza os campos da instalação
        Installation existingInstallation = installations_database.get(installation_number);
        existingInstallation.setInstallation_CEP(installation.getInstallation_CEP());
        existingInstallation.setInstallation_address(installation.getInstallation_address());
        existingInstallation.setInstallation_activity(installation.isInstallation_activity());

        installations_database.put(installation_number, existingInstallation);

        return ResponseEntity.ok("Instalação atualizada com sucesso!");
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

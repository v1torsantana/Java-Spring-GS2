package com.vitor.java.gs2_spring.operations;

import com.vitor.java.gs2_spring.settings.Contract;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contracts")
@Service
public class Contracts {
    private Map<Integer, Contract> contract_database = new HashMap<>();

    @PostMapping("/register-contracts")
    public ResponseEntity<String> setContractsInfos(@RequestBody Contract contract){
        String clientIDString = String.valueOf(contract.getClientID());
        String installation_numberString = String.valueOf(contract.getInstallation_number());
        if(clientIDString.length()==5) {
            if(installation_numberString.length()==6){
                contract_database.put(contract.getInstallation_number(), contract);
                return ResponseEntity.ok("Contrato adicionado com sucesso!");
            }
            else{
                return ResponseEntity.badRequest().body("O número da instalação deve ter 6 dígitos");
            }
        }
        else{
                return ResponseEntity.badRequest().body("O ID do cliente deve ter 5 dígitos");
        }
    }

    @GetMapping("/show-contract/{installation_number}")
    public ResponseEntity<Contract> getContractInfo(@PathVariable int installation_number){
        Contract contract = contract_database.get(installation_number);
        if (contract != null) {
            return ResponseEntity.ok(contract);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/show-contracts")
    public ResponseEntity<List<Contract>> getContractsInfos(){
        if (contract_database.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        else{
            List<Contract> contractList = new ArrayList<>(contract_database.values());
            return ResponseEntity.ok(contractList);
        }
    }

    @PutMapping("/update-contracts/{installation_number}")
    public ResponseEntity<String> updateContractsInfos(
            @PathVariable int installation_number,
            @RequestBody Contract contract) {

        String clientIDString = String.valueOf(contract.getClientID());
        if (clientIDString.length() != 5) {
            return ResponseEntity.badRequest().body("O ID do cliente deve ter 5 dígitos.");
        }

        String installation_numberString = String.valueOf(installation_number);
        if (installation_numberString.length() != 6) {
            return ResponseEntity.badRequest().body("O número da instalação deve ter 6 dígitos.");
        }

        if (!contract_database.containsKey(installation_number)) {
            return ResponseEntity.notFound().build();
        }

        Contract existingContract = contract_database.get(installation_number);
        existingContract.setClientID(contract.getClientID());
        existingContract.setContract_activity(contract.isContract_activity());
        existingContract.setContract_durationMonths(contract.getContract_durationMonths());

        contract_database.put(installation_number, existingContract);

        return ResponseEntity.ok("Contrato atualizado com sucesso!");
    }


    @DeleteMapping("/delete-contract/{installation_number}")
    public ResponseEntity<String> deleteContractsInfos(@PathVariable int installation_number){
        if(contract_database.containsKey(installation_number)){
            contract_database.remove(installation_number);
            return ResponseEntity.ok("Contrato removido com sucesso!");
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}

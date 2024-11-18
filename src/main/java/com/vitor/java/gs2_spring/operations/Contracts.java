package com.vitor.java.gs2_spring.operations;

import com.vitor.java.gs2_spring.settings.Contract;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@RestController
@RequestMapping("/contracts")
@Service
public class Contracts {
    private Map<Integer, Contract> contract_database = new HashMap<>();

    @PostMapping("/register-contracts")
    public ResponseEntity<String> setContractsInfos(@RequestBody Contract contract){
        contract_database.put(contract.getInstallation_number(), contract);
        LocalDate startDate = contract.getStart_data().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        if(startDate.plusMonths(contract.getContract_durationMonths()).isBefore(LocalDate.now())){
            contract.setContract_activity(false);
            contract_database.put(contract.getInstallation_number(), contract);
        }
        return ResponseEntity.ok("Contrato adicionado com sucesso!");
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
    public ResponseEntity<String> updateContractsInfos(@PathVariable int installation_number, @RequestBody Contract contract){
        if(contract_database.containsKey(installation_number)){
            Contract existingContract = contract_database.get(installation_number);
            existingContract.setContract_activity(contract.isContract_activity());
            existingContract.setContract_durationMonths(contract.getContract_durationMonths());
            existingContract.setStart_data(contract.getStart_data());
            contract_database.put(installation_number, contract);
            return ResponseEntity.ok("Contrato atualizado com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-contract/{installation_number}")
    public ResponseEntity<String> deleteContractsInfos(@PathVariable int installation_number){
        if(contract_database.containsKey(installation_number)){
            Contract contract = contract_database.get(installation_number);
            contract.setContract_activity(false);
            return ResponseEntity.ok("Contrato removido com sucesso!");
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}

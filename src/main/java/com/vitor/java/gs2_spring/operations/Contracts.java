package com.vitor.java.gs2_spring.operations;

import com.vitor.java.gs2_spring.connection.Connect;
import com.vitor.java.gs2_spring.settings.Contract;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class Contracts extends Connect {
    Connection conn = Connect.connect();
    PreparedStatement pstmt = null;
    private Map<Integer, Contract> contract_database = new HashMap<>();

    @PostMapping("/register-contracts")
    public ResponseEntity<String> setContractsInfos(@RequestBody Contract contract) {
        if (conn != null) {
            try {
                System.out.println("Procurando pelo cliente com o ID: " + contract.getClientId());

                String checkClient = "SELECT COUNT(*) FROM clients WHERE client_id = ?";
                pstmt = conn.prepareStatement(checkClient);
                pstmt.setString(1, contract.getClientId());
                ResultSet rs = pstmt.executeQuery();

                if (rs.next() && rs.getInt(1) == 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente não encontrado.");
                }

                System.out.println("Procurando pelo cliente com o InO: " + contract.getInstallationNumber());

                String checkInstallation = "SELECT COUNT(*) FROM installations WHERE installation_number = ?";
                pstmt = conn.prepareStatement(checkInstallation);
                pstmt.setString(1, contract.getInstallationNumber());
                rs = pstmt.executeQuery();

                if (rs.next() && rs.getInt(1) == 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Instalação não encontrada.");
                }


                String sql = "INSERT INTO contracts (start_data, contract_duration, contract_activity, client_id, installation_number, contract_number) VALUES (?, ?, ?, ?, ?, ?)";

                pstmt = conn.prepareStatement(sql);
                String c_number = String.valueOf(contract.getContract_number());
                pstmt.setLong(1, contract.getTimestamp());
                pstmt.setInt(2, contract.getContract_durationMonths());
                pstmt.setBoolean(3, contract.isContract_activity());
                pstmt.setString(4, contract.getClientId());
                pstmt.setString(5, contract.getInstallationNumber());
                pstmt.setString(6, c_number);
                pstmt.executeUpdate();


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.ok("Contrato adicionado com sucesso: " + contract.getContract_number());

    }

    @GetMapping("/show-contract/{contract_number}")
    public ResponseEntity<List<Map<String, Object>>> getContractInfos(@PathVariable String contract_number) throws SQLException {
        List<Map<String, Object>> contractList = new ArrayList<>();

        String sql = "SELECT * FROM contracts WHERE contract_number=?";

        try (
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, contract_number);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> contractMap = new HashMap<>();
                    contractMap.put("contract_number", rs.getString("contract_number"));
                    contractMap.put("start_data", rs.getDate("start_data"));
                    contractMap.put("contract_duration", rs.getDate("contract_duration"));
                    contractMap.put("contract_activity", rs.getString("contract_activity"));
                    contractList.add(contractMap);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(contractList);
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

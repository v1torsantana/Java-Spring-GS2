package com.vitor.java.gs2_spring.operations;

import com.vitor.java.gs2_spring.connection.Connect;
import com.vitor.java.gs2_spring.settings.Installation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/installations")
@Service
public class Installations extends Connect {
    Connection conn = Connect.connect();
    PreparedStatement pstmt = null;
    private Map<Integer, Installation> installations_database = new HashMap<>();

    @PostMapping("/register-installation")
    public ResponseEntity<Map<String, Object>> setInstallationInfos(@RequestBody Installation installation) throws SQLException {
        Map<String, Object> response = new HashMap<>();
        if (conn != null) {
            try {
                String checkInstallation = "SELECT COUNT(*) FROM installations WHERE installation_CEP=?";
                pstmt = conn.prepareStatement(checkInstallation);
                pstmt.setString(1, installation.getInstallation_CEP());
                ResultSet rs = pstmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    response.put("error", "Instalação (CEP) já cadastrada!");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }

                    if (installation.getInstallation_CEP().length() == 8) {
                        String sql = "INSERT INTO installations (installation_number, installation_address, installation_CEP, installation_activity) VALUES (?, ?, ?, ?)";
                        String i_number = String.valueOf(installation.getInstallation_number());
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, i_number);
                        pstmt.setString(2, installation.getInstallation_address());
                        pstmt.setString(3, installation.getInstallation_CEP());
                        pstmt.setBoolean(4, true);
                        pstmt.executeUpdate();

                        response.put("installation_address", installation.getInstallation_address());
                        response.put("installation_CEP", installation.getInstallation_CEP());
                        response.put("installation_activity", true);
                        response.put("installation_id", installation.getInstallation_number());

                        return ResponseEntity.ok(response);

                    } else {
                        response.put("error", "CEP com dígitos errados!");
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                    }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        response.put("error", "Conexão com o banco falhou.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/show-installation/{installation_number}")
    public ResponseEntity<List<Map<String, Object>>> getInstallationInfos(@PathVariable String installation_number) throws SQLException {
        List<Map<String, Object>> installationList = new ArrayList<>();

        String sql = "SELECT * FROM installations WHERE installation_number=?";

        try (
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, installation_number);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> installationMap = new HashMap<>();
                    installationMap.put("installation_number", rs.getString("installation_number"));
                    installationMap.put("installation_address", rs.getString("installation_address"));
                    installationMap.put("installation_CEP", rs.getString("installation_CEP"));
                    installationMap.put("installation_activity", rs.getBoolean("installation_activity"));
                    installationList.add(installationMap);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(installationList);
    }


    @GetMapping("/show-installations")
    public ResponseEntity<List<Map<String, Object>>> getInstallationsInfos(){
        List<Map<String, Object>> installationList = new ArrayList<>();

        String sql = "SELECT * FROM installations";

        try (
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> installationMap = new HashMap<>();
                    installationMap.put("installation_number", rs.getString("installation_number"));
                    installationMap.put("installation_address", rs.getString("installation_address"));
                    installationMap.put("installation_CEP", rs.getString("installation_CEP"));
                    installationMap.put("installation_activity", rs.getBoolean("installation_activity"));
                    installationList.add(installationMap);
                }
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(installationList);
    }

    @PutMapping("/update-installation/{installation_number}")
    public ResponseEntity<String> updateInstallationInfos(@PathVariable String installation_number, @RequestBody Installation installation) throws SQLException {
        try {
            if (installation.getInstallation_CEP().length() == 8) {
                String sql = "UPDATE installations SET installation_address=?, installation_CEP=?, installation_activity=? WHERE installation_number=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, installation.getInstallation_address());
                pstmt.setString(2, installation.getInstallation_CEP());
                pstmt.setBoolean(3, true);
                pstmt.setString(4, installation_number);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CEP com dígitos errados!");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        pstmt.executeUpdate();
        return ResponseEntity.ok("Instalação atualizada com sucesso!");
    }

    @DeleteMapping("/delete-installation/{installation_number}")
    public ResponseEntity<Map<String, Object>> deleteInstallationInfos(@PathVariable String installation_number) throws SQLException {




        String select= "SELECT * FROM installations WHERE installation_number=?";
        pstmt = conn.prepareStatement(select);
        pstmt.setString(1, installation_number);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String sql = "UPDATE installations SET installation_activity=? WHERE installation_number=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, false);
            pstmt.setString(2, installation_number);
            pstmt.executeUpdate();

            Map<String, Object> installationsMap = new HashMap<>();
            installationsMap.put("installation_number", rs.getString("installation_number"));
            installationsMap.put("installation_address", rs.getString("installation_address"));
            installationsMap.put("installation_CEP", rs.getString("installation_CEP"));
            installationsMap.put("installation_activity", rs.getBoolean("installation_activity"));

            return ResponseEntity.ok(installationsMap);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }
    }
}

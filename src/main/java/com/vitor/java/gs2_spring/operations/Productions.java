package com.vitor.java.gs2_spring.operations;

import com.vitor.java.gs2_spring.connection.Connect;
import com.vitor.java.gs2_spring.settings.Production;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/production")
@Service
public class Productions extends Connect {
    Connection conn = Connect.connect();
    PreparedStatement pstmt = null;

    @PostMapping("/register-production")
    public ResponseEntity<Map<String, Object>> createProduction(@RequestBody Production production) throws SQLException {
        Map<String, Object> response = new HashMap<>();


        String check_installation = "SELECT COUNT(*) FROM installations WHERE installation_number=?";
        pstmt = conn.prepareStatement(check_installation);
        pstmt.setString(1, production.getInstallation_number());
        ResultSet rs = pstmt.executeQuery();

        if(rs.next() && rs.getInt(1)==0){
            response.put("error", "Instalação não encontrada!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }


        if (conn!= null) {
            String sql = "INSERT INTO productions (production_id, installation_number, timestamp_measuring, start_data, production_kWh) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                String id = String.valueOf(production.getProduction_id());
                Timestamp tsmeasuring = production.getTimestamp_measuring();

                pstmt.setString(1, id);
                pstmt.setString(2, production.getInstallation_number());
                pstmt.setTimestamp(3, tsmeasuring);
                pstmt.setTimestamp(4, production.getStart_data());
                pstmt.setDouble(5, production.getProduction_kWh());

                pstmt.executeUpdate();


                response.put("production_id", production.getProduction_id());
                response.put("installation_number", production.getInstallation_number());
                response.put("production_kWh", production.getProduction_id());
                response.put("timestamp_measuring", production.getTimestamp_measuring());
                response.put("start_data", production.getStart_data());

                return ResponseEntity.ok(response);


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }response.put("error", "Conexão com o banco falhou.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}

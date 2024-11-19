package com.vitor.java.gs2_spring.operations;

import com.vitor.java.gs2_spring.connection.Connect;
import com.vitor.java.gs2_spring.settings.Consumption;
import com.vitor.java.gs2_spring.settings.Production;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@RestController
@RequestMapping("/production")
@Service
public class Productions extends Connect {
    Connection conn = Connect.connect();
    PreparedStatement pstmt = null;

    @PostMapping("/register-production")
    public ResponseEntity<String> createProduction(@RequestBody Production production) {
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("Adicionado com sucesso!");
    }

}

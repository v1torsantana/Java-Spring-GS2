package com.vitor.java.gs2_spring.operations;

import com.vitor.java.gs2_spring.connection.Connect;
import com.vitor.java.gs2_spring.settings.Consumption;
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
import java.util.Map;

@RestController
@RequestMapping("/consumption")
@Service
public class Consumptions extends Connect {
    Connection conn = Connect.connect();
    PreparedStatement pstmt = null;

    @PostMapping("/register-consumption")
    public ResponseEntity<String> createConsumption(@RequestBody Consumption consumption) {
        String sql = "INSERT INTO consumptions (consumpt_id, installation_number, consumption_kWh, timestamp_measuring, start_data) VALUES (?, ?, ?, FROM_UNIXTIME(?), ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String id = String.valueOf(consumption.getConsumption_id());
            pstmt.setString(1, id);

            pstmt.setString(2, consumption.getInstallation_number());
            pstmt.setDouble(3, consumption.getConsumption_kWh());
            pstmt.setLong(4, consumption.getTimestamp_measuring());
            pstmt.setTimestamp(5, consumption.getStart_data());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("Adicionado com sucesso!");
    }
}

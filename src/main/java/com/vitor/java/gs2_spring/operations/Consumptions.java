package com.vitor.java.gs2_spring.operations;

import com.vitor.java.gs2_spring.connection.Connect;
import com.vitor.java.gs2_spring.settings.Consumption;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/consumption")
@Service
public class Consumptions extends Connect {
    Connection conn = Connect.connect();
    PreparedStatement pstmt = null;

    @PostMapping("/register-consumption")
    public ResponseEntity<Map<String, Object>> createConsumption(@RequestBody Consumption consumption) throws SQLException {
        Map<String, Object> response = new HashMap<>();
        String sql = "INSERT INTO consumptions (consumpt_id, installation_number, consumption_kWh, timestamp_measuring, start_data) VALUES (?, ?, ?, ?, ?)";

        String check_installation = "SELECT COUNT(*) FROM installations WHERE installation_number=?";
        pstmt = conn.prepareStatement(check_installation);
        pstmt.setString(1, consumption.getInstalation_number());
        ResultSet rs = pstmt.executeQuery();

        if(rs.next() && rs.getInt(1)==0){
            response.put("error", "Instalação não encontrada!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (conn!= null) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                String id = String.valueOf(consumption.getConsumption_id());
                long timestampLong = consumption.getTimestamp_measuring();
                Timestamp tsmeasuring = new Timestamp(timestampLong);

                pstmt.setString(1, id);
                pstmt.setString(2, consumption.getInstallation_number());
                pstmt.setDouble(3, consumption.getConsumption_kWh());
                pstmt.setTimestamp(4, tsmeasuring);
                pstmt.setTimestamp(5, consumption.getStart_data());

                pstmt.executeUpdate();

                response.put("consumption_id", consumption.getConsumption_id());
                response.put("installation_number", consumption.getInstallation_number());
                response.put("consumption_kWh", consumption.getConsumptionkWh());
                response.put("timestamp_measuring", consumption.getTimestamp_measuring());
                response.put("start_data", consumption.getStart_data());

                return ResponseEntity.ok(response);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }response.put("error", "Conexão com o banco falhou.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/show-month/{installation_number}")
    public ResponseEntity<Map<String, Object>> showMonthlyConsumption(@PathVariable("installation_number") String installationNumber) {
        try {
            List<Consumption> consumptions = fetchConsumptionData(installationNumber);

            if (consumptions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Sem dados"));
            }

            Map<String, Object> consumptionData = computeMonthlyConsumption(consumptions);
            return ResponseEntity.ok(consumptionData);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erro ao acessar o banco de dados"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erro inesperado: " + e.getMessage()));
        }
    }

    private List<Consumption> fetchConsumptionData(String installationNumber) throws SQLException {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        String query = "SELECT installation_number, consumption_kWh, timestamp_measuring, start_data "
                + "FROM consumptions WHERE installation_number = ? AND start_data BETWEEN ? AND ? ORDER BY start_data";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, installationNumber);
            pstmt.setTimestamp(2, Timestamp.valueOf(startOfMonth.atStartOfDay()));
            pstmt.setTimestamp(3, Timestamp.valueOf(endOfMonth.atTime(23, 59, 59)));

            ResultSet rs = pstmt.executeQuery();
            List<Consumption> consumptions = new ArrayList<>();

            while (rs.next()) {
                Consumption consumption = new Consumption(
                        rs.getString("installation_number"),
                        rs.getDouble("consumption_kWh"),
                        rs.getTimestamp("timestamp_measuring").getTime(),
                        rs.getTimestamp("start_data")
                );
                consumptions.add(consumption);
            }
            return consumptions;
        }
    }

    private Map<String, Object> computeMonthlyConsumption(List<Consumption> consumptions) {
        Consumption firstRecord = consumptions.get(0);
        Consumption lastRecord = consumptions.get(consumptions.size() - 1);

        double totalConsumption = lastRecord.getConsumption_kWh() - firstRecord.getConsumption_kWh();
        long daysRecorded = ChronoUnit.DAYS.between(
                firstRecord.getStart_data().toLocalDateTime().toLocalDate(),
                lastRecord.getStart_data().toLocalDateTime().toLocalDate()
        );

        double dailyAverageConsumption = daysRecorded > 0 ? totalConsumption / daysRecorded : totalConsumption;
        long currentTimestamp = System.currentTimeMillis() / 1000;

        String referenceMonth = firstRecord.getStart_data().toLocalDateTime().getMonth()
                .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));

        Map<String, Object> result = new HashMap<>();
        result.put("instalacao_uuid", firstRecord.getInstallation_number());
        result.put("timestamp_calculo", currentTimestamp);
        result.put("dia_referencia", String.valueOf(firstRecord.getStart_data().toLocalDateTime().getDayOfMonth()));
        result.put("mes_referencia", referenceMonth);
        result.put("ano_referencia", String.valueOf(firstRecord.getStart_data().toLocalDateTime().getYear()));
        result.put("dias_para_acabar_o_mes", String.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()))));
        result.put("consumo_mensal_medio_kwh", totalConsumption);
        result.put("consumo_diario_medio_kwh", dailyAverageConsumption);
        result.put("consumo_mensal_estimado_kwh", dailyAverageConsumption * LocalDate.now().lengthOfMonth());

        return result;
    }

}
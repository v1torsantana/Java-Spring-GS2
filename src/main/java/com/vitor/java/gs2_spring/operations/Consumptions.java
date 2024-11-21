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
    public ResponseEntity<Map<String, Object>> showMonthConsumption(@PathVariable("installation_number") String installationNumber) {
        List<Consumption> consumptions = getMonthlyConsumptionData(installationNumber);

        if (consumptions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Sem dados"));
        }

        Map<String, Object> consumptionData = calculateMonthlyConsumption(consumptions);

        return ResponseEntity.ok(consumptionData);
    }

    private List<Consumption> getMonthlyConsumptionData(String installationNumber) {
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());



        String sql = "SELECT *  FROM consumptions WHERE installation_number = ? AND start_data BETWEEN ? AND? ORDER BY start_data";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, installationNumber);
            pstmt.setTimestamp(2, Timestamp.valueOf(firstDay.atStartOfDay()));
            pstmt.setTimestamp(3, Timestamp.valueOf(lastDay.atStartOfDay().plusDays(1)));

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
        } catch (SQLException e) {
            throw new RuntimeException("Ocorreu um erro",  e);


        }
    }




    private Map<String, Object> calculateMonthlyConsumption(List<Consumption> consumptions) {
        if (consumptions.isEmpty()) {
            return Map.of("message", "Sem dados");
        }

        Consumption first = consumptions.get(0);
        Consumption last = consumptions.get(consumptions.size() - 1);

        double monthlyConsumption = last.getConsumption_kWh() - first.getConsumption_kWh();
        long daysInMonth = ChronoUnit.DAYS.between(first.getStart_data().toLocalDateTime().toLocalDate(), last.getStart_data().toLocalDateTime().toLocalDate());

        double dailyConsumption = 10.4;
        if (daysInMonth > 0) {
            dailyConsumption = monthlyConsumption / daysInMonth;
        }

        long timestampCalculation = System.currentTimeMillis() / 1000;

        String month = first.getStart_data().toLocalDateTime().getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));

        Map<String, Object> result = new HashMap<>();
        result.put("instalacao_uuid", first.getInstallation_number());
        result.put("timestamp_calculo", timestampCalculation);
        result.put("dia_referencia", String.valueOf(first.getStart_data().toLocalDateTime().getDayOfMonth()));
        result.put("mes_referencia", month);
        result.put("ano_referencia", String.valueOf(first.getStart_data().toLocalDateTime().getYear()));
        result.put("dias_para_acabar_o_mes", String.valueOf(30 - last.getStart_data().toLocalDateTime().getDayOfMonth()));
        result.put("consumo_mensal_medio_kwh", monthlyConsumption);
        result.put("consumo_diario_medio_kwh", dailyConsumption);
        result.put("consumo_mensal_estimado_kwh", dailyConsumption * daysInMonth);

        return result;
    }

}
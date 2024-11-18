package com.vitor.java.gs2_spring.settings;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

@RestController
@RequestMapping("energy")
@Service
public class CalculateConsumption {
    @GetMapping("/calculate-consumption")
    public ResponseEntity<String> calculate() throws InterruptedException {
        double totalConsumption = 0;

        List<Double> consumptions = new ArrayList<>();
        Instant timestamp = Instant.now();
        LocalDateTime dateTime = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());

        int hours = dateTime.getHour();
        double minutes = dateTime.getMinute();
        double seconds = dateTime.getSecond();

        double consumption = 800 * (seconds + 5)/3600;
        consumptions.add(consumption);
        totalConsumption+=consumption;
        System.out.println();
        for (Double i : consumptions){
            totalConsumption  += i;
        }
        return ResponseEntity.ok("ϟϟϟϟϟ Consumo: até " + hours + "h" + minutes +"m" + seconds +"s" + " : " + consumption + "Consumo total: " + totalConsumption);

    }
}
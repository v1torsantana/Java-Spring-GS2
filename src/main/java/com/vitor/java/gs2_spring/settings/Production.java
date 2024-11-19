package com.vitor.java.gs2_spring.settings;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Production {
    private UUID production_id;
    private String installation_number;
    private Timestamp timestamp_measuring;
    private Timestamp start_data;
    private double production_kWh;

    private static UUID generator_id() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    private static Timestamp timestamp_calculator() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        return Timestamp.valueOf(now);
    }
    public Production(String installation_number, double production_kWh, Timestamp timestamp_measuring, Timestamp start_data) {
        this.production_id = generator_id();
        this.installation_number = installation_number;
        this.production_kWh = production_kWh;
        this.timestamp_measuring = timestamp_measuring;
        this.start_data = timestamp_calculator();
    }

    public UUID getProduction_id() {
        return production_id;
    }

    public String getInstallation_number() {
        return installation_number;
    }

    public Timestamp getTimestamp_measuring() {
        return timestamp_measuring;
    }

    public Timestamp getStart_data() {
        return start_data;
    }

    public double getProduction_kWh() {
        return production_kWh;
    }

    public void setProduction_id(UUID production_id) {
        this.production_id = production_id;
    }

    public void setInstallation_number(String installation_number) {
        this.installation_number = installation_number;
    }

    public void setTimestamp_measuring(Timestamp timestamp_measuring) {
        this.timestamp_measuring = timestamp_measuring;
    }

    public void setStart_data(Timestamp start_data) {
        this.start_data = start_data;
    }

    public void setProduction_kWh(double production_kWh) {
        this.production_kWh = production_kWh;
    }
}
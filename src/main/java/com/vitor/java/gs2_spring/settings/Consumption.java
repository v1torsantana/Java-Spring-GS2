package com.vitor.java.gs2_spring.settings;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

public class Consumption {
    private UUID consumption_id ;
    private String installation_number;
    private long timestamp_measuring;
    private Timestamp start_data;
    private double consumption_kWh;

    private static UUID generator_id(){
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    private static Timestamp timestamp_calculator() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        return Timestamp.valueOf(now);
    }

    private static Timestamp convertToMilli(long timestampInSeconds) {
        return new Timestamp(timestampInSeconds * 1000);
    }

    public Consumption(String installation_number, double consumption_kWh, long timestamp_measuring, Timestamp start_data) {
        this.consumption_id = generator_id();
        this.installation_number = installation_number;
        this.consumption_kWh = consumption_kWh;
        this.timestamp_measuring = timestamp_measuring;
        this.start_data = timestamp_calculator();
    }

    public void setConsumption_id(UUID consumption_id) {
        this.consumption_id = consumption_id;
    }

    public String getInstalation_number() {
        return installation_number;
    }

    public void setInstalation_number(String installation_number) {
        this.installation_number = installation_number;
    }

    public double getConsumption_kWh() {
        return consumption_kWh;
    }

    public void setConsumption_kWh(double consumption_kWh) {
        this.consumption_kWh = consumption_kWh;
    }

    public long getTimestamp_measuring() {
        return timestamp_measuring;
    }

    public void setTimestamp_measuring(long timestamp_measuring) {
        this.timestamp_measuring = timestamp_measuring;
    }

    public Timestamp getStart_data() {
        return start_data;
    }

    public void setStart_data(Timestamp start_data) {
        this.start_data = start_data;
    }

    public String getInstallation_number() {
        return installation_number;
    }


    public double getConsumptionkWh() {
        return consumption_kWh;
    }

    public void setInstallation_number(String installation_number) {
        this.installation_number = installation_number;
    }

    public void setConsumptionkWh(double consumption_kWh) {
        this.consumption_kWh = consumption_kWh;
    }

    public UUID getConsumption_id() {
        return consumption_id;
    }

    public Timestamp getMedicaoTimestamp() {
        return convertToMilli(this.timestamp_measuring);
    }
}
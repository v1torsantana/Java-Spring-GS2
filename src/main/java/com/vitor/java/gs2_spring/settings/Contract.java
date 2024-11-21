package com.vitor.java.gs2_spring.settings;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.time.ZoneOffset;

public class Contract {
    private String client_ID;
    private String installation_number;
    private UUID contract_number;
    private Date start_data;
    private int contract_durationMonths;
    private boolean contract_activity;
    private Timestamp timestamp;


    private static UUID generator_id(){
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    private static Timestamp timestamp_calculator(){
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        return Timestamp.valueOf(now);
    }

    public Contract(Timestamp timestamp, Date start_data, int contract_durationMonths, boolean contract_activity) {
        this.timestamp = timestamp;
        this.contract_number = Contract.generator_id();
        this.start_data = start_data;
        this.contract_durationMonths = contract_durationMonths;
        this.contract_activity = contract_activity;
    }

    public Timestamp getTimestamp() {
        return timestamp_calculator();
    }

    public Date getStart_data() {
        return start_data;
    }

    public int getContract_durationMonths() {
        return contract_durationMonths;
    }

    public boolean isContract_activity() {
        return contract_activity;
    }

    public void setStart_data(Date start_data) {
        this.start_data = start_data;
    }

    public void setContract_durationMonths(int contract_durationMonths) {
        this.contract_durationMonths = contract_durationMonths;
    }

    public void setContract_activity(boolean contract_activity) {
        this.contract_activity = contract_activity;
    }

    public String getClient_ID() {
        return client_ID;
    }

    public String getClientId() {
        return client_ID;
    }

    public String getInstallationNumber() {
        return installation_number;
    }

    public void setClient_ID(String client_ID) {
        this.client_ID = client_ID;
    }

    public void setInstallation_number(String installation_number) {
        this.installation_number = installation_number;
    }

    public UUID getContract_number() {
        return contract_number;
    }
}

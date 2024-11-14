package com.vitor.java.gs2_spring.settings.operations;

import java.util.Date;

public class Contract {
    private int clientID;
    private int instalation_number;
    private Date start_data;
    private int contract_durationMonths;  // Duração em meses
    private boolean contract_activity;

    public Contract(int clientID, int instalation_number, Date start_data, int contract_durationMonths, boolean contract_activity) {
        this.clientID = clientID;
        this.instalation_number = instalation_number;
        this.start_data = start_data;
        this.contract_durationMonths = contract_durationMonths;
        this.contract_activity = contract_activity;
    }

    public int getClientID() {
        return clientID;
    }

    public int getInstalation_number() {
        return instalation_number;
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

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setInstalation_number(int instalation_number) {
        this.instalation_number = instalation_number;
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
}

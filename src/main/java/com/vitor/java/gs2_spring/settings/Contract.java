package com.vitor.java.gs2_spring.settings;

import java.util.Date;
import java.util.Random;

public class Contract {
    private final int clientID;
    private final int installation_number;
    private Date start_data;
    private int contract_durationMonths;
    private boolean contract_activity;

    public static int generator_installation_number(){
        int installation_no = 0;
        Random generator = new Random();
        installation_no = generator.nextInt(10000, 99999);
        return installation_no;
    }

    private static int generator_id(){
        int id = 0;
        Random generator = new Random();
        id = generator.nextInt(10000, 99999);
        return id;
    }

    public Contract(Date start_data, int contract_durationMonths, boolean contract_activity) {
        this.clientID = generator_id();
        this.installation_number = generator_installation_number();
        this.start_data = start_data;
        this.contract_durationMonths = contract_durationMonths;
        this.contract_activity = contract_activity;
    }

    public int getClientID() {
        return clientID;
    }

    public int getInstallation_number() {
        return installation_number;
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
}

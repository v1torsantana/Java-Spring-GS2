package com.vitor.java.gs2_spring.settings.operations;

public class Instalation {
    private int instalation_number;
    private String instalation_address;
    private int instalation_CEP;
    private boolean instalation_activity;

    public Instalation(int instalation_number, String instalation_address, int instalation_CEP, boolean instalation_activity) {
        this.instalation_number = instalation_number;
        this.instalation_address = instalation_address;
        this.instalation_CEP = instalation_CEP;
        this.instalation_activity = instalation_activity;
    }

    public int getInstalation_number() {
        return instalation_number;
    }

    public void setInstalation_number(int instalation_number) {
        this.instalation_number = instalation_number;
    }

    public String getInstalation_address() {
        return instalation_address;
    }

    public void setInstalation_address(String instalation_address) {
        this.instalation_address = instalation_address;
    }

    public int getInstalation_CEP() {
        return instalation_CEP;
    }

    public void setInstalation_CEP(int instalation_CEP) {
        this.instalation_CEP = instalation_CEP;
    }

    public boolean isInstalation_activity() {
        return instalation_activity;
    }

    public void setInstalation_activity(boolean instalation_activity) {
        this.instalation_activity = instalation_activity;
    }
}

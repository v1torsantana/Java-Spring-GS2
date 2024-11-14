package com.vitor.java.gs2_spring.settings;

public class Installation {
    private int installation_number;
    private String installation_address;
    private String installation_CEP;
    private boolean installation_activity;

    public Installation(int installation_number, String installation_address, String installation_CEP, boolean installation_activity) {
        this.installation_number = installation_number;
        this.installation_address = installation_address;
        this.installation_CEP = installation_CEP;
        this.installation_activity = installation_activity;
    }

    public int getInstallation_number() {
        return installation_number;
    }

    public void setInstallation_number(int installation_number) {
        this.installation_number = installation_number;
    }

    public String getInstallation_address() {
        return installation_address;
    }

    public void setInstallation_address(String installation_address) {
        this.installation_address = installation_address;
    }

    public String getInstallation_CEP() {
        return installation_CEP;
    }

    public void setInstallation_CEP(String installation_CEP) {
        this.installation_CEP = installation_CEP;
    }

    public boolean isInstallation_activity() {
        return installation_activity;
    }

    public void setInstallation_activity(boolean installation_activity) {
        this.installation_activity = installation_activity;
    }
}

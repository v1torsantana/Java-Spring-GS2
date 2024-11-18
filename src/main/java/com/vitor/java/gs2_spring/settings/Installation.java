package com.vitor.java.gs2_spring.settings;

import java.util.Random;

public class Installation {
    private final int installation_number;
    private String installation_address;
    private String installation_CEP;
    private boolean installation_activity;

    public static int generator_installation_number(){
        int installation_no = 0;
        Random generator = new Random();
        installation_no = generator.nextInt(10000, 99999);
        return installation_no;
    }
    public Installation(String installation_address, String installation_CEP, boolean installation_activity) {
        this.installation_number = generator_installation_number();
        this.installation_address = installation_address;
        this.installation_CEP = installation_CEP;
        this.installation_activity = installation_activity;
    }

    public int getInstallation_number() {
        return installation_number;
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

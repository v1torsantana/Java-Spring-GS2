package com.vitor.java.gs2_spring.settings;

import java.util.Random;
import java.util.UUID;

public class Installation {
    private final UUID installation_number;
    private String installation_address;
    private String installation_CEP;
    private boolean installation_activity;

    private static UUID generator_id(){
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    public Installation(String installation_address, String installation_CEP, boolean installation_activity) {
        this.installation_number = Installation.generator_id();
        this.installation_address = installation_address;
        this.installation_CEP = installation_CEP;
        this.installation_activity = installation_activity;
    }

    public UUID getInstallation_number() {
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

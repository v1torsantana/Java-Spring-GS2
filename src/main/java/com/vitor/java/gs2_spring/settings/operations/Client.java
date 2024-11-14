package com.vitor.java.gs2_spring.settings.operations;

public class Client {
    private int client_ID;
    private String client_name;
    private String client_address;
    private int client_CPF;
    private String client_type;
    private int client_CEP;
    private boolean client_activity;

    public Client(int client_ID, String client_name, String client_address, int client_CPF, String client_type, int client_CEP, boolean client_activity) {
        this.client_ID = client_ID;
        this.client_name = client_name;
        this.client_address = client_address;
        this.client_CPF = client_CPF;
        this.client_type = client_type;
        this.client_CEP = client_CEP;
        this.client_activity = client_activity;

    }

    public int getClientID() {
        return client_ID;
    }

    public String getClient_name() {
        return client_name;
    }

    public String getClient_address() {
        return client_address;
    }

    public int getClient_CPF() {
        return client_CPF;
    }

    public String isClient_type() {
        return client_type;
    }

    public int getClient_CEP() {
        return client_CEP;
    }

    public boolean isClient_activity() {
        return client_activity;
    }

    public void setClientID(int clientID) {
        this.client_ID = clientID;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    public void setClient_CPF(int client_CPF) {
        this.client_CPF = client_CPF;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public void setClient_CEP(int client_CEP) {
        this.client_CEP = client_CEP;
    }

    public void setClient_activity(boolean client_activity) {
        this.client_activity = client_activity;
    }

}

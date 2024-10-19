package br.edu.ufersa.pd.servermonitoring.utils;

public enum ServiceType {

    WEBSERVICE(0), DATABASESERVICE(1);

    private final int value;

    private ServiceType(int id) {
        this.value = id;
    }

    public int getValue() { return this.value; }
}

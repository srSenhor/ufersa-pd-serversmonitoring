package br.edu.ufersa.pd.servermonitoring.utils;

public enum Status {
    OK(0), WARNING(1), CRITICAL(2);

    private final int value;

    private Status(int id) {
        this.value = id;
    }

    public int getValue() { return this.value; }
}

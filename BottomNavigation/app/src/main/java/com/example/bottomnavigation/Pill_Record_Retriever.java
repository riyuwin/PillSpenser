package com.example.bottomnavigation;

public class Pill_Record_Retriever {

    private String PillIntake;
    private String PillSkip;
    private String PillTotal;

    public Pill_Record_Retriever(String pillIntake, String pillSkip, String pillTotal) {
        PillIntake = pillIntake;
        PillSkip = pillSkip;
        PillTotal = pillTotal;
    }

    public String getPillIntake() {
        return PillIntake;
    }

    public void setPillIntake(String pillIntake) {
        PillIntake = pillIntake;
    }

    public String getPillSkip() {
        return PillSkip;
    }

    public void setPillSkip(String pillSkip) {
        PillSkip = pillSkip;
    }

    public String getPillTotal() {
        return PillTotal;
    }

    public void setPillTotal(String pillTotal) {
        PillTotal = pillTotal;
    }
}

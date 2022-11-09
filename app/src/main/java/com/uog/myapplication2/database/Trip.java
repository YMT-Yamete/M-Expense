package com.uog.myapplication2.database;

public class Trip {
    private Integer id;
    private String name;
    private String destination;
    private long date;
    private String totalDays;
    private String travelAgency;
    private boolean riskAssessment;
    private String description;

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getTravelAgency() {
        return travelAgency;
    }

    public void setTravelAgency(String travelAgency) {
        this.travelAgency = travelAgency;
    }

    public Trip() {
    }

    public Trip(Integer id, String name, String destination, long date, String totalDays, String travelAgency, boolean riskAssessment, String description) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.date = date;
        this.totalDays = totalDays;
        this.travelAgency = travelAgency;
        this.riskAssessment = riskAssessment;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(boolean riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

package com.gursil.footballresults.Models;

public class Match {
    private long id;
    private String date;
    private String city;
    private String groupA;
    private String groupB;
    private int goalsA;
    private int goalsB;

    public Match(long id, String date, String city, String groupA, String groupB, int goalsA, int goalsB) {
        this.id = id;
        this.date = date;
        this.city = city;
        this.groupA = groupA;
        this.groupB = groupB;
        this.goalsA = goalsA;
        this.goalsB = goalsB;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGroupA() {
        return groupA;
    }

    public void setGroupA(String groupA) {
        this.groupA = groupA;
    }

    public String getGroupB() {
        return groupB;
    }

    public void setGroupB(String groupB) {
        this.groupB = groupB;
    }

    public int getGoalsA() {
        return goalsA;
    }

    public void setGoalsA(int goalsA) {
        this.goalsA = goalsA;
    }

    public int getGoalsB() {
        return goalsB;
    }

    public void setGoalsB(int goalsB) {
        this.goalsB = goalsB;
    }
}

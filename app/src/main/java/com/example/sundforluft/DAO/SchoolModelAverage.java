package com.example.sundforluft.DAO;

public class SchoolModelAverage extends SchoolModel implements Comparable<SchoolModelAverage> {
    private double average;

    public SchoolModelAverage(int Id, String Name) {
        super(Id, Name);
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    @Override
    public int compareTo(SchoolModelAverage avg) {
        return Double.compare(average, avg.average);
    }
}

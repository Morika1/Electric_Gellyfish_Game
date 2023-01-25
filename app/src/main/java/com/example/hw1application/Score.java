package com.example.hw1application;


public class Score {


    int theScore;
    double[] location;


    public Score(){
       location = new double[2]; // location[0] is lat and location[1] is lon

    }


    public int getTheScore() {
        return theScore;
    }

    public Score setTheScore(int theScore) {
        this.theScore = theScore;
        return this;
    }

    public double[] getLocation() {
        return location;
    }

    public Score setLocation(double[] location) {
        this.location = location;
        return this;
    }

    @Override
    public String toString() {
        return "Score{" +
                "theScore=" + theScore +
                ", location= lat: " + location[0] + " lon: "+ location[1] +
                '}';
    }
}

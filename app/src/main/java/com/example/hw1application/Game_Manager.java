package com.example.hw1application;

import android.util.Log;

public class Game_Manager {

    private int life;
    private int distance; // num of rows in matrix
    private int numOfRoads; // num of cols in matrix, num of spongbobs..
    private int spongbobIndex;
    private int[] jellyFishesIndex;


    public Game_Manager(int life, int distance, int numOfRoads){
        this.life= life;
        this.distance = distance;
        this.numOfRoads = numOfRoads;
        this.spongbobIndex = numOfRoads/2;
        Log.d("Game Manager builder:", "num of roads in game manager: "+ numOfRoads);
        this.jellyFishesIndex = new int[numOfRoads];

        for(int i=0; i<numOfRoads;i++)
            jellyFishesIndex[i] = (-i-2);

    }

    public int getLife() {
        return life;
    }

    public int[] getJellyfishesIndex(){
        return jellyFishesIndex;
    }

    public int getSpongbobIndex(){
        return spongbobIndex;
    }

    public int getNumOfRoads(){
        return numOfRoads;
    }


    public void moveJellyFishes() {

        for(int i=0; i<numOfRoads;i++){
            if(jellyFishesIndex[i] < distance)
                jellyFishesIndex[i]++;
            else
                jellyFishesIndex[i] = (-i-1);
        }

    }

    public void moveSpongbob(boolean direction) {
        if(spongbobIndex < numOfRoads -1 && direction)
            spongbobIndex++;

        if(spongbobIndex > 0 && !direction)
            spongbobIndex--;
    }

    public boolean isCrash() {
        if(jellyFishesIndex[spongbobIndex] == distance){
            life--;
            return true;
        }
        return false;
    }


    public boolean isGameOver() {
        return life==0;
    }


    public boolean isInBoundary(int floor) {
        return floor>=0 && floor<distance;
    }

    public int getDistance() {
        return distance;
    }
}

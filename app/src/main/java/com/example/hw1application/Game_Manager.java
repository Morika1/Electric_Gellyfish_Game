package com.example.hw1application;


public class Game_Manager {

    private int life;
    private int distance; // num of rows in matrix
    private int numOfRoads; // num of cols in matrix, num of spongbobs..
    private int spongbobIndex;
    private int score;
    private int[] coinsIndex;
    private int[] jellyFishesIndex;


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int[] getCoinsIndex() {
        return coinsIndex;
    }

    public void setCoinsIndex(int[] coinsIndex) {
        this.coinsIndex = coinsIndex;
    }

    public Game_Manager(int life, int distance, int numOfRoads){
        this.life= life;
        this.distance = distance;
        this.numOfRoads = numOfRoads;
        this.spongbobIndex = numOfRoads/2;
        this.score =0;
        this.coinsIndex = new int[numOfRoads];
        this.jellyFishesIndex = new int[numOfRoads];



        for(int i=0; i<numOfRoads;i++){
            if(i%2 == 0){
                jellyFishesIndex[i] = (-i-2);
                coinsIndex[i] = (i == 0 ? 0 : -i);

            }else{
                jellyFishesIndex[i] = (-i);
                coinsIndex[i] = (-i-2);
            }

        }


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
                jellyFishesIndex[i] = (i%2 == 0? (-i-2) : -i);
        }

    }

    public void moveCoins(){

        for(int i=0; i<numOfRoads;i++){
            if(coinsIndex[i] < distance)
                coinsIndex[i]++;
            else{
                if(i%2 ==0)
                    coinsIndex[i] = (i == 0? 0 : -i);

                else
                    coinsIndex[i] = (-i-2);
            }
        }
    }



    public void moveSpongbob(boolean direction) {
        if(spongbobIndex < numOfRoads - 1 && direction)
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

    public boolean isCatch(){
        if(coinsIndex[spongbobIndex ] == distance){
            score+=10;
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

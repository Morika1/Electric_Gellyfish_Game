package com.example.hw1application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopTen {

    private List<Score> topScores;

    public TopTen(){
    }

    public List<Score> getTopScores() {
        return topScores;
    }


    public boolean isEmpty() {
        if (topScores == null) {
            topScores = new ArrayList<>();
            return true;
        } else if (topScores.size() == 0) {
            return true;
        } else
            return false;

    }
    public boolean isFull(){ return topScores.size() == 10;}

    public void addScore(Score score){
        topScores.add(score);
    }

    public void sortScores(){
        Collections.sort(topScores, (score1, score2) -> score2.getTheScore() - score1.getTheScore());
    }

}

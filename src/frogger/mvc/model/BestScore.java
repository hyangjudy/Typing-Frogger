package frogger.mvc.model;

import java.io.*;

public class BestScore implements Serializable{

    public int[] scores;

    public BestScore(int[] pScores){
        scores = new int[pScores.length];
        for(int i=0;i<pScores.length;i++)
        {
            scores[i]=pScores[i];
        }
    }

}

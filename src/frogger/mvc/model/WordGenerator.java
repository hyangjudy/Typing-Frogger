package frogger.mvc.model;

import frogger.mvc.controller.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public final class WordGenerator {

    public static String generate()
    {
        ArrayList<String> words = new ArrayList<String>();

        String wordListFilePath;

        switch (Game.difficulty)
        {
            case EASY:
                wordListFilePath = ".//src//frogger//words//wordlist-easy.txt"; //the text file
                break;
            case MEDIUM:
                wordListFilePath = ".//src//frogger//words//wordlist-medium.txt"; //the text file
                break;
            case HARD:
                wordListFilePath = ".//src//frogger//words//wordlist-hard.txt"; //the text file
                break;
            default:
                wordListFilePath = ".//src//frogger//words//wordlist-easy.txt"; //the text file
                break;
        }

        try{
            Scanner in = new Scanner(new File(wordListFilePath));
            while(in.hasNextLine())
            {
                words.add(in.nextLine());
            }
        }
        catch(Exception e){
            System.out.printf(e.getMessage());
        }

        return words.get((int)(Math.random()*words.size()));
    }


}

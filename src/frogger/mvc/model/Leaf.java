package frogger.mvc.model;

import frogger.mvc.controller.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import java.util.ArrayList;

public class Leaf extends Sprite{
    private Image imgLeaf = new ImageIcon(strImageDir + "Leaf.gif").getImage();
    private int nCenterX;
    private int nCenterY;
    private int speed;
    private String word;
    private int numEntered;
    private boolean hasActiveWord;
    private boolean needSpawn;
    private boolean isRight;
    private int row;

    public Leaf(int nCenterX, int nCenterY, ArrayList<Word> existedWords, int speed, int row){
        super(nCenterX,nCenterY);
        this.nCenterX = nCenterX;
        this.nCenterY = nCenterY;
        this.numEntered=0;
        this.hasActiveWord=false;
        this.speed=speed;
        this.row=row;
        setDeltaX(this.speed);

        //avoid the newly-added word is already among the words on the screen
        boolean isDuplicate;
        do{
            isDuplicate = false;
            word = WordGenerator.generate();
            for(int i=0;i<existedWords.size();i++)
            {
                if(existedWords.get(i).getContent().equals(word)){isDuplicate=true;}
            }
        }while(isDuplicate);
        setHeight(106);
        setWidth(225);
        needSpawn=false;
        isRight=false;
    }

    public void setGlobalValue(boolean hasActiveWord, int numEntered, boolean isRight){
        this.hasActiveWord=hasActiveWord;
        this.numEntered=numEntered;
        this.isRight=isRight;
    }

    @Override
    public void draw(Graphics g) {
        //color the entered string
        //if speed < 0, draw on the right
        //else, draw on the left
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(imgLeaf, getCenter().x, getCenter().y, null);
        Font font;
        switch (Game.difficulty)
        {
            case EASY:
                font = new Font("Courier", Font.PLAIN, 24);
                break;
            case MEDIUM:
                font = new Font("Courier", Font.PLAIN, 16);
                break;
            case HARD:
                font = new Font("Courier", Font.PLAIN, 14);
                break;
            default:
                font = new Font("Courier", Font.PLAIN, 24);
                break;
        }

        Rectangle2D r = font.getStringBounds(word, g2d.getFontRenderContext());
        int wordWidth = (int)r.getWidth();
        final int STRING_OFFSET = 15;
        AttributedString as = new AttributedString(word);
        as.addAttribute(TextAttribute.FONT, font);

        if (!isRight) {
            as.addAttribute(TextAttribute.FOREGROUND, Color.white, 0, word.length());

        } else if (!hasActiveWord) {
            as.addAttribute(TextAttribute.FOREGROUND, Color.white, 0, word.length());

        } else {
            if (word.length() == numEntered) {
                as.addAttribute(TextAttribute.FOREGROUND, Color.red, 0, word.length());
            } else {
                as.addAttribute(TextAttribute.FOREGROUND, Color.red, 0, numEntered);
                as.addAttribute(TextAttribute.FOREGROUND, Color.white, numEntered, word.length());
            }
        }

        if (speed <= 0) {
            g2d.drawString(as.getIterator(), getCenter().x + Leaf.super.getWidth() -wordWidth-STRING_OFFSET, getCenter().y + Leaf.super.getHeight() / 2);
        }
        else{
            g2d.drawString(as.getIterator(), getCenter().x + STRING_OFFSET, getCenter().y + Leaf.super.getHeight() / 2);
        }
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public void move(){
        super.move();
        nCenterX+= getDeltaX();
        setCenter(new Point(nCenterX, nCenterY));
    }

    public String getWord() {
        return word;
    }

    public int getnCenterX() {
        return nCenterX;
    }

    public int getnCenterY() {
        return nCenterY;
    }

    public int getLeafSpeed() {
        return speed;
    }

    public void setNeedSpawn(boolean needSpawn) {
        this.needSpawn = needSpawn;
    }

    public boolean isNeedSpawn() {
        return needSpawn;
    }

    public int getRow() {
        return row;
    }
}

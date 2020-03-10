package frogger.mvc.controller;

import frogger.mvc.model.*;
import frogger.mvc.view.GamePanel;
import frogger.sounds.Sound;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends MouseMotionAdapter implements Runnable, KeyListener, MouseListener {

    // FIELDS
    public static final Dimension DIM = new Dimension(1020, 780); //the dimension of the game.
    private GamePanel gmpPanel;

    // updates (animation)
    public final static int ANI_DELAY = 45; // milliseconds between screen
    public static final int GAME_MAX_LEVEL =5; // Define max number of levels to determine if game is over.
    private Thread thrAnim;
    private int nTick = 0;
    private long lStartTime = System.currentTimeMillis();
    private Clip clpMusicBackground;

    private ArrayList<Word> words;
    private ArrayList<Word> activeWords;
    private boolean hasActiveWord;
    private int numEntered;
    private int row;

    //parameter for leaf
    private int speedLeaf = 1;
    private final int LEAF_HEIGHT_1 = 160;
    private final int LEAF_HEIGHT_2 = 270;
    private final int LEAF_HEIGHT_3 = 380;
    private final int LEAF_HEIGHT_4 = 490;
    private final int LEAF_HEIGHT_5 = 600;
    private final int LEAF_X_POSITION_1 = 0;
    private final int LEAF_X_POSITION_2 = 100;
    private final int LEAF_X_POSITION_3 = 50;
    private final int LEAF_X_POSITION_4 = 150;
    private final int ROW_1 = 1;
    private final int ROW_2 = 2;
    private final int ROW_3 = 3;
    private final int ROW_4 = 4;
    private final int ROW_5 = 5;

    public static boolean isEasyButtonHover;
    public static boolean isMediumButtonHover;
    public static boolean isHardButtonHover;
    public static boolean isPauseButtonHover;
    public static boolean isRestartButtonHover;

    public enum DIFFICULTIES {EASY,MEDIUM,HARD}
    public static DIFFICULTIES difficulty;

    public enum STATES {GAME, DEFAULT, START, PAUSE, END}
    public static STATES state;

    private int lastTimeSpot;
    private long lastScore;

    Leaf tempLeaf;

    public Game() {
        //initialize panel
        gmpPanel = new GamePanel(DIM);
        gmpPanel.addKeyListener(this);
        gmpPanel.addMouseListener(this);
        gmpPanel.addMouseMotionListener(this);

        //initialize parameter in Game
        words = new ArrayList<Word>();
        hasActiveWord = false;
        activeWords = new ArrayList<Word>();
        numEntered = 0;
        row=0;
        state = STATES.DEFAULT;
        isEasyButtonHover =false;
        isMediumButtonHover =false;
        isHardButtonHover =false;
        isPauseButtonHover=false;
        isRestartButtonHover=false;
        difficulty=DIFFICULTIES.EASY;
        lastTimeSpot=60;
        lastScore=0;

    }

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Game game = new Game();
                    game.fireUpAnimThread();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fireUpAnimThread() {
        if (thrAnim == null) {
            thrAnim = new Thread(this);
            thrAnim.start();
        }
    }


    public void run() {

        thrAnim.setPriority(Thread.MIN_PRIORITY);

        while (Thread.currentThread() == thrAnim) {

            tick();

            // Check items in order of priority
            checkJustDie();
            checkJustWin();
            startGame();
            checkGameOver();
            checkIsDead();
            gmpPanel.update(gmpPanel.getGraphics());
            checkLeaf();
            processQueue();

            try {
                lStartTime += ANI_DELAY;
                Thread.sleep(Math.max(0, lStartTime - System.currentTimeMillis()));
            } catch (InterruptedException e) {
                continue;
            }
        } // end while
    } // end run


    //check if just win
    private void checkJustWin() {
        if(row==5) {
            setNextLevel();
            speedLeaf += 1 ;
            lastScore = CommandCenter.getInstance().getScore();
        }
    }

    //check if just die, reset
    private void checkJustDie() {
        if (!CommandCenter.getInstance().hasActiveWords())
        {
            for(int i=0;i<activeWords.size();i++)
            {
                updateHasActiveWord(activeWords.get(i),false);
            }
            activeWords.clear();
            CommandCenter.getInstance().setReturnHasActiveWords(true);
            row=0;
            CommandCenter.getInstance().setScore(lastScore);
        }
    }


    // Check if Game is over and stop background music
    private void checkGameOver() {
        if (CommandCenter.getInstance().finalStatus() != CommandCenter.STATUS.IN_PROGRESS) {
            stopLoopingSounds(clpMusicBackground);
        }
    }

    // Method to draw leaf.
    private void drawLeaf() {
        //row 1
        tempLeaf = new Leaf(LEAF_X_POSITION_1, LEAF_HEIGHT_1, words ,speedLeaf, ROW_1);
        CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
        words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));
        for(int i=0;i<words.size();i++)
            System.out.print(words.get(i).getContent() +" ");
        System.out.println(" ");

        tempLeaf = new Leaf(DIM.width / 3+LEAF_X_POSITION_1, LEAF_HEIGHT_1,words ,speedLeaf, ROW_1);
        CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
        words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));
        for(int i=0;i<words.size();i++)
            System.out.print(words.get(i).getContent()+" ");
        System.out.println(" ");


        tempLeaf = new Leaf(DIM.width / 3*2+LEAF_X_POSITION_1, LEAF_HEIGHT_1,words,speedLeaf, ROW_1);
        CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
        words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));
        for(int i=0;i<words.size();i++)
            System.out.print(words.get(i).getContent()+" ");

        System.out.println(" ");


        //row 2
        tempLeaf = new Leaf(LEAF_X_POSITION_2, LEAF_HEIGHT_2, words ,-speedLeaf, ROW_2);
        CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
        words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));

        tempLeaf = new Leaf(DIM.width / 3+LEAF_X_POSITION_2, LEAF_HEIGHT_2,words ,-speedLeaf, ROW_2);
        CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
        words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));

        tempLeaf = new Leaf(DIM.width / 3*2+LEAF_X_POSITION_2, LEAF_HEIGHT_2,words,-speedLeaf, ROW_2);
        CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
        words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));

        //row 3
        tempLeaf = new Leaf(LEAF_X_POSITION_3, LEAF_HEIGHT_3, words ,speedLeaf, ROW_3);
        CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
        words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));

        tempLeaf = new Leaf(DIM.width / 3+LEAF_X_POSITION_3, LEAF_HEIGHT_3,words ,speedLeaf, ROW_3);
        CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
        words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));

        tempLeaf = new Leaf(DIM.width / 3*2+LEAF_X_POSITION_3, LEAF_HEIGHT_3,words,speedLeaf, ROW_3);
        CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
        words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));

        //row 4

        tempLeaf = new Leaf(LEAF_X_POSITION_4, LEAF_HEIGHT_4, words,-speedLeaf, ROW_4);
        CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
        words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));

        tempLeaf = new Leaf(DIM.width / 3+LEAF_X_POSITION_4, LEAF_HEIGHT_4,words,-speedLeaf, ROW_4);
        CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
        words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));

        tempLeaf = new Leaf(DIM.width / 3*2+LEAF_X_POSITION_4, LEAF_HEIGHT_4,words ,-speedLeaf, ROW_4);
        CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
        words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));

        //end point
        tempLeaf = new Leaf(DIM.width / 2, LEAF_HEIGHT_5,words ,0, ROW_5);
        CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
        words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));

    }



    // check if the frog is dead
    private void checkIsDead() {
        if (CommandCenter.getInstance().getFrog() != null) {
            Frog frog = CommandCenter.getInstance().getFrog();
            if (frog.isDead()) {
                CommandCenter.getInstance().getOpsList().enqueue(frog, CollisionOp.Operation.REMOVE);
                CommandCenter.getInstance().spawnFrog(false);
                if (CommandCenter.getInstance().finalStatus()== CommandCenter.STATUS.IN_PROGRESS) {
                    clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
        }
        System.gc();
    }

    // Method to check if leaf is out of screen, then remove and add a replacement for the same type
    private void checkLeaf() {
        for (Movable movLeaf : CommandCenter.getInstance().getMovLeaf()) {
            //make the cloud spawn more smooth

            //from right to left
            if (movLeaf instanceof Leaf && (movLeaf.getCenter().x < -200) && movLeaf.getSpeed()<0) {
                CommandCenter.getInstance().getOpsList().enqueue(movLeaf, CollisionOp.Operation.REMOVE);
                removedWords(((Leaf) movLeaf).getWord());
                for(int k=0;k<words.size();k++){System.out.print(words.get(k).getContent()+" ");}
                System.out.println("  ");
                ((Leaf) movLeaf).setNeedSpawn(false);
            }
            else if (movLeaf instanceof Leaf && movLeaf.getCenter().x < 0  && movLeaf.getSpeed()<0){
                if(((Leaf) movLeaf).isNeedSpawn()==false) {
                    ((Leaf) movLeaf).setNeedSpawn(true);
                    int romovedHeight = ((Leaf) movLeaf).getnCenterY();
                    int romovedSpeed = ((Leaf) movLeaf).getLeafSpeed();
                    int removedRow = ((Leaf) movLeaf).getRow();
                    Leaf tempLeaf;
                    tempLeaf = new Leaf(DIM.width, romovedHeight, words, romovedSpeed, removedRow);
                    CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
                    words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));

                    for(int k=0;k<words.size();k++){System.out.print(words.get(k).getContent()+" ");}
                    System.out.println("  ");

                }
            }
            //from left to right
            else if (movLeaf instanceof Leaf && (movLeaf.getCenter().x >DIM.width) && movLeaf.getSpeed()>0) {
                CommandCenter.getInstance().getOpsList().enqueue(movLeaf, CollisionOp.Operation.REMOVE);
                removedWords(((Leaf) movLeaf).getWord());
                ((Leaf) movLeaf).setNeedSpawn(false);
                for(int k=0;k<words.size();k++){System.out.print(words.get(k).getContent()+" ");}
                System.out.println("  ");
            }
            else if (movLeaf instanceof Leaf && movLeaf.getCenter().x + movLeaf.getWidth() > DIM.width  && movLeaf.getSpeed()>0){
                if(((Leaf) movLeaf).isNeedSpawn()==false) {
                    ((Leaf) movLeaf).setNeedSpawn(true);
                    int romovedHeight = ((Leaf) movLeaf).getnCenterY();
                    int romovedSpeed = ((Leaf) movLeaf).getLeafSpeed();
                    int removedRow = ((Leaf) movLeaf).getRow();
                    Leaf tempLeaf;
                    tempLeaf = new Leaf(-movLeaf.getWidth(), romovedHeight, words, romovedSpeed, removedRow);
                    CommandCenter.getInstance().getOpsList().enqueue(tempLeaf, CollisionOp.Operation.ADD);
                    words.add( new Word(tempLeaf.getWord(), tempLeaf.getRow()));
                    for(int k=0;k<words.size();k++){System.out.print(words.get(k).getContent()+" ");}
                    System.out.println("  ");
                }
            }

        }
    }


    private void processQueue() {

        //we are dequeuing the opsList and performing operations in serial to avoid mutating the movable arraylists while iterating them above
        while (!CommandCenter.getInstance().getOpsList().isEmpty()) {
            CollisionOp cop = CommandCenter.getInstance().getOpsList().dequeue();
            Movable mov = cop.getMovable();
            CollisionOp.Operation operation = cop.getOperation();

            if(mov instanceof Leaf){
                if (operation == CollisionOp.Operation.ADD) {
                    CommandCenter.getInstance().getMovLeaf().add(mov);
                } else {
                    CommandCenter.getInstance().getMovLeaf().remove(mov);
                }
            }
            else if (mov instanceof Frog){
                if (operation == CollisionOp.Operation.ADD) {
                    CommandCenter.getInstance().getMovFrog().add(mov);
                } else {
                    CommandCenter.getInstance().getMovFrog().remove(mov);
                }
            }
        }
        System.gc();
    }//end meth

    //some methods for timing events in the game, such as the appearance of UFOs, floaters (power-ups), etc.
    public void tick() {
        if (nTick == Integer.MAX_VALUE)
            nTick = 0;
        else
            nTick++;
    }

    public int getTick() {
        return nTick;
    }

    // start game
    private void startGame() {
        if(state==STATES.START) {
            clpMusicBackground = Sound.clipForLoopFactory("game.wav");
            clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
            CommandCenter.getInstance().clearAll();
            CommandCenter.getInstance().initGame();
            CommandCenter.getInstance().setPlaying(true);
            CommandCenter.getInstance().setPaused(false);
            drawLeaf();
            CommandCenter.getInstance().spawnFrog(true);
            state = STATES.GAME;
        }
    }

    // restart game
    private void restartGame(){
        lastScore=0;
        speedLeaf=1;
        row=0;
        words.clear();
        activeWords.clear();
        for(int i=0;i<activeWords.size();i++)
        {
            updateHasActiveWord(activeWords.get(i),false);
        }
        CommandCenter.getInstance().setReturnHasActiveWords(false);
        CommandCenter.getInstance().setLevelClear(false);
        CommandCenter.getInstance().clearAll();
        CommandCenter.getInstance().spawnFrog(true);
        CommandCenter.getInstance().setTimeLeft(60);
        CommandCenter.getInstance().setPlaying(false);
        CommandCenter.getInstance().setLevel(0);
        CommandCenter.getInstance().setScore(0);
        try {
            Thread.sleep(100);
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }
        lastTimeSpot=60;
        gmpPanel.setbPlayGameOverSound(true);
    }


    // Varargs for stopping looping-music-clips
    private static void stopLoopingSounds(Clip... clpClips) {
        for (Clip clp : clpClips) {
            clp.stop();
        }
    }

    public void mouseClicked(MouseEvent e) {

    }

    //use to set the button-like things in the game
    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if(mx>=Game.DIM.width/2-90 && mx<=Game.DIM.width/2+90 &&state==STATES.DEFAULT) {
            if (my >= 480 && my <= 520) {
                state = STATES.START;
                difficulty = DIFFICULTIES.EASY;
            }
            else if(my >= 560 && my<=600){
                state = STATES.START;
                difficulty = DIFFICULTIES.MEDIUM;
            }
            else if(my >=640 && my<=680){
                state = STATES.START;
                difficulty = DIFFICULTIES.HARD;
            }
        }
        else if(mx>=5 && mx<=185 &&state==STATES.GAME){
            if (my >= 40 && my <= 80) {
                CommandCenter.getInstance().setPaused(true);
                state=STATES.PAUSE;
            }
        }
        else if(mx>=5 && mx<=185   &&state==STATES.PAUSE){
            if (my >= 40 && my <= 80) {
                CommandCenter.getInstance().setPaused(false);
                state=STATES.GAME;
            }
        }
        else if(mx>=Game.DIM.width/2-90 && mx<=Game.DIM.width/2+90 && state==STATES.END){
            if (my >= 560 && my <= 600) {
                state=STATES.DEFAULT;
                restartGame();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    //use to set the button-like things are hovered
    public void mouseMoved(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();
        if(state==STATES.DEFAULT && mx>=Game.DIM.width/2-90 && mx<=Game.DIM.width/2+90){
            if(my>=480 && my<=520){
                isEasyButtonHover = true;
            }
            else if(my>=560 && my<=600){
                isMediumButtonHover = true;
            }
            else if(my>=640 && my<=680){
                isHardButtonHover = true;
            }
            else{
                isEasyButtonHover = false;
                isMediumButtonHover = false;
                isHardButtonHover = false;
            }
        }
        else if(mx>=5 && mx<=185 && (state==STATES.GAME || state==STATES.PAUSE)){
            if (my >= 40 && my <= 80) {
                isPauseButtonHover=true;
            }
            else{
                isPauseButtonHover=false;
            }
        }
        else if(mx>=Game.DIM.width/2-90 && mx<=Game.DIM.width/2+90 && state==STATES.END){
            if (my >= 560 && my <= 600) {
                isRestartButtonHover=true;
            }
            else{
                isRestartButtonHover=false;
            }
        }
    }


    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    //check if the typed in characters are matched to those words in the screen
    public void keyPressed(KeyEvent e) {
        Frog frog = CommandCenter.getInstance().getFrog();
        int nKey = e.getKeyCode();
        if (frog != null && state==STATES.GAME) {
            if (nKey >= 65 && nKey <= 122) {
                // do not have active words, check those words whose first character matches the first typed-in char
                if (!hasActiveWord) {
                    for (Word candidate : words) {
                        if (candidate.getContent().charAt(0) == (char) (nKey + 32) && candidate.getRow()==row+1) {
                            activeWords.add(candidate);
                        }
                    }
                    if (activeWords.size() > 0) {
                        hasActiveWord = true;
                        numEntered = 1;
                    }
                    for(int i=0;i<activeWords.size();i++){
                        updateHasActiveWord(activeWords.get(i),true);
                    }
                }
                // have active words, check if the next typed-in char matches
                else{
                    int j=0;
                    //if do not match a word, remove the word from activeWords
                    while(j<activeWords.size())
                    {
                        if(activeWords.get(j).getContent().charAt(numEntered)!=(char)(nKey+32) ){
                            updateHasActiveWord(activeWords.get(j),false);
                            activeWords.remove(j);
                        }
                        else{j++;}
                    }

                    numEntered++;

                    //if there is no words remaining
                    if(activeWords.size()==0) {
                        numEntered=0;
                        hasActiveWord=false;
                        activeWords.clear();
                        for (int k = 0; k < words.size(); k++) {
                            updateHasActiveWord(words.get(k),false);
                        }
                    }
                    else if(activeWords.size()>0) {
                        for (int i = 0; i < activeWords.size(); i++) {

                            //color the matched words just checked
                            updateHasActiveWord(activeWords.get(i),true);

                            //if the whole word matches
                            if(activeWords.get(i).getContent().length() == numEntered){
                                jumpToCorrectWord(activeWords.get(i));
                                numEntered = 0;
                                hasActiveWord = false;
                                activeWords.clear();

                                for (int k = 0; k < words.size(); k++) {
                                    updateHasActiveWord(words.get(k),false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}


    // Method to increment to next level of the game.
    private void setNextLevel() {
        clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
        row=0;
        words.clear();
        activeWords.clear();
        for(int i=0;i<activeWords.size();i++)
        {
            updateHasActiveWord(activeWords.get(i),false);
        }
        CommandCenter.getInstance().setReturnHasActiveWords(false);
        CommandCenter.getInstance().setLevelClear(false);
        CommandCenter.getInstance().setNextLevel();
        try {
            Thread.sleep(100);
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }
        CommandCenter.getInstance().clearAll();
        drawLeaf();
        CommandCenter.getInstance().spawnFrog(true);
        CommandCenter.getInstance().setTimeLeft(60);
        lastTimeSpot=CommandCenter.getInstance().getGameTimeLeft();
    }

    //identify which leaf to jump onto, change the frog speed to the leaf speed, set score
    private void jumpToCorrectWord(Word word)
    {
        clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
        row+=1;
        for (Movable movLeaf : CommandCenter.getInstance().getMovLeaf()) {
            if (movLeaf instanceof Leaf) {
                if (((Leaf) movLeaf).getWord().equals(word.getContent())){
                    //if speed<0 jump to left side
                    //else jump t right side
                    if(((Leaf) movLeaf).getLeafSpeed()<=0){
                        CommandCenter.getInstance().getFrog().move(((Leaf) movLeaf).getnCenterX(),((Leaf) movLeaf).getnCenterY());
                    }
                    else{
                        CommandCenter.getInstance().getFrog().move(((Leaf) movLeaf).getnCenterX()+movLeaf.getWidth()-CommandCenter.getInstance().getFrog().getWidth(),((Leaf) movLeaf).getnCenterY());
                    }
                    Sound.playSound("Boing.wav");
                    CommandCenter.getInstance().getFrog().setFrogSpeed(((Leaf) movLeaf).getLeafSpeed());
                    switch (difficulty){
                        case EASY:
                            CommandCenter.getInstance().addScore(20*Math.abs(speedLeaf)/(lastTimeSpot-CommandCenter.getInstance().getGameTimeLeft()));
                            break;
                        case MEDIUM:
                            CommandCenter.getInstance().addScore(40*Math.abs(speedLeaf)/(lastTimeSpot-CommandCenter.getInstance().getGameTimeLeft()));
                            break;
                        case HARD:
                            CommandCenter.getInstance().addScore(80*Math.abs(speedLeaf)/(lastTimeSpot-CommandCenter.getInstance().getGameTimeLeft()));
                            break;
                    }
                    lastTimeSpot=CommandCenter.getInstance().getGameTimeLeft();
                    break;
                }
            }
        }
    }

    //help to set the words removed from the activeWords list to white
    public void updateHasActiveWord(Word word, boolean isRight){
        clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
        for (Movable movLeaf : CommandCenter.getInstance().getMovLeaf()) {
            if (movLeaf instanceof Leaf) {
                if(((Leaf) movLeaf).getWord().equals(word.getContent())) {
                    ((Leaf) movLeaf).setGlobalValue(hasActiveWord, numEntered,isRight);
                }
            }
        }
    }

    //help to remove words from words
    public void removedWords(String word){
        int removedOrder=0;
        for(int i=0;i<words.size();i++){
            if(words.get(i).getContent().equals(word)){
                removedOrder = i;
                break;
            }
        }
        words.remove(removedOrder);
    }

}


package frogger.mvc.model;

import frogger.mvc.controller.Game;

import java.util.ArrayList;
import java.util.List;

public class CommandCenter {

    private  int nNumFrogs;
	private  int nLevel;
	private  long lScore;
	private  int nCoins;
	private  Frog frog;
	private  boolean bPlaying;
	private  boolean bPaused;
    private int nSecondsLeft;
    private long lSysTimeSeconds;
    private boolean bLevelClear = false;

    private boolean rHasActiveWords = false;
    public enum STATUS{WIN,LOSE,IN_PROGRESS}


	// These ArrayLists with capacities set
    private List<Movable> movLeaf = new ArrayList<>(300);
    private List<Movable> movFrog = new ArrayList<>(50);

	private GameOpsList opsList = new GameOpsList();

	private static CommandCenter instance = null;

    private int nMoveCountX = 0;
    private int nDeltaX = 0;

	// Constructor made private - static Utility class only
	private CommandCenter() {}

	public static CommandCenter getInstance(){
		if (instance == null){
			instance = new CommandCenter();
		}
		return instance;
	}

	public  void initGame(){
		setLevel(1);
		setScore(0);
        setCoins(0);
		setNumFrogs(5);
        nSecondsLeft = 60;
        lSysTimeSeconds = System.currentTimeMillis()/1000;
	}

    public void setTimeLeft(int nSecondsLeft) {
        this.nSecondsLeft = nSecondsLeft;
    }

	
	// The parameter is true if this is for the beginning of the game, otherwise false
	public  void spawnFrog(boolean bFirst) {

        if (getNumFrogs() != 0) {
            frog = new Frog(Game.DIM.width/2-50,60);
            opsList.enqueue(frog, CollisionOp.Operation.ADD);
            if (!bFirst) {
                setNumFrogs(getNumFrogs() - 1);
                rHasActiveWords=false;
            }
		}
	}

    public void setReturnHasActiveWords(boolean returnHasActiveWords) {
        this.rHasActiveWords = returnHasActiveWords;
    }


	public GameOpsList getOpsList() {
		return opsList;
	}

	public void setOpsList(GameOpsList opsList) {
		this.opsList = opsList;
	}

	public  void clearAll(){
		movLeaf.clear();
		movFrog.clear();
	}

	public  boolean isPlaying() {
		return bPlaying;
	}

	public  void setPlaying(boolean bPlaying) {
		this.bPlaying = bPlaying;
	}

	public  boolean isPaused() {
		return bPaused;
	}

	public  void setPaused(boolean bPaused) {
		this.bPaused = bPaused;
	}
	
    public STATUS finalStatus() {		//if the number of Marios is zero or seconds left is zero, then game over
		if ((getNumFrogs() == 0 || nSecondsLeft == 0 ) && nLevel != 0) {
			return STATUS.LOSE;
		}
		else if(nLevel > Game.GAME_MAX_LEVEL && nLevel != 0){
		    return STATUS.WIN;
        }
        else{
            return STATUS.IN_PROGRESS;
        }
	}

    public boolean hasActiveWords() {
        return rHasActiveWords;
    }

    public  int getLevel() {
		return nLevel;
	}

    public void setNextLevel() {
        nLevel++;
    }

    public boolean isLevelClear() {
        return bLevelClear;
    }

    public void setLevelClear(boolean bLevelClearStatus) {
        bLevelClear = bLevelClearStatus;
    }

	public long getScore() {
		return lScore;
	}

    public void setCoins(int nCoins) {
        this.nCoins = nCoins;
    }

    public int getCoins() {
        return nCoins;
    }

    public void incrCoinScore() {
        nCoins++;
    }

	public  void setScore(long lParam) {
		lScore = lParam;
	}

    public  void addScore(long lParam) {
        lScore += lParam;
    }

	public  void setLevel(int n) {
		nLevel = n;
	}

	public  int getNumFrogs() {
		return nNumFrogs;
	}

	public  void setNumFrogs(int nParam) {
		nNumFrogs = nParam;
	}
	
	public Frog getFrog(){
		return frog;
	}
	
	public  void setFrog(Frog frogParam){
		frog = frogParam;
	}

    public List<Movable> getMovLeaf() { return movLeaf; }

    public List<Movable> getMovFrog() { return movFrog; }

    public int getMoveCountX() {
        return nMoveCountX;
    }

    public void setMoveCountX(int nMoveCount) {
        this.nMoveCountX = nMoveCount;
    }

    public void decrMoveCountX() {
        nMoveCountX--;
    }

    public int getDeltaX() {
        return nDeltaX;
    }

    public void setDeltaX(int nDeltaX) {
        this.nDeltaX = nDeltaX;
    }

    public int getGameTimeLeft() {
        return nSecondsLeft;
    }

    public void updateTimeLeft() {
        if (lSysTimeSeconds != System.currentTimeMillis()/1000) {
            nSecondsLeft--;
            lSysTimeSeconds = System.currentTimeMillis()/1000;
        }
    }
}

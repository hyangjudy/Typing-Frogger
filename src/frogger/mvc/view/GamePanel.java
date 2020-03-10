package frogger.mvc.view;

import frogger.mvc.controller.Game;
import frogger.mvc.model.BestScore;
import frogger.mvc.model.CommandCenter;
import frogger.mvc.model.Movable;
import frogger.mvc.model.Sprite;
import frogger.sounds.Sound;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;


public class GamePanel extends Panel {

	// FIELDS
	private Dimension dimOff;
	private Image imgOff;
	private Graphics grpOff;
	
	private GameFrame gmf;
	private Font fnt = new Font("SansSerif", Font.BOLD, 12);
	private Font fntBig = new Font("SansSerif", Font.BOLD, 28);
    private Font customFont;
	private FontMetrics fmt;
	private String strDisplay = "";
    private boolean bPlayGameOverSound = true;




    // CONSTRUCTOR
	public GamePanel(Dimension dim){
	    gmf = new GameFrame();
		gmf.getContentPane().add(this);
		gmf.pack();
		initView();
		
		gmf.setSize(dim);
		gmf.setTitle("Typing Frogger");
		gmf.setResizable(false);
		gmf.setVisible(true);
		this.setFocusable(true);

	}

	// METHODS
	
	private void drawScore(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        g2D.setColor(Color.darkGray);

		g2D.setFont(new Font("Courier", Font.BOLD, 24));

        Rectangle pauseButton = new Rectangle(5,40,180,40);
        Rectangle pauseButtonHover = new Rectangle(5,30,180,60);

        if(Game.isPauseButtonHover==true){
            g2D.draw(pauseButtonHover);
        }
        else{
            g2D.draw(pauseButton);
        }

        if(CommandCenter.getInstance().isPaused()){
            strDisplay="CONTINUE";
            g.drawString(strDisplay, pauseButton.width / 2 - fmt.stringWidth(strDisplay), 70);
            strDisplay="GAME PAUSED";
            g.drawString(strDisplay, Game.DIM.width/ 2-90,Game.DIM.height/2);
        }
        else {
            strDisplay = "PAUSE";
            g2D.drawString(strDisplay, pauseButton.width / 2 - fmt.stringWidth(strDisplay), 70);
        }

        // Draw level
        g2D.drawString("LEVEL ", Game.DIM.width/2-50, 50);
        strDisplay = String.format("%01d",CommandCenter.getInstance().getLevel());
        g2D.drawString(strDisplay, Game.DIM.width/2+50,  50);

        // Draw Frog's total score
		g2D.drawString("SCORE", 800, 50);
        strDisplay = String.format("%04d",CommandCenter.getInstance().getScore());
		g2D.drawString(strDisplay, 920, 50);

        // Draw time left
        g2D.drawString("TIME", 800,  80);
        strDisplay = String.format("%03d",CommandCenter.getInstance().getGameTimeLeft());
        g2D.drawString(strDisplay, 930,  80);

        // Draw Frog's lives left
        g2D.drawString("LIFE", 803,  110);
        strDisplay = String.format("%1d",CommandCenter.getInstance().getNumFrogs());
        g2D.drawString(strDisplay, 955, 110);

	}
	
	@SuppressWarnings("unchecked")
	public void update(Graphics g) {
		if (grpOff == null || Game.DIM.width != dimOff.width
				|| Game.DIM.height != dimOff.height) {
			dimOff = Game.DIM;
			imgOff = createImage(Game.DIM.width, Game.DIM.height);
			grpOff = imgOff.getGraphics();
		}

		// Fill in background with Frog blue.
		grpOff.setColor(new Color(43, 87, 112));
		grpOff.fillRect(0, 0, Game.DIM.width, Game.DIM.height);
        Image river = new ImageIcon(Sprite.strImageDir + "River.gif").getImage();
        grpOff.drawImage(river,0,0,null);
        Image bg = new ImageIcon(Sprite.strImageDir + "Back.png").getImage();
        grpOff.drawImage(bg,0,81,null);


		if (!CommandCenter.getInstance().isPlaying() && CommandCenter.getInstance().finalStatus()== CommandCenter.STATUS.IN_PROGRESS) {
            displayTextOnScreenBefore(grpOff);
        }
        else if(CommandCenter.getInstance().finalStatus() != CommandCenter.STATUS.IN_PROGRESS){
            Game.state = Game.STATES.END;
            displayTextOnScreenAfter(grpOff);
        }
		else {
            drawScore(grpOff);


		    if(!CommandCenter.getInstance().isPaused()) {

                // Update game timer
                if (CommandCenter.getInstance().getFrog() != null && !CommandCenter.getInstance().getFrog().isDead()) {
                    CommandCenter.getInstance().updateTimeLeft();
                }
                moveMovables(grpOff,
                        (ArrayList<Movable>) CommandCenter.getInstance().getMovLeaf(),
                        (ArrayList<Movable>) CommandCenter.getInstance().getMovFrog()
                );

            }

		}
		//draw the double-Buffered Image to the graphics context of the panel
		g.drawImage(imgOff, 0, 0, this);

        // Decrement global move counter
        if (CommandCenter.getInstance().getMoveCountX() != 0) {
            CommandCenter.getInstance().decrMoveCountX();
        }
	} 

	//for each movable array, process it.
	private void moveMovables(Graphics g, ArrayList<Movable>...movMovz){
		
		for (ArrayList<Movable> movMovs : movMovz) {
			for (Movable mov : movMovs) {
                mov.move();
				mov.draw(g);
			}
		}
	}


	private void initView() {
		Graphics g = getGraphics();			// get the graphics context for the panel
        loadGameFont();
		g.setFont(fnt);						// take care of some simple font stuff
		fmt = g.getFontMetrics();
		g.setFont(fntBig);					// set font info
	}
	
	// This method draws some text to the middle of the screen before a game
	private void displayTextOnScreenBefore(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        Image imgBackground = new ImageIcon(Sprite.strImageDir + "River.gif").getImage();
        Image imgTyping = new ImageIcon(Sprite.strImageDir + "Typing.png").getImage();
        Image imgFrogger = new ImageIcon(Sprite.strImageDir + "Frogger.png").getImage();
        Image imgFront = new ImageIcon(Sprite.strImageDir + "Frontimg.png").getImage();

        g2d.drawImage(imgBackground,0,0,null);
        g2d.drawImage(imgTyping,150,110,null);
        g2d.drawImage(imgFrogger,100,240,null);
        g2d.drawImage(imgFront,500,50,null);

        g.setColor(Color.darkGray);

        strDisplay = "TYPE A-Z TO MATCH WORDS ON LEAVES";
        grpOff.drawString(strDisplay,
                Game.DIM.width/2 - fmt.stringWidth(strDisplay)-100 , 400);


        strDisplay = "CHOOSE ONE DIFFICULTY TO START";
        grpOff.drawString(strDisplay,
                Game.DIM.width/2 - fmt.stringWidth(strDisplay)-100 , 450);


        Rectangle easyButton = new Rectangle(Game.DIM.width/2-90,480,180,40);
        Rectangle easyButtonHover = new Rectangle(Game.DIM.width/2-100,470,200,60);

        Rectangle mediumButton = new Rectangle(Game.DIM.width/2-90,560,180,40);
        Rectangle mediumButtonHover = new Rectangle(Game.DIM.width/2-100,550,200,60);

        Rectangle hardButton = new Rectangle(Game.DIM.width/2-90,640,180,40);
        Rectangle hardButtonHover = new Rectangle(Game.DIM.width/2-100,630,200,60);



        if(Game.isEasyButtonHover){
            g2d.draw(easyButtonHover);
        }
        else {
            g2d.draw(easyButton);
        }

        if(Game.isMediumButtonHover){
            g2d.draw(mediumButtonHover);
        }
        else {
            g2d.draw(mediumButton);
        }

        if(Game.isHardButtonHover){
            g2d.draw(hardButtonHover);
        }
        else {
            g2d.draw(hardButton);
        }

        g.setFont(customFont);

        strDisplay = "EASY";
        grpOff.drawString(strDisplay,easyButton.x+easyButton.width/2-g.getFontMetrics().stringWidth(strDisplay)/2,easyButton.y+easyButton.height/2+g.getFontMetrics().getHeight()/2);

        strDisplay = "MEDIUM";
        grpOff.drawString(strDisplay,mediumButton.x+mediumButton.width/2-g.getFontMetrics().stringWidth(strDisplay)/2,mediumButton.y+mediumButton.height/2+g.getFontMetrics().getHeight()/2);

        strDisplay = "HARD";
        grpOff.drawString(strDisplay,hardButton.x+hardButton.width/2-g.getFontMetrics().stringWidth(strDisplay)/2,hardButton.y+hardButton.height/2+g.getFontMetrics().getHeight()/2);


	}


    // This method draws some text to the middle of the screen before a game
    private void displayTextOnScreenAfter(Graphics g) {

	    //read score history
        String scoreHistoryPath;

        scoreHistoryPath = ".//src//frogger//score//history.ser"; //the text file

        int[] scores = new int[3];

        BestScore e = null;
        try {
            FileInputStream fileIn = new FileInputStream(scoreHistoryPath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (BestScore) in.readObject();
            scores = e.scores;
            in.close();
            fileIn.close();
        } catch (IOException i) {
                System.out.println("Can not find history of game...");
                for(int k=0;k <scores.length;k++){scores[k]=0;}
//            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("BestScore class not found");
            c.printStackTrace();
            return;
        }

        //update score history
        try {
            switch (Game.difficulty) {
                case EASY:
                    scores[0] = scores[0]<(int) CommandCenter.getInstance().getScore()?(int) CommandCenter.getInstance().getScore():scores[0];
                    break;
                case MEDIUM:
                    scores[1] = scores[1]<(int) CommandCenter.getInstance().getScore()?(int) CommandCenter.getInstance().getScore():scores[1];
                    break;
                case HARD:
                    scores[2] = scores[2]<(int) CommandCenter.getInstance().getScore()?(int) CommandCenter.getInstance().getScore():scores[2];
                    break;

            }

            FileOutputStream fileOut = new FileOutputStream(scoreHistoryPath);
            BestScore bScore = new BestScore(scores);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(bScore);
            out.close();
        }
        catch (IOException e1) {

            e1.printStackTrace();
        }

        Graphics2D g2d = (Graphics2D)g;

        Image imgBackground = new ImageIcon(Sprite.strImageDir + "River.gif").getImage();
        g2d.drawImage(imgBackground,0,0,null);

        grpOff.setColor(Color.darkGray);
        grpOff.setFont(customFont);
        strDisplay = "YOUR SCORE : " + String.format("%05d",CommandCenter.getInstance().getScore());
        grpOff.drawString(strDisplay,(Game.DIM.width - fmt.stringWidth(strDisplay))/2 - 100, Game.DIM.height / 2 -150);
        if (CommandCenter.getInstance().finalStatus() == CommandCenter.STATUS.LOSE) {
            strDisplay = "YOU LOSE"; // Manual alignment of 40 pixels to keep text in center
            if (bPlayGameOverSound) {
                Sound.playSound("Uh-oh.wav");
                bPlayGameOverSound = false;
            }
        }
        else if (CommandCenter.getInstance().finalStatus()== CommandCenter.STATUS.WIN) {
            strDisplay = "YOU WIN"; // Manual alignment of 40 pixels to keep text in center
            if (bPlayGameOverSound) {
                Sound.playSound("Oh-yeah.wav");
                bPlayGameOverSound = false;
            }
        }
        grpOff.drawString(strDisplay,(Game.DIM.width - fmt.stringWidth(strDisplay))/2 - 50, Game.DIM.height / 2-200);


        strDisplay = "BEST RECORD";
        grpOff.drawString(strDisplay,Game.DIM.width/2 - 100, Game.DIM.height / 2 - 50);

        strDisplay = "EASY     " + String.format("%04d",scores[0]);
        grpOff.drawString(strDisplay,Game.DIM.width/2 - 125, Game.DIM.height / 2 );

        strDisplay = "MEDIUM   " + String.format("%04d",scores[1]);
        grpOff.drawString(strDisplay,Game.DIM.width/2 - 125, Game.DIM.height / 2 + 50);

        strDisplay = "HARD     " + String.format("%04d",scores[2]);
        grpOff.drawString(strDisplay,Game.DIM.width/2 - 125, Game.DIM.height / 2 + 100);


        Rectangle restartButton = new Rectangle(Game.DIM.width/2-90,560,180,40);
        Rectangle restartButtonHover = new Rectangle(Game.DIM.width/2-100,550,200,60);

        if(Game.isRestartButtonHover){
            g2d.draw(restartButtonHover);
        }
        else {
            g2d.draw(restartButton);
        }

        strDisplay = "RESTART";
        grpOff.drawString(strDisplay,Game.DIM.width/2 - 73, 590);

    }

    // Custom font for Frog
    private void loadGameFont() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File(Sprite.strFontDir + "font.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);

        } catch (java.io.IOException | java.awt.FontFormatException e)  {
            e.printStackTrace();
            System.out.println("Invalid font or font file not found");
        }
    }

    public void setbPlayGameOverSound(boolean bPlayGameOverSound) {
        this.bPlayGameOverSound = bPlayGameOverSound;
    }
}
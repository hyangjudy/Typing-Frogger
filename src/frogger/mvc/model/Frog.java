package frogger.mvc.model;

import frogger.mvc.controller.Game;
import frogger.sounds.Sound;

import javax.swing.*;
import java.awt.*;

public class Frog extends Sprite {

  private Image imgFrog = new ImageIcon(strImageDir + "Frog.gif").getImage();
  private int frogSpeed;
  private int nMarioDeadTimeLeft = 0;
  private int nDeltaY = -4; // Delta during Frog Spawn to ensure Frog comes out of pipe slowly

  public Frog(int nCenterX, int nCenterY) {
    super(nCenterX,nCenterY);
    setCenter(new Point(nCenterX, nCenterY));
    setHeight(117);
    setWidth(108);
    setDeltaY(nDeltaY);
    frogSpeed=0;
  }

  @Override
  public void draw(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;
    g2d.drawImage(imgFrog,getCenter().x,getCenter().y,null);
  }

  @Override
  public int getSpeed() {
    return frogSpeed;
  }

  public int getMarioDeadTimeLeft() {
    return nMarioDeadTimeLeft;
  }

  public void move(int leafx, int leafy) {
    super.move();
    setCenter(new Point(leafx, leafy));
  }

  public void setFrogSpeed(int frogSpeed) {
    this.frogSpeed = frogSpeed;
  }

  @Override
  public void move(){
      super.move();
      setCenter(new Point(getCenter().x + frogSpeed, getCenter().y));
      //check whether die
      if ( getCenter().x<0 || (getCenter().x + getWidth()> Game.DIM.width) ) {
          Sound.playSound("splash.wav");
          super.setDead();
      }
  }

}
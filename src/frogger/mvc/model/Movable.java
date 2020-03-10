package frogger.mvc.model;

import java.awt.*;

public interface Movable {

	//for the game to move and draw movable objects
	void move();
     void setDead();
    boolean isDead();
	void draw(Graphics g);

	//for collision detection
	Point getCenter();
	int getHeight();
	int getWidth();

    //get the speed of leaf
    int getSpeed();

} //end Movable

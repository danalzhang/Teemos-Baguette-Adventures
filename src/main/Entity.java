
package main;

import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Entity {
	
	protected double xVel;
	protected double yVel;
	protected double speed;
	protected double gravity = 0.9;
	protected Rectangle player;
	protected boolean dead;
	
	protected String standingR, standingL, jumpingR, jumpingL, walkingL1, walkingL2, walkingR1, walkingR2;
	protected String direction;
	
	protected int spriteAlt = 1;
	protected int spriteBuffer = 0;
}
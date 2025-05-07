package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.*;

import main.game;

public class Player extends Entity{

	private game g;
	private keyHandler kH;
	private double jumpSpeed = 19;
	private boolean airborne = true;
	private int buffer = 0;//buffer for acceleration
	private boolean jumping = false;
	private final int maxXVel = 6;
	
	//camera
	private final int screenX;//position of character relative to the camera
	private final int screenY;
	
	String lMD = "";
	Clip death;
	Clip pDeath;
	
	public Player(game g, keyHandler kH) {
		// sound
		try {
			
			AudioInputStream sound = AudioSystem.getAudioInputStream(new File ("death.wav"));
			death = AudioSystem.getClip();
			death.open(sound);
			sound = AudioSystem.getAudioInputStream(new File ("playerDeath.wav"));
			pDeath = AudioSystem.getClip();
			pDeath.open(sound);
		}
		catch(Exception e) {
			
		}
		
		this.g = g;
		this.kH = kH;
		
		xVel = 0;
		yVel = 0;
		player = new Rectangle((int)(g.tileSize*0),0,48,48);
		speed = 1;
		direction = "standingR";
		
		screenX = g.screenWidth/2 - (g.tileSize/2);//makes sure that the character is at center
		screenY = g.screenHeight/2 - (g.tileSize/2);
	}

	public void move() {
		if(kH.left) {
			if(buffer >= 10) {
				if(xVel >-maxXVel) 
					xVel -= speed;
				buffer =0;
			}
			else if(buffer < 7 && xVel > 0) 
				buffer+=4;
			else
				buffer ++;
			direction = "left";
			lMD = "left";
		}
		else if(kH.right) {
			if(buffer >= 10) {
				if(xVel < maxXVel) 	
					xVel += speed;
				buffer = 0;
			}
			else if(buffer < 7 && xVel < 0) {
				buffer+=4;
			}
			else
				buffer ++;
			
			direction = "right";
			lMD = "right";
		}
		else {//friction after player stops
			if(buffer >= 7) {
				if(xVel < 0) {
					direction = "slidingL";
					xVel += speed;
					
				}
				else if(xVel > 0) {
					direction = "slidingR";
					xVel -= speed;
					
				}
				buffer = 0;
			}
			else 
				buffer += 2;
			
			if(xVel == 0) {
				if(lMD == "right")
					direction = "standingR";
				else if(lMD == "left")
					direction = "standingL";
			}
		}
		if(airborne) {
			yVel -= gravity;
			if(jumping) {
				if(lMD == "right")
					direction = "jumpingR";
				else if (lMD == "left")
					direction = "jumpingL";
			}
		}
		else {
			yVel = 0;
			if(kH.jump) {
				airborne = true;
				jumping = true;
				yVel = jumpSpeed;
			}
			else
				jumping = false;
		}
		
		player.x += xVel;
		player.y -= yVel;
		
		spriteBuffer++;
		
		if(spriteBuffer > 10) {
			if(spriteAlt == 1)
				spriteAlt = 0;
			else
				spriteAlt = 1;
			spriteBuffer = 0;
		}
		
	}
	
	public void draw(Graphics2D g2) {
		String image = null;
		
		if(direction == "left") {
			if(spriteAlt == 1)
				image = "character/walkingL2.png";
			else
				image = "character/walkingL1.png";
		}
		else if(direction == "right") {
			if(spriteAlt == 1)
				image = "character/walkingR2.png";
			else
				image = "character/walkingR1.png";
		}
		else if(direction == "standingR")
			image = "character/standingR.png";
		else if(direction == "standingL")
			image = "character/standingL.png";
		else if(direction == "jumpingL")
			image = "character/jumpingL1.png";
		else if (direction == "jumpingR")
			image = "character/jumpingR1.png";
		else if (direction == "slidingL")
			image = "character/slidingL.png";
		else if (direction == "slidingR")
			image = "character/slidingR.png";
			
			int x = screenX;
			int y = screenY;
			if(screenX > player.x)
				x=player.x;
			if(screenY > player.y)
				y=player.y;
			
			int rightOffSet = g.screenWidth - screenX;
			if(rightOffSet > g.worldWidth - player.x) {
				x = g.screenWidth - (g.worldWidth - player.x);
			}
			
			int bottomOffSet = g.screenHeight - screenY;
			if(bottomOffSet > g.worldHeight - player.y) {
				y = g.screenHeight - (g.worldHeight - player.y);
			}
			
			g2.drawImage(Toolkit.getDefaultToolkit().getImage(image), x, y, g.tileSize,g.tileSize, null);
	
	}
	
	void checkCollision(Tile block) {
		//check if rect touches wall
		Rectangle tile = block.getBlockHitBox();
		if(player.intersects(tile)) {
			//stop the rect from moving
			double left1 = player.getX();
			double right1 = player.getX() + player.getWidth();
			double top1 = player.getY();
			double bottom1 = player.getY() + player.getHeight();
			double left2 = tile.getX();
			double right2 = tile.getX() + tile.getWidth();
			double top2 = tile.getY();
			double bottom2 = tile.getY() + tile.getHeight();
			
			if(right1 > left2 && 
			   left1 < left2 && 
			   right1 - left2 < bottom1 - top2 && 
			   right1 - left2 < bottom2 - top1) {
	            //rect collides from left side of the wall
				if(block.getCollision()) {
					player.x = tile.x - player.width;
				}
				else if(block.getBlockNum()==9||block.getBlockNum()==4) {
					dead = true;
	        	}
				else if(block.getBlockNum()== 10 && g.mapNum <3) {
					g.mapNum += 1;
					player.x = 0;
					player.y = 50;
					airborne = true;
					g.changeWorld = true;
				}
				else if(block.getBlockNum () == 10 && g.mapNum == 3) {
					g.gameState = g.endState;
				}
				else
					airborne = true;
	        }
	        else if(left1 < right2 &&
	        		right1 > right2 && 
	        		right2 - left1 < bottom1 - top2 && 
	        		right2 - left1 < bottom2 - top1) {
	            //rect collides from right side of the wall
	        	if(block.getCollision()) {
	        		player.x = tile.x + tile.width;
	        	}
	        	else if(block.getBlockNum()==9||block.getBlockNum()==4) {
	        		dead = true;
	        	}
	        	else if(block.getBlockNum()== 10 && g.mapNum <3) {
					g.mapNum += 1;
					player.x = 0;
					player.y = 50;
					airborne = true;
					g.changeWorld = true;
				}
				else if(block.getBlockNum () == 10 && g.mapNum == 3) {
					g.gameState = g.endState;
				}
	        	else
	        		airborne = true;
	        }
	        else if(bottom1 > top2 && top1 < top2 ){
	            //rect collides from top side of the wall
	        	if(block.getCollision()) {
	        		player.y = tile.y - player.height;
	        		airborne = false;
	        	}
	        	else if(block.getBlockNum()==9||block.getBlockNum()==4) {
	        		dead = true;
	        	}
	        	else if(block.getBlockNum()== 10 && g.mapNum <3) {
					g.mapNum += 1;
					player.x = 0;
					player.y = 50;
					airborne = true;
					g.changeWorld = true;
				}
				else if(block.getBlockNum () == 10 && g.mapNum == 3) {
					g.gameState = g.endState;
				}
	        	else
	        		airborne = true;
	        	
	        }
	        else if(top1 < bottom2 && bottom1 > bottom2){
	            //rect collides from bottom side of the wall
	        
		        if(block.getCollision()) {
		        	player.y = tile.y + tile.height;
		        	airborne = true;
		        }
		        else if(block.getBlockNum()==9||block.getBlockNum()==4) {
		        	dead = true;
	        	}
		        else if(block.getBlockNum()== 10 && g.mapNum <3) {
					g.mapNum += 1;
					player.x = 0;
					player.y = 50;
					airborne = true;
					g.changeWorld = true;
				}
				else if(block.getBlockNum () == 10 && g.mapNum == 3) {
					g.gameState = g.endState;
				}
		        else
		        	airborne = true;
	        }
		}
	
		
	}
	
	void collisionEnemy(Enemy e) {// if player hits an enemy
		Rectangle enemy = e.player;
		if(player.intersects(enemy)) {
			double top1 = player.getY();
			double bottom1 = player.getY() + player.getHeight();
			double top2 = enemy.getY();

			// if stomps the top of the head, enemy dies
	        if(bottom1 > top2 && top1 < top2 ) {
			    // death sound
				death.stop();
			    death.setFramePosition(0);
				death.start();
				
        		player.y = enemy.y - player.height;
        		e.dead = true;
        		yVel = 5;
        		airborne = false;
        	}	     
	        // otherwise, player dies and starts at the beginning
	        else {
	        	dead = true;
	        }
 	        
		}
	}
	
	void keepInBound() {
		if(player.x < 0) {
			player.x = 0;
		}
		else if(player.x > g.worldWidth - player.width) {
			player.x = g.worldWidth - player.width;
		}
		
		if(player.y < 0) {
			player.y = 0;
			yVel = 0;
		}else if(player.y > g.worldHeight - player.height) {
			player.y = g.worldHeight - player.height;
			airborne = false;
			yVel = 0;
			dead = true;
		}
	}
	
	void playerDead () {
	    // player death sound
		pDeath.stop();
	    pDeath.setFramePosition(0);
		pDeath.start();
		
		g.ui.hello.stop();
		try {
			Thread.sleep(3000);
			g.ui.hello.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		player.y = 0;
		player.x = 0;
		airborne = true;
	}
	
	//getters and setters
	public int getScreenX() {
		return screenX;
	}
	public int getScreenY() {
		return screenY;
	}

}
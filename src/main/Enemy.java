package main;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Enemy extends Entity{

	private int enemyNumber;
	private game g;
	private boolean airborne;
	
	public Enemy(game g, Rectangle player, int enemyNumber) {
		dead = false;
		this.g = g;
		this.player = player;
		direction = "left";
		speed = 3;
		airborne = false;
	}
	
	public void move() {
		if (direction.equals("left")) {
			xVel = -speed;
		}
		else if (direction.equals("right")) {
			xVel = speed;
		}
		else if(direction.equals("down")) {
			airborne = true;
		}
		
		if(airborne) {
			yVel -= gravity;
		}
		else {
			yVel = 0;
		}
		
		player.x += xVel;
		player.y -= yVel;
	}
	
	public void draw(Graphics2D g2) {
		String image = null;
		
		if(direction.equals("left")) {
			image = "enemyL.png";
		}
		else if(direction.equals("right")) {
			image = "enemyR.png";
		}
		
		int xPosition = player.x - g.player1.player.x + g.player1.getScreenX();
		int yPosition = player.y - g.player1.player.y + g.player1.getScreenY();
		
		if(g.player1.getScreenX() > g.player1.player.x)
			xPosition= player.x;
		if(g.player1.getScreenY() > g.player1.player.y)
			yPosition= player.y;
		
		int rightOffSet = g.screenWidth - g.player1.getScreenX();
		if(rightOffSet > g.worldWidth - g.player1.player.x) {
			xPosition = g.screenWidth - (g.worldWidth - player.x);
		}
		
		int bottomOffSet = g.screenHeight - g.player1.getScreenY();
		if(bottomOffSet > g.worldHeight - g.player1.player.y) {
			yPosition = g.screenHeight - (g.worldHeight - player.y);
		}
		
		g2.drawImage(Toolkit.getDefaultToolkit().getImage(image), xPosition, yPosition, g.tileSize,g.tileSize, null);
		
	}
	
	public void checkCollision(Tile block) {
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
					direction="left";
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
	        		direction="right";
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
	        	else
	        		airborne = true;
	        	
	        }
	        else if(top1 < bottom2 && bottom1 > bottom2){
	            //rect collides from bottom side of the wall
	        
		        if(block.getCollision()) {
		        	player.y = tile.y + tile.height;
		        	airborne = true;
		        }
		        else
		        	airborne = true;
	        }
		}
	}
	
	public void keepInBound() {
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
	
	public boolean equals(Object o) {
		Enemy e = (Enemy)o;
		return this.enemyNumber == e.enemyNumber;
	}
	
}

package main;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Tile {

	private Image image;
	private Rectangle blockHitBox;
	private boolean collision;
	private int blockNum;
	
	public Tile(Image image, Rectangle blockHitBox, boolean collision, int blockNum) {
		this.setImage(image);
		this.setBlockHitBox(blockHitBox);
		this.setCollision(collision);
		this.setBlockNum(blockNum);
	}

	//getters and setters
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Rectangle getBlockHitBox() {
		return blockHitBox;
	}

	public void setBlockHitBox(Rectangle blockHitBox) {
		this.blockHitBox = blockHitBox;
	}

	public boolean getCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public int getBlockNum() {
		return blockNum;
	}

	public void setBlockNum(int blockNum) {
		this.blockNum = blockNum;
	}
	

	
}
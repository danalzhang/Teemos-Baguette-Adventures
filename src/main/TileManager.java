package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import main.game;

public class TileManager {
	
	private game g;
	private Image[] tile;
	private int mapTileNum[][];
	
	ArrayList<Tile> tiles = new ArrayList<>();
	
	public TileManager(game g) {
		
		this.g = g;
		
		tile = new Image[12];
		mapTileNum = new int[g.maxWorldCol][g.maxWorldRow];
		
		getTileImage();
		
	}
	
	public void getTileImage() {
		tile[0] = Toolkit.getDefaultToolkit().getImage("Tiles/water00.png");
		
		tile[1] = Toolkit.getDefaultToolkit().getImage("Tiles/wall.png");
		
		tile[2] = Toolkit.getDefaultToolkit().getImage("Tiles/road02.png");
		
		tile[3] = Toolkit.getDefaultToolkit().getImage("Tiles/grass00.png");
		
		tile[4] = Toolkit.getDefaultToolkit().getImage("Tiles/spike.png");
		
		tile[5] = Toolkit.getDefaultToolkit().getImage("Tiles/mossyWall.png");
		
		tile[6] =  Toolkit.getDefaultToolkit().getImage("Tiles/cloud.png");
		
		tile[7] = Toolkit.getDefaultToolkit().getImage("Tiles/wallBack.png");
		
		tile[8] = Toolkit.getDefaultToolkit().getImage("Tiles/castleTorch.png");
		
		tile[9] = Toolkit.getDefaultToolkit().getImage("Tiles/flame.png");
		
		tile[10] = Toolkit.getDefaultToolkit().getImage("Tiles/bread1.png");
		
	}
	
	public void loadMap() {
		
			try {
				BufferedReader mapReader = new BufferedReader(new FileReader("map_"+g.mapNum+".txt"));
				int row = 0;
				int col = 0;
				
				while(row < g.maxWorldRow) {
					String line = mapReader.readLine();
					StringTokenizer importMapNum = new StringTokenizer(line);
					while (col < g.maxWorldCol) {
						int x =Integer.parseInt(importMapNum.nextToken());
						mapTileNum[col][row] = x;
						
						if(tiles.size()<3696) {
							if(x==1||x==2||x==5||x==6)
								tiles.add(new Tile(tile[x], new Rectangle(col*g.tileSize,row*g.tileSize,g.tileSize,g.tileSize), true, x));
							else
								tiles.add(new Tile(tile[x], new Rectangle(col*g.tileSize,row*g.tileSize,g.tileSize,g.tileSize), false, x));
						}
							
						
						col++;
			
					}
					if(col == g.maxWorldCol) {//resetting col and starts at 0 and the next row.
						col = 0;
						row ++;
					}
				}
				mapReader.close();
				
			}
			catch(Exception e) {
				
			}
		
	}
	
	public void draw(Graphics2D g2) {
		
		
		int x = 0;//position of the current block
		int y =0;
		
		while(x < g.maxWorldCol && y < g.maxWorldRow) {
			
			int tileNum = mapTileNum[x][y];
			

			int worldX = x * g.tileSize;//the x position of the block
			int worldY = y * g.tileSize;//the y position of the block
			
			//the displayed position of the block. Instead of drawing a moving player, it draws a moving map
			int screenX = worldX - g.player1.player.x + g.player1.getScreenX();
			int screenY = worldY - g.player1.player.y + g.player1.getScreenY();
			
			//confining camera so it doesn't go out of bounds
			if(g.player1.getScreenX() > g.player1.player.x) {
				screenX = worldX;
			}
			if(g.player1.getScreenY() > g.player1.player.y) {
				screenY = worldY;
			}
			
			int rightOffSet = g.screenWidth - g.player1.getScreenX();
			if(rightOffSet > g.worldWidth - g.player1.player.x) {
				screenX = g.screenWidth - (g.worldWidth - worldX);
			}
			
			int bottomOffSet = g.screenHeight - g.player1.getScreenY();
			if(bottomOffSet > g.worldHeight - g.player1.player.y) {
				screenY = g.screenHeight - (g.worldHeight - worldY);
			}
			//
			if(worldX + g.tileSize> g.player1.player.x - g.player1.getScreenX() && worldX - g.tileSize < g.player1.player.x + g.player1.getScreenX() &&
			   worldY + g.tileSize> g.player1.player.y - g.player1.getScreenY() && worldY - g.tileSize< g.player1.player.y + g.player1.getScreenY()) {		
				g2.drawImage(tile[tileNum],screenX,screenY,g.tileSize,g.tileSize,null);
			}//reduce rendering and drawing out the whole map for better performance
			else if(g.player1.getScreenX() > g.player1.player.x||g.player1.getScreenX() > g.player1.player.y||
					rightOffSet > g.worldWidth - g.player1.player.x	|| bottomOffSet > g.worldHeight - g.player1.player.y) {
				g2.drawImage(tile[tileNum],screenX,screenY,g.tileSize,g.tileSize,null);
			}
			
			x++;
			
			if(x==g.maxWorldCol) {
				x = 0;
				y++;
			}
		}
		
	}
	public ArrayList<Tile>getTiles(){
		return tiles;
	}
	
}
package main;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.sound.sampled.*;

public class UI implements ChangeListener{
	
	game g;
	Font font;
	public Graphics2D g2;
	public int commandNum = 0;
	
	JSlider slider;
	JSlider slider1;
	
	Clip hello;
	static Calendar cal = Calendar.getInstance();
	
	public UI(game g) {
		this.g = g;
		try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("patrick.ttf"));
            g.setFont(font.deriveFont(80f));
        } 
		catch (IOException | FontFormatException e) {
        }
		catch (Exception e) {
		}
		// music slider
		slider = new JSlider(-40,6);
		slider.setPreferredSize(new Dimension (500,50));
		slider.setPaintTrack(true);
		slider.setOpaque(true);
		slider.setBackground(new Color(60,120,60));
		slider.setMajorTickSpacing(25);
		slider.addChangeListener(this);
		// sfx slider
		slider1 = new JSlider(-40,6);
		slider1.setPreferredSize(new Dimension (500,50));
		slider1.setPaintTrack(true);
		slider1.setOpaque(true);
		slider1.setBackground(new Color(60,120,60));
		slider1.addChangeListener(this);
		
		// sound
		try {
			
			AudioInputStream sound = AudioSystem.getAudioInputStream(new File ("music.wav"));
			hello = AudioSystem.getClip();
			hello.open(sound);
		} 
		catch (Exception e) {
		}
		hello.loop(Clip.LOOP_CONTINUOUSLY);
		hello.start();
	}

	
	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		
		if(g.gameState == g.titleState) {
			drawTitleScreen();
		}
		if(g.gameState == g.playState) {
			drawStats();
		}
		if(g.gameState == g.instructionState) {
			drawInstructionScreen();
		}
		if(g.gameState == g.leaderboardState) {
			drawLeaderboardScreen();
		}
		if(g.gameState == g.settingsState) {
			drawSettingsScreen();
			slider.setLocation((int)(g.tileSize*6.7),(int) (g.tileSize*8.2));
			g.add(slider);
			slider1.setLocation((int)(g.tileSize*6.7),(int) (g.tileSize*11.3));
			g.add(slider1);
			
			// music label
			String text = "Music:";
			g2.setFont(g2.getFont().deriveFont(Font.BOLD,50F));
			int x = centerText(text);
			int y = (int) (g.tileSize*7.7);
		
			g2.setColor(Color.black);
			g2.drawString(text,x+5,y+5);
			
			g2.setFont(g2.getFont().deriveFont(Font.BOLD,50F));
			g2.setColor(Color.white);
			g2.drawString(text,x,y);
			
			// SFX
			text = "SFX:";
			g2.setFont(g2.getFont().deriveFont(Font.BOLD,50F));
			x = centerText(text);
			y = (int) (g.tileSize*10.8);
		
			g2.setColor(Color.black);
			g2.drawString(text,x+5,y+5);
			
			g2.setFont(g2.getFont().deriveFont(Font.BOLD,50F));
			g2.setColor(Color.white);
			g2.drawString(text,x,y);
			
		}
		if(g.gameState == g.creditsState) {
			drawCreditsScreen();
		}
		if(g.gameState == g.endState) {
			drawEndScreen();
		}
	}
	
	public void drawTitleScreen() {
		// set background
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("background2.jpg"),0,0,g.screenWidth, g.screenHeight,null);
		
		// teemo
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("teemo.png"),g.tileSize*18,0,g.tileSize*6,g.tileSize*6,null);
		
		// baguette
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("baguettes.png"),g.tileSize*16,g.tileSize*9,g.tileSize*3,g.tileSize*3,null);

		// Title Name
		g.setFont(g2.getFont().deriveFont(Font.BOLD,110F));
		String text = "Teemo's";
		String text1 = "Baguette";
		String text2 = "Adventure";
		int x = g.tileSize;
		int y = g.tileSize*3;
		
		// draw oval
		g2.setColor(new Color(60,120,60));
		g2.fillOval(-920,-280,1500,1500);
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(6));
		g2.drawOval(-920,-280,1500,1500);
	
		// shadow title color
		g2.setColor(Color.white);
		g2.drawString(text,x+5,y+5);
		g2.drawString(text1,x+5,y+125);
		g2.drawString(text2,x+5,y+245);
		
		// main title color
		g2.setColor(Color.black);
		g2.drawString(text,x,y);
		g2.drawString(text1,x,y+120);
		g2.drawString(text2,x+5,y+240);
	
		// seperating lines
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(10));
		g2.drawLine(28,420,g.tileSize*11,420);
		g2.drawLine(28,820,g.tileSize*9,820);
		
		// MENU
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,50F));
		
		// play button
		text = "PLAY";
		x = g.tileSize*2;
		y += g.tileSize*7;
		if(commandNum == 0) {
			g2.setColor(Color.white);
			g2.drawString(">",x-g.tileSize,y);
			g2.setColor(Color.white);
			g2.drawString(text,x+5,y+5);
			g2.setColor(Color.black);
			g2.drawString (text,x,y);
		}
		else{
			g2.setColor(Color.black);
			g2.drawString(text,x+5,y+5);
			g2.setColor(Color.white);
			g2.drawString (text,x,y);
		}
		
		// how to play button
		text = "HOW TO PLAY";
		y += g.tileSize*1.3;
		if(commandNum == 1) {
			g2.drawString(">",x-g.tileSize,y);
			g2.setColor(Color.white);
			g2.drawString(text,x+5,y+5);
			g2.setColor(Color.black);
			g2.drawString (text,x,y);
		}
		else {
			g2.setColor(Color.black);
			g2.drawString(text,x+5,y+5);
			g2.setColor(Color.white);
			g2.drawString (text,x,y);
		}
		
		// leaderboard button
		text = "LEADERBOARDS";
		y += g.tileSize*1.3;
		if(commandNum == 2) {
			g2.drawString(">",x-g.tileSize,y);
			g2.setColor(Color.white);
			g2.drawString(text,x+5,y+5);
			g2.setColor(Color.black);
			g2.drawString (text,x,y);
		}
		else {
			g2.setColor(Color.black);
			g2.drawString(text,x+5,y+5);
			g2.setColor(Color.white);
			g2.drawString (text,x,y);
		}
		
		// setting button
		text = "SETTINGS";
		y += g.tileSize*1.3;
		if(commandNum == 3) {
			g2.drawString(">",x-g.tileSize,y);
			g2.setColor(Color.white);
			g2.drawString(text,x+5,y+5);
			g2.setColor(Color.black);
			g2.drawString (text,x,y);
		}
		else {
			g2.setColor(Color.black);
			g2.drawString(text,x+5,y+5);
			g2.setColor(Color.white);
			g2.drawString (text,x,y);
		}
		
		// credit button
		text = "CREDITS";
		y += g.tileSize*1.3;
		if(commandNum == 4) {
			g2.drawString(">",x-g.tileSize,y);
			g2.setColor(Color.white);
			g2.drawString(text,x+5,y+5);
			g2.setColor(Color.black);
			g2.drawString (text,x,y);
		}
		else {
			g2.setColor(Color.black);
			g2.drawString(text,x+5,y+5);
			g2.setColor(Color.white);
			g2.drawString (text,x,y);
		}
		
		// exit button
		text = "EXIT";
		y += g.tileSize*1.3;
		if(commandNum == 5) {
			g2.drawString(">",x-g.tileSize,y);
			g2.setColor(Color.white);
			g2.drawString(text,x+5,y+5);
			g2.setColor(Color.black);
			g2.drawString (text,x,y);
		}
		else {
			g2.setColor(Color.black);
			g2.drawString(text,x+5,y+5);
			g2.setColor(Color.white);
			g2.drawString (text,x,y);
		}
	}
	
	public void drawInstructionScreen() {
		
		// set background
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("background2.jpg"),0,0,g.screenWidth, g.screenHeight,null);
		
		// draw rectangle
		g2.setColor(Color.black);
		g2.fillRoundRect(g.tileSize*3,g.tileSize,g.screenWidth-g.tileSize*6,g.screenHeight-g.tileSize*2,50,50);
		
		g2.setColor(new Color(60,120,60));
		int x = (int) (g.tileSize*3.5);
		int y = (int) (g.tileSize*1.5);
		g2.fillRoundRect(x,y,g.screenWidth-x*2,g.screenHeight-y*2,50,50);
		
		// Title Name
		g.setFont(g2.getFont().deriveFont(Font.BOLD,60F));
		String text = "HOW TO PLAY";
		x = centerText(text);
		y = g.tileSize*3;
	
		// shadow title color
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		// main title color
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
	
		// seperating lines
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(10));
		x = (int) (g.tileSize*4.5);
		y = (int) (g.tileSize*3.5);
		g2.drawLine(x*2,y,g.screenWidth - x*2, y);
		
		// rectangle
		g2.setColor(Color.black);
		g2.fillRoundRect((int) (g.tileSize*4),(int) (g.tileSize*4),(int) (g.tileSize*16),(int) (g.tileSize*4.5),50,50);
		
		// goal instructions
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,35F));
		text = "GOAL: REACH THE BAGUETTE BEFORE TIME RUNS OUT!";
		String text1 = "1. AVOID ALL MONSTERS";
		String text2 = "2. DEFEAT ENEMIES BY JUMPING ON THEIR HEAD";
		String text3 = "3. YOUR SCORE WILL BE TALLIED AT THE END BASED ON TIME";
		
		x = centerText(text);
		y += g.tileSize*1.5;
	
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,35F));
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		x = centerText(text1);
		y += g.tileSize;
	
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,30F));
		g2.setColor(Color.black);
		g2.drawString(text1,x+5,y+5);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,30F));
		g2.setColor(Color.white);
		g2.drawString(text1,x,y);
		
		x = centerText(text2);
		y += g.tileSize;
	
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,30F));
		g2.setColor(Color.black);
		g2.drawString(text2,x+5,y+5);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,30F));
		g2.setColor(Color.white);
		g2.drawString(text2,x,y);
		
		x = centerText(text3);
		y += g.tileSize;
	
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,30F));
		g2.setColor(Color.black);
		g2.drawString(text3,x+5,y+5);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,30F));
		g2.setColor(Color.white);
		g2.drawString(text3,x,y);
		
		
		// wasd key image + instructions
		y += g.tileSize*2.5;
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("key.png"),g.tileSize*5,y-g.tileSize*3,g.tileSize*5, g.tileSize*5,null);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,35F));
		text = "USE WASD KEYS TO MOVE";
		x = centerText(text) + g.tileSize*2;
	
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		// esc key image + instructions
		y += g.tileSize*2.5;
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("escape.png"),g.tileSize*4,y-g.tileSize*2,g.tileSize*4, g.tileSize*4,null);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,35F));
		text = "PRESS ESCAPE TO PAUSE THE GAME";
		x = centerText(text) + g.tileSize;
		y += g.tileSize*0.5;
	
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		// leave how to play instructions
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,25F));
		text = "-=-=-=-=-=-=-=- PRESS ESC TO GO BACK TO MAIN MENU -=-=-=-=-=-=-=-";
		x = centerText(text);
		y += g.tileSize*2.5;
		
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
	
	}
	
	public void drawLeaderboardScreen() {
		// set background
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("background2.jpg"),0,0,g.screenWidth, g.screenHeight,null);
		
		// draw rectangle
		g2.setColor(Color.black);
		g2.fillRoundRect(g.tileSize*3,g.tileSize,g.screenWidth-g.tileSize*6,g.screenHeight-g.tileSize*2,50,50);
		
		g2.setColor(new Color(60,120,60));
		int x = (int) (g.tileSize*3.5);
		int y = (int) (g.tileSize*1.5);
		g2.fillRoundRect(x,y,g.screenWidth-x*2,g.screenHeight-y*2,50,50);
		
		// Title Name
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,60F));
		String text = "LEADERBOARDS";
		x = centerText(text);
		y = g.tileSize*3;
	
		// shadow title color
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		// main title color
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		// seperating lines
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(10));
		x = (int) (g.tileSize*4.5);
		y = (int) (g.tileSize*3.5);
		g2.drawLine(x*2,y,g.screenWidth - x*2, y);
		
		// numbers
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,50F));
		text = "1.";
		x = g.tileSize*5;
		y = (int)(g.tileSize*6.5);
	
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		text = "2.";
		y = (int)(g.tileSize*8.5);
	
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		text = "3.";
		y = (int)(g.tileSize*10.5);
	
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		text = "4.";
		y = (int)(g.tileSize*12.5);
	
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		text = "5.";
		y = (int)(g.tileSize*14.5);
	
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		// Labels
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,50F));
		text = "DATE";
		x = g.tileSize*9;
		y = g.tileSize*5;
	
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		text = "SCORE";
		x = g.tileSize * 15;
		y = g.tileSize*5;
	
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		// Load Leaderboard - Dates
		x = g.tileSize*7;
		y = (int)(g.tileSize*4.5);
		for(int i = 0; i < 5; i++) {
			if(i < g.sortedScores.size()) {
				g2.setFont(g2.getFont().deriveFont(Font.BOLD,35F));
				text = g.sortedScores.get(i).getDate();
				y += (int)(g.tileSize*2);
			
				g2.setColor(Color.black);
				g2.drawString(text,x+5,y+5);
				
				g2.setColor(Color.white);
				g2.drawString(text,x,y);
			}
		}
		
		// Load Leaderboard - Dates
		x = g.tileSize*16;
		y = (int)(g.tileSize*4.5);
		for(int i = 0; i < 5; i++) {
			if(i < g.sortedScores.size()) {
				g2.setFont(g2.getFont().deriveFont(Font.BOLD,35F));
				text = Integer.toString(g.sortedScores.get(i).getScore());
				y += (int)(g.tileSize*2);
			
				g2.setColor(Color.black);
				g2.drawString(text,x+5,y+5);
				
				g2.setColor(Color.white);
				g2.drawString(text,x,y);
			}
		}
		
		
				
	}
	
	public void drawSettingsScreen() {
		// set background
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("background2.jpg"),0,0,g.screenWidth, g.screenHeight,null);
		
		// draw rectangle
		g2.setColor(Color.black);
		g2.fillRoundRect(g.tileSize*5,g.tileSize*3,g.screenWidth-g.tileSize*10,g.screenHeight-g.tileSize*6,50,50);
		
		g2.setColor(new Color(60,120,60));
		int x = (int) (g.tileSize*5.5);
		int y = (int) (g.tileSize*3.5);
		g2.fillRoundRect(x,y,g.screenWidth-x*2,g.screenHeight-y*2,50,50);
		
		// Title Name
		g.setFont(g2.getFont().deriveFont(Font.BOLD,60F));
		String text = "SETTINGS";
		x = centerText(text);
		y = g.tileSize*5;
	
		// shadow title color
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		// main title color
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		// seperating lines
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(10));
		x = (int) (g.tileSize*4.5);
		y = (int) (g.tileSize*5.5);
		g2.drawLine(x*2,y,g.screenWidth - x*2, y);
		
		// leave how to play instructions
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,25F));
		text = "-=-=-=- PRESS ESC TO GO BACK TO MAIN MENU -=-=-=-";
		x = centerText(text);
		y = g.tileSize*14;
		
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
	}
	
	public void drawCreditsScreen() {
		// set background
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("background2.jpg"),0,0,g.screenWidth, g.screenHeight,null);
		
		// draw rectangle
		g2.setColor(Color.black);
		g2.fillRoundRect(g.tileSize*5,g.tileSize*3,g.screenWidth-g.tileSize*10,g.screenHeight-g.tileSize*6,50,50);
		
		g2.setColor(new Color(60,120,60));
		int x = (int) (g.tileSize*5.5);
		int y = (int) (g.tileSize*3.5);
		g2.fillRoundRect(x,y,g.screenWidth-x*2,g.screenHeight-y*2,50,50);
		
		// Title Name
		g.setFont(g2.getFont().deriveFont(Font.BOLD,60F));
		String text = "CREDITS";
		x = centerText(text);
		y = g.tileSize*5;
		
		// shadow title color
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		// main title color
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		// seperating lines
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(10));
		x = (int) (g.tileSize*4.5);
		y = (int) (g.tileSize*5.5);
		g2.drawLine(x*2,y,g.screenWidth - x*2, y);
		
		// text
		text = "MADE BY:";
		x = centerText(text);
		y += g.tileSize*2;

		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		text = "SAMI PENG(UIN)";
		x = centerText(text);
		y += g.tileSize*2;

		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		text = "DANIEL ZHANG";
		x = centerText(text);
		y += g.tileSize*2;

		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);

		// leave how to play inStructions
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,25F));
		text = "=-=-=-=- PRESS ESC TO GO BACK TO MAIN MENU -=-=-=-=";
		x = centerText(text);
		y += g.tileSize*2.5;
		
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
	}
	
	public void drawPauseScreen() {
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,110F));
		int x = centerText("GAME PAUSED");
		int y = g.tileSize*8;
		g2.setColor(Color.black);
		g2.drawString("GAME PAUSED",x,y);
		g2.setColor(Color.white);
		g2.drawString("GAME PAUSED",x+5,y+5);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,60F));
		y += g.tileSize*2;
		g2.setColor(Color.black);
		g2.drawString("PRESS ESC TO UNPAUSE",x,y);
		g2.setColor(Color.white);
		g2.drawString("PRESS ESC TO UNPAUSE",x+5,y+5);
		
	}
	
	public void drawStats() {
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));
		g2.setColor(Color.white);
		g2.drawString("World 1-" + g.mapNum,200,50);
		
		g2.drawString("Score = 0000", 450, 50);
		
		g2.drawString("Time Left = " + g.timer.time/10, 750, 50);
		
	}
	
	public void drawTimerEndScreen() {

		g2.setFont(g2.getFont().deriveFont(Font.BOLD,110F));
		String text = "TIME HAS RUN OUT :(";
		int x = centerText(text);
		int y = g.tileSize*8;
		g2.setColor(Color.black);
		g2.drawString(text,x,y);
		g2.setColor(Color.white);
		g2.drawString(text,x+5,y+5);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,60F));
		y += g.tileSize*2;
		g2.setColor(Color.black);
		g2.drawString("PRESS ESC TO RETURN TO TITLE SCREEN",x,y);
		g2.setColor(Color.white);
		g2.drawString("PRESS ESC TO RETURN TO TITLE SCREEN",x+5,y+5);
		
	}
	
	public void drawEndScreen() {
		//SET BACKGROUND AND FONT
		g.setBackground(new Color(60,120,60));
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,90F));
		
		//TEXT
		String text = "CONGRAGULATIONS";
		int x = centerText(text);
		int y = g.tileSize*4;
		g2.setColor(Color.black);
		g2.drawString(text,x,y);
		g2.setColor(Color.white);
		g2.drawString(text,x+5,y+5);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));
		text = "YOU HAVE BEATEN THE GAME! U ARE EPIC GAMER FO SHO";
		x = centerText(text);
		y = g.tileSize*6;
		g2.setColor(Color.black);
		g2.drawString(text,x,y);
		g2.setColor(Color.white);
		g2.drawString(text,x+5,y+5);
		
		// teemo pic
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("standingL.png"),g.tileSize*11,g.tileSize*7,g.tileSize*3,g.tileSize*3,null);
		
		// display player score and date
		text = "DATE ACCOMPLISHED: " + g.sortedScores.get(g.sortedScores.size()-1).getDate();
		x = centerText(text);
		y = g.tileSize*12;
		g2.setColor(Color.black);
		g2.drawString(text,x,y);
		g2.setColor(Color.white);
		g2.drawString(text,x+5,y+5);

		text = "YOUR FINAL SCORE IS: " + g.sortedScores.get(g.sortedScores.size()-1).getScore();
		x = centerText(text);
		y = g.tileSize*14;
		g2.setColor(Color.black);
		g2.drawString(text,x,y);
		g2.setColor(Color.white);
		g2.drawString(text,x+5,y+5);
		
		// leave how to play inStructions
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,25F));
		text = "-=-=-=-=-=-=-=-=-=-=- PRESS ESC TO GO BACK TO MAIN MENU -=-=-=-=-=-=-=-=-=-=-";
		x = centerText(text);
		y = g.tileSize * 16;
		
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
	}
	
	public int centerText(String text) {
		int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
		int x = g.screenWidth/2 - length/2;
		return x;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
        	int value = (int)source.getValue();

        	if(value == -35) {
        		value =-80;
        	}
        	if (source.equals(slider)) {
	        	FloatControl gainControl = (FloatControl) hello.getControl(FloatControl.Type.MASTER_GAIN);
	        	gainControl.setValue(value);
        	}
        	else if(source.equals(slider1)) {
	        	FloatControl gainControl = (FloatControl) g.key.jumpSound.getControl(FloatControl.Type.MASTER_GAIN);
	        	gainControl.setValue(value);
	        	FloatControl gainControl1 = (FloatControl) g.player1.pDeath.getControl(FloatControl.Type.MASTER_GAIN);
	        	gainControl1.setValue(value);
	        	FloatControl gainControl2 = (FloatControl) g.player1.death.getControl(FloatControl.Type.MASTER_GAIN);
	        	gainControl2.setValue(value);
        	}

        }
	}
}

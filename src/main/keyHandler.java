package main;

import java.awt.event.*;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class keyHandler implements KeyListener {

	game g;
	public boolean left, right, jump; 
	Clip jumpSound;
	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public keyHandler(game g) {
		this.g = g;
		
		// sound
		try {
			
			AudioInputStream sound = AudioSystem.getAudioInputStream(new File ("jump.wav"));
			jumpSound = AudioSystem.getClip();
			jumpSound.open(sound);
		} 
		catch (Exception e) {
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		// title user controls
		if(g.gameState == g.titleState) {
			if(key == KeyEvent.VK_W) {
				g.ui.commandNum--;
				if(g.ui.commandNum < 0) {
					g.ui.commandNum = 5;
				}
			}
			if(key == KeyEvent.VK_S) {
				g.ui.commandNum++;
				if(g.ui.commandNum > 5) {
					g.ui.commandNum = 0;
				}
			}
			// user selects an option
			if(key == KeyEvent.VK_ENTER) {
				if(g.ui.commandNum == 0) {
					g.gameState = g.playState;
					g.allowMove = true;
				}
				if(g.ui.commandNum == 1) {
					g.gameState = g.instructionState;
				}
				if(g.ui.commandNum == 2) {
					g.gameState = g.leaderboardState;
				}
				if(g.ui.commandNum == 3) {
					g.gameState = g.settingsState;
				}
				if(g.ui.commandNum == 4) {
					g.gameState = g.creditsState;
				}
				if(g.ui.commandNum == 5) {
					System.exit(0);
				}
			}
		}
		
		// exit the current screen
		if(g.gameState == g.playState || g.gameState == g.instructionState || g.gameState == g.leaderboardState || g.gameState == g.settingsState || g.gameState == g.creditsState) {
			if(key == KeyEvent.VK_ESCAPE) {
				if(g.gameState == g.playState) {
					if (g.allowMove) {
						g.allowMove = false;
						g.timer.timer.stop();
					}
					else if(g.allowMove == false && g.timer.time != 0) {
						g.allowMove = true;
						g.timer.timer.stop();
					}
					else if(g.allowMove == false && g.timer.time == 0) {
						g.gameState = g.titleState;
						g.timer.time = 2000;
						g.player1.player.x = 0;
						g.player1.player.y = 0;
						g.mapNum = 1;
						g.changeWorld = true;
						g.currentScore = 0;
					}
				}
				else {
					g.gameState = g.titleState;
					g.allowMove = false;
					g.removeAll();
				}
			}
		}
		if(g.gameState == g.endState) {
			if(key == KeyEvent.VK_ESCAPE) {
				g.gameState = g.titleState;
				g.timer.time = 2000;
				g.player1.player.x = 0;
				g.player1.player.y = 0;
				g.mapNum = 1;
				g.changeWorld = true;
				g.currentScore = 0;
				g.ui.hello.stop();
				g.ui.hello.setFramePosition(0);
				g.ui.hello.start();
			}
		}
		
		if (g.gameState == g.playState) {
			// game movement
			if(key == KeyEvent.VK_A) {
				left = true;
				right = false;
			}
			else if(key == KeyEvent.VK_D) {
				right = true;
				left = false;
			}
			else if(key == KeyEvent.VK_W) {
				if(g.allowMove) {
				    // jump sound
					jumpSound.stop();
				    jumpSound.setFramePosition(0);
					jumpSound.start();
				}
				jump = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_A) {
			left = false;
		}else if(key == KeyEvent.VK_D) {
			right = false;
		}else if(key == KeyEvent.VK_W) {
			jump = false;
		}
	}
}

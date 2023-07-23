package main;
import java.util.*;
import java.util.Timer;
import java.awt.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class game extends JPanel implements Runnable{
	
	final int TileSize = 16;
	final int scale = 3;
	public boolean changeWorld;
	
	public final int tileSize = TileSize * scale;//48x48 
	public final int maxScreenCol = 24;
	public final int maxScreenRow = 18;
	public final int screenWidth = tileSize*maxScreenCol;//1152
	public final int screenHeight = tileSize*maxScreenRow;//864
	
	public final int maxWorldCol = 168;
	public final int maxWorldRow = 22;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;
	
	SwingTimer timer = new SwingTimer(this);
	public int currentScore;
	boolean recordStats = false;
	
	String time = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(cal.getTime());
	static Calendar cal = Calendar.getInstance();
	// map declaration
	HashMap <String,Integer> leaderboard = new HashMap <>();
	ArrayList <Stats> sortedScores = new ArrayList <>();
	
	Thread gameThread;
	
	public TileManager tM = new TileManager(this);
	public keyHandler key = new keyHandler(this);
	public Player player1 = new Player(this, key);
	public ArrayList<Enemy> enemyList = new ArrayList<>();
	
	
	// State of Game
	public int mapNum = 1;
	public int gameState = 0;
	public final int titleState = 0;
	public final int playState = 1;
	public final int instructionState = 2;
	public final int leaderboardState = 3;
	public final int settingsState = 4;
	public final int creditsState = 5;
	public final int endState = 6;
	public final int timerEndState = 7;
	
	public boolean allowMove = false;

	//System
	public UI ui = new UI(this);
	
	public game() {
		
		tM.loadMap();		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight ));
		this.setDoubleBuffered(true);
		this.addKeyListener(key);
		this.setFocusable(true);
		
		gameThread = new Thread(this);
		gameThread.start();
		
		// get leaderboards
		leaderboard.clear();
		readLeaderboards();
		Collection<Integer> score = leaderboard.values();
		Iterator<String> date = leaderboard.keySet().iterator();
		for(Integer i: score) {
			sortedScores.add(new Stats(date.next(), i));
			
		}		
		Collections.sort(sortedScores);

	}

	@Override
	public void run() {
		
		while(gameThread!=null) {
			
			if(allowMove) {
				move();
			}
			if(changeWorld) {
				if(changeWorld) {
					//clears arrays and adjusts for new map
					tM.getTiles().clear();
					enemyList.clear();
					tM.loadMap();
					changeWorld = false;
				}
			}
			checkCollision();
			keepInBound();
			
			this.repaint();
			
			try {
				Thread.sleep(1000/60);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void readLeaderboards() {
        int score = 0;
        String date = "";
        // Import leaderboard stats
        try {
            BufferedReader in = new BufferedReader(new FileReader("leaderboard.txt"));
            String line = "";
            while ((line = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "/", false);
           
                while (st.hasMoreTokens()) {
                    date = st.nextToken();
                    score = Integer.parseInt(st.nextToken());
                }
                leaderboard.put(date, score);
            }
            in.close();
        } 
        catch (FileNotFoundException e) {
        } 
        catch (IOException e) {
        }
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		//title
		if(this.gameState == this.titleState) {
			ui.draw(g2);
		}
		// load the game
		if(this.gameState == this.playState) {
			// set record stats to false to record at the end of game
			recordStats = false;
			// set background
			g2.setColor(Color.black);
			g2.fillRect(0,0,screenWidth,screenHeight);
			// draw components
			tM.draw(g2);
			player1.draw(g2);
			
			if(allowMove)
				// start timer
				timer.timer.start();
				
			for (int i = 0; i < enemyList.size();i++) {
				enemyList.get(i).draw(g2);;
			}
			ui.draw(g2);
			
			if(allowMove == false && timer.time != 0) {
				ui.drawPauseScreen();
			}
			else if(allowMove == false && timer.time == 0) {
				ui.drawTimerEndScreen();
			}
			
			if(player1.dead) {
				// emit death sound
				player1.playerDead();
				player1.dead = false;
				//this.gameState = this.endState;
			}
			
			//NEW
			//spawn enemies map1
			if(mapNum ==1) {
				for (int i = 0; i < 4;i++) {
					if(!enemyList.contains(new Enemy(this,null,i))) {
						enemyList.add(new Enemy(this, new Rectangle(14*tileSize,15*tileSize,tileSize,tileSize), i));
						enemyList.add(new Enemy(this, new Rectangle(47*tileSize,15*tileSize,tileSize,tileSize), i));
						enemyList.add(new Enemy(this, new Rectangle(114*tileSize,18*tileSize,tileSize,tileSize), i));
						enemyList.add(new Enemy(this, new Rectangle(140*tileSize,6*tileSize,tileSize,tileSize), i));
					}
				}
			}
				//spawn enemies map2
			if(mapNum == 2) {
				for (int i = 0; i < 5;i++) {
					if(!enemyList.contains(new Enemy(this,null,i))) {
						enemyList.add(new Enemy(this, new Rectangle(50*tileSize,15*tileSize,tileSize,tileSize), i));
						enemyList.add(new Enemy(this, new Rectangle(59*tileSize,20*tileSize,tileSize,tileSize), i));
						enemyList.add(new Enemy(this, new Rectangle(84*tileSize,15*tileSize,tileSize,tileSize), i));
						enemyList.add(new Enemy(this, new Rectangle(115*tileSize,15*tileSize,tileSize,tileSize), i));
						enemyList.add(new Enemy(this, new Rectangle(96*tileSize,5*tileSize,tileSize,tileSize), i));
					}
				}
			}
			//spawn enemies map3
			if(mapNum == 3) {
				for (int i = 0; i < 5;i++) {
					if(!enemyList.contains(new Enemy(this,null,i))) {
						enemyList.add(new Enemy(this, new Rectangle(8*tileSize,13*tileSize,tileSize,tileSize), i));
						enemyList.add(new Enemy(this, new Rectangle(36*tileSize,5*tileSize,tileSize,tileSize), i));
						enemyList.add(new Enemy(this, new Rectangle(45*tileSize,15*tileSize,tileSize,tileSize), i));
						enemyList.add(new Enemy(this, new Rectangle(73*tileSize,8*tileSize,tileSize,tileSize), i));
						enemyList.add(new Enemy(this, new Rectangle(92*tileSize,8*tileSize,tileSize,tileSize), i));
					}
				}
			}
	
			//NEW END HERE
			
			//NEW HERE
			for (int i = 0; i < enemyList.size();i++) {
				//loop through entire enemy array. If they are within player vicinty, it will be drawn.
				if(Math.abs(enemyList.get(i).player.x-player1.player.x)<screenWidth)
					enemyList.get(i).draw(g2);
			}
			ui.draw(g2);
			if(allowMove == false) {
				ui.drawPauseScreen();
			}
			if(player1.dead) {
				// emit death sound
				player1.playerDead();
				player1.dead = false;
				
			//NEW HERE
				enemyList.removeAll(enemyList);
				
			}
		}
			
		// load other screens
		if(this.gameState == this.endState || this.gameState == this.timerEndState || this.gameState == this.settingsState || this.gameState == this.instructionState || this.gameState == this.leaderboardState || this.gameState == this.creditsState){
			ui.draw(g2);
			// record player stats at the end of the game
			if(this.gameState == this.endState && recordStats == false) {
				recordStats = true;
				timer.timer.stop();
				int timeRemaining = timer.time;
	            String time = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(cal.getTime());
	            Stats pScore = new Stats(time,timeRemaining*10 + currentScore);
	            
	            try {
	            	System.out.println();
	                PrintWriter output = new PrintWriter(new FileWriter("leaderboard.txt", true));
	                // add to sorted scores to easily get at the end to display to user
	                sortedScores.add(new Stats(pScore.getDate(), pScore.getScore()));
	                // sort it right after to get new leaderboards
	                Collections.sort(sortedScores);
	                output.println(pScore.getDate() + "/" + pScore.getScore());
	                System.out.println(pScore.getDate() + "/" + pScore.getScore());
	                output.close();
	            }
	            catch (IOException e) {
	            }
	           
			}

		}
	}
	

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	void move() {
		player1.move();
		for(int i = 0; i < enemyList.size();i++) {//enemies will only move if the player is in a certain range
			if(Math.abs(enemyList.get(i).player.x-player1.player.x)<screenWidth/2)
			enemyList.get(i).move();
		}
	}
	
	void checkCollision() {
		 for(int i = 0; i < tM.getTiles().size();i++) {
			 //takes in a arrayList of tiles and will loop through them to check their rectangles with player rectangle
			 player1.checkCollision(tM.getTiles().get(i));
		 	 for(int j = 0; j < enemyList.size();j++){//also checks enemy collision
		 		 enemyList.get(j).checkCollision(tM.getTiles().get(i));
		 	 }
		 }
		 for(int j = 0; j < enemyList.size();j++) {
			 player1.collisionEnemy(enemyList.get(j));//player and enemy collision
			 
			 if(enemyList.get(j).dead)
				 enemyList.remove(j);
		 }
		 
	}

	
	void keepInBound() {
		player1.keepInBound();
		for(int i = 0; i < enemyList.size();i++)
			enemyList.get(i).keepInBound();
	}
	
	
	public static void main(String args[]) {	
		JFrame mainFrame = new JFrame("Game Frame");
		game gamePanel = new game();
		
		mainFrame.add(gamePanel);
		mainFrame.setVisible(true);
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println("running");
	} 
	
}

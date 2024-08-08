package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{

	final int originalTileSize = 16; //changed to 16
	public int scale = 3; 
	public int tileSize = originalTileSize * scale; //16 * 3 = 48
	
	//1024 x 768.. no longer bc 16x16 * scale = 48*48
	public final int maxScreenCol = 16; //change??
	public final int maxScreenRow = 12;//12
	public final int screenWidth = tileSize * maxScreenCol;
	public final int screenHeight = tileSize * maxScreenRow;
	
	//WORLD 21x21 maze map ( can change )
	public int maxWorldCol = 21; //21 21
	public int maxWorldRow = 21; //changed
	public int worldWidth = tileSize * maxWorldCol;
	public int worldHeight = tileSize * maxWorldRow;	
	
	//FPS
	int FPS = 60;
	
	// GAME STATE
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int popupState = 3; // signs ?
	public final int gameOverState = 4;
	public int gameState;// = playState;
	
	//INITALIZE CLASSES
	Thread gameThread;
	public KeyHandler keyH = new KeyHandler(this);
	public TileManager tileM = new TileManager(this);
	public AssetSetter aS = new AssetSetter(this);
	public CollisionDetection cD = new CollisionDetection(this);
	public UI ui = new UI(this);
	
	//*ENTITY, PLAYER, OBJECTS
	public Player player = new Player(this, keyH);
	public Entity npc[] = new Entity[3];
	public Entity enemies[] = new Entity[10];
	public Entity obj[] = new Entity[5]; //3 orbs sharing 1 index and 1 sign?
	ArrayList<Entity> entities = new ArrayList<>();
	
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
//		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void setUp() {
		aS.setObject();
		aS.setFirstMapNPC();
		
		this.gameState = this.titleState;
	}
	
	public void retryOption() {
		tileM.loadMap("res/maps/first_level.TXT");
		player.resetPlayer();
		aS.setObject();
		aS.setFirstMapNPC();
	}
	public void quitOption() {
		tileM.loadMap("res/maps/first_level.TXT");
		player.resetPlayer();
		aS.setObject();
		aS.setFirstMapNPC();
	}
	
	//game loop
	@Override
	public void run() {
		
		double timePerFrame = 1_000_000_000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		long timer = 0;
		long frameCount = 0;
		
		while(gameThread != null) {
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / timePerFrame;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				delta--;
				frameCount++;
			}
			if(timer >= 1_000_000_000) {
				System.out.println("FPS: "+frameCount);
				frameCount = 0;
				timer = 0;
			}
		}
		
	}
	
	public void update() {
//		player.update();
		if(gameState == playState) {
			//PLAYER
			player.update();
			
			//NPC
			for(int i = 0; i < npc.length; i++) {
				if(npc[i]!= null) {
					npc[i].update();
				}
			}
			
			//SKELETONS
			for(int i = 0; i < enemies.length; i++) {
				if(enemies[i] != null) {
					if(enemies[i].isAlive && !enemies[i].isDying) {
						enemies[i].update();						
					}
					else if(!enemies[i].isAlive) {
						enemies[i] = null;
					}	
				}
			}
		//SECOND STATE	
		}
		else if(gameState == pauseState) { //pause
			ui.pauseScreen();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		//title screen check
		if(this.gameState == this.titleState) {
			ui.draw(g2);
		}
		else {
			//TILE
			tileM.draw(g2);
			
			//add entities to array list
			entities.add(player);
			for(int i = 0; i < npc.length; i++) {
				if(npc[i] != null) {
					entities.add(npc[i]);
				}
			}
			for(int i = 0; i < obj.length; i++) {
				if(obj[i] != null) {
					entities.add(obj[i]);
				}
			}
			for(int i = 0; i < enemies.length; i++) {
				if(enemies[i] != null) {
					entities.add(enemies[i]);
				}
			}
			
			//sort entities list by worldY to render in correct order
			Collections.sort(entities, new Comparator<Entity>() {
				@Override
				public int compare(Entity e1, Entity e2) {
					return Integer.compare(e1.worldY, e2.worldY);	
				}			
			});
			//DRAW ENTITIES
			for(int i = 0; i < entities.size(); i++) {
				if(entities.get(i) != null) {
					entities.get(i).draw(g2);
				}
			}
			//CLEAR LIST AFTER DRAWING
			entities.clear();
			
			//UI
			ui.draw(g2);
		}
		g2.dispose();
	}
	
	
}

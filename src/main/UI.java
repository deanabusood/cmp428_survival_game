package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import entity.Entity;
import object.Heart;

public class UI {

	GamePanel gp;
	Graphics2D g2;
	
//	BufferedImage orb;
	public String message = "";
	public boolean messageOn = false;
	public int messageTimer = 0;
	public boolean gameFinished = false;
	
	//dialogue
	public String dialogue = "";
	
	//fonts test
	Font smallFont;
	Font largeFont;
	
	//title screen 
	public int optionNum = 0;
	
	//health status
	BufferedImage full_heart, half_heart, empty_heart;
	
	public UI(GamePanel gp) {
		this.gp = gp;
		
		smallFont = new Font("Arial", Font.PLAIN, 40);
		largeFont = new Font("Arial", Font.BOLD, 80);
		
//		BlueOrb blueOrb = new BlueOrb(gp);
//		orb = blueOrb.image;
		
		//HUD
		Entity heart = new Heart(gp);
		full_heart = heart.image;
		half_heart = heart.image2;
		empty_heart = heart.image3;
		
		
	}
	
	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}
		
	public void draw(Graphics2D g2) {
		this.g2 = g2;
		
		if(gameFinished == true) {
			g2.setFont(smallFont);
			g2.setColor(Color.white);
			
			g2.setColor(new Color(0,0,0, 150));
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
			
			g2.setColor(Color.white);
			String text = "YOU HAVE BEAT THE MAZE!";
			int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			int x = gp.screenWidth / 2 - textLength/2;
			int y = gp.screenHeight / 2 - (gp.tileSize*3);		
			g2.drawString(text, x, y);	

			gp.gameThread = null;
		}
		else {
			g2.setFont(smallFont);
			g2.setColor(Color.white);
			//GAME STATES
			if(gp.gameState == gp.titleState) {
				drawTitleScreen();
			}
			else if(gp.gameState == gp.playState) {
				drawHUD();
			}
			else if(gp.gameState == gp.pauseState) {
				drawHUD();
				pauseScreen();
			}
			else if(gp.gameState == gp.popupState) {
				drawHUD();
				popupMessage();
			}
			else if(gp.gameState == gp.gameOverState) {
				drawGameOverScreen();
			}
			
//			if(gp.player != null & gp.player.orbNum == 1) {
//				drawHUD();
//			}		
		}
		
	}
		
	public void drawGameOverScreen() {
		g2.setColor(new Color(0,0,0, 150));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		//shadow
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));
		g2.setColor(Color.black);
		String text = "GAME OVER";
		int x = getCenteredX(text);
		int y = gp.tileSize*4;
		g2.drawString(text, x, y);
		
		//main text
		g2.setColor(Color.white);
		g2.drawString(text, x-4, y-4);
		
		//retry option
		g2.setFont(g2.getFont().deriveFont(50f));
		text = "Retry";
		x = getCenteredX(text);
		y += gp.tileSize*4;
		g2.drawString(text, x, y);
		if(optionNum == 0) {
			g2.drawString(">", x-40, y);
		}
		
		//quit
		text = "Quit";
		x= getCenteredX(text);
		y += 55;
		g2.drawString(text, x, y);
		if(optionNum == 1) {
			g2.drawString(">", x-40, y);
		}
	}

	public void drawPlayerHealth() {
//		gp.player.health = 3;		
		int x = gp.tileSize / 2;
		int y = gp.tileSize / 2;	
		int i = 0;
		//2 lives = 1 heart, divide by 2 (DRAWS MAX LIFE)
		while(i < gp.player.maxHealth / 2) {
			g2.drawImage(empty_heart, x, y, null);
			i++;
			x+= gp.tileSize;
		}
		
		//reset values
		x = gp.tileSize / 2;
		y = gp.tileSize / 2;	
		i = 0;
		//DRAW CURR LIFE
		while(i < gp.player.health) {
			g2.drawImage(half_heart, x, y, null);
			i++;
			
			if(i < gp.player.health) {
				g2.drawImage(full_heart, x, y, null);
			}
			i++;
			x+= gp.tileSize;
		}
	}
	
	public void drawHUD() {
		drawPlayerHealth();
	}

	public void drawTitleScreen() {
		//background color
		g2.setColor(new Color(82, 92, 78));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		//text
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 75F));
		String text = "The Maze";
		int x = getCenteredX(text);
		int y = gp.tileSize * 3;
		
		//shadow text
		g2.setColor(Color.black);
		g2.drawString(text, x+5, y+5);
		
		//main text color
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		
		//OPTIONS
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
		
		text = "START";
		x = getCenteredX(text);
		y += gp.tileSize * 4;
		g2.drawString(text, x, y);
		if(optionNum == 0) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
		text = "QUIT";
		x = getCenteredX(text);
		y += gp.tileSize * 4;
		g2.drawString(text, x, y);
		if(optionNum == 1) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
	}

	public void popupMessage() {
		//window size and location
		int x = gp.tileSize * 2; //2x to the right from edge
		int y = gp.tileSize / 2;
		int width = gp.screenWidth - (gp.tileSize *4);
		int height = gp.tileSize * 4;
		
		drawWindow(x, y, width, height);
		
		//TEXT LOCATION/SIZE
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
		x += gp.tileSize;
		y+= gp.tileSize;
		
		//SPLIT TEXT IN DIALOGUE TO DISPLAY ON NEW LINE
		for(String line: dialogue.split("\n")) {
			
			g2.drawString(line, x, y);
			y += 40; //next line displayed after
			
			//UPDATES AND DISPLAYS SKELETONS KILLED IN DIALOGUE
			if(line.startsWith("Defeat 7 skeletons and you will be granted your freedom.")){
				y+= gp.tileSize;
				g2.drawString("\n\nDefeated: "+gp.player.skeletonsKilled, x, y);
			}
			
			
		}
	}
	
	public void drawWindow(int x, int y, int width, int height) {
		g2.setColor(new Color(0, 0, 0, 220)); //220 opacity
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		g2.setColor(new Color(255, 255, 255));
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
	}

	public void pauseScreen() {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
		String text = "PAUSED";
		
		int x = getCenteredX(text);
		int y = gp.screenHeight/2;
		
		
		g2.drawString(text, x, y);
	}
	
	public int getCenteredX(String text) {
		//TEXT AT CENTER OF SCREEN
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth/2 - length/2;
		return x;
	}
	
	
}

package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.Utility;

public class Entity {
	
	GamePanel gp;
	//Object images
	public BufferedImage image, image2, image3;
	public String name;
	public boolean collision = false;
	
	//Entity world values
	public int worldX, worldY;
	public int speed;
	
	//SPRITE
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
	public String direction = "up";
	//sprite movement
	public int spriteCounter = 0;
	public int spriteNum = 1;
	
	//collision
	public int type; // 0 is player, 1 is npc, 2 is enemies
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false;
	
	//NPC
	public int actionCounter = 0;
	
	//dialogue
	String dialogues[] = new String[20];	
	public int dialogueNum = 0;
	
	//life status
	public boolean isAlive = true;
	public boolean isDying = false;
	boolean showHealthBar = false;
	int healthBarCounter = 0;
	int dyingCounter = 0;
	
	public int maxHealth;
	public int health;
	
	//player health
	public boolean isInvincible = false;
	public int invincibleCounter = 0;
	
	//player attack
	public boolean isPlayerAttacking = false;
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	
	
	public Entity(GamePanel gp) {
		this.gp = gp;
	}
	
	//overwritten in subclasses
	public void setAction() {	
	}
	
	public void entityDamageReaction() {
		
	}
	
	public void speak() {
		if(gp.player.skeletonsKilled != 7 && gp.npc[1] == null && gp.npc[2] != null) { //prevent dialogueNum from increasing
			updateDirection();
			return;
		}
		
		gp.ui.dialogue = dialogues[dialogueNum];
		dialogueNum++;
		
		if(dialogueNum > 4) {
			gp.ui.gameFinished = true;
		}
		
		//NPC looks at player when in dialogue
		updateDirection();
	}
	
	public void updateDirection() {
		switch(gp.player.direction) {
		case "up":
			this.direction = "down";
			break;
		case "down":
			this.direction = "up";
			break;
		case "left":
			this.direction = "right";
			break;
		case "right":
			this.direction = "left";
			break;
		}
	}
	
	public void update() {
		setAction(); //subclass has method (NPC)
		
		collisionOn = false;
		
		gp.cD.checkTileCollision(this);
		gp.cD.checkObjectCollision(this, false);
		gp.cD.checkEntityCollision(this, gp.npc);
		gp.cD.checkEntityCollision(this, gp.enemies);
		
//		gp.cD.checkPlayerCollision(this);
		boolean isPlayerContact = gp.cD.checkPlayerCollision(this);
		if(isPlayerContact && this.type == 2) {
			if(gp.player.isInvincible == false) {	//prevents player from being damaged twice
				gp.player.health -= 1;
				gp.player.isInvincible = true;
			}
		}
		
		
		if(collisionOn == false) {	
			switch(direction) {
			case "up":    worldY -= speed;
				break;
			case "down":  worldY += speed;
				break;
			case "left":  worldX -= speed;
				break;
			case "right": worldX += speed;
				break;
			}	
		}
		
		spriteCounter++;
		if(spriteCounter > 20) {
			if(spriteNum == 1) {
				spriteNum = 2;
			}
			else if(spriteNum == 2) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}	
		
		//ENEMIES
		if(this.isInvincible) {
			this.invincibleCounter++;
			
			if(invincibleCounter > 40) { //1 second
				this.isInvincible = false;
				this.invincibleCounter = 0;
			}
		}
		
	}
	
	
	public BufferedImage setupImage(String imagePath, int width, int height) {
		Utility utility = new Utility();
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath+".png"));
			image = utility.scaleImage(image, width, height);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return image;	
	}

	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		int screenX = worldX - gp.player.worldX + gp.player.screenX; //determines where on screen to draw
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		//improves render to only load tiles visible to player/in near distance (+/- gp.tileSize removes black border)
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
		&& worldY +gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
			
			switch(direction) {
			case "up": 
				if(spriteNum == 1) {
					image = up1;
				}
				if(spriteNum == 2) {
					image = up2;
				}
			break;
			
			case "down": 
				if(spriteNum == 1) {
					image = down1;
				}
				if(spriteNum == 2) {
					image = down2;
				}
			break;
			
			case "left": 
				if(spriteNum == 1) {
					image = left1;
				}
				if(spriteNum == 2) {
					image = left2;
				}
			break;
			
			case "right": 
				if(spriteNum == 1) {
					image = right1;
				}
				if(spriteNum == 2) {
					image = right2;
				}
			break;
			}	
			
			//enemy HP bar
			if(this.type == 2 && showHealthBar == true) {
				
				double oneBar = (double)gp.tileSize/maxHealth; 
				double hpBarValue = oneBar * health;
				
				//border color
				g2.setColor(new Color(35,35,35));
				g2.fillRect(screenX-1, screenY-16, gp.tileSize+2, 12);
				//health color
				g2.setColor(new Color(255, 0 , 30));
				g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
				
				healthBarCounter++;
				
				if(healthBarCounter > 600) { //10 seconds, bar disappears
					healthBarCounter = 0;
					showHealthBar = false;
				}
			}
			
			//flicker entity when damaged
			if(this.isInvincible) {
				this.showHealthBar = true;
				healthBarCounter = 0;
				changeAlphaValue(g2, 0.4f);
			}
			if(this.isDying) {
				dyingAnimation(g2);
			}
			
			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null); //, gp.tileSize, gp.tileSize, not needed
			//reset flicker
			changeAlphaValue(g2, 1f);
		}
	}

	

	public void dyingAnimation(Graphics2D g2) {
		dyingCounter++;
		
		int interval = 5;
		
		//EVERY 5 frames
		if(dyingCounter <= interval) {
			changeAlphaValue(g2, 0f);
		}
		if(dyingCounter > interval && dyingCounter <= interval*2) {
			changeAlphaValue(g2, 1f);
		}
		if(dyingCounter > interval*2 && dyingCounter <= interval*3) {
			changeAlphaValue(g2, 0f);
		}
		if(dyingCounter > interval*3 && dyingCounter <= interval*4) {
			changeAlphaValue(g2, 1f);
		}
		if(dyingCounter > interval*4 && dyingCounter <= interval*5) {
			changeAlphaValue(g2, 0f);
		}
		if(dyingCounter > interval*5 && dyingCounter <= interval*6) {
			changeAlphaValue(g2, 1f);
		}
		if(dyingCounter > interval*6 && dyingCounter <= interval*7) {
			changeAlphaValue(g2, 0f);
		}
		if(dyingCounter > interval*7 && dyingCounter <= interval*8) {
			changeAlphaValue(g2, 1f);
		}
		
		if(dyingCounter > interval*8) {
			isDying = false;
			isAlive = false;
		}
	}
	
	public void changeAlphaValue(Graphics2D g2, float value) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, value));
	}
	
	
}

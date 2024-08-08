package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{

	KeyHandler keyH;
	
	public final int screenX, screenY;

	public int orbNum = 0;
	public int skeletonsKilled = 0;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		super(gp);
		
		this.keyH = keyH;
		
		this.screenX = gp.screenWidth / 2 - (gp.tileSize / 2); //center of screen instead of top left corner of character
		this.screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
		
		//OVERRIDE DEFAULT RECT IN ENTITY
		solidArea = new Rectangle(8, 16, 32, 32); // a little smaller than character
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		attackArea.width = 36;
		attackArea.height = 36;
		
		setDefaultValues();
		getPlayerWalkImage();
		getPlayerAttackImage();
	}
	
	
	//where player spawns, speed, direction
	public void setDefaultValues() {
		this.worldX = 480;//480 //gp.tileSize * 21;
		this.worldY = 908; //gp.tileSize * 21;
		this.speed = 3;
		this.direction = "up";
		
		//player health
		this.maxHealth = 6; //3 hearts
		this.health = maxHealth;
	}
	
	public void resetPlayer() {
		this.worldX = 480;
		this.worldY = 908;
		this.speed = 3;
		this.direction = "up";
		
		this.maxHealth = 6; //3 hearts
		this.health = maxHealth;
		this.isInvincible = false;
	}
	
	//get player movement direction images
	public void getPlayerWalkImage() {
		
//		try {
//			up1 = ImageIO.read(getClass().getResourceAsStream("/player/character_up_1.png"));
//			up2 = ImageIO.read(getClass().getResourceAsStream("/player/character_up_2.png"));
//			
//			down1 = ImageIO.read(getClass().getResourceAsStream("/player/character_down_1.png"));
//			down2 = ImageIO.read(getClass().getResourceAsStream("/player/character_down_2.png"));
//			
//			left1 = ImageIO.read(getClass().getResourceAsStream("/player/character_left_1.png"));
//			left2 = ImageIO.read(getClass().getResourceAsStream("/player/character_left_2.png"));
//			
//			right1 = ImageIO.read(getClass().getResourceAsStream("/player/character_right_1.png"));
//			right2 = ImageIO.read(getClass().getResourceAsStream("/player/character_right_2.png"));	
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		up1 = setupImage("/player/character_up_1", gp.tileSize, gp.tileSize); //16x17
		up2 = setupImage("/player/character_up_2", gp.tileSize, gp.tileSize);
		
		down1 = setupImage("/player/character_down_1", gp.tileSize, gp.tileSize);
		down2 = setupImage("/player/character_down_2", gp.tileSize, gp.tileSize);
		
		left1 = setupImage("/player/character_left_1", gp.tileSize, gp.tileSize);
		left2 = setupImage("/player/character_left_2", gp.tileSize, gp.tileSize);
		
		right1 = setupImage("/player/character_right_1", gp.tileSize, gp.tileSize);
		right2 = setupImage("/player/character_right_2", gp.tileSize, gp.tileSize);
	}
	
	public void getPlayerAttackImage() {	
		//ORIGINAL IS 23x23 but tiles are 16x16 so forced to 48x48
		attackUp1 = setupImage("/player/character_up_atk_1", gp.tileSize+17, gp.tileSize); //23x18
		attackUp2 = setupImage("/player/character_up_atk_2", gp.tileSize+17, gp.tileSize);
		
		attackDown1 = setupImage("/player/character_down_atk_1", gp.tileSize + 20, gp.tileSize + 15); //23x23
		attackDown2 = setupImage("/player/character_down_atk_2", gp.tileSize + 20, gp.tileSize + 15);
		
		attackLeft1 = setupImage("/player/character_left_atk_1", gp.tileSize+5, gp.tileSize+8);
		attackLeft2 = setupImage("/player/character_left_atk_2", gp.tileSize+5, gp.tileSize+8);
		
		attackRight1 = setupImage("/player/character_right_atk_1", gp.tileSize+5, gp.tileSize+8);
		attackRight2 = setupImage("/player/character_right_atk_2", gp.tileSize+5, gp.tileSize+8);
	}
	
	//handles checking key handler and setting direction
	public void update() {
		if(isPlayerAttacking) {
			
			playerAttacking();
			
		}
		else if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {
			//SET DIRECTION
			if(keyH.upPressed) {
				direction = "up";
			}
			if(keyH.downPressed) {
				direction = "down";
			}
			if(keyH.leftPressed) {
				direction = "left";
			}
			if(keyH.rightPressed) {
				direction = "right";
			}
			
			//change later ////////////////////////////////
//			System.out.println("X: "+worldX+" Y: "+worldY);
			
			//tile collision
			collisionOn = false;
			gp.cD.checkTileCollision(this);
			
			//object collision
			int objIndex = gp.cD.checkObjectCollision(this, true);//true for player
			pickUpObject(objIndex);
			
			//npc collision
			int npcIndex = gp.cD.checkEntityCollision(this, gp.npc);
			interactNPC(npcIndex);
			
			//enemy collision
			int enemyIndex = gp.cD.checkEntityCollision(this, gp.enemies);
			playerHit(enemyIndex);
						
			//CHECKER
			if(collisionOn == false && keyH.enterPressed == false) {	
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
			gp.keyH.enterPressed = false; //reset after checking condition
			
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
		}	
		
		//check invincible status
		if(this.isInvincible) {
			this.invincibleCounter++;
			
			if(invincibleCounter > 60) { //1 second
				this.isInvincible = false;
				this.invincibleCounter = 0;
			}
		}
		
		if(this.health <= 0) {
			gp.gameState = gp.gameOverState;
		}
		
	}
	
	public void playerAttacking() {
		spriteCounter++;
		
		//time for animation
		if(spriteCounter <= 5) {
			spriteNum = 1;
		}
		if(spriteCounter > 5 && spriteCounter <= 25) {
			spriteNum = 2;
			
			//check collision with player weapon
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			switch(direction) {
			case "up": worldY -= attackArea.height;
				break;
			case "down": worldY += attackArea.height;
				break;
			case "left": worldX -= attackArea.width;
				break;
			case "right": worldX += attackArea.width;
				break;
			}
			//attack area becomes solid area
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			
			//check enemy collision with updated world values
			int enemyIndex = gp.cD.checkEntityCollision(this, gp.enemies);
			damageEnemy(enemyIndex);
			
			//after collision check
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;
			
		}
		if(spriteCounter > 25) {
			spriteNum = 1;
			spriteCounter = 0;
			isPlayerAttacking = false;
		}
		
	}

	public void damageEnemy(int enemyIndex) {
		if(enemyIndex != -1) {
			if(gp.enemies[enemyIndex].isInvincible == false) {
				gp.enemies[enemyIndex].health -= 1;
				gp.enemies[enemyIndex].isInvincible = true;
//				gp.enemies[enemyIndex].entityDamageReaction();
				
				if(gp.enemies[enemyIndex].health <= 0) {
					gp.enemies[enemyIndex].isDying = true;
					this.skeletonsKilled++;
				}
				
			}
			
		}
	}
	

	public void playerHit(int enemyIndex) {
		if(enemyIndex != -1) {
			if(!this.isInvincible) { //not invincible
				health -= 1;
				this.isInvincible = true;
			}
		}
	}


	public void pickUpObject(int index) {
		if(index != -1) {
			String objectName = gp.obj[index].name;
			
			switch(objectName) {
			case "Green":
			case "Red":
			case "Blue":
				this.orbNum++;
				gp.obj[index] = null;
//				gp.ui.showMessage("NICE");
//				gp.ui.gameFinished = true; //testing
//				break;
//			case "Sign":
//				if(this.orbNum == 0) { //COLLECTED 0 ORBS					
//				}
				if(this.orbNum == 1) { //COLLECTED 1 ORB
					loadSecondMap();		
				}
//				if(this.orbNum == 2) { //COLLECTED 2 ORBS
//					gp.ui.gameFinished = true;
//				}
//				if(this.orbNum == 3) { //COLLECTED ALL ORBS //ending
//					gp.ui.gameFinished = true;
//				}
				break;
			}
		}
	}
	
	private void loadSecondMap() {
		gp.tileM.loadMap("res/maps/map1_5.txt");
		gp.aS.setSecondMapNPC(); //makes wizard locate to new area
	}


	public void checkDialogueCount(int index) {
		if(gp.npc == null)
			return;
		
		int dialogueCount = 0; //temp value, later initialized
		if(gp.npc[index] != null) {
			dialogueCount = gp.npc[index].dialogueNum;
		}
		//first npc
		if(dialogueCount == 1) {
			gp.npc[index] = null;
			return;
		}
		//second spawn
		if(dialogueCount == 3 && gp.npc[2] == null) {
			gp.npc[index] = null;
			loadThirdMap();
			return;
		}
		
	}
	
	public void loadThirdMap() {
		gp.tileM.loadMap("res/maps/map2.txt"); //THIRD MAP
		gp.aS.setThirdMapSkeletons(); //set 7 skeletons
		gp.aS.setThirdMapNPC();
	}
	

	public void interactNPC(int index) {
		if(gp.keyH.enterPressed) {
			
			if(index != -1) {
				gp.gameState = gp.popupState;
				gp.npc[index].speak();
				checkDialogueCount(index);	
			}//			gp.keyH.enterPressed = false;
			else {				
				isPlayerAttacking = true;
			}	
		}
		
	}
	
	public void draw(Graphics2D g2) {
//		g2.setColor(Color.white);
//		g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		BufferedImage image = null;
		int tempScreenX = screenX;
		int tempScreenY = screenY;
		
		switch(direction) {
		case "up": 
			if(!isPlayerAttacking) {
				if(spriteNum == 1) {image = up1;}
				if(spriteNum == 2) {image = up2;}
			}
			else if(isPlayerAttacking) {
//				tempScreenY = screenX +2;
				if(spriteNum == 1) {image = attackUp1;}
				if(spriteNum == 2) {image = attackUp2;}
			}
		break;
		
		case "down": 
			if(!isPlayerAttacking) {
				if(spriteNum == 1) {image = down1;}
				if(spriteNum == 2) {image = down2;}
			}
			else if(isPlayerAttacking) {
				if(spriteNum == 1) {image = attackDown1;}
				if(spriteNum == 2) {image = attackDown2;}
			}	
		break;
		
		case "left": 
			if(!isPlayerAttacking) {
				if(spriteNum == 1) {image = left1;}
				if(spriteNum == 2) {image = left2;}
			}
			else if(isPlayerAttacking) {
				if(spriteNum == 1) {image = attackLeft1;}
				if(spriteNum == 2) {image = attackLeft2;}
			}
		break;
		
		case "right": 
			if(!isPlayerAttacking) {
				if(spriteNum == 1) {image = right1;}
				if(spriteNum == 2) {image = right2;}
			}
			else if(isPlayerAttacking) {
				if(spriteNum == 1) {image = attackRight1;}
				if(spriteNum == 2) {image = attackRight2;}
			}
		break;
		}	
		
		//flicker player when damaged
		if(this.isInvincible) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
		}
		
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		
		//reset flicker
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
	
}

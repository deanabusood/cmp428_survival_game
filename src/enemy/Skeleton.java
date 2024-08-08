package enemy;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class Skeleton extends Entity{
	
	GamePanel gp;
	
	public Skeleton(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "Skeleton";
		type = 2;
		speed = 1;
		maxHealth = 4;
		health = maxHealth;
		
//		solidArea = new Rectangle(8, 16, 32, 32);
//		solidAreaDefaultX = solidArea.x;
//		solidAreaDefaultY = solidArea.y;
		
		solidArea.x = 3;
		solidArea.y =18;
		solidArea.width = 43;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
	}
	
	public void getImage() {
		
		up1 = setupImage("/enemy/skeleton_up_1", gp.tileSize, gp.tileSize); //16x17
		up2 = setupImage("/enemy/skeleton_up_2", gp.tileSize, gp.tileSize);
		
		down1 = setupImage("/enemy/skeleton_down_1", gp.tileSize, gp.tileSize);
		down2 = setupImage("/enemy/skeleton_down_2", gp.tileSize, gp.tileSize);
		
		left1 = setupImage("/enemy/skeleton_left_1", gp.tileSize, gp.tileSize);
		left2 = setupImage("/enemy/skeleton_left_2", gp.tileSize, gp.tileSize);
		 
		right1 = setupImage("/enemy/skeleton_right_1", gp.tileSize, gp.tileSize);
		right2 = setupImage("/enemy/skeleton_right_2", gp.tileSize, gp.tileSize);	
	}

	@Override
	public void setAction() {
		actionCounter++;
		//wont change for 2 seconds (120 frames)
		if(actionCounter == 120) {
			Random random = new Random();
			int i = random.nextInt(100)+1; //1-100
			
			if(i <= 25) {
				direction = "up";
			}
			if(i > 25 && i <= 50) {
				direction = "down";
			}
			if(i > 50 && i <= 75) {
				direction = "left";
			}
			if(i > 75) {
				direction = "right";
			}
			actionCounter = 0;
		}
	}
	
	@Override
	public void entityDamageReaction() {
		actionCounter = 0;
		
//		switch(gp.player.direction) {
//		case "up": direction = "down";
//			break;
//		case "down": direction = "up";
//			break;
//		case "left": direction = "right";
//			break;
//		case "right": direction = "left";
//			break;
//		}
		//skeletons move away from player when hit
		direction = gp.player.direction;	
	}
	
	
}

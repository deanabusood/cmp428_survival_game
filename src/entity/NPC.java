package entity;

import main.GamePanel;

public class NPC extends Entity{
	
	public NPC(GamePanel gp) {
		super(gp);
		
		direction = "down";
		speed = 0;
		
		getNPCImage();
		setDialogue();
	}
	
	public void setDialogue() {
		dialogues[0] = "Hello traveler, you have entered my maze. \nCollect the orb to make it out alive safely!";
		
		dialogues[1] = "Did you really think it'd be that easy?";	
		dialogues[2] = "My favorite number is 7. \nDefeat 7 skeletons and you will be granted your freedom.";
		
		dialogues[3] = "You have proven yourself worthy. \nSafe adventures, traveler.";
	}
	
	public void speak() {
		super.speak();
	}
	
	
	//get npc movement direction images
	public void getNPCImage() {
		up1 = setupImage("/npc/oldman_up_1", gp.tileSize, gp.tileSize);
		up2 = setupImage("/npc/oldman_up_2", gp.tileSize, gp.tileSize);
		
		down1 = setupImage("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
		down2 = setupImage("/npc/oldman_down_2", gp.tileSize, gp.tileSize);
		
		left1 = setupImage("/npc/oldman_left_1", gp.tileSize, gp.tileSize);
		left2 = setupImage("/npc/oldman_left_2", gp.tileSize, gp.tileSize);
		
		right1 = setupImage("/npc/oldman_right_1", gp.tileSize, gp.tileSize);
		right2 = setupImage("/npc/oldman_right_2", gp.tileSize, gp.tileSize);
	}
	
	//OVERRIDES
//	@Override
//	public void setAction() {
//		actionCounter++;
//		//wont change for 2 seconds (120 frames)
//		if(actionCounter == 120) {
//			Random random = new Random();
//			int i = random.nextInt(100)+1; //1-100
//			
//			if(i <= 25) {
//				direction = "up";
//			}
//			if(i > 25 && i <= 50) {
//				direction = "down";
//			}
//			if(i > 50 && i <= 75) {
//				direction = "left";
//			}
//			if(i > 75) {
//				direction = "right";
//			}
//			actionCounter = 0;
//		}
//		
//	}
		
}

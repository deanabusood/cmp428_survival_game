package main;

import enemy.Skeleton;
import entity.NPC;
import object.GreenOrb;


public class AssetSetter {

	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	//where you place objects
	public void setObject() {
		gp.obj[0] = new GreenOrb(gp);
		gp.obj[0].worldX = 744;
		gp.obj[0].worldY = 815;
	}
	public void setSecondLevelObject() {
		gp.obj[0] = new GreenOrb(gp);
		gp.obj[0].worldX = 744;
		gp.obj[0].worldY = 815;
	}
	
	public void setFirstMapNPC() {
		gp.npc[0] = new NPC(gp);
		gp.npc[0].worldX = 480;//480;
		gp.npc[0].worldY = 875;//857;
		gp.npc[0].speed = 1;
	}

	public void setSecondMapNPC() {
		gp.npc[1] = new NPC(gp);
		gp.npc[1].worldX = 750;//768;
		gp.npc[1].worldY = 815;
		gp.npc[1].dialogueNum = 1;
	}
	
	public void setThirdMapNPC() {
		gp.npc[2] = new NPC(gp);
		gp.npc[2].worldX = 138;
		gp.npc[2].worldY = 161;
		gp.npc[2].dialogueNum = 3;
		gp.npc[2].direction = "right";
	}
	
	public void setThirdMapSkeletons() {		
		gp.enemies[0] = new Skeleton(gp);
		gp.enemies[0].worldX = 480;
		gp.enemies[0].worldY = 569;
		
		gp.enemies[1] = new Skeleton(gp);
		gp.enemies[1].worldX = 285;
		gp.enemies[1].worldY = 719;
		
		gp.enemies[2] = new Skeleton(gp);
		gp.enemies[2].worldX = 816;
		gp.enemies[2].worldY = 197;
		
		gp.enemies[3] = new Skeleton(gp);
		gp.enemies[3].worldX = 282;
		gp.enemies[3].worldY = 830;
		
		gp.enemies[4] = new Skeleton(gp);
		gp.enemies[4].worldX = 432;
		gp.enemies[4].worldY = 329;
		
		gp.enemies[5] = new Skeleton(gp);
		gp.enemies[5].worldX = 198;
		gp.enemies[5].worldY = 671;
		
		gp.enemies[6] = new Skeleton(gp);
		gp.enemies[6].worldX = 429;
		gp.enemies[6].worldY = 143;
	}
	
}

package main;

import entity.Entity;

public class CollisionDetection {
	
	GamePanel gp;
	
	public CollisionDetection(GamePanel gp) {
		this.gp = gp;
	}
	
	public void checkTileCollision(Entity e) { //entity to check for player and npc collision
		//check left x, right x, top y, bottom y
		int entityLeftWorldX = e.worldX + e.solidArea.x;
		int entityRightWorldX = e.worldX + e.solidArea.x + e.solidArea.width;
		
		int entityTopWorldY = e.worldY + e.solidArea.y;
		int entityBottomWorldY = e.worldY + e.solidArea.y + e.solidArea.height;
		
		//find col and row from these coordinates
		int entityLeftCol = entityLeftWorldX/gp.tileSize;
		int entityRightCol = entityRightWorldX/gp.tileSize;
		int entityTopRow = entityTopWorldY/gp.tileSize;
		int entityBottomRow = entityBottomWorldY/gp.tileSize;
		
		//only need to check 2 tiles for each direction
		int tileNum1, tileNum2;
		
		switch(e.direction) {
		case "up":
			entityTopRow = (entityTopWorldY - e.speed) / gp.tileSize; //predicts where player will be to see where they will step in
			tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
			if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
				e.collisionOn = true;
			}
			break;
		case "down":
			entityBottomRow = (entityBottomWorldY + e.speed) / gp.tileSize; //predicts where player will be to see where they will step in
			tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
				e.collisionOn = true;
			}
			break;
		case "left":
			entityLeftCol = (entityLeftWorldX - e.speed) / gp.tileSize; //predicts where player will be to see where they will step in
			tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
				e.collisionOn = true;
			}
			break;
		case "right":
			entityRightCol = (entityRightWorldX + e.speed) / gp.tileSize; //predicts where player will be to see where they will step in
			tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
				e.collisionOn = true;
			}
			break;
		}	
	}
	
	public int checkObjectCollision(Entity e, boolean player) {
		int index = -1;
		
		for(int i = 0; i < gp.obj.length; i++) {
			if(gp.obj[i] != null) {
				//get entity's solid area pos
				e.solidArea.x = e.worldX + e.solidArea.x;
				e.solidArea.y = e.worldY + e.solidArea.y;
				//get obj's solid ara pos
				gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
				gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;
				
				switch(e.direction) {
				case "up":
					e.solidArea.y -= e.speed;				
					break;
				case "down":
					e.solidArea.y += e.speed;
					break;
				case "left":
					e.solidArea.x -= e.speed;
					break;
				case "right":
					e.solidArea.x += e.speed;
					break;
				}
				
				if(e.solidArea.intersects(gp.obj[i].solidArea)) {
					if(gp.obj[i].collision == true) {
						e.collisionOn = true;
					}
					if(player == true) {
						index = i;
					}
				}
				
				e.solidArea.x = e.solidAreaDefaultX;
				e.solidArea.y = e.solidAreaDefaultY;
				gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
				gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
			}
		}
		
		
		return index;
	}
	
	//returns index of collided npc
	//NPC OR MONSTER COLLISION
	public int checkEntityCollision(Entity e, Entity [] target) {
		int index = -1;
		
		for(int i = 0; i < target.length; i++) {
			if(target[i] != null) {
				//get entity's solid area pos
				e.solidArea.x = e.worldX + e.solidArea.x;
				e.solidArea.y = e.worldY + e.solidArea.y;
				//get obj's solid ara pos
				target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
				target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;
				
				switch(e.direction) {
				case "up":
					e.solidArea.y -= e.speed;
					break;
				case "down":
					e.solidArea.y += e.speed;
					break;
				case "left":
					e.solidArea.x -= e.speed;
					break;
				case "right":
					e.solidArea.x += e.speed;
					break;
				}
				
				if(e.solidArea.intersects(target[i].solidArea)) {
					if(target[i] != e) {
						e.collisionOn = true;
						index = i;
					}	
				}
				
				e.solidArea.x = e.solidAreaDefaultX;
				e.solidArea.y = e.solidAreaDefaultY;
				target[i].solidArea.x = target[i].solidAreaDefaultX;
				target[i].solidArea.y = target[i].solidAreaDefaultY;
			}
		}
			return index;
		
	}
	
	public boolean checkPlayerCollision(Entity e) {
		boolean isPlayerContact = false;
		
			//get entity's solid area pos
			e.solidArea.x = e.worldX + e.solidArea.x;
			e.solidArea.y = e.worldY + e.solidArea.y;
			//get obj's solid ara pos
			gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
			gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
			
			switch(e.direction) {
			case "up":
				e.solidArea.y -= e.speed;
				break;
			case "down":
				e.solidArea.y += e.speed;
				break;
			case "left":
				e.solidArea.x -= e.speed;
				break;
			case "right":
				e.solidArea.x += e.speed;
				break;
			}
			
			if(e.solidArea.intersects(gp.player.solidArea)) {
				e.collisionOn = true;
				isPlayerContact = true;
			}
			
			e.solidArea.x = e.solidAreaDefaultX;
			e.solidArea.y = e.solidAreaDefaultY;
			gp.player.solidArea.x = gp.player.solidAreaDefaultX;
			gp.player.solidArea.y = gp.player.solidAreaDefaultY;	
			
			return isPlayerContact;
	}
	
}

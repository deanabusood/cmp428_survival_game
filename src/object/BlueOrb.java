package object;

import entity.Entity;
import main.GamePanel;

public class BlueOrb extends Entity{
	
	public BlueOrb(GamePanel gp) {
		super(gp);
		
		name = "Blue";
		up1 = setupImage("/objects/blue_orb", gp.tileSize, gp.tileSize);
	}
	
	
}
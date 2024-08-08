package object;

import entity.Entity;
import main.GamePanel;

public class GreenOrb extends Entity{
	
	public GreenOrb(GamePanel gp) {
		super(gp);
		
		name = "Green";	
		up1 = setupImage("/objects/green_orb", gp.tileSize, gp.tileSize);
	}
	
	
}

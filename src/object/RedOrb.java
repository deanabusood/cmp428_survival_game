package object;

import entity.Entity;
import main.GamePanel;

public class RedOrb extends Entity{
	
	public RedOrb(GamePanel gp) {
		super(gp);
		
		name = "Red";
		up1 = setupImage("/objects/red_orb", gp.tileSize, gp.tileSize);
	}
	
	
}

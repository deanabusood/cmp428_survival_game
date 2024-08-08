package object;

import entity.Entity;
import main.GamePanel;

public class Heart extends Entity{

	public Heart(GamePanel gp) {
		super(gp);
		
		name = "Heart";
		image = setupImage("/objects/full_heart", gp.tileSize, gp.tileSize);
		image2 = setupImage("/objects/half_heart", gp.tileSize, gp.tileSize);
		image3 = setupImage("/objects/empty_heart", gp.tileSize, gp.tileSize);
	}
	
}

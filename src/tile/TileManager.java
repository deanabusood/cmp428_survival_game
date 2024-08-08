package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.Utility;

public class TileManager {
	GamePanel gp;
	
	public Tile[] tile;
	public int mapTileNum[][];
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		tile = new Tile[10];
		
		mapTileNum = new int [gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage();
		//initial
//		loadMap("res/maps/map.txt");
		loadMap("res/maps/first_level.TXT");
	}
	
	public void getTileImage() {
			
		
//			try {
//				tile[0] = new Tile();
//				tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
//				tile[0].collision = false;
//				
//				tile[1] = new Tile();
//				tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/rock.png"));
//				tile[1].collision = true;
//				
//				tile[2] = new Tile();
//				tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/bush.png"));
//				tile[2].collision = true;
//				
//				tile[3] = new Tile();
//				tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sign.png"));
//				tile[3].collision = true;
//			} catch (IOException e) {
//				
//				e.printStackTrace();
//			}
			
			setUpTiles(0, "grass", false);
			setUpTiles(1, "rock", true);
			setUpTiles(2, "bush", true);
			setUpTiles(3, "sign", true);
	}
	
	//set up tiles - improve render performance
	public void setUpTiles(int index, String name, boolean collision) {
		Utility utility = new Utility();
		
		try {
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+name+".png"));
			tile[index].image = utility.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void loadMap(String fileName) {
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
				
			int col = 0;
			int row = 0;
			
			String line = reader.readLine();
				
				while(col < gp.maxWorldCol) {
					
					String [] numbers = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[col][row] = num; // 16 x 12// changed to 50x50
					col++;
					
					if(col == gp.maxWorldCol) {
						row++;
						col = 0; 
						line = reader.readLine(); //read again for next line //now use map shown in download site
						
						//check if it's finished otherwise it'll return array out of bounds
						if(row == gp.maxWorldRow) {
							break;
						}
					}
					
					
					}					
			
			reader.close();	
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		
		int worldCol = 0;
		int worldRow = 0;
		
		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) { //50 * 50
			
			int tileNum = mapTileNum[worldCol][worldRow];
			
			int worldX = worldCol * gp.tileSize; //_ * 48
			int worldY = worldRow * gp.tileSize; //_ * 48
			int screenX = worldX - gp.player.worldX + gp.player.screenX; //determines where on screen to draw
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			//improves render to only load tiles visible to player/in near distance (+/- gp.tileSize removes black border)
			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
			&& worldY +gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
			}
			
			worldCol++;
			
			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;
			}
			
			
		}
		
		
	}
	
}

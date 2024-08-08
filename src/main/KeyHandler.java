package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

	public boolean upPressed, downPressed, leftPressed, rightPressed;
	public boolean enterPressed;
	GamePanel gp;
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		//TITLE STATE
		if(gp.gameState == gp.titleState) {
			if(code == KeyEvent.VK_W) {
				gp.ui.optionNum--;
				if(gp.ui.optionNum < 0) {
					gp.ui.optionNum = 1;
				}
				
			}
			if(code == KeyEvent.VK_S) {
				gp.ui.optionNum++;
				if(gp.ui.optionNum > 1) {
					gp.ui.optionNum = 0;
				}
			}
			if(code == KeyEvent.VK_ENTER) {
				//START
				if(gp.ui.optionNum == 0) {
					this.gp.gameState = this.gp.playState;
				}//QUIT
				else if(gp.ui.optionNum == 1) {
					System.exit(0);
				}
				
			}
		}
		//PLAYSTATE
		else if(gp.gameState == gp.playState) {
			if(code == KeyEvent.VK_W) {
				upPressed = true;
			}
			if(code == KeyEvent.VK_A) {
				leftPressed = true;
			}
			if(code == KeyEvent.VK_S) {
				downPressed = true;
			}
			if(code == KeyEvent.VK_D) {
				rightPressed = true;
			}
		
			//PAUSE
			if(code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.pauseState;
			}
			if(code == KeyEvent.VK_ENTER) {
				enterPressed = true;
			}
		}
		//PAUSE STATE
		else if(gp.gameState == gp.pauseState) {
			if(code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.playState;
			}
		}
		//POPUP STATE
		else if(gp.gameState == gp.popupState) {
			if(code == KeyEvent.VK_ENTER) {
				gp.gameState = gp.playState;
			}
		}
		
		//GAME OVER STATE
		else if(gp.gameState == gp.gameOverState) {
			if(code == KeyEvent.VK_W) {
				gp.ui.optionNum--;
				
				if(gp.ui.optionNum < 0) {
					gp.ui.optionNum = 1;
				}
			}
			else if(code == KeyEvent.VK_S) {
				gp.ui.optionNum++;
				
				if(gp.ui.optionNum > 1) {
					gp.ui.optionNum = 0;
				}
			}
			if(code == KeyEvent.VK_ENTER) {
				//RETRY
				if(gp.ui.optionNum == 0) {
					gp.gameState = gp.playState;
					gp.retryOption();
				}
				//QUIT
				if(gp.ui.optionNum == 1) {
					gp.gameState = gp.titleState;
					gp.quitOption();
				}
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_W) {
			upPressed = false;
		}
		if(code == KeyEvent.VK_A) {
			leftPressed = false;
		}
		if(code == KeyEvent.VK_S) {
			downPressed = false;
		}
		if(code == KeyEvent.VK_D) {
			rightPressed = false;
		}
		
	}

	
	//UNUSED
	@Override
	public void keyTyped(KeyEvent e) {
	}

}

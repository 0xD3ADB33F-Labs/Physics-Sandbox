package io.github.nickelme.Physics_Sandbox;

import javax.swing.JFileChooser;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

public class PhysicUserInput implements InputProcessor {

	private PhysicsSandboxGame psGame;
	
	private PhysicsWorld world;
	
	private ObjectThrower objThrow;
	
	private boolean bIsDragging = false;
	
	private JFileChooser filechoose;
	
	boolean shootsphere = true;
	
	//Sphere:    0
	//Model:     1
	//Explosion: 2
	int clickMode = 0;
	
	String modelToThrow = null;
	
	public PhysicUserInput(PhysicsSandboxGame curGame){
		psGame = curGame;
		objThrow = new ObjectThrower(psGame);
		filechoose = new JFileChooser();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		world = psGame.getPhysicsWorld();
		switch(keycode){
		
		case Keys.RIGHT_BRACKET:
			world.increaseStepSpeed();
			return true;
			
		case Keys.LEFT_BRACKET:
			world.decreaseStepSpeed();
			return true;
			
		case Keys.L:
			int ret = filechoose.showOpenDialog(null);
			if(ret == JFileChooser.APPROVE_OPTION){
		        modelToThrow = filechoose.getSelectedFile().getAbsolutePath();
		        
		        clickMode = 1;
			}
			return true;
			
		case Keys.K:
			clickMode = 0;
			return true;
			
		case Keys.O:
			clickMode = 2;
			return true;
			
		case Keys.F5:
			psGame.ClearWorld();
			psGame.CreateCubeOfCubes();
			return true;
			
		case Keys.F6:
			psGame.ClearWorld();
			psGame.CreateCubePyramid();
			return true;
			
		case Keys.F7:
			psGame.ClearWorld();
			psGame.CreateBowlingAlley();
			return true;
			
		case Keys.F8:
			psGame.ClearWorld();
			psGame.CreateCoinFlip();
			return true;
			
		case Keys.P:
			psGame.bDebugRender = !psGame.bDebugRender;
			return true;
			
		case Keys.B:
			psGame.Explode(new Vector3(40,40,40), 1000.0f, 100000.0f);
			return true;
			
		case Keys.F:
			psGame.FlipCoin();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(!bIsDragging){
			switch(clickMode){
			case 0:
				objThrow.ThrowObject(new Vector3(screenX, screenY, 1));
				break;
				
			case 1:
				objThrow.ThrowModel(new Vector3(screenX, screenY, 1), modelToThrow);
				break;
				
			case 2:
				objThrow.ThrowBomb(new Vector3(screenX, screenY, 1));
				break;
			}
		}else{
			bIsDragging = false;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		bIsDragging = true;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}

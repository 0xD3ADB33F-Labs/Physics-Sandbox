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
	
	String modelToThrow = null;
	
	public PhysicUserInput(PhysicsSandboxGame curGame){
		psGame = curGame;
		objThrow = new ObjectThrower(psGame);
		filechoose = new JFileChooser();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		world = new PhysicsWorld();
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
		        
				shootsphere = false;
			}
			return true;
			
		case Keys.K:
			shootsphere = true;
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
			if(shootsphere){
				objThrow.ThrowObject(new Vector3(screenX, screenY, 1));
			}else{
				objThrow.ThrowModel(new Vector3(screenX, screenY, 1), modelToThrow);
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

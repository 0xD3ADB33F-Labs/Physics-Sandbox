package io.github.nickelme.Physics_Sandbox;

import java.io.File;

import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;

public class PhysicUserInput implements InputProcessor {

	private PhysicsSandboxGame psGame;
	
	private ObjectThrower objThrow;
	
	private boolean bIsDragging = false;
	
	private JFileChooser filechoose;
	
	private boolean shootsphere = true;
	
	private String modelToThrow = null;
	
	public PhysicUserInput(PhysicsSandboxGame curGame){
		psGame = curGame;
		objThrow = new ObjectThrower(psGame);
		filechoose = new JFileChooser();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		
		case Keys.RIGHT_BRACKET:
			psGame.increasePhysicsStepSpeed();
			return true;
			
		case Keys.LEFT_BRACKET:
			psGame.decreasePhysicsStepSpeed();
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

package io.github.nickelme.Physics_Sandbox;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;


// AKA The Bully
public class ObjectThrower {
	
	private PhysicsSandboxGame psGame;
	private float throwSpeed = 500.0f;
	
	
	public ObjectThrower(PhysicsSandboxGame parent){
		psGame = parent;
	}
	
	public void ThrowObject(Vector3 screenSpace){
		Camera cam = psGame.getCamera();
		Vector3 location = cam.position;
		Vector3 dir = cam.unproject(screenSpace);
		dir.nor();
		dir.scl(throwSpeed);
		PrimitiveSphere sphere = new PrimitiveSphere(new Vector3(20.0f, 20.0f, 20.0f), new Matrix4(location, new Quaternion(), new Vector3(1,1,1)));
		sphere.setMassMultiplier(100.0f);
		//cube.SetColor(Color.ORANGE);
		psGame.addObject(sphere);
		sphere.setVelocity(dir);
	}
	
	public void ThrowBomb(Vector3 screenSpace){
		Camera cam = psGame.getCamera();
		Vector3 location = cam.position;
		Vector3 dir = cam.unproject(screenSpace);
		dir.nor();
		dir.scl(throwSpeed);
		BombObject sphere = new BombObject(new Vector3(20.0f, 20.0f, 20.0f), new Matrix4(location, new Quaternion(), new Vector3(1,1,1)), 10000.0f, 10000.0f);
		//sphere.setMassMultiplier(100.0f);
		//cube.SetColor(Color.ORANGE);
		psGame.addObject(sphere);
		sphere.setVelocity(dir);
	}
	
	public void ThrowModel(Vector3 screenSpace, String model){
		Camera cam = psGame.getCamera();
		Vector3 location = cam.position;
		Vector3 dir = cam.unproject(screenSpace);
		dir.nor();
		dir.scl(throwSpeed);
		ModelObject sphere = new ModelObject(model, new Matrix4(location, new Quaternion(), new Vector3(1.0f, 1.0f, 1.0f)));
		//cube.SetColor(Color.ORANGE);
		psGame.addObject(sphere);
		sphere.setVelocity(dir);
	}
	
	
	

}

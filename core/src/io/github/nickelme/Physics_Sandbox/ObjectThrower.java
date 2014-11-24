package io.github.nickelme.Physics_Sandbox;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
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
		PrimitiveSphere sphere = new PrimitiveSphere(new Vector3(25.0f, 25.0f, 25.0f), new Matrix4(location, new Quaternion(), new Vector3(1,1,1)));
		//cube.SetColor(Color.ORANGE);
		psGame.addObject(sphere);
		sphere.setVelocity(dir);
	}

}

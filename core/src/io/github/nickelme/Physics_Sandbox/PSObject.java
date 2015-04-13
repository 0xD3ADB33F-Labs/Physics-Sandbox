package io.github.nickelme.Physics_Sandbox;

import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public abstract class PSObject implements RenderableProvider {

	public abstract btRigidBody getRigidBody();
	
	private Long Id;
	
	public Long getId(){
		return Id;
	}
	
	public void setId(Long newId){
		Id = newId;
	}
	
	public abstract void Update();
	
	public abstract void setLocation(Vector3 location);
	
	public abstract void setMatrix(Matrix4 mat);
	
	public abstract void setVelocity(Vector3 vel);
	
	public abstract Matrix4 getMatrix();
	
	public abstract Vector3 getVelocity();
	
	public abstract Vector3 getSize();
	
	public abstract boolean needsNeetUpdate();
}

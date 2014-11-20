package io.github.nickelme.Physics_Sandbox;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public abstract class PSObject implements RenderableProvider {

	public abstract btRigidBody getRigidBody();
	
	public abstract void Update();
	
	public abstract void setLocation(Vector3 location);
	
	public abstract void setVelocity(Vector3 vel);

}

package io.github.nickelme.Physics_Sandbox;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public abstract class PSObject implements RenderableProvider {

	public abstract btRigidBody getRigidBody();
	
	public abstract void Update();

}

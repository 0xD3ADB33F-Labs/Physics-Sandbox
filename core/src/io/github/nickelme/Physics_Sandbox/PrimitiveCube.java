package io.github.nickelme.Physics_Sandbox;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class PrimitiveCube extends PSObject {

	protected Model rendercube;
	protected ModelInstance instance;
	protected btRigidBody rigidbody;
	protected Matrix4 worldTransform;
	protected Vector3 boxExtent;
	
	protected Matrix4 lastnetup;
	
	protected float density;
	
	
	
	public PrimitiveCube(Vector3 size, Matrix4 transform) {
		density = 750.0f;
		boxExtent = size;
		worldTransform = transform;
        ModelBuilder modelBuilder = new ModelBuilder();
        rendercube = modelBuilder.createBox(boxExtent.x , boxExtent.y, boxExtent.z, 
            new Material(
            		//ColorAttribute.createDiffuse(Color.GREEN),
            		ColorAttribute.createDiffuse(colors[(int)(Math.random() * ((14)))])),
            		Usage.Position | Usage.Normal);
        
        instance = new ModelInstance(rendercube);
        lastnetup = worldTransform.cpy();
	}
	
	public void SetColor(Color newcolor){
        ModelBuilder modelBuilder = new ModelBuilder();
        rendercube = modelBuilder.createBox(boxExtent.x, boxExtent.y, boxExtent.z, 
                new Material(ColorAttribute.createDiffuse(newcolor)),
                Usage.Position | Usage.Normal);
        instance = new ModelInstance(rendercube);
        
	}
	
	@Override
	public btRigidBody getRigidBody() {
		if(rigidbody == null){
			Vector3 halfBoxExtent = boxExtent.cpy().scl(0.5f);
			btCollisionShape fallShape = new btBoxShape(halfBoxExtent);
			btDefaultMotionState fallMotionState = new btDefaultMotionState(worldTransform);
	        float mass = (float) ((boxExtent.x * boxExtent.y * boxExtent.z)*density)*0.0254f;
	        Vector3 fallInertia = new Vector3(0, 0, 0);
	        fallShape.calculateLocalInertia(mass, fallInertia);
	        //fallShape.setMargin(0.00001f);
	       
	        btRigidBodyConstructionInfo fallRigidBodyCI = new btRigidBodyConstructionInfo(mass, fallMotionState, fallShape, fallInertia);
	        rigidbody = new btRigidBody(fallRigidBodyCI);
	        
	        rigidbody.setRestitution(0.5f);
	        rigidbody.setFriction(1f);
	        //rigidbody.setSleepingThresholds(0.01f, 0.01f);
	        //fallRigidBodyCI.dispose();
	       // System.out.println("RenderCube: " + boxExtent.x/3 + " " + boxExtent.y/3 + " " + boxExtent.z/3 + "\nRigidBody: " + halfBoxExtent );
		} 
		return rigidbody;
	}

	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		instance.getRenderables(renderables, pool);
	}
	
	@Override
	public void Update() {
		rigidbody.getMotionState().getWorldTransform(instance.transform);
		//instance.transform.scale(0.5f, 0.5F, 0.5F);
		worldTransform.set(instance.transform);
	}

	@Override
	public void setLocation(Vector3 location) {
		worldTransform.setTranslation(location);
		rigidbody.setWorldTransform(worldTransform);
		instance.transform.set(worldTransform);
		
	}

	@Override
	public void setVelocity(Vector3 vel) {
		rigidbody.setLinearVelocity(vel);
		
	}

	@Override
	public Matrix4 getMatrix() {
		return worldTransform;
	}

	@Override
	public Vector3 getVelocity() {
		return null;
	}

	@Override
	public Vector3 getSize() {
		return boxExtent;
	}

	@Override
	public void setMatrix(Matrix4 mat) {
		rigidbody.getMotionState().setWorldTransform(mat);
		worldTransform.set(mat);
	}

	@Override
	public boolean needsNeetUpdate() {
		if(Arrays.equals(lastnetup.val, worldTransform.val)){
			return false;
		}else{
			lastnetup = worldTransform.cpy();
			return true;
		}
	}
	
	
	

}

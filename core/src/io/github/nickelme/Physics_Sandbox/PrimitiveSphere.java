package io.github.nickelme.Physics_Sandbox;

import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class PrimitiveSphere extends PSObject{

	Model rendersphere;
	ModelInstance instance;
	btRigidBody rigidbody;
	Matrix4 worldTransform;
	Vector3 spheresize;
	private float density = 750.0f;
	
	public PrimitiveSphere(Vector3 size, Matrix4 transform){
		worldTransform = transform;
		spheresize = size;
		ModelBuilder modelbuilder = new ModelBuilder();
		
		rendersphere = modelbuilder.createSphere(spheresize.x, spheresize.y, spheresize.z, 32, 32, new Material(ColorAttribute.createDiffuse(colors[1 + (int)(Math.random() * ((14 - 1) + 1))])),Usage.Position | Usage.Normal);
		instance = new ModelInstance(rendersphere);
		
	}


	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		instance.getRenderables(renderables, pool);
		
	}

	


	@Override
	public btRigidBody getRigidBody() {
		if (rigidbody == null){
			btCollisionShape fallshShape = new btSphereShape(spheresize.x/2);
			btDefaultMotionState motionstate = new btDefaultMotionState(worldTransform);
			float mass = ((4/3)*(((float)Math.PI)*((spheresize.x * spheresize.x*spheresize.x)*0.0254f)))*density;
			Vector3 fallinertia = new Vector3(0, 0, 0);
			fallshShape.calculateLocalInertia(mass, fallinertia);
			btRigidBodyConstructionInfo fallrigidbodyCI =new btRigidBodyConstructionInfo(mass, motionstate, fallshShape);
			rigidbody = new btRigidBody(fallrigidbodyCI);
			
			//stuuff
			rigidbody.setRestitution(0.5f);
			rigidbody.setDamping(0.2f, 0.1f);
			rigidbody.setGravity(new Vector3(0f, 0f, 0f));
			//rigidbody.setAngularFactor(1f);
			
			//rigidbody.setAngularVelocity(new Vector3(0.0f,-1.5f,2f));
			//rigidbody.setAnisotropicFriction(new Vector3(0.1f, 2f, 2f));
		}
		return rigidbody;
	}


	@Override
	public void Update() {
		rigidbody.getMotionState().getWorldTransform(instance.transform);
		//instance.transform.scale(2.0f, 2.0f, 2.0f);
		
	}


	@Override
	public void setLocation(Vector3 location) {
		worldTransform.setTranslation(location);
		rigidbody.setWorldTransform(worldTransform);
		instance.transform.set(worldTransform);
		
	}


	@Override
	public void setVelocity(Vector3 vel) {
		if(rigidbody != null){
			rigidbody.setLinearVelocity(vel);
		}else{
			getRigidBody().setLinearVelocity(vel);
		}
	}


	@Override
	public Matrix4 getMatrix() {
		return null;
	}


	@Override
	public Vector3 getVelocity() {
		return null;
	}


	@Override
	public Vector3 getSize() {
		return null;
	}


	@Override
	public void setMatrix(Matrix4 mat) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean needsNeetUpdate() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}

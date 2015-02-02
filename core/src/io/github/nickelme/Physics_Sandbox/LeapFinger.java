package io.github.nickelme.Physics_Sandbox;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.physics.bullet.collision.ContactResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Vector;

class LeapFingerCollider extends ContactResultCallback{
	
	public LeapFingerCollider() {
		super();
	}
	
	LeapFinger leap;
	
	@Override
	public float addSingleResult(btManifoldPoint cp, btCollisionObjectWrapper colObj0Wrap, int partId0, int index0, btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
		if(leap != null && leap.rigidbody != null){
			leap.rigidbody.getMotionState().setWorldTransform(leap.previousTransform);
		}
		return 0.0f;
	}
}

public class LeapFinger extends PSObject {

	Model renderhand;
	ModelInstance instance;
	btRigidBody rigidbody;
	Matrix4 worldTransform;
	Vector3 fingerSize;
	
	Matrix4 previousTransform;
	
	LeapFingerCollider callback;
	
	private boolean isAlive = true;
	
	public LeapFinger(){
		callback = new LeapFingerCollider();
		callback.leap = this;
		worldTransform = new Matrix4();
		previousTransform = new Matrix4();
		fingerSize = new Vector3(5f, 5f, 5f);
		Gdx.app.postRunnable(new Runnable(){

			@Override
			public void run() {
				ModelBuilder modelBuilder = new ModelBuilder();
				renderhand = modelBuilder.createSphere(fingerSize.x, fingerSize.y, fingerSize.z,32, 32, new Material(ColorAttribute.createDiffuse(Color.CYAN)),Usage.Position | Usage.Normal);

				instance = new ModelInstance(renderhand);
				PhysicsSandboxGame psGame = PhysicsSandboxGame.getInstance();
				psGame.addObject(LeapFinger.this);
			}
		});
	}
	
	public void LeapUpdate(Finger finger){
		Vector vec = finger.jointPosition(Finger.Joint.JOINT_TIP);
		Vector3 vec3 = new Vector3(vec.getX(), vec.getY(), vec.getZ());
		vec3.scl(LeapController.LEAP_SCALE_FACTOR);
		worldTransform.setTranslation(vec3);
	}
	
	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		instance.getRenderables(renderables, pool);

	}

	@Override
	public btRigidBody getRigidBody() {
		if (rigidbody == null){
			btCollisionShape fallshShape = new btSphereShape(fingerSize.x);
			btDefaultMotionState motionstate = new btDefaultMotionState(worldTransform);
			float mass = 0;
			Vector3 fallinertia = new Vector3(0, 0, 0);
			fallshShape.calculateLocalInertia(mass, fallinertia);
			btRigidBodyConstructionInfo fallrigidbodyCI =new btRigidBodyConstructionInfo(mass, motionstate, fallshShape);
			rigidbody = new btRigidBody(fallrigidbodyCI);
			rigidbody.setCollisionFlags(rigidbody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
			rigidbody.setActivationState(4);

		}
		return rigidbody;
	}

	@Override
	public void Update() {
		if(instance != null){
			Vector3 loc = new Vector3();
			worldTransform.getTranslation(loc);
			instance.transform.setTranslation(loc);
			Matrix4 trans = instance.transform.cpy();
			trans.scale(0.5f, 0.5f, 0.5f);
			rigidbody.getMotionState().getWorldTransform(previousTransform);
			rigidbody.getMotionState().setWorldTransform(trans);
			//PhysicsSandboxGame ps = PhysicsSandboxGame.getInstance();
			//ps.getPhysicsWorld().getWorld().contactTest(rigidbody, callback);
		}

		if(!isAlive){
			PhysicsSandboxGame.getInstance().RemoveObject(this);
		}

	}

	@Override
	public void setLocation(Vector3 location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVelocity(Vector3 vel) {
		// TODO Auto-generated method stub

	}
	
	public void CleanSelfUp(){
		isAlive = false;
	}

}

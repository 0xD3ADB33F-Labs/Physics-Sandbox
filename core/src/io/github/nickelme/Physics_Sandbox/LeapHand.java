package io.github.nickelme.Physics_Sandbox;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btActivatingCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Matrix;
import com.leapmotion.leap.Vector;

public class LeapHand extends PSObject {

	Model renderhand;
	ModelInstance instance;
	btRigidBody rigidbody;
	Matrix4 worldTransform;
	Vector3 spheresize;
	
	HashMap<Finger.Type, LeapFinger> Fingers;
	


	private boolean isAlive = true;

	public LeapHand(){
		Fingers = new HashMap<Finger.Type, LeapFinger>();
		worldTransform = new Matrix4();
		spheresize = new Vector3(20.0f, 2.5f, 20.0f);

		Gdx.app.postRunnable(new Runnable(){

			@Override
			public void run() {
				ModelBuilder modelBuilder = new ModelBuilder();
				renderhand = modelBuilder.createSphere(spheresize.x, spheresize.y, spheresize.z, 32, 32, new Material(ColorAttribute.createDiffuse(Color.CYAN)),Usage.Position | Usage.Normal);

				instance = new ModelInstance(renderhand);
				PhysicsSandboxGame psGame = PhysicsSandboxGame.getInstance();
				psGame.addObject(LeapHand.this);
			}
		});
	}

	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		instance.getRenderables(renderables, pool);

	}

	@Override
	public btRigidBody getRigidBody() {
		if (rigidbody == null){
			btCollisionShape fallshShape = new btBoxShape(spheresize);
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

	public void LeapUpdate(Hand leapHand){
	    Vector handXBasis =  leapHand.palmNormal().cross(leapHand.direction()).normalized();
	    Vector handYBasis = leapHand.palmNormal().opposite();
	    Vector handZBasis = leapHand.direction().opposite();
	    Vector handOrigin =  leapHand.palmPosition();
	    handOrigin.setX(handOrigin.getX()*LeapController.LEAP_SCALE_FACTOR);
	    handOrigin.setY(handOrigin.getY()*LeapController.LEAP_SCALE_FACTOR);
	    handOrigin.setZ(handOrigin.getZ()*LeapController.LEAP_SCALE_FACTOR);
	    Matrix handTransform = new Matrix(handXBasis, handYBasis, handZBasis, handOrigin);
		
		//worldTransform.setTranslation(vec3);
		worldTransform.set(handTransform.toArray4x4());
		
		FingerList fgrlist = leapHand.fingers();
		Iterator<Finger> fgritr = fgrlist.iterator();
		while(fgritr.hasNext()){
			Finger fgr = fgritr.next();
			if(Fingers.containsKey(fgr.type())){
				Fingers.get(fgr.type()).LeapUpdate(fgr);
			}else{
				LeapFinger psfinger = new LeapFinger();
				Fingers.put(fgr.type(), psfinger);
				psfinger.LeapUpdate(fgr);
			}
		}

	}

	@Override
	public void Update() {
		if(instance != null){
			Vector3 loc = new Vector3();
			worldTransform.getTranslation(loc);
			instance.transform.set(worldTransform);
			Matrix4 trans = instance.transform.cpy();
			trans.scale(0.5f, 0.5f, 0.5f);
			rigidbody.getMotionState().setWorldTransform(trans);
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
		Iterator<Finger.Type> fgritr = Fingers.keySet().iterator();
		while(fgritr.hasNext()){
			Finger.Type curfgr = fgritr.next();
			Fingers.get(curfgr).CleanSelfUp();
			Fingers.remove(curfgr);
		}
	}

}

package io.github.nickelme.Physics_Sandbox;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
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
import com.leapmotion.leap.Bone;
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
	Matrix4 localTransform;
	Vector3 spheresize;

	HashMap<Finger.Type, HashMap<Bone.Type, LeapFinger>> Fingers;



	private boolean isAlive = true;

	public LeapHand(Hand hand){
		Fingers = new HashMap<Finger.Type, HashMap<Bone.Type, LeapFinger>>();
		worldTransform = new Matrix4();
		localTransform = new Matrix4();
		spheresize = new Vector3(hand.palmWidth()*LeapController.LEAP_SCALE_FACTOR, 5.0f*LeapController.LEAP_SCALE_FACTOR, hand.palmWidth()*LeapController.LEAP_SCALE_FACTOR);

		Gdx.app.postRunnable(new Runnable(){

			@Override
			public void run() {
				ModelBuilder modelBuilder = new ModelBuilder();
				renderhand = modelBuilder.createSphere(spheresize.x, spheresize.y, spheresize.z, 32, 32, new Material(ColorAttribute.createDiffuse(Color.CYAN)),Usage.Position | Usage.Normal);

				instance = new ModelInstance(renderhand);
				PhysicsSandboxGame psGame = PhysicsSandboxGame.getInstance();
				Camera cam = PhysicsSandboxGame.getInstance().cam;
				Vector3 dir = cam.direction.cpy();
				dir.x = -dir.x;
				dir.y = 0;
				Matrix4 rot = new Matrix4();
				rot.setToWorld(Vector3.Zero, dir, Vector3.Y);
				dir.nor();
				Matrix4 trans = new Matrix4();
				trans.setTranslation(0.0f, -50.0f, -100.0f);
				worldTransform.set(cam.position, new Quaternion()).mul(rot).mul(trans).mul(localTransform);
				instance.transform.set(worldTransform);
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
			Vector3 halfsize = spheresize.cpy();
			halfsize.scl(0.5f);
			btCollisionShape fallShape = new btBoxShape(halfsize);
			btDefaultMotionState motionstate = new btDefaultMotionState(worldTransform);
			float mass = (float)((spheresize.x * spheresize.y * spheresize.z)*0.0254f)*LeapController.HUMAN_MASS_FACTOR;
			Vector3 fallinertia = new Vector3(0, 0, 0);
			fallShape.calculateLocalInertia(mass, fallinertia);
			btRigidBodyConstructionInfo fallrigidbodyCI =new btRigidBodyConstructionInfo(mass, motionstate, fallShape);
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
		handOrigin.setY(handOrigin.getY() - 100.0f*LeapController.LEAP_SCALE_FACTOR);
		//handOrigin.setZ(handOrigin.getZ() + 50.0f);
		//handOrigin = handOrigin.normalized();
		Matrix handTransform = new Matrix(handXBasis, handYBasis, handZBasis, handOrigin);

		//worldTransform.setTranslation(vec3);
		localTransform.set(handTransform.toArray4x4());

		FingerList fgrlist = leapHand.fingers();
		Iterator<Finger> fgritr = fgrlist.iterator();
		while(fgritr.hasNext()){
			Bone.Type[] bones = Bone.Type.values();
			Finger fgr = fgritr.next();
			if(!Fingers.containsKey(fgr.type())){
				Fingers.put(fgr.type(), new HashMap<Bone.Type,LeapFinger>());
			}
			for(int i = 1; i < bones.length; i++){
				Bone.Type curbone = bones[i];
				
				if(Fingers.get(fgr.type()).containsKey(curbone)){
					Fingers.get(fgr.type()).get(curbone).LeapUpdate(fgr);
				}else{
					Vector3 FingerSize = new Vector3();
					Bone bone = fgr.bone(curbone);
					FingerSize.x = bone.width();
					FingerSize.y = bone.width();
					FingerSize.z = bone.length();
					FingerSize.scl(LeapController.LEAP_SCALE_FACTOR);
					LeapFinger psfinger = new LeapFinger(this, fgr.type(), curbone, FingerSize);
					Fingers.get(fgr.type()).put(curbone, psfinger);
					psfinger.LeapUpdate(fgr);
				}
			}
		}

	}

	@Override
	public void Update() {
		if(instance != null){
			Vector3 loc = new Vector3();
			Camera cam = PhysicsSandboxGame.getInstance().cam;
			Vector3 dir = cam.direction.cpy();
			dir.x = -dir.x;
			dir.y = 0;
			Matrix4 rot = new Matrix4();
			rot.setToWorld(Vector3.Zero, dir, Vector3.Y);
			dir.nor();
			Matrix4 trans = new Matrix4();
			trans.setTranslation(0.0f, -50.0f*LeapController.LEAP_SCALE_FACTOR, -100.0f*LeapController.LEAP_SCALE_FACTOR);
			worldTransform.set(cam.position, new Quaternion()).mul(rot).mul(trans).mul(localTransform);
			instance.transform.set(worldTransform);
			//Matrix4 trans = instance.transform.cpy();
			//trans.scale(0.5f, 0.5f, 0.5f);
			rigidbody.getMotionState().setWorldTransform(worldTransform);
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
			Bone.Type[] bones = Bone.Type.values();
			for(int i = 1; i < bones.length; i++){
				Fingers.get(curfgr).get(bones[i]).CleanSelfUp();
				Fingers.get(curfgr).remove(bones[i]);
			}
			Fingers.remove(curfgr);
		}
	}

	@Override
	public Matrix4 getMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector3 getVelocity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector3 getSize() {
		// TODO Auto-generated method stub
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

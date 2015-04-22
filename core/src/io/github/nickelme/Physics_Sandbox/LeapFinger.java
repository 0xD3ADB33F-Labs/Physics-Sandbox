package io.github.nickelme.Physics_Sandbox;

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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ContactResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.physics.bullet.dynamics.btHingeConstraint;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Matrix;
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
	Matrix4 localTransform;
	Vector3 fingerSize;
	
	Matrix4 previousTransform;
	
	LeapFingerCollider callback;
	
	LeapHand mParent;
	Finger.Type mType;
	Bone.Type mBone;
	
	btHingeConstraint mHinge;
	Vector3 mHingeloc;
	
	
	private boolean isAlive = true;
	
	public LeapFinger(LeapHand parent, Finger.Type type, Bone.Type bone, Vector3 size){
		mParent = parent;
		mType = type;
		mBone = bone;
		callback = new LeapFingerCollider();
		callback.leap = this;
		worldTransform = new Matrix4();
		previousTransform = new Matrix4();
		localTransform = new Matrix4();
		fingerSize = size;
		Gdx.app.postRunnable(new Runnable(){

			@Override
			public void run() {
				ModelBuilder modelBuilder = new ModelBuilder();
				renderhand = modelBuilder.createBox(fingerSize.x, fingerSize.y, fingerSize.z, new Material(ColorAttribute.createDiffuse(Color.CYAN)),Usage.Position | Usage.Normal);

				instance = new ModelInstance(renderhand);
				
				PhysicsSandboxGame psGame = PhysicsSandboxGame.getInstance();
				psGame.addObject(LeapFinger.this);
			}
		});
	}
	
	public void LeapUpdate(Finger finger){
		Bone bone = finger.bone(mBone);
		Vector vec = bone.center();
		Matrix basis = bone.basis();
		basis.setOrigin(vec);
		basis.setOrigin(basis.getOrigin().times(LeapController.LEAP_SCALE_FACTOR));
		basis.getOrigin().setY(basis.getOrigin().getY() - 100.0f*LeapController.LEAP_SCALE_FACTOR);
		//basis.getOrigin().setZ(basis.getOrigin().getZ() + 50.0f);
		//Vector3 vec3 = new Vector3(vec.getX(), vec.getY(), vec.getZ());
		//vec3.scl(LeapController.LEAP_SCALE_FACTOR);
		localTransform.set(basis.toArray4x4());
		Vector hingeloc = bone.prevJoint();
		mHingeloc = new Vector3(hingeloc.getX(), hingeloc.getY(), hingeloc.getZ());
		mHingeloc.scl(LeapController.LEAP_SCALE_FACTOR);
		
	}
	
	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		instance.getRenderables(renderables, pool);

	}

	@Override
	public btRigidBody getRigidBody() {
		if (rigidbody == null){
			Vector3 halfsize = fingerSize.cpy();
			halfsize.scl(0.5f);
			btCollisionShape fallshShape = new btBoxShape(halfsize);
			btDefaultMotionState motionstate = new btDefaultMotionState(worldTransform);
			float mass = (float)((fingerSize.x * fingerSize.y * fingerSize.z)*0.0254f)*LeapController.HUMAN_MASS_FACTOR;
			Vector3 fallinertia = new Vector3(0, 0, 0);
			fallshShape.calculateLocalInertia(mass, fallinertia);
			btRigidBodyConstructionInfo fallrigidbodyCI =new btRigidBodyConstructionInfo(mass, motionstate, fallshShape);
			rigidbody = new btRigidBody(fallrigidbodyCI);
			rigidbody.setFriction(1.0f);
			//hinge = new btHingeConstraint(rigidbody, )
			rigidbody.setCollisionFlags(rigidbody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
			rigidbody.setActivationState(4);

		}
		return rigidbody;
	}

	@Override
	public void Update() {
		if(instance != null){
			Vector3 prevloc = new Vector3();
			Vector3 loc = new Vector3();
			Camera cam = PhysicsSandboxGame.getInstance().getCam();
			Vector3 dir = cam.direction.cpy();
			dir.x = -dir.x;
			dir.y = 0;
			Matrix4 rot = new Matrix4();
			rot.setToWorld(Vector3.Zero, dir, Vector3.Y);
			Matrix4 trans = new Matrix4();
			trans.setTranslation(0.0f, -50.0f*LeapController.LEAP_SCALE_FACTOR, -100.0f*LeapController.LEAP_SCALE_FACTOR);
			worldTransform.set(cam.position, new Quaternion()).mul(rot).mul(trans).mul(localTransform);
			//worldTransform.set(mParent.worldTransform).mul(localTransform);
			instance.transform.set(worldTransform);
			//Matrix4 trans = instance.transform.cpy();
			//trans.scale(0.5f, 0.5f, 0.5f);
			rigidbody.getMotionState().getWorldTransform(previousTransform);
			previousTransform.getTranslation(prevloc);
			worldTransform.getTranslation(loc);
			float dist = 0;
			if((dist = loc.dst(prevloc)) > 100.0f){
				System.out.println("Distance change high: " + dist);
				System.out.println(prevloc.toString() + " : " + loc.toString());
				
			}
			rigidbody.getMotionState().setWorldTransform(worldTransform);
			//rigidbody.setWorldTransform(trans);
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

package io.github.nickelme.Physics_Sandbox;

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
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;


class CollisionCallback extends ContactResultCallback{
	
	BombObject obj;
	
	@Override
	public float addSingleResult(btManifoldPoint cp, btCollisionObjectWrapper colObj0Wrap, int partId0, int index0, btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
		obj.Explode();
		
		return 0.0f;
	}
}

public class BombObject extends PSObject {

	
	Model rendersphere;
	ModelInstance instance;
	btRigidBody rigidbody;
	Matrix4 worldTransform;
	Vector3 spheresize;
	private float massmult = 1.0f;
	float ExplodeSize;
	float ExplodeForce;
	
	boolean hasExploded = false;
	
	CollisionCallback callback;
	
	public BombObject(Vector3 size, Matrix4 transform, float explodesize, float explodeforce){
		ExplodeForce = explodeforce;
		ExplodeSize = explodesize;
		Color[] colors = {Color.RED,Color.ORANGE,Color.YELLOW,Color.GREEN,Color.BLUE,Color.PINK,Color.PURPLE,Color.WHITE,Color.CYAN,Color.DARK_GRAY,Color.MAGENTA,Color.MAROON,Color.NAVY,Color.OLIVE,Color.TEAL};
		worldTransform = transform;
		spheresize = size;
		ModelBuilder modelbuilder = new ModelBuilder();
		
		rendersphere = modelbuilder.createSphere(spheresize.x, spheresize.y, spheresize.z, 32, 32, new Material(ColorAttribute.createDiffuse(colors[1 + (int)(Math.random() * ((14 - 1) + 1))])),Usage.Position | Usage.Normal);
		instance = new ModelInstance(rendersphere);
		
	}
	
	@Override
	public btRigidBody getRigidBody() {
		if (rigidbody == null){
			btCollisionShape fallshShape = new btSphereShape(spheresize.x);
			btDefaultMotionState motionstate = new btDefaultMotionState(worldTransform);
			float mass = (4*(((float)Math.PI)*(spheresize.x * spheresize.x)))*massmult;
			Vector3 fallinertia = new Vector3(0, 0, 0);
			fallshShape.calculateLocalInertia(mass, fallinertia);
			btRigidBodyConstructionInfo fallrigidbodyCI =new btRigidBodyConstructionInfo(mass, motionstate, fallshShape);
			rigidbody = new btRigidBody(fallrigidbodyCI);
			callback = new CollisionCallback();
			callback.obj = this;
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
		instance.transform.scale(2.0f, 2, 2);
		worldTransform.set(instance.transform);
		PhysicsSandboxGame ps = PhysicsSandboxGame.getInstance();
		if(!hasExploded){
			ps.getPhysicsWorld().getWorld().contactTest(rigidbody, callback);
		}else{
			ps.RemoveObject(this);
		}
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
	
	public void Explode(){
		PhysicsSandboxGame ps = PhysicsSandboxGame.getInstance();
		Vector3 loc = new Vector3();
		rigidbody.getMotionState().getWorldTransform(instance.transform);
		instance.transform.scale(2.0f, 2, 2);
		worldTransform.set(instance.transform);
		worldTransform.getTranslation(loc);
		ps.getStructures().Explode(loc, ExplodeSize, ExplodeForce);
		hasExploded = true;
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

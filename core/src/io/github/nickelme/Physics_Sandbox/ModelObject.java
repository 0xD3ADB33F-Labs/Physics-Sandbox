package io.github.nickelme.Physics_Sandbox;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.collision.btShapeHull;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ModelObject extends PSObject {

	protected ModelInstance instance;
	protected Model objModel;
	protected btRigidBody rigidbody;
	protected Matrix4 worldTransform;
	
	public ModelObject(Model pmodel, Matrix4 transform){
		objModel = pmodel;
		instance = new ModelInstance(objModel);
		worldTransform = transform;
	}
	
	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		instance.getRenderables(renderables, pool);
		
	}

	@Override
	public btRigidBody getRigidBody() {
		if(rigidbody == null){
			btConvexHullShape fallShape = new btConvexHullShape();
			for(int i = 0; i<objModel.meshes.size; i++){
				for(int j = 0; j<objModel.meshes.get(i).getNumVertices(); j++){
					float[] vertex = new float[3];
					objModel.meshes.get(i).getVertices(j*3, vertex);
					Vector3 point = new Vector3(vertex[0], vertex[1], vertex[2]);
					fallShape.addPoint(point);
				}
			}
			btShapeHull hull = new btShapeHull(fallShape);
			float margin = fallShape.getMargin();
			hull.buildHull(margin);
			btConvexHullShape simplifiedConvexShape = new btConvexHullShape(hull);
			
			btDefaultMotionState fallMotionState = new btDefaultMotionState(worldTransform);
	        float mass = 500f;
	        Vector3 fallInertia = new Vector3(0, 0, 0);
	        simplifiedConvexShape.calculateLocalInertia(mass, fallInertia);
	        btRigidBodyConstructionInfo fallRigidBodyCI = new btRigidBodyConstructionInfo(mass, fallMotionState, simplifiedConvexShape, fallInertia);
	        rigidbody = new btRigidBody(fallRigidBodyCI);
	        //fallRigidBodyCI.dispose();
		}
		return rigidbody;
	}

	@Override
	public void Update() {
		rigidbody.getMotionState().getWorldTransform(instance.transform);
		instance.transform.scale(1, 1, 1);
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

}

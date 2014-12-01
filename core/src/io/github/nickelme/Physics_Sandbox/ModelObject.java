package io.github.nickelme.Physics_Sandbox;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
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

	private String ModelPath;

	private static HashMap<String, Model> loadedmodels = new HashMap<String, Model>();
	private static HashMap<String, btConvexHullShape> loadedBodies = new HashMap<String, btConvexHullShape>();

	public ModelObject(String path, Matrix4 transform){
		ModelPath = path;
		if(loadedmodels.containsKey(path)){
			objModel = loadedmodels.get(ModelPath);
		}else{
			ModelLoader loader = new ObjLoader();
			objModel = loader.loadModel(Gdx.files.absolute(ModelPath));
			loadedmodels.put(path, objModel);
		}
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
			btConvexHullShape simplifiedConvexShape = null;
			if(!loadedBodies.containsKey(ModelPath)){
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
				simplifiedConvexShape = new btConvexHullShape(hull);
				loadedBodies.put(ModelPath, simplifiedConvexShape);
			}else{
				simplifiedConvexShape = loadedBodies.get(ModelPath);
			}
			btDefaultMotionState fallMotionState = new btDefaultMotionState(worldTransform);
			BoundingBox out = new BoundingBox();
			float mass = 500.0f;
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

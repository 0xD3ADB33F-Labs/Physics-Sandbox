package io.github.nickelme.Physics_Sandbox;

import java.nio.ShortBuffer;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btTriangleMesh;
import com.badlogic.gdx.physics.bullet.collision.btTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ConcaveModelObject extends PSObject {

	protected ModelInstance instance;
	protected Model objModel;
	protected btRigidBody rigidbody;
	protected Matrix4 worldTransform;

	private String ModelPath;

	private static HashMap<String, Model> loadedmodels = new HashMap<String, Model>();
	private static HashMap<String, btCollisionShape> loadedBodies = new HashMap<String, btCollisionShape>();
	
	
	public ConcaveModelObject(String path, Matrix4 transform, boolean isabsolute){
		ModelPath = path;
		if(loadedmodels.containsKey(path)){
			objModel = loadedmodels.get(ModelPath);
		}else{
			@SuppressWarnings("rawtypes")
			ModelLoader loader = new ObjLoader();
			if(isabsolute){
				objModel = loader.loadModel(Gdx.files.absolute(ModelPath));
			}else{
				objModel = loader.loadModel(Gdx.files.internal(ModelPath));
			}
			loadedmodels.put(path, objModel);
		}
		instance = new ModelInstance(objModel);
		worldTransform = transform;
	}
	
	public ConcaveModelObject(String path, Matrix4 transform){
		this(path, transform, true);
	}
	
	public String getPath(){
		return ModelPath;
	}

	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		instance.getRenderables(renderables, pool);

	}

	@Override
	public btRigidBody getRigidBody() {
		if(rigidbody == null){
			//btConvexHullShape simplifiedConvexShape = null;
			btCollisionShape shape = null;
			if(!loadedBodies.containsKey(ModelPath)){
				btTriangleMesh fallShape = new btTriangleMesh();
				for(int i = 0; i<objModel.meshParts.size; i++){
					Mesh curmesh = objModel.meshParts.get(i).mesh;
					ShortBuffer indices = curmesh.getIndicesBuffer();
					while(indices.remaining() > 0){
						short[] tri = new short[3];
						indices.get(tri);
						float[] vert1 = new float[3];
						float[] vert2 = new float[3];
						float[] vert3 = new float[3];
						curmesh.getVertices(tri[0]*(curmesh.getVertexSize()/4), vert1);
						curmesh.getVertices(tri[1]*(curmesh.getVertexSize()/4), vert2);
						curmesh.getVertices(tri[2]*(curmesh.getVertexSize()/4), vert3);
						Vector3 vec1 = new Vector3(vert1);
						Vector3 vec2 = new Vector3(vert2);
						Vector3 vec3 = new Vector3(vert3);
						fallShape.addTriangle(vec1, vec2, vec3);
					
					}
				}
				btTriangleMeshShape meshshape = new btBvhTriangleMeshShape(objModel.meshParts);
				shape = meshshape;
				loadedBodies.put(ModelPath, shape);
			}else{
				shape = loadedBodies.get(ModelPath);
			}
			btDefaultMotionState fallMotionState = new btDefaultMotionState(worldTransform);
			BoundingBox out = new BoundingBox();
			for(int i = 0; i<objModel.meshParts.size; i++){
				if(objModel.meshParts.get(i).mesh.getNumIndices() > 0){
					objModel.meshParts.get(i).mesh.extendBoundingBox(out, 0, objModel.meshParts.get(i).mesh.getNumIndices());
				}
			}
			float mass = out.getDepth()*out.getWidth()*out.getHeight();
			Vector3 fallInertia = new Vector3(0, 0, 0);
			shape.calculateLocalInertia(mass, fallInertia);
			btRigidBodyConstructionInfo fallRigidBodyCI = new btRigidBodyConstructionInfo(mass, fallMotionState, shape, fallInertia);
			rigidbody = new btRigidBody(fallRigidBodyCI);
			rigidbody.setSleepingThresholds(0.25f, 0.25f);
			
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

	@Override
	public void setMatrix(Matrix4 mat) {
		// TODO Auto-generated method stub
		
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
	public boolean needsNeetUpdate() {
		// TODO Auto-generated method stub
		return false;
	}

}


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
	
	
	public PrimitiveCube(Vector3 size, Matrix4 transform) {
		
		Color[] colors = {Color.RED,Color.ORANGE,Color.YELLOW,Color.GREEN,Color.BLUE,Color.PINK,Color.PURPLE,Color.WHITE,Color.CYAN,Color.DARK_GRAY,Color.MAGENTA,Color.MAROON,Color.NAVY,Color.OLIVE,Color.TEAL};
		
		boxExtent = size;
		worldTransform = transform;
        ModelBuilder modelBuilder = new ModelBuilder();
        rendercube = modelBuilder.createBox(boxExtent.x, boxExtent.y, boxExtent.z, 
            new Material(
            		ColorAttribute.createDiffuse(Color.GREEN),
            		//ColorAttribute.createDiffuse(colors[1 + (int)(Math.random() * ((14 - 1) + 1))])),
            		Usage.Position | Usage.Normal);
        
        instance = new ModelInstance(rendercube);
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
			btCollisionShape fallShape = new btBoxShape(boxExtent);
			btDefaultMotionState fallMotionState = new btDefaultMotionState(worldTransform);
	        float mass = boxExtent.x * boxExtent.y * boxExtent.z;
	        Vector3 fallInertia = new Vector3(0, 0, 0);
	        fallShape.calculateLocalInertia(mass, fallInertia);
	        btRigidBodyConstructionInfo fallRigidBodyCI = new btRigidBodyConstructionInfo(mass, fallMotionState, fallShape, fallInertia);
	        rigidbody = new btRigidBody(fallRigidBodyCI);
	        //fallRigidBodyCI.dispose();
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

package io.github.nickelme.Physics_Sandbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btStaticPlaneShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;

public class PhysicsSandboxGame extends ApplicationAdapter {

	private FirstPersonCameraController fpcontrol;
	private PerspectiveCamera cam;
	private ModelBatch modelBatch;
	private List<PSObject> Objects = new ArrayList<PSObject>();
	private PhysicsWorld world;
	private Long lasttick;
	private Environment env;
	private PhysicUserInput physin;
	private InputMultiplexer inplex;

	@Override
	public void create () {
		Bullet.init();
		modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 0f, 10f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 1000f;
		cam.update(true);



		world = new PhysicsWorld();
		Floor floor = new Floor(new Vector3(1000,1,1000),  new Matrix4(new Vector3(0,-5,0), new Quaternion(), new Vector3(1,1,1)));
		floor.SetColor(Color.WHITE);
		Objects.add(floor);
		world.AddObject(floor);
		for(int x = 0; x < 10; x++){
			for(int y=0; y < 10; y++){
				for(int z=0; z<5; z++){
					PrimitiveCube cube = new PrimitiveCube(new Vector3(5,5,5), new Matrix4(new Vector3(x*10,(y*10),z*10), new Quaternion(), new Vector3(1,1,1)));
					Objects.add(cube);
					world.AddObject(cube);
				}
			}
		}

		inplex = new InputMultiplexer();

		physin = new PhysicUserInput(this);
		inplex.addProcessor(physin);

		fpcontrol = new FirstPersonCameraController(cam);
		fpcontrol.setVelocity(20.0f);
		inplex.addProcessor(fpcontrol);

		Gdx.input.setInputProcessor(inplex);

		env = new Environment();
		env.set(new ColorAttribute(ColorAttribute.AmbientLight, new Color(0.5f,0.5f,0.5f, 1.0f)));
		env.add(new DirectionalLight().set(Color.WHITE, new Vector3(0,-90, 0)));

		lasttick = System.currentTimeMillis();

	}

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width;
		cam.viewportWidth = height;
		cam.update(true);
	};

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		float delta = ((float)(System.currentTimeMillis() - lasttick))/1000.0f;
		world.Stimulate(delta);
		for(int i = 0; i<Objects.size(); i++){
			Objects.get(i).Update();
		}
		fpcontrol.update();
		cam.update();

		modelBatch.begin(cam);
		for(int i = 0; i <Objects.size(); i++){
			modelBatch.render(Objects.get(i), env);
		}
		modelBatch.end();
		lasttick = System.currentTimeMillis();
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
	}

	public void increasePhysicsStepSpeed(){
		world.setStepSpeed(world.getStepSpeed() + 0.25f);
	}

	public void decreasePhysicsStepSpeed(){
		if(world.getStepSpeed() > 0.0f){
			world.setStepSpeed(world.getStepSpeed() - 0.25f);
		}
	}

	public Camera getCamera(){
		return cam;
	}

	public void addObject(PSObject obj){
		Objects.add(obj);
		world.AddObject(obj);
	}
}



class Floor extends PrimitiveCube{

	public Floor(Vector3 size, Matrix4 transform) {
		super(size, transform);
		// TODO Auto-generated constructor stub
	}

	@Override
	public btRigidBody getRigidBody() {
		if(rigidbody == null){
			btCollisionShape groundShape = new btStaticPlaneShape(new Vector3(0, 1, 0), 1);
			btDefaultMotionState fallMotionState = new btDefaultMotionState(worldTransform);
			btRigidBodyConstructionInfo fallRigidBodyCI = new btRigidBodyConstructionInfo(0, fallMotionState, groundShape, new Vector3(0,0,0));
			rigidbody = new btRigidBody(fallRigidBodyCI);
		}
		return rigidbody;
	}

}
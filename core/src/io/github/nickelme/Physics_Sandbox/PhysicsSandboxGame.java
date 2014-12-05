package io.github.nickelme.Physics_Sandbox;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btStaticPlaneShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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
	private AssetManager assetman;
	private Overlay overlay;
	private NetworkController netcon;
	
	@Override
	public void create () {
		netcon = new NetworkController();
		netcon.Start();
		netcon.SendAliveRequest();
		overlay = new Overlay(this);
		
		assetman = new AssetManager();
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
		floor.SetColor(Color.DARK_GRAY);
		Objects.add(floor);
		world.AddObject(floor);
		CreateCubeOfCubes();

		inplex = new InputMultiplexer();
		physin = new PhysicUserInput(this);
		

		fpcontrol = new FirstPersonCameraController(cam);
		fpcontrol.setVelocity(50.0f);

		Gdx.input.setInputProcessor(inplex);

		env = new Environment();
		env.set(new ColorAttribute(ColorAttribute.AmbientLight, new Color(0.5f,0.5f,0.5f, 1.0f)));
		env.add(new DirectionalLight().set(Color.WHITE, new Vector3(0,-90, 0)));
		
		lasttick = System.currentTimeMillis();

	    inplex.addProcessor(overlay.getStage());
	    inplex.addProcessor(physin);
	    inplex.addProcessor(fpcontrol);
	    
	    
	    
	   
	    
	}        

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width;
		cam.viewportWidth = height;
		cam.update(true);
		overlay.ScreenResized(width, height);
		
	};

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		world.Stimulate(Gdx.graphics.getDeltaTime());
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
		
		overlay.Draw();
		
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
		overlay.dispose();

	}

	public Camera getCamera(){
		return cam;
	}

	public void addObject(PSObject obj){
		Objects.add(obj);
		world.AddObject(obj);
	}
	
	public AssetManager getAssetManager(){
		return assetman;
	}
	
	public void updateSliders(){
		//rValue.
	}
	
	public PhysicsWorld getPhysicsWorld(){
		return world;
	}
	
	public PhysicUserInput getPhysicsInput(){
		return physin;
	}
	
	public void ClearWorld(){
		for(int i = 0; i<Objects.size(); i++){
			if(!(Objects.get(i) instanceof Floor)){
				world.ClearObject(Objects.get(i));
				Objects.remove(i);
				i--;
			}
		}
	}

	public void CreateCubeOfCubes() {
		for(int x = 0; x < 8; x++){
			for(int y=0; y < 8; y++){
				for(int z=0; z< 8; z++){
					PrimitiveCube cube = new PrimitiveCube(new Vector3(5,5,5), new Matrix4(new Vector3(x*10,(y*10),z*10), new Quaternion(), new Vector3(1,1,1)));
					Objects.add(cube);
					world.AddObject(cube);
					overlay.cubeCounter++;
				}
			}
		}
	}
}



class Floor extends PrimitiveCube{

	public Floor(Vector3 size, Matrix4 transform) {
		super(size, transform);
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
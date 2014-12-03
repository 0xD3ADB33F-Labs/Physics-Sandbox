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
	private SpriteBatch spriteBatch;
	private List<PSObject> Objects = new ArrayList<PSObject>();
	private PhysicsWorld world;
	private Long lasttick;
	private Environment env;
	private PhysicUserInput physin;
	private InputMultiplexer inplex;
	private Skin skin;
	private Stage stage;
	private AssetManager assetman;
	private Overlay overlay;
	
	@Override
	public void create () {
		overlay = new Overlay();
		
		assetman = new AssetManager();
		Bullet.init();
		modelBatch = new ModelBatch();
		
		
		
		spriteBatch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage();
		
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
		for(int x = 0; x < 10; x++){
			for(int y=0; y < 10; y++){
				for(int z=0; z<10; z++){
					PrimitiveCube cube = new PrimitiveCube(new Vector3(5,5,5), new Matrix4(new Vector3(x*10,(y*10),z*10), new Quaternion(), new Vector3(1,1,1)));
					Objects.add(cube);
					world.AddObject(cube);
				}
			}
		}

		inplex = new InputMultiplexer();
		physin = new PhysicUserInput(this);
		

		fpcontrol = new FirstPersonCameraController(cam);
		fpcontrol.setVelocity(50.0f);

		Gdx.input.setInputProcessor(inplex);

		env = new Environment();
		env.set(new ColorAttribute(ColorAttribute.AmbientLight, new Color(0.5f,0.5f,0.5f, 1.0f)));
		env.add(new DirectionalLight().set(Color.WHITE, new Vector3(0,-90, 0)));
		
		lasttick = System.currentTimeMillis();

	    inplex.addProcessor(stage);
	    inplex.addProcessor(physin);
	    inplex.addProcessor(fpcontrol);
	    
	    
	    stage.addActor(overlay.cameraInfo);
	    stage.addActor(overlay.decreaseButton);
	    stage.addActor(overlay.fps);
	    stage.addActor(overlay.importModel);
	    stage.addActor(overlay.increaseButton);
	    stage.addActor(overlay.resetButton);
	    stage.addActor(overlay.resetModel);
	    stage.addActor(overlay.stepSpeed);
	    stage.addActor(overlay.cLabel);
	    stage.addActor(overlay.rSlider);
	    stage.addActor(overlay.gSlider);
	    stage.addActor(overlay.bSlider);
	    stage.addActor(overlay.rVal);
	    stage.addActor(overlay.gVal);
	    stage.addActor(overlay.bVal);
	    
	    
	    
	    overlay.Draw();
	    
	    overlay.increaseButton.addListener(new ClickListener(){
        	public void clicked(InputEvent event, float x, float y){
        		world.increaseStepSpeed();
        	}
        });
	    
	    overlay.decreaseButton.addListener(new ClickListener(){
	    		public void clicked(InputEvent event, float x, float y){
	    			if (world.getStepSpeed() > 0) {;
	    				world.decreaseStepSpeed();
	    			}
	    			else {
	    				world.setStepSpeed(0f);;
	    			}
	    		};
	    });
	    
	    overlay.resetButton.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		world.resetStepSpeed();
	    		
	    	}
	    });
        
	    
	    overlay.importModel.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		int ret = overlay.fc.showOpenDialog(null);
	    		if(ret == JFileChooser.APPROVE_OPTION){
			        physin.modelToThrow = overlay.fc.getSelectedFile().getAbsolutePath();
			        
					physin.shootsphere = false;
	    		}
	    	};
	    });
	    
	    overlay.resetModel.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		physin.shootsphere = true;
	    	}
	    });
	    
	    overlay.rSlider.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Slider slider = (Slider) actor;
				float value = slider.getValue();
				
				if (value == 0){
					overlay.rVal.setText("R: " + 0);
				}
				else{
					overlay.rVal.setText("R: " + (int) value);
				}
			}
		});
	    
	    overlay.gSlider.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Slider slider = (Slider) actor;
				float value = slider.getValue();
				
				if (value == 0){
					overlay.gVal.setText("R: " + 0);
				}
				else{
					overlay.gVal.setText("R: " + (int) value);
				}
			}
		});
	    
	    overlay.bSlider.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Slider slider = (Slider) actor;
				float value = slider.getValue();
				
				if (value == 0){
					overlay.bVal.setText("R: " + 0);
				}
				else{
					overlay.bVal.setText("R: " + (int) value);
				}
			}
		});
	    
	    
	}        

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width;
		cam.viewportWidth = height;
		cam.update(true);
		stage.getViewport().update(width, height, true);
		
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
		
		spriteBatch.begin();
		stage.draw();
		
		overlay.fps.setText("FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()));
		overlay.cameraInfo.setText("X: "+ cam.position.x + "\nY: " + cam.position.y + "\nZ: " + cam.position.z);
		overlay.stepSpeed.setText("Physics step speed: " + world.getStepSpeed());
		spriteBatch.end();
		
		
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
		spriteBatch.dispose();

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
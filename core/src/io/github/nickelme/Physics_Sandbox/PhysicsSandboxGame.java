package io.github.nickelme.Physics_Sandbox;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;

public class PhysicsSandboxGame extends ApplicationAdapter {

	private FirstPersonCameraController fpcontrol;
	public PerspectiveCamera cam;
	private ModelBatch modelBatch;
	public HashMap<Long, PSObject> Objects = new HashMap<Long, PSObject>();
	private PhysicsWorld world;
	private Long lasttick;
	private Environment env;
	private PhysicUserInput physin;
	public InputMultiplexer inplex;
	private AssetManager assetman;
	private Overlay overlay;
	public Controller controllerClass;
	private DebugDrawer dDrawer;

	private Long nextID = 0l;

	public boolean iscoin = false;

	private static PhysicsSandboxGame instance;

	public boolean bDebugRender = false;

	private LeapController handController;

	private ArrayList<PSObject> addObjects = new ArrayList<PSObject>();
	private ArrayList<PSObject> removeObjects = new ArrayList<PSObject>();

	//Networker mNetman;

	@Override
	public void create () {
		float halfCubeLength = 0.5f/3f;
		instance = this;
		overlay = new Overlay(this);
		controllerClass = new Controller(this);
		//mNetman = new Networker();

		handController = new LeapController();

		assetman = new AssetManager();
		Bullet.init();
		modelBatch = new ModelBatch();


		cam = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0,0,0);
		cam.near = 0.01f;
		cam.far = 1000f;
		cam.up.x = 0.0f;
		cam.up.z = 0.0f;
		cam.update(true);

		System.out.println("Width: " + Gdx.graphics.getWidth() + "\nHeight: " + Gdx.graphics.getHeight());


		world = new PhysicsWorld();
		//world.getWorld().setGravity(new Vector3());
		Floor floor = new Floor(new Vector3(1000,2f,1000),  new Matrix4(new Vector3(0,0,0), new Quaternion(), new Vector3(1,1,1)));
		//floor.SetColor(Color.DARK_GRAY);

		addObject(floor);


		CreateCubeOfCubes();





		inplex = new InputMultiplexer();
		physin = new PhysicUserInput(this);


		fpcontrol = new FirstPersonCameraController(cam);
		fpcontrol.setVelocity(5f);

		Gdx.input.setInputProcessor(inplex);


		env = new Environment();
		env.set(new ColorAttribute(ColorAttribute.AmbientLight, new Color(0.5f,0.5f,0.5f, 1.0f)));
		env.add(new DirectionalLight().set(Color.WHITE, new Vector3(0,-45, 0)));

		lasttick = System.currentTimeMillis();

		inplex.addProcessor(overlay.getStage());
		inplex.addProcessor(physin);
		//inplex.addProcessor(physin);
		inplex.addProcessor(fpcontrol);


		dDrawer = new DebugDrawer();
		dDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);

		world.getWorld().setDebugDrawer(dDrawer);


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
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		SyncObjects();
		
		world.Stimulate(Gdx.graphics.getDeltaTime());
		for(Long key : Objects.keySet()){
			Objects.get(key).Update();
		}
		fpcontrol.update();
		cam.update();
		if (iscoin){
			Vector3 vec = new Vector3();
			for(Long key : Objects.keySet()){
				if((Objects.get(key) instanceof ModelObject)){
					ModelObject obj = (ModelObject) Objects.get(key);
					if(obj.getPath().equalsIgnoreCase("Quater/Quater.obj")){
						vec = obj.getRigidBody().getWorldTransform().getTranslation(vec);
						System.out.println(vec);
					}
				}
			}
			cam.up.x = 0.0f;
			cam.up.z = 0.0f;
			cam.lookAt(vec);
		}

		modelBatch.begin(cam);
		for(Long key : Objects.keySet()){
			modelBatch.render(Objects.get(key), env);
		}

		modelBatch.end();
		if(bDebugRender){
			dDrawer.begin(cam);
			world.getWorld().debugDrawWorld();
			dDrawer.end();
		}
		lasttick = System.currentTimeMillis();

		overlay.Draw();
		controllerClass.Nudge();
		//mNetman.NetworkUpdate();
		//System.out.println(cam.direction);
		//Explode(new Vector3(10, 10, 10), 1000, 1000);

	}

	private void SyncObjects(){
		for(PSObject obj : removeObjects){
			if(Objects.containsKey(obj.getId())){
				world.ClearObject(Objects.get(obj.getId()));
				Objects.remove(obj.getId());
			}
		}
		removeObjects.clear();

		for(PSObject obj : addObjects){
			if(obj.getId() != -1l){
				if(Objects.containsKey(obj.getId())){
					world.ClearObject(Objects.get(obj.getId()));
					Objects.remove(obj.getId());
				}
				Objects.put(obj.getId(), obj);
				world.AddObject(obj);
			}else{
				while(Objects.containsKey(nextID)){
					nextID++;
				}
				
				Objects.put(nextID, obj);
				obj.setId(nextID);
				world.AddObject(obj);
				nextID++;
			}

		}
		removeObjects.clear();
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
		overlay.dispose();

	}

	public Camera getCamera(){
		return cam;
	}

	public void addObjectAt(PSObject obj, Long id){
		obj.setId(id);
		addObjects.add(obj);
		removeObjects.remove(obj);

	}

	public void addObject(PSObject obj){
		obj.setId(-1l);
		addObjects.add(obj);
		removeObjects.remove(obj);
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
		iscoin = false;
		for(Long key : Objects.keySet()){
			if(!(Objects.get(key) instanceof Floor)){
				RemoveObject(Objects.get(key));
			}
		}
	}

	public void CreateCubeOfCubes() {
		for(int x = 0; x < 5; x++){
			for(int y=0; y < 5; y++){
				for(int z=0; z< 5; z++){
					PrimitiveCube cube = new PrimitiveCube(new Vector3(1,1,1), new Matrix4(new Vector3(x,(y + 1.5f),z), new Quaternion(), new Vector3(1,1,1)));
					addObject(cube);

				}
			}
		}
	}

	public void CreateCubePyramid(){
		for(int y = 2; y > 0; y--){
			for(int z = -y; z < y; z++){
				for(int x = -y; x < y; x++){
					PrimitiveCube cube = new PrimitiveCube(new Vector3(1f,1f,1f), new Matrix4(new Vector3(x * 1f,(1 -y) + 2.5f,z * 1f), new Quaternion(), new Vector3(1,1,1)));
					addObject(cube);

				}
			}
		}
	}


	public void Explode(Vector3 loc, float size, float force){
		for(Long key : Objects.keySet()){
			PSObject obj = Objects.get(key);
			Matrix4 objtrans = obj.getRigidBody().getWorldTransform();
			Vector3 objloc = new Vector3(0,0,0);
			objtrans.getTranslation(objloc);
			if(loc.dst(objloc) < size){
				Vector3 workvec = new Vector3(objloc);
				workvec.sub(loc);
				workvec.nor();
				workvec.scl(force);

				//System.out.println("\tAngle: " + Math.toDegrees((Math.atan2(loc.y-objloc.y, loc2d.dst(objloc2d)))));
				if(!obj.getRigidBody().isActive()){
					obj.getRigidBody().activate();
				}
				obj.getRigidBody().applyCentralForce(workvec);
			}
		}
	}

	public void FlipCoin(){
		for(Long key : Objects.keySet()){
			if((Objects.get(key) instanceof ModelObject)){
				ModelObject obj = (ModelObject) Objects.get(key);
				if(obj.getPath().equalsIgnoreCase("Quater/Quater.obj")){
					obj.getRigidBody().activate();
					float flipforce = (float) (Math.random()*500+300);
					System.out.println("Flipping with force: " + flipforce);
					obj.getRigidBody().applyImpulse(new Vector3(0, flipforce, 0), new Vector3(5,0,0));
				}
			}
		}
	}

	public void RemoveObject(PSObject obj){
		removeObjects.add(obj);
		addObjects.remove(obj);
	}

	public static PhysicsSandboxGame getInstance(){
		return instance;
	}

	public PSObject getObjectAt(int i){
		return Objects.get(i);
	}

	public int getNumberOfObjects(){
		return Objects.size();
	}

	public void CreateCoinFlip(){
		ModelObject obj = new ModelObject("Quater/Quater.obj", new Matrix4(new Vector3(0,30,0), new Quaternion(), new Vector3(1.0f, 1.0f, 1.0f)), false);
		addObject(obj);
		iscoin = true;
	}

	public void CreateBowlingAlley(){
		for(int i = 1; i<10; i++){
			for(int j = 0; j<i; j++){
				ModelObject obj = new ModelObject("BowlingPin/Bowling Pin.obj", new Matrix4(new Vector3(i*30.0f, 0.0f, (j*30.0f)-((i*30.0f)/2)), new Quaternion(), new Vector3(1.0f, 1.0f, 1.0f)), false);
				addObject(obj);
			}
		}
	}

	public void CustomCubeOfCubes(int a, int b, int c){
		for(int x = 0; x < a; x++){
			for(int y=0; y < b; y++){
				for(int z=0; z< c; z++){
					PrimitiveCube cube = new PrimitiveCube(new Vector3(1,1,1), new Matrix4(new Vector3(x , y + 1.5f, z), new Quaternion(), new Vector3(1,1,1)));
					addObject(cube);
				}
			}
		}
	}



	public void CustomPyramidOfCubes(int a){
		for(int y = a; y > 0; y--){
			for(int z = -y; z < y; z++){
				for(int x = -y; x < y; x++){
					PrimitiveCube cube = new PrimitiveCube(new Vector3(1,1,1), new Matrix4(new Vector3(x*1,((a-1)-y)+ 2.5f,z*1), new Quaternion(), new Vector3(1,1,1)));
					addObject(cube);
				}
			}
		}
	}
	
	public void CarTest(){
		Vehicle veh = new Vehicle(new Vector3(10,2,2), new Matrix4(new Vector3(0,10,0), new Quaternion(), new Vector3(1.0f, 1.0f, 1.0f)));
		addObject(veh);
	}

	public void chessGame(){
		ModelObject obj = new ModelObject("ChessGame/table.obj", new Matrix4(new Vector3(0,0,0), new Quaternion(), new Vector3(1.0f, 1.0f, 1.0f)), false);
		addObject(obj);
		obj.rigidbody.setFriction(1f);
		obj.rigidbody.setMassProps(27.2155422f*673.0f, new Vector3(0,0,0));
		for(int y = 2; y > 0; y--){
			for(int z = -y; z < y; z++){
				for(int x = -y; x < y; x++){
					PrimitiveCube cube = new PrimitiveCube(new Vector3(1f,1f,1f), new Matrix4(new Vector3(x*1f,((5-y) + 6)*1,z*1), new Quaternion(), new Vector3(1,1,1)));
					addObject(cube);

				}
			}
		}
	}

	public void resetCamera(){
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0,0,0);
		cam.near = 0.01f;
		cam.far = 10000.0f;
		cam.up.x = 0.0f;
		cam.up.z = 0.0f;
		cam.update(true);
	}
}



class Floor extends PrimitiveCube{

	public Floor(Vector3 size, Matrix4 transform) {
		super(size, transform);
		Texture tex = new Texture("Textures/dirt.png");
		tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		ModelBuilder modelBuilder = new ModelBuilder();

		rendercube = modelBuilder.createBox(boxExtent.x, boxExtent.y, boxExtent.z, 
				new Material(
						//ColorAttribute.createDiffuse(Color.GREEN),
						TextureAttribute.createDiffuse(tex)),
						Usage.Position | Usage.TextureCoordinates);
		for(int i = 0; i<rendercube.meshParts.size; i++){
			Mesh curmesh = rendercube.meshParts.get(i).mesh;
			int vertsize = curmesh.getVertexSize();
			for(int j = 0; j<curmesh.getNumVertices(); j++){
				float[] floats = new float[vertsize/4];
				curmesh.getVertices(j * (vertsize/4),floats);
				floats[3] *= 100;
				floats[4] *= 100;
				curmesh.updateVertices(j * (vertsize/4),floats);
			}
		}

		instance = new ModelInstance(rendercube);
	}

	@Override
	public btRigidBody getRigidBody() {
		if(rigidbody == null){
			btCollisionShape groundShape = new btBvhTriangleMeshShape(rendercube.meshParts, true);
			btDefaultMotionState fallMotionState = new btDefaultMotionState(worldTransform);//new Matrix4(new Quaternion(0,0,0,1)));
			Vector3 fallInertia = new Vector3(0, 0, 0);
			groundShape.calculateLocalInertia(0, fallInertia);
			btRigidBodyConstructionInfo fallRigidBodyCI = new btRigidBodyConstructionInfo(0, fallMotionState, groundShape, fallInertia);
			rigidbody = new btRigidBody(fallRigidBodyCI);
			rigidbody.setRestitution(0.5f);
			rigidbody.setFriction(1.0f);
		}
		return rigidbody;
	}
	
	@Override
	public void Update() {
		worldTransform.set(instance.transform);
	}

}
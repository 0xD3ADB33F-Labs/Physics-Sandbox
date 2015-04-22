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
	private PerspectiveCamera cam;
	private ModelBatch modelBatch;
	private PhysicsWorld world;
	@SuppressWarnings("unused")
	private Long lasttick;
	private Environment env;
	private PhysicUserInput physin;
	private InputMultiplexer inplex;
	private AssetManager assetman;
	private Overlay overlay;
	private Controller controllerClass;
	private DebugDrawer dDrawer;
	private Vehicle vehicle;
	private Structures structures;
	private  HashMap<Long, PSObject> Objects = new HashMap<Long, PSObject>();
	
	private Long nextID = 0l;

	public boolean iscoin = false;

	private static PhysicsSandboxGame instance;

	public boolean bDebugRender = false;

	@SuppressWarnings("unused")
	private LeapController handController;

	private ArrayList<PSObject> addObjects = new ArrayList<PSObject>();
	private ArrayList<PSObject> removeObjects = new ArrayList<PSObject>();
	
	boolean carMode;
	public float vehicleSpeeed;
	public int width;
	public int height;
	//Networker mNetman;

	@Override
	public void create () {
		instance = this;
		setOverlay(new Overlay(this));
		controllerClass = new Controller(this);
		setStructures(new Structures(this));
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		//mNetman = new Networker();

		//handController = new LeapController();

		assetman = new AssetManager();
		Bullet.init();
		modelBatch = new ModelBatch();

		setCam(new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		getCam().position.set(10f, 10f, 10f);
		getCam().lookAt(0,0,0);
		getCam().near = 0.01f;
		getCam().far = 1000f;
		getCam().up.x = 0.0f;
		getCam().up.z = 0.0f;
		getCam().update(true);

		world = new PhysicsWorld();
		Floor floor = new Floor(new Vector3(1000,2f,1000),  new Matrix4(new Vector3(0,0,0), new Quaternion(), new Vector3(1,1,1)));
		addObject(floor);

		getStructures().createSimpleStructure(structures.DEFAULT_CUBE);
		
		
		
		inplex = new InputMultiplexer();
		physin = new PhysicUserInput(this);

		fpcontrol = new FirstPersonCameraController(getCam());
		fpcontrol.setVelocity(5f);

		Gdx.input.setInputProcessor(inplex);

		env = new Environment();
		env.set(new ColorAttribute(ColorAttribute.AmbientLight, new Color(0.5f,0.5f,0.5f, 1.0f)));
		env.add(new DirectionalLight().set(Color.WHITE, new Vector3(0,-45, 0)));

		lasttick = System.currentTimeMillis();

		inplex.addProcessor(getOverlay().getStage());
		inplex.addProcessor(physin);
		inplex.addProcessor(fpcontrol);

		dDrawer = new DebugDrawer();
		dDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);

		world.getWorld().setDebugDrawer(dDrawer);
		controllerClass.assignController();
	}        

	@Override
	public void resize(int width, int height) {
		getCam().viewportWidth = width;
		getCam().viewportWidth = height;
		getCam().update(true);
		getOverlay().ScreenResized(width, height);
	};

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		SyncObjects();
		
		world.Stimulate(Gdx.graphics.getDeltaTime());
		for(Long key : getObjects().keySet()){
			getObjects().get(key).Update();
		}
		fpcontrol.update();
		getCam().update();
		if (iscoin){
			Vector3 vec = new Vector3();
			for(Long key : getObjects().keySet()){
				if((getObjects().get(key) instanceof ModelObject)){
					ModelObject obj = (ModelObject) getObjects().get(key);
					if(obj.getPath().equalsIgnoreCase("Quater/Quater.obj")){
						vec = obj.getRigidBody().getWorldTransform().getTranslation(vec);
					}
				}
			}
			getCam().up.x = 0.0f;
			getCam().up.z = 0.0f;
			getCam().lookAt(vec);
			
		}
		modelBatch.begin(getCam());
		
		for(Long key : getObjects().keySet()){
			modelBatch.render(getObjects().get(key), env);
		}

		modelBatch.end();
		if(bDebugRender){
			dDrawer.begin(getCam());
			world.getWorld().debugDrawWorld();
			dDrawer.end();
		}
		lasttick = System.currentTimeMillis();

		getOverlay().Draw();
		controllerClass.Nudge();
	}

	private void SyncObjects(){
		for(PSObject obj : removeObjects){
			if(getObjects().containsKey(obj.getId())){
				world.ClearObject(getObjects().get(obj.getId()));
				getObjects().remove(obj.getId());
			}
		}
		removeObjects.clear();

		for(PSObject obj : addObjects){
			if(obj.getId() != -1l){
				if(getObjects().containsKey(obj.getId())){
					world.ClearObject(getObjects().get(obj.getId()));
					getObjects().remove(obj.getId());
				}
				getObjects().put(obj.getId(), obj);
				world.AddObject(obj);
			}else{
				while(getObjects().containsKey(nextID)){
					nextID++;
				}
				
				getObjects().put(nextID, obj);
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
		getOverlay().dispose();

	}

	public Camera getCamera(){
		return getCam();
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

	public PhysicsWorld getPhysicsWorld(){
		return world;
	}

	public PhysicUserInput getPhysicsInput(){
		return physin;
	}

	public void RemoveObject(PSObject obj){
		removeObjects.add(obj);
		addObjects.remove(obj);
	}

	public static PhysicsSandboxGame getInstance(){
		return instance;
	}

	public PSObject getObjectAt(int i){
		return getObjects().get(i);
	}

	public int getNumberOfObjects(){
		return getObjects().size();
	}


	public PerspectiveCamera getCam() {
		return cam;
	}

	public void setCam(PerspectiveCamera cam) {
		this.cam = cam;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public HashMap<Long, PSObject> getObjects() {
		return Objects;
	}

	public void setObjects(HashMap<Long, PSObject> objects) {
		Objects = objects;
	}

	public Overlay getOverlay() {
		return overlay;
	}

	public void setOverlay(Overlay overlay) {
		this.overlay = overlay;
	}

	public Structures getStructures() {
		return structures;
	}

	public void setStructures(Structures structures) {
		this.structures = structures;
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
			btDefaultMotionState fallMotionState = new btDefaultMotionState(worldTransform);
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
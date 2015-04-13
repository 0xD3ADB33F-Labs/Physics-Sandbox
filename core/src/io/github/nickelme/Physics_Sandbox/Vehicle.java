package io.github.nickelme.Physics_Sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
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
import com.badlogic.gdx.physics.bullet.collision.btActivatingCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDefaultVehicleRaycaster;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle.btVehicleTuning;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btVehicleRaycaster;
import com.badlogic.gdx.physics.bullet.dynamics.btWheelInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Vehicle extends PSObject implements InputProcessor{

	btVehicleTuning vehTune = new btVehicleTuning();
	btDefaultVehicleRaycaster vehRay;
	btRaycastVehicle veh;

	private static final Vector3 wheelDirectionCS0 = new Vector3(0,-1,0);
	private static final Vector3 wheelAxleCS = new Vector3(0,0,-1);

	protected Model rendercube;
	protected ModelInstance instance;
	protected btRigidBody rigidbody;
	protected Matrix4 worldTransform;
	protected Vector3 boxExtent;

	protected float density;

	private static float gEngineForce = 0.f;
	private static float gBreakingForce = 0.f;

	private static float maxEngineForce = 1000.f;//this should be engine/velocity dependent
	private static float maxBreakingForce = 100.f;


	private static float gVehicleSteering = 0.3f;
	private static float steeringIncrement = 0.04f;
	private static float steeringClamp = 20f;
	private static float wheelRadius = 1.f;
	private static float wheelWidth = 0.8f;
	private static float wheelFriction = 0.8f;//1e30f;
	private static float suspensionStiffness = 75.f;
	private static float suspensionDamping = 0.4f;
	private static float suspensionCompression = 0.1f;
	private static float rollInfluence = 0.1f;//1.0f;

	float connectionHeight = -0.5f;
	float suspensionRestLength = 1.5f;

	private ModelInstance wheels[] = new ModelInstance[4];
	private Model wheelsm[] = new Model[4];

	public Vehicle(Vector3 size, Matrix4 transform){
		density = 750.0f;
		Color[] colors = {Color.RED,Color.ORANGE,Color.YELLOW,Color.GREEN,Color.BLUE,Color.PINK,Color.PURPLE,Color.WHITE,Color.CYAN,Color.DARK_GRAY,Color.MAGENTA,Color.MAROON,Color.NAVY,Color.OLIVE,Color.TEAL};
		boxExtent = size;
		worldTransform = transform;
		ModelBuilder modelBuilder = new ModelBuilder();
		rendercube = modelBuilder.createBox(boxExtent.x, boxExtent.y, boxExtent.z, 
				new Material(
						//ColorAttribute.createDiffuse(Color.GREEN),
						ColorAttribute.createDiffuse(colors[1 + (int)(Math.random() * ((14 - 1) + 1))])),
						Usage.Position | Usage.Normal);

		instance = new ModelInstance(rendercube);



		for(int i = 0; i < 4; i++){
			wheelsm[i] = modelBuilder.createBox(wheelRadius*2, wheelWidth, wheelRadius*2, new Material(ColorAttribute.createDiffuse(Color.BLUE)), Usage.Position | Usage.Normal);
			wheels[i] = new ModelInstance(wheelsm[i]);
		}
		//PhysicsSandboxGame.getInstance().inplex.addProcessor(2, this);
	}

	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		instance.getRenderables(renderables, pool);
		for(int i = 0; i < 4; i++){
			wheels[i].getRenderables(renderables, pool);
		}
	}

	@Override
	public btRigidBody getRigidBody() {
		Vector3 halfBoxExtent = boxExtent.cpy().scl(0.5f);
		if(rigidbody == null){
			btCollisionShape fallShape = new btBoxShape(halfBoxExtent);
			btDefaultMotionState fallMotionState = new btDefaultMotionState(worldTransform);
			float mass = (float) ((boxExtent.x * boxExtent.y * boxExtent.z)*density)*0.0254f;
			Vector3 fallInertia = new Vector3(0, 0, 0);
			fallShape.calculateLocalInertia(800, fallInertia);
			//fallShape.setMargin(0.00001f);

			btRigidBodyConstructionInfo fallRigidBodyCI = new btRigidBodyConstructionInfo(mass, fallMotionState, fallShape, fallInertia);
			rigidbody = new btRigidBody(fallRigidBodyCI);

			//rigidbody.setRestitution(0.5f);
			//rigidbody.setFriction(1f);
			//rigidbody.setSleepingThresholds(0.01f, 0.01f);
			//fallRigidBodyCI.dispose();
			// System.out.println("RenderCube: " + boxExtent.x/3 + " " + boxExtent.y/3 + " " + boxExtent.z/3 + "\nRigidBody: " + halfBoxExtent );
			vehRay = new btDefaultVehicleRaycaster(PhysicsSandboxGame.getInstance().getPhysicsWorld().getWorld());
			veh = new btRaycastVehicle(vehTune, rigidbody, vehRay);
			rigidbody.setActivationState(4);
			veh.setCoordinateSystem(0, 1, 2);

			Vector3 connectionPoint = new Vector3(halfBoxExtent.x - wheelRadius/2, connectionHeight, halfBoxExtent.z - (0.3f * wheelWidth)); 

			System.out.println("Wheel 0: " + connectionPoint);

			veh.addWheel(connectionPoint, wheelDirectionCS0, wheelAxleCS, suspensionRestLength, wheelRadius, vehTune, true);

			connectionPoint.set(halfBoxExtent.x - wheelRadius/2, connectionHeight, -halfBoxExtent.z + (0.3f * wheelWidth));

			System.out.println("Wheel 1: " + connectionPoint);

			veh.addWheel(connectionPoint, wheelDirectionCS0, wheelAxleCS, suspensionRestLength, wheelRadius, vehTune, true);

			connectionPoint.set(-halfBoxExtent.x + wheelRadius/2, connectionHeight, -halfBoxExtent.z + (0.3f * wheelWidth));

			System.out.println("Wheel 2: " + connectionPoint);

			veh.addWheel(connectionPoint, wheelDirectionCS0, wheelAxleCS, suspensionRestLength, wheelRadius, vehTune, false);

			connectionPoint.set(-halfBoxExtent.x + wheelRadius/2, connectionHeight, halfBoxExtent.z - (0.3f * wheelWidth));

			System.out.println("Wheel 3: " + connectionPoint);

			veh.addWheel(connectionPoint, wheelDirectionCS0, wheelAxleCS, suspensionRestLength, wheelRadius, vehTune, false);

			System.out.println("Wheels: " + veh.getNumWheels());

			for (int i = 0; i < veh.getNumWheels(); i++) {
				btWheelInfo wheel = veh.getWheelInfo(i);
				wheel.setSuspensionStiffness(suspensionStiffness);
				wheel.setWheelsDampingRelaxation((float) ((float)suspensionDamping*2.0f*Math.sqrt(suspensionStiffness)));
				wheel.setWheelsDampingCompression((float) (suspensionCompression*2.0f*Math.sqrt(suspensionStiffness)));
				wheel.setFrictionSlip(wheelFriction);
				wheel.setRollInfluence(rollInfluence);
				wheel.setMaxSuspensionForce(50000.f);
				//wheel.setMaxSuspensionForce(500.f);
			}


		} 

		if(veh == null){

		}

		return rigidbody;
	}

	@Override
	public void Update() {
		if(rigidbody != null){

			if(Gdx.input.isKeyPressed(Keys.UP)){
				if(gEngineForce == 0){
					gEngineForce = 1;
				}
				gEngineForce += gEngineForce *0.25;
				if(gEngineForce > maxEngineForce){
					gEngineForce = maxEngineForce;
				}
			}else{
				gEngineForce = 0;
			}

			if(Gdx.input.isKeyPressed(Keys.DOWN)){
				gBreakingForce += 10;
				if(gBreakingForce > maxBreakingForce){
					gBreakingForce = maxBreakingForce;
				}
			}else{
				gBreakingForce = 0;
			}

			if(Gdx.input.isKeyPressed(Keys.LEFT)){
				gVehicleSteering -= steeringIncrement;
				if(gVehicleSteering < -steeringClamp){
					gVehicleSteering = -steeringClamp;
				}else if(gVehicleSteering > steeringClamp){
					gVehicleSteering = steeringClamp;
				}
			}

			if(Gdx.input.isKeyPressed(Keys.RIGHT)){
				gVehicleSteering += steeringIncrement;
				if(gVehicleSteering < -steeringClamp){
					gVehicleSteering = -steeringClamp;
				}else if(gVehicleSteering > steeringClamp){
					gVehicleSteering = steeringClamp;
				}
			}

			int wheelIndex = 2;
			veh.applyEngineForce(-gEngineForce,wheelIndex);
			veh.setBrake(gBreakingForce,wheelIndex);
			wheelIndex = 3;
			veh.applyEngineForce(-gEngineForce,wheelIndex);
			veh.setBrake(gBreakingForce,wheelIndex);

			wheelIndex = 0;
			veh.setSteeringValue(gVehicleSteering,wheelIndex);
			wheelIndex = 1;
			veh.setSteeringValue(gVehicleSteering,wheelIndex);
			veh.updateVehicle(Gdx.graphics.getDeltaTime());
			for(int i = 0; i < veh.getNumWheels(); i++){
				veh.updateWheelTransform(i, true);
				btWheelInfo wheel = veh.getWheelInfo(i);
				//wheels[i].transform.set(wheel.getWorldTransform().inverse().inv());
				Matrix4 loc = veh.getWheelTransformWS(i).rotate(new Vector3(0,0,1.0f), 90);
				//Matrix4 cha = veh.getChassisWorldTransform();
				//loc.translate(0, cha.val[Matrix4.M13], 0);
				wheels[i].transform.set(loc);

			}
			rigidbody.getMotionState().getWorldTransform(worldTransform);
			instance.transform.set(veh.getChassisWorldTransform());
			Vector3 vehtarget = new Vector3();
			veh.getChassisWorldTransform().getTranslation(vehtarget);
			Camera cam = PhysicsSandboxGame.getInstance().cam;
			Vector3 camloc = new Vector3();
			camloc = cam.position.cpy();
			camloc.y = (15.0f*camloc.y + vehtarget.y + 5.f) / 16.0f;
			//#endif

			Vector3 camToObject = vehtarget.cpy();
			camToObject.sub(camloc);

			// keep distance between min and max distance
			float cameraDistance = camToObject.len();
			float correctionFactor = 0f;
			if (cameraDistance < 5.0f)
			{
				correctionFactor = 0.15f*(5.0f-cameraDistance)/cameraDistance;
			}
			if (cameraDistance > 10.f)
			{
				correctionFactor = 0.15f*(10.f-cameraDistance)/cameraDistance;
			}
			Vector3 tmp = camToObject.cpy();
			tmp.scl(correctionFactor);
			camloc.sub(tmp);


			cam.up.x = 0.0f;
			cam.up.z = 0.0f;
			PhysicsSandboxGame.getInstance().cam.lookAt(instance.transform.getTranslation(new Vector3()));
			cam.position.set(camloc);
			System.out.println("Speed: " + veh.getCurrentSpeedKmHour() + "Km/h");
		}

	}

	@Override
	public void setLocation(Vector3 location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMatrix(Matrix4 mat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVelocity(Vector3 vel) {
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

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){


		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}

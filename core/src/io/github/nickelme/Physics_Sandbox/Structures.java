package io.github.nickelme.Physics_Sandbox;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;



public class Structures {
	public final int EMPTY_WORLD = 0;
	public final int DEFAULT_CUBE = 1;
	public final int DEFAULT_PYRAMID = 2;
	public final int CUSTOM_CUBE = 3;
	public final int CUSTOM_PYRAMID = 4;
	public final int COIN_FLIP_DEMO = 5;
	public final int BOWLING_DEMO = 6;
	public final int CHESS_DEMO = 7;
	public final int VEHICLE_DEMO = 8;
	
	
	private PhysicsSandboxGame psGame;
	private int currentStructureId = 0;
	
	public Structures(PhysicsSandboxGame physicsSandboxGame) {
		psGame = physicsSandboxGame;
		
	}

	
	public void createSimpleStructure(int id){
		currentStructureId = id;
		switch (currentStructureId) {
		case 0:
			resetCamera();
			ClearWorld();
			break;
		case 1:
			resetCamera();
			ClearWorld();
			CreateCubeOfCubes();
			break;
		case 2:
			resetCamera();
			ClearWorld();
			CreateCubePyramid();
			break;
		case 5:
			resetCamera();
			ClearWorld();
			CreateCoinFlip();
			break;
		case 6:
			resetCamera();
			ClearWorld();
			CreateBowlingAlley();
			break;
		case 7:
			resetCamera();
			ClearWorld();
			chessGame();
			break;
		case 8:
			resetCamera();
			ClearWorld();
			VehicleDemo();
			break;
			
		default:
			break;
		}
	}
	
	public void createCustomStructure(int id, int x, int y, int z) {
		currentStructureId = id;
		switch (currentStructureId) {
		case 3:
			resetCamera();
			ClearWorld();
			CustomCubeOfCubes(x, y, z);
			break;
		case 4:
			resetCamera();
			ClearWorld();
			CustomPyramidOfCubes(y);
		default:
			break;
		}
	}
	
	public int getCurrentStructureId(){
		return currentStructureId;
	}
	
	public void setCurrentStructureId(int id) {
		currentStructureId = id;
	}
	
	public void ClearWorld(){
		psGame.iscoin = false;
		if (psGame.getOverlay().getSelectBox().getSelected().equalsIgnoreCase("Vehicle")){
			psGame.carMode = true;
		}else
			psGame.carMode = false;
		for(Long key : psGame.getObjects().keySet()){
			if(!(psGame.getObjects().get(key) instanceof Floor)){
				psGame.RemoveObject(psGame.getObjects().get(key));
			}
		}
	}
	
	public void resetCamera(){
			psGame.getCam().position.set(10f, 10f, 10f);
			psGame.getCam().lookAt(0.0f, 0.0f, 0.0f);
			psGame.getCam().near = 0.01f;
			psGame.getCam().far = 10000.0f;
			psGame.getCam().up.x = 0.0f;
			psGame.getCam().up.z = 0.0f;
			psGame.getCam().update(true);
	}
	
	public void prepWorld(){
		psGame.getCam().position.set(10f, 10f, 10f);
		psGame.getCam().lookAt(0,0,0);
		psGame.getCam().near = 0.01f;
		psGame.getCam().far = 10000.0f;
		psGame.getCam().up.x = 0.0f;
		psGame.getCam().up.z = 0.0f;
		psGame.getCam().update(true);
		
		psGame.iscoin = false;
		for(Long key : psGame.getObjects().keySet()){
			if(!(psGame.getObjects().get(key) instanceof Floor)){
				psGame.RemoveObject(psGame.getObjects().get(key));
			}
		}
	}
	
	public void CreateCubeOfCubes() {
		for(int x = 0; x < 5; x++){
			for(int y=0; y < 5; y++){
				for(int z=0; z< 5; z++){
					PrimitiveCube cube = new PrimitiveCube(new Vector3(1,1,1), new Matrix4(new Vector3(x,(y + 1.5f),z), new Quaternion(), new Vector3(1,1,1)));
					psGame.addObject(cube);

				}
			}
		}
	}
	
	public void CreateCubePyramid(){
		for(int y = 2; y > 0; y--){
			for(int z = -y; z < y; z++){
				for(int x = -y; x < y; x++){
					PrimitiveCube cube = new PrimitiveCube(new Vector3(1f,1f,1f), new Matrix4(new Vector3(x * 1f,(1 -y) + 2.5f,z * 1f), new Quaternion(), new Vector3(1,1,1)));
					psGame.addObject(cube);

				}
			}
		}
	}
	
	public void FlipCoin(){
		for(Long key : psGame.getObjects().keySet()){
			if((psGame.getObjects().get(key) instanceof ModelObject)){
				ModelObject obj = (ModelObject) psGame.getObjects().get(key);
				if(obj.getPath().equalsIgnoreCase("Quater/Quater.obj")){
					obj.getRigidBody().activate();
					float flipforce = (float) (Math.random()*500+300);
					System.out.println("Flipping with force: " + flipforce);
					obj.getRigidBody().applyImpulse(new Vector3(0, flipforce, 0), new Vector3(5,0,0));
				}
			}
		}
	}
	
	public void CreateBowlingAlley(){
		for(int i = 1; i<10; i++){
			for(int j = 0; j<i; j++){
				ModelObject obj = new ModelObject("BowlingPin/Bowling Pin.obj", new Matrix4(new Vector3(i*30.0f, 0.0f, (j*30.0f)-((i*30.0f)/2)), new Quaternion(), new Vector3(1.0f, 1.0f, 1.0f)), false);
				psGame.addObject(obj);
			}
		}
	}
	
	public void CustomCubeOfCubes(int a, int b, int c){
		for(int x = 0; x < a; x++){
			for(int y=0; y < b; y++){
				for(int z=0; z< c; z++){
					PrimitiveCube cube = new PrimitiveCube(new Vector3(1,1,1), new Matrix4(new Vector3(x , y + 1.5f, z), new Quaternion(), new Vector3(1,1,1)));
					psGame.addObject(cube);
				}
			}
		}
	}
	
	public void CustomPyramidOfCubes(int a){
		for(int y = a; y > 0; y--){
			for(int z = -y; z < y; z++){
				for(int x = -y; x < y; x++){
					PrimitiveCube cube = new PrimitiveCube(new Vector3(1,1,1), new Matrix4(new Vector3(x*1,((a-1)-y)+ 2.5f,z*1), new Quaternion(), new Vector3(1,1,1)));
					psGame.addObject(cube);
				}
			}
		}
	}
	
	public void VehicleDemo(){
		psGame.carMode = true;
		psGame.setVehicle(new Vehicle(new Vector3(5,2,5), new Matrix4(new Vector3(0,10,0), new Quaternion(), new Vector3(1.0f, 1.0f, 1.0f))));
		psGame.addObject(psGame.getVehicle());
		psGame.vehicleSpeeed = 0f;
	}
	
	public void CreateCoinFlip(){
		ModelObject obj = new ModelObject("Quater/Quater.obj", new Matrix4(new Vector3(0,30,0), new Quaternion(), new Vector3(1.0f, 1.0f, 1.0f)), false);
		psGame.addObject(obj);
		psGame.iscoin = true;
	}
	public void chessGame(){
		ModelObject obj = new ModelObject("ChessGame/table.obj", new Matrix4(new Vector3(0,0,0), new Quaternion(), new Vector3(1.0f, 1.0f, 1.0f)), false);
		psGame.addObject(obj);
		obj.rigidbody.setFriction(1f);
		obj.rigidbody.setMassProps(27.2155422f*673.0f, new Vector3(0,0,0));
		for(int y = 2; y > 0; y--){
			for(int z = -y; z < y; z++){
				for(int x = -y; x < y; x++){
					PrimitiveCube cube = new PrimitiveCube(new Vector3(1f,1f,1f), new Matrix4(new Vector3(x*1f,((5-y) + 6)*1,z*1), new Quaternion(), new Vector3(1,1,1)));
					psGame.addObject(cube);

				}
			}
		}
	}
	
	public void Explode(Vector3 loc, float size, float force){
		for(Long key : psGame.getObjects().keySet()){
			PSObject obj = psGame.getObjects().get(key);
			Matrix4 objtrans = obj.getRigidBody().getWorldTransform();
			Vector3 objloc = new Vector3(0,0,0);
			objtrans.getTranslation(objloc);
			if(loc.dst(objloc) < size){
				Vector3 workvec = new Vector3(objloc);
				workvec.sub(loc);
				workvec.nor();
				workvec.scl(force);

				if(!obj.getRigidBody().isActive()){
					obj.getRigidBody().activate();
				}
				obj.getRigidBody().applyCentralForce(workvec);
			}
		}
	}
	
	
	
}

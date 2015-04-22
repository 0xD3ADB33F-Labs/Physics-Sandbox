package io.github.nickelme.Physics_Sandbox;


import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Overlay{
	private PhysicsSandboxGame psGame;
	private JFileChooser fc = new JFileChooser(".");
	private Skin skin;
	private Stage stage;
	private SpriteBatch spriteBatch;
    private TextButton resetButton;
    private TextButton importModel;
    private TextButton resetModel;
    private TextButton resetWorld;
    private TextButton debugRender;
    private TextButton customCube, customPyramid;
    private TextButton flipCoin;
    private Label fps;
    private Label cameraInfo;
    private Label stepSpeed;
    private Label cLabel;
    private Label cCubeDims,baseLab;
    private Label controllerInfo;
    private Slider physSlider;
    private String fName;
    private SelectBox<String> selectBox;
    private TextField dimX,dimY,dimZ,baseSize;
    private Label cubeCounter;
    private Structures structures;
    public Overlay(PhysicsSandboxGame curGame){
    	psGame = curGame;
    	structures = new Structures(curGame);
    	fName = "PrimitiveSphere";
    	
		spriteBatch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage();
	    resetButton = new TextButton("Reset Step Speed", skin, "default");;
	    importModel = new TextButton("Import Wavefront Model", skin, "default");
	    resetModel = new TextButton("Reset Model to Sphere", skin, "default");
	    resetWorld = new TextButton("Reset World", skin, "default");
	    debugRender = new TextButton("Toggle Debug Render", skin, "default");
	    customCube = new TextButton("Create", skin, "default");
	    customPyramid = new TextButton("Create", skin, "default");
	    flipCoin = new TextButton("Flip Coin", skin, "default");
	    controllerInfo = new Label("DualShock 4 Wireless Controller: Connected", skin, "default");
	    cubeCounter = new Label("Number of objects: ", skin, "default");
	    fps = new Label("FPS: " + Gdx.graphics.getFramesPerSecond(), skin, "default");
	    cameraInfo = new Label("", skin);
	    stepSpeed = new Label("Physics step speed: 1", skin, "default");
	    cLabel = new Label("Camera position", skin, "default");
	    cCubeDims = new Label("Enter Dimensions:\n\nX:\n\n\nY:\n\n\nZ:", skin, "default");
	    dimX = new TextField("", skin);
	    dimY = new TextField("", skin);
	    dimZ = new TextField("", skin);
	    baseSize = new TextField("", skin);
	    baseLab = new Label("Number of Levels", skin);
	   
	    physSlider = new Slider(-10f, 10f, 0.25f, false, skin); 
    	
    	
    	float width = Gdx.graphics.getWidth();
    	float height = Gdx.graphics.getHeight();

        resetButton.setWidth(150f);
        resetButton.setHeight(30f);
        
        resetButton.setPosition(15f, height / 40.0f);
        
        importModel.setWidth(185f);
        importModel.setHeight(30f);
        importModel.setPosition(875f, 20f);
        
        resetModel.setWidth(175f);
        resetModel.setHeight(30f);
        resetModel.setPosition(1090f, 20.0f);
        
        fps.setPosition(width - (width * 0.05f) - 5f, height - (height / 20f) );
        
        cameraInfo.setPosition(width - (width * 0.99f), height - (height / 10f) - 20f);
        
        cubeCounter.setPosition(width - (width * 0.99f), height - (height / 10f) - 110f);
        
        cLabel.setPosition(width - (width * 0.99f), height - (height / 20f));
        
        stepSpeed.setPosition(width - (width * 0.99f), 80f);
        
        
        physSlider.setPosition(15f, 60f);
        physSlider.setRange(-10f, 10f);
        physSlider.setValue(1f);
        
        resetWorld.setWidth(175f);
        resetWorld.setHeight(30f);
        resetWorld.setPosition(1090f, 70f);
        
        debugRender.setWidth(185f);
        debugRender.setHeight(30f);
        debugRender.setPosition(875f, 70f);
        
        setSelectBox(new SelectBox<String>(skin, "default"));
        getSelectBox().setItems(new String[] {"5x5 Cube", "Pyramid", "Bowling Alley", "Custom Cube", "Custom Pyramid", "Coin Flip", "Chess Game", "Vehicle"});
        getSelectBox().setX(400f);
        getSelectBox().setY(500f);
        getSelectBox().setWidth(150f);
        getSelectBox().setPosition(width - (width * 0.99f), height - (height / 5f) - 100f);
        
        customCube.setWidth(100f);
        customCube.setHeight(30f);
        customCube.setPosition(25f, 215f);
        customCube.setVisible(false);
        
        cCubeDims.setPosition(25f, 250f);
        cCubeDims.setVisible(false);
        
        controllerInfo.setPosition(500f, 18f);
        controllerInfo.setVisible(false);
        
        dimX.setVisible(false);
        dimX.setPosition(50f, 370f);
        dimX.setHeight(25f);
        dimX.setWidth(50f);
        
        dimY.setVisible(false);
        dimY.setPosition(50f, 310f);
        dimY.setHeight(25f);
        dimY.setWidth(50f);
        
        dimZ.setVisible(false);
        dimZ.setPosition(50f, 250f);
        dimZ.setHeight(25f);
        dimZ.setWidth(50f);
        
        dimX.setMessageText("X");
        dimY.setMessageText("Y");
        dimZ.setMessageText("Z");
        
        baseLab.setPosition(18f, 425f);
        baseLab.setVisible(false);
        
        baseSize.setPosition(18f, 380f);
        baseSize.setVisible(false);
        baseSize.setMessageText("Levels");
        
        customPyramid.setPosition(18f, 325f);
        customPyramid.setWidth(100f);
        customPyramid.setHeight(30f);
        customPyramid.setVisible(false);
        
	    flipCoin.setPosition(20f, 425f);
	    flipCoin.setVisible(false);
	    flipCoin.setWidth(100f);
	    flipCoin.setHeight(30f);
        
	    resetButton.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		physSlider.setValue(1f);
	    	}
	    });
        
	    
	    importModel.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		int ret = fc.showOpenDialog(null);
	    		if(ret == JFileChooser.APPROVE_OPTION){
	    			psGame.getPhysicsInput().modelToThrow = fc.getSelectedFile().getAbsolutePath();
			        
	    			psGame.getPhysicsInput().shootsphere = false;
	    			
	    			fName = fc.getSelectedFile().getName();
	    		}
	    	};
	    });
	    
	    resetModel.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		psGame.getPhysicsInput().shootsphere = true;
	    		fName = "PrimitiveSphere";
	    	}
	    });
	    
	    physSlider.addListener(new ChangeListener(){
	    	public void changed(ChangeEvent event, Actor actor){
	    		Slider slider = (Slider) actor;
	    		float value = slider.getValue();
	    		
	    		if (value == 1){
	    			stepSpeed.setText("Physics step speed: 1");
	    			psGame.getPhysicsWorld().setStepSpeed(1.0f);
	    		}else{
	    			stepSpeed.setText("Physics step speed: " + physSlider.getValue());
	    			psGame.getPhysicsWorld().setStepSpeed(physSlider.getValue());
	    		}
	    	}
	    });
	    
	    baseSize.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		enablePyramidVars();
	    	}
	    });
	    
	    dimX.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		enableCubeVars();
	    	}
	    });
	    
	    dimY.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		enableCubeVars();
	    	}
	    });
	    
	    dimZ.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		enableCubeVars();
	    	}
	    });
	    
	    resetWorld.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		if (structures.getCurrentStructureId() == structures.EMPTY_WORLD) {
	    			structures.createSimpleStructure(structures.EMPTY_WORLD);
	    		}else if (structures.getCurrentStructureId() == structures.DEFAULT_CUBE) {
	    			structures.createSimpleStructure(structures.DEFAULT_CUBE);
	    		}else if (structures.getCurrentStructureId() == structures.DEFAULT_PYRAMID) {
	    			structures.createSimpleStructure(structures.DEFAULT_PYRAMID);
	    		}else if (structures.getCurrentStructureId() == structures.CUSTOM_CUBE) {
	    			structures.createCustomStructure(structures.getCurrentStructureId(), Integer.parseInt(dimX.getText()), Integer.parseInt(dimY.getText()), Integer.parseInt(dimZ.getText()));
	    			showCubeVars();
	    			disableCubeVars();
	    		}else if (structures.getCurrentStructureId() == structures.CUSTOM_PYRAMID) {
	    			structures.createCustomStructure(structures.getCurrentStructureId(), 0, Integer.parseInt(baseSize.getText()), 0);
	    			showPyramidVars();
	    			disablePyramidVars();
	    		}else if (structures.getCurrentStructureId() == structures.COIN_FLIP_DEMO) {
	    			structures.createSimpleStructure(structures.getCurrentStructureId());
	    		}else if (structures.getCurrentStructureId() ==  structures.BOWLING_DEMO) {
	    			structures.createSimpleStructure(structures.getCurrentStructureId());
	    		}else if (structures.getCurrentStructureId() == structures.CHESS_DEMO) {
	    			structures.createSimpleStructure(structures.getCurrentStructureId());
	    		}else if (structures.getCurrentStructureId() == structures.VEHICLE_DEMO) {
	    			structures.createSimpleStructure(structures.getCurrentStructureId());
	    		}
	    	}
	    });
	    debugRender.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		psGame.bDebugRender = !psGame.bDebugRender;
	    	}
	    });
	    
	    getSelectBox().addListener(new ChangeListener(){
			public void changed(ChangeEvent arg0, Actor arg1) {
				if(getSelectBox().getSelected().equalsIgnoreCase("5x5 Cube")){
					structures.setCurrentStructureId(structures.DEFAULT_CUBE);
					hideMiscVars(structures.getCurrentStructureId());
					structures.createSimpleStructure(structures.getCurrentStructureId());
				}else if(getSelectBox().getSelected().equalsIgnoreCase("Pyramid")){
					structures.setCurrentStructureId(structures.DEFAULT_PYRAMID);
					hideMiscVars(structures.getCurrentStructureId());
					structures.createSimpleStructure(structures.getCurrentStructureId());
				}else if(getSelectBox().getSelected().equalsIgnoreCase("Bowling Alley")){
					structures.setCurrentStructureId(structures.BOWLING_DEMO);
					hideMiscVars(structures.getCurrentStructureId());
					structures.createSimpleStructure(structures.getCurrentStructureId());
				}else if(getSelectBox().getSelected().equalsIgnoreCase("Custom Cube")){
					structures.setCurrentStructureId(structures.CUSTOM_CUBE);
					hideMiscVars(structures.getCurrentStructureId());
				}else if(getSelectBox().getSelected().equalsIgnoreCase("Custom Pyramid")){
					structures.setCurrentStructureId(structures.CUSTOM_PYRAMID);
					hideMiscVars(structures.getCurrentStructureId());
				}else if(getSelectBox().getSelected().equalsIgnoreCase("Coin Flip")){
					structures.setCurrentStructureId(structures.COIN_FLIP_DEMO);
					hideMiscVars(structures.getCurrentStructureId());
					structures.createSimpleStructure(structures.getCurrentStructureId());					
				}else if(getSelectBox().getSelected().equalsIgnoreCase("Chess Game")){
					structures.setCurrentStructureId(structures.CHESS_DEMO);
					hideMiscVars(structures.getCurrentStructureId());
					structures.createSimpleStructure(structures.getCurrentStructureId());
				}else if (getSelectBox().getSelected().equalsIgnoreCase("Vehicle")){
					structures.setCurrentStructureId(structures.VEHICLE_DEMO);
					hideMiscVars(structures.getCurrentStructureId());
					structures.createSimpleStructure(structures.getCurrentStructureId());
				}
			}
	    });
	    
	    customCube.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		structures.createCustomStructure(structures.getCurrentStructureId(), Integer.parseInt(dimX.getText()), Integer.parseInt(dimY.getText()), Integer.parseInt(dimZ.getText()));
	    		disableCubeVars();
	    	}
	    });
	    
	    customPyramid.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		structures.createCustomStructure(structures.getCurrentStructureId(), 0, Integer.parseInt(baseSize.getText()), 0);
	    		disablePyramidVars();
				
	    	}
	    });
	    
	    flipCoin.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		structures.FlipCoin();
	    	}
	    });
	    stage.addActor(cameraInfo);
	    stage.addActor(fps);
	    stage.addActor(importModel);
	    stage.addActor(resetButton);
	    stage.addActor(resetModel);
	    stage.addActor(stepSpeed);
	    stage.addActor(cLabel);
	    stage.addActor(resetWorld);
	    stage.addActor(cubeCounter);
	    stage.addActor(debugRender);
	    stage.addActor(getSelectBox());
	    stage.addActor(customCube);
	    stage.addActor(cCubeDims);
	    stage.addActor(dimX);
	    stage.addActor(dimY);
	    stage.addActor(dimZ);
	    stage.addActor(baseSize);
	    stage.addActor(baseLab);
	    stage.addActor(customPyramid);
	    stage.addActor(physSlider);
	    stage.addActor(flipCoin);
	    stage.addActor(controllerInfo);
    }
    
    public void Draw(){
    	
    	Camera cam = psGame.getCamera();
		fps.setText("FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()));
		if (psGame.carMode) {
			cameraInfo.setText("X: "+ String.format("%.01f", cam.position.x) + "\nY: " + String.format("%.01f", cam.position.y) + "\nZ: " + String.format("%.01f", cam.position.z) + "\n\nCurrent projectile: " + fName + "\nVehicle Speed: " + String.format("%.0f", Math.abs(psGame.getVehicle().veh.getCurrentSpeedKmHour())) + " KM/H");
		}else
		cameraInfo.setText("X: "+ String.format("%.01f", cam.position.x) + "\nY: " + String.format("%.01f", cam.position.y) + "\nZ: " + String.format("%.01f", cam.position.z) + "\n\nCurrent projectile: " + fName);
		if (psGame.carMode){
			cubeCounter.setVisible(false);
		}
		
		if (psGame.bDebugRender == true && !psGame.carMode){
			cubeCounter.setText("Number of objects: " + (psGame.getNumberOfObjects() - 1)  + "\n\nDebug Render: True");
		}else if (psGame.bDebugRender == false && !psGame.carMode){
			cubeCounter.setText("Number of objects: " + (psGame.getNumberOfObjects() - 1) + "\n\nDebug Render: False");
		}
		spriteBatch.begin();
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		spriteBatch.end();
		
		

    }
     
    
    public void ScreenResized(int width, int height){
    	stage.getViewport().update(width, height, true);
    }
     
    
    public void dispose(){
    	spriteBatch.dispose();
    }
    
    public void hideMiscVars(int structure) {
    	if (structure == structures.DEFAULT_CUBE) {
    		flipCoin.setVisible(false);
			hideCubeVars();
			hidePyramidVars();
    	}else if (structure == structures.DEFAULT_PYRAMID) {
    		flipCoin.setVisible(false);
			hidePyramidVars();
			hideCubeVars();
    	}else if (structure == structures.CUSTOM_CUBE) {
    		System.out.println("Check 1");
    		flipCoin.setVisible(false);
			hidePyramidVars();
			showCubeVars();
    	}else if (structure == structures.CUSTOM_PYRAMID) {
    		flipCoin.setVisible(false);
			hideCubeVars();
			showPyramidVars();
    	}else if (structure == structures.COIN_FLIP_DEMO) { 
    		flipCoin.setVisible(true);
    		hideCubeVars();
    		hidePyramidVars();
    	}else if (structure == structures.CHESS_DEMO) {
    		hidePyramidVars();
			hideCubeVars();
			flipCoin.setVisible(true);
    	}else if (structure == structures.BOWLING_DEMO) {
    		flipCoin.setVisible(false);
			hidePyramidVars();
			hideCubeVars();
    	}else if (structure == structures.CHESS_DEMO) {
    		hidePyramidVars();
			hideCubeVars();
			flipCoin.setVisible(false);
    	}else if (structure == structures.VEHICLE_DEMO) {
    		hideCubeVars();
			hidePyramidVars();
			flipCoin.setVisible(false);
    	}
    }
    
    public void hidePyramidVars(){
    	System.out.println("Check 2");
    	baseSize.setVisible(false);
		baseLab.setVisible(false);
		baseSize.setDisabled(true);
		customPyramid.setVisible(false);
    }
    
    public void hideCubeVars(){
    	dimX.setVisible(false);
		dimY.setVisible(false);
		dimZ.setVisible(false);
		cCubeDims.setVisible(false);
		customCube.setVisible(false);
		dimX.setDisabled(true);
		dimY.setDisabled(true);
		dimZ.setDisabled(true);
    }
    
    public void disableCubeVars(){
    	dimX.setDisabled(true);
    	dimY.setDisabled(true);
    	dimZ.setDisabled(true);
    }
    
    public void disablePyramidVars(){
    	baseSize.setDisabled(true);
    }
    
    public void enableCubeVars(){
    	dimX.setDisabled(false);
    	dimY.setDisabled(false);
    	dimZ.setDisabled(false);
    }
    
    public void enablePyramidVars(){
    	baseSize.setDisabled(false);
    }
    
    public void showPyramidVars(){
    	baseSize.setVisible(true);
		baseLab.setVisible(true);
		baseSize.setDisabled(false);
		customPyramid.setVisible(true);
    }
    
    public void showCubeVars(){
    	System.out.println("Check 3");
    	dimX.setVisible(true);
		dimY.setVisible(true);
		dimZ.setVisible(true);
		cCubeDims.setVisible(true);
		customCube.setVisible(true);
		dimX.setDisabled(false);
		dimY.setDisabled(false);
		dimZ.setDisabled(false);
    }
    
    public Stage getStage(){
    	return stage;
    }

	public SelectBox<String> getSelectBox() {
		return selectBox;
	}

	public void setSelectBox(SelectBox<String> selectBox) {
		this.selectBox = selectBox;
	}
    
    
}
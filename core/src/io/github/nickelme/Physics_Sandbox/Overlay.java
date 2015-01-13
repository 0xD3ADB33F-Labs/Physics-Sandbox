package io.github.nickelme.Physics_Sandbox;


import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
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

	JFileChooser fc = new JFileChooser(".");
	boolean shootsphere = true;
	private Skin skin;
	private Stage stage;
	private SpriteBatch spriteBatch;
    
    TextButton increaseButton;
    TextButton decreaseButton;
    TextButton resetButton;
    TextButton importModel;
    TextButton resetModel;
    TextButton resetWorld;
    TextButton debugRender;
    TextButton customCube, customPyramid;
    TextButton flipCoin;
    
    
    Label fps;
    Label cameraInfo;
    Label stepSpeed;
    Label cLabel;
    Label rVal;
    Label gVal;
    Label bVal;
    Label cubeCount;
    Label cCubeDims,baseLab;
    Label controllerInfo;
    
    
    Slider rSlider;
    Slider gSlider;
    Slider bSlider;
    Slider physSlider;
    String fName;
    BitmapFont font;
    SelectBox selectBox;
    
    final TextField dimX,dimY,dimZ,baseSize;
    
    int cubeCounter;
    
    public Overlay(PhysicsSandboxGame curGame){
    	psGame = curGame;
    	fName = "PrimitiveSphere";
 
		spriteBatch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage();
		
		
	    increaseButton = new TextButton("Step Speed +", skin, "default");
	    decreaseButton = new TextButton("Step Speed -", skin, "default");
	    resetButton = new TextButton("Reset Step Speed", skin, "default");;
	    importModel = new TextButton("Import Wavefront Model", skin, "default");
	    resetModel = new TextButton("Reset Model to Sphere", skin, "default");
	    resetWorld = new TextButton("Reset World", skin, "default");
	    debugRender = new TextButton("Toggle Debug Render", skin, "default");
	    customCube = new TextButton("Create", skin, "default");
	    customPyramid = new TextButton("Create", skin, "default");
	    flipCoin = new TextButton("Flip Coin", skin, "default");
	    controllerInfo = new Label("DualShock 4 Wireless Controller: Connected", skin, "default");
	    
	    fps = new Label("FPS: " + Gdx.graphics.getFramesPerSecond(), skin, "default");
	    cameraInfo = new Label("", skin);
	    stepSpeed = new Label("Physics step speed: 1", skin, "default");
	    cLabel = new Label("Camera position", skin, "default");
	    rVal = new Label("R: ", skin, "default");
	    gVal = new Label("G: ", skin, "default");
	    bVal = new Label("B: ", skin, "default");
	    cubeCount = new Label("Number of cubes: ", skin, "default");
	    cCubeDims = new Label("Enter Dimensions:\n\nX:\n\n\nY:\n\n\nZ:", skin, "default");
	    dimX = new TextField("", skin);
	    dimY = new TextField("", skin);
	    dimZ = new TextField("", skin);
	    baseSize = new TextField("", skin);
	    baseLab = new Label("Enter Size of The Base", skin);
	    
	    rSlider = new Slider(0,255,1,false,skin);
	    gSlider = new Slider(0,255,1,false,skin);
	    bSlider = new Slider(0,255,1,false,skin);
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
        
        cubeCount.setPosition(width - (width * 0.99f), height - (height / 10f) - 100f);
        
        cLabel.setPosition(width - (width * 0.99f), height - (height / 20f));
        
        stepSpeed.setPosition(width - (width * 0.99f), 80f);
        
        rSlider.setVisible(true);
        rSlider.setPosition(15f, 85f);
        rSlider.setRange(0f, 255f);
        rVal.setPosition(160f, 85f);
       // rSlider.setValue(0f);
        
        gSlider.setVisible(true);
        gSlider.setPosition(15f, 65f);
        gSlider.setRange(0f, 255f);
        gVal.setPosition(160f, 65f);
      //  gSlider.setValue(255f);
        
        bSlider.setVisible(true);
        bSlider.setPosition(15f, 45f);
        bSlider.setRange(0f, 255f);
        bVal.setPosition(160f, 45f);
       // bSlider.setValue(255f);\
        
        physSlider.setPosition(15f, 60f);
        physSlider.setRange(-10f, 10f);
        physSlider.setValue(1f);
        
        resetWorld.setWidth(175f);
        resetWorld.setHeight(30f);
        resetWorld.setPosition(1090f, 70f);
        
        debugRender.setWidth(185f);
        debugRender.setHeight(30f);
        debugRender.setPosition(875f, 70f);
        
        selectBox = new SelectBox(skin, "default");
        selectBox.setItems(new String[] {"8x8 Cube", "Pyramid", "Bowling Alley", "Custom Cube", "Custom Pyramid", "Coin Flip"});
        selectBox.setX(400f);
        selectBox.setY(500f);
        selectBox.setWidth(150f);
        selectBox.setPosition(width - (width * 0.99f), height - (height / 5f) - 100f);
        
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
        baseSize.setMessageText("Base Size");
        
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
	    
	    rSlider.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Slider slider = (Slider) actor;
				float value = slider.getValue();
				
				if (value == 0){
					rVal.setText("R: " + 0);
				}else{
					rVal.setText("R: " + (int) value);
				}
			}
		});
	    
	    gSlider.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Slider slider = (Slider) actor;
				float value = slider.getValue();
				
				if (value == 0){
					gVal.setText("R: " + 0);
				}else{
					gVal.setText("R: " + (int) value);
				}
			}
		});
	    
	    bSlider.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Slider slider = (Slider) actor;
				float value = slider.getValue();
				
				if (value == 0){
					bVal.setText("R: " + 0);
				}else{
					bVal.setText("R: " + (int) value);
				}
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
	    		if(selectBox.getSelected()=="8x8 Cube"){
	    			psGame.resetCamera();
	    			psGame.ClearWorld();
					psGame.CreateCubeOfCubes();
				}else if(selectBox.getSelected()=="Pyramid"){
					psGame.ClearWorld();
					psGame.CreateCubePyramid();
				}else if(selectBox.getSelected()=="Bowling Alley"){
					psGame.ClearWorld();
					psGame.CreateBowlingAlley();
				}else if (selectBox.getSelected()=="Custom Cube"){
					psGame.ClearWorld();
		    		psGame.CustomCubeOfCubes(Integer.parseInt(dimX.getText()), Integer.parseInt(dimY.getText()), Integer.parseInt(dimZ.getText()));
					showCubeVars();
					disableCubeVars();
				}else if (selectBox.getSelected()=="Custom Pyramid"){
					psGame.ClearWorld();
		    		psGame.CustomPyramidOfCubes(Integer.parseInt(baseSize.getText()));
		    		showPyramidVars();
		    		disablePyramidVars();
				}else if(selectBox.getSelected()=="Coin Flip"){
					psGame.ClearWorld();
					psGame.CreateCoinFlip();
					
				}
	    			cubeCounter = 0;
	    			
	    		}
	    	});
	    debugRender.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		psGame.bDebugRender = !psGame.bDebugRender;
	    	}
	    });
	    
	    selectBox.addListener(new ChangeListener(){
			public void changed(ChangeEvent arg0, Actor arg1) {
				if(selectBox.getSelected()=="8x8 Cube"){
					psGame.resetCamera();
					flipCoin.setVisible(false);
					hideCubeVars();
					hidePyramidVars();
					psGame.ClearWorld();
					psGame.CreateCubeOfCubes();
				}else if(selectBox.getSelected()=="Pyramid"){
					psGame.resetCamera();
					flipCoin.setVisible(false);
					hidePyramidVars();
					hideCubeVars();
					psGame.ClearWorld();
					psGame.CreateCubePyramid();
				}else if(selectBox.getSelected()=="Bowling Alley"){
					psGame.resetCamera();
					flipCoin.setVisible(false);
					hidePyramidVars();
					hideCubeVars();
					psGame.ClearWorld();
					psGame.CreateBowlingAlley();
				}else if(selectBox.getSelected()=="Custom Cube"){
					psGame.resetCamera();
					flipCoin.setVisible(false);
					hidePyramidVars();
					showCubeVars();
				}else if(selectBox.getSelected()=="Custom Pyramid"){
					psGame.resetCamera();
					flipCoin.setVisible(false);
					hideCubeVars();
					showPyramidVars();
				}else if(selectBox.getSelected()=="Coin Flip"){
					hidePyramidVars();
					hideCubeVars();
					flipCoin.setVisible(true);
					psGame.ClearWorld();
					psGame.CreateCoinFlip();
					
				}
			}
	    });
	    
	    customCube.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		psGame.ClearWorld();
	    		psGame.CustomCubeOfCubes(Integer.parseInt(dimX.getText()), Integer.parseInt(dimY.getText()), Integer.parseInt(dimZ.getText()));
	    		hideCubeVars();
	    		
	    		
	    	}
	    });
	    
	    customPyramid.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		psGame.ClearWorld();
	    		psGame.CustomPyramidOfCubes(Integer.parseInt(baseSize.getText()));
	    		hidePyramidVars();
				
	    	}
	    });
	    
	    flipCoin.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		psGame.FlipCoin();
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
	    stage.addActor(cubeCount);
	    stage.addActor(debugRender);
	    stage.addActor(selectBox);
	    stage.addActor(customCube);
	    stage.addActor(cCubeDims);
	    stage.addActor(dimX);
	    stage.addActor(dimY);
	    stage.addActor(dimZ);
	    stage.addActor(baseSize);
	    stage.addActor(baseLab);
	    stage.addActor(customPyramid);
	    //stage.addActor(overlay.rSlider);
	    //stage.addActor(overlay.gSlider);
	    //stage.addActor(bSlider);
	    //stage.addActor(overlay.rVal);
	    //stage.addActor(overlay.gVal);
	    //stage.addActor(bVal);
	    stage.addActor(physSlider);
	    stage.addActor(flipCoin);
	    stage.addActor(controllerInfo);
    }
    
    
    //ArrayList<Object> objects = new ArrayList<Object>();
    
    public void Draw(){
    	Camera cam = psGame.getCamera();
		fps.setText("FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()));
		cameraInfo.setText("X: "+ cam.position.x + "\nY: " + cam.position.y + "\nZ: " + cam.position.z + "\n\nCurrent projectile: " + fName);
		
		cubeCount.setText("Number of cubes: " + cubeCounter + "\n");
		
		if (psGame.bDebugRender == true){
			cubeCount.setText(/*"Number of cubes: " + cubeCounter + "*/"Debug Render: True");
		}else{
			cubeCount.setText(/*"Number of cubes: " + cubeCounter + "*/"Debug Render: False");
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
    
    public void hidePyramidVars(){
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
}
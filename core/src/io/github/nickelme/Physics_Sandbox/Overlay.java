package io.github.nickelme.Physics_Sandbox;

import javax.naming.LimitExceededException;
import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

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
    
    Label fps;
    Label cameraInfo;
    Label stepSpeed;
    Label cLabel;
    Label rVal;
    Label gVal;
    Label bVal;
    
    
    Slider rSlider;
    Slider gSlider;
    Slider bSlider;
    Slider physSlider;
    
    public Overlay(PhysicsSandboxGame curGame){
    	psGame = curGame;
    	
		spriteBatch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage();
		
	    increaseButton = new TextButton("Step speed +", skin, "default");
	    decreaseButton = new TextButton("Step speed -", skin, "default");
	    resetButton = new TextButton("Reset step speed", skin, "default");;
	    importModel = new TextButton("Import wavefront model", skin, "default");
	    resetModel = new TextButton("Reset model to sphere", skin, "default");
	    
	    fps = new Label("FPS: " + Gdx.graphics.getFramesPerSecond(), skin, "default");
	    cameraInfo = new Label("", skin);
	    stepSpeed = new Label("Physics step speed: 1", skin, "default");
	    cLabel = new Label("Camera position", skin, "default");
	    rVal = new Label("R: ", skin, "default");
	    gVal = new Label("G: ", skin, "default");
	    bVal = new Label("B: ", skin, "default");
	    
	    
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
        importModel.setPosition(width - (width / 2.0f), height / 20.0f);
        
        resetModel.setWidth(175f);
        resetModel.setHeight(30f);
        resetModel.setPosition(width - (width / 4.0f), height / 15f);
        
        fps.setPosition(width - (width * 0.05f), height - (height / 20f));
        
        cameraInfo.setPosition(width - (width * 0.99f), height - (height / 10f));;
        
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
	    		}
	    	};
	    });
	    
	    resetModel.addListener(new ClickListener(){
	    	public void clicked(InputEvent event, float x, float y){
	    		psGame.getPhysicsInput().shootsphere = true;
	    	}
	    });
	    
	    rSlider.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Slider slider = (Slider) actor;
				float value = slider.getValue();
				
				if (value == 0){
					rVal.setText("R: " + 0);
				}
				else{
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
				}
				else{
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
				}
				else{
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
	    
	    stage.addActor(cameraInfo);
	    stage.addActor(fps);
	    stage.addActor(importModel);
	    stage.addActor(resetButton);
	    stage.addActor(resetModel);
	    stage.addActor(stepSpeed);
	    stage.addActor(cLabel);
	    
	    //stage.addActor(overlay.rSlider);
	    //stage.addActor(overlay.gSlider);
	    //stage.addActor(bSlider);
	    //stage.addActor(overlay.rVal);
	    //stage.addActor(overlay.gVal);
	    //stage.addActor(bVal);
	    stage.addActor(physSlider);
    }
    
    
    //ArrayList<Object> objects = new ArrayList<Object>();
    
    public void Draw(){
    	Camera cam = psGame.getCamera();
		fps.setText("FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()));
		cameraInfo.setText("X: "+ cam.position.x + "\nY: " + cam.position.y + "\nZ: " + cam.position.z);
		
		
		spriteBatch.begin();
		stage.draw();
		spriteBatch.end();

    }
     
    
    public void ScreenResized(int width, int height){
    	stage.getViewport().update(width, height, true);
    }
     
    
    public void dispose(){
    	spriteBatch.dispose();
    }
    
    public Stage getStage(){
    	return stage;
    }
}
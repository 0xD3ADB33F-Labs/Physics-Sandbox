package io.github.nickelme.Physics_Sandbox;

import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Overlay{
	
	JFileChooser fc = new JFileChooser(".");
	boolean shootsphere = true;
    Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));;
    
    TextButton increaseButton = new TextButton("Step speed +", skin, "default");
    TextButton decreaseButton = new TextButton("Step speed -", skin, "default");
    TextButton resetButton = new TextButton("Reset step speed", skin, "default");;
    TextButton importModel = new TextButton("Import wavefront model", skin, "default");
    TextButton resetModel = new TextButton("Reset model to sphere", skin, "default");
    
    Label fps = new Label("FPS: " + Gdx.graphics.getFramesPerSecond(), skin, "default");
    Label cameraInfo = new Label("", skin);
    Label stepSpeed = new Label("Physics step speed: ", skin, "default");
    Label cLabel = new Label("Camera position", skin, "default");
    Label rVal = new Label("R: ", skin, "default");
    Label gVal = new Label("G: ", skin, "default");
    Label bVal = new Label("B: ", skin, "default");
    
    
    public Slider rSlider = new Slider(0,255,1,false,skin);
    public Slider gSlider = new Slider(0,255,1,false,skin);
    public Slider bSlider = new Slider(0,255,1,false,skin);
    
    
    //ArrayList<Object> objects = new ArrayList<Object>();
    
    public void Draw(){
    	float width = Gdx.graphics.getWidth();
    	float height = Gdx.graphics.getHeight();
    	
        increaseButton.setWidth(150f);
        increaseButton.setHeight(30f);
        increaseButton.setPosition(width - (width / 8.0f), height / 10.0f);
         
        decreaseButton.setWidth(150f);
        decreaseButton.setHeight(30f);
        decreaseButton.setPosition(width - (width / 8.0f), height / 40.0f);
         
        resetButton.setWidth(150f);
        resetButton.setHeight(30f);
        resetButton.setPosition(width - (width / 4.0f), height / 40.0f);
        
        importModel.setWidth(150f);
        importModel.setHeight(30f);
        importModel.setPosition(width - (width / 2.0f), height / 20.0f);
        
        resetModel.setWidth(175f);
        resetModel.setHeight(30f);
        resetModel.setPosition(width - (width / 4.0f), height / 15f);
        
        importModel.setWidth(190f);
        importModel.setHeight(30f);
        importModel.setPosition(width - (width / 1.80f), height / 40f); 
        
        resetModel.setWidth(175f);
        resetModel.setHeight(30f);
        resetModel.setPosition(width - (width / 2.52f), height / 40f);
        
        fps.setPosition(width - (width * 0.05f), height - (height / 20f));
        
        cameraInfo.setPosition(width - (width * 0.99f), height - (height / 10f));;
        
        cLabel.setPosition(width - (width * 0.99f), height - (height / 20f));
        
        stepSpeed.setPosition(width - (width * 0.99f), height / 40f);
        
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
       // bSlider.setValue(255f);
    }
     
     
}
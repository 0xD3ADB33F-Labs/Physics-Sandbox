package io.github.nickelme.Physics_Sandbox;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;





public class Controller extends ApplicationAdapter implements ControllerListener {
   SpriteBatch batch;
   Sprite sprite;
   BitmapFont font;
   Overlay overlay;
   boolean hasControllers = true;
   com.badlogic.gdx.controllers.Controller controller;
   
   
   private PhysicsSandboxGame psGame;
   
   
   public Controller(PhysicsSandboxGame curGame){
	   psGame = curGame;
	   Controllers.addListener(this);
	   //overlay = new Overlay(curGame);
	   
   }
   
   public void Nudge(){
	   if(Controllers.getControllers().size == 0)
       {
           hasControllers = false;
           
       }
       else{
           controller = Controllers.getControllers().first();
           
       }
	   if (hasControllers){
		   	
		   	if (controller.getAxis(DS4.AXIS_LEFT_X) > 0.2f || controller.getAxis(DS4.AXIS_LEFT_X) < -0.2f){
	   			Vector3 translate = psGame.cam.direction.cpy();
	   			translate.rotate(90.0f, 0.0f, 1.0f, 0.0f);
	   			translate.nor();
	   			translate.scl(-controller.getAxis(DS4.AXIS_LEFT_X));
	   			psGame.cam.translate(translate);
	   		}	
	   
	   		if(controller.getAxis(DS4.AXIS_LEFT_Y) > 0.2f || controller.getAxis(DS4.AXIS_LEFT_Y) <-0.2f){
	   			Vector3 translate = psGame.cam.direction.cpy();
	   			translate.nor();
	   			translate.scl(-controller.getAxis(DS4.AXIS_LEFT_Y));
		   		psGame.cam.translate(translate);
	   		}
	   
	   		if(controller.getAxis(DS4.AXIS_RIGHT_X) > 0.2f || controller.getAxis(DS4.AXIS_RIGHT_X) < -0.2f){
	   			psGame.cam.rotate(-controller.getAxis(DS4.AXIS_RIGHT_X), 0, 1, 0);
	   		}
	   
	   		if(controller.getAxis(DS4.AXIS_RIGHT_Y) > 0.2f || controller.getAxis(DS4.AXIS_RIGHT_Y) < -0.2f){
	   			Vector3 tmp = new Vector3();
	   			tmp.set(psGame.cam.direction).crs(psGame.cam.up).nor();
	   			psGame.cam.rotate(tmp, -controller.getAxis(DS4.AXIS_RIGHT_Y));
	   		}
	   }
	   
	   
	   
//	   float value = controller.getAxis(DS4.AXIS_LEFT_X);
//		  value = value * 10f;
//		  System.out.println(value);
//	   psGame.cam.position.set(value, 0f, 100f);
   }
   
    
    public int update(int x){
    	x++;
    	return x;
    }
@Override
	public boolean accelerometerMoved(com.badlogic.gdx.controllers.Controller arg0, int arg1, Vector3 arg2) {
	
	   return false;
   	}

   @Override
   public boolean axisMoved(com.badlogic.gdx.controllers.Controller arg0, int arg1, float arg2) {
	   
	   return false;
   }

   @Override
   public boolean buttonDown(com.badlogic.gdx.controllers.Controller arg0, int button) {
	switch (button) {
	case DS4.BUTTON_CIRCLE:
		System.out.println("Circle");
		System.out.println(psGame.cam.direction.toString());
		return true;
		
	case DS4.BUTTON_CROSS:
		System.out.println("Cross");
		return true;
		
	case DS4.BUTTON_L2:
		System.out.println("L2");
		return true;
		
	case DS4.BUTTON_L3:
		System.out.println("L3");
		return true;
		
	case DS4.BUTTON_OPTIONS:
		System.out.println("Options");
		return true;
		
	case DS4.BUTTON_PS:
		System.out.println("PS");
		return true;
		
	case DS4.BUTTON_R1:
		System.out.println("R1");
		return true;
		
	case DS4.BUTTON_R2:
		System.out.println("R2");
		return true;
		
	case DS4.BUTTON_R3:
		System.out.println("R3");
		return true;
		
	case DS4.BUTTON_SHARE:
		System.out.println("Share");
		return true;
		
	case DS4.BUTTON_SQUARE:
		System.out.println("Square");
		return true;
		
	case DS4.BUTTON_TOUCHPAD:
		System.out.println("Touchpad");
		return true;
		
	case DS4.BUTTON_TRIANGLE:
		System.out.println("Triangle");
		return true;
	
	case DS4.BUTTON_L1:
		System.out.println("L1");
		return true;
	}
	   return false;
   }

   @Override
   public boolean buttonUp(com.badlogic.gdx.controllers.Controller arg0, int arg1) {
	   return false;
   }

   @Override
   public void connected(com.badlogic.gdx.controllers.Controller arg0) {
	
   }

   @Override
   public void disconnected(com.badlogic.gdx.controllers.Controller arg0) {
	
   }
   
   public boolean axisMoved(Controller controller, int axisCode, float value) {
       // Left Stick
       if(axisCode == DS4.AXIS_LEFT_X)
          
       if(axisCode == DS4.AXIS_LEFT_Y)
         
    	   
       // Right stick
       if(axisCode == DS4.AXIS_RIGHT_X)
           System.out.println("g");
       if(axisCode == DS4.AXIS_RIGHT_Y)
    	   System.out.println(value);
       return false;
   }

   @Override
   public boolean povMoved(com.badlogic.gdx.controllers.Controller arg0, int povCode, PovDirection value) {
	   if(value == DS4.BUTTON_DPAD_LEFT)
		  System.out.println("Left");
       if(value == DS4.BUTTON_DPAD_RIGHT)
    	   System.out.println("Right");
       if(value == DS4.BUTTON_DPAD_UP)
           System.out.println("Up");
       if(value == DS4.BUTTON_DPAD_DOWN)
           System.out.println("Down");
       return false;
	   
   }

   @Override
   public boolean xSliderMoved(com.badlogic.gdx.controllers.Controller arg0, int arg1, boolean arg2) {
	   return false;
   }

   @Override
   public boolean ySliderMoved(com.badlogic.gdx.controllers.Controller arg0, int arg1, boolean arg2) {
	   return false;
   }
   
   

}
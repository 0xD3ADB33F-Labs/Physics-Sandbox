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
   public Vehicle vehicle;
   boolean hasControllers = true;
   com.badlogic.gdx.controllers.Controller controller;
   
   
   private PhysicsSandboxGame psGame;   
   public Controller(PhysicsSandboxGame curGame){
	   psGame = curGame;
	   Controllers.addListener(this);
	   
   }
   
   public void assignController(){
	   if (Controllers.getControllers().size == 0){
		   hasControllers = false;
	   }else {
		   controller = Controllers.getControllers().first();
	   }
   }
   
   public void Nudge(){
	   boolean isLinux = true;
	   if (psGame.carMode) {
		   if (hasControllers){
			   
			   if (controller.getButton(DS4Linux.BUTTON_R2)){
				   psGame.getVehicle().accelerating = true;
			   }else {
				psGame.getVehicle().accelerating = false;
			   }
			   if (controller.getButton(DS4Linux.BUTTON_L2)){
				   psGame.getVehicle().braking = true;
			   }else{
				   psGame.getVehicle().braking = false;
			   }
			   
			   if (isLinux){
	    		   
	    		   if (controller.getAxis(DS4Linux.AXIS_LEFT_X) > 0.2f || controller.getAxis(DS4Linux.AXIS_LEFT_X) < -0.2f){
	    			   if(controller.getAxis(DS4Linux.AXIS_LEFT_X) < 0.2f) {
	    				   psGame.getVehicle().turningRight = false;
	    				   psGame.getVehicle().turningLeft = true;
	    				   psGame.getVehicle().turnLeft();
	    				   
	    			   }else if (controller.getAxis(DS4Linux.AXIS_LEFT_X) > -0.2f) {
	    				   psGame.getVehicle().turningLeft = false;
	    				   psGame.getVehicle().turningRight = true;
	    				   psGame.getVehicle().turnRight();
	    			   }else if (controller.getAxis(DS4Linux.AXIS_LEFT_X) > -0.2f && controller.getAxis(DS4Linux.AXIS_LEFT_X) < 0.2f){
	    				   psGame.getVehicle().turningLeft = false;
	    				   psGame.getVehicle().turningRight = false;
	    				   psGame.getVehicle().resetDirection();
	    			   }
	    			   
	    		   	}	
	    		   
	    		   	if(controller.getAxis(DS4Linux.AXIS_LEFT_Y) > 0.2f || controller.getAxis(DS4Linux.AXIS_LEFT_Y) <-0.2f){
	    		   		Vector3 translate = psGame.getCam().direction.cpy();
	    		   		translate.nor();
	    		   		translate.scl((-controller.getAxis(DS4Linux.AXIS_LEFT_Y) * 0.10f));
	    			   	psGame.getCam().translate(translate);
	    		   	}
	    		   
	    		   	if(controller.getAxis(DS4Linux.AXIS_RIGHT_X) > 0.2f || controller.getAxis(DS4Linux.AXIS_RIGHT_X) < -0.2f){
	    		   		psGame.getCam().rotate(-controller.getAxis(DS4Linux.AXIS_RIGHT_X), 0, 1, 0);
	    		   	}
	    		   
	    		   	if(controller.getAxis(DS4Linux.AXIS_RIGHT_Y) > 0.2f || controller.getAxis(DS4Linux.AXIS_RIGHT_Y) < -0.2f){
	    		   		Vector3 tmp = new Vector3();
	    		   		tmp.set(psGame.getCam().direction).crs(psGame.getCam().up).nor();
	    		   		psGame.getCam().rotate(tmp, -controller.getAxis(DS4Linux.AXIS_RIGHT_Y));
	    		   	}
	    	   }else {
	    		   if (!isLinux){
	        		   
	        		   if (controller.getAxis(DS4.AXIS_LEFT_X) > 0.2f || controller.getAxis(DS4.AXIS_LEFT_X) < -0.2f){
	        			   Vector3 translate = psGame.getCam().direction.cpy();
	        			   translate.rotate(90.0f, 0.0f, 1.0f, 0.0f);
	        			   translate.nor();
	        			   translate.scl(-controller.getAxis(DS4.AXIS_LEFT_X) * 0.15f);
	        			   psGame.getCam().translate(translate);
	        			   
	        		   	}	
	        		   
	        		   	if(controller.getAxis(DS4.AXIS_LEFT_Y) > 0.2f || controller.getAxis(DS4.AXIS_LEFT_Y) <-0.2f){
	        		   		Vector3 translate = psGame.getCam().direction.cpy();
	        		   		translate.nor();
	        		   		translate.scl((-controller.getAxis(DS4.AXIS_LEFT_Y) * 0.10f));
	        			   	psGame.getCam().translate(translate);
	        		   	}
	        		   
	        		   	if(controller.getAxis(DS4.AXIS_RIGHT_X) > 0.2f || controller.getAxis(DS4.AXIS_RIGHT_X) < -0.2f){
	        		   		psGame.getCam().rotate(-controller.getAxis(DS4.AXIS_RIGHT_X), 0, 1, 0);
	        		   	}
	        		   
	        		   	if(controller.getAxis(DS4.AXIS_RIGHT_Y) > 0.2f || controller.getAxis(DS4.AXIS_RIGHT_Y) < -0.2f){
	        		   		Vector3 tmp = new Vector3();
	        		   		tmp.set(psGame.getCam().direction).crs(psGame.getCam().up).nor();
	        		   		psGame.getCam().rotate(tmp, -controller.getAxis(DS4.AXIS_RIGHT_Y));
	        		   	}
	        	   }
	    	   }
	       }
	   }else if(!psGame.carMode) {
		   if (hasControllers){
			   if (isLinux){
	    		   
	    		   if (controller.getAxis(DS4Linux.AXIS_LEFT_X) > 0.2f || controller.getAxis(DS4Linux.AXIS_LEFT_X) < -0.2f){
	    			   Vector3 translate = psGame.getCam().direction.cpy();
	    			   translate.rotate(90.0f, 0.0f, 1.0f, 0.0f);
	    			   translate.nor();
	    			   translate.scl(-controller.getAxis(DS4Linux.AXIS_LEFT_X) * 0.15f);
	    			   psGame.getCam().translate(translate);
	    			   
	    		   	}	
	    		   
	    		   	if(controller.getAxis(DS4Linux.AXIS_LEFT_Y) > 0.2f || controller.getAxis(DS4Linux.AXIS_LEFT_Y) <-0.2f){
	    		   		Vector3 translate = psGame.getCam().direction.cpy();
	    		   		translate.nor();
	    		   		translate.scl((-controller.getAxis(DS4Linux.AXIS_LEFT_Y) * 0.10f));
	    			   	psGame.getCam().translate(translate);
	    		   	}
	    		   
	    		   	if(controller.getAxis(DS4Linux.AXIS_RIGHT_X) > 0.2f || controller.getAxis(DS4Linux.AXIS_RIGHT_X) < -0.2f){
	    		   		psGame.getCam().rotate(-controller.getAxis(DS4Linux.AXIS_RIGHT_X), 0, 1, 0);
	    		   	}
	    		   
	    		   	if(controller.getAxis(DS4Linux.AXIS_RIGHT_Y) > 0.2f || controller.getAxis(DS4Linux.AXIS_RIGHT_Y) < -0.2f){
	    		   		Vector3 tmp = new Vector3();
	    		   		tmp.set(psGame.getCam().direction).crs(psGame.getCam().up).nor();
	    		   		psGame.getCam().rotate(tmp, -controller.getAxis(DS4Linux.AXIS_RIGHT_Y));
	    		   	}
	    	   }else {
	    		   if (!isLinux){
	        		   
	        		   if (controller.getAxis(DS4.AXIS_LEFT_X) > 0.2f || controller.getAxis(DS4.AXIS_LEFT_X) < -0.2f){
	        			   Vector3 translate = psGame.getCam().direction.cpy();
	        			   translate.rotate(90.0f, 0.0f, 1.0f, 0.0f);
	        			   translate.nor();
	        			   translate.scl(-controller.getAxis(DS4.AXIS_LEFT_X) * 0.15f);
	        			   psGame.getCam().translate(translate);
	        			   
	        		   	}	
	        		   
	        		   	if(controller.getAxis(DS4.AXIS_LEFT_Y) > 0.2f || controller.getAxis(DS4.AXIS_LEFT_Y) <-0.2f){
	        		   		Vector3 translate = psGame.getCam().direction.cpy();
	        		   		translate.nor();
	        		   		translate.scl((-controller.getAxis(DS4.AXIS_LEFT_Y) * 0.10f));
	        			   	psGame.getCam().translate(translate);
	        		   	}
	        		   
	        		   	if(controller.getAxis(DS4.AXIS_RIGHT_X) > 0.2f || controller.getAxis(DS4.AXIS_RIGHT_X) < -0.2f){
	        		   		psGame.getCam().rotate(-controller.getAxis(DS4.AXIS_RIGHT_X), 0, 1, 0);
	        		   	}
	        		   
	        		   	if(controller.getAxis(DS4.AXIS_RIGHT_Y) > 0.2f || controller.getAxis(DS4.AXIS_RIGHT_Y) < -0.2f){
	        		   		Vector3 tmp = new Vector3();
	        		   		tmp.set(psGame.getCam().direction).crs(psGame.getCam().up).nor();
	        		   		psGame.getCam().rotate(tmp, -controller.getAxis(DS4.AXIS_RIGHT_Y));
	        		   	}
	        	   }
	    	   }
	       }
	   }
	   
	   
   }
	   
	   
	   
//	   float value = controller.getAxis(DS4.AXIS_LEFT_X);
//		  value = value * 10f;
//		  System.out.println(value);
//	   psGame.cam.position.set(value, 0f, 100f);
   
   
    
   
@Override
	public boolean accelerometerMoved(com.badlogic.gdx.controllers.Controller arg0, int arg1, Vector3 arg2) {

	   return false;
   	}

   
   public boolean axisMoved(com.badlogic.gdx.controllers.Controller arg0, int arg1, float arg2) {
	   
	   return false;
   }

   
   public boolean buttonDown(com.badlogic.gdx.controllers.Controller arg0, int button) {
	   
	switch (button) {
	case DS4.BUTTON_CIRCLE:
		
		
		return true;
		
	case DS4.BUTTON_CROSS:
		
		return true;
		
	case DS4.BUTTON_L2:
		
		return true;
		
	case DS4.BUTTON_L3:
		return true;
		
	case DS4.BUTTON_OPTIONS:
		return true;
		
	case DS4.BUTTON_PS:
		return true;
		
	case DS4.BUTTON_R1:
		return true;
		
	case DS4.BUTTON_R2:
		
		return true;
		
	case DS4.BUTTON_R3:
		return true;
		
	case DS4.BUTTON_SHARE:
		return true;
		
	case DS4.BUTTON_SQUARE:
		return true;
		
	case DS4.BUTTON_TOUCHPAD:
		return true;
		
	case DS4.BUTTON_TRIANGLE:
		return true;
	
	case DS4.BUTTON_L1:
		return true;
	default: 
		psGame.getVehicle().resetDirection();
		psGame.getVehicle().accelerating = false;
		psGame.getVehicle().braking = false;
		
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
      
       return false;
	
   }

   @Override
   public boolean povMoved(com.badlogic.gdx.controllers.Controller arg0, int povCode, PovDirection value) {
	   if(value == DS4.BUTTON_DPAD_LEFT) {
		   
	   }
		  
       if(value == DS4.BUTTON_DPAD_RIGHT) {
    	   
       }
    	   
       if(value == DS4.BUTTON_DPAD_UP) {
    	   
       }
           
       if(value == DS4.BUTTON_DPAD_DOWN) {
    	   
       }
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
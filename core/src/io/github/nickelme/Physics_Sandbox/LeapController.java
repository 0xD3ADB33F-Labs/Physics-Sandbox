package io.github.nickelme.Physics_Sandbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;

public class LeapController extends Listener {

	public final static float LEAP_SCALE_FACTOR = 0.5f;
	
	Controller control;

	HashMap<Integer, LeapHand> hands;

	public LeapController(){
		hands = new HashMap<Integer, LeapHand>();
		control = new Controller();
		control.addListener(this);

	}

	public void onConnect(Controller controller) {
		System.out.println("Controller Connected");
	}

	public void onFrame(Controller controller) {
		PhysicsSandboxGame psGame = PhysicsSandboxGame.getInstance();
		Frame frame = controller.frame();
		HandList handlist = frame.hands();
		
		
		Iterator<Hand> handsitr = handlist.iterator();
		while(handsitr.hasNext()){
			Hand curhand = handsitr.next();
			if(!hands.containsKey(new Integer(curhand.id()))){
				LeapHand newhand = new LeapHand(curhand);
				hands.put(curhand.id(), newhand);
				System.out.println("Adding id: " + curhand.id());
				//psGame.addObject(newhand);
				newhand.LeapUpdate(curhand);
			}else{
				LeapHand existhand = hands.get(new Integer(curhand.id()));
				existhand.LeapUpdate(curhand);
			}

		}
		List<Integer> idstoerase = new ArrayList<Integer>();
		Iterator<Integer> ids = hands.keySet().iterator();
		while(ids.hasNext()){
			boolean stillexists = false;
			Integer curid = ids.next();
		
			for(int i = 0; i<handlist.count(); i++){
				Hand curhand = handlist.get(i);
				if(curhand.id() == curid.intValue()){
					stillexists = true;
					break;
				}
			}
			if(!stillexists){
				System.out.println("Removing id: " + curid);
				idstoerase.add(curid);
			}
		}
		for(int i = 0; i<idstoerase.size(); i++){
			hands.get(idstoerase.get(i)).CleanSelfUp();
			hands.remove(idstoerase.get(i));
		}
	}


}

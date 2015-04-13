package io.github.nickelme.Physics_Sandbox;

import io.github.nickelme.Physics_Sandbox.Comms.PSObject.ObjectType;
import io.github.nickelme.Physics_Sandbox.Comms.PSPacket;
import io.github.nickelme.Physics_Sandbox.Comms.PSPacket.PacketType;
import io.github.nickelme.Physics_Sandbox.Comms.PSSendObjectsPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.google.protobuf.ByteString.Output;


public class Client {
	
	private Socket mSock;
	
	private Runnable mRecieveRunner = new Runnable(){

		@Override
		public void run() {
			try {
				DataInputStream instream = new DataInputStream(mSock.getInputStream());
				int size = 0;
				while(true){
					size = instream.readInt();
					byte data[] = new byte[size];
					
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	};
	
	public void sendPacket(PSPacket pack){
		try{
			DataOutputStream outstream = new DataOutputStream(mSock.getOutputStream());
			byte data[] = pack.toByteArray();
			outstream.writeInt(data.length);
			System.out.println("Data length: " + data.length);
			outstream.write(data);
			outstream.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Client(Socket sock){
		mSock = sock;
		PSSendObjectsPacket.Builder sendbuild =	PSSendObjectsPacket.newBuilder();
		HashMap<Long, PSObject> Objects = PhysicsSandboxGame.getInstance().Objects;
		for(Long key : Objects.keySet()){
			if((Objects.get(key) instanceof PrimitiveCube && !(Objects.get(key) instanceof Floor))){
				PSObject obj = Objects.get(key);
				Comms.PSObject.Builder netobjb = Comms.PSObject.newBuilder().setType(ObjectType.CUBE);
				float mat[] = obj.getMatrix().getValues();
				Comms.Matrix4 netmat = Comms.Matrix4.newBuilder()
						.setM00(mat[0]).setM01(mat[1]).setM02(mat[2]).setM03(mat[3])
						.setM10(mat[4]).setM11(mat[5]).setM12(mat[6]).setM13(mat[7])
						.setM20(mat[8]).setM21(mat[9]).setM22(mat[10]).setM23(mat[11])
						.setM30(mat[12]).setM31(mat[13]).setM32(mat[14]).setM33(mat[15]).build();
				netobjb.setLocation(netmat);
				Vector3 objsize = obj.getSize();
				Vector3 objvec = Vector3.Zero;
				netobjb.setSize(Comms.Vector3.newBuilder().setX(objsize.x).setY(objsize.y).setZ(objsize.z).build());
				netobjb.setVelocity(Comms.Vector3.newBuilder().setX(objvec.x).setY(objvec.y).setZ(objvec.z).build());
				netobjb.setUOID(obj.getId());
				sendbuild.addObjects(netobjb.build());
				//System.out.println("Added Object to send");
			}
		}
		PSPacket.Builder build = PSPacket.newBuilder().setPacketId(PacketType.SEND_OBJECTS);
		PSSendObjectsPacket pack = sendbuild.build();
		System.out.println("Objects to Send: " + pack.getObjectsCount());
		build.setExtension(PSSendObjectsPacket.packet, pack);
		sendPacket(build.build());
	}

}

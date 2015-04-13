package io.github.nickelme.Physics_Sandbox;

import io.github.nickelme.Physics_Sandbox.Comms.PSPacket;
import io.github.nickelme.Physics_Sandbox.Comms.PSSendObjectsPacket;
import io.github.nickelme.Physics_Sandbox.Comms.PSUpdateObjectsPacket;
import io.github.nickelme.Physics_Sandbox.Comms.PSPacket.PacketType;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.google.protobuf.ExtensionRegistry;

class ObjectBuilder{

	Matrix4 matrix;
	Vector3 size;
	Long id;
}

public class Networker {

	private ServerSocket mServerSock;
	private ArrayList<Client> mClients = new ArrayList<Client>();

	private Socket mSock;


	void UpdateObjects(PSPacket pack){
		PSUpdateObjectsPacket recpack = pack.getExtension(PSUpdateObjectsPacket.packet);
		for(Comms.PSObject obj : recpack.getObjectsList()){
			if(obj.hasLocation()){
				Comms.Matrix4 commat = obj.getLocation();
				float mat[] = new float[]{
						commat.getM00(),commat.getM01(),commat.getM02(),commat.getM03(),
						commat.getM10(),commat.getM11(),commat.getM12(),commat.getM13(),
						commat.getM20(),commat.getM21(),commat.getM22(),commat.getM23(),
						commat.getM30(),commat.getM31(),commat.getM32(),commat.getM33(),
				};
				Matrix4 matrix = new Matrix4(mat);
				PhysicsSandboxGame.getInstance().Objects.get(obj.getUOID()).setMatrix(matrix);
			}
		}
	}

	private Runnable mAcceptor = new Runnable(){

		@Override
		public void run() {
			try {
				mServerSock = new ServerSocket(mPort);
				while(bIsAlive){
					Socket sock = mServerSock.accept();
					System.out.println("New Client");
					Client ncli = new Client(sock);
					mClients.add(ncli);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	};

	private Runnable mConnectandRecieve = new Runnable(){

		@Override
		public void run() {
			try {
				mSock = new Socket(mHost, mPort);
				System.out.println("Connected");
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						PhysicsSandboxGame.getInstance().ClearWorld();
					}
				});
				DataInputStream instream = new DataInputStream(mSock.getInputStream());
				while(bIsAlive){
					int size = instream.readInt();
					byte data[] = new byte[size];
					instream.readFully(data);
					System.out.println("Data Length: " + data.length);
					ExtensionRegistry registry = ExtensionRegistry.newInstance();
					registry.add(PSSendObjectsPacket.packet);
					registry.add(PSUpdateObjectsPacket.packet);
					PSPacket pack = PSPacket.parseFrom(data, registry);
					switch(pack.getPacketId()){

					case SEND_OBJECTS:
						System.out.println("New Objects");
						if(pack.hasExtension(PSSendObjectsPacket.packet)){
							System.out.println("Has SendObjectsPacket");
						}
						PSSendObjectsPacket recpack = pack.getExtension(PSSendObjectsPacket.packet);
						System.out.println("Objects Recieved: " + recpack.getObjectsCount());
						final ArrayList<ObjectBuilder> objects = new ArrayList<ObjectBuilder>();
						for(Comms.PSObject obj : recpack.getObjectsList()){

							switch(obj.getType()){

							case CUBE:
								Vector3 objsize = new Vector3(obj.getSize().getX(),obj.getSize().getY(), obj.getSize().getZ());
								Comms.Matrix4 commat = obj.getLocation();
								float mat[] = new float[]{
										commat.getM00(),commat.getM01(),commat.getM02(),commat.getM03(),
										commat.getM10(),commat.getM11(),commat.getM12(),commat.getM13(),
										commat.getM20(),commat.getM21(),commat.getM22(),commat.getM23(),
										commat.getM30(),commat.getM31(),commat.getM32(),commat.getM33(),
								};
								Matrix4 matrix = new Matrix4(mat);
								ObjectBuilder objtb = new ObjectBuilder();
								objtb.matrix = matrix.cpy();
								objtb.size = objsize.cpy();
								objtb.id = obj.getUOID();
								objects.add(objtb);
								break;

							default:
								System.out.println("Unknow Object");
								break;
							}
						}
						Gdx.app.postRunnable(new Runnable() {
							public void run() {
								System.out.println("Adding Objects to World");
								for(ObjectBuilder objtb : objects){
									PrimitiveCube psobj = new PrimitiveCube(objtb.size, objtb.matrix);
									PhysicsSandboxGame.getInstance().addObjectAt(psobj, objtb.id);
								}
								objects.clear();
							}
						});
						break;

					case UPDATE_OBJECTS:
						UpdateObjects(pack);


					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	};

	private boolean bIsAlive = false;
	
	private boolean bIsServer = false;

	private String mHost;

	private short mPort = 7472;

	private Thread mServerThread;

	private Thread mClientThread;

	public void StartServer(short port){
		bIsAlive = true;
		bIsServer = true;
		mServerThread = new Thread(mAcceptor);
		mServerThread.start();
	}

	public void ConnectToHost(String host){
		bIsAlive = true;
		mHost = host;
		mClientThread = new Thread(mConnectandRecieve);
		mClientThread.start();
	}

	public void NetworkUpdate(){
		if(bIsAlive && bIsServer){
			HashMap<Long, PSObject> Objects = PhysicsSandboxGame.getInstance().Objects;
			PSUpdateObjectsPacket.Builder sendbuild =	PSUpdateObjectsPacket.newBuilder();
			for(Long key : Objects.keySet()){
				if((Objects.get(key) instanceof PrimitiveCube && !(Objects.get(key) instanceof Floor))){
					PSObject obj = Objects.get(key);
					if(obj.needsNeetUpdate()){
						Comms.PSObject.Builder bobj = Comms.PSObject.newBuilder();
						bobj.setUOID(obj.getId());
						float mat[] = obj.getMatrix().getValues();
						Comms.Matrix4 netmat = Comms.Matrix4.newBuilder()
								.setM00(mat[0]).setM01(mat[1]).setM02(mat[2]).setM03(mat[3])
								.setM10(mat[4]).setM11(mat[5]).setM12(mat[6]).setM13(mat[7])
								.setM20(mat[8]).setM21(mat[9]).setM22(mat[10]).setM23(mat[11])
								.setM30(mat[12]).setM31(mat[13]).setM32(mat[14]).setM33(mat[15]).build();
						bobj.setLocation(netmat);
						sendbuild.addObjects(bobj.build());
					}
				}
			}
			PSPacket.Builder build = PSPacket.newBuilder().setPacketId(PacketType.UPDATE_OBJECTS);
			PSUpdateObjectsPacket pack = sendbuild.build();
			if(pack.getObjectsCount() > 0){
				System.out.println("Objects to Send: " + pack.getObjectsCount());
				build.setExtension(PSUpdateObjectsPacket.packet, pack);
				PSPacket headpack = build.build();
				for(Client cli : mClients){
					cli.sendPacket(headpack);
				}
			}
		}
	}

}

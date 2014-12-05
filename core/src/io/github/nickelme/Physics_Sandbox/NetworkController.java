package io.github.nickelme.Physics_Sandbox;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class NetworkController {

	private boolean isRunning;

	private static final int PORT = 3456;
	private static final String MULTICAST_GROUP = "225.125.1.5";
	private static final byte[] PROGRAM_ID = "NJPS".getBytes();
	private static final int VERSION = 0001;


	private Thread recieveThread;

	private MulticastSocket multiSock;

	private Runnable recieveRunner = new Runnable() {
		public void run() {
			while(isRunning){
				byte[] rx = new byte[65507];
				DatagramPacket pkt = new DatagramPacket(rx, rx.length);
				try {
					multiSock.receive(pkt);
					ByteBuffer buf = ByteBuffer.wrap(pkt.getData());
					byte[] progpkt = new byte[4];
					buf.get(progpkt);
					if(Arrays.equals(PROGRAM_ID,progpkt)){
						byte[] packtype = new byte[4];
						buf.get(packtype);
						for(PacketType type : PacketType.values()){
							if(Arrays.equals(type.getType(), packtype)){
								try {
									Packet pack =  type.getPacketClass().getDeclaredConstructor(ByteBuffer.class).newInstance(buf);
								} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
									e.printStackTrace();
								}
							}
						}
					}else{
						System.err.println("Packet not of program");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	};


	public NetworkController(){
		isRunning = false;
	}


	public void Start(){
		isRunning = true;
		try {
			multiSock = new MulticastSocket(PORT);
			multiSock.joinGroup(InetAddress.getByName(MULTICAST_GROUP));
			multiSock.setLoopbackMode(true);
			recieveThread = new Thread(recieveRunner);

			recieveThread.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void Stop(){
		isRunning = false;
		recieveThread.interrupt();
		try {
			recieveThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			multiSock.leaveGroup(InetAddress.getByName(MULTICAST_GROUP));
			multiSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SendAliveRequest(){
		ByteBuffer buf = ByteBuffer.allocate(12);
		buf.put(PROGRAM_ID);
		buf.put(PacketType.ISALIVE.getType());
		buf.putInt(VERSION);
		try {
			DatagramPacket pkt = new DatagramPacket(buf.array(), buf.limit(), InetAddress.getByName(MULTICAST_GROUP), PORT);
			multiSock.send(pkt);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

package io.github.nickelme.Physics_Sandbox;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class EyeTracker {
	
	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	
	private VideoCapture cap;
	
	private SpriteBatch batch;
	
	private CascadeClassifier faceDetector;
	
	private CascadeClassifier eyeDetector;
	
	public EyeTracker(){
		 System.out.println("Welcome to OpenCV " + Core.VERSION);
		 cap = new VideoCapture(0);
		 if(!cap.isOpened()){
			 System.err.println("Failed to Open Camera");
			 return;
		 }else{
			 System.out.println("Camera Ok!");
		 }
		 batch = new SpriteBatch();
		 Gdx.files.internal("haarcascade_frontalface_default.xml").copyTo(Gdx.files.external(""));
		 Gdx.files.internal("haarcascade_eye.xml").copyTo(Gdx.files.external(""));
		 faceDetector = new CascadeClassifier(Gdx.files.external("haarcascade_frontalface_default.xml").file().toString());
		 eyeDetector = new CascadeClassifier(Gdx.files.external("haarcascade_eye.xml").file().toString());
		 //aceDetector.load(Gdx.files.internal("lbpcascade_frontalface.xml").file().toString());
		 if(faceDetector.empty()){
			 System.err.println("Fail to load definitions");
		 }
		 
	}
	
	public void Draw(){
	    Mat frame = new Mat();

	    cap.read(frame);
	    
	    Mat scaled = new Mat();
	    //Imgproc.resize(frame, scaled, new Size(160,120));
	    Imgproc.cvtColor(frame, scaled, Imgproc.COLOR_BGR2GRAY);
	    Imgproc.equalizeHist(scaled, scaled);
	    
	    MatOfRect faceDetections = new MatOfRect();
	    faceDetector.detectMultiScale(scaled, faceDetections, 1.1, 2, 2, new Size(0,0), new Size());

	    //System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
	    float scaleh = frame.height()/scaled.height();
	    float scalew = frame.width()/scaled.width();
	    
	    
	    // Draw a bounding box around each face.
	    for (Rect rect : faceDetections.toArray()) {
	    	Rect facerect = new Rect(new Point(rect.x*scalew, rect.y*scaleh), new Point((rect.x*scalew) + (rect.width*scalew), (rect.y*scaleh) + (rect.height*scaleh)));
		    Mat face = new Mat(frame, facerect);
		    MatOfRect eyes = new MatOfRect();
		    eyeDetector.detectMultiScale(face, eyes);
		    if(eyes.total() < 3){
		    	for(Rect eyerect : eyes.toArray()){
		    		Core.rectangle(frame, new Point(eyerect.x+facerect.x, eyerect.y+facerect.y), new Point((eyerect.x+facerect.x) + (eyerect.width), (eyerect.y+facerect.y) + (eyerect.height)), new Scalar(255, 0, 0));
		    	}
		    	Core.rectangle(frame, new Point(rect.x*scalew, rect.y*scaleh), new Point((rect.x*scalew) + (rect.width*scalew), (rect.y*scaleh) + (rect.height*scaleh)), new Scalar(0, 255, 0));
		    }else{
		    	Core.rectangle(frame, new Point(rect.x*scalew, rect.y*scaleh), new Point((rect.x*scalew) + (rect.width*scalew), (rect.y*scaleh) + (rect.height*scaleh)), new Scalar(255, 0, 255));
		    }
	    }

	    int bufferSize = frame.channels()*frame.cols()*frame.rows();
	    //System.out.println("Buff size: " + bufferSize);
	    byte[] bytes = new byte[bufferSize];
	    frame.get(0, 0, bytes);
	    for(int i = 0; i<bufferSize; i+=3){
	    	byte R = bytes[i+2];
	    	byte B = bytes[i];
	    	bytes[i] = R;
	    	bytes[i+2] = B;
	    }
	    Pixmap map = new Pixmap(frame.width(), frame.height(), Format.RGB888);
	    
	    //System.out.println("Capacity :" + map.getPixels().capacity());
	    map.getPixels().clear();
	    map.getPixels().put(bytes);
	    map.getPixels().flip();
	    //map.getPixels().rewind();
	    //System.out.println("Capacity :" + map.getPixels().capacity());
	    Texture tex = new Texture(map);
	    //tex.getTextureData().consumePixmap().getPixels().put(bytes);
	    //tex.getTextureData().prepare();
	    batch.begin();
	    batch.draw(tex, 0, 0);
	    batch.end();
	    tex.dispose();
	    map.dispose();
	}

}

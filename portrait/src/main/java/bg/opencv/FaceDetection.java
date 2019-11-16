package bg.opencv;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetection {
	CascadeClassifier faceDetector ;

	private FaceDetection() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		 faceDetector = new CascadeClassifier();
		 faceDetector.load("haarcascade_frontalface_alt.xml");
	}
    static FaceDetection instance = new FaceDetection();
	public static void main(String[] args) {

		// For proper execution of native libraries
		// Core.NATIVE_LIBRARY_NAME must be loaded before
		// calling any of the opencv methods
		

		// Face detector creation by loading source cascade xml file
		// using CascadeClassifier.
		// the file can be downloade from
		// https://github.com/opencv/opencv/blob/master/data/haarcascades/
		// haarcascade_frontalface_alt.xml
		// and must be placed in same directory of the source java file

		File f1 = new File("D:\\input.jpg");
		File dirTest = new File("OUT_TEST");
		getInstance().processFile(f1,new File(dirTest,"OUT_CROP_BRUT"), new File(dirTest,"OUT_TEMP"));
	}
	
	public static FaceDetection getInstance() {
		return instance;
	}

	public  void processFile(File f1, File dirCrop, File dirTemp) {
		
		System.err.println("faceDetector.load");
		faceDetector.load("haarcascade_frontalface_alt.xml");

		System.err.println(" f " + f1.exists() + "   " + f1.getAbsolutePath());
		// https://github.com/opencv/opencv/blob/master/data/haarcascades/
		// haarcascade_frontalface_alt.xml
		// and must be placed in same directory of the source java file

		// Input image
		Mat image = Imgcodecs.imread(f1.getAbsolutePath());

		// Detecting faces
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);

		// Creating a rectangular box showing faces detected
		int i = 0;
		for (Rect rect : faceDetections.toArray()) {
			Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
					new Scalar(0, 255, 0));
			Rect rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);
			Mat image_roi = new Mat(image, rectCrop);
			
			dirCrop.mkdirs();
			File fOut2 = new File(dirCrop, i++ + "_crop_brut."+getTypeImage(f1));
			Imgcodecs.imwrite(fOut2.getAbsolutePath(), image_roi);
		}

		// Saving the output image

		
		dirTemp.mkdirs();
		File fOut = new File(dirTemp, f1.getName());
		Imgcodecs.imwrite(fOut.getAbsolutePath(), image);
		System.err.println("Done "+f1.getName());
	}

	private String getTypeImage(File f1) {
		String suffix=null;
		String name = f1.getName();
		int i = name.lastIndexOf(".");
		if (i>0) {
			suffix = name.substring(i);
		}
		return suffix;
	}
}
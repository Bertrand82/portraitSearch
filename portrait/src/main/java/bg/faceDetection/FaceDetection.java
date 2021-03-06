package bg.faceDetection;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bg.portrait.googleCSE.GoogleImageCSE;

/*
 * For proper execution of native libraries
		Core.NATIVE_LIBRARY_NAME must be loaded before
		 calling any of the opencv methods
		

		Face detector creation by loading source cascade xml file
		using CascadeClassifier.
		 the file can be downloade from
		 https://github.com/opencv/opencv/blob/master/data/haarcascades/
		 haarcascade_frontalface_alt.xml
		 and must be placed in same directory of the source java file
*/
public class FaceDetection {
	private static final Logger logger = LoggerFactory.getLogger(FaceDetection.class);
	
	private static final String OUT_CROP_NORMALIZED = "CROP_NORMALIZED";
	public static String OUT_CROP_BRUT="CROP_BRUT";
	public static String OUT_WORK = "WORK";
	private CascadeClassifier faceDetector;

	private FaceDetection() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		faceDetector = new CascadeClassifier();
		faceDetector.load("haarcascade_frontalface_alt.xml");
	}

	static FaceDetection instance = new FaceDetection();

	public static void main(String[] args) throws Exception{
		File dirOutTest = new File("OUT_TEST2");
		dirOutTest.mkdirs();
		File dir = new File("src/test/resources");
		File f0 = new File(dir,"input.jpg");
		
		System.err.println("f0 exists "+f0.exists());
		File fDest0 = new File(dirOutTest,"input.jpg");
		Files.copy(f0.toPath(), fDest0.toPath(), StandardCopyOption.REPLACE_EXISTING);
		getInstance().processFile(fDest0);
		
		File dir2 = new File("D:/IMAGES/2/7/e/5/27e5b263baa4b6b809985527c0ca235d");
		File f2 = new File(dir2,"27e5b263baa4b6b809985527c0ca235d_master.jpg");
		File fDest2 = new File(dirOutTest,"input2.jpg");
		Files.copy(f2.toPath(), fDest2.toPath(), StandardCopyOption.REPLACE_EXISTING);
		getInstance().processFile(fDest2);
		
	}

	public File processFile(File f1) {
		File dirImage = f1.getParentFile();
		File dirNormalized = new File(dirImage, OUT_CROP_NORMALIZED);
		processFile(f1, new File(dirImage, OUT_CROP_BRUT), new File(dirImage,OUT_WORK ),dirNormalized);
		return dirNormalized;
	}

	public static FaceDetection getInstance() {
		return instance;
	}

	public void processFile(File f1, File dirCropBrut, File dirTemp, File dirCropNormalized) {
		dirCropBrut.mkdirs();
		dirCropNormalized.mkdirs();
		System.err.println("faceDetector.load");
		faceDetector.load("haarcascade_frontalface_alt.xml");

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
			Mat image_crop = new Mat(image, rectCrop);
			Mat image_crop_resized = new Mat();
			Size sz = new Size(200,200);
			Imgproc.resize( image_crop, image_crop_resized, sz );
			
			File fCrop = new File(dirCropBrut, i++ + "_crop_brut." + getTypeImage(f1));
			Imgcodecs.imwrite(fCrop.getAbsolutePath(), image_crop);
			
			File fCropResized = new File(dirCropNormalized, i++ + "_crop." + getTypeImage(f1));
			Imgcodecs.imwrite(fCropResized.getAbsolutePath(), image_crop_resized);
		}

		// Saving the output image

		dirTemp.mkdirs();
		File fOut = new File(dirTemp, f1.getName());
		Imgcodecs.imwrite(fOut.getAbsolutePath(), image);
		if (i == 0) {
			logger.info("No Face Detected");
		}
	}

	private String getTypeImage(File f1) {
		String suffix = null;
		String name = f1.getName();
		int i = name.lastIndexOf(".");
		if (i > 0) {
			suffix = name.substring(i+1);
		}
		return suffix;
	}
}
package bg.process;

import java.io.File;

import bg.faceDetection.FaceDetection;
import bg.faceRecognition.ProcessFaceRecognition;


public class MainFaceRecognition {
	private static File dirWorking = new File("D:\\imagesWorking");

	public static void main(String args[]) {
		dirWorking.mkdirs();
		String fileName = "bg.jpg";
		try {
			System.out.println("processing  file :" + fileName);
			File fileMaster = (new File(dirWorking,fileName)).getCanonicalFile();
			System.out.println(" file master exists " + fileMaster.exists());
			File dirNormalized = FaceDetection.getInstance().processFile(fileMaster);
			File fileNormalized = dirNormalized.listFiles()[0];
			if (fileNormalized == null) {
				System.err.println("Aie aie aie Pas de face detectée dans image : "+fileMaster.getAbsolutePath());
				return;
			}
			System.out.println("Exists : " + fileNormalized.exists() + "  " + fileNormalized.getAbsolutePath());
			
			ProcessFaceRecognition.processImageNormalized(fileNormalized);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

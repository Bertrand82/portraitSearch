package bg.faceRecognition;

import java.io.File;

import bg.portrait.util.UtilFile;

public class ProcessFaceRecognition {

	private static File dir = new File("D:\\imagesTemp");

	public static File processImageNormaized(File imageNormalized) {

		try {
			EigenFaceCreator creator = new EigenFaceCreator();

			// creator.USE_CACHE = -1;
			System.out.println("Constructing face-spaces from " + dir + " ...");
			creator.readFaceBundles(dir);

			if (imageNormalized == null) {
				System.err.println("Aie aie aie is null");
			}
			System.out.println(" " + imageNormalized.exists() + "  " + imageNormalized.getAbsolutePath());
			String result = creator.checkAgainst(imageNormalized);
			System.out.println("result : " + result);
			if (result == null) {
				System.out.println("No result ! ");
				return null;
			} else {
				File fResult = new File(dir, result);
				System.out.println("Most closly reseambling: " + result + " with " + creator.distance + " distance.");
				System.out.println(fResult.getAbsolutePath());
				String hashMd5 = UtilFile.getHashFromFile(fResult);
				File dir = UtilFile.getDirRootFromHashMd5(hashMd5);
				System.out.println(dir.getAbsolutePath());
				return fResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}

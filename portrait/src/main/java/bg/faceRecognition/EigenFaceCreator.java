package bg.faceRecognition;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bg.portrait.util.UtilFile;

/**
 * Creates the FaceBundle's from the list of images and tries to match against
 * submitted image.
 *
 */
public class EigenFaceCreator {

	private static final int MAGIC_SETNR = 16;
	private FaceBundle[] faceBundle = null;
	/**
	 * Seuil minimal pour accepter une ressemblance
	 */
	public static double SEUILL_MINI = 3.0;

	/**
	 * Our minimum distance observed for the submitted image in the face-spaces.
	 *
	 */
	public double distance = Double.MAX_VALUE;

	/**
	 * This determines if caching of face-spaces should be activated. Anything above
	 * zero means yes. Anything else means no.
	 */
	public final int USE_CACHE = 1;

	private static FileFilter imageFilter = new FileFilter() {

		public boolean accept(File f) {
			String fName = f.getName();
			if ((fName.endsWith(".ppm")) || (fName.endsWith(".pnm")) || (fName.endsWith(".jpg")) || (fName.endsWith(".jpeg"))) {
				return true;
			} else {
				return false;
			}
		}
	};

	/**
	 * Match against the given file.
	 *
	 * @return The Identifier of the image in the face-space. If image not found
	 *         (based on Seuill Mini) null is returned.
	 */
	public String checkAgainst(File file) throws Exception {

		String id = null;
		if (faceBundle == null) {
			System.out.println("bundle is null !!!!!!!!!!! ");
		} else {
			double small = Double.MAX_VALUE;
			int idx = -1;
			IImage iimage = readIImage(file);
			double[] imgArray = iimage.getDouble();
			System.out.println("bundle length " + faceBundle.length);
			for (int i = 0; i < faceBundle.length; i++) {
				faceBundle[i].submitFace(imgArray);
				System.out.println("small " + small + "  distance " + faceBundle[i].distance() + "  " + faceBundle[i].getIndexName());
				if (small > faceBundle[i].distance()) {
					small = faceBundle[i].distance();
					idx = i;
				}
			}
			distance = small;
			if (small < SEUILL_MINI + 1) {
				id = faceBundle[idx].getIndexName();
			}
		}
		return id;
	}

	/**
	 * Construct the face-spaces from the given directory. There must be at least
	 * sixteen images in that directory and each image must have the same
	 * dimensions. The face-space bundles are also cached in that directory for
	 * speeding up further initialization.
	 *
	 * @param n
	 *            The directory where the training images are located.
	 *
	 * @throws FileNotFoundException
	 *             The <code>n</code> directory does not exist.
	 * @throws IOException
	 *             Problems reading images from the given directory or saving the
	 *             cache file (if caching is enabled)
	 * @throws IllegalArgumentException
	 *             The arguments submitted are wrong.
	 * @throws ClassNotFoundException
	 *             The cached objects are out-of-date or are not this version's
	 *             face-space objects
	 *
	 */
	public void readFaceBundles(File dirRoot) throws Exception {
		File[] files = dirRoot.listFiles(imageFilter);
		List<File> filenames = Arrays.asList(files);

		faceBundle = new FaceBundle[(files.length / MAGIC_SETNR) + 1];

		// Read each set of 16 images.
		for (int i = 0; i < faceBundle.length; i++) {
			List<File> listFiles = new ArrayList<File>();
			for (int j = 0; j < MAGIC_SETNR; j++) {
				if (filenames.size() > j + MAGIC_SETNR * i) {
					File f = filenames.get(j + MAGIC_SETNR * i);
					listFiles.add(f);
				}
			}
			faceBundle[i] = submitSet(listFiles);
		}
	}

	/**
	 * Submit a set of sixteen images in the <code>dir</code> directory and
	 * construct a face-space object. This can be done either by reading the cached
	 * objects (if there are any) or computing the {@link FaceBundle}.
	 *
	 * @param dir:
	 *            les images
	 * @param files
	 *            : String array of the names of the files (ie: "image01.jpg")
	 * 
	 */
	private FaceBundle submitSet(List<File> listFiles) throws Exception {
		File dirCache = UtilFile.DATA_CACHE;
		dirCache.mkdirs();

		String name = "cache";
		for (File f : listFiles) {
			name = name + f.getName(); // Construct the cache name
		}
		String hashStr = UtilFile.hashMd5(name);
		File fileCache = new File(dirCache, hashStr + ".cache");
		FaceBundle bundle = null;
		if (fileCache.exists() && (USE_CACHE > 0)) /* it's cached */
			bundle = readBundle(fileCache);
		else {
			bundle = computeBundle(dirCache, listFiles);
			if (USE_CACHE > 0)
				saveFaceBundle(fileCache, bundle);
		}

		return bundle;
	}

	/**
	 * Caches the face-space object serielised in file.
	 *
	 * @param file
	 * @param bundle
	 *            :The face-space object.
	 */
	private void saveFaceBundle(File file, FaceBundle bundle) throws FileNotFoundException, IOException {

		file.createNewFile();
		FileOutputStream out = new FileOutputStream(file.getAbsolutePath());
		ObjectOutputStream fos = new ObjectOutputStream(out);
		fos.writeObject(bundle);
		fos.close();
	}

	/**
	 * Read the cache object from file.
	 *
	 * @param f
	 *            File where to read from.
	 * @throws ClassNotFoundException
	 *             The cached objects are out-of-date or are not this version's
	 *             face-space objects
	 * @throws FileNotFoundException
	 *             The <code>f</code> is invalid.
	 * @throws IOException
	 *             Problems reading the data.
	 */
	private FaceBundle readBundle(File f) throws FileNotFoundException, IOException, ClassNotFoundException {

		FileInputStream in = new FileInputStream(f);
		ObjectInputStream fo = new ObjectInputStream(in);
		FaceBundle bundle = (FaceBundle) fo.readObject();
		fo.close();
		return bundle;
	}

	/**
	 * Construct the face-spaces from the given directory.
	 *
	 * @param dir
	 *            The directory where to read from.
	 * @param fileNameArray
	 *            The names of the files which to read from.
	 * @throws Exception
	 */
	private FaceBundle computeBundle(File dirCache, List<File> listFiles) throws Exception {

		List<IImage> listII = new ArrayList<>();

		int width = -1;
		int height = -1;

		for (File f : listFiles) {
			String fName = f.getName().toLowerCase();
			IImage ifile = null;
			if (fName.endsWith(".jpg") || fName.endsWith(".jpeg"))
				ifile = new JPGFile(f);
			else if (fName.endsWith("ppm") || fName.endsWith("pnm"))
				ifile = new PPMFile(f);

			listII.add(ifile);
			if (width < 0) {
				width = ifile.getWidth();
				height = ifile.getHeight();
			}
			if ((width != ifile.getWidth()) || (height != ifile.getHeight()))
				throw new IllegalArgumentException("All image files must have the same width and height!");
		}

		double[][] face_v = new double[MAGIC_SETNR][width * height];
		int i = 0;
		for (IImage iImageFile : listII) {
			face_v[i] = iImageFile.getDouble();
			i++;
		}

		FaceBundle faceBundle = EigenFaceComputation.submit(face_v, width, height, listFiles, false);
		return faceBundle;

	}

	public IImage readIImage(File f) throws Exception {

		IImage ifile = null;
		String temp = f.getName().toLowerCase();
		if (temp.endsWith(".jpg")) {
			ifile = new JPGFile(f);
		} else if (temp.endsWith(".ppm") || temp.endsWith(".pnm")) {
			ifile = new PPMFile(f);
		}
		return ifile;
	}

}

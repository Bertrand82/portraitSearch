package bg.portrait.traitemment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bg.portrait.achi.MetaImage;
import bg.portrait.util.UtilFile;

public class ParserImages {
	
	private final List<IProcessImageFile> listProcess = new ArrayList<>();
	List<File> listDirFailled = new ArrayList<File>();
	List<File> listDirOK = new ArrayList<File>();

	public void parse() {
		int i = 0;
		String sHtml = "";
		String sHtmlFailed = "";
		File dirRoot = UtilFile.DATA_ROOT;
		for (File dir1 : dirRoot.listFiles()) {
			if (dir1.isDirectory()) {
				for (File dir2 : dir1.listFiles()) {
					if (dir2.isDirectory()) {
						for (File dir3 : dir2.listFiles()) {
							if (dir3.isDirectory()) {
								for (File dir4 : dir3.listFiles()) {
									if (dir4.isDirectory()) {
										for (File dir5 : dir4.listFiles()) {
											System.out.println(i++ + " " + dir5.getName());
											File image = getImageNormalized(dir5);
											if (isOk(dir5)) {
												sHtml += "<a href=\"" + dir5.getAbsolutePath() + "\"><img src=\"" + image.getAbsolutePath() + "\"></a>\n";
												listDirOK.add(dir5);
												for(IProcessImageFile processor : this.listProcess) {
													processor.process(dir5);
												}
											} else {
												File imageOriginale = getImageOriginale(dir5);
												String type = UtilFile.getType(imageOriginale);
												sHtmlFailed += "<a href=\"" + dir5.getAbsolutePath() + "\"><img src=\"" + imageOriginale.getAbsolutePath() + "\"> "+type+"</a>\n";
												processDirFail(dir5);
											}

										}
									}
								}
							}
						}
					}
				}
			}
		}
		save(sHtml, new File("index.html"));
		save(sHtmlFailed, new File("indexFailled.html"));
	}



	private File getImageOriginale(File dir5) {
		for (File f : dir5.listFiles()) {
			if (f.getName().startsWith(dir5.getName())) {
				return f;
			}
		}
		return dir5;
	}

	private void save(String sHtml, File file) {
		try {

			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("<!DOCTYPE html><html><head><title>Images</title></head><body>\n");
			bw.write(sHtml);
			bw.write("\n </body></html>\n");
			bw.close();
			System.out.println(sHtml);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processDirFail(File dir5) {
		if (dir5.listFiles().length == 0) {
			dir5.delete();
		} else {
			listDirFailled.add(dir5);
		}

	}

	private boolean isOk(File dir) {
		return this.getImageNormalized(dir).exists();
	}

	public static File getDirImageNormalized(File dir) {
		File dirN = new File(dir, "CROP_NORMALIZED");
		return dirN;
	}

	public static File getImageNormalized(File dir) {
		File dirImage = getDirImageNormalized(dir);
		File image = new File(dirImage, "1_crop.jpg");
		return image;
	}

	public String synthese() {
		String s = " ok : " + listDirOK.size();
		s += "  fail " + listDirFailled.size();
		return s;
	}

	public List<IProcessImageFile> getListProcess() {
		return listProcess;
	}

}

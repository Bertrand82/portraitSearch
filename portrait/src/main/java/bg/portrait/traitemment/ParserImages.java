package bg.portrait.traitemment;

import java.io.File;

import bg.portrait.achi.MetaImage;



public class ParserImages {

	public void parse() {
		int i=0;
		String sHtml ="";
		File dirRoot = MetaImage.DATA_ROOT;
		for(File dir1 : dirRoot.listFiles()) {
			if (dir1.isDirectory()) {
				for(File dir2 : dir1.listFiles()) {
					if (dir2.isDirectory()){
						for(File dir3 : dir2.listFiles()) {
						if (dir3.isDirectory()) {
							for(File dir4 : dir3.listFiles()) {
								if (dir4.isDirectory()) {
									for(File dir5 : dir4.listFiles()) {
										System.out.println(i+++" "+dir5.getName());
										String d = getImageNormalized(dir5)+"\\1_crop.jpg";
										sHtml+="<a href=\""+d+"\"><img src=\""+d+"\"></a>\n";
									}
								}
							}
						}
						}
					}
				}
			}
		}
		System.out.println(sHtml);
	}
	
	private File getImageNormalized(File dir) {
		File dirN = new File(dir,"CROP_NORMALIZED");
		return dirN;
	}

}

package bg.portrait.traitemment;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

public class ProcessCopyImage implements IProcessImageFile {

	File dirDest = new File("D:\\imagesTemp");

	public ProcessCopyImage() {
	}

	@Override
	public void process(File dir) {
		try {
			this.dirDest.mkdirs();
			File dirImageNormalized = ParserImages.getDirImageNormalized(dir);
			File[] imagesNormalised = dirImageNormalized.listFiles();
			if (imagesNormalised.length == 1) {
				File fImageNormalized = imagesNormalised[0];
				File to = new File(dirDest, dir.getName() + "_" + fImageNormalized.getName());
				Files.copy(fImageNormalized, to);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

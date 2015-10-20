package as.cavd;

import java.io.File;

public class ReadFileNames {

	public static void main(String[] args) {
		File mainDir = new File("./src/captions/");
		displayContents(mainDir);
	}
	
	public static void displayContents(File dir) {
		File files[] = dir.listFiles();
		for(File file:files) {
			if(file.isDirectory())
				displayContents(file);
			else
				System.out.println(file);
		}
		// TODO Auto-generated method stub

	}

}

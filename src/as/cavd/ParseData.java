package as.cavd;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;

import com.google.common.io.Files;

public class ParseData {
	
	private static String videoId;
	
	private static File videoInfoLoc;
	
	public static void main(String[] args) {
//		String videoId = "hqkl9i7_5-0";
		File mainDir = new File("./src/captions/");
		getFileName(mainDir);
	}
	
	public static void parseVideoInfo() {
		GetTitleInfo titleInfo = new GetTitleInfo(videoId);
		String title = titleInfo.getTitle();
		String desc = titleInfo.getDesc();
		String category = titleInfo.getCategory();
//		String delims = "[.?!]+";
		/*String titleParsed[] = title.split(delims);
		String descParsed[] = desc.split(delims);
		String categoryParsed[] = category.split(delims);*/
		
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(videoInfoLoc, true)));
			out.println(title);
			out.println(desc);
			out.println(category);
			out.close();
			
//			Scanner capFile = new Scanner(new File("./src/flG17bmFius_0_en.srt"));
//			BufferedReader br = new BufferedReader(new FileReader("./src/hqkl9i7_5-0_0_en-GB.srt"));
			
			/*BufferedReader br = new BufferedReader(new FileReader(videoInfoLoc));
			String line,input[],regEx = "^([a-z]|[A-Z])+(.)*$";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher;
			List<String> wordList = new ArrayList<String>();
			while((line=br.readLine())!= null) {
				input = line.split("[.?!\r\n]+");
				for(String word: input) {
					matcher = pattern.matcher(word);
					if(matcher.matches())
						wordList.add(word);
				}
			}
			br.close();*/
			
//			String inputRaw = capFile.useDelimiter("\\Z").next();
//			capFile.close();
//			String input[] = inputRaw.split("[ \r\n]+");
			//skip all numbers
//			String regEx = "^([a-z]|[A-Z])+$";
//			Pattern pattern = Pattern.compile(regEx);
//			Matcher matcher;
//			List<String> wordList = new ArrayList<String>();
			/*for(String word: input) {
				matcher = pattern.matcher(word);
				if(matcher.matches())
					wordList.add(word);
			}*/
			
			/*PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
			for(String word:titleParsed)
				writer.println(word);
			writer.println("\n\n");
			for(String word:descParsed)
				writer.println(word);
			writer.println("\n\n");
			for(String word:categoryParsed)
				writer.println(word);
			writer.println("\n\n");
			for(String word:wordList)
				writer.println(word);
			
			writer.close();*/
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
		
	}

	public static void getFileName(File dir) {
		File[] files = dir.listFiles();
		for(File file: files) {
//			System.out.println(file.toString());
			if(file.isDirectory())
				getFileName(file);
			else {
				videoId = FilenameUtils.removeExtension(file.getName());
				videoInfoLoc = file;
//				System.out.println(videoId+"\n"+videoInfoLoc);
				parseVideoInfo();
			}
		}
	}
}

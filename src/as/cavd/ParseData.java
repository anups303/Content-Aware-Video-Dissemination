package as.cavd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseData {
	
	public static void main(String[] args) {
		String videoId = "xobUjTK-6h4";
		GetTitleInfo titleInfo = new GetTitleInfo(videoId);
		String title = titleInfo.getTitle();
		String desc = titleInfo.getDesc();
		String category = titleInfo.getCategory();
		String delims = "[ .,?!]+";
		String titleParsed[] = title.split(delims);
		String descParsed[] = desc.split(delims);
		String categoryParsed[] = category.split(delims);
		
		try {
//			Scanner capFile = new Scanner(new File("./src/flG17bmFius_0_en.srt"));
			BufferedReader br = new BufferedReader(new FileReader("./src/flG17bmFius_0_en.srt"));
			String line,input[],regEx = "^([a-z]|[A-Z])+(\\S)*$";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher;
			List<String> wordList = new ArrayList<String>();
			while((line=br.readLine())!= null) {
				input = line.split("[ .,?!\r\n]+");
				for(String word: input) {
					matcher = pattern.matcher(word);
					if(matcher.matches())
						wordList.add(word);
				}
			}
			br.close();
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
			PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
			for(String word:wordList) {
				writer.println(word);
			}
			writer.close();
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
		
	}

}

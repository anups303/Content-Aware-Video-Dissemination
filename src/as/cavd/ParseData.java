package as.cavd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
			Scanner capFile = new Scanner(new File("./src/flG17bmFius_0_en.srt"));
			String inputRaw = capFile.useDelimiter("\\Z").next();
			capFile.close();
			String input[] = inputRaw.split("[ \r\n]+");
			//skip all numbers
			String regEx = "^([a-z]|[A-Z])+$";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher;
			List<String> wordList = new ArrayList<String>();
			for(String word: input) {
				matcher = pattern.matcher(word);
				if(matcher.matches())
					wordList.add(word);
			}
			for(String word:categoryParsed) {
				System.out.println(word);
			}
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
		
	}

}

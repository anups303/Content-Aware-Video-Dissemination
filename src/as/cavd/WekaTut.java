package as.cavd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Random;

import com.google.common.io.Files;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.core.tokenizers.AlphabeticTokenizer;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.RemoveByName;
import weka.filters.unsupervised.attribute.RemoveType;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class WekaTut {
	public static void main(String[] args) throws Exception {
//		BufferedReader breader = null;
//		breader = new BufferedReader(new FileReader("D:\\Program Files\\Weka-3-6\\data\\iris.arff"));
//		
//		Instances train = new Instances(breader);
//		train.setClassIndex(train.numAttributes() -1);
//		
//		breader.close();
//		
//		NaiveBayes nB = new NaiveBayes();
//		nB.buildClassifier(train);
//		Evaluation eval = new Evaluation(train);
//		eval.crossValidateModel(nB, train, 10, new Random(1));
//		System.out.println(eval.toSummaryString("\nResults\n===========\n", true));
//		System.out.println(eval.fMeasure(1)+" "+eval.precision(1)+" "+eval.recall(1));
		
		TextDirectoryLoader loader = new TextDirectoryLoader();
		loader.setDirectory(new File("D:/Dropbox/Dropbox/TUD/Thesis_topics/Content_Aware_Video_Streaming/captions/"));
		Instances dataRaw = loader.getDataSet();
		
//		System.out.println(dataRaw.toString());
//		String[] options = {"-R first-last -W 10000 -prune-rate -1.0 -N 0 -L -stemmer weka.core.stemmers.NullStemmer -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \" \\r\\n\\t.,;:\\\'\\\"()?!\""};
		StringToWordVector stringFilter = new StringToWordVector();
		/*stringFilter.setOptions(weka.core.Utils.splitOptions(
				"-R first-last -W 1000 -prune-rate -1.0 -N 0 -L -stemmer "
				+ "weka.core.stemmers.NullStemmer -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer "
				+ "-delimiters \" \\r\\n\\t.,;:\\\'\\\"()?!\""));*/
		stringFilter.setLowerCaseTokens(true);
		stringFilter.setOutputWordCounts(true);
		stringFilter.setWordsToKeep(10000);
		stringFilter.setIDFTransform(true);
		stringFilter.setTFTransform(true);
		AlphabeticTokenizer tokenizer = new AlphabeticTokenizer();
		stringFilter.setTokenizer(tokenizer);
//		stringFilter.setOptions(options);
		/*RemoveType removeNumbers = new RemoveType();
		removeNumbers.setOptions(weka.core.Utils.splitOptions("-T numeric"));
		RemoveByName removeAttrib = new RemoveByName();
		removeAttrib.setExpression("^[0-9]+$");*/
		MultiFilter filter = new MultiFilter();
		filter.setInputFormat(dataRaw);
		filter.setFilters(new Filter[]{stringFilter});
		Instances dataFiltered = Filter.useFilter(dataRaw, filter);
//		System.out.println(dataFiltered.toString());
		PrintWriter writer = new PrintWriter("output.txt");
		writer.println(dataFiltered.toString());
		writer.close();
		
//		StringToWordVector filter = new StringToWordVector();
//		filter.setInputFormat(dataRaw);
//		Instances dataFiltered = Filter.useFilter(dataRaw, filter);
//		
//		J48 classifier = new J48();
//		classifier.buildClassifier(dataFiltered);
//		System.out.println("\nClassifier model:\n"+classifier);
	}
}

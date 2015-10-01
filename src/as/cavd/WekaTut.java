package as.cavd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
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
		loader.setDirectory(new File("D:/Dropbox/Dropbox/TUD/Thesis_topics/Content_Aware_Video_Streaming/weka/text_example/text_example/"));
		Instances dataRaw = loader.getDataSet();
		
		StringToWordVector filter = new StringToWordVector();
		filter.setInputFormat(dataRaw);
		Instances dataFiltered = Filter.useFilter(dataRaw, filter);
		
		J48 classifier = new J48();
		classifier.buildClassifier(dataFiltered);
		System.out.println("\nClassifier model:\n"+classifier);
	}
}

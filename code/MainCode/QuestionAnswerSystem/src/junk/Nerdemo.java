package junk;

import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import java.io.IOException;

public class Nerdemo {
	public static void main(String[] args) throws IOException {
		String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<?> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		String s1 = "Billy Cotton Band Show";
		System.out.println(classifier.classifyWithInlineXML(s1));
	}
}
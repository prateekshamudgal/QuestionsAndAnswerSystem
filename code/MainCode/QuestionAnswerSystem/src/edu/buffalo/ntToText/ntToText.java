package edu.buffalo.ntToText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ntToText {
	static BufferedReader br;
	static BufferedWriter bw;
	static final String read= "/home/rahul/Downloads/wikiData/long_abstracts_en.nt";
	static final String write = "/home/rahul/Downloads/wikiData/long_abstract.txt";
	
	public static void main(String[] args) throws IOException {
		try {
			br = new BufferedReader(new FileReader(read));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("unable to read input file");
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(write));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("write block");
			e.printStackTrace();
		}
		String line;
		while((line=br.readLine())!=null)
		{
			String[] data = line.split(" ", 2);
			String title = data[0];
			String abst = data[1];
			title = title.replaceFirst("<http://dbpedia.org/resource/", "");
			title = title.replaceFirst(">", "");
		//	System.out.println("title: "+title);
			abst = abst.replaceFirst("<http://dbpedia.org/ontology/abstract> \"", "");
			abst = abst.replaceFirst("\"@en .", "");
		//	System.out.println("abstract: "+abst);
			bw.write(title);
			bw.write("~");
			bw.write(abst);
			bw.newLine();
			
		}
		br.close();
		bw.close();
		System.out.println("completed!! :)");
	}

}

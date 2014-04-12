package edu.buffalo.ntToText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ShortSummaryScore {
	static final String read = "/home/rahul/Downloads/wikiData/short_abstract.txt";
	static BufferedReader br;
	static BufferedWriter bw;
	static final String write = "/home/rahul/Downloads/wikiData/short_abstarct_score_final.txt";
	
	
	private static double LogScoreCalculator(int x)
	{
	    return Math.log10(x/5);
	}
	
	public static double penalty(double val)
	{
		if(val>LogScoreCalculator(150))
		{
			val -= 2*Math.pow((val-LogScoreCalculator(150)), 1.1);
		}
		return val;
	}
	
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
		String[] split;
		
		
		double value;
		
		line=br.readLine();
		
		
		while((line=br.readLine())!=null)
		{
			split = line.split("~");
			if(split.length == 2)
			{
				int count = split[1].split("\\s+").length;
				value = LogScoreCalculator(count);
				value = penalty(value);
				value = (value/LogScoreCalculator(150))*100;
				
				if(value < 5)
					value = 5;
				//System.out.println(value);
				bw.write(split[0]);
				bw.write("~");
				bw.write(String.valueOf(value));
				bw.newLine();
			}
		
		
		}
		
		br.close();
		bw.close();
		System.out.println("Completed  :)");
	}

}

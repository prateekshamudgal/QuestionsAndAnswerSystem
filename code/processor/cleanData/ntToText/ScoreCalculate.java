package edu.buffalo.ntToText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ScoreCalculate {
	static final String read = "/home/rahul/Downloads/wikiData/links_count.txt";
	static BufferedReader br;
	static BufferedWriter bw;
	static final String write = "/home/rahul/Downloads/wikiData/External_Link_Score_final.txt";
	
	/*
	 * Log Score calculator 
	 */
	private static double LogScoreCalculator(int x)
	{
	    return Math.log10(x*5);
	}
	
	/*
	 * penalty calculation
	 */
	public static double penalty(double val)
	{
		if(val>220)
		{
			val -= 0.8* Math.pow((val-220), 1.2);
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
		split = line.split("~");
		
		while((line=br.readLine())!=null)
		{
			split = line.split("~");
			value = LogScoreCalculator(Integer.parseInt(split[1]))* 100;
			value = penalty(value);
			value = (value / 220) *100;
			
			bw.write(split[0]);
			bw.write("~");
			bw.write(String.valueOf(value));
			bw.newLine();
			
			
			
		}
		
		br.close();
		bw.close();
		System.out.println("Completed  :)");
	}
	
	
	


}

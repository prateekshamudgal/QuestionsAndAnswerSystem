package edu.buffalo.ntToText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class countLinks {
	static final String read = "/home/rahul/Downloads/wikiData/external_links.txt";
	static BufferedReader br;
	static BufferedWriter bw;
	static final String write = "/home/rahul/Downloads/wikiData/links_count.txt";
	
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
		String previous = "";
		String line;
		String[] split;
		int count = 1;
		line=br.readLine();
		line=br.readLine();
		split = line.split("~");
		previous = split[0];
		while((line=br.readLine())!=null)
		{
			split = line.split("~");
			
			if(!previous.equals(split[0]))
			{
				bw.write(previous);
				System.out.println(previous + "~" + count);
				bw.write("~");
				bw.write(String.valueOf(count));
				bw.newLine();
				previous  = split[0];
				count = 1;
			}
			else
				count++;
		}
		bw.write(previous);
		System.out.println(previous + "~" + count);
		bw.write("~");
		bw.write(String.valueOf(count));
		bw.newLine();
		br.close();
		bw.close();
		System.out.println("Completed  :)");
	}
	
	
	

}

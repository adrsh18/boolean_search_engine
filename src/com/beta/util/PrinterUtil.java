package com.beta.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import com.beta.index.Posting;
import com.beta.index.PostingsList;

public class PrinterUtil {

	public static void printPostings(File outputFile, String term, PostingsList postingsList) {
		PrintWriter writer = null;
		if(postingsList == null) {
			postingsList = new PostingsList();
		}
		try{
			writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile, true),StandardCharsets.UTF_8));
			writer.println("GetPostings");
			writer.println(term);
			String result = "Postings list: ";
			if(postingsList.size() == 0) {
				result +="empty";
			}
			for(Posting p : postingsList) {
				result +=p.getDocumentID()+" ";
			}
			writer.println(result);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try{ 
				writer.close();
			}catch(Exception e) {}
		}
		
	}
	
	public static void printResults(File outputFile, String operation, String [] terms, List<Integer> resultList, int comparisons) {
		PrintWriter writer = null;
		if(resultList == null) {
			resultList = Collections.emptyList();
		}
		
		try{
			writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile, true),StandardCharsets.UTF_8));
			writer.println(operation);
			String result = "";
			for(String term : terms) {
				result += term+" ";
			}
			writer.println(result);
			
			result = "Results: ";
			if(resultList.size() == 0) {
				result += "empty";
			}
			for(Integer i: resultList) {
				result += i+" ";
			}
			writer.println(result);
			writer.println("Number of documents in results: "+resultList.size());
			writer.println("Number of comparisons: "+comparisons);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try{ 
				writer.close();
			}catch(Exception e) {}
		}
		
	}

}

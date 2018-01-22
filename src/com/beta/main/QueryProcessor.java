package com.beta.main;

import java.io.File;
import java.util.ArrayList;

import com.beta.index.InvertedIndex;
import com.beta.index.PostingsList;
import com.beta.util.AlgorithmResult;
import com.beta.util.Algorithms;
import com.beta.util.PrinterUtil;

public class QueryProcessor {

	public static final String TAATAND = "TaatAnd";
	public static final String TAATOR = "TaatOr";
	public static final String DAATAND = "DaatAnd";
	public static final String DAATOR = "DaatOr";
	
	private String [] terms;
	
	public QueryProcessor(String [] terms) {
		// TODO Auto-generated constructor stub
		this.terms = terms;
	}
	
	public QueryProcessor(ArrayList<String> terms) {
		try{
			this.terms = (String []) terms.toArray();
		}catch(Exception e) {
			e.printStackTrace();
			this.terms = null;
		}
	}
	
	public ArrayList<PostingsList> getPostings(File outputFile) {
		ArrayList<PostingsList> result = new ArrayList<PostingsList>();
		InvertedIndex mainIndex = InvertedIndex.getIndex();
		for(String term : terms) {
			//System.out.println("Processing term : "+term+"; Length: "+term.length());
			PostingsList postingsList = mainIndex.getPosting(term);
			PrinterUtil.printPostings(outputFile, term, postingsList);
			if(postingsList == null) {
				postingsList = new PostingsList();
			}
			result.add(postingsList);
		}
		return result;
	}

    public void TaatAnd(File outputFile, ArrayList<PostingsList> allPostings, int numDocs) {
    	//Calls the actual algorithm
    	AlgorithmResult aResult = Algorithms.TaatAnd(allPostings, numDocs);
    	PrinterUtil.printResults(outputFile, QueryProcessor.TAATAND, terms, aResult.getResult(), aResult.getComparisons());
    }
    
    public void TaatOr(File outputFile, ArrayList<PostingsList> allPostings, int numDocs) {
    	//Calls the actual algorithm
    	AlgorithmResult aResult = Algorithms.TaatOr(allPostings, numDocs);
    	PrinterUtil.printResults(outputFile, QueryProcessor.TAATOR, terms, aResult.getResult(), aResult.getComparisons());
    }
    
    public void DaatAnd(File outputFile, ArrayList<PostingsList> allPostings) {
    	//Calls the actual algorithm
    	AlgorithmResult aResult = Algorithms.DaatAnd(allPostings);
    	PrinterUtil.printResults(outputFile, QueryProcessor.DAATAND, terms, aResult.getResult(), aResult.getComparisons());
    }
    
    public void DaatOr(File outputFile, ArrayList<PostingsList> allPostings) {
    	//Calls the actual algorithm
    	AlgorithmResult aResult = Algorithms.DaatOr(allPostings);
    	PrinterUtil.printResults(outputFile, QueryProcessor.DAATOR, terms, aResult.getResult(), aResult.getComparisons());
    }
}

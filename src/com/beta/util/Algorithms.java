package com.beta.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.beta.index.Posting;
import com.beta.index.PostingsList;

public class Algorithms {

	public static AlgorithmResult TaatAnd(List<PostingsList> allPostings, int numDocs) {
		
		/*
		 * LOGIC:
		 * Initialize scores for all docs to 0 (zero)
		 * Go through each postings list
		 * For each document found, update score (just 1 in this case/project)
		 * All documents with score > [Number of terms in query], add to the final result
		 */
		int comparisons = 0;
		if(allPostings == null) {
			allPostings = Collections.emptyList();
		}
		ArrayList<Integer> result = new ArrayList<Integer>();
		int [] allDocs = new int[numDocs];
		for(PostingsList postingsList : allPostings) {
			for(Posting p: postingsList) {
				comparisons++;
				allDocs[(int)p.getDocumentID()]++;
			}
		}
		
		for(int i = 0; i < allDocs.length; i++) {
			if(allDocs[i] == allPostings.size()) {
				result.add(i);
			}
		}
		return new AlgorithmResult(result, comparisons);
	}
	
	public static AlgorithmResult TaatOr(List<PostingsList> allPostings, int numDocs) {
		
		/*
		 * LOGIC:
		 * Initialize scores for all docs to 0 (zero)
		 * Go through each postings list
		 * For each document found, update score (just 1 in this case/project)
		 * All documents with score > 0, add to the final result
		 */
		int comparisons = 0;
		if(allPostings == null) {
			allPostings = Collections.emptyList();
		}
		ArrayList<Integer> result = new ArrayList<Integer>();
		int [] allDocs = new int[numDocs];
		for(PostingsList postingsList : allPostings) {
			for(Posting p: postingsList) {
				comparisons++;
				allDocs[(int)p.getDocumentID()]++;
			}
		}
		
		for(int i = 0; i < allDocs.length; i++) {
			if(allDocs[i] > 0) {
				result.add(i);
			}
		}
		return new AlgorithmResult(result, comparisons);
	}
	
	public static AlgorithmResult DaatAnd(List<PostingsList> allPostings) {
		
		/*
		 * LOGIC:
		 * while(None of the postings lists reaches NULL/empty)
		 * {
		 * 		Find minimum and maximum
		 * 		if( minimum == maximum) 
		 * 		{
		 * 			All postings lists point to same Doc ID
		 * 			Add Doc ID to result and Advance all pointers by one
		 * 		} else {
		 * 			Advance all postings list until they all match up to maximum
		 * 		}
		 * }
		 */
		ArrayList<Integer> result = new ArrayList<Integer>();
		int comparisons = 0;
		
		int k = (allPostings == null) ? 0 : allPostings.size();
		
		Posting [] kList = new Posting[k];
		
		for(int i = 0; i < k; i++) {
			kList[i] = allPostings.get(i).getFirst();
		}
		
		while(!isAnyNull(kList)) {
			int minIndex = 0;
			int maxIndex = k-1;
			
			for(int i = 0; i < k; i++) {
				if(kList[i].getDocumentID() < kList[minIndex].getDocumentID()) {
					minIndex = i;
				}
				if(kList[i].getDocumentID() > kList[maxIndex].getDocumentID()) {
					maxIndex = i;
				}
				comparisons++;
			}
			if(kList[minIndex].getDocumentID() == kList[maxIndex].getDocumentID()) {
				result.add((int) kList[minIndex].getDocumentID());
				advanceAllPostings(kList);
				comparisons++;
			} else {
				comparisons += advanceMultiplePostings(kList, maxIndex);
			}
			
		}
		return new AlgorithmResult(result, comparisons);
	}
	
	private static boolean isAnyNull(Posting [] kList) {
		
		/*
		 * Return true if any of the pointers reaches NULL
		 */
		boolean result = false;
		for(Posting p : kList){
			if(p == null){
				result = true;
				break;
			}
			
		}
		return result;
	}
	
	private static void advanceAllPostings(Posting [] kList) {
		/*
		 * Advances all pointers by one jump
		 */
		for(int i = 0; i < kList.length; i++) {
			kList[i] = kList[i].getNext();
		}
	}
	
	private static int advanceMultiplePostings(Posting [] kList, int maxIndex) {
		/*
		 * Advances all pointers that point to elements less than the maximum
		 * Makes use of Skip Pointers
		 */
		int comparisons = 0;
		for(int i =0; i < kList.length; i++) {
			while(i != maxIndex && kList[i] != null && kList[i].getDocumentID() < kList[maxIndex].getDocumentID()) {
				comparisons++;
				if(kList[i].getSkipPointer() != null) {
					comparisons++;
					if(kList[i].getSkipPointer().getDocumentID() < kList[maxIndex].getDocumentID()) {
						kList[i] = kList[i].getSkipPointer();
					} else {
						kList[i] = kList[i].getNext();
					}
					//System.out.println("Skip Advancing "+kList[i]);
					
				} else {
					//System.out.println("Advancing "+kList[i]);
					kList[i] = kList[i].getNext();
				}
			}
		}
		return comparisons;
	}
	
	public static AlgorithmResult DaatOr(List<PostingsList> allPostings) {
		/* LOGIC:
		 * 
		 * while(All Postings Lists are NOT empty/null)
		 * {
		 * 		Find the minimum among all postings lists and add it to result
		 * 		If multiple minima, add only once and advance the duplicates
		 * }
		 */
		
		
		int comparisons = 0;
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		int k = (allPostings == null) ? 0 : allPostings.size();
		
		Posting [] kList = new Posting[k];
		
		for(int i = 0; i < k; i++) {
			kList[i] = allPostings.get(i).getFirst();
		}
		
		NullCheckResult nullCheckResult;
		while((nullCheckResult = numNull(kList)).getNumNulls() != k) {
			int minIndex = nullCheckResult.getLastNonNullIndex();
			
			for(int i = 0; i < k; i++) {
				comparisons++;
				if(kList[i] != null && kList[i].getDocumentID() < kList[minIndex].getDocumentID()) {
					minIndex = i;
				}
			}
			
			result.add((int)kList[minIndex].getDocumentID());
			long lastAddedDocumentId = kList[minIndex].getDocumentID();
			for(int i = 0; i < k; i++) {
				if(kList[i] != null && kList[i].getDocumentID() == lastAddedDocumentId) {
					kList[i] = kList[i].getNext();
				}
			}
			
		}
		
		return new AlgorithmResult(result, comparisons);
	}
	
	private static NullCheckResult numNull(Posting [] kList) {
		int result = 0;
		int nonNullIndex = 0;
		for(int i = 0; i < kList.length; i++){
			if(kList[i] == null) {
				result++;
			} else {
				nonNullIndex = i;
			}
		}
		return new NullCheckResult(result, nonNullIndex);
	}
	
}

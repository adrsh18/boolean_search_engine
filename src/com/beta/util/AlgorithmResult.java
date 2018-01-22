package com.beta.util;

import java.util.ArrayList;

public class AlgorithmResult {
	
	private ArrayList<Integer> result;
	private int comparisons;

	public AlgorithmResult(ArrayList<Integer> result, int comparisons) {
		this.result = result;
		this.comparisons = comparisons;
	}

	public ArrayList<Integer> getResult() {
		return result;
	}

	public void setResult(ArrayList<Integer> result) {
		this.result = result;
	}

	public int getComparisons() {
		return comparisons;
	}

	public void setComparisons(int comparisons) {
		this.comparisons = comparisons;
	}
	
}

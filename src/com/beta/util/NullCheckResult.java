package com.beta.util;

public class NullCheckResult {

	private int numNulls;
	private int lastNonNullIndex;
	
	public NullCheckResult(int numNulls, int lastNonNullIndex) {
		this.numNulls = numNulls;
		this.lastNonNullIndex = lastNonNullIndex;
	}

	public int getNumNulls() {
		return numNulls;
	}

	public void setNumNulls(int numNulls) {
		this.numNulls = numNulls;
	}

	public int getLastNonNullIndex() {
		return lastNonNullIndex;
	}

	public void setLastNonNullIndex(int lastNonNullIndex) {
		this.lastNonNullIndex = lastNonNullIndex;
	}
}

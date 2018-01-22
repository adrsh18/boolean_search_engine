package com.beta.index;

public class Posting {

	private long documentID;
	private long frequency;
	private Posting skipPointer;
	private Posting next;
	
	public Posting getNext() {
		return next;
	}
	public void setNext(Posting next) {
		this.next = next;
	}
	public Posting(int documentID, int frequency) {
		// TODO Auto-generated constructor stub
		this.documentID = documentID;
		this.frequency = frequency;
		this.skipPointer = null;
	}
	public Posting(int documentID) {
		this.documentID = documentID;
		this.frequency = -1;
		this.skipPointer = null;
	}

	public long getDocumentID() {
		return documentID;
	}

	public void setDocumentID(int documentID) {
		this.documentID = documentID;
	}

	public long getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public Posting getSkipPointer() {
		return skipPointer;
	}

	public void setSkipPointer(Posting skipPointer) {
		this.skipPointer = skipPointer;
	}
	@Override
	public String toString() {
		if(skipPointer == null) {
			return "[" + documentID + "," + frequency + ", null]";
		} else {
			return "[" + documentID + "," + frequency + ", "+skipPointer.getDocumentID() + "]";
		}
	}
	

}

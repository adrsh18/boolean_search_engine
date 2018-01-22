package com.beta.index;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PostingsList implements Iterable<Posting>{

	private Posting first;
	private Posting last;
	private int count;
	
	public PostingsList() {
		this.first = null;
		this.last = null;
		this.count = 0;
	}
	
	public void add(Posting posting) {
		if(posting == null) {
			throw new NullPointerException("Failed to add null Posting to PostingsList");
		}
		if(!this.isEmpty()) {
			Posting prev = this.getLast();
			prev.setNext(posting);
			last = posting;
		} else {
			last = posting;
			first = last;
		}
		count++;
	}
	
	public void addAll(PostingsList newPostingsList) {
		int xcount = 0;
		Posting a = this.getFirst();
		Posting b = newPostingsList.getFirst();
		Posting dummyHead, curr; dummyHead = new Posting(-1); curr = dummyHead;
		while(a !=null && b!= null) {
	        if(a.getDocumentID() < b.getDocumentID()) { curr.setNext(a); a = a.getNext(); }
	        else if(a.getDocumentID() == b.getDocumentID()) { curr.setNext(a); a = a.getNext(); b = b.getNext(); xcount++;}
	        else { curr.setNext(b); b = b.getNext(); }
	        curr = curr.getNext();
	    }
	    curr.setNext((a == null) ? b : a);
	    this.setFirst(dummyHead.getNext());
	    if(curr.getNext() == null) {
	    	this.setLast(curr);
	    }else {
	    	this.setLast(curr.getNext());
	    }
	    this.count = this.count + newPostingsList.getCount() - xcount;
	}
	
	public Iterator<Posting> iterator() {
		return new PostingsListIterator();
	}
	
	public Posting getFirst() {
		return first;
	}

	public void setFirst(Posting first) {
		this.first = first;
	}

	public int getCount() {
		return count;
	}
	
	public Posting getLast() {
		return last;
	}

	public void setLast(Posting last) {
		this.last = last;
	}

	public boolean isEmpty() {
		return first == null;
	}
	
	public int size() {
		return count;
	}
	
	public void initializeSkips() {
		int skips = (int) Math.floor(Math.sqrt(count));
		if(count <= 1 || skips == count) {
			return;
		}
		Posting current = first;
		Posting skip = current;
		while(current.getNext() != null) {
			skip = this.getPostingAfterSkip(current, skips);
			current.setSkipPointer(skip);
			current = skip;
		}
		
	}
	
	private Posting getPostingAfterSkip(Posting p, int skips) {
		int k = skips;
		while(k > 0 && p.getNext() != null) {
			p = p.getNext();
			k--;
		}
		return p;
	}
	
	@Override public String toString() {
        StringBuilder s = new StringBuilder();
        for (Posting item : this)
            s.append(item + " ");
        return s.toString();
    }
	
	private class PostingsListIterator implements Iterator<Posting> {
		private Posting current = first;
		
		public Posting next() {
			if(!hasNext()) {throw new NoSuchElementException(); }
			Posting result = current;
			current = current.getNext();
			return result;
		}
		
		public boolean hasNext() { return current != null; }
		
		public void remove() { throw new UnsupportedOperationException(); }
	}
}

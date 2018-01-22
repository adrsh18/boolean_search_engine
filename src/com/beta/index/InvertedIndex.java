package com.beta.index;
import java.util.HashMap;
import java.util.Set;

public class InvertedIndex {
	
	static InvertedIndex singletonIndex;
	private HashMap<String, PostingsList> dictionary;

	private InvertedIndex() {
		// TODO Auto-generated constructor stub
		dictionary = new HashMap<String,PostingsList>();
	}
	public static InvertedIndex getIndex() {
		if(singletonIndex == null) {
			singletonIndex = new InvertedIndex();
		}
		return singletonIndex;
	}
	public void addPosting(String term, Posting posting) {
		PostingsList postingsList = dictionary.get(term);
		if(postingsList == null) {
			postingsList = new PostingsList();
			dictionary.put(term, postingsList);
		}
		postingsList.add(posting);
	}
	public void addPosting(String term, int documentID, int frequency) {
		Posting posting = new Posting(documentID, frequency);
		this.addPosting(term, posting);
	}
	public void addPosting(String term, PostingsList newPostingsList){
		PostingsList currentPostingsList = dictionary.get(term);
		if(currentPostingsList == null) {
			dictionary.put(term, newPostingsList);
		}else {
			currentPostingsList.addAll(newPostingsList);
		}
	}
	public PostingsList getPosting(String term) {
		PostingsList postingsList = dictionary.get(term);
		return postingsList;
	}
	public HashMap<String, PostingsList> getDictionary() {
		return dictionary;
	}
	public int numTermCount() {
		return dictionary.size();
	}

	public void initializeSkips() {
		Set<String> terms = this.getDictionary().keySet();
		for(String term : terms) {
			PostingsList postingsList = this.getDictionary().get(term);
			postingsList.initializeSkips();
		}
	}

}

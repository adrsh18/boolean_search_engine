package com.beta.main;

public class Main {

	public static void main(String[] args) {
		
		ProjectControl control = ProjectControl.getControlInstance();
		control.initialize(args);
		control.buildIndex();
		control.setUpSkips();
		control.processInput();
		//System.out.println("Number of Docs: "+control.numDocs+"; Number of Terms: "+InvertedIndex.getIndex().getDictionary().size());
				
	}
	
}

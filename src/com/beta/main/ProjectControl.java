package com.beta.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import com.beta.index.InvertedIndex;
import com.beta.index.Posting;
import com.beta.index.PostingsList;

public class ProjectControl {

	static private ProjectControl control = null;
	public  int numDocs;
	private File inputFile;
	private File outputFile;
	private IndexReader reader;
	
	public static ProjectControl getControlInstance() {
		if(control == null) {
			control = new ProjectControl();
		}
		return control;
	}
	
	private ProjectControl() {
		// TODO Auto-generated constructor stub
		inputFile = null;
		outputFile = null;
		reader = null;
	}
	
	public void initialize(String[] args) {
		try{
			
			if(args.length < 3) {
				System.out.println("Insufficient Arguments!\nUsage: java -jar UBITName_project2.jar path_of_index output.txt input.txt");
				System.exit(0);
			}
			
			String pathOfIndex = args[0];
			String oFile = args[1];
			String iFile = args[2];
			
			System.out.println("Found args: "+pathOfIndex+", "+oFile+", "+iFile);
			
			Path indexDirPath = Paths.get(pathOfIndex);
			//Path outputFilePath = Paths.get(oFile);
			Path inputFilePath = Paths.get(iFile);
			
			if(!Files.exists(indexDirPath)) {
				System.out.println("Directory does not exist : "+pathOfIndex);
				System.exit(-1);
			}
			if(!Files.exists(inputFilePath)) {
				System.out.println("File does not exist : "+inputFile);
				System.exit(-1);
			}
			this.setInputFile(new File(iFile));
			this.setOutputFile(new File(oFile));
			
			Directory dir = FSDirectory.open(indexDirPath);
			this.setReader(DirectoryReader.open(dir));
			this.numDocs = this.getReader().numDocs();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void buildIndex() {
		
		/*
		 * Builds Inverted Index
		 */
		InvertedIndex mainIndex = InvertedIndex.getIndex();
		long start = System.currentTimeMillis();
		try {
			
			Fields allFields = MultiFields.getFields(reader);
			
			for(String field : allFields) {
				if(field.equals("id")||field.equals("_version_")){
					continue;
				}
				//System.out.println(field);
				Terms terms = allFields.terms(field);
				TermsEnum termsEnum = terms.iterator();
				BytesRef text;
				
				while((text = termsEnum.next()) != null) {
					if(termsEnum.seekExact(text)) {
						String term = text.utf8ToString();
						PostingsList postingsList = new PostingsList();
						PostingsEnum postingsEnum = null;
						postingsEnum = termsEnum.postings(postingsEnum, PostingsEnum.FREQS);
						int doc;
						while((doc = postingsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
							postingsList.add(new Posting(doc,postingsEnum.freq()));
						}
						mainIndex.addPosting(term, postingsList);
					}
					
				}
				
			}
			
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		System.out.println("Time to go through Postings Lists: "+ (end-start)/1000.0);
	}
	
	public void setUpSkips() {
		/*
		 * Initializes skip pointers for all postings lists in Inverted Index
		 */
		InvertedIndex mainIndex = InvertedIndex.getIndex();
		mainIndex.initializeSkips();
	}
	
	public void processInput() {
		/*
		 * Processes input file line by line
		 * Asks QueryProcessor to get to work on each set of query terms
		 */
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.getInputFile()), StandardCharsets.UTF_8))) {
			for(String line; (line = br.readLine()) != null; ) {
				String [] terms = line.split("\\s+");
				//System.out.println(line);
				QueryProcessor processor = new QueryProcessor(terms);
				ArrayList<PostingsList> allPostings = processor.getPostings(this.getOutputFile());
				processor.TaatAnd(this.getOutputFile(), allPostings, this.numDocs);
				processor.TaatOr(this.getOutputFile(), allPostings, this.numDocs);
				processor.DaatAnd(this.getOutputFile(), allPostings);
				processor.DaatOr(this.getOutputFile(), allPostings);
			}
		}catch(IOException ioe) {
			System.out.println("Failed to read input file!");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public File getInputFile() {
		return inputFile;
	}

	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public IndexReader getReader() {
		return reader;
	}

	public void setReader(IndexReader reader) {
		this.reader = reader;
	}

}

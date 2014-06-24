package com.custom.analyzer;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;


public class CnTokenizer extends Tokenizer {
	private static TernarySearchTrie dict = new TernarySearchTrie("SDIC.txt");
	//private TermAttribute termAtt; //Term attribute
	protected CnTokenizer(Reader input) {
		super(input);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean incrementToken() throws IOException {
		//final char[] termBuffer = termAtt.termBuffer();
		System.out.println("I'm increment Token");
		return false;
	}

}

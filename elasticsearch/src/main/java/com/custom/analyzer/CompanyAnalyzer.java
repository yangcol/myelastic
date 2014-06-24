package com.custom.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.util.Version;

public class CompanyAnalyzer extends Analyzer{

	@Override
	protected TokenStreamComponents createComponents(String arg0, Reader arg1) {
		 Tokenizer source = new CnTokenizer(arg1);
	     TokenStream filter = new LengthFilter(Version.LUCENE_47, source, 3, Integer.MAX_VALUE);
	     filter = new CompanyFilter(filter);
	     return new TokenStreamComponents(source, filter);
	}
	
}

package com.custom.analyzer;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

public class CompanyFilter extends TokenFilter{

	protected CompanyFilter(TokenStream input) {
		super(input);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean incrementToken() throws IOException {
		System.out.println("I'm in company Token");
		while (input.incrementToken()) {
			System.out.println("token:" + reflectAsString(true));
		}
		return false;
	}

}

package com.chinese.match.cuttinggraph;

public class CnToken {

	public String termText;
	public int start;
	public int end;
	public String type; //词性
	public double logProb;
	
	public CnToken(int vertexForm, int vertexTo, String word) {
		start = vertexForm;
		end = vertexTo;
		termText = word;
	}
}

package com.hint.auto;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CustomDictionary {
	
	private boolean sorted;
	
	public CustomDictionary(String path) throws IOException {
		dicArray = new ArrayList<Data>();
		sorted = false;
		LoadBaseDict(path, new DictParser());
		sortDict();
	}
	
	private void Load(String path) {
		
	}
	
	
	public boolean isSorted() {
		return this.sorted;
	}
	
	public ArrayList<Data> getData() {
		return this.dicArray;
	}
	
	private ArrayList<Data> dicArray;
	
	private void sortDict() {
		if (sorted) {
			return;
		}
		
		Collections.sort(dicArray, Data.BY_WORD);
		sorted = true;
	}
	
	private void LoadBaseDict(String path, DictParser dp) throws IOException {
		// Check input path
		if (null == path) {
			throw new IllegalArgumentException("Input path should not be null");
		}
		
		if (null == dicArray) {
			throw new InternalError("Dict should not be null");
		}
		
		// Only Basic dict
		BufferedReader br = new BufferedReader(new FileReader(path));
		
		String line = null;
		String[] res = null;
		Data data = null;
		while (null != (line = br.readLine())) {
			data = dp.parseLine(line);
			if (null != data) {
				dicArray.add(data);
			}
		}
	}
	
	public static void main(String[] args) {
		String a = "123  32, gogo";
		String[] b = a.split("[\\s,]");
		for (String c: b) {
			System.out.println(c);
		}
	}
}

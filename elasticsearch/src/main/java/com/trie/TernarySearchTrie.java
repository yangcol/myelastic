package com.trie;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TernarySearchTrie {
	
	
	void outputBalanced(BufferedWriter fp, ArrayList<String> k, int offset,
			int n) throws IOException {
		int m;
		if (n < 1) {
			return;
		}

		m = n >> 1;

		String item = k.get(m + offset);
		fp.write(item);
		fp.write('\n');
		fp.flush();
		outputBalanced(fp, k, offset, m); // output left part
		outputBalanced(fp, k, offset + m + 1, n - m - 1); // output right part
	}
	
	public static void main(String[] args) throws IOException {
		TernarySearchTrie tsn = new TernarySearchTrie();
		BufferedWriter fp = new BufferedWriter(new FileWriter("dict.txt"));
		ArrayList<String> k = new ArrayList<String>();
		k.add("is");
		k.add("as");
		k.add("be");
		k.add("by");
		k.add("he");
		k.add("in");
		k.add("is");
		k.add("of");
		Collections.sort(k);
		tsn.outputBalanced(fp, k , 0, k.size());
	}
}

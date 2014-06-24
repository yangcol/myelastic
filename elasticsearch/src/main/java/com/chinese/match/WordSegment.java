package com.chinese.match;

import java.util.ArrayList;
import java.util.List;

public class WordSegment {
	
	// private Dictionary _dict;
	public WordSegment() {
	}

	List<String> matchLongSegment(TernaryTree idc, String text) {
		List<String> res = new ArrayList<String>();
		int offset = 0;
		String singleWord = null;
		while (offset < text.length()) {
			singleWord = idc.matchLong(text, offset);
			if (null == singleWord) {
				singleWord = text.substring(offset, offset + 1);
			}
			res.add(singleWord);
			offset += singleWord.length();
		}
		return res;
	}
	
	public static void main(String[] args) {
		TernaryTree idc = new TernaryTree();
		String[] words = {"大学生", "活动", "中心", "大", "大学"};
		for (String word: words) {
			idc.Insert(word);
		}
		WordSegment ws = new WordSegment();
		List<String> result = ws.matchLongSegment(idc, "小人爱吃米");
		List<String> result2 = ws.matchLongSegment(idc, "一个大学生村官去了活动中心");
		for (String res: result) {
			System.out.println(res);
		}
		
		for (String res: result2) {
			System.out.println(res);
		}
		
	}
}

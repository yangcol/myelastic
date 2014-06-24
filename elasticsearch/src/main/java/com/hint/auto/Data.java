package com.hint.auto;

import java.util.Comparator;

public class Data{
	public static final Comparator<Data> BY_WORD = new ByWord();
	public static final Comparator<Data> BY_SEARCH = new BySearch();
	public static final Comparator<Data> BY_HOT = new ByHot();
	
	/**
	 * word that contains
	 */
	public String word;
	
	/**
	 * Search Count
	 */
	public int s_count;
	
	/**
	 * Hot score
	 */
	public int h_score;
	
	String[] aliases;
	
	public Data(String word, int s_count, int h_score, String[] aliases) {
		this.word = word;
		this.s_count = s_count;
		this.h_score = h_score;
		this.aliases = aliases;
	}
	
	
	public Data(Data b) {
		this.word = b.word;
		this.s_count = b.s_count;
		this.h_score = b.h_score;
		if (null == b.aliases) {
			this.aliases = null;
		} else {
			this.aliases = b.aliases.clone();
		}
	}
	
	/**
	 * Set hot score, Thread Safe
	 * @param score
	 */
	public synchronized void setHotScore(int score) {
		this.h_score = score;
	}
	
	/**
	 * Get score
	 * @param score
	 * @return Hot Score
	 */
	public int getHotScore(int score) {
		return this.h_score; 
	}
	
	public synchronized void incSearchCount() {
		s_count+=1;
	}
	
	public int getSearchCount() {
		return this.s_count;
	}

	
	private static class ByWord implements Comparator<Data> {
        public int compare(Data v, Data w) {
           return v.word.compareTo(w.word);
        }
    }
	
	private static class BySearch implements Comparator<Data> {
		public int compare(Data v, Data w) {
			return v.s_count - w.s_count;
		}
	}
	
	private static class ByHot implements Comparator<Data> {
		public int compare(Data v, Data w) {
			return v.h_score - w.h_score;
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("word: ");
		sb.append(word);
		sb.append(", s_count:");
		sb.append(String.valueOf(s_count));
		sb.append(", h_score:");
		sb.append(String.valueOf(this.h_score));
		sb.append(", aliases");
		for (String s: this.aliases) {
			sb.append(s + ",");
		}
	
		return sb.toString();
	}
}
	
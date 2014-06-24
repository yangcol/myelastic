package com.hint.auto;

import java.util.Comparator;

import com.trie.TrieNode.NodeType;


public class Node {
	public static final Comparator<Node> BY_HOT = new ByHOT();
	Character Word;
	Node leftChild, centerChild, rightChild;
	NodeType type;
	Data data;
	
	Node(Character ch, NodeType type) {
		this.Word = ch;
		this.type = type;
	}
	
	public String toString() {
		return Word.toString();
	}
	
	public void setData(Data data) {
		this.data = data;
	}
	
	public Data getData() {
		return new Data(this.data);
	}
	
	private static class ByHOT implements Comparator<Node> {
		public int compare(Node o1, Node o2) {
			return o1.data.h_score - o2.data.h_score;
		}
		
	}
}

package com.chinese.match;

import com.trie.TrieNode.NodeType;


public class Node {
	Character Word;
	Node leftChild, centerChild, rightChild;
	NodeType type;
	String data;
	
	Node(Character ch, NodeType type) {
		this.Word = ch;
		this.type = type;
	}
	
	public String toString() {
		return Word.toString();
	}
}

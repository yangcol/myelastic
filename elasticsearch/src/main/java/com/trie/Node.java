package com.trie;

import com.trie.TrieNode.NodeType;


public class Node {
	Character Word;
	Node leftChild, centerChild, rightChild;
	NodeType type;
	
	Node(Character ch, NodeType type) {
		this.Word = ch;
		this.type = type;
	}
	
	public String toString() {
		return Word.toString();
	}
}

package com.trie;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
	
	private NodeType type;
	public Character nodeChar;
	
	public Map<Character, TrieNode> childNode = new HashMap<Character, TrieNode>();
	

	public TrieNode(char word) {
		this.nodeChar = word;
		this.type = NodeType.UNCOMPLETED;
	}
	
	public TrieNode(TrieNode tn) {
		this.nodeChar = tn.nodeChar;
		this.childNode.putAll(tn.childNode);
		this.type = tn.type;
	}
	
	public void add(TrieNode child) throws Exception {	
		if (!childNode.containsKey(child.nodeChar)) {
			TrieNode newNode = new TrieNode(child);
			childNode.put(child.nodeChar, newNode);
		} else {
			throw new Exception("Already exits");
		}
	}
	
	public boolean childEmpty() {
		return childNode.isEmpty();
	}
	
	public void add(Character c) {
		if (!childNode.containsKey(c)) {
			childNode.put(c, new TrieNode(c));
		}
	}
	
	public void delete(char c) throws Exception {
		if (!childNode.containsKey(c)) {
			childNode.remove(c);
		} else {
			throw new Exception("No such key");
		}
	}
	
	public void setType(NodeType type) {
		this.type = type;
	}
	
	public NodeType getType() {
		return this.type;
	}
	
	public enum NodeType {
		ROOT,
		COMPLETED,
		UNCOMPLETED
	}
}

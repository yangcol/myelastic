package com.trie;

import java.util.Map;

import com.trie.TrieNode.NodeType;


public class Trie {

	private TrieNode _root;

	
	public Trie()
	{
		_root = new TrieNode(' ');
		_root.setType(NodeType.ROOT);
	}
	
	public void add(String word) {
		if (null == word) {
			throw new IllegalArgumentException("Input word empty");
		}
		
		TrieNode current = _root;
		for (Character c: word.toCharArray()) {
			current.add(c);
			current = current.childNode.get(c);
		}
		current.setType(NodeType.COMPLETED);
	}
	
	public void delete(String word) {
		if (null == word) {
			throw new IllegalArgumentException("Input word empty");
		}	
	}
	
	public void show() {
		visit(_root, new StringBuffer(""));
	}
	
	private void visit(TrieNode start, StringBuffer sb) {
		if (NodeType.COMPLETED == start.getType()) {
			System.out.println(sb);
		}
		
		if (start.childEmpty()) {
			return;
		}
		
		for (Map.Entry<Character, TrieNode>subNode: start.childNode.entrySet()) {
			StringBuffer newsb = new StringBuffer(sb.toString());
			visit(subNode.getValue(), newsb.append(subNode.getValue().nodeChar));
		}
	}
	
	public static void main(String[] args) {
		Trie t = new Trie();
		t.add("hello");
		t.add("hell");
		t.add("hil");
		t.add("world");
		t.visit(t._root, new StringBuffer(""));
	}
}

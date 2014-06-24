package com.myown.ahocorasick;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class AhoCorasick {
	private final class TreeNode {
		private char _char;
		private TreeNode _parent;
		private TreeNode _failure;
		private ArrayList<String> _results;	//存储模式串的数组变量
		private TreeNode[] _transitionAr;
		private Hashtable<Character, TreeNode> _transHash;
		public TreeNode(TreeNode parent, char c) {
			_char = c;
			_parent = parent;
			_results = new ArrayList<String>();
			_transitionAr = new TreeNode[] {};
			_transHash = new Hashtable<Character, TreeNode>();
		}
		
		//将模式中不在_results中的模式串添加进来
		public void addResult(String result) {
			if (_results.contains(result))
				return;
			_results.add(result);
		}
		
		//增加一个孩子节点
		public void addTransition(TreeNode node) {
			_transHash.put(node._char, node);
			TreeNode[] ar = new TreeNode[_transHash.size()];
			Iterator<TreeNode> it = _transHash.values().iterator();
			for (int i=0; i < ar.length; i++) {
				if (it.hasNext()) {
					ar[i] = it.next();
				}
			}
			_transitionAr = ar;
		}
		
		public TreeNode getTransition(char c) {
			return _transHash.get(c);
		}
		
		public boolean containsTransition(char c) {
			return getTransition(c) != null;
		}
		
		public char getChar() {
			return _char;
		}
		
		public TreeNode parent() {
			return _parent;
		}
		
		public TreeNode failure(TreeNode value) {
			_failure = value;
			return _failure;
		}
		
		public TreeNode[] transitions() {
			return null;
		}
	}
}

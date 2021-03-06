package com.chinese.match;

import java.util.HashSet;
import java.util.Set;

import com.trie.TrieNode.NodeType;

public class TernaryTree {

	public TernaryTree() {

	}

	private Node _root;

	private HashSet<String> _hashSet;


	public void Insert(String s) {
		if (null == s || s.isEmpty()) {
			throw new IllegalArgumentException();
		}

		int offset = 0;
		if (null == _root) {
			_root = new Node(s.charAt(0), NodeType.UNCOMPLETED);
		}

		Node current = _root;
		while (offset < s.length()) {
			if (s.charAt(offset) < current.Word) {
				if (null == current.leftChild) {
					current.leftChild = new Node(s.charAt(offset),
							NodeType.UNCOMPLETED);
				}
				current = current.leftChild;
			} else if (s.charAt(offset) > current.Word) {
				if (null == current.rightChild) {
					current.rightChild = new Node(s.charAt(offset),
							NodeType.UNCOMPLETED);
				}
				current = current.rightChild;
			} else {
				if (offset + 1 == s.length()) {
					current.type = NodeType.COMPLETED;
					//current.data = s;
					break;
				} else {
					// Center child actually is equal child.
					if (null == current.centerChild) {
						current.centerChild = new Node(s.charAt(offset + 1),
								NodeType.UNCOMPLETED);
					}
				}
				current = current.centerChild;
				offset += 1;
			}
		}
	}

	
	public Node Find(String s) {
		int pos = 0;
		Node node = _root;
		_hashSet = new HashSet<String>();
		while (node != null) {
			if (s.charAt(pos) < node.Word) {
				node = node.leftChild;
			} else if (s.charAt(pos) > node.Word) {
				node = node.rightChild;
			} else {
				if (++pos == s.length()) {
					if (node.type == NodeType.COMPLETED) {
						_hashSet.add(s);
					}
					return node;
				}

				node = node.centerChild;
			}
		}

		return null;
	}

	private void DFS(String prefix, Node node) {
		if (node != null) {
			if (NodeType.COMPLETED == node.type) {
				_hashSet.add(prefix + node.Word);
			}

			DFS(prefix, node.leftChild);
			DFS(prefix + node.Word, node.centerChild);
			DFS(prefix, node.rightChild);
		}
	}

	// / <summary>
	// / Finds the similar world.
	// / </summary>
	// / <param name="s">The prefix of the world.</param>
	// / <returns>The world has the same prefix.</returns>
	public HashSet<String> FindSimilar(String s) {
		// create a hash
		Node node = this.Find(s);
		this.DFS(node.Word.toString(), node.centerChild);
		return _hashSet;
	}
	
	/**
	 * Return null if key is not in directory
	 * @param key
	 * @param offset
	 * @return
	 */
	public String matchLong(String key, int offset) {
		if (null == key
				|| "".equals(key)) {
			return null;
		}
		String ret = null;
		
		Node current = _root;
		int charIndex = offset;
		while (true) {
			if (null == current) {
				return ret;
			}
			
			int charComp = key.charAt(charIndex) - current.Word;
			
			if (charComp == 0) {
				charIndex++;
				
				if(current.type == NodeType.COMPLETED) {
					ret = key.substring(offset, charIndex);
				}
				
				if (charIndex == key.length()) {
					return ret;
				}
				
				current = current.centerChild;
			} else if (charComp < 0) {
				current = current.leftChild;
			} else {
				current = current.rightChild;
			}
 		}
	}

	public static boolean test_matchLong() {
		TernaryTree t = new TernaryTree();
		t.Insert("大");
		t.Insert("大学生");
		t.Insert("大学");
		t.Insert("活动");
		t.Insert("活动中心");
		String sentence = "大学生活动中心";
		int offset = 0;
		String ret = t.matchLong(sentence, offset);
		if ("大学生".equals(ret)) {
			System.out.println("Pass test match long");
			return true;
		} else {
			System.out.println("Fail to pass test test_match long");
			return false;
		}
	}
	
	public static boolean test_ternary() {
		TernaryTree t = new TernaryTree();
		t.Insert("is");
		t.Insert("as");
		t.Insert("he");
		t.Insert("in");
		t.Insert("isbel");
		Node n = t.Find("in");
		if (null != n) {
			//System.out.println("Find now");
			// System.out.println(n.Word);
		} else {
			System.out.println("Fail to pass test test_ternary Find");
			return false;
			//System.out.println("Find nothing");
		}
		Set<String> res = t.FindSimilar("i");
		
		if (!res.contains("is")
				|| !res.contains("in")
				|| !res.contains("isbel")) {
			System.out.println("Fail to pass test test_ternary FindSimilar");
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		test_matchLong();
	}
}

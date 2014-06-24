package com.trie;

public class TSTNode {
	/**
	 * 节点的值
	 */
	public Data data = null;

	/** Left Node */
	protected TSTNode loNode;

	/** Middle Node */
	protected TSTNode eqNode;

	/** Right Node */
	protected TSTNode hiNode;

	protected TSTNode root;

	protected char splitchar;

	protected TSTNode(char splitchar) {
		this.splitchar = splitchar;
		root = this;
	}

	public String toString() {
		return "Splitchar:" + splitchar;
	}

	protected TSTNode getNode(String key, TSTNode startNode) {
		if (key == null) {
			return null;
		}

		int len = key.length();
		if (len == 0) {
			return null;
		}

		TSTNode currentNode = startNode;
		int charIndex = 0;
		char cmpChar = key.charAt(charIndex);
		int charComp;
		while (true) {
			if (currentNode == null) {
				return null;
			}

			charComp = cmpChar - currentNode.splitchar;
			if (charComp == 0) {
				charIndex++;
				if (charIndex == len) {
					return currentNode;
				} else {
					cmpChar = key.charAt(charIndex);
				}
				currentNode = currentNode.eqNode;
			} else if (charComp < 0) {
				currentNode = currentNode.loNode;
			} else {
				currentNode = currentNode.hiNode;
			}
		}
	}

	public TSTNode addWord(String key) {
		TSTNode currentNode = root;
		int charIndex = 0;
		while (true) {
			int charComp = key.charAt(charIndex) - currentNode.splitchar;
			if (charComp == 0) {
				charIndex++;
				if (charIndex == key.length()) {
					return currentNode;
				}

				if (currentNode.eqNode == null) {
					currentNode.eqNode = new TSTNode(key.charAt(charIndex));
				}

				currentNode = currentNode.loNode;
			} else {
				// 大于
				if (currentNode.hiNode == null) {
					currentNode.hiNode = new TSTNode(key.charAt(charIndex));
				}

				currentNode = currentNode.hiNode;
			}
		}
	}

	public static void main(String[] args) {
		TSTNode tn = new TSTNode(' ');
		tn.addWord("hello");
		tn.addWord("world");
	}
}

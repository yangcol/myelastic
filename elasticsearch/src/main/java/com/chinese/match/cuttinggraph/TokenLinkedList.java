package com.chinese.match.cuttinggraph;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class TokenLinkedList implements Iterable<TokenInf> {
	public static class Node {
		public TokenInf item;
		public Node next;
		
		Node(TokenInf item) {
			this.item = item;
			next = null;
		}
	}
	
	private Node head;
	
	public TokenLinkedList() {
		head = null;
	}
	
	public void put(TokenInf item) {
		Node n = new Node(item);
		n.next = head;
		head = n;
	}
	
	public Node getHead() {
		return head;
	}
	
	public Iterator<TokenInf> iterator() {
		return new LinkIterator(head);
	}
	
	private class LinkIterator implements Iterator<TokenInf> {

		public LinkIterator(Node begin) {
			itr = begin;
		}
		
		Node itr;
		public boolean hasNext() {
			return itr != null; 
		}

		public TokenInf next() {
			if (null == itr) {
				throw new NoSuchElementException();
			}
			Node cur = itr;
			itr = itr.next;
			return cur.item;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		Node cur = head;
		
		while (cur != null) {
			buf.append(cur.item.toString());
			buf.append("\t");
			cur = cur.next;
		}
		
		return buf.toString();
	}

}

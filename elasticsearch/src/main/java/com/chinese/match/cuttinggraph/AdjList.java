package com.chinese.match.cuttinggraph;

import java.util.ArrayList;
import java.util.Iterator;

import com.chinese.match.Node;
import com.chinese.match.TernaryTree;
import com.trie.TernarySearchTrie;

public class AdjList {
	private TokenLinkedList list[]; // AdjList的图结构
	
	public AdjList(int verticesNum) {
		list = new TokenLinkedList[verticesNum];
		
		for (int index = 0; index < verticesNum; index++) {
			list[index] = new TokenLinkedList();
		}
	}
	
	public int getVerticesNum() {
		return list.length;
	}
	
	/**  
     * 增加一个边到图中  
     */  
    public void addEdge(TokenInf newEdge) {  
        list[newEdge.end].put(newEdge);  
    }  
 
    /**  
     * 返回一个迭代器，包含以指定点结尾的所有的边  
     */  
    public Iterator<TokenInf> getAdjacencies(int vertex) {  
        TokenLinkedList ll = list[vertex];  
        if(ll == null)  
            return null;  
        return ll.iterator();  
    }
    
    public static void main(String[] args) {
    	// Dictionary below
    	TernaryTree t = new TernaryTree();
		//有   意见   分歧
    	t.Insert("有");
		t.Insert("意见");
		t.Insert("分歧");
		
		//有意  见  分歧
		t.Insert("有意");
		t.Insert("见");
		t.Insert("分歧");
    	String text = "有意见分歧";
    	AdjList a = new AdjList(text.length());
    	TokenInf newEdge1 = new TokenInf("有");
    	TokenInf newEdge2 = new TokenInf("意");
    	TokenInf newEdge3 = new TokenInf("见");
    	TokenInf newEdge4 = new TokenInf("分");
    	TokenInf newEdge5 = new TokenInf("歧");
    	a.addEdge(newEdge1);
    	a.addEdge(newEdge2);
    	a.addEdge(newEdge3);
		
	}

}

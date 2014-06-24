package test.hint.auto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import com.hint.auto.CustomDictionary;
import com.hint.auto.Node;
import com.hint.auto.TernaryTree;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Test_TernarySearchTree extends TestCase{
	/**
	 * This test cost a lot of time because insert nodes into trie
	 * cost a lot of money
	 */
	public void test_InsertDict() {
		TernaryTree tt = new TernaryTree();
		CustomDictionary cd = null;
		try {
			long before = System.currentTimeMillis();
			cd = new CustomDictionary("mydict.dic");
			long end = System.currentTimeMillis();
			System.out.println("Load dict cost: " + (end - before));
		} catch (IOException e1) {
			e1.printStackTrace();
			Assert.fail();
		}
		
		try {
			long before = System.currentTimeMillis();
			tt.InsertDict(cd);
			long end = System.currentTimeMillis();
			System.out.println("Insert dict cost: " + (end - before));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Insert Dict fail");
		}
		
		long before = System.currentTimeMillis();
		Set<String> sim = tt.FindSimilar("来自");
		ArrayList<Node> simNode = tt.FindSimilarNodes("来自");
		Collections.sort(simNode, Node.BY_HOT);
		
		for (Node s: simNode) {
			System.out.println(s.getData().word + ", " + s.getData().h_score);
		}
		long end = System.currentTimeMillis();
		System.out.println("Find similar cost: " + (end - before));
	}
	
	public void another() {

	}
}

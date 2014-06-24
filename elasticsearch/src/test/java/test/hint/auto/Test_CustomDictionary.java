package test.hint.auto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.hint.auto.CustomDictionary;
import com.hint.auto.Data;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Test_CustomDictionary extends TestCase {
	public void test_new_customDictionary() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("dict.txt"));
		String indict = "in,1,2,amalias\nby,2,3,byalias\nhoho,3,4";
		bw.write(indict);
		bw.flush();
		bw.close();
		CustomDictionary cd = null;
		try {
			cd = new CustomDictionary("dict.txt");
		} catch (IOException e) {
			Assert.fail("Fail to get the dict");
			e.printStackTrace();
			return;
		}
		ArrayList<Data> data = cd.getData();
		for (Data da: data) {
			Assert.assertTrue(indict.contains(da.word));
			Assert.assertTrue(indict.contains(String.valueOf(da.h_score)));
			Assert.assertTrue(indict.contains(String.valueOf(da.s_count)));
		}
		
		
		// Delete file
		File f = new File("dict.txt");
		if (f.exists()) {
			f.delete();
		}
	}
	
}

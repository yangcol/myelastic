package com.app.jest.es.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAll extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TestESUtil.class);
        suite.addTestSuite(TestESClient.class);
		return suite;
	}
}

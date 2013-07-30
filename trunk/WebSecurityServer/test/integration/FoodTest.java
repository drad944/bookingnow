package integration;

import junit.framework.Test;
import junit.framework.TestSuite;


import com.pitaya.bookingnow.service.impl.TestFoodService;

public class FoodTest {
	public static Test suite() {
	       TestSuite suite = new TestSuite();
	       suite.addTest(new TestFoodService("testAdd"));
	       suite.addTest(new TestFoodService("testModify"));
	       suite.addTest(new TestFoodService("testSearchFoods"));
	       suite.addTest(new TestFoodService("testRemoveFoodById"));
	       
	       return suite;
	    }
}

package StepDefinition;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class Hooks {

	@BeforeTest
	public void BeforeSc() {
		System.out.println("Started Execution of test");
	}

	@AfterTest
	public void AfterSc() {
		System.out.println("Completed Test Execution");
	}
}

package TestRunner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.runner.RunWith;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.vimalselvam.cucumber.listener.ExtentProperties;
import com.vimalselvam.cucumber.listener.Reporter;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "Features", 
		glue = { "StepDefinition" },
		plugin = { "com.vimalselvam.cucumber.listener.ExtentCucumberFormatter:" }, 
		monochrome = true)

public class Runner {
	@AfterClass
	public static void teardown() {
		Reporter.loadXMLConfig(new File("config/report.xml"));
		Reporter.setSystemInfo("user", System.getProperty("user.name"));
		Reporter.setSystemInfo("os", "Windows 10");
		Reporter.setTestRunnerOutput("Sample test runner output message");
	}

	@BeforeClass
	public static void setup() {
		ExtentProperties extentProperties = ExtentProperties.INSTANCE;
		extentProperties.setProjectName("TestProject");
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_hhmmss");
		Date curdate = new Date();
		String strDate = sdf.format(curdate);
		String filename = System.getProperty("user.dir") + "\\TestResults\\Test_Report" + strDate + ".html";
		extentProperties.setReportPath(filename);
	}

}

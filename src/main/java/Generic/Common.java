package Generic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.vimalselvam.cucumber.listener.Reporter;

public class Common {
	public WebDriver driver;
	public String Config_Path = "./Config/Config.properties";
	public String DataPath = getProperty(Config_Path, "DataPath");
	public String Screenshotpath = getProperty(Config_Path, "ResultPath");
	public String LocatorsPath = getProperty(Config_Path, "LocatorPath");
	public WebElement ele;

	public void AddStepLog(String Status, String logcontent) {
		System.out.println("|| " + Status + " || " + logcontent);
		if (Status.toUpperCase().contentEquals("PASS")) {
			Reporter.addStepLog("<font size='2' color='blue'> <b> || Pass || </b> " + logcontent + " </font>");
		} else {
			Reporter.addStepLog("<font size='2' color='red'> <b> || Fail || </b> " + logcontent + " </font>");
		//	Assert.fail();
		}
	}

	public String getProperty(String Ppath, String Pkey) {
		String PValue = "";
		try {
			Properties p = new Properties();
			p.load(new FileInputStream(Ppath));
			PValue = p.getProperty(Pkey);
		} catch (Exception e) {
		}
		if (PValue == null) {
			PValue = Pkey;
		}
		return PValue;
	}

	public String getJsonValue(String JpathA, String Jkey) {
		String JValue = "";
		try {
			String Jpath = getProperty(Config_Path, JpathA);
			Object obj = new JSONParser().parse(new FileReader(Jpath));
			JSONObject jo = (JSONObject) obj;
			JValue = (String) jo.get(Jkey);
			System.out.println(JValue);
		} catch (Exception e) {
			AddStepLog("Fail", "getJsonValue function failed");
		}
		if (JValue == null) {
			JValue = Jkey;
		}
		return JValue;
	}

	public void takePhoto(WebDriver driver, String TestName) {
		try {
		String dateTime = new Date().toString().replaceAll(":", "_");
		TakesScreenshot t = (TakesScreenshot) driver;
		File srcFile = t.getScreenshotAs(OutputType.FILE);
		String dstPath = Screenshotpath + "/" + TestName + "-" + dateTime + ".png";
			FileUtils.copyFile(srcFile, new File(dstPath));
			AddStepLog("Pass", "Screenshot captured.");
			Reporter.addScreenCaptureFromPath(new File(dstPath).getAbsolutePath(), TestName);
		} catch (Exception e) {
			AddStepLog("Fail", "takePhoto function failed");
		}
	}

	public WebDriver GetDriver(WebDriver driver) {
		String BROWSER = getProperty(Config_Path, "BROWSER");
		String RunMode = getProperty(Config_Path, "RunMode");

		if (BROWSER.contentEquals("CHROME")) {
			String CHROME_KEY = getProperty(Config_Path, "CHROME_KEY");
			String CHROME_VALUE = getProperty(Config_Path, "CHROME_VALUE");
			System.setProperty(CHROME_KEY, CHROME_VALUE);
			// driver=new ChromeDriver();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-extensions");
			options.setExperimentalOption("useAutomationExtension", false);
			options.addArguments("start-maximized");
			if (RunMode.toUpperCase().equals("HEADLESS")) {
				options.addArguments("--headless");
			}
			driver = new ChromeDriver(options);
			AddStepLog("Pass", "Chrome browser Launched.");
		} else if (BROWSER.contentEquals("IE")) {
			String IE_KEY = getProperty(Config_Path, "IE_KEY");
			String IE_VALUE = getProperty(Config_Path, "IE_VALUE");
			System.setProperty(IE_KEY, IE_VALUE);
			driver = new InternetExplorerDriver();
			AddStepLog("Pass", "IE browser Launched.");
		} else {
			AddStepLog("Fail", "Browser Launch Failed.");
		}
		return driver;
	}

	public void LaunchURL(WebDriver driver, String URLA) {
		try {
			String AUT = getProperty(DataPath, URLA);
			driver.get(AUT);
			AddStepLog("Pass", "URL Launched.");
		} catch (Exception e) {
			AddStepLog("Fail", "URL Launch Failed.");
		}
	}

	public WebElement GetLocator(WebDriver driver, String Jkey) {
		Map locator = null;
		try {
			
			Object obj = new JSONParser().parse(new FileReader(LocatorsPath));
			JSONObject jo = (JSONObject) obj;
			locator = ((Map) jo.get(Jkey));
			Iterator<Map.Entry> itr1 = locator.entrySet().iterator();
			Map.Entry pair = itr1.next();
			String locatorType = (String) pair.getKey();
			String locatorpath = (String) pair.getValue();
			System.out.println(locatorType + " : " + locatorpath);

			if (locatorType.toUpperCase().equals("NAME")) {
				try {
					ele = driver.findElement(By.name(locatorpath));
				} catch (Exception e) {
					ele = null;
				}
			} else if (locatorType.toUpperCase().equals("ID")) {
				try {
					ele = driver.findElement(By.id(locatorpath));
				} catch (Exception e) {
					ele = null;
				}
			} else if (locatorType.toUpperCase().equals("XPATH")) {
				try {
					ele = driver.findElement(By.xpath(locatorpath));
				} catch (Exception e) {
					ele = null;
				}
			} else if (locatorType.toUpperCase().equals("CSS")) {
				try {
					ele = driver.findElement(By.cssSelector(locatorpath));
				} catch (Exception e) {
					ele = null;
				}
			} else if (locatorType.toUpperCase().equals("LINKTEXT")) {
				try {
					ele = driver.findElement(By.linkText(locatorpath));
				} catch (Exception e) {
					ele = null;
				}
			} else if (locatorType.toUpperCase().equals("CLASSNAME")) {
				try {
					ele = driver.findElement(By.className(locatorpath));
				} catch (Exception e) {
					ele = null;
				}
			} else if (locatorType.toUpperCase().equals("TAGNAME")) {
				try {
					ele = driver.findElement(By.tagName(locatorpath));
				} catch (Exception e) {
					ele = null;
				}
			} else if (locatorType.toUpperCase().equals("PARTIALLINKTEXT")) {
				try {
					ele = driver.findElement(By.partialLinkText(locatorpath));
				} catch (Exception e) {
					ele = null;
				}
			}
		} catch (Exception e) {
			AddStepLog("Fail", "GetLocator Function Failed. " + e);
		}
		return ele;
	}

	public void tearDown(WebDriver driver) {
		try {
			driver.quit();
			AddStepLog("Pass", "tearDown Function successfull. ");
		} catch (Exception e) {
			AddStepLog("Fail", "tearDown Function failed. " + e);
		}
	}

	public void Input(WebDriver driver, String element, String stepData) {
		try {
			WebElement ele = GetLocator(driver, element);
			stepData = getProperty(DataPath, stepData);
			if (ele == null) {
				AddStepLog("Fail", "Retrive element failed. " + element);
			} else {
				// ele.clear();
				ele.sendKeys(stepData);
				AddStepLog("Pass", "Input Function successfull. " + stepData);
			}
		} catch (Exception e) {
			AddStepLog("Fail", "Input Function failed. " + e);
		}
	}

	public void Click(WebDriver driver, String element) {
		try {
			WebElement ele = GetLocator(driver, element);
			if (ele == null) {
				AddStepLog("Fail", "Retrive element failed. " + element);
			} else {
				ele.click();
				AddStepLog("Pass", "Click Function successfull. ");
			}
		} catch (Exception e) {
			AddStepLog("Fail", "Click Function failed. " + e);
		}
	}

	public void Wait(String stepData) {
		try {
			stepData = getProperty(DataPath, stepData);
			Thread.sleep(Integer.parseInt(stepData));
			AddStepLog("Pass", "Waited for in ms -" + stepData);
		} catch (Exception e) {
			AddStepLog("Fail", "Wait Function failed. " + e);
		}
	}

	public void ImplicitWait(WebDriver driver, String stepData) {
		try {
			stepData = getProperty(DataPath, stepData);
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(stepData), TimeUnit.SECONDS);
			AddStepLog("Pass", "ImplicitWaited for " + stepData);
		} catch (Exception e) {
			AddStepLog("Fail", "ImplicitWait Function failed. " + e);
		}
	}

	public void WaitTillVisibleElement(WebDriver driver, String element, String stepData) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Integer.parseInt(stepData));
			Map locator = null;
			Object obj = new JSONParser().parse(new FileReader(LocatorsPath));
			JSONObject jo = (JSONObject) obj;
			locator = ((Map) jo.get(element));
			Iterator<Map.Entry> itr1 = locator.entrySet().iterator();
			Map.Entry pair = itr1.next();
			String locatorType = (String) pair.getKey();
			String locatorpath = (String) pair.getValue();
			if (locatorType.toUpperCase().equals("NAME")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorpath)));
				AddStepLog("Pass", "WaitTillVisibleElement successful by using name for" + element);
			} else if (locatorType.toUpperCase().equals("ID")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorpath)));
				AddStepLog("Pass", "WaitTillVisibleElement successful by using id for" + element);
			} else if (locatorType.toUpperCase().equals("XPATH")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorpath)));
				AddStepLog("Pass", "WaitTillVisibleElement successful by using Xpath for" + element);
			} else if (locatorType.toUpperCase().equals("CSS")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locatorpath)));
				AddStepLog("Pass", "WaitTillVisibleElement successful by using CssSelector for" + element);
			} else if (locatorType.toUpperCase().equals("CLASSNAME")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(locatorpath)));
				AddStepLog("Pass", "WaitTillVisibleElement successful by using ClassName for" + element);
			}
		} catch (Exception e) {
			AddStepLog("Fail", "WaitTillVisibleElement Function failed. " + e);
		}
	}

	public void VerifyPageTitle(WebDriver driver, String stepData) {
		try {
			stepData = getProperty(DataPath, stepData);
			if (driver.getTitle().contentEquals(stepData)) {
				AddStepLog("Pass", "VerifyPageTitle successfull. " + driver.getTitle() + " == " + stepData);
			} else {
				AddStepLog("Fail", "VerifyPageTitle function failed. " + driver.getTitle());
				takePhoto(driver, "VerifyPageTitle");
			}

		} catch (Exception e) {
			AddStepLog("Fail", "VerifyPageTitle Function failed. " + e);
		}
	}

	public void VerifyAttribute(WebDriver driver, String element, String stepData) {
		try {
			WebElement ele = GetLocator(driver, element);
			if (ele.getAttribute("value") == null) {
				AddStepLog("Fail", "Retreive element failed. " + element);
			} else {
				stepData = getProperty(DataPath, stepData);
				if (ele.getAttribute("value").contentEquals(stepData)) {
					AddStepLog("Pass", "VerifyAttribute successfull. " + ele.getAttribute("value") + " == " + stepData);
				} else {
					AddStepLog("Fail", "VerifyAttribute function failed. " + ele.getAttribute("value"));
					takePhoto(driver, "VerifyAttribute");
				}
			}
		} catch (Exception e) {
			AddStepLog("Fail", "VerifyAttribute Function failed. " + e);
		}
	}

	public void VerifyContains(WebDriver driver, String element, String stepData) {
		try {
			WebElement ele = GetLocator(driver, element);
			if (ele == null) {
				AddStepLog("Fail", "Retrive element failed. " + element);
			} else {
				stepData = getProperty(DataPath, stepData);
				if (ele.getText().contains(stepData)) {
					AddStepLog("Pass", "VerifyContains successfull. " + ele.getText() + " == " + stepData);
				} else {
					AddStepLog("Fail", "VerifyContains function failed. " + ele.getText() + " <> " + stepData);
					takePhoto(driver, "VerifyContains");
				}
			}
		} catch (Exception e) {
			AddStepLog("Fail", "VerifyContains Function failed. " + e);
		}
	}

	public void VerifyDisplayed(WebDriver driver, String element, String stepData) {
		try {
			WebElement ele = GetLocator(driver, element);
			if (ele == null & (stepData=="false")) {
				AddStepLog("Pass", "Element Not displayed" + element);
			} else if (ele!= null & (stepData=="false")) {
				AddStepLog("Pass", "Element is displayed" + element);
			}
			else {
				AddStepLog("Fail", "Validation fail" + element);
				takePhoto(driver, stepData);
			}
		} catch (Exception e) {
			AddStepLog("Fail", "Validation failed. " + e);
		}
	}
}

package StepDefinition;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import Generic.Common;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Steps {
	WebDriver driver = null;
	Common Comm = new Common();

	@Given("^I am in borrowing calculator page$")
	public void i_am_in_borrowing_calculator_page() throws Throwable {
		System.out.println("Open the Browser and launch the application");
		// Launch the Browser and Enter the URL
		driver = Comm.GetDriver(driver);
		Comm.Wait("4000");
		Comm.LaunchURL(driver, "URLA");
		Comm.ImplicitWait(driver, "10");
		Comm.VerifyPageTitle(driver, "PageTitle");
		Comm.takePhoto(driver, "HomePage");
	}

	@When("^I fill my details \"([^\"]*)\"$")
	public void i_fill_my_details(String arg1) throws Throwable {
		System.out.println("Fill your required details");
		Comm.Click(driver, "applicationTypeSingle");
		System.out.println("Single is clicked");
		Comm.Click(driver, "noOfDependantsDropDown");
		driver.findElement(By.xpath("//option[text()=0]")).click();
		System.out.println("noOfDependantsDropDown is clicked");
		Comm.Click(driver, "homeToLiveIn");
		Comm.takePhoto(driver, "HomePage");
	}

	@And("^I fill my earnings details$")
	public void i_fill_my_earnings_details(DataTable dt) throws Throwable {
		System.out.println("Fill your earning details");
		List<String> list=dt.asList(String.class);
		Comm.Click(driver, "incomeField");
		Comm.Input(driver, "incomeField", list.get(2));
		Comm.Click(driver, "otherIncomeField");
		Comm.Input(driver, "otherIncomeField", list.get(3));
		Comm.takePhoto(driver, "HomePage");
	}

	@And("^I fill my expenses details$")
	public void i_fill_my_expenses_details(DataTable dt) throws Throwable {
		System.out.println("Fill your expenses details");
		List<String> list=dt.asList(String.class);
		Comm.Click(driver, "livingExpenseField");
		Comm.Input(driver, "livingExpenseField", list.get(5));
		Comm.Click(driver, "homeLoanRepaymentField");
		Comm.Input(driver, "homeLoanRepaymentField", list.get(6));
		Comm.Click(driver, "otherLoanRepaymentField");
		Comm.Input(driver, "otherLoanRepaymentField", list.get(7));
		Comm.Click(driver, "otherCommitmentsField");
		Comm.Input(driver, "otherCommitmentsField", list.get(8));
		Comm.Click(driver, "creditCardLimitsField");
		Comm.Input(driver, "creditCardLimitsField", list.get(9));
		Comm.takePhoto(driver, "HomePage");
	}

	@And("^I click on work out borrow button$")
	public void i_click_on_work_out_borrow_button() throws Throwable {
		Comm.Click(driver, "borrowButton");
		System.out.println("Clicked on Work out how much I could Borrow button");
		Comm.takePhoto(driver, "HomePage");
	}

	@Then("^I should get borrowing estimate$")
	public void i_should_get_borrowing_estimate() throws Throwable {
		Comm.VerifyDisplayed(driver, "borrowAmount", "true");
		Comm.tearDown(driver);
	}

	@And("^I click on start over button$")
	public void i_click_on_start_over_button() throws Throwable {
		Comm.WaitTillVisibleElement(driver, "startOverButton", "5000");
		Comm.Click(driver, "startOverButton");
		System.out.println("Clicked on start over button");
		Comm.takePhoto(driver, "HomePage");
	}
	
	@Then("^All the fields gets cleared$")
	public void all_the_fields_gets_cleared() throws Throwable {
		Comm.VerifyAttribute(driver, "incomeField", "0");
		Comm.VerifyAttribute(driver, "otherIncomeField", "0");
		Comm.VerifyAttribute(driver, "livingExpenseField", "0");
		Comm.VerifyAttribute(driver, "homeLoanRepaymentField", "0");
		Comm.VerifyAttribute(driver, "otherLoanRepaymentField", "0");
		Comm.VerifyAttribute(driver, "otherCommitmentsField", "0");
		Comm.VerifyAttribute(driver, "creditCardLimitsField", "0");
		System.out.println("All the fields are cleared after clicking on start over button");
		Comm.tearDown(driver);
	}

	@And("^I fill only living expenses$")
	public void i_fill_only_living_expenses(DataTable dt) throws Throwable {
		List<String> list=dt.asList(String.class);
		Comm.Click(driver, "livingExpenseField");
		Comm.Input(driver, "livingExpenseField", list.get(1));

	}

	@Then("^I should not get estimation and get valid proper error message$")
	public void i_should_not_get_estimation_and_get_valid_proper_error_message() throws Throwable {
		Comm.VerifyContains(driver, "borrowErrorMessage", "we're unable to give you an estimate");
		if ("borrowErrorMessage".contains("we're unable to give you an estimate")) {
			Reporter.log("Displayed proper error message");
		} else {
			Reporter.log("Proper error message is not displayed");

		}
		Comm.tearDown(driver);
	}
	

}

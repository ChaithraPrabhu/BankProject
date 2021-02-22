Feature: Borrowing calculator scenarios

	@TestA
  	Scenario: I want to estimate how much I may be able to borrow based on my current income and existing financial commitments
    
    Given I am in borrowing calculator page
    When I fill my details "0"
    And I fill my earnings details
    |incomeField|OtherIncomeField|
    |80000|10000|
    And I fill my expenses details
    |livingExpenseField|homeLoanRepaymentField|otherLoanRepaymentField|otherCommitmentsField|creditCardLimitsField|
    |500|0|100|0|100000|
    And I click on work out borrow button
    Then I should get borrowing estimate

	@TestB
	Scenario: I want to clear all the entered loan details

    Given I am in borrowing calculator page
    When I fill my details "0"
    And I fill my earnings details
    |incomeField|OtherIncomeField|
    |80000|10000|
    And I fill my expenses details
	|livingExpenseField|homeLoanRepaymentField|otherLoanRepaymentField|otherCommitmentsField|creditCardLimitsField|
    |500|0|100|0|100000|
    And I click on work out borrow button
    And I click on start over button
    Then All the fields gets cleared

	@TestC
	Scenario: I want to estimate borrow by filling only living expenses and leaving all other fields as zero
   
    Given I am in borrowing calculator page
    And I fill only living expenses
    |livingExpenseField|
    |1|
    And I click on work out borrow button
    Then I should not get estimation and get valid proper error message
  
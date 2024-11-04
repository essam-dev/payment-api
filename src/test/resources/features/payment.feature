Feature: Generate a payment of 2 transactions

  Scenario: Create a first transaction
    Given I have a credit card payment
    When I modify the payment status to authorized then to captured
    Then I should see the payment is captured

  Scenario: Create a second transaction
    Given I have a paypal payment
    When I modify the payment status to canceled
    Then I should see the payment is canceled

  Scenario: Retrieve all transactions
    When I retrieve all transactions
    Then I should see two transactions



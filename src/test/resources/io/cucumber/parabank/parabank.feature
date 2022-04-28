Feature: Parabank

  Scenario: Register unique user with Parabank
    Given The Parabank website is opened
    When I register with a new user
    Then The page title should start with "ParaBank | Customer Created"

  Scenario: Register a user with no details
    Given The Parabank website is opened
    When I register an empty user
    Then Error messages should be present

  Scenario: exploratory test the login fields

  Scenario: Open a savings account
    Given The Parabank website is opened
    When I register with a new user
    And I open a new savings account
    Then The Account Overview is correct

  Scenario: Transfer funds
    Given The Parabank website is opened
    When I register with a new user
    And I open a new savings account
    And I transfer "42.00" from one account to another
    Then The transfer has been successful

  Scenario: Retrieve forgotten login info
    Given The Parabank website is opened
    When I have forgotten my login details
    Then I can retrieve my details and login

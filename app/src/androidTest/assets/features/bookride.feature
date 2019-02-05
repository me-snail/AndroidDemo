Feature: Book a ride with driver on mytaxi app

  @ScenarioId("MobileAppAutomationTest.Task.1") @bookride-scenarios
  Scenario: Search second result via driver name and call driver.
    Given Mytaxi app login page is open
    When User with seed id "a1f30d446f820665" logs into app
    And User is navigated to main app page
    And User search drivers with search string "sa" and open second driver by name
    And User call driver to book a ride
    Then User navigates to main app page from driver profile
    And User logs out of application
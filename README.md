# MobileAppAutomationTest Update

✏️ **Task Given**:
* Login case, the up-to-date credential (`username` & `password`) you can find [here](https://randomuser.me/api/?seed=a1f30d446f820665)).
* Search for "***sa***", select the 2nd result (via the name, not the index) from the list, then click the call button.
* Deploy the tests on [**CircleCI**](https://circleci.com/), and send us the link to the CircleCI builds overview page.

**Tools Used/Explored**:
   * Espresso : for android application UI automation.
   * JunitFramework : for running junit tests.
   * Cucumber : for running BDD tests.
   * CircleCI : for continuous deployment and test.

**Task Output**:
* Junit Test written for Task: com.mytaxi.android_demo.MainActivityTest, Automation run completed successfully on CircleCI.
* Cucumber Test for Task : androidTest/assets/features/bookride.feature, Automation run not completed successfully on CircleCI.

**Pending Items from Task**
* Handle location access dialog in cucumber testcase run,as handled in junit testcase.
* Handle edge case scenario and exception in cucumber testcase
* Include cucumber testcase in CircleCI run configuration.

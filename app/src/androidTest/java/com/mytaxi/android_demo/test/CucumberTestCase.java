package com.mytaxi.android_demo.test;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "features", // Test scenarios
        glue = {"com.mytaxi.android_demo.cucumber.steps"}, // Steps definitions
        format = {"pretty", // Cucumber report formats and location to store
                //"html:build/reports/cucumber-reports/cucumber-html-report",
                //"json:build/reports/cucumber-reports/cucumber.json",
                //"junit:build/reports/cucumber-reports//cucumber.xml"
        },
        tags={"~@manual","@bookride-scenarios"}
)

public class CucumberTestCase {
}

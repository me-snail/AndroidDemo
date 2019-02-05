package com.mytaxi.android_demo.cucumber.steps;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.os.PowerManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;

import com.mytaxi.android_demo.R;
import com.mytaxi.android_demo.activities.MainActivity;
import com.mytaxi.android_demo.cucumber.pages.AuthenticationPage;
import com.mytaxi.android_demo.cucumber.pages.BasePage;
import com.mytaxi.android_demo.cucumber.pages.DriverProfilePage;
import com.mytaxi.android_demo.cucumber.pages.MainPage;
import com.mytaxi.android_demo.testutils.ActivityFinisher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertNotNull;

@SuppressWarnings("JUnitTestCaseWithNoTests")
@RunWith(AndroidJUnit4.class)
public class StepDefinitions {

    @SuppressWarnings("unused")
    public static final String TAG = StepDefinitions.class.getSimpleName();
    @SuppressWarnings("unused")
    private Context mInstrumentationContext;
    @SuppressWarnings("unused")
    private Context mAppContext;
    private BasePage mCurrentPage;
    private Activity mActivity;
    private PowerManager.WakeLock mFullWakeUpLock;

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class,false,false);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void setUp() throws Exception {
        mInstrumentationContext = InstrumentationRegistry.getContext();
        mAppContext = InstrumentationRegistry.getTargetContext();
        mActivity = mActivityRule.launchActivity(new Intent()); // Start Activity before each test scenario
        assertNotNull(mActivity);

    }

    @Test
    public void book_ride_feature_test(){
    }

    @After
    public void tearDown() throws Exception {
        ActivityFinisher.finishOpenActivities(); // Required for testing App with multiple activities
    }


    @Given("^I wait for manual attachment of the debugger$")
    public void wait_for_manual_attachment_of_debugger() throws InterruptedException {
        while (!Debug.isDebuggerConnected()) {
            Thread.sleep(1000);
        }
    }

    @Given("^Mytaxi app login page is open$")
    public void mytaxi_app_login_page_is_open() {
        mCurrentPage = new AuthenticationPage();
    }

    @When("^User with seed id \"(.+)\" logs into app$")
    public void login_with_seed_id(final String seedId) throws InterruptedException {
        mCurrentPage = mCurrentPage.is(AuthenticationPage.class).loginMytaxiApp(seedId);
    }

    @And("^User is navigated to main app page$")
    public void user_sees_a_welcome_page() {
        mCurrentPage.is(MainPage.class);
    }

    @And("^User search drivers with search string \"(.+)\" and open second driver by name$")
    public void find_driver_using_search_string(final String searchContent) throws InterruptedException {
        mCurrentPage.is(MainPage.class).inputTextSearch(searchContent);
        //this.mActivity = mActivityRule.getActivity();

        AutoCompleteTextView mRecyclerView = mActivity.findViewById(R.id.textSearch);
        ListAdapter da = mRecyclerView.getAdapter();

        String[] result = mCurrentPage.is(MainPage.class).searchSecondDriver(da,searchContent);
        View view = mActivity.getWindow().getDecorView();
        mCurrentPage = mCurrentPage.is(MainPage.class).navigateToDriverProfile(result[0],result[1],view);
        mCurrentPage.is(DriverProfilePage.class);
    }

    @And("^User call driver to book a ride$")
    public void book_ride() throws InterruptedException {
        mCurrentPage.is(DriverProfilePage.class).dialDriver();
    }

    @Then("^User navigates to main app page from driver profile$")
    public void go_to_main_page_from_driver_profile() throws InterruptedException {
        mCurrentPage = mCurrentPage.is(DriverProfilePage.class).navigateToMainActivity();
    }

    @And("^User logs out of application$")
    public void logout_from_app() throws InterruptedException {
        mCurrentPage.is(MainPage.class).logoutMytaxiApp();
    }

}
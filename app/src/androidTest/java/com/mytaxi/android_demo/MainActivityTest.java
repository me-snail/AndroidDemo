package com.mytaxi.android_demo;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.action.ViewActions.click;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.widget.AutoCompleteTextView;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListAdapter;

import com.mytaxi.android_demo.activities.MainActivity;
import com.mytaxi.android_demo.models.Driver;
import com.mytaxi.android_demo.utils.network.HttpClient;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class);

    @Rule public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private MainActivity mActivity = null;
    private AutoCompleteTextView mRecyclerView = null;
    private ListAdapter da = null;
    private static int waitTimeout = 10000;
    private HttpClient mHttpClient = new HttpClient();
    private String name = null, phoneNumber = null;


    @Before
    public void Test_0_Login_to_mytaxi_app_with_given_credential_info() throws InterruptedException, IOException {

        String[] credentials = mHttpClient.getUserCredentials("a1f30d446f820665");
        Thread.sleep(this.waitTimeout);
        onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_username)).perform(typeText(credentials[0]));
        onView(withId(R.id.edt_password)).perform(typeText(credentials[1]));
        onView(withId(R.id.btn_login)).check(matches(isClickable()));
        onView(withId(R.id.btn_login)).perform(click());
        //onView(isRoot()).perform(waitId(R.id.textSearch, this.waitTimeout));
        Thread.sleep(this.waitTimeout);
    }

    @Test
    public void Test_1_Call_second_driver_from_search_based_on_name() throws InterruptedException {

        String search_string = "sa";
        onView(withId(R.id.textSearch)).check(matches(isDisplayed()));

        onView(withId(R.id.textSearch)).perform(typeText(search_string));
        System.out.println ("Search all drivers");
        Thread.sleep(this.waitTimeout);

        /* obtaining second largest name from the Adapter Data*/
        // TBD : write below code using onData to get driver name from filtered adapter view data.
        this.mActivity = mActivityRule.getActivity();
        this.mRecyclerView = this.mActivity.findViewById(R.id.textSearch);
        this.da = this.mRecyclerView.getAdapter();

        int driverCount = this.da.getCount();
        System.out.println ("Got drivers count "+driverCount);
        TreeMap<String, String> driverDetails = extractDriversInfo(this.da,search_string);

        int arrLen = driverDetails.size();

        if(arrLen != 0) {

            if (arrLen > 1) {
                name = (String) driverDetails.keySet().toArray()[arrLen - (arrLen - 1)];
                phoneNumber = (String) driverDetails.get(name);
            } else if (arrLen == 1) {
                name = (String) driverDetails.keySet().toArray()[0];
                phoneNumber = (String) driverDetails.get(name);
            }

            onView(withId(R.id.textSearch)).perform(clearText());
            onView(withId(R.id.textSearch)).perform(typeText(name));
            onView(withText(name)).inRoot(withDecorView(not(this.mActivity.getWindow().getDecorView()))).check(matches(isCompletelyDisplayed())).perform(click());

            Thread.sleep(this.waitTimeout);

            onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
            onView(withText(R.string.title_activity_driver_profile)).check(matches(withParent(withId(R.id.toolbar))));
            onView(allOf(withId(R.id.textViewDriverName), withText(name))).check(matches(isDisplayed()));

            Intent resultData = new Intent(Intent.ACTION_DIAL);
            resultData.putExtra("phone", phoneNumber);
            ActivityResult result = new ActivityResult(Activity.RESULT_OK, resultData);
            intending(hasAction(Intent.ACTION_DIAL)).respondWith(result);
            onView(withId(R.id.fab)).check(matches(isClickable())).perform(click());

            Thread.sleep(this.waitTimeout);
            intended(allOf(hasAction(Intent.ACTION_DIAL)));
            Thread.sleep(this.waitTimeout);
            onView(isRoot()).perform(ViewActions.pressBack());
            Thread.sleep(this.waitTimeout);
        }
        else{
            System.out.println("No Driver found");
        }
    }

    @After
    public void Test_3_Logout_from_mytaxi_app(){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText(R.string.app_name)).check(matches(withParent(withId(R.id.toolbar))));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));

        onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
    }

    private TreeMap<String, String> extractDriversInfo(ListAdapter drivers,String searchStr) {
        Map<String, String> driverInfo = new HashMap<>();
        TreeMap<String, String> sorteddriverInfo = new TreeMap<>();
        for(int i=0; i < drivers.getCount(); i++) {
            Driver driver = (Driver) drivers.getItem(i);
            // Condition in case of no match. Need to remove this getting filtered data from adapter view.
            if(driver.getName().toLowerCase().startsWith(searchStr)){
                System.out.println("Driver found :"+driver.getName());
                driverInfo.put(driver.getName(),driver.getPhone());
            }else{
                System.out.println("Driver not found, no filters applied in adapter view data");
                return new TreeMap<>();
            }
        }
        sorteddriverInfo.putAll(driverInfo);
        for (Map.Entry<String, String> entry : sorteddriverInfo.entrySet())
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
        return sorteddriverInfo;
    }


}

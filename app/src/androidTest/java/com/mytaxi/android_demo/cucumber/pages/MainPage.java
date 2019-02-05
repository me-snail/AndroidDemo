package com.mytaxi.android_demo.cucumber.pages;

import android.app.Activity;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;

import com.mytaxi.android_demo.R;
import com.mytaxi.android_demo.activities.MainActivity;
import com.mytaxi.android_demo.models.Driver;

import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;

import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class MainPage extends BasePage {

    protected static final String SCREENSHOT_TAG = "MainActivity";
    private static int waitTimeout = 10000;
    //private Activity mActivity = new Activity();
    //private MainActivity mActivity;
    //private ListAdapter da = null;

//    @Rule
//    public IntentsTestRule<MainActivity> mActivityRule =
//            new IntentsTestRule<>(MainActivity.class,false,false);
//
//    @Rule
//    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    public MainPage() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText(R.string.app_name)).check(matches(withParent(withId(R.id.toolbar))));
    }

    public void inputTextSearch(String searchStr) throws InterruptedException {
        onView(withId(R.id.textSearch)).check(matches(isDisplayed()));
        onView(withId(R.id.textSearch)).perform(typeText(searchStr));
        Thread.sleep(this.waitTimeout);
    }

    public String[] searchSecondDriver(ListAdapter da,String searchStr) throws InterruptedException {
        //onView(withId(R.id.textSearch)).check(matches(isDisplayed()));

        //onView(withId(R.id.textSearch)).perform(typeText(searchStr));

        /* obtaining second largest name from the Adapter Data*/
        // TBD : write below code using onData to get driver name from filtered adapter view data.

//        this.mActivity = mActivityRule.getActivity();
//        Thread.sleep(this.waitTimeout);
//
//        AutoCompleteTextView mRecyclerView = this.mActivity.findViewById(R.id.textSearch);
//        ListAdapter da = mRecyclerView.getAdapter();

        int driverCount = da.getCount();
        System.out.println ("Got drivers count "+driverCount);
        String[] result = new String[2];
        TreeMap<String, String> driverDetails = extractDriversInfo(da,searchStr);

        int arrLen = driverDetails.size();
        String name = null, phoneNumber = null;
        if(arrLen != 0) {
            if (arrLen > 1) {
                name = (String) driverDetails.keySet().toArray()[arrLen - (arrLen - 1)];
                phoneNumber = (String) driverDetails.get(name);
            } else if (arrLen == 1) {
                name = (String) driverDetails.keySet().toArray()[0];
                phoneNumber = (String) driverDetails.get(name);
            }
            result[0] = name;
            result[1] = phoneNumber;
            return result;
            //return navigateToDriverProfile(name,phoneNumber);
        }
        else{
            System.out.println("No Driver found");
        }
        return result;
    }

    public DriverProfilePage navigateToDriverProfile(String driverName, String phoneNumber, View mActivity) throws InterruptedException{
        onView(withId(R.id.textSearch)).perform(clearText());
        onView(withId(R.id.textSearch)).perform(typeText(driverName));
        onView(withText(driverName)).inRoot(withDecorView(not(mActivity))).check(matches(isCompletelyDisplayed())).perform(click());

        Thread.sleep(this.waitTimeout);
        return new DriverProfilePage(driverName,phoneNumber);
    }

    public void logoutMytaxiApp() throws InterruptedException{
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText(R.string.app_name)).check(matches(withParent(withId(R.id.toolbar))));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
        Thread.sleep(this.waitTimeout);
        onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
    }

    private TreeMap<String, String> extractDriversInfo(ListAdapter drivers, String searchStr) {
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

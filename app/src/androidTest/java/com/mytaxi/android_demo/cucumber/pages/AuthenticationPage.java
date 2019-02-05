package com.mytaxi.android_demo.cucumber.pages;


import android.support.test.rule.GrantPermissionRule;
import com.mytaxi.android_demo.R;

import com.mytaxi.android_demo.utils.network.HttpClient;

import org.junit.Rule;
import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.action.ViewActions.click;

public class AuthenticationPage extends BasePage {

    protected static final String SCREENSHOT_TAG = "AuthenticationActivity";
    private static int waitTimeout = 10000;

    public AuthenticationPage() {
        onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
    }

    public MainPage loginMytaxiApp(String seed) throws InterruptedException {
        HttpClient mHttpClient = new HttpClient();
        String[] credentials = new String[0];
        try {
            credentials = mHttpClient.getUserCredentials(seed);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread.sleep(this.waitTimeout);
        onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_username)).perform(typeText(credentials[0]));
        onView(withId(R.id.edt_password)).perform(typeText(credentials[1]));
        onView(withId(R.id.btn_login)).check(matches(isClickable()));
        onView(withId(R.id.btn_login)).perform(click());
        Thread.sleep(this.waitTimeout);

        return new MainPage();
    }

}

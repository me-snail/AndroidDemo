package com.mytaxi.android_demo.cucumber.pages;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.action.ViewActions;

import com.mytaxi.android_demo.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class DriverProfilePage extends BasePage {

    private String name=null, contact=null;
    private static int waitTimeout = 10000;

    public DriverProfilePage(String name,String phoneNo){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText(R.string.title_activity_driver_profile)).check(matches(withParent(withId(R.id.toolbar))));
        onView(allOf(withId(R.id.textViewDriverName), withText(name))).check(matches(isDisplayed()));
        this.name = name;
        this.contact = phoneNo;
    }

    public void dialDriver() throws InterruptedException{
        Intent resultData = new Intent(Intent.ACTION_DIAL);
        resultData.putExtra("phone", contact);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasAction(Intent.ACTION_DIAL)).respondWith(result);
        onView(withId(R.id.fab)).check(matches(isClickable())).perform(click());

        Thread.sleep(this.waitTimeout);
    }

    public MainPage navigateToMainActivity() throws InterruptedException{
        onView(isRoot()).perform(ViewActions.pressBack());
        Thread.sleep(this.waitTimeout);
        return new MainPage();
    }

}

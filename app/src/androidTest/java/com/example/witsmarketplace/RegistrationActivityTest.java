package com.example.witsmarketplace;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import android.app.Instrumentation;
import android.widget.EditText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import com.example.witsmarketplace.LandingPage.LandingPage;
import com.example.witsmarketplace.Login.RegistrationActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RegistrationActivityTest {
    @Rule
    public IntentsTestRule<RegistrationActivity> rActivityTestRule = new IntentsTestRule<>(RegistrationActivity.class);
    private RegistrationActivity mActivity = null;
    public String wrongDetails = "You are already registered, click the link above to login.";
    public String correctDetails = "Registered successfully";
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(RegistrationActivity.class.getName(),null,false);

    @Test
    public void FirstnameCheck(){
        EditText firstname = mActivity.findViewById(R.id.Firstname);
        assertNotNull(firstname);
    }
    @Test
    public void LastnameCheck(){
        EditText lastname = mActivity.findViewById(R.id.Lastname);
        assertNotNull(lastname);
    }
    @Test
    public void Emailcheck(){
        EditText Email = mActivity.findViewById(R.id.email);
        assertNotNull(Email);
    }
    @Test
    public void Password(){
        EditText Password = mActivity.findViewById(R.id.Password);
        assertNotNull(Password);
    }
    @Test
    public void ConPassword(){
        EditText ConnPassowrd = mActivity.findViewById(R.id.ConfirmPassword);
        assertNotNull(ConnPassowrd);
    }

    @Test
    public void enterOldDetails(){
        Espresso.onView(withId(R.id.Firstname)).perform(typeText("Neal")).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.Lastname)).perform(typeText("Neal")).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.email)).perform(typeText("nealneal@gmail.com")).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.Password)).perform(typeText("123")).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.ConfirmPassword)).perform(typeText("123")).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.date)).perform(typeText("2000-01-01")).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.SignUp)).perform(click());
        RegistrationActivity activity = rActivityTestRule.getActivity();
        Espresso.onView(withText(wrongDetails)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(isDisplayed()));

    }

    @Before
    public void setUp() throws Exception {
        mActivity = rActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null ;
    }
}
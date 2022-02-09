package com.example.witsmarketplace;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.app.Instrumentation;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.TypeTextAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;
import com.example.witsmarketplace.LandingPage.LandingPage;
import com.example.witsmarketplace.Login.LoginActivity;
import com.example.witsmarketplace.fave_cart.cart;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);
    private LoginActivity mActivity = null;
    public String wrongDetails = "Incorrect email, make sure you have registered before you can login.";
    public String correctDetails = "Login successful. Thank you for shopping with us!";
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(LoginActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void checkUsernameField()
    {
        EditText etUsername = mActivity.findViewById(R.id.etUsername);
        assertNotNull(etUsername); // this checks that the username field is not empty (has hint)

    }

    @Test
    public void checkPasswordField(){
        EditText etPassword = mActivity.findViewById(R.id.etPassword);
        assertNotNull(etPassword);
    }

    @Test
    public void checkTextView(){
        TextView tvWits = mActivity.findViewById(R.id.text_view_id);
        String sWits = tvWits.getText().toString().trim();
        assertEquals("Welcome to Wits MarketPlace",sWits);
    }
    @Test
    public void noUsername(){
        Espresso.onView(withId(R.id.etPassword)).perform(typeText("abc")).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.login_btn)).perform(click());
        assertNotNull(monitor);
    }

    @Test
    public void noPassword(){
        Espresso.onView(withId(R.id.etUsername)).perform(typeText("abc")).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.login_btn)).perform(click());
        assertNotNull(monitor);
    }

    @Test
    public void wrongDetails(){
        Espresso.onView(withId(R.id.etUsername)).perform(typeText("abc")).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.etPassword)).perform(typeText("abc")).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.login_btn)).perform(click());
        assertNotNull(monitor);
    }

    @Test
    public void correctDetails(){
        Espresso.onView(withId(R.id.etUsername)).perform(typeText("vince@gmail.com")).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.etPassword)).perform(typeText("123")).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.login_btn)).perform(click());
        Instrumentation.ActivityMonitor LandingMonitor = getInstrumentation().addMonitor(LandingPage.class.getName(),null,false);
        assertNotNull(LandingMonitor);
    }



    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}
package com.example.witsmarketplace;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToHolder;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertNotNull;

import android.app.Instrumentation;
import android.content.ClipData;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.example.witsmarketplace.LandingPage.LandingPage;
import com.example.witsmarketplace.ViewMore.ViewMore;
import com.example.witsmarketplace.fave_cart.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LandingPageTest {
    @Rule
    public ActivityTestRule<LandingPage> mLandingTest = new ActivityTestRule<>(LandingPage.class);
    private LandingPage mActivity = null;
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(LandingPage.class.getName(),null,false);

    @Before
    public void setUp(){
        mActivity = mLandingTest.getActivity();
    }

    @Test
    public void ViewMoreBooksTest(){
        EditText txt_search = mActivity.findViewById(R.id.txt_search);
        Espresso.onView(withId(R.id.txt_search)).perform(typeText(""));
        ViewActions.closeSoftKeyboard();
        Espresso.onView(withId(R.id.vm_books)).perform(click());
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ViewMore.class.getName(),null,false);
        assertNotNull(activityMonitor);

    }

    @Test
    public void ViewMoreCompsTest(){
        EditText txt_search = mActivity.findViewById(R.id.txt_search);
        Espresso.onView(withId(R.id.txt_search)).perform(typeText(""));
        ViewActions.closeSoftKeyboard();
        Espresso.onView(withId(R.id.vm_computers)).perform(click());
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ViewMore.class.getName(),null,false);
        assertNotNull(activityMonitor);

    }

    @Test
    public void cartPageTest(){
        Espresso.onView(withId(R.id.nav_cart)).perform(click());
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(cart.class.getName(),null,false);
        assertNotNull(activityMonitor);
    }

    @Test
    public void favesPageTest(){
        Espresso.onView(withId(R.id.nav_favorite)).perform(click());
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(favorite.class.getName(),null,false);
        assertNotNull(activityMonitor);
    }


    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}

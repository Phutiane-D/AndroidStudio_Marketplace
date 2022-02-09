package com.example.witsmarketplace.fave_cart;

import static org.junit.Assert.*;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.example.witsmarketplace.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class cartTest {
    @Rule
    public ActivityTestRule<cart> mcartTestRule = new ActivityTestRule<cart>(cart.class);

    private cart mcart = null;

    @Before
    public void setUp()throws Exception{
        mcart = mcartTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view = mcart.findViewById(R.id.cartList);
        assertNotNull(view);

    }


    @After
    public void tearDown() throws Exception{
        mcart = null;
    }

}
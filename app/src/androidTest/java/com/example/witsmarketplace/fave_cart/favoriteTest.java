package com.example.witsmarketplace.fave_cart;

import static org.junit.Assert.*;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.example.witsmarketplace.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class favoriteTest {
@Rule
public ActivityTestRule<favorite> mfavoriteTestRule = new ActivityTestRule<favorite>(favorite.class);

private favorite mfavorite = null;

@Before
public void setUp()throws Exception{
    mfavorite = mfavoriteTestRule.getActivity();
}

@Test
    public void testLaunch(){
    View view = mfavorite.findViewById(R.id.rv_fav);
    assertNotNull(view);
}

@After
  public void tearDown(){
   mfavorite =null;
}

}
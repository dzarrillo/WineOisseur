package com.dzartek.wineoisseur;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@android.support.test.filters.LargeTest
public class FragmentWineResultsTest {

    @Rule
    public ActivityTestRule<MainWineActivity> mActivityTestRule = new ActivityTestRule<>(MainWineActivity.class);

    @Test
    public void fragmentWineResultsTest() {
        /*
        Tests to make sure we get a result set back from the snooth api call
        To run this test make sure you disable the following in Developer options on your test device:
            Window animation scale = off
            Transition animation Scale  = off
            Animator duration scale = off

        Running and testing the app for the first time will return a result set due the default
        parameters & the test should pass:
            Region = USA
            Rating = *** (3 stars)
            Varietal = Red
                Merlot
            Price = $0 - $10

        If you change the parameters to the following by running the application first you will not
        return a result set & the test should fail:
            Region = USA
            Rating = ***** (5 stars)
            Varietal = Red
                Merlot
            Price = $0 - $10

         */


        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.buttonSearch), withText("Search"),
                        withParent(allOf(withId(R.id.relativeLayoutWineSearch),
                                withParent(withId(R.id.scrollViewWineSearch))))));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerViewWineResults),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayoutWineResults),
                                        childAtPosition(
                                                withId(R.id.fragmentHolder),
                                                0)),
                                0),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

//        ViewInteraction textView = onView(
//                allOf(withId(R.id.textViewName), withText(not("")),
//                        childAtPosition(
//                                allOf(withId(R.id.relativeLayoutCardViewWineResults),
//                                        childAtPosition(
//                                                withId(R.id.cardviewResult),
//                                                0)),
//                                3),
//                        isDisplayed()));
//        textView.check(matches(isDisplayed()));
//
//        ViewInteraction textView2 = onView(
//                allOf(withId(R.id.textViewPrice), withText(not("")),
//                        childAtPosition(
//                                allOf(withId(R.id.relativeLayoutCardViewWineResults),
//                                        childAtPosition(
//                                                withId(R.id.cardviewResult),
//                                                0)),
//                                6),
//                        isDisplayed()));
//        textView2.check(matches(isDisplayed()));
//
//        ViewInteraction ratingBar = onView(
//                allOf(withId(R.id.ratingBarWine),
//                        childAtPosition(
//                                allOf(withId(R.id.relativeLayoutCardViewWineResults),
//                                        childAtPosition(
//                                                withId(R.id.cardviewResult),
//                                                0)),
//                                2),
//                        isDisplayed()));
//        ratingBar.check(matches(isDisplayed()));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

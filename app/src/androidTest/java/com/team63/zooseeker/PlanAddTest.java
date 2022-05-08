package com.team63.zooseeker;


import static android.content.Context.MODE_PRIVATE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PlanAddTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void resetDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences preferences = context.getSharedPreferences("filenames", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("vertex_info", context.getString(R.string.test_vertex_info));
        editor.putString("edge_info", context.getString(R.string.test_edge_info));
        editor.putString("zoo_graph", context.getString(R.string.test_zoo_graph));
        editor.commit();
        NodeDatabase.resetDatabase(context);
    }

    @After
    public void resetSharedPreferences() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences preferences = context.getSharedPreferences("filenames", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    @Test
    public void gorillaTest() {
        ViewInteraction searchAutoComplete = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.search_item),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.plan_item_text), withText("Gorillas"),
                        withParent(withParent(withId(R.id.pre_plan_items))),
                        isDisplayed()));
        textView.check(matches(withText("Gorillas")));
    }

    @Test
    public void moreAnimalTest()
    {
        ViewInteraction searchAutoComplete2 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete2.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.search_item),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                1)));
        recyclerView2.perform(actionOnItemAtPosition(4, click()));

        ViewInteraction searchAutoComplete3 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete3.perform(click());

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.search_item),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                1)));
        recyclerView3.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.plan_item_text), withText("Lions"),
                        withParent(withParent(withId(R.id.pre_plan_items))),
                        isDisplayed()));
        textView3.check(matches(withText("Lions")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.plan_item_text), withText("Elephant Odyssey"),
                        withParent(withParent(withId(R.id.pre_plan_items))),
                        isDisplayed()));
        textView4.check(matches(withText("Elephant Odyssey")));
    }

    @Test
    public void planAddTest() {
        ViewInteraction searchAutoComplete = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.search_item),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.plan_item_text), withText("Gorillas"),
                        withParent(withParent(withId(R.id.pre_plan_items))),
                        isDisplayed()));
        textView.check(matches(withText("Gorillas")));

        ViewInteraction searchAutoComplete2 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete2.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.search_item),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                1)));
        recyclerView2.perform(actionOnItemAtPosition(4, click()));

        ViewInteraction searchAutoComplete3 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete3.perform(click());

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.search_item),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                1)));
        recyclerView3.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.plan_item_text), withText("Lions"),
                        withParent(withParent(withId(R.id.pre_plan_items))),
                        isDisplayed()));
        textView3.check(matches(withText("Lions")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.plan_item_text), withText("Elephant Odyssey"),
                        withParent(withParent(withId(R.id.pre_plan_items))),
                        isDisplayed()));
        textView4.check(matches(withText("Elephant Odyssey")));

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.plan_btn), withText("plan"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.plan_item_text), withText("Gorillas (200 ft)"),
                        withParent(withParent(withId(R.id.plan_items))),
                        isDisplayed()));
        textView5.check(matches(withText("Gorillas (200 ft)")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.plan_item_text), withText("Lions (400 ft)"),
                        withParent(withParent(withId(R.id.plan_items))),
                        isDisplayed()));
        textView6.check(matches(withText("Lions (400 ft)")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.plan_item_text), withText("Elephant Odyssey (600 ft)"),
                        withParent(withParent(withId(R.id.plan_items))),
                        isDisplayed()));
        textView7.check(matches(withText("Elephant Odyssey (600 ft)")));
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

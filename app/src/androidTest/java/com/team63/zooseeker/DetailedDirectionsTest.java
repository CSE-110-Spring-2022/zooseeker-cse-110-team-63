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
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DetailedDirectionsTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void resetDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences preferences = context.getSharedPreferences("filenames", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("vertex_info", context.getString(R.string.vertex_info_v2));
        editor.putString("edge_info", context.getString(R.string.edge_info_v2));
        editor.putString("zoo_graph", context.getString(R.string.zoo_graph_v2));
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

    @AfterClass
    public static void resetDatabaseFromTest() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences preferences = context.getSharedPreferences("filenames", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("vertex_info", context.getString(R.string.vertex_info));
        editor.putString("edge_info", context.getString(R.string.edge_info));
        editor.putString("zoo_graph", context.getString(R.string.zoo_graph));
        editor.commit();
        NodeDatabase.resetDatabase(context);
    }

    ViewInteraction recyclerView = onView(
            allOf(withId(R.id.search_item),
                    childAtPosition(
                            withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                            1)));

    @Test
    public void detailedDirectionsTestNew() {
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
        recyclerView2.perform(actionOnItemAtPosition(6, click()));

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
        recyclerView3.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.plan_btn), withText("plan"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.get_directions_btn), withText("Get directions"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.exhibit_view), withText("Flamingos\n(90.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("Flamingos\n(90.0 ft)")));


        ViewInteraction textView3 = onView(
                allOf(withId(R.id.directions_view), withText("1: Proceed on Gate Path 10 ft towards Front Street.\n\n2: Proceed on Front Street 50 ft towards Monkey Trail.\n\n3: Proceed on Monkey Trail 30 ft towards Flamingos.\n\n"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView3.check(matches(withText("1: Proceed on Gate Path 10 ft towards Front Street.\n\n2: Proceed on Front Street 50 ft towards Monkey Trail.\n\n3: Proceed on Monkey Trail 30 ft towards Flamingos.\n\n")));


        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.next_exhibit_btn), withText("Next Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.exhibit_view), withText("Bali Mynah\n(205.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView5.check(matches(withText("Bali Mynah\n(205.0 ft)")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.directions_view), withText("1: Proceed on Monkey Trail 30 ft towards Front Street.\n\n2: Proceed on Front Street 50 ft towards Treetops Way.\n\n3: Proceed on Treetops Way 30 ft towards Fern Canyon Trail.\n\n4: Proceed on Fern Canyon Trail 60 ft towards Fern Canyon.\n\n5: Proceed on Aviary Trail 30 ft towards Owens Aviary Walkway.\n\n6: Proceed on Owens Aviary Walkway 5 ft towards Bali Mynah.\n\n"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView6.check(matches(withText("1: Proceed on Monkey Trail 30 ft towards Front Street.\n\n2: Proceed on Front Street 50 ft towards Treetops Way.\n\n3: Proceed on Treetops Way 30 ft towards Fern Canyon Trail.\n\n4: Proceed on Fern Canyon Trail 60 ft towards Fern Canyon.\n\n5: Proceed on Aviary Trail 30 ft towards Owens Aviary Walkway.\n\n6: Proceed on Owens Aviary Walkway 5 ft towards Bali Mynah.\n\n")));
        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.next_exhibit_btn), withText("Next Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.exhibit_view), withText("Crocodiles\n(155.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView7.check(matches(withText("Crocodiles\n(155.0 ft)")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.directions_view), withText("1: Proceed on Owens Aviary Walkway 5 ft towards Aviary Trail.\n\n2: Proceed on Aviary Trail 50 ft towards Treetops Way.\n\n3: Proceed on Treetops Way 60 ft towards Hippo Trail.\n\n4: Proceed on Hippo Trail 40 ft towards Crocodiles.\n\n"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView8.check(matches(withText("1: Proceed on Owens Aviary Walkway 5 ft towards Aviary Trail.\n\n2: Proceed on Aviary Trail 50 ft towards Treetops Way.\n\n3: Proceed on Treetops Way 60 ft towards Hippo Trail.\n\n4: Proceed on Hippo Trail 40 ft towards Crocodiles.\n\n")));

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.next_exhibit_btn), withText("Next Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.exhibit_view), withText("Entrance and Exit Gate\n(210.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView9.check(matches(withText("Entrance and Exit Gate\n(210.0 ft)")));

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.directions_view), withText("1: Proceed on Hippo Trail 40 ft towards Treetops Way.\n\n2: Proceed on Treetops Way 100 ft towards Gate Path.\n\n3: Proceed on Gate Path 10 ft towards Entrance and Exit Gate.\n\n"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView10.check(matches(withText("1: Proceed on Hippo Trail 40 ft towards Treetops Way.\n\n2: Proceed on Treetops Way 100 ft towards Gate Path.\n\n3: Proceed on Gate Path 10 ft towards Entrance and Exit Gate.\n\n")));
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

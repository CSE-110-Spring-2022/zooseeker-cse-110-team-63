package com.team63.zooseeker;


import static android.content.Context.MODE_PRIVATE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
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
public class SkipReplanTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

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
    public void skipTest() {
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

        recyclerView.perform(actionOnItemAtPosition(0, click()));

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
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

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

        ViewInteraction searchAutoComplete4 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete4.perform(click());

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.search_item),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                1)));
        recyclerView4.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction searchAutoComplete5 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete5.perform(click());

        ViewInteraction recyclerView5 = onView(
                allOf(withId(R.id.search_item),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                1)));
        recyclerView5.perform(actionOnItemAtPosition(4, click()));

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
                allOf(withId(R.id.directions_view), withText("1: Proceed on Entrance Way 10 ft towards Reptile Road.\n\n2: Proceed on Reptile Road 100 ft towards Alligators.\n\n"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("1: Proceed on Entrance Way 10 ft towards Reptile Road.\n\n2: Proceed on Reptile Road 100 ft towards Alligators.\n\n")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.exhibit_view), withText("Alligators\n(110.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView2.check(matches(withText("Alligators\n(110.0 ft)")));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.next_exhibit_btn), withText("Next Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.exhibit_view), withText("Lions\n(200.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView3.check(matches(withText("Lions\n(200.0 ft)")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.directions_view), withText("1: Proceed on Sharp Teeth Shortcut 200 ft towards Lions.\n\n"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView4.check(matches(withText("1: Proceed on Sharp Teeth Shortcut 200 ft towards Lions.\n\n")));

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.next_exhibit_btn), withText("Next Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.exhibit_view), withText("Elephant Odyssey\n(200.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView5.check(matches(withText("Elephant Odyssey\n(200.0 ft)")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.directions_view), withText("1: Proceed on Africa Rocks Street 200 ft towards Elephant Odyssey.\n\n"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView6.check(matches(withText("1: Proceed on Africa Rocks Street 200 ft towards Elephant Odyssey.\n\n")));

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.previous_exhibit_btn), withText("Previous Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.skip_exhibit_btn), withText("Skip Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(android.R.id.button1), withText("Yes"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton7.perform(scrollTo(), click());

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.exhibit_view), withText("Gorillas\n(300.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView7.check(matches(withText("Gorillas\n(300.0 ft)")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.directions_view), withText("1: Proceed on Reptile Road 100 ft towards Africa Rocks Street.\n\n2: Proceed on Africa Rocks Street 200 ft towards Gorillas.\n\n"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView8.check(matches(withText("1: Proceed on Reptile Road 100 ft towards Africa Rocks Street.\n\n2: Proceed on Africa Rocks Street 200 ft towards Gorillas.\n\n")));

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.previous_exhibit_btn), withText("Previous Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.exhibit_view), withText("Alligators\n(110.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView9.check(matches(withText("Alligators\n(110.0 ft)")));
    }

    @Test
    public void replanTest() {
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

        recyclerView.perform(actionOnItemAtPosition(0, click()));

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
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

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

        ViewInteraction searchAutoComplete4 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete4.perform(click());

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.search_item),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                1)));
        recyclerView4.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction searchAutoComplete5 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete5.perform(click());

        ViewInteraction recyclerView5 = onView(
                allOf(withId(R.id.search_item),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                1)));
        recyclerView5.perform(actionOnItemAtPosition(4, click()));

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
                allOf(withId(R.id.directions_view), withText("1: Proceed on Entrance Way 10 ft towards Reptile Road.\n\n2: Proceed on Reptile Road 100 ft towards Alligators.\n\n"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("1: Proceed on Entrance Way 10 ft towards Reptile Road.\n\n2: Proceed on Reptile Road 100 ft towards Alligators.\n\n")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.exhibit_view), withText("Alligators\n(110.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView2.check(matches(withText("Alligators\n(110.0 ft)")));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.next_exhibit_btn), withText("Next Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.exhibit_view), withText("Lions\n(200.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView3.check(matches(withText("Lions\n(200.0 ft)")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.directions_view), withText("1: Proceed on Sharp Teeth Shortcut 200 ft towards Lions.\n\n"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView4.check(matches(withText("1: Proceed on Sharp Teeth Shortcut 200 ft towards Lions.\n\n")));

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.next_exhibit_btn), withText("Next Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.exhibit_view), withText("Elephant Odyssey\n(200.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView5.check(matches(withText("Elephant Odyssey\n(200.0 ft)")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.directions_view), withText("1: Proceed on Africa Rocks Street 200 ft towards Elephant Odyssey.\n\n"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView6.check(matches(withText("1: Proceed on Africa Rocks Street 200 ft towards Elephant Odyssey.\n\n")));

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.previous_exhibit_btn), withText("Previous Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.skip_exhibit_btn), withText("Skip Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(android.R.id.button1), withText("Yes"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton7.perform(scrollTo(), click());

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.exhibit_view), withText("Gorillas\n(300.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView7.check(matches(withText("Gorillas\n(300.0 ft)")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.directions_view), withText("1: Proceed on Reptile Road 100 ft towards Africa Rocks Street.\n\n2: Proceed on Africa Rocks Street 200 ft towards Gorillas.\n\n"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView8.check(matches(withText("1: Proceed on Reptile Road 100 ft towards Africa Rocks Street.\n\n2: Proceed on Africa Rocks Street 200 ft towards Gorillas.\n\n")));

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.previous_exhibit_btn), withText("Previous Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.exhibit_view), withText("Alligators\n(110.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView9.check(matches(withText("Alligators\n(110.0 ft)")));

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.plan_btn), withText("Plan"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton9.perform(click());

        ViewInteraction materialButton10 = onView(
                allOf(withId(R.id.get_directions_btn), withText("Get directions"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton10.perform(click());

        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.next_exhibit_btn), withText("Next Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton11.perform(click());

        ViewInteraction materialButton12 = onView(
                allOf(withId(R.id.next_exhibit_btn), withText("Next Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton12.perform(click());

        ViewInteraction materialButton13 = onView(
                allOf(withId(R.id.previous_exhibit_btn), withText("Previous Exhibit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton13.perform(click());

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.exhibit_view), withText("Lions\n(200.0 ft)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView10.check(matches(withText("Lions\n(200.0 ft)")));
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

package com.team63.zooseeker;

import java.util.List;

/*
 * This interface describes a single Direction, that will be displayed in the Navigation user story
 * A single Direction describes all the steps required to get from one planned exhibit to another
 * When testing navigation screens, you can write a mock object implementing this interface
 */
public interface IDirection {
    String getName(); // the name of the destination exhibit, e.g. "Gorillas"
    int getDistance(); // in meters, doesn't require floating-point accuracy
    List<String> getTextDirection(); // returns a list of direction steps (in string form)
    /* Example of getTextDirection() output format:
     * ["Proceed on Treetops Way 160 ft to Hippo Trail", "Proceed on Hippo Trail 50 ft to the
     * Lost Forest Hippo Exhibit"]
     */
}

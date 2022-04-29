package com.team63.zooseeker;

/*
 * This interface describes a single Direction, that will be displayed in the Navigation user story
 * When testing navigation screens, you can write a mock object implementing this interface
 */
public interface IDirection {
    String getName(); // the name of the destination exhibit, e.g. "Gorillas"
    int getDistance(); // in meters, doesn't require floating-point accuracy
    String getTextDirection(); // the actual text direction, as a string
}

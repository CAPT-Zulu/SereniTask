package com.serenitask.model;

import java.time.Duration;
import java.time.LocalDate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Class representing a single day as an object. A day has a date, and a series of time windows of available time
 * for event allocation.
 */
public class Day {

    // Private parameters
    private int priority;
    private int freeTime;
    private boolean dateSet = false;
    private LocalDate startDate;
    private LocalDate endDate;
    private TimeWindow biggestWindow;

    // List containing all TimeWindows for the day
    List<TimeWindow> timeWindows;

    /**
     * Base Constructor; represents a single day on a date containing windows of free time
     */
    public Day() {
        priority = -1;
        freeTime = 0;
        timeWindows = new ArrayList<>();
    }

    /**
     * Adds a window of time to the day. A window has a start and an end. The free time of the add will be totaled
     * and added to the Day's free time.
     *
     * @param open  Localtime object representing the start of the window
     * @param close Localtime object representing the end of the window
     */
    public void addWindow(LocalTime open, LocalTime close) {
        TimeWindow newWindow = new TimeWindow(open, close);
        timeWindows.add(newWindow);
        freeTime = calculateFreeTime();
        System.out.println("new Free time: " + freeTime);
    }

    /**
     * Calculates the total free time for the day. Cycles through all time windows and sums their differences
     *
     * @return integer sum of total free time for day
     */
    private int calculateFreeTime() {
        int sum = 0;
        for (TimeWindow window : timeWindows) {
            sum += MINUTES.between(window.getWindowOpen(), window.getWindowClose());
        }
        return sum;
    }

    /**
     * Getter for Priority parameter
     *
     * @return integer representing the Days priority when ordered
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Setter for priority parameter
     *
     * @param priority integer value to set priority as
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Getter for free time parameter
     *
     * @return integer representing the days total free time
     */
    public int getFreeTime() {
        return freeTime;
    }

    /**
     * Setter for free time parameter
     *
     * @param freeTime integer value to set free time as
     */
    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    /**
     * Getter for dateSet boolean parameter
     *
     * @return True if the date field has been set, otherwise false
     */
    public boolean isDateSet() {
        return dateSet;
    }

    /**
     * Setter for dateSet parameter
     *
     * @param dateSet True or False
     */
    public void setDateSet(boolean dateSet) {
        this.dateSet = dateSet;
    }

    /**
     * Getter for biggest window parameter. Calculates biggest window prior to returning
     *
     * @return TimeWindow object representing the largest window of the day
     */
    public TimeWindow getBiggestWindow() {
        biggestWindow = findBiggestWindow();
        return biggestWindow;
    }

    /**
     * Getter for StartDate parameter
     *
     * @return LocalDate object representing days Start Date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Setter for StartDate parameter
     *
     * @param startDate LocalDate object to set as StartDate
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for EndDate parameter
     *
     * @return LocalDate object representing EndDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Setter for EndDate parameter
     *
     * @param endDate LocalDate object to set as endDate
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Function calculates the biggest window of the day. Parses all windows and returns the window with the largest
     * difference
     *
     * @return TimeWindow object representing the largest window of the day
     */
    private TimeWindow findBiggestWindow() {
        System.out.println("Windows in day: " + timeWindows.size());
        int maxDiff = 0;
        TimeWindow maxWindow = null;
        int index = 0;
        int indexer = 0;

        for (TimeWindow window : timeWindows) {
            Duration duration = Duration.between(window.getWindowOpen(), window.getWindowClose());
            System.out.println("window Duration: " + duration.getSeconds());
            int diff = (int) duration.getSeconds();
            if (diff > maxDiff) {
                index = indexer;
                maxDiff = diff;
                maxWindow = window;
            }
            indexer++;

        }
        if (!timeWindows.isEmpty()) {
            timeWindows.remove(index);
            return maxWindow;
        } else {
            return new TimeWindow(LocalTime.of(0, 0, 0), LocalTime.of(0, 0, 0));
        }

    }

    /**
     * Prints out validation data to ensure days are generating as required.
     * Debug only, will be removed for final release
     */
    public void validate() {
        System.out.println("Priority: " + priority);
        System.out.println("Free time: " + freeTime);
        System.out.println("Date Start: " + startDate);
        System.out.println("Date End: " + endDate);
        System.out.println("Date Set: " + dateSet);

        System.out.println("Biggest Window: " + biggestWindow);
        System.out.println("Windows: ");
        int index = 1;
        for (TimeWindow window : timeWindows) {
            System.out.println("Window " + index + " - start: " + window.getWindowOpen() + " - to close: " + window.getWindowClose());
        }
    }
}
package com.serenitask.controller;


import com.serenitask.model.*;
import com.serenitask.util.DatabaseManager.EventDAO;
import com.serenitask.util.DatabaseManager.GoalDAO;
import javafx.stage.Window;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RoutineOneController {

    // Dummy value of 7, will be replaced with 28 for 1 month or ~4 weeks
    private int allocationThreshold = 7;
    private LocalDate today = LocalDate.now();
    private LocalTime DayStart = LocalTime.of(8, 0, 0);
    private LocalTime DayEnd = LocalTime.of(18, 0, 0);

    private int stdBlock = 7;
    // Dummy variables setup

    // dummy goals
    DummyGoal goalOne = new DummyGoal("Workout", "null", 30, 120, 0, "never", "weekly");
    DummyGoal goalTwo = new DummyGoal("Study", "null", 30, 120, 0, "never", "weekly");
    DummyGoal goalThree = new DummyGoal("Read", "null", 30, 60, 0, "never", "weekly");


    // // Stages


    public void runRoutine() {
        EventDAO eventDAO = new EventDAO();
        List<Event> eventList = eventDAO.getAllEvents();

        List<DummyGoal> goalList = getGoalList();
        List<DummyGoal> parsedList = parseGoalList(goalList);

        for (DummyGoal goal : parsedList) {
            allocateGoal(goal, eventList);
        }
    }

    // 1. Pull list of time base-based goals
    private List<DummyGoal> getGoalList() {
        List<DummyGoal> goalList = new ArrayList<>();
        goalList.add(goalOne);
        goalList.add(goalTwo);

        return goalList;
    }

    ;

    // 2. Parse list and remove any that exceed the allocation data threshold today + ~4weeks
    private List<DummyGoal> parseGoalList(List<DummyGoal> goalList) {
        LocalDate targetDate = getTargetDate();
        List<DummyGoal> parsedList = new ArrayList<>();

        // only add goals that need allocating, drop the rest
        for (DummyGoal goal : goalList) {
            // If difference between targetDate and allocated is positive
            // then there are unallocated days
            int difference = targetDate.compareTo(goal.getAllocatedUpTo());
            if (difference > 0) {
                parsedList.add(goal);
                goal.setDaysOutstanding(difference);
            }
        }
        return parsedList;
    }

    ;

    // 3. Will now have a list of time based goals that need to be implemented
    // from (stored_date -> threshold_date)
    // use a ceiling type method, must allocate up until a sunday. so could be (4 weeks -- 4 weeks + 6 days)
    // allocate time based goals to fill windows

    private void allocateGoal(DummyGoal goal, List<Event> eventList) {
        //LocalDate allocationStart = goal.getAllocatedUpTo().plusDays(1);

        // Get the events list, Create a new list for each block,
        // If start date on block start to block end
        // then sort into days
        // then prep next stage

        int firstBlock = getDifferenceSunday(goal.getAllocatedUpTo());

        if (firstBlock != 7) {

            allocatePartialWeek(firstBlock, goal, eventList);
            goal.subtractDaysOutstanding(firstBlock);
        }

        while (goal.getDaysOutstanding() > 7) {
            allocateWeek(goal);
        }

    }

    private void allocatePartialWeek(int days, DummyGoal goal, List<Event> eventList) {
        int blockTarget = (int) Math.floor(goal.getGoalTargetAmount() * ((double) days / 7));
        LocalDate allocationStart = goal.getAllocatedUpTo().plusDays(1);
        LocalDate allocationEnd = getNextSunday(allocationStart);

        List<List<Event>> rawDaysLists = splitDays(allocationStart, allocationEnd, eventList);

        // Days are in order of their priority
        List<Day> prioritizedDays = getDaysList(rawDaysLists);

        while (blockTarget > 0) {
            for (Day day : prioritizedDays) {
                TimeWindow window = day.getBiggestWindow();
                Duration duration = Duration.between(window.getWindowOpen(), window.getWindowClose());
                int windowMins = (int) (duration.getSeconds() / 60);

                if (windowMins > goal.getMinChunk()) {
                    if (windowMins > goal.getMaxChunk()) {
                        int middlePoint = (int) (duration.getSeconds() / 2);
                        LocalTime middleTime = window.getWindowOpen().plusSeconds(middlePoint);
                        LocalTime startTime = middleTime.minusMinutes(60);
                        LocalTime EndTime = middleTime.plusMinutes(60);

                        // Create new event with times above
                        // get all details from goal class
                        // remove allocated time (120) here from blockTarget
                    }


                }


            }
        }

        // pull Database from allocationStart to allocationStart.plusdays(days);
        // load in a list of lists
    }


    private List<Day> getDaysList(List<List<Event>> rawDaysLists) {
        List<Day> daysList = new ArrayList<>();
        for (List<Event> list : rawDaysLists) {
            Day newDay = new Day();
            List<Event> sortedList = getSortedList(list);

            LocalTime windowStart = DayStart;

            for (Event event : sortedList) {
                if (windowStart.isBefore(DayEnd)) {
                    if (windowStart.compareTo(event.getStartTime().toLocalTime()) > 0) {
                        newDay.addWindow(windowStart, event.getStartTime().toLocalTime());
                    } else {
                        windowStart = event.getStartTime().toLocalTime().plusSeconds(event.getDuration());
                    }
                }
            }
            daysList.add(newDay);
        }
        return prioritizeDays(daysList);
    }


    private List<Day> prioritizeDays(List<Day> rawDays) {

        // sorting by free time;
        List<Day> sorted = new ArrayList<>();
        int highestPrio = rawDays.size();

        while (!rawDays.isEmpty()) {
            int freeMinutes = 0;
            Day newDay = new Day();

            for (Day day : rawDays) {

                if (day.getFreeTime() > freeMinutes) {
                    newDay = day;
                }
            }

            // set the priority for that day
            newDay.setPriority(highestPrio);
            highestPrio--;

            // add to sorted and remove from unsortedlist
            sorted.add(newDay);
            rawDays.remove(newDay);
        }
        return sorted;
    }

    private List<Event> getSortedList(List<Event> unsortedlist) {
        List<Event> sorted = new ArrayList<>();

        while (!unsortedlist.isEmpty()) {
            LocalTime minTime = LocalTime.of(23, 59);
            Event earliestEvent = new Event();

            for (Event event : unsortedlist) {
                if (event.getStartTime().toLocalTime().compareTo(minTime) >= 0) {
                    earliestEvent = event;
                }
            }
            // add to sorted and remove from unsortedlist
            sorted.add(earliestEvent);
            unsortedlist.remove(earliestEvent);
        }
        return sorted;
    }

    private List<List<Event>> splitDays(LocalDate start, LocalDate end, List<Event> eventList) {
        List<List<Event>> dayLists = new ArrayList<>();
        while (end.compareTo(start) > 0) {
            List<Event> dayList = new ArrayList<>();
            for (Event event : eventList) {
                // What if a day
                if (event.getStartDate() == start || event.getEndDate() == start) {
                    dayList.add(event);
                }
            }
            dayLists.add(dayList);
        }

        return dayLists;
    }

    private void allocateWeek(DummyGoal goal) {

        goal.subtractDaysOutstanding(stdBlock);


        // load in a list of lists


    }

    // Helpers
    private LocalDate getTargetDate() {
        LocalDate targetDate = LocalDate.now().plusDays(allocationThreshold);

        return getNextSunday(targetDate);
    }

    private LocalDate getNextSunday(LocalDate startDate) {
        LocalDate sunday = startDate;

        while (sunday.getDayOfWeek().getValue() != 7) {
            sunday = sunday.plusDays(1);
        }

        return sunday;
    }


    private int getDifferenceSunday(LocalDate date) {
        LocalDate targetDate = date;

        while (targetDate.getDayOfWeek().getValue() != 7) {
            targetDate = targetDate.plusDays(1);
        }

        int difference = targetDate.compareTo(date);
        return difference;
    }


}

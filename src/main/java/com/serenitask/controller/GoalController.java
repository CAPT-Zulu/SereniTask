package com.serenitask.controller;

import com.serenitask.util.DatabaseManager.SqliteConnection;
import com.serenitask.util.DatabaseManager.GoalDAO;
import com.serenitask.model.Goal;

import java.util.ArrayList;
import java.util.List;


public class GoalController {

    public List<String> loadSimpleGoal()
    {
        GoalDAO goalDAO = new GoalDAO();

        List<Goal> goalList= goalDAO.getAllGoals();
        List<String> stringList = new ArrayList<>();
        for(Goal goal : goalList)
        {
            stringList.add(goal.getTitle());
        }
        return stringList;
    }

    public void controlSimpleGoal(String title) {

        Goal goal = new Goal(title);
        GoalDAO goalDAO = new GoalDAO();
        goalDAO.addGoal(goal);
    }


}
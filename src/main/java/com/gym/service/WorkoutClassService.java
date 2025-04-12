package com.gym.service;

import com.gym.dao.WorkoutClassDAO;
import com.gym.model.WorkoutClass;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class WorkoutClassService {
    private final WorkoutClassDAO workoutClassDAO;

    public WorkoutClassService(Connection connection) {
        this.workoutClassDAO = new WorkoutClassDAO(connection);
    }

    public void addWorkoutclass(WorkoutClass workoutClass) throws SQLException {
        workoutClassDAO.addWorkoutClass(workoutClass);
    }

    public List<WorkoutClass> getAllWorkoutClasses() throws SQLException {
        return workoutClassDAO.getAllWorkoutClasses();
    }
}

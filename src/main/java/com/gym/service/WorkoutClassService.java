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

    public void addWorkoutClass(WorkoutClass workoutClass) throws SQLException {
        workoutClassDAO.addWorkoutClass(workoutClass);
    }

    public List<WorkoutClass> getAllWorkoutClasses() throws SQLException {
        return workoutClassDAO.getAllWorkoutClasses();
    }

    public List<WorkoutClass> getWorkoutClassesByTrainerId(int trainerId) throws SQLException {
        return workoutClassDAO.getWorkoutClassesByTrainerId(trainerId);
    }

    public void deleteWorkoutClass(int workoutClassId) throws SQLException {
        workoutClassDAO.deleteWorkoutClass(workoutClassId);
    }
}

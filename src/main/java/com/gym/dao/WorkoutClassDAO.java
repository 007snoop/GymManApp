package com.gym.dao;

import com.gym.model.WorkoutClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutClassDAO {
    private final Connection connection;

    public WorkoutClassDAO(Connection connection) {
        this.connection = connection;
    }

    public void addWorkoutClass(WorkoutClass wc) throws SQLException {
        String SQL = """
                INSERT INTO workout_classes (
                workout_class_type, workout_class_desc, trainer_id
                ) VALUES (?,?,?)""";

        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, wc.getWorkoutClassType());
            stmt.setString(2, wc.getWorkoutClassDesc());
            stmt.setInt(3, wc.getTrainerId());
            stmt.executeUpdate();
        }
    }

    public List<WorkoutClass> getAllWorkoutClasses() throws SQLException {
        List<WorkoutClass> classes = new ArrayList<>();
        String SQL = """
                SELECT *
                FROM
                workout_classes""";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            while (rs.next()) {
                WorkoutClass wc = new WorkoutClass(
                        rs.getInt("workout_class_id"),
                        rs.getString("workout_class_type"),
                        rs.getString("workout_class_desc"),
                        rs.getInt("trainer_id")
                );
                classes.add(wc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }
}

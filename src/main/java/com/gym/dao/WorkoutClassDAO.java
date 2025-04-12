package com.gym.dao;

import com.gym.model.WorkoutClass;

import java.security.PublicKey;
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

    public List<WorkoutClass> getWorkoutClassesByTrainerId(int trainerId) throws SQLException {
        List<WorkoutClass> classes = new ArrayList<>();
        String SQL = """
                SELECT *
                FROM workout_classes
                WHERE trainer_id = ?""";

        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, trainerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                WorkoutClass wc = new WorkoutClass(
                        rs.getInt("workout_class_id"),
                        rs.getString("workout_class_type"),
                        rs.getString("workout_class_desc"),
                        rs.getInt("trainer_id")
                );
                classes.add(wc);
            }
        }

        return classes;
    }

    public void deleteWorkoutClass(int workoutClassId) throws SQLException {
        String SQL = """
                DELETE FROM
                workout_classes WHERE
                workout_class_id = ?""";

        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, workoutClassId);
            stmt.executeUpdate();
        }
    }

}

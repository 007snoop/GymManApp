package com.gym.dao;

import com.gym.model.WorkoutClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberWorkoutClassDAO {
    private Connection connection;

    public MemberWorkoutClassDAO(Connection connection) {
        this.connection = connection;
    }

    public void enrollMemberInClass(int memberId, int classId) throws SQLException {
        String SQL = """
                INSERT INTO member_workout_classes(
                member_id, workout_class_id
                ) VALUES (?,?)""";

        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, memberId);
            stmt.setInt(2, classId);
            stmt.executeUpdate();
        }
    }

    public List<WorkoutClass> getEnrolledClasses(int memberId) throws SQLException {
        String SQL = """
                SELECT wc.*
                FROM workout_classes wc
                JOIN member_workout_classes mwc ON wc.workout_class_id = wmc.workout_class_id
                WHERE mwc.member_id = ?
                """;

        List<WorkoutClass> classes = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, memberId);
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

    public List<WorkoutClass> getEnrolledClassesByMemberId(int memberId) throws SQLException {
        List<WorkoutClass> classes = new ArrayList<>();

        String SQL = """
                SELECT wc.*
                FROM workout_classes wc
                JOIN member_workout_classes mwc
                ON wc.workout_class_id = mwc.workout_class_id
                WHERE mwc.member_id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, memberId);

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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classes;
    }

}

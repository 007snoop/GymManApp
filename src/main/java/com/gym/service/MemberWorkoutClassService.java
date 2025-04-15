package com.gym.service;

import com.gym.dao.MemberWorkoutClassDAO;
import com.gym.model.WorkoutClass;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MemberWorkoutClassService {
    private MemberWorkoutClassDAO memberWorkoutClassDAO;

    public MemberWorkoutClassService(Connection connection) {
        this.memberWorkoutClassDAO = new MemberWorkoutClassDAO(connection);
    }

    public void enroll(int memberId, int classId) throws SQLException {
        memberWorkoutClassDAO.enrollMemberInClass(memberId,classId);
    }

    public List<WorkoutClass> getEnrolledClasses(int memberId) throws SQLException{
        return memberWorkoutClassDAO.getEnrolledClasses(memberId);
    }
}

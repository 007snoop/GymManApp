package com.gym.dao;

import com.gym.model.Membership;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MembershipDAO {
    private final Connection connection;

    public MembershipDAO(Connection connection) {
        this.connection = connection;
    }

    public void addMembership(Membership membership) throws SQLException {
        String SQL = """
                INSERT INTO memberships (
                membership_type,
                membership_desc,
                membership_cost,
                member_id
                ) VALUES
                (?,?,?,?)""";

        try (PreparedStatement stmt = connection.prepareStatement(SQL)){
            stmt.setString(1, membership.getMembershipType());
            stmt.setString(2, membership.getMembershipDesc());
            stmt.setDouble(3, membership.getMembershipCost());
            stmt.setInt(4, membership.getMemberId());
            stmt.executeUpdate();
        }
    }

    public List<Membership> getAllMemberships() throws SQLException {
        List<Membership> memberships = new ArrayList<>();

        String SQL = """
                SELECT *
                FROM 
                memberships""";

        try (PreparedStatement stmt = connection.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                memberships.add(new Membership(
                        rs.getInt("membership_id"),
                        rs.getString("membership_type"),
                        rs.getString("membership_desc"),
                        rs.getDouble("membership_cost"),
                        rs.getInt("member_id")
                ));
            }
        }
        return memberships;
    }

    public double getTotalRevenue() throws SQLException {
        String SQL = """
                SELECT SUM(
                membership_cost) AS
                total FROM
                memberships""";
        try (PreparedStatement stmt = connection.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getDouble("total");
        }
        return 0.0;
    }
}

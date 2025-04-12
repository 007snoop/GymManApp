package com.gym.service;

import com.gym.dao.MembershipDAO;
import com.gym.model.Membership;

import java.sql.SQLException;
import java.util.List;

public class MembershipService {
    private final MembershipDAO membershipDAO;

    public MembershipService(MembershipDAO membershipDAO) {
        this.membershipDAO = membershipDAO;
    }

    public void purchaseMembership(Membership membership) throws SQLException {
        membershipDAO.addMembership(membership);
    }

    public List<Membership> listAllMemberships() throws SQLException {
        return membershipDAO.getAllMemberships();
    }

    public double getTotalRevenue() throws SQLException {
        return membershipDAO.getTotalRevenue();
    }
}

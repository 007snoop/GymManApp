package com.gym.model;

public class Membership {
    private int membershipId;
    private String membershipType;
    private String membershipDesc;
    private double membershipCost;
    private int memberId; // fk to user_id in users table

    public Membership(int membershipId, String membershipType, String membershipDesc, double membershipCost,
                      int memberId) {
        this.membershipId = membershipId;
        this.membershipType = membershipType;
        this.membershipDesc = membershipDesc;
        this.membershipCost = membershipCost;
        this.memberId = memberId;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public double getMembershipCost() {
        return membershipCost;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getMembershipDesc() {
        return membershipDesc;
    }

    public String getMembershipType() {
        return membershipType;
    }
}

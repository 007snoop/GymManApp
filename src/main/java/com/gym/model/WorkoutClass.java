package com.gym.model;

public class WorkoutClass {
    private int workoutClassId;
    private String workoutClassType;
    private String workoutClassDesc;
    private int trainerId;

    public WorkoutClass(int workoutClassId, String workoutClassType, String workoutClassDesc, int trainerId) {
        this.workoutClassId = workoutClassId;
        this.workoutClassType = workoutClassType;
        this.workoutClassDesc = workoutClassDesc;
        this.trainerId = trainerId;
    }

    public int getWorkoutClassId() {
        return workoutClassId;
    }

    public void setWorkoutClassId(int workoutClassId) {
        this.workoutClassId = workoutClassId;
    }

    public String getWorkoutClassType() {
        return workoutClassType;
    }

    public void setWorkoutClassType(String workoutClassType) {
        this.workoutClassType = workoutClassType;
    }

    public String getWorkoutClassDesc() {
        return workoutClassDesc;
    }

    public void setWorkoutClassDesc(String workoutClassDesc) {
        this.workoutClassDesc = workoutClassDesc;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }
}

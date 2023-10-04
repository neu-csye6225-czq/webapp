package com.neu.csye6225.webapp.entity.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class AssignmentRequestBody {
    private String name;
    private Integer points;
    private Integer numOfAttempts;
    private Date deadline;

    @JsonCreator
    public AssignmentRequestBody(@JsonProperty("name") String name,
                                 @JsonProperty("points") int points,
                                 @JsonProperty("num_of_attempts") int numOfAttempts,
                                 @JsonProperty("deadline") Date deadline) {
        this.name = name;
        this.points = points;
        this.numOfAttempts = numOfAttempts;
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Integer getNumOfAttempts() {
        return numOfAttempts;
    }

    public void setNumOfAttempts(int numOfAttempts) {
        this.numOfAttempts = numOfAttempts;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}

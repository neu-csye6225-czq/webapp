package com.neu.csye6225.webapp.entity.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "assignments")
public class Assignment {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "assignment_id")
    private String id;

    @Column(unique = true)
    private String name;

    private int points;

    @Column(name = "num_of_attempts")
    private int numOfAttempts;

    private Date deadline;

    @Column(name = "assignment_created")
    private Date assignmentCreated;

    @Column(name = "assignment_updated")
    private Date assignmentUpdated;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getNumOfAttempts() {
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

    public Date getAssignmentCreated() {
        return assignmentCreated;
    }

    public void setAssignmentCreated(Date assignmentCreated) {
        this.assignmentCreated = assignmentCreated;
    }

    public Date getAssignmentUpdated() {
        return assignmentUpdated;
    }

    public void setAssignmentUpdated(Date assignmentUpdated) {
        this.assignmentUpdated = assignmentUpdated;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

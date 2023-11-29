package com.neu.csye6225.webapp.entity.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @ManyToOne
    @JoinColumn(name = "assigment_id")
    @JsonProperty("assigment_id")
    private Assignment assignment;

    @Column(name = "submission_url")
    private String submissionUrl;

    @Column(name = "submission_date")
    private Date submissionDate;

    @Column(name = "submission_updated")
    private Date submissionUpdated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public String getSubmissionUrl() {
        return submissionUrl;
    }

    public void setSubmissionUrl(String submissionUrl) {
        this.submissionUrl = submissionUrl;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Date getSubmissionUpdated() {
        return submissionUpdated;
    }

    public void setSubmissionUpdated(Date submissionUpdated) {
        this.submissionUpdated = submissionUpdated;
    }
}

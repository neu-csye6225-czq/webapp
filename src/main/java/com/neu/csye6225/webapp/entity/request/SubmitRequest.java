package com.neu.csye6225.webapp.entity.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmitRequest {
    private String submission_url;

    @JsonCreator
    SubmitRequest(@JsonProperty("submission_url") String submission_url) {
        this.submission_url = submission_url;
    }

    public String getSubmission_url() {
        return submission_url;
    }

    public void setSubmission_url(String submission_url) {
        this.submission_url = submission_url;
    }
}

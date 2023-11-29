package com.neu.csye6225.webapp.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SNSService {
    private final AmazonSNS amazonSNS;

    public SNSService(@Value("${aws.sns.region}") String region) {
        this.amazonSNS = AmazonSNSClientBuilder.standard().withRegion(region).build();
    }

    public void sendMessageToSnsTopic(String topicArn, String url, String email) {
        String message = "User email: " + email + " Submission Url: " + url;

        PublishRequest request = new PublishRequest()
                .withTopicArn(topicArn)
                .withMessage(message);

        amazonSNS.publish(request);
    }

}

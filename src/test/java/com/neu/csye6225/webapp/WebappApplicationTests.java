package com.neu.csye6225.webapp;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebappApplicationTests {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testHealthzEndpoint() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/healthz", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
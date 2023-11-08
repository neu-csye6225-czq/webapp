package com.neu.csye6225.webapp.controller;

import com.neu.csye6225.webapp.service.HealthService;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class HealthController {
    private final HealthService healthService;

    private final Logger logger = LoggerFactory.getLogger(HealthController.class);

    private StatsDClient statsD = new NonBlockingStatsDClient("webapp.healthz", "localhost", 8125);

    @Autowired
    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @RequestMapping(value = "/healthz")
    public ResponseEntity<String> healthz(HttpServletRequest request,
                                          @RequestBody(required = false) String body) {
        logger.info("A Health check is called");
        statsD.incrementCounter("total");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Cache-Control", "no-cache, no-store, must-revalidate");
        httpHeaders.set("Pragma", "no-cache");
        httpHeaders.set("X-Content-Type-Options", "nosniff");

        if (!healthService.testDatabaseConnection()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).headers(httpHeaders).build();
        } else if (!request.getMethod().equals("GET")) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).headers(httpHeaders).build();
        } else if (body != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(httpHeaders).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).build();
        }
    }
}

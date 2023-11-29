package com.neu.csye6225.webapp.controller;

import com.neu.csye6225.webapp.dao.AccountDao;
import com.neu.csye6225.webapp.dao.AssignmentDao;
import com.neu.csye6225.webapp.dao.SubmissionDao;
import com.neu.csye6225.webapp.entity.db.Account;
import com.neu.csye6225.webapp.entity.db.Assignment;
import com.neu.csye6225.webapp.entity.db.Submission;
import com.neu.csye6225.webapp.entity.request.AssignmentRequestBody;
import com.neu.csye6225.webapp.entity.request.SubmitRequest;
import com.neu.csye6225.webapp.service.SNSService;
import com.neu.csye6225.webapp.util.Utils;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;

@Controller
public class AssignmentController {
    private final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    private StatsDClient statsD = new NonBlockingStatsDClient("webapp.assignment", "localhost", 8125);

    @Autowired
    AssignmentDao assignmentDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    SubmissionDao submissionDao;

    @Autowired
    SNSService service;

    private boolean checkRequestBody(AssignmentRequestBody requestBody) {
//        System.out.println(11111);
        if (requestBody == null) return false;
//        System.out.println(22222);
        if (requestBody.getName() == null || requestBody.getDeadline() == null
                || requestBody.getPoints() == null || requestBody.getNumOfAttempts() == null) return false;
        if (Utils.inRange(requestBody.getNumOfAttempts())
                && Utils.inRange(requestBody.getPoints())) return true;
        return false;
    }


    @RequestMapping(value = "/v1/assignments", method = RequestMethod.GET)
    public ResponseEntity<Set<Assignment>> getAssignmentList(
            @RequestHeader String Authorization,
            @RequestBody(required = false) String body) {
        logger.info("Get list of assignments is called.");
        statsD.incrementCounter("getAssigmentList.total");
        Account account = accountDao.getAccountByToken(Authorization);
        if (body != null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (account == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        logger.info(String.format("Account %s getting assignments' info.", account.getEmail()));
        return new ResponseEntity<>(account.getAssignments(), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/assignments", method = RequestMethod.POST)
    public ResponseEntity<Assignment> createAssignment(@RequestHeader String Authorization,
                                                        @RequestBody AssignmentRequestBody requestBody) {
        logger.info("Create assignment is called.");
        statsD.incrementCounter("createAssigment.total");
        Account account = accountDao.getAccountByToken(Authorization);
        if (account == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (!checkRequestBody(requestBody)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Assignment assignment = assignmentDao.getAssignmentByName(requestBody.getName());
        if (assignment != null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        assignment = new Assignment();
        assignment.setName(requestBody.getName());
        assignment.setPoints(requestBody.getPoints());
        assignment.setNumOfAttempts(requestBody.getNumOfAttempts());
        assignment.setDeadline(requestBody.getDeadline());
        Date date = new Date();
        assignment.setAssignmentCreated(date);
        assignment.setAssignmentUpdated(date);
        assignment.setAccount(account);
        assignmentDao.save(assignment);

        logger.info(String.format("Account %s create a new assignment %s", account.getEmail(), assignment.getName()));

        return new ResponseEntity<>(assignment, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/v1/assignments/{id}", method = RequestMethod.GET)
    public ResponseEntity<Assignment> findAssignment(@RequestHeader String Authorization,
                                                     @PathVariable("id") String id,
                                                     @RequestBody(required = false) String body) {
        logger.info("Get assignment info is called.");
        statsD.incrementCounter("getAssigmentInfo.total");
        Account account = accountDao.getAccountByToken(Authorization);
        if (account == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (!Utils.isValidUUID(id))  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (body != null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Assignment assignment = assignmentDao.getAssignmentById(id);
        if (assignment == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        System.out.println(account.getId());
        System.out.println(assignment.getAccount().getId());
        if (!assignment.getAccount().getId().equals(account.getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        logger.info(String.format("Account %s get assignment %s info", account.getEmail(), assignment.getName()));
        return new ResponseEntity<>(assignment, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/assignments/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Assignment> deleteAssignment(@RequestHeader String Authorization,
                                                       @PathVariable("id") String id,
                                                       @RequestBody(required = false) String body) {
        logger.info("Delete assignment is called.");
        statsD.incrementCounter("deleteAssigment.total");
        Account account = accountDao.getAccountByToken(Authorization);
        if (account == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (body != null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!Utils.isValidUUID(id))  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Assignment assignment = assignmentDao.getAssignmentById(id);
        if (assignment == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!assignment.getAccount().getId().equals(account.getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        assignmentDao.delete(assignment);
        logger.info(String.format("Account %s delete assignment %s", account.getEmail(), assignment.getName()));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/v1/assignments/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Assignment> updateAssignment(@RequestHeader String Authorization,
                                                       @PathVariable("id") String id,
                                                       @RequestBody AssignmentRequestBody requestBody) {
        logger.info("Update assignment is called.");
        statsD.incrementCounter("updateAssigment.total");
        Account account = accountDao.getAccountByToken(Authorization);
        if (account == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (!Utils.isValidUUID(id))  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!checkRequestBody(requestBody)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Assignment assignment = assignmentDao.getAssignmentById(id);
        if (assignment == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!assignment.getAccount().getId().equals(account.getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Assignment assignment1 = assignmentDao.getAssignmentByName(requestBody.getName());
        if (assignment1 != null && !assignment1.getId().equals(id))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        assignment.setName(requestBody.getName());
        assignment.setPoints(requestBody.getPoints());
        assignment.setNumOfAttempts(requestBody.getNumOfAttempts());
        assignment.setDeadline(requestBody.getDeadline());
        Date date = new Date();
        assignment.setAssignmentUpdated(date);
        assignmentDao.update(assignment);
        logger.info(String.format("Account %s update assignment %s", account.getEmail(), assignment.getName()));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @RequestMapping(value = "/v1/assignments/{id}/submission", method = RequestMethod.POST)
    public ResponseEntity<Submission> submit(@RequestHeader String Authorization,
                                             @PathVariable("id") String id,
                                             @RequestBody SubmitRequest requestBody,
                                             @Value("${aws.topic.arn}") String arn) {
        logger.info("Submit assignment is called.");
        statsD.incrementCounter("submitAssigment.total");

        Account account = accountDao.getAccountByToken(Authorization);
        if (account == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (!Utils.isValidUUID(id))  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Assignment assignment = assignmentDao.getAssignmentById(id);
        if (assignment == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!assignment.getAccount().getId().equals(account.getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if (requestBody == null || requestBody.getSubmission_url() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        Date curDate = new Date();
        if (curDate.after(assignment.getDeadline())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (assignment.getNumOfAttempts() <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        assignment.setNumOfAttempts(assignment.getNumOfAttempts() - 1);
        assignmentDao.update(assignment);

        Submission submission = submissionDao.getByAssigmentId(assignment.getId());

        if (submission == null) {
            submission = new Submission();
            submission.setAssignment(assignment);
            submission.setSubmissionDate(curDate);
            submission.setSubmissionUpdated(curDate);
            submission.setSubmissionUrl(requestBody.getSubmission_url());
            submissionDao.save(submission);
        }

        submission.setSubmissionUpdated(curDate);
        submissionDao.update(submission);

        service.sendMessageToSnsTopic(arn, submission.getSubmissionUrl(), account.getEmail());

        return new ResponseEntity<>(submission, HttpStatus.CREATED);

    }
}

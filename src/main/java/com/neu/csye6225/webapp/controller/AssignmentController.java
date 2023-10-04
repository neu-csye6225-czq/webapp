package com.neu.csye6225.webapp.controller;

import com.neu.csye6225.webapp.dao.AccountDao;
import com.neu.csye6225.webapp.dao.AssignmentDao;
import com.neu.csye6225.webapp.entity.db.Account;
import com.neu.csye6225.webapp.entity.db.Assignment;
import com.neu.csye6225.webapp.entity.request.AssignmentRequestBody;
import com.neu.csye6225.webapp.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Controller
public class AssignmentController {
    @Autowired
    AssignmentDao assignmentDao;

    @Autowired
    AccountDao accountDao;

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
        Account account = accountDao.getAccountByToken(Authorization);
        if (body != null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (account == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(account.getAssignments(), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/assignments", method = RequestMethod.POST)
    public ResponseEntity<Assignment> createAssignment(@RequestHeader String Authorization,
                                                        @RequestBody AssignmentRequestBody requestBody) {
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

        return new ResponseEntity<>(assignment, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/v1/assignments/{id}", method = RequestMethod.GET)
    public ResponseEntity<Assignment> findAssignment(@RequestHeader String Authorization,
                                                     @PathVariable("id") String id) {
        Account account = accountDao.getAccountByToken(Authorization);
        if (account == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (!Utils.isValidUUID(id))  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Assignment assignment = assignmentDao.getAssignmentById(id);
        if (assignment == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        System.out.println(account.getId());
        System.out.println(assignment.getAccount().getId());
        if (!assignment.getAccount().getId().equals(account.getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(assignment, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/assignments/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Assignment> deleteAssignment(@RequestHeader String Authorization,
                                                       @PathVariable("id") String id) {
        Account account = accountDao.getAccountByToken(Authorization);
        if (account == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (!Utils.isValidUUID(id))  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Assignment assignment = assignmentDao.getAssignmentById(id);
        if (assignment == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!assignment.getAccount().getId().equals(account.getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        assignmentDao.delete(assignment);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/v1/assignments/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Assignment> updateAssignment(@RequestHeader String Authorization,
                                                       @PathVariable("id") String id,
                                                       @RequestBody AssignmentRequestBody requestBody) {
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
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

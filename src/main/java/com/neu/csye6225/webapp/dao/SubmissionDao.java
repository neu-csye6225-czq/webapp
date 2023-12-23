package com.neu.csye6225.webapp.dao;


import com.neu.csye6225.webapp.entity.db.Assignment;
import com.neu.csye6225.webapp.entity.db.Submission;
import org.hibernate.PersistentObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class SubmissionDao {
    @Autowired
    SessionFactory sessionFactory;

//    public Submission getByAssigmentId(String id) {
//        Session session = sessionFactory.openSession();
//        session = sessionFactory.openSession();
//        Transaction transaction = session.beginTransaction();
//        Query query = session.createQuery("from Submission where assignment = :assigment");
//        query.setParameter("assigment", id);
//        Submission submission = (Submission) query.uniqueResult();
//        transaction.commit();
//        session.close();
//        return submission;
//    }

    public boolean save(Submission submission) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(submission);
            session.getTransaction().commit();
        } catch (ConstraintViolationException | PersistentObjectException | IllegalStateException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        } finally {
            if (session != null) session.close();
        }
        return true;
    }

    public boolean update(Submission submission) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(submission);
            session.getTransaction().commit();
        } catch (ConstraintViolationException | PersistentObjectException | IllegalStateException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        } finally {
            if (session != null) session.close();
        }
        return true;
    }
}

package com.neu.csye6225.webapp.dao;

import com.neu.csye6225.webapp.entity.db.Account;
import com.neu.csye6225.webapp.entity.db.Assignment;
import org.hibernate.PersistentObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AssignmentDao {
    @Autowired
    private SessionFactory sessionFactory;

    public Assignment getAssignmentById(String id) {
        Session session = sessionFactory.openSession();
        Assignment assignment = session.get(Assignment.class, id);
        session.close();
        return assignment;
    }

    public Assignment getAssignmentByName(String name) {
        Session session = null;
        session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from Assignment where name = :name");
        query.setParameter("name", name);
        Assignment assignment = (Assignment) query.uniqueResult();
        transaction.commit();
        System.out.println(assignment);
        if (session != null) session.close();
        return assignment;
    }

    public boolean save(Assignment assignment) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(assignment);
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

    public boolean update(Assignment assignment) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(assignment);
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

    public boolean delete(Assignment assignment) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(assignment);
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

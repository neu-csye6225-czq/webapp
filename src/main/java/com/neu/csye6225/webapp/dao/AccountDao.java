package com.neu.csye6225.webapp.dao;


import com.neu.csye6225.webapp.entity.db.Account;
import com.neu.csye6225.webapp.util.Utils;
import jdk.jshell.execution.Util;
import org.apache.catalina.User;
import org.hibernate.PersistentObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Base64;

@Repository
public class AccountDao {
    @Autowired
    SessionFactory sessionFactory;

    public Account getAccountByToken(String token) {
        if (token.isBlank()) return null;
        String res[] = new String(Base64.getDecoder().decode(token.split(" ")[1])).split(":");
        if (res.length < 2) return null;
        System.out.println("[INFO] Get token " + res[0] + " " + res[1]);

        Account account = getAccountByEmail(res[0]);
        if (account == null) return null;
        if (Utils.matchesPassword(res[1], account.getPassword())) {
            return account;
        }
        return null;
    }

    public Account getAccountByEmail(String email) {
        Session session = null;
        session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from Account where email = :email");
        query.setParameter("email", email);
        Account account = (Account) query.uniqueResult();
        transaction.commit();
        System.out.println(account);
        if (session != null) session.close();
        return account;
    }

    public boolean save(Account account) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(account);
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

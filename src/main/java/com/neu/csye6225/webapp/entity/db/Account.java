package com.neu.csye6225.webapp.entity.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "account_id")
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @JsonIgnore
    private String password;

    @Column(unique = true)
    private String email;

    @Column(name = "account_created")
    private Date accountCreated;

    @Column(name = "account_updated")
    private Date accountUpdated;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    @JsonIgnore
    Set<Assignment> assignments = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getAccountCreated() {
        return accountCreated;
    }

    public void setAccountCreated(Date accountCreated) {
        this.accountCreated = accountCreated;
    }

    public Date getAccountUpdated() {
        return accountUpdated;
    }

    public void setAccountUpdated(Date accountUpdated) {
        this.accountUpdated = accountUpdated;
    }

    public Set<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(HashSet<Assignment> assignments) {
        this.assignments = assignments;
    }
}

package com.neu.csye6225.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class HealthService {
    private final DataSource dataSource;

    @Autowired
    public HealthService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

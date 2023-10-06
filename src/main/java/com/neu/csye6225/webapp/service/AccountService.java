package com.neu.csye6225.webapp.service;

import com.neu.csye6225.webapp.dao.AccountDao;
import com.neu.csye6225.webapp.entity.db.Account;
import com.neu.csye6225.webapp.util.Utils;
import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

@Service
public class AccountService {
    @Autowired
    private AccountDao accountDao;

    @PostConstruct
    public void loadAccountCsv() throws IOException {
        System.out.println("[INFO] Start loading csv");
        File file = new File("/opt/users.csv");
        if (!file.exists()) return;
        try (FileReader fileReader = new FileReader("/opt/users.csv");
             CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader)) {
            for (CSVRecord csvRecord : csvParser) {
                String email = csvRecord.get("email");
                Account account = accountDao.getAccountByEmail(email);
                if (account != null) continue;
                String firstName = csvRecord.get("first_name");
                String lastName = csvRecord.get("last_name");
                String password = csvRecord.get("password");

                password = Utils.encryptPassword(password);

                account = new Account();
                account.setFirstName(firstName);
                account.setLastName(lastName);
                account.setEmail(email);
                account.setPassword(password);
                Date date = new Date();
                account.setAccountCreated(date);
                account.setAccountUpdated(date);

                if (!accountDao.save(account)) {
                    System.out.println("[LOAD CSV] Conflict line");
                }
            }
            System.out.println("[INFO] Finish loading");
        }

    }

}

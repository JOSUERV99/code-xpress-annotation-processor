package com.javatpoint;

import dev.tools.annotationprocessor.db.tablecreator.TableCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommandStartupLineRunner implements CommandLineRunner {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CommandStartupLineRunner(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void run(String...args) {
        final TableCreator tableCreator = new TableCreator(namedParameterJdbcTemplate);
        tableCreator.createTableIfNotExists(Message.class);
    }
}
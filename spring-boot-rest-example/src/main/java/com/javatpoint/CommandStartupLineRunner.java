package com.javatpoint;

import dabba.doo.annotationprocessor.db.tablecreator.TableCreator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandStartupLineRunner implements CommandLineRunner {

    @Override
    public void run(String...args) {
        TableCreator.createTableIfNotExists(Message.class);
    }
}
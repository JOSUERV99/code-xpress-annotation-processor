package com.javatpoint;

import dev.tools.annotationprocessor.core.annotations.CXSpringRestCrudApi;
import dev.tools.annotationprocessor.core.annotations.entity.CXColumn;
import dev.tools.annotationprocessor.core.annotations.entity.CXEntity;
import dev.tools.annotationprocessor.core.annotations.entity.CXId;
import dev.tools.annotationprocessor.core.annotations.entity.types.column.BigInt;
import dev.tools.annotationprocessor.core.annotations.entity.types.column.Int;
import dev.tools.annotationprocessor.core.annotations.entity.types.column.Varchar;
import lombok.Data;

@Data
@CXSpringRestCrudApi(path = "/message")
@CXEntity(tableName = "message_demo_01")
public class Message {

    @CXId(generated = true)
    @CXColumn(name = "ID")
    @BigInt()
    private Long id;

    @CXColumn(name = "CONTENT")
    @Varchar()
    private String content;

    @CXColumn(name = "FROM_CONTACT_NAME")
    @Varchar(length = 200)
    private String from;

    @CXColumn(name = "TO_CONTACT_NAME")
    @Varchar(length = 199)
    private String to;

    @CXColumn(name = "READERS_COUNTER")
    @Int
    private Integer readBy;
}
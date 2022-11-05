package com.javatpoint;

import dabba.doo.annotationprocessor.core.annotations.J2dSpringRestCrudApi;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dEntity;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dId;
import dabba.doo.annotationprocessor.core.annotations.entity.types.BigIntColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.types.IntColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.types.VarcharColumn;
import lombok.Data;

@Data
@J2dSpringRestCrudApi(path = "/message")
@J2dEntity(tableName = "message")
public class Message {

    @J2dId(generated = true)
    @J2dColumn(name = "ID")
    @BigIntColumn()
    private Long id;

    @J2dColumn(name = "CONTENT")
    @VarcharColumn()
    private String content;

    @J2dColumn(name = "FROM_CONTACT_NAME")
    @VarcharColumn(length = 200)
    private String from;

    @J2dColumn(name = "TO_CONTACT_NAME")
    @VarcharColumn(length = 199)
    private String to;

    @J2dColumn(name = "READERS_COUNTER")
    @IntColumn
    private Integer readBy;
}

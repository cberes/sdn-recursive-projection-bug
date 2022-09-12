package com.example.recursiveprojection;

import lombok.Getter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

@Getter
public abstract class BaseEntity<T extends BaseEntity<T>> {

    @Id
    @GeneratedValue(generatorClass = SomeStringGenerator.class)
    private String Id;
}

package com.example.recursiveprojection;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Node
public class Person extends BaseEntity<Person> {
    private String name;

    private List<Person> children = new LinkedList<>();
}

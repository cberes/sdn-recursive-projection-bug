package com.example.recursiveprojection;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jTemplate;

import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest
class RecursiveProjectionTest {
    private final PersonRepository personRepository;
    private final Neo4jTemplate template;

    @Autowired
    RecursiveProjectionTest(PersonRepository personRepository, Neo4jTemplate template) {
        this.personRepository = personRepository;
        this.template = template;
    }

    @BeforeEach
    void setup() {
        personRepository.deleteAll();
    }

    private Stream<SaveMethod> saves() {
        return Stream.of(
                new SaveMethod("repository save", personRepository::save),
                new SaveMethod("using ordinary projection", person -> template.saveAs(person, PersonWithChildren.class)),
                new SaveMethod("using recursive projection", person -> template.saveAs(person, PersonWithChildrenRecursive.class))
        );
    }

    @TestFactory
    Stream<DynamicTest> saveOne() {
        return saves().map(save -> dynamicTest("saveOne: " + save.getDescription(), () -> {
            Person alice = new Person();
            alice.setName("Alice");
            save.getFunc().accept(alice);

            Person found = personRepository.findById(alice.getId()).get();
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(found.getName()).as("name").isEqualTo("Alice");
            softly.assertThat(found.getChildren()).as("children").isEmpty();
            softly.assertAll();
        }));
    }

    @TestFactory
    Stream<DynamicTest> saveOneWithChild() {
        return saves().map(save -> dynamicTest("saveOneWithChild: " + save.getDescription(), () -> {
            Person alice = new Person();
            alice.setName("Alice");
            Person bob = new Person();
            bob.setName("Bob");
            alice.getChildren().add(bob);
            save.getFunc().accept(alice);

            Person found = personRepository.findById(alice.getId()).get();
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(found.getName()).as("name").isEqualTo("Alice");
            softly.assertThat(found.getChildren()).as("children").hasSize(1);
            softly.assertThat(found.getChildren().get(0).getName()).as("child name").isEqualTo("Bob");
            softly.assertThat(found.getChildren().get(0).getChildren()).as("child children").isEmpty();
            softly.assertAll();
        }));
    }
}

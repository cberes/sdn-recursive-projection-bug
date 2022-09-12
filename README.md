# sdn-recursive-projection-bug

[SDN](https://github.com/spring-projects/spring-data-neo4j) is unable to save nodes using a projection that
recursively references itself.

For example, say you have a node entity that recursively references itself

    @Getter
    @Node
    public abstract class Person {
      private String name;

      private List<Person> children = new LinkedList<>();
    }

And a corresponding projection that references itself as well

    public interface PersonWithChildrenRecursive {
      String getName();

      List<PersonWithChildrenRecursive> getChildren();
    }

You cannot save a person using this projection

    Person alice = new Person();
    alice.setName("Alice");
    neo4jTemplate.saveAs(alice, PersonWithChildrenRecursive.class); // throws StackOverflowError

Note that other ways to save will succeed, such as `CrudRepository.save` or `Neo4jTemplate.saveAs`
with a projection that is _not_ recursive.

I don't know if this a new bug or a regression. I noticed it first while using 6.3.3-SNAPSHOT
but the bug is present in 6.3.2 as well.

## Requirements

Don't forget to set database credentials in [application.properties](src/main/resources/application.properties).

## Tests

Run [tests](src/test/java/com/example/recursiveprojection/RecursiveProjectionTest.java) via `mvn clean install` or via an IDE.


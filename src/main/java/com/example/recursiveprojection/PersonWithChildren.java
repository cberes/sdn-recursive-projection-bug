package com.example.recursiveprojection;

import java.util.List;

public interface PersonWithChildren {
    String getName();

    List<PersonPropertiesOnly> getChildren();
}

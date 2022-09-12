package com.example.recursiveprojection;

import java.util.List;

public interface PersonWithChildrenRecursive {
    String getName();

    List<PersonWithChildrenRecursive> getChildren();
}

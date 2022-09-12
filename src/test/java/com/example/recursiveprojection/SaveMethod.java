package com.example.recursiveprojection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
class SaveMethod {
    private final String description;
    private final Consumer<Person> func;
}

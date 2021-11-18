package ru.geekbrains.javaback.enums;

import lombok.Getter;

public enum CategoryType {
    FOOD("Food", 1),
    ELECLRONIC("Electronic", 2),
    FURNITURE("Furniture", 3),
    CLOTHES("Furniture", 4);
    @Getter
    private final String title;
    @Getter
    private final Integer id;

    CategoryType(String title, Integer id) {
        this.title = title;
        this.id = id;
    }
}

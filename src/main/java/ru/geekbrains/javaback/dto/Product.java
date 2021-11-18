package ru.geekbrains.javaback.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
@ToString
public class Product {
    Integer id;
    String title;
    Integer price;
    String categoryTitle;

}

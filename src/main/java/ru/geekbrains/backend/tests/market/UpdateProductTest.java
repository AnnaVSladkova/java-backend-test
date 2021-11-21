package ru.geekbrains.backend.tests.market;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.javaback.db.dao.ProductsMapper;
import ru.geekbrains.javaback.dto.Product;
import ru.geekbrains.javaback.enums.CategoryType;
import ru.geekbrains.javaback.utils.DbUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class UpdateProductTest {
    static ProductsMapper productsMapper;
    Faker faker = new Faker();
    Product product;

    @BeforeAll
    static void beforeAll() {
        productsMapper = DbUtils.getProductsMapper();
    }
    @SneakyThrows
    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice(100)
                .withCategoryTitle(CategoryType.FOOD.getTitle())
                .withCategoryId(CategoryType.FOOD.getId());
        product = DbUtils.createProducts(productsMapper, product);
    }


    @SneakyThrows
    @Test
    void putProductTest() throws IOException{
        product.setPrice(200);
        DbUtils.updateProduct(productsMapper, product);
        Product _product = DbUtils.getProduct(productsMapper, product.getId());
        assertThat(_product.getPrice(), CoreMatchers.is(product.getPrice()));
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        DbUtils.deleteProduct(productsMapper, product.getId());
    }

}

package ru.geekbrains.backend.tests.market;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
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
import static org.hamcrest.Matchers.equalTo;

public class ProductTest {

    static ProductsMapper productsMapper;
    int productId;

    Faker faker = new Faker();
    Product product;

    @BeforeAll
    static void beforeAll() {
        productsMapper = DbUtils.getProductsMapper();
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) (Math.random() + 1) * 100)
                .withCategoryId(CategoryType.FOOD.getId())
                .withCategoryTitle(CategoryType.FOOD.getTitle());
    }

    @Test
    void postProductTest() throws IOException {
        Integer countProductsBefore = DbUtils.countProducts(productsMapper);
        product = DbUtils.createProducts(productsMapper, product);
        Integer countProductsAfter = DbUtils.countProducts(productsMapper);
        assertThat(countProductsAfter, equalTo(countProductsBefore+1));
        productId = product.getId();
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        DbUtils.deleteProduct(productsMapper, productId);
    }

}

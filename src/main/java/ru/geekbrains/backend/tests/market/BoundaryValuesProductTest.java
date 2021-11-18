package ru.geekbrains.backend.tests.market;

import com.github.javafaker.Faker;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.geekbrains.javaback.dto.Product;
import ru.geekbrains.javaback.enums.CategoryType;
import ru.geekbrains.javaback.service.CategoryService;
import ru.geekbrains.javaback.service.ProductService;
import ru.geekbrains.javaback.utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class BoundaryValuesProductTest {

    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;
    Faker faker = new Faker();
    Product product;

    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().fruit())
                .withPrice(573)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
//                .withCategoryTitle(CategoryType.FURNITURE.getTitle());
    }

    @Test
    void getProductTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.code(), Matchers.is(201));
        String computeCategory = "";
        if (response.body().getPrice() >= 1 && response.body().getPrice() <= 1000) {
            computeCategory = CategoryType.FOOD.getTitle();
        } else if (response.body().getPrice() >= 1001 && response.body().getPrice() <= 3000) {
            computeCategory = CategoryType.FURNITURE.getTitle();
        } else if (response.body().getPrice() >= 3001) {
            computeCategory = CategoryType.ELECLRONIC.getTitle();
        }
        assertThat(response.body().getCategoryTitle(), Matchers.equalTo(computeCategory));
        System.out.println(response.body());


    }
}
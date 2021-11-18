package ru.geekbrains.backend.tests.market;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
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
import static org.hamcrest.Matchers.equalTo;

public class NegativeProductTest {

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


    @Test
    void postProductTest() throws IOException {
        product = new Product();
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.code(), CoreMatchers.is(500));

    }


    @SneakyThrows
    @Test
    void setUp() {
        product = new Product()
                .withTitle(faker.food().spice())
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.body().getPrice(), CoreMatchers.is(0));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));

    }
}


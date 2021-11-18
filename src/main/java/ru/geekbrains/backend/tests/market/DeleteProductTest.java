package ru.geekbrains.backend.tests.market;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
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

public class DeleteProductTest {

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

    @SneakyThrows
    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().spice())
                .withPrice((int) (Math.random() + 1) * 100)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        product = productService.createProduct(product).execute().body();
    }

    @SneakyThrows
    @Test
    void deleteDeleteProductTest() throws IOException {

        Response<ResponseBody> response = productService.deleteProduct(product.getId()).execute();
        assertThat(response.code(), CoreMatchers.is(200));
        assertThat(productService.getProduct(product.getId()).execute().code(), CoreMatchers.is(404));


    }

}


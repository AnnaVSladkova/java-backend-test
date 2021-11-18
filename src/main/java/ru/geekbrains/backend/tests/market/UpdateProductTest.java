package ru.geekbrains.backend.tests.market;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
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

public class UpdateProductTest {
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
                .withTitle(faker.food().dish())
                .withPrice(100)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        product = productService.createProduct(product).execute().body();
    }



    @Test
    void putProductTest() throws IOException{
        product.setPrice(200);
        Response<ResponseBody> result = productService.updatePost(product).execute();
        assertThat(result.code(), CoreMatchers.not(404));
        assertThat(result.code(), CoreMatchers.is(200));

        Response<Product> execute = productService.getProduct(product.getId()).execute();
        assertThat(execute.code(), CoreMatchers.not(404));
        assertThat(execute.code(), CoreMatchers.is(200));
        assertThat(execute.body().getPrice(),CoreMatchers.is(200));
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        Response<ResponseBody> response = productService.deleteProduct(product.getId()).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

}

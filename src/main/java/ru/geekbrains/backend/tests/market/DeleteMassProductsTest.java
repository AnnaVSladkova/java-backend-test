package ru.geekbrains.backend.tests.market;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;

public class DeleteMassProductsTest {

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
        for (int i = 0; i < 10; i++) {
            Product _product = new Product()
                    .withTitle(faker.food().spice())
                    .withPrice((int) (969696))
                    .withCategoryTitle(CategoryType.FOOD.getTitle());
            Response<Product> _result = productService.createProduct(_product).execute();
        }
    }

    @SneakyThrows
    @Test
    void deleteProductOnePrice() {
        Response<ArrayList<Product>> products = productService.getProducts().execute();
        List<Integer> ids = products.body().stream().filter(a -> a.getPrice() == 969696).map(Product::getId).collect(Collectors.toList());
        for (Integer id : ids)
            productService.deleteProduct(id).execute();
        Response<ArrayList<Product>> products2 = productService.getProducts().execute();
        assertThat(products2.body().stream().map(Product::getId).anyMatch(ids::contains), CoreMatchers.is(false));
    }
}
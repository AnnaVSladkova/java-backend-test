package ru.geekbrains.javaback.service;

import retrofit2.Call;
import retrofit2.http.*;
import ru.geekbrains.javaback.dto.Product;
import okhttp3.ResponseBody;

import java.util.ArrayList;

public interface ProductService {

    @GET("products")
    Call<ArrayList<Product>> getProducts();

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") Integer id);

    @POST("products")
    Call<Product> createProduct(@Body Product product);

    @PUT("products")
    Call<ResponseBody>updatePost(@Body Product product);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);
}
